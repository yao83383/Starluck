const slPage = require('../../utils/sl-page.js')
const store = require('../../utils/store.js')
const API = require('../../utils/api.js')

slPage({
  data: {
    sessionId: '',
    peerId: 0,
    isFake: false,
    peer: { name:'...', av:1, vip:false, online:true },
    text: '',
    msgs: [],
    statusBarHeight: 20,
    scrollIntoView: ''
  },

  onLoad(q) {
    const app = getApp()
    const rawQ = q && q.id ? q.id : ''
    const isFake = /^f/i.test(rawQ)
    const peerId = parseInt(rawQ.replace(/[^0-9]/g, '')) || 0
    const qName = q.name ? decodeURIComponent(q.name) : ''
    const qAv = parseInt(q.av) || 1

    this.setData({
      peerId,
      isFake,
      statusBarHeight: app.globalData.statusBarHeight || 20
    })

    if (qName) {
      this.setData({ peer: { name: qName, av: qAv, vip: false, online: true } })
    }

    if (peerId > 0) {
      if (isFake) {
        const list = (getApp().lastDiscoverList || [])
        const found = list.find(u => u.isFake && u.rawId === peerId)
        if (found) {
          this.setData({
            peer: { name: found.name, av: found.av, vip: found.vip, online: true }
          })
        }
      } else {
        API.user.getOther(peerId).then(profile => {
          this.setData({
            peer: {
              name: profile.nickname || qName || '神秘星人',
              av: profile.avatarNo || qAv,
              vip: profile.isVip || false,
              online: true
            }
          })
        }).catch(() => {})
      }

      // 2. 创建/获取会话 → 加载历史
      API.chat.createSession(peerId, isFake).then(session => {
        const sid = session.sessionId
        if (!sid) { this.toast('会话创建失败'); return }
        this.setData({ sessionId: sid })
        return API.chat.getMessages(sid)
      }).then(msgs => {
        if (msgs && msgs.length > 0) {
          this.setData({
            msgs: msgs.map(m => ({
              type: m.senderId === this.data.peerId ? 'in' : 'out',
              text: m.content,
              cost: m.cost || 0,
              isRead: m.isRead === 1
            })),
            scrollIntoView: 'msg-end'
          })
        }
      }).catch(err => {
        this.toast('加载消息失败：' + (err.message || ''))
      })
    }

    // 3. WebSocket 实时推送监听
    this._wsHandler = (data) => this.onWsMessage(data)
    API.ws.on(this._wsHandler)
    if (!API.ws.connected) API.ws.connect()

    this._pollTimer = setInterval(() => this._pollMessages(), 3000)
  },

  onUnload() {
    if (this._wsHandler) API.ws.off(this._wsHandler)
    if (this._pollTimer) { clearInterval(this._pollTimer); this._pollTimer = null }
  },

  _pollMessages() {
    if (!this.data.sessionId) return
    API.chat.getMessages(this.data.sessionId).then(raw => {
      if (!raw) return
      const msgs = raw.map(m => ({
        type: m.senderId === this.data.peerId ? 'in' : (m.senderRole === 'system' ? 'sys' : 'out'),
        text: m.content,
        cost: m.costDiamond || 0,
        isRead: m.isRead === 1
      }))
      this.setData({ msgs, scrollIntoView: 'msg-end' })
    }).catch(() => {})
  },

  onWsMessage(data) {
    if (!data) return
    if (String(data.sessionId) !== String(this.data.sessionId)) return

    if (data.type === 'messages_read') {
      const msgs = this.data.msgs.map(m =>
        m.type === 'out' ? { ...m, isRead: true } : m
      )
      this.setData({ msgs })
      return
    }

    if (data.type === 'new_message') {
      const meId = store.get().user && store.get().user.dbId
      if (meId && String(data.senderId) === String(meId)) return
      const msgs = this.data.msgs.slice()
      msgs.push({ type:'in', text: data.content, cost: 0, isRead: true })
      this.setData({ msgs, scrollIntoView: 'msg-end' })
    }
  },

  onInput(e) { this.setData({ text: e.detail.value }) },

  send() {
    const t = this.data.text.trim()
    if (!t) return
    if (!this.data.sessionId) {
      this.toast('会话未建立')
      return
    }

    const s = store.get()
    let cost = 30
    if (s.user.vip && s.chatFreeRemain > 0) {
      cost = 0
      store.update({ chatFreeRemain: s.chatFreeRemain - 1 })
    } else if (s.diamonds < 30) {
      const msgs = this.data.msgs.slice()
      msgs.push({ type:'failed', text:t, cost:0, isRead:false })
      this.setData({ msgs, text:'', scrollIntoView: 'msg-end' })
      this.toast('星光不足，请充值后再发送')
      return
    } else {
      store.update({ diamonds: s.diamonds - 30 })
    }

    const msgs = this.data.msgs.slice()
    msgs.push({ type:'out', text:t, cost, isRead: false })
    this.setData({ msgs, text:'', scrollIntoView: 'msg-end' })

    API.ws.send({ type: 'chat_send', sessionId: this.data.sessionId, content: t }) ||
    API.chat.send(this.data.sessionId, t).catch(err => {
      const idx = msgs.findIndex(m => m.text === t && m.type === 'out')
      if (idx > -1) msgs.splice(idx, 1)
      this.setData({ msgs })
      this.toast(err.message || '发送失败')
      if (cost > 0) store.update({ diamonds: s.diamonds + cost })
    })
  },

  goRecharge() { this.go('/pages/recharge/recharge') },
  openGift() { this.toast('待实现：星愿礼物面板') },

  retryMsg(e) {
    const idx = e.currentTarget.dataset.index
    const msg = this.data.msgs[idx]
    if (!msg) return
    const s = store.get()
    if (s.diamonds < 30) {
      this.toast('星光仍不足，请充值')
      return
    }
    store.update({ diamonds: s.diamonds - 30 })
    const msgs = this.data.msgs.slice()
    msgs.splice(idx, 1)
    msgs.push({ type:'out', text: msg.text, cost: 30, isRead: false })
    this.setData({ msgs, scrollIntoView: 'msg-end' })
    API.chat.send(this.data.sessionId, msg.text).catch(err => {
      this.toast(err.message || '发送失败')
    })
  }
})
