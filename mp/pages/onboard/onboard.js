const slPage = require('../../utils/sl-page.js')
const store = require('../../utils/store.js')
const API = require('../../utils/api.js')

const INTERESTS = ['旅行','咖啡','摄影','美食','音乐','电影','健身','读书','宠物','游戏','汉服','瑜伽']

slPage({
  data: {
    step: 1,
    avNo: 1,
    gender: '',
    name: '',
    city: '',
    height: '',
    job: '',
    interests: [],
    allInterests: INTERESTS,
    statusBarHeight: 20
  },

  onLoad() {
    const app = getApp()
    this.setData({ statusBarHeight: app.globalData.statusBarHeight || 20 })
  },

  switchAv() { this.setData({ avNo: (this.data.avNo % 8) + 1 }) },
  pickGender(e) { this.setData({ gender: e.currentTarget.dataset.g }) },
  onName(e)   { this.setData({ name:   e.detail.value }) },
  onCity(e)   { this.setData({ city:   e.detail.value }) },
  onHeight(e) { this.setData({ height: e.detail.value }) },
  onJob(e)    { this.setData({ job:    e.detail.value }) },

  toggleInterest(e) {
    const i = e.currentTarget.dataset.i
    const list = this.data.interests.slice()
    const idx = list.indexOf(i)
    if (idx > -1) list.splice(idx, 1)
    else if (list.length < 8) list.push(i)
    this.setData({ interests: list })
  },

  next() {
    const { step, gender, name, city, interests, avNo } = this.data
    if (step === 2 && !gender) { this.toast('请选择性别'); return }
    if (step === 3 && (!name || !city)) { this.toast('请填写昵称和城市'); return }
    if (step === 5 && interests.length < 3) { this.toast('至少选 3 个兴趣'); return }

    if (step < 5) {
      this.setData({ step: step + 1 })
      return
    }

    const profile = {
      nickname: name,
      avatarNo: avNo,
      gender: gender === 'male' ? 'M' : 'F',
      city: city,
      height: this.data.height ? parseInt(this.data.height) : null,
      job: this.data.job || null,
      sign: '寻找那颗读懂你光芒的星',
      tags: JSON.stringify(interests)
    }

    // 同时写后端 + 本地 store
    Promise.all([
      API.user.updateProfile(profile).catch(() => {}),
      new Promise(resolve => {
        store.update({
          onboarded: true,
          diamonds: store.get().diamonds + 88,
          user: { av: avNo, name: name || '新人', city: city || '上海' }
        })
        resolve()
      })
    ]).then(() => {
      this.toast('资料完善 +88 ✨')
      setTimeout(() => wx.switchTab({ url: '/pages/discover/discover' }), 800)
  },

  skip() {
    store.update({ onboarded: true })
    wx.switchTab({ url: '/pages/discover/discover' })
  }
})
