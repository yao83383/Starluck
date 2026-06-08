const slPage = require('../../utils/sl-page.js')
const store = require('../../utils/store.js')
const API = require('../../utils/api.js')

const QUICK = [
  { key:'liked',   icon:'💝', label:'喜欢我', color:'c1', badge:0, vip:true },
  { key:'visit',   icon:'👣', label:'访客',   color:'c2', badge:0, vip:true },
  { key:'gift',    icon:'🎁', label:'星愿',   color:'c3', badge:0 },
  { key:'notice',  icon:'🔔', label:'通知',   color:'c4', badge:0 }
]

slPage({
  data: {
    quick: QUICK,
    chats: [],
    statusBarHeight: 20
  },

  onLoad() {
    const app = getApp()
    this.setData({ statusBarHeight: app.globalData.statusBarHeight || 20 })
  },

  onShow() {
    this.loadSessions()
    this._pollTimer = setInterval(() => this.loadSessions(), 5000)
  },

  onHide() {
    if (this._pollTimer) { clearInterval(this._pollTimer); this._pollTimer = null }
  },

  loadSessions() {
    API.chat.getSessions().then(list => {
      console.log('[messages] getSessions:', JSON.stringify(list && list.length))
      if (list && list.length > 0) {
        const pinned = store.get().pinnedSessions || []
        const chats = list.map(s => ({
          id: s.sessionId || 'c' + s.peerUserId,
          uid: (s.isFake ? 'f' : 'u') + s.peerUserId,
          name: s.peerName || '神秘星人',
          av: s.peerAvatarNo || 1,
          vip: s.peerVip || false,
          online: s.peerOnline || false,
          last: s.lastMsg || '暂无消息',
          time: s.lastTime || '',
          unread: s.unread || 0,
          pinned: pinned.indexOf(s.sessionId) >= 0
        }))
        chats.sort((a, b) => {
          if (a.pinned !== b.pinned) return a.pinned ? -1 : 1
          if ((a.unread > 0) !== (b.unread > 0)) return a.unread > 0 ? -1 : 1
          return 0
        })
        this.setData({ chats })
        const total = chats.reduce((sum, c) => sum + (c.unread || 0), 0)
        store.update({ totalUnread: total })
      }
    }).catch(err => {
      console.error('[messages] getSessions failed:', err.message || err)
    })
  },

  onQuick(e) {
    const key = e.currentTarget.dataset.key
    const item = QUICK.find(q => q.key === key)
    if (item && item.vip && !this.data.store.user.vip) {
      this.toast('开通星耀会员可查看完整列表')
      setTimeout(() => this.go('/pages/vip/vip'), 800)
      return
    }
    this.toast('待实现：' + item.label)
  },

  goChat(e) {
    const ds = e.currentTarget.dataset
    const idx = this.data.chats.findIndex(c => c.uid === ds.uid)
    if (idx >= 0 && this.data.chats[idx].unread > 0) {
      const s = store.get()
      const newTotal = Math.max(0, (s.totalUnread || 0) - this.data.chats[idx].unread)
      store.update({ totalUnread: newTotal })
      const chats = this.data.chats.slice()
      chats[idx].unread = 0
      this.setData({ chats })
    }
    const url = '/pages/chat/chat?id=' + ds.uid + '&name=' + encodeURIComponent(ds.name || '') + '&av=' + (ds.av || 1)
    this.go(url)
  },

  onLongPress(e) {
    const ds = e.currentTarget.dataset
    const chat = this.data.chats.find(c => c.uid === ds.uid)
    if (!chat) return
    const isPinned = chat.pinned
    wx.showActionSheet({
      itemList: [isPinned ? '取消置顶' : '置顶会话'],
      success: (res) => {
        if (res.tapIndex === 0) {
          const s = store.get()
          let pinned = s.pinnedSessions || []
          const sessionId = chat.id
          if (isPinned) {
            pinned = pinned.filter(id => id !== sessionId)
          } else {
            pinned = [sessionId, ...pinned]
          }
          store.update({ pinnedSessions: pinned })
          this.loadSessions()
        }
      }
    })
  }
})
