const slPage = require('../../utils/sl-page.js')
const API = require('../../utils/api.js')

slPage({
  data: {
    user: null,
    isFake: false,
    rawId: 0,
    statusBarHeight: 20
  },

  onLoad(q) {
    const app = getApp()
    this.setData({ statusBarHeight: app.globalData.statusBarHeight || 20 })

    const rawQ = q && q.id ? q.id : ''
    const isFake = /^f/i.test(rawQ)
    const rawId = parseInt(rawQ.replace(/[^0-9]/g, '')) || 0
    this.setData({ isFake, rawId })

    if (rawId > 0 && !isFake) {
      // 真实用户走 /api/user/profile/:id
      API.user.getOther(rawId).then(profile => {
        this.setData({
          user: {
            id: rawId,
            name: profile.nickname || '神秘星人',
            av: profile.avatarNo || 1,
            age: profile.age || 20,
            city: profile.city || '',
            dist: '',
            sign: profile.sign || '',
            vip: profile.isVip || false,
            height: profile.height || 165,
            weight: profile.weight || 50,
            job: profile.job || '',
            edu: profile.edu || '',
            home: profile.hometown || '',
            love: profile.loveStatus || '单身',
            tags: Array.isArray(profile.tags) ? profile.tags : []
          }
        })
      }).catch(() => {})
    } else if (rawId > 0 && isFake) {
      // 假用户：从 discover 列表里找（不调单独接口，简化处理）
      // 直接从全局列表里查找
      const list = (getApp().lastDiscoverList || [])
      const found = list.find(u => u.isFake && u.rawId === rawId)
      if (found) {
        this.setData({
          user: {
            id: rawId,
            name: found.name,
            av: found.av,
            age: found.age,
            city: found.city,
            dist: found.dist,
            sign: found.sign,
            vip: found.vip,
            height: 165, weight: 48,
            job: '', edu: '', home: found.city,
            love: '单身',
            tags: found.tags
          }
        })
      } else {
        // fallback：基本展示
        this.setData({
          user: {
            id: rawId, name: '神秘星人', av: 1, age: 22,
            city: '', dist: '', sign: '',
            vip: false, height: 165, weight: 48,
            job: '', edu: '', home: '', love: '单身', tags: []
          }
        })
      }
    }
  },

  gift() { this.toast('星愿礼物待接入') },
  fav() { this.toast('已点亮 ✨') },

  chat() {
    if (!this.data.user) return
    // 用 f 或 u 前缀传给 chat 页，chat 页会识别是否假用户
    const prefix = this.data.isFake ? 'f' : 'u'
    wx.navigateTo({ url: '/pages/chat/chat?id=' + prefix + this.data.rawId })
  }
})
