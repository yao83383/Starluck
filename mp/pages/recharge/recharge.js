const slPage = require('../../utils/sl-page.js')
const store = require('../../utils/store.js')

const PACKAGES = [
  { id:1, diamond:60,   price:6 },
  { id:2, diamond:300,  price:30 },
  { id:3, diamond:680,  price:68,   hot:true,   bonus:'首充翻倍' },
  { id:4, diamond:1280, price:128,  bonus:'首充翻倍' },
  { id:5, diamond:3280, price:328,  bonus:'首充翻倍' },
  { id:6, diamond:6480, price:648,  bonus:'首充翻倍' }
]

const METHODS = [
  { id:'wx', label:'微信支付', emoji:'💚' },
  { id:'ap', label:'支付宝',  emoji:'💙' }
]

slPage({
  data: {
    packages: PACKAGES,
    methods: METHODS,
    selected: 3,
    selectedPkg: PACKAGES[2],
    payMethod: 'wx',
    statusBarHeight: 20
  },

  onLoad() {
    const app = getApp()
    this.setData({ statusBarHeight: app.globalData.statusBarHeight || 20 })
  },

  pickPkg(e) {
    const id = e.currentTarget.dataset.id
    const pkg = PACKAGES.find(p => p.id === id)
    this.setData({ selected: id, selectedPkg: pkg })
  },
  pickPay(e) { this.setData({ payMethod: e.currentTarget.dataset.id }) },

  doPay() {
    const pkg = this.data.selectedPkg
    if (!pkg) return
    const s = store.get()
    store.update({
      diamonds: s.diamonds + pkg.diamond,
      firstRechargeShown: true
    })
    this.toast('充值成功 +' + pkg.diamond + ' ✨')
    setTimeout(() => this.back(), 1200)
  }
})
