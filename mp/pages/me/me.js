const slPage = require('../../utils/sl-page.js')
const store = require('../../utils/store.js')

slPage({
  data: {
    statusBarHeight: 20
  },

  onLoad() {
    const app = getApp()
    this.setData({ statusBarHeight: app.globalData.statusBarHeight || 20 })
  },

  goMenu(e) {
    const url = e.currentTarget.dataset.url
    if (!url) return
    if (url.indexOf('/pages/') === 0) {
      wx.navigateTo({ url })
    }
  },

  doLogout() {
    wx.showModal({
      title: '暂时熄灭你的星光？',
      content: '退出后需重新登录',
      confirmColor: '#ff5c8a',
      success: (res) => {
        if (res.confirm) {
          store.update({ loggedIn: false })
          wx.reLaunch({ url: '/pages/login/login' })
        }
      }
    })
  },

  openVip() { this.go({ currentTarget:{dataset:{url:'/pages/vip/vip'}} }) },
  openWallet() { this.go({ currentTarget:{dataset:{url:'/pages/wallet/wallet'}} }) },
  openRecharge() { this.go({ currentTarget:{dataset:{url:'/pages/recharge/recharge'}} }) },
  openSettings() { this.go({ currentTarget:{dataset:{url:'/pages/settings/settings'}} }) },
  openTheme() { this.go({ currentTarget:{dataset:{url:'/pages/theme/theme'}} }) },
  openOnboard() { this.go({ currentTarget:{dataset:{url:'/pages/onboard/onboard'}} }) }
})
