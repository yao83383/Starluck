const slPage = require('../../utils/sl-page.js')
const store = require('../../utils/store.js')
const { listThemes, getTheme } = require('../../utils/theme.js')

slPage({
  data: {
    themes: [],
    statusBarHeight: 20
  },

  onLoad() {
    const app = getApp()
    const all = listThemes()
    this.setData({
      themes: all,
      statusBarHeight: app.globalData.statusBarHeight || 20
    })
  },

  selectTheme(e) {
    const id = e.currentTarget.dataset.id
    const cur = store.get().themeId
    if (id === cur) {
      this.toast('已是当前主题')
      return
    }
    store.setTheme(id)
    const t = getTheme(id)
    wx.showToast({
      title: '已切换至「' + t.name + '」',
      icon: 'none',
      duration: 1800
    })
    // 短延迟以确保 setData 完成后视觉过渡更柔和
    if (wx.vibrateShort) {
      try { wx.vibrateShort({ type: 'light' }) } catch (e) {}
    }
  }
})
