/**
 * ✨ Starluck · 主题配置
 * --------------------------------------------------------
 * 统一定义三套主题的所有色彩 token
 * 切换主题时只改这里，全局自动响应
 *
 * 调用方式：
 *   const { THEMES, getTheme } = require('utils/theme.js')
 *   const theme = getTheme('warm-night')
 */

const THEMES = {
  /* ============ 暖夜蓝 + 流光粉（默认） ============ */
  'warm-night': {
    id: 'warm-night',
    name: '暖夜蓝',
    en: 'Warm Night',
    desc: '流光粉 × 夜蓝 · 温暖心动',
    bgDeep: '#080a18',
    bgCard: '#23264c',
    bgCard2: '#2a2d52',
    bgGradient: 'linear-gradient(180deg,#23264c 0%,#080a18 100%)',
    bgRadial: 'radial-gradient(ellipse at 50% 0%,#23264c 0%,#15182f 50%,#080a18 100%)',
    brand1: '#ff8ab0',
    brand2: '#7a9eff',
    brandGrad: 'linear-gradient(135deg,#ff8ab0 0%,#7a9eff 100%)',
    brandCool: '#a8c4ff',
    brandWarm: '#ffd6e8',
    vipGold: '#ffd966',
    vipGrad: 'linear-gradient(135deg,#ffd966 0%,#ff9a3c 100%)',
    success: '#5eead4',
    danger: '#ff5c8a',
    text1: '#ffffff',
    text2: 'rgba(255,255,255,0.7)',
    text3: 'rgba(255,255,255,0.4)',
    border1: 'rgba(255,138,176,0.2)',
    overlayCard: 'rgba(255,138,176,0.08)',
    overlayMask: 'rgba(8,10,24,0.85)',
    shadowCard: '0 8rpx 24rpx rgba(8,10,24,0.5)',
    shadowBtn: '0 6rpx 18rpx rgba(255,138,176,0.4)',
    shadowGlow: '0 0 32rpx rgba(255,138,176,0.5)',
    navBg: '#0b0e22',
    tabActiveColor: '#ff8ab0'
  },

  /* ============ 深紫银河 ============ */
  'galaxy-purple': {
    id: 'galaxy-purple',
    name: '深紫银河',
    en: 'Galaxy Purple',
    desc: '银河紫 × 星云粉 · 高级浪漫',
    bgDeep: '#0a0418',
    bgCard: '#1f0d3a',
    bgCard2: '#2a1545',
    bgGradient: 'linear-gradient(180deg,#1f0d3a 0%,#0a0418 100%)',
    bgRadial: 'radial-gradient(ellipse at 70% 30%,#3a1a52 0%,#1a0a2e 50%,#0a0418 100%)',
    brand1: '#a18cff',
    brand2: '#ff8ad4',
    brandGrad: 'linear-gradient(135deg,#a18cff 0%,#ff8ad4 100%)',
    brandCool: '#7ab5ff',
    brandWarm: '#ffd6f5',
    vipGold: '#ffd966',
    vipGrad: 'linear-gradient(135deg,#ffd966 0%,#ff9a3c 100%)',
    success: '#5eead4',
    danger: '#ff5c8a',
    text1: '#ffffff',
    text2: 'rgba(255,255,255,0.7)',
    text3: 'rgba(255,255,255,0.4)',
    border1: 'rgba(180,140,255,0.2)',
    overlayCard: 'rgba(180,140,255,0.08)',
    overlayMask: 'rgba(10,4,24,0.85)',
    shadowCard: '0 8rpx 24rpx rgba(10,4,24,0.5)',
    shadowBtn: '0 6rpx 18rpx rgba(161,140,255,0.4)',
    shadowGlow: '0 0 32rpx rgba(161,140,255,0.5)',
    navBg: '#0e0620',
    tabActiveColor: '#a18cff'
  },

  /* ============ 粉色原版（归档） ============ */
  'pink-original': {
    id: 'pink-original',
    name: '粉色原版',
    en: 'Pink Original',
    desc: '初代粉紫 · 历史归档',
    bgDeep: '#fef6f9',
    bgCard: '#ffffff',
    bgCard2: '#fafafd',
    bgGradient: 'linear-gradient(180deg,#fef6f9 0%,#f3eaff 100%)',
    bgRadial: 'radial-gradient(ellipse at 50% 0%,#fef6f9 0%,#f3eaff 100%)',
    brand1: '#ff5a8a',
    brand2: '#b66dff',
    brandGrad: 'linear-gradient(135deg,#ff5a8a 0%,#b66dff 100%)',
    brandCool: '#7c5aff',
    brandWarm: '#fef0f5',
    vipGold: '#ffd75e',
    vipGrad: 'linear-gradient(135deg,#ffd75e 0%,#ff9a3c 100%)',
    success: '#4ade80',
    danger: '#ff3b5c',
    text1: '#2d2d3a',
    text2: '#666666',
    text3: '#999999',
    border1: '#f0f0f5',
    overlayCard: '#ffffff',
    overlayMask: 'rgba(0,0,0,0.5)',
    shadowCard: '0 6rpx 16rpx rgba(0,0,0,0.05)',
    shadowBtn: '0 6rpx 18rpx rgba(255,90,138,0.35)',
    shadowGlow: '0 0 32rpx rgba(255,90,138,0.3)',
    navBg: '#ffffff',
    tabActiveColor: '#ff5a8a'
  }
}

const DEFAULT_THEME = 'warm-night'

function getTheme(id) {
  return THEMES[id] || THEMES[DEFAULT_THEME]
}

function listThemes() {
  return Object.values(THEMES)
}

/**
 * 把主题对象转成 CSS 变量字符串，用于注入到根元素的 style 上
 * 注意：小程序不能直接全局设置 CSS 变量，
 * 需要在每个 page 的根 view 上 :style="..." 绑定
 */
function themeToStyle(theme) {
  return Object.keys(theme)
    .filter(k => typeof theme[k] === 'string' && k !== 'id' && k !== 'name' && k !== 'en' && k !== 'desc')
    .map(k => `--sl-${k}:${theme[k]};`)
    .join('')
}

module.exports = {
  THEMES,
  DEFAULT_THEME,
  getTheme,
  listThemes,
  themeToStyle
}
