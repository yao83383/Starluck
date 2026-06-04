const slPage = require('../../utils/sl-page.js')

const TABS = [
  { key: 'follow',  label: '关注' },
  { key: 'recommend', label: '推荐' },
  { key: 'nearby',   label: '同片星域' },
  { key: 'topic',    label: '话题' }
]

const TOPICS = [
  '#周末分享', '#汉服', '#美食', '#旅行', '#宠物', '#读书', '#音乐', '#咖啡'
]

const FEEDS = [
  {
    id: 'f1',
    uid: 'u1',
    name: '小诗',
    av: 1,
    vip: true,
    time: '10分钟前',
    text: '周末逛了一家很有调性的咖啡店，分享给大家～',
    tags: ['#周末分享', '#咖啡探店'],
    likes: 128,
    comments: 23,
    imgs: ['p1', 'p2', 'p3']
  },
  {
    id: 'f2',
    uid: 'u5',
    name: '梨花',
    av: 5,
    vip: false,
    time: '1小时前',
    text: '最近迷上了汉服～感觉穿越了一样～有人一起约拍吗',
    tags: ['#汉服', '#古风'],
    likes: 256,
    comments: 45,
    imgs: ['p4', 'p5']
  },
  {
    id: 'f3',
    uid: 'u4',
    name: '子墨',
    av: 4,
    vip: true,
    time: '3小时前',
    text: '今天的早餐~每一天都要好好生活',
    tags: ['#美食'],
    likes: 89,
    comments: 12,
    imgs: []
  },
  {
    id: 'f4',
    uid: 'u8',
    name: '若兮',
    av: 8,
    vip: true,
    time: '5小时前',
    text: '瑜伽真的是打开一天的最好方式',
    tags: ['#瑜伽', '#晨间'],
    likes: 312,
    comments: 67,
    imgs: ['p1']
  }
]

slPage({
  data: {
    tabs: TABS,
    tabKey: 'recommend',
    topics: TOPICS,
    feeds: FEEDS,
    statusBarHeight: 20
  },

  onLoad() {
    const app = getApp()
    this.setData({ statusBarHeight: app.globalData.statusBarHeight || 20 })
  },

  switchTab(e) {
    this.setData({ tabKey: e.currentTarget.dataset.key })
  },

  goUser(e) {
    const uid = e.currentTarget.dataset.uid
    this.go('/pages/profile/profile?id=' + uid)
  },

  goTopic(e) {
    this.toast('待实现：话题聚合页')
  },

  like(e) {
    const id = e.currentTarget.dataset.id
    const feeds = this.data.feeds.slice()
    const f = feeds.find(x => x.id === id)
    if (f) f.likes++
    this.setData({ feeds })
  },

  goProfile(e) {
    const uid = e.currentTarget.dataset.uid
    this.go('/pages/profile/profile?id=' + uid)
  }
})
