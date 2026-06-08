/**
 * ✨ Starluck · 全局响应式状态管理
 * --------------------------------------------------------
 * 提供跨页面的状态同步，所有订阅页面在 store 变化时自动重新渲染主题相关字段
 */

const STORAGE_KEY = 'starluck_store'

const DEFAULT_STATE = {
  // 用户态
  loggedIn: false,
  onboarded: false,
  user: {
    id: 'me',
    name: '我',
    av: 1,
    age: 24,
    city: '上海',
    sign: '寻找那颗读懂你光芒的星',
    vip: false,
    vipExpire: ''
  },
  // 资产
    diamonds: 666,        // 星光
  cash: 88.50,
  isAuthed: false,
  chatFreeRemain: 5,

  // 当前主题
  themeId: 'warm-night',

  // 消息未读总数
  totalUnread: 0,

  // 置顶会话
  pinnedSessions: [],

  // 设置
  settings: {
    invisible: false,
    hideLoc: false,
    vipOnly: true,
    sound: true,
    vibrate: true
  },

  // 邀请
  invitedCount: 12,
  invitedPaid: 5,
  invitedReward: 686.50,
  myRank: 28,
  inviteCode: 'SL8888',

  firstRechargeShown: false
}

const state = {}
let _initialized = false
const _subscribers = []

function _load() {
  try {
    const saved = wx.getStorageSync(STORAGE_KEY)
    if (saved && typeof saved === 'object') {
      Object.assign(state, DEFAULT_STATE, saved)
      // user 等嵌套对象单独合并
      state.user = Object.assign({}, DEFAULT_STATE.user, saved.user || {})
      state.settings = Object.assign({}, DEFAULT_STATE.settings, saved.settings || {})
      return
    }
  } catch (e) {}
  Object.assign(state, JSON.parse(JSON.stringify(DEFAULT_STATE)))
}

function _save() {
  try {
    wx.setStorageSync(STORAGE_KEY, state)
  } catch (e) {}
}

function init() {
  if (_initialized) return
  _load()
  _initialized = true
}

function get() {
  return state
}

/**
 * 局部更新状态
 * @param {Object} patch  要合并的字段
 * @param {Boolean} silent 静默模式不触发订阅者
 */
function update(patch, silent) {
  if (!patch) return
  Object.keys(patch).forEach(k => {
    if (typeof patch[k] === 'object' && patch[k] !== null && !Array.isArray(patch[k])) {
      state[k] = Object.assign({}, state[k], patch[k])
    } else {
      state[k] = patch[k]
    }
  })
  _save()
  if (!silent) _notify()
}

function reset() {
  Object.keys(state).forEach(k => delete state[k])
  Object.assign(state, JSON.parse(JSON.stringify(DEFAULT_STATE)))
  _save()
  _notify()
}

function subscribe(fn) {
  _subscribers.push(fn)
  return () => {
    const i = _subscribers.indexOf(fn)
    if (i > -1) _subscribers.splice(i, 1)
  }
}

function _notify() {
  _subscribers.slice().forEach(fn => {
    try { fn(state) } catch (e) { console.error('[store] subscriber error', e) }
  })
}

/* ============ 主题相关 ============ */
function setTheme(themeId) {
  update({ themeId })
}

module.exports = {
  init,
  get,
  update,
  reset,
  subscribe,
  setTheme
}
