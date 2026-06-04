const slPage = require('../../utils/sl-page.js')
const store = require('../../utils/store.js')

const PLANS = [
  { id:'month',  name:'月卡', price:30,  origin:'',    extra:'30天' },
  { id:'season', name:'季卡', price:68,  origin:'¥135', extra:'限时5折', hot:true },
  { id:'year',   name:'年卡', price:198, origin:'',    extra:'365天' }
]

const RIGHTS = [
  { ic:'👁️', nm:'谁看过我' },
  { ic:'💝', nm:'喜欢我' },
  { ic:'🔍', nm:'高级筛选' },
  { ic:'⭐', nm:'优先曝光' },
  { ic:'💬', nm:'100条/日' },
  { ic:'📞', nm:'通话8折' },
  { ic:'✨', nm:'每月300星屑' },
  { ic:'🎁', nm:'专属星愿' }
]

slPage({
  data: {
    plans: PLANS,
    rights: RIGHTS,
    selected: 'season',
    selectedPlan: PLANS[1],
    statusBarHeight: 20
  },

  onLoad() {
    const app = getApp()
    this.setData({ statusBarHeight: app.globalData.statusBarHeight || 20 })
  },

  pickPlan(e) {
    const id = e.currentTarget.dataset.id
    const p = PLANS.find(x => x.id === id)
    this.setData({ selected: id, selectedPlan: p })
  },

  doPay() {
    store.update({ user: { vip: true, vipExpire: '90 天后' } })
    this.toast('已开通星耀会员 · ' + this.data.selectedPlan.name)
    setTimeout(() => this.back(), 1200)
  }
})
