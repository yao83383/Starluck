const slPage = require('../../utils/sl-page.js')
const API = require('../../utils/api.js')

slPage({
  data: {
    oldPwd: '',
    newPwd: '',
    loading: false,
    statusBarHeight: 20
  },

  onLoad() {
    const app = getApp()
    this.setData({ statusBarHeight: app.globalData.statusBarHeight || 20 })
  },

  onOldPwd(e) { this.setData({ oldPwd: e.detail.value }) },
  onNewPwd(e) { this.setData({ newPwd: e.detail.value }) },

  doSetPassword() {
    const np = this.data.newPwd
    if (!np || np.length < 6) {
      wx.showToast({ title: '密码至少6位', icon: 'none' })
      return
    }
    this.setData({ loading: true })
    API.user.updatePassword(this.data.oldPwd, np).then(() => {
      wx.showToast({ title: '密码设置成功', icon: 'success' })
      setTimeout(() => wx.navigateBack(), 1000)
    }).catch(err => {
      this.setData({ loading: false })
      wx.showToast({ title: err.message || '设置失败', icon: 'none' })
    })
  }
})
