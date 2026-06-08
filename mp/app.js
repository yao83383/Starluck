/**
 * ✨ Starluck 小程序入口
 */
const store = require('utils/store.js')
const API = require('utils/api.js')

App({
  onLaunch() {
    store.init()

    try {
      const info = wx.getWindowInfo ? wx.getWindowInfo() : wx.getSystemInfoSync()
      this.globalData.statusBarHeight = info.statusBarHeight || 20
      this.globalData.windowWidth = info.windowWidth || 375
      this.globalData.windowHeight = info.windowHeight || 812
    } catch (e) {
      this.globalData.statusBarHeight = 20
    }

    // 网络连通性测试
    wx.request({
      url: 'http://8.140.249.127:8080/api/auth/send-code',
      method: 'POST',
      header: { 'Content-Type': 'application/json' },
      data: { phone: '10000000000' },
      success(res) { console.log('[网络测试] 成功 status=' + res.statusCode, res.data) },
      fail(err) { console.log('[网络测试] 失败', err.errMsg) }
    })

    if (API.getToken()) {
      setTimeout(() => API.ws.connect(), 500)
    }
  },

  onShow() {
    // 从后台返回时重连 WebSocket（如果断了）
    if (API.getToken() && !API.ws.connected) {
      API.ws.connect()
    }
  },

  globalData: {
    statusBarHeight: 20,
    windowWidth: 375,
    windowHeight: 812
  },

  store,
  API
})
