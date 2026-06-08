const slPage = require('../../utils/sl-page.js')
const store = require('../../utils/store.js')

const PACKAGES = [
  { id:1, diamond:600,   price:6 },
  { id:2, diamond:3000,  price:30,   bonus:300 },
  { id:3, diamond:6800,  price:68,   hot:true,   bonus:980 },
  { id:4, diamond:12800, price:128,  bonus:2000 },
  { id:5, diamond:32800, price:328,  bonus:5000 },
  { id:6, diamond:64800, price:648,  bonus:10000 }
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
    const total = pkg.diamond + (pkg.bonus || 0)
    store.update({
      diamonds: s.diamonds + total,
      firstRechargeShown: true
    })
    const msg = pkg.bonus ? `充值成功 +${pkg.diamond}+${pkg.bonus} ✨` : `充值成功 +${pkg.diamond} ✨`
    this.toast(msg)
    setTimeout(() => this.back(), 1200)
  }
})
