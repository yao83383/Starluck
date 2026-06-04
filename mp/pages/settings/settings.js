const slPage = require('../../utils/sl-page.js')
const store = require('../../utils/store.js')
const API = require('../../utils/api.js')
const { listThemes, getTheme } = require('../../utils/theme.js')

slPage({
  data: {
    statusBarHeight: 20,
    themes: [],
    phone: ''
  },

  onLoad() {
    const app = getApp()
    this.setData({
      statusBarHeight: app.globalData.statusBarHeight || 20,
      themes: listThemes()
    })
    API.auth.getUserInfo().then(info => {
      const phone = (info && info.phone) ? info.phone : ''
      const masked = phone.length >= 11
        ? phone.substring(0, 3) + '****' + phone.substring(7)
        : phone
      this.setData({ phone: masked })
    }).catch(() => {})
  },

  goPassword() {
    wx.navigateTo({ url: '/pages/password/password' })
  },

  toggle(e) {
    const key = e.currentTarget.dataset.key
    const cur = store.get().settings[key]
    store.update({ settings: { [key]: !cur } })
  },

  pickTheme(e) {
    const id = e.currentTarget.dataset.id
    const cur = store.get().themeId
    if (id === cur) return
    store.setTheme(id)
    const t = getTheme(id)
    this.toast('已切换至「' + t.name + '」主题')
  },

  goTheme() { this.go('/pages/theme/theme') },

  doLogout() {
    wx.showModal({
      title: '暂时熄灭你的星光？',
      content: '退出登录后需重新登录',
      confirmColor: '#ff5c8a',
      success: (res) => {
        if (res.confirm) {
          store.update({ loggedIn: false })
          wx.reLaunch({ url: '/pages/login/login' })
        }
      }
    })
  },

  doDelete() {
    wx.showModal({
      title: '永久离开星海？',
      content: '注销账号会永久删除你的所有数据，且无法恢复',
      confirmColor: '#ff5c8a',
      success: (res) => {
        if (res.confirm) {
          store.reset()
          wx.reLaunch({ url: '/pages/login/login' })
        }
      }
    })
  }
})
