const slPage = require('../../utils/sl-page.js')
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
  },

  loadSessions() {
    API.chat.getSessions().then(list => {
      if (list && list.length > 0) {
        const chats = list.map(s => ({
          id: s.sessionId || 'c' + s.peerUserId,
          uid: 'u' + s.peerUserId,
          name: s.peerNickname || '神秘星人',
          av: s.peerAvatarNo || 1,
          vip: s.peerVip || false,
          online: true,
          last: s.lastMessage || '暂无消息',
          time: s.lastTime || '',
          unread: s.unreadCount || 0
        }))
        this.setData({ chats })
      }
    }).catch(() => {})
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
    const id = e.currentTarget.dataset.id
    this.go('/pages/chat/chat?id=' + id)
  }
})
