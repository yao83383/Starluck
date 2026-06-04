/**
 * ✨ Starluck · Page / Component 主题助手
 * --------------------------------------------------------
 * 把 wx Page() 包一层，自动注入：
 *   - data.theme        当前主题对象
 *   - data.themeStyle   注入到根 view 的 style 字符串（CSS 变量）
 *   - data.store        全局状态引用
 *
 * 使用：
 *   const slPage = require('utils/sl-page.js')
 *   slPage({
 *     data: { foo: 1 },
 *     onLoad() { ... },
 *   })
 *
 * 在 wxml 里：
 *   <view class="page" style="{{themeStyle}}">  ...  </view>
 */

const store = require('./store.js')
const { getTheme, themeToStyle } = require('./theme.js')

function applyTheme(self) {
  const s = store.get()
  const theme = getTheme(s.themeId)
  const themeStyle = themeToStyle(theme)
  // 创建浅拷贝以触发小程序响应式更新
  const storeSnapshot = Object.assign({}, s, { user: Object.assign({}, s.user), settings: Object.assign({}, s.settings) })
  // 仅当变化时才 setData，避免无意义渲染
  if (self.data._themeId !== theme.id) {
    self.setData({
      theme,
      themeStyle,
      _themeId: theme.id,
      store: storeSnapshot
    })
  } else {
    self.setData({ store: storeSnapshot })
  }
}

function slPage(opt) {
  const _onLoad = opt.onLoad
  const _onShow = opt.onShow
  const _onUnload = opt.onUnload

  opt.data = Object.assign({
    theme: getTheme(store.get().themeId || 'warm-night'),
    themeStyle: '',
    store: store.get(),
    _themeId: ''
  }, opt.data || {})

  opt.onLoad = function (q) {
    store.init()
    applyTheme(this)
    this._unsubscribe = store.subscribe(() => applyTheme(this))
    if (_onLoad) _onLoad.call(this, q)
  }

  opt.onShow = function () {
    applyTheme(this)
    if (_onShow) _onShow.call(this)
  }

  opt.onUnload = function () {
    if (this._unsubscribe) this._unsubscribe()
    if (_onUnload) _onUnload.call(this)
  }

  /* 通用工具方法注入 */
  opt.go = function (url) {
    wx.navigateTo({ url })
  }

  opt.switchTo = function (url) {
    wx.reLaunch({ url })
  }

  if (!opt.back) opt.back = function () {
    wx.navigateBack({ delta: 1 })
  }

  if (!opt.toast) opt.toast = function (title, icon) {
    wx.showToast({
      title: title || '',
      icon: icon || 'none',
      duration: 1800
    })
  }

  opt.go = function (url) {
    wx.navigateTo({ url })
  }

  opt.switchTo = function (url) {
    wx.reLaunch({ url })
  }

  opt.back = function () {
    wx.navigateBack({ delta: 1 })
  }

  Page(opt)
}

module.exports = slPage
