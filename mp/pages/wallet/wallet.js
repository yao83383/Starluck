const slPage = require('../../utils/sl-page.js')

const BILLS = [
  { id:1, type:'in',  cat:'gift', icon:'🎁', t1:'收到 阿哲 的星愿', t2:'10-31 14:23', amt:39.60 },
  { id:2, type:'in',  cat:'gift', icon:'🎁', t1:'收到 子墨 的星愿', t2:'10-30 22:10', amt:113.60 },
  { id:3, type:'out', cat:'wd',   icon:'💸', t1:'提现到微信',       t2:'10-28 10:15', amt:200.00 },
  { id:4, type:'in',  cat:'call', icon:'📞', t1:'同频时刻收益',     t2:'10-27 21:30', amt:18.00 }
]

slPage({
  data: { bills: BILLS, statusBarHeight: 20 },
  onLoad() {
    const app = getApp()
    this.setData({ statusBarHeight: app.globalData.statusBarHeight || 20 })
  },
  doWithdraw() { this.toast('提现入口待实现') },
  doRecharge() { this.go('/pages/recharge/recharge') }
})
