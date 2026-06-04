/**
 * ✨ Starluck · 小程序 API 客户端
 * --------------------------------------------------------
 * 替代 H5 的 api.js，使用 wx.request 对接后端
 *
 * 使用：
 *   const API = require('../../utils/api.js')
 *   const data = await API.auth.login('13800000001', '123456')
 */

const BASE = 'http://8.140.249.127:8080'
const TOKEN_KEY = 'sl_token'

let _token = ''

function getToken() {
  if (_token) return _token
  try { _token = wx.getStorageSync(TOKEN_KEY) || '' } catch (e) {}
  return _token
}

function setToken(token) {
  _token = token || ''
  try {
    if (token) wx.setStorageSync(TOKEN_KEY, token)
    else wx.removeStorageSync(TOKEN_KEY)
  } catch (e) {}
}

/**
 * 统一请求
 * @param {string} method GET/POST/PUT/DELETE
 * @param {string} path 如 '/api/auth/login'
 * @param {object} [body]
 * @param {object} [query]
 */
function request(method, path, body, query) {
  return new Promise((resolve, reject) => {
    let url = BASE + path
    if (query) {
      const qs = Object.keys(query)
        .filter(k => query[k] !== undefined && query[k] !== null)
        .map(k => encodeURIComponent(k) + '=' + encodeURIComponent(query[k]))
        .join('&')
      if (qs) url += (url.indexOf('?') >= 0 ? '&' : '?') + qs
    }

    const header = { 'Content-Type': 'application/json' }
    const token = getToken()
    if (token) header['Authorization'] = 'Bearer ' + token

    wx.request({
      url,
      method,
      header,
      data: body,
      success(res) {
        const json = res.data
        if (res.statusCode >= 200 && res.statusCode < 300 && json && json.code === 200) {
          resolve(json.data || json)
        } else if (json && json.code === 401) {
          setToken('')
          reject(new Error(json.message || '登录已过期'))
        } else {
          reject(new Error((json && json.message) || '请求失败(' + res.statusCode + ')'))
        }
      },
      fail(err) {
        reject(new Error(err.errMsg || '网络请求失败'))
      }
    })
  })
}

/* ============ 认证 ============ */
const auth = {
  sendCode(phone) { return request('POST', '/api/auth/send-code', { phone }) },
  login(phone, code) {
    return request('POST', '/api/auth/login', { phone, code }).then(data => {
      if (data && data.token) setToken(data.token)
      return data
    })
  },
  loginByPassword(phone, password) {
    return request('POST', '/api/auth/login-by-password', { phone, password }).then(data => {
      if (data && data.token) setToken(data.token)
      return data
    })
  },
  getUserInfo() { return request('GET', '/api/auth/user-info') },
  logout() { setToken('') },
  isLoggedIn() { return !!getToken() }
}

/* ============ 用户 ============ */
const user = {
  getProfile() { return request('GET', '/api/user/profile') },
  updateProfile(profile) { return request('PUT', '/api/user/profile', profile) },
  getOther(userId) { return request('GET', '/api/user/profile/' + userId) },
  discover() { return request('GET', '/api/user/discover') },
  updatePassword(oldPassword, newPassword) { return request('PUT', '/api/user/password', { oldPassword, newPassword }) }
}

/* ============ 聊天 ============ */
const chat = {
  getSessions() { return request('GET', '/api/chat/sessions') },
  getMessages(sessionId) { return request('GET', '/api/chat/messages/' + sessionId) },
  send(sessionId, content) { return request('POST', '/api/chat/send', { sessionId, content }) },
  createSession(peerUserId, isFake) { return request('POST', '/api/chat/session/create', null, { peerUserId, isFake: !!isFake }) }
}

/* ============ 支付 / VIP ============ */
const payment = {
  getWallet() { return request('GET', '/api/payment/wallet') },
  recharge(pkgId, method) { return request('POST', '/api/payment/recharge', { packageId: pkgId, payMethod: method || 'wechat' }) },
  withdraw(amount, method) { return request('POST', '/api/payment/withdraw', { amount, method: method || 'wechat' }) }
}

const vip = {
  getPlans() { return request('GET', '/api/vip/plans') },
  subscribe(planType, method) { return request('POST', '/api/vip/subscribe', null, { planType, payMethod: method || 'wechat' }) }
}

/* ============ WebSocket 实时推送 ============ */
const ws = {
  socket: null,
  listeners: [],
  connected: false,
  reconnecting: false,

  /** 连接 WebSocket，需先登录 */
  connect() {
    const token = getToken()
    if (!token) return
    if (this.connected || this.socket) return

    const wsBase = BASE.replace(/^http/, 'ws')
    const url = wsBase + '/ws/chat?token=' + encodeURIComponent(token)
    const self = this

    try {
      this.socket = wx.connectSocket({
        url,
        success: () => {},
        fail: (err) => { console.warn('[ws] connect fail', err) }
      })

      wx.onSocketOpen(() => {
        self.connected = true
        self.reconnecting = false
        console.log('[ws] connected')
      })

      wx.onSocketMessage((res) => {
        let data = res.data
        try { data = JSON.parse(data) } catch (e) {}
        self.listeners.forEach(fn => {
          try { fn(data) } catch (e) { console.error('[ws] listener error', e) }
        })
      })

      wx.onSocketClose(() => {
        self.connected = false
        self.socket = null
        // 5 秒后自动重连
        if (!self.reconnecting && getToken()) {
          self.reconnecting = true
          setTimeout(() => self.connect(), 5000)
        }
      })

      wx.onSocketError((err) => {
        console.warn('[ws] error', err)
      })
    } catch (e) {
      console.error('[ws] init error', e)
    }
  },

  /** 注册消息监听 */
  on(fn) {
    if (typeof fn === 'function' && this.listeners.indexOf(fn) === -1) {
      this.listeners.push(fn)
    }
  },

  /** 移除监听 */
  off(fn) {
    const i = this.listeners.indexOf(fn)
    if (i > -1) this.listeners.splice(i, 1)
  },

  /** 断开连接 */
  disconnect() {
    if (this.socket) {
      try { wx.closeSocket() } catch (e) {}
      this.socket = null
      this.connected = false
    }
    this.listeners.length = 0
  }
}

module.exports = {
  BASE,
  getToken,
  setToken,
  request,
  auth,
  user,
  chat,
  payment,
  vip,
  ws
}
