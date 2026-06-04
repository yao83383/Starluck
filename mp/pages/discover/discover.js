const slPage = require('../../utils/sl-page.js')
const API = require('../../utils/api.js')

const TABS = [
  { key:'recommend', label:'推荐' },
  { key:'nearby',    label:'同片星域' },
  { key:'new',       label:'新人' },
  { key:'online',    label:'闪耀中' },
  { key:'feed',      label:'动态' }
]

slPage({
  data: {
    tabs: TABS,
    tabKey: 'recommend',
    users: [],
    refreshing: false,
    statusBarHeight: 20
  },

  onLoad() {
    const app = getApp()
    this.setData({ statusBarHeight: app.globalData.statusBarHeight || 20 })
    this.loadUsers()
  },

  onShow() {
    if (this.data.users.length === 0) this.loadUsers()
  },

  // 下拉刷新
  onRefresh() {
    if (this.data.refreshing) return
    this.setData({ refreshing: true })
    this.loadUsers(() => this.setData({ refreshing: false }))
  },

  loadUsers(done) {
    API.user.discover().then(list => {
      const arr = Array.isArray(list) ? list : []
      const users = arr.map(u => ({
        id: (u.isFake ? 'f' : 'u') + u.userId,
        rawId: u.userId,
        isFake: !!u.isFake,
        name: u.nickname || '神秘星人',
        av: u.avatarNo || 1,
        age: u.age || 20,
        city: u.city || '未设置',
        dist: u.dist || '',
        gender: u.gender || '',
        tags: Array.isArray(u.tags) ? u.tags : [],
        sign: u.sign || '',
        online: u.online !== false,
        vip: u.isVip || false
      }))
      this.setData({ users })
      try { getApp().lastDiscoverList = users } catch (e) {}
      if (done) done()
    }).catch(() => {
      if (done) done()
    })
  },

  switchTab(e) {
    this.setData({ tabKey: e.currentTarget.dataset.key })
  },

  goUser(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: '/pages/profile/profile?id=' + id })
  }
})
