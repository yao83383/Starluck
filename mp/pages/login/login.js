const slPage = require('../../utils/sl-page.js')
const store = require('../../utils/store.js')
const API = require('../../utils/api.js')

slPage({
  data: {
    mode: 'sms',        // 'sms' | 'password'
    phone: '',
    code: '',
    password: '',
    agreed: true,
    cd: 0,
    loading: false
  },

  onLoad() {
    // 已登录直接进
    if (store.get().loggedIn) {
      wx.switchTab({ url: '/pages/discover/discover' })
    }
  },

  switchMode(e) {
    this.setData({ mode: e.currentTarget.dataset.mode })
  },

  onPhoneInput(e) { this.setData({ phone: e.detail.value }) },
  onCodeInput(e)  { this.setData({ code:  e.detail.value }) },
  onPwdInput(e)   { this.setData({ password: e.detail.value }) },
  toggleAgree()   { this.setData({ agreed: !this.data.agreed }) },

  sendCode() {
    if (this.data.cd > 0) return
    if (!/^1\d{10}$/.test(this.data.phone)) {
      this.toast('请输入正确手机号')
      return
    }
    this.setData({ cd: 60 })
    const t = setInterval(() => {
      const c = this.data.cd - 1
      this.setData({ cd: c })
      if (c <= 0) clearInterval(t)
    }, 1000)

    API.auth.sendCode(this.data.phone).then(data => {
      const code = (typeof data === 'string') ? data : (data && data.code ? data.code : '未知')
      wx.showModal({
        title: '验证码',
        content: '你的验证码是：' + code + '\n\n有效期 5 分钟',
        showCancel: false,
        confirmText: '我知道了',
        success: () => {
          if (code !== '未知') { this.setData({ code: code }) }
        }
      })
    }).catch(err => {
      wx.showModal({ title: '发送失败', content: err.message || '网络错误', showCancel: false })
    })
  },

  doLogin() {
    if (!this.data.agreed) { this.toast('请同意用户协议'); return }
    if (!/^1\d{10}$/.test(this.data.phone)) { this.toast('请输入正确手机号'); return }

    if (this.data.mode === 'sms') {
      if (!this.data.code) { this.toast('请输入验证码'); return }
      this.smsLogin()
    } else {
      if (!this.data.password) { this.toast('请输入密码'); return }
      this.pwdLogin()
    }
  },

  smsLogin() {
    this.setData({ loading: true })
    API.auth.login(this.data.phone, this.data.code).then(data => {
      this.onLoginSuccess(data)
    }).catch(err => {
      this.setData({ loading: false })
      wx.showToast({ title: err.message || '登录失败', icon: 'none' })
    })
  },

  pwdLogin() {
    this.setData({ loading: true })
    API.auth.loginByPassword(this.data.phone, this.data.password).then(data => {
      this.onLoginSuccess(data)
    }).catch(err => {
      this.setData({ loading: false })
      wx.showToast({ title: err.message || '登录失败', icon: 'none' })
    })
  },

  onLoginSuccess(data) {
    store.update({
      loggedIn: true,
      diamonds: data.diamonds || 666,
      onboarded: data.onboarded || false,
      user: { id: 'me', name: data.nickname || '新人', av: data.avatarNo || 1, dbId: data.userId }
    })
    // 登录成功后建立 WebSocket 长连接
    API.ws.connect()
    wx.showToast({ title: '登录成功', icon: 'success' })
    setTimeout(() => {
      if (!store.get().onboarded) {
        wx.redirectTo({ url: '/pages/onboard/onboard' })
      } else {
        wx.switchTab({ url: '/pages/discover/discover' })
      }
    }, 600)
  }
})
