/**
 * Starluck 后端API客户端
 *
 * 用途：将 app.html / admin.html 中原本的 localStorage Mock 操作
 *      迁移到对真实后端 (http://localhost:8090) 的调用
 *
 * 使用方式：在 HTML 中 <script src="api.js"></script>
 * 之后即可通过 window.starluckApi.* 调用接口
 *
 * @author AI
 * @date 2026-06-01
 * @ai-assisted ai辅助生成,开发人员已完成审查与测试。
 */
(function (global) {
  'use strict'

  // ============ 基础配置 ============
  // 生产环境后端地址
  // 如需切换，在引入 api.js 前设置 window.__STARLUCK_API_BASE__
  var DEFAULT_BASE = 'http://8.140.249.127:8080'
  var BASE = global.__STARLUCK_API_BASE__ || DEFAULT_BASE
  var TOKEN_KEY = 'starluck_token'

  // ============ Token管理 ============
  function getToken () {
    try { return global.localStorage.getItem(TOKEN_KEY) || '' } catch (e) { return '' }
  }
  function setToken (token) {
    try {
      if (token) global.localStorage.setItem(TOKEN_KEY, token)
      else global.localStorage.removeItem(TOKEN_KEY)
    } catch (e) {}
  }

  // ============ 统一请求方法 ============
  /**
   * 发起后端请求
   * @param {string} method HTTP方法 GET/POST/PUT/DELETE
   * @param {string} path 接口路径（不含base）
   * @param {object} [opts] 可选参数 {body, query, headers, raw}
   * @returns {Promise<any>} 解析后的 data 字段或抛错
   */
  function request (method, path, opts) {
    opts = opts || {}
    var url = BASE + path
    if (opts.query) {
      var qs = Object.keys(opts.query)
        .filter(function (k) { return opts.query[k] !== undefined && opts.query[k] !== null })
        .map(function (k) { return encodeURIComponent(k) + '=' + encodeURIComponent(opts.query[k]) })
        .join('&')
      if (qs) url += (url.indexOf('?') >= 0 ? '&' : '?') + qs
    }

    var headers = Object.assign({ 'Content-Type': 'application/json' }, opts.headers || {})
    var token = getToken()
    if (token) headers['Authorization'] = 'Bearer ' + token

    var init = { method: method, headers: headers }
    if (opts.body !== undefined) init.body = typeof opts.body === 'string' ? opts.body : JSON.stringify(opts.body)

    return global.fetch(url, init).then(function (resp) {
      if (!resp.ok && resp.status !== 401 && resp.status !== 500 && resp.status !== 400) {
        throw new Error('HTTP ' + resp.status)
      }
      return resp.json().catch(function () { return { code: resp.status, message: 'invalid json', data: null } })
    }).then(function (json) {
      if (opts.raw) return json
      if (json && json.code === 200) return json.data
      // 401: 登录态失效
      if (json && json.code === 401) {
        setToken('')
        throw new Error(json.message || '登录已过期，请重新登录')
      }
      throw new Error((json && json.message) || '请求失败')
    })
  }

  // ============ 认证接口 ============
  var auth = {
    /** 发送短信验证码 */
    sendCode: function (phone) {
      return request('POST', '/api/auth/send-code', { body: { phone: phone } })
    },
    /**
     * 手机号验证码登录
     * @returns {Promise<{token,userId,phone,nickname,avatar,onboarded,diamonds,isVip}>}
     */
    login: function (phone, code) {
      return request('POST', '/api/auth/login', { body: { phone: phone, code: code } })
        .then(function (data) {
          if (data && data.token) setToken(data.token)
          return data
        })
    },
    /** 获取当前登录用户信息 */
    getUserInfo: function () {
      return request('GET', '/api/auth/user-info')
    },
    /** 本地退出（清除token） */
    logout: function () {
      setToken('')
    },
    /** 是否已登录（仅看本地token是否存在） */
    isLoggedIn: function () {
      return !!getToken()
    }
  }

  // ============ 用户资料接口 ============
  var user = {
    /** 获取个人资料 */
    getProfile: function () {
      return request('GET', '/api/user/profile')
    },
    /**
     * 更新个人资料
     * @param {object} profile 资料对象（nickname/avatarNo/gender/age/city/...）
     */
    updateProfile: function (profile) {
      return request('PUT', '/api/user/profile', { body: profile })
    },
    /** 获取他人资料 */
    getOtherProfile: function (userId) {
      return request('GET', '/api/user/profile/' + userId)
    }
  }

  // ============ 聊天接口 ============
  var chat = {
    /** 会话列表 */
    getSessions: function () {
      return request('GET', '/api/chat/sessions')
    },
    /** 消息历史 */
    getMessages: function (sessionId) {
      return request('GET', '/api/chat/messages/' + sessionId)
    },
    /** 发送消息（自动扣费） */
    sendMessage: function (sessionId, content) {
      return request('POST', '/api/chat/send', { body: { sessionId: sessionId, content: content } })
    },
    /** 创建/获取会话 */
    createSession: function (peerUserId, isFake) {
      return request('POST', '/api/chat/session/create', {
        query: { peerUserId: peerUserId, isFake: !!isFake }
      })
    }
  }

  // ============ 礼物接口 ============
  var gift = {
    /** 礼物列表 */
    getList: function (category) {
      return request('GET', '/api/gift/list', { query: category ? { category: category } : {} })
    },
    /** 赠送礼物 */
    send: function (receiverId, giftId, quantity, sessionId) {
      return request('POST', '/api/gift/send', {
        body: { receiverId: receiverId, giftId: giftId, quantity: quantity || 1, sessionId: sessionId }
      })
    }
  }

  // ============ 支付接口 ============
  var payment = {
    /** 钱包信息 */
    getWallet: function () {
      return request('GET', '/api/payment/wallet')
    },
    /** 创建充值订单（演示环境直接完成支付） */
    recharge: function (packageId, payMethod) {
      return request('POST', '/api/payment/recharge', {
        body: { packageId: packageId, payMethod: payMethod || 'wechat' }
      })
    },
    /** 申请提现 */
    withdraw: function (amount, method, account) {
      return request('POST', '/api/payment/withdraw', {
        body: { amount: amount, method: method || 'wechat', account: account || '' }
      })
    }
  }

  // ============ VIP接口 ============
  var vip = {
    /** 套餐列表 */
    getPlans: function () {
      return request('GET', '/api/vip/plans')
    },
    /** 开通VIP */
    subscribe: function (planType, payMethod) {
      return request('POST', '/api/vip/subscribe', {
        query: { planType: planType, payMethod: payMethod || 'wechat' }
      })
    }
  }

  // ============ 后台管理接口 ============
  var admin = {
    /** 后台管理员登录 */
    login: function (phone, password) {
      return request('POST', '/api/admin/login', { body: { phone: phone, password: password } })
        .then(function (data) {
          if (data && data.token) setToken(data.token)
          return data
        })
    },
    /** 管理员/客服账号列表 */
    getUsers: function () {
      return request('GET', '/api/admin/users')
    },
    /** 创建管理员/客服账号 */
    createUser: function (phone, password, role) {
      return request('POST', '/api/admin/users', {
        body: { phone: phone, password: password, role: role }
      })
    },
    /** 更新账号信息 */
    updateUser: function (id, data) {
      return request('PUT', '/api/admin/users/' + id, { body: data })
    },
    /** 删除账号 */
    deleteUser: function (id) {
      return request('DELETE', '/api/admin/users/' + id)
    },
    /** 重置密码 */
    resetPassword: function (id, newPassword) {
      return request('PUT', '/api/admin/users/' + id + '/password', {
        body: { password: newPassword }
      })
    },
    /** 当前CS自己的统计 */
    getMyStats: function () {
      return request('GET', '/api/admin/users/me/stats')
    },
    /** 查指定CS统计（管理员用） */
    getCsStats: function (id) {
      return request('GET', '/api/admin/users/' + id + '/stats')
    },
    /** 假用户列表 */
    getFakeUsers: function (keyword) {
      return request('GET', '/api/admin/fake-users', { query: keyword ? { keyword: keyword } : {} })
    },
    /** 新增/更新假用户 */
    saveFakeUser: function (fakeUser) {
      return request('POST', '/api/admin/fake-users', { body: fakeUser })
    },
    /** 删除假用户 */
    deleteFakeUser: function (id) {
      return request('DELETE', '/api/admin/fake-users/' + id)
    },
    /** 批量生成假用户 */
    batchGenerate: function (count) {
      return request('POST', '/api/admin/fake-users/batch', { query: { count: count || 5 } })
    },
    /** 手动推送 */
    manualPush: function (fakeUserId, targetUserId) {
      return request('POST', '/api/admin/push/manual', {
        query: { fakeUserId: fakeUserId, targetUserId: targetUserId }
      })
    },
    /** 推送规则 */
    getPushRule: function () { return request('GET', '/api/admin/push/rule') },
    updatePushRule: function (rule) { return request('PUT', '/api/admin/push/rule', { body: rule }) },
    /** AI回复建议 */
    getAiSuggestion: function (sessionId) {
      return request('POST', '/api/admin/ai/suggest', { query: { sessionId: sessionId } })
    },
    /** 获取所有会话（管理员/CS） */
    getSessions: function () {
      return request('GET', '/api/admin/sessions')
    },
    /** 管理员加载会话消息（不更改已读状态） */
    getChatMessages: function (sessionId) {
      return request('GET', '/api/admin/chat/messages/' + sessionId)
    },
    /** 管理员标记会话已读 */
    markRead: function (sessionId) {
      return request('POST', '/api/admin/chat/mark-read/' + sessionId)
    },
    /** 以假用户身份发送消息 */
    chatSend: function (sessionId, content) {
      return request('POST', '/api/admin/chat/send', { body: { sessionId: sessionId, content: content } })
    },
    /** 分配假用户给客服 */
    assignFakeToCs: function (fakeId, csUserId, csName) {
      return request('PUT', '/api/admin/fake-users/' + fakeId + '/assign', {
        query: { csUserId: csUserId || 0, csName: csName || '' }
      })
    }
  }

  // ============ WebSocket（聊天实时推送） ============
  var ws = {
    socket: null,
    listeners: [],
    /** 连接 WebSocket */
    connect: function () {
      var token = getToken()
      if (!token) return null
      var wsBase = BASE.replace(/^http/, 'ws')
      var url = wsBase + '/ws/chat?token=' + encodeURIComponent(token)
      try {
        this.socket = new global.WebSocket(url)
        var self = this
        this.socket.onmessage = function (evt) {
          var data
          try { data = JSON.parse(evt.data) } catch (e) { data = { raw: evt.data } }
          self.listeners.forEach(function (fn) {
            try { fn(data) } catch (e) {}
          })
        }
        this.socket.onclose = function () { self.socket = null }
        return this.socket
      } catch (e) {
        return null
      }
    },
    /** 注册消息监听 */
    on: function (fn) {
      if (typeof fn === 'function') this.listeners.push(fn)
    },
    /** 断开连接 */
    disconnect: function () {
      if (this.socket) { try { this.socket.close() } catch (e) {} this.socket = null }
    }
  }

  // ============ 导出全局 API 对象 ============
  global.starluckApi = {
    BASE: BASE,
    getToken: getToken,
    setToken: setToken,
    request: request,
    auth: auth,
    user: user,
    chat: chat,
    gift: gift,
    payment: payment,
    vip: vip,
    admin: admin,
    ws: ws
  }

  // 兼容缩写
  global.api = global.starluckApi
})(typeof window !== 'undefined' ? window : globalThis);
