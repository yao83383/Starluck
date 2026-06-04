/**
 * ✨ Starluck 小程序入口
 */
const store = require('utils/store.js')
const API = require('utils/api.js')

App({
  onLaunch() {
    store.init()

    // 初始化系统信息
    try {
      const info = wx.getWindowInfo ? wx.getWindowInfo() : wx.getSystemInfoSync()
      this.globalData.statusBarHeight = info.statusBarHeight || 20
      this.globalData.windowWidth = info.windowWidth || 375
      this.globalData.windowHeight = info.windowHeight || 812
    } catch (e) {
      this.globalData.statusBarHeight = 20
    }

    // 已登录则恢复 WebSocket
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
