# ✨ Starluck · 微信小程序版

> 版本：v1.0
> 默认主题：**暖夜蓝 + 流光粉**
> 创建日期：2026-06-03

---

## 一、项目结构

```
mp/
├─ app.js                  应用入口（初始化 store、系统信息）
├─ app.json                全局配置（页面注册、TabBar、组件）
├─ app.wxss                全局基础样式 + sl-* 组件类
├─ project.config.json     开发者工具项目配置
├─ sitemap.json            搜索收录配置
│
├─ utils/                  工具与核心逻辑
│   ├─ theme.js            ⭐ 主题配置（3 套主题 token 定义）
│   ├─ store.js            ⭐ 全局响应式状态管理 + localStorage
│   └─ sl-page.js          ⭐ Page 包装器（自动注入主题 + 订阅 store）
│
├─ components/             公共组件（usingComponents 全局注册）
│   ├─ starfield/          星光粒子背景层
│   ├─ sl-button/          统一品牌按钮
│   ├─ sl-card/            毛玻璃卡片容器
│   └─ sl-tabbar/          自定义底部导航
│
├─ pages/                  页面（12 个）
│   ├─ login/              ① 登录注册
│   ├─ discover/           ② 发现 / 用户列表        ★ Tab
│   ├─ messages/           ③ 消息列表              ★ Tab
│   ├─ me/                 ④ 个人中心              ★ Tab
│   ├─ onboard/            ⑤ 新人资料填写
│   ├─ profile/            ⑥ 个人主页
│   ├─ chat/               ⑦ 聊天对话
│   ├─ recharge/           ⑧ 星屑充值
│   ├─ vip/                ⑨ 星耀会员开通
│   ├─ wallet/             ⑩ 钱包
│   ├─ settings/           ⑪ 设置 & 安全中心 (含主题切换)
│   └─ theme/              ⑫ 主题中心 (完整主题展示页)
│
└─ images/                 静态资源（预留）
```

---

## 二、主题系统架构 ⭐

### 2.1 设计原则

| 原则 | 说明 |
|------|------|
| **单一数据源** | `utils/theme.js` 集中定义所有主题 token，组件不允许硬编码颜色 |
| **响应式切换** | 用户切换主题 → store 更新 → 所有页面通过订阅自动重新注入 CSS 变量 |
| **零侵入** | 业务代码无需关心主题，统一通过 `sl-page.js` 注入 `themeStyle` 到根 view |
| **持久化** | 用户选择的主题保存在 localStorage，下次启动自动应用 |

### 2.2 已注册主题

| ID | 名称 | 主色 | 定位 |
|----|------|------|------|
| `warm-night` ⭐**默认** | 暖夜蓝 | `#ff8ab0 → #7a9eff` | 温暖 · 心动 · 大众 |
| `galaxy-purple` | 深紫银河 | `#a18cff → #ff8ad4` | 高级 · 神秘 · 浪漫 |
| `pink-original` | 粉色原版 | `#ff5a8a → #b66dff` | 历史归档 |

### 2.3 主题切换两种方式

| 入口 | 路径 | 体验 |
|------|------|------|
| **设置页快速切换** | `pages/settings/settings` 顶部 | 3 个色块卡片，一键切换 |
| **主题中心完整页** | `pages/theme/theme` | 大卡片预览 + 5 色色板 + 应用按钮 |

### 2.4 工程实现关键点

每个页面必须按如下方式编写（已被 `sl-page.js` 简化）：

```js
// 页面 js
const slPage = require('../../utils/sl-page.js')
slPage({
  data: { foo: 1 },     // 你的业务数据
  onLoad() { ... }       // 你的逻辑
})
// sl-page 自动注入：data.theme, data.themeStyle, data.store
```

```xml
<!-- 页面 wxml：根 view 必须包裹 .page 并 :style 绑定 themeStyle -->
<view class="page" style="{{themeStyle}}">
  ...
</view>
```

```css
/* 页面 wxss：所有颜色用 CSS 变量，禁止硬编码 */
.my-element {
  background: var(--sl-bgCard);
  color: var(--sl-text1);
  border: 1rpx solid var(--sl-border1);
}
```

### 2.5 主题 token 速查

| Token | 含义 |
|-------|------|
| `--sl-bgDeep` | 最深背景（页面底色） |
| `--sl-bgCard` | 卡片背景 |
| `--sl-bgGradient` | 页面主渐变 |
| `--sl-bgRadial` | 页面径向渐变 |
| `--sl-brand1` / `--sl-brand2` | 品牌色 1/2 |
| `--sl-brandGrad` | 品牌主渐变（CTA 按钮、Logo） |
| `--sl-brandCool` / `--sl-brandWarm` | 辅助冷/暖色 |
| `--sl-vipGold` / `--sl-vipGrad` | VIP 金色 / 渐变 |
| `--sl-success` / `--sl-danger` | 成功 / 危险 |
| `--sl-text1` / `--sl-text2` / `--sl-text3` | 三级文字色 |
| `--sl-border1` | 主边框 |
| `--sl-overlayCard` | 半透明卡片底 |
| `--sl-overlayMask` | 模态遮罩 |
| `--sl-shadowCard` / `--sl-shadowBtn` / `--sl-shadowGlow` | 阴影系列 |

---

## 三、调试与运行

### 3.1 微信开发者工具导入

1. 打开微信开发者工具
2. 「导入项目」→ 选择目录 `mp/`
3. AppID 填 `touristappid`（演示模式）或填你自己的小程序 AppID
4. 不使用云开发
5. 立即编译，会自动跳转到登录页

### 3.2 调试快捷操作

打开开发者工具 → 调试器 Console，可执行：

```js
// 重置所有数据
wx.removeStorageSync('starluck_store'); location.reload()

// 跳转任意页面
wx.navigateTo({ url:'/pages/theme/theme' })

// 模拟登录态
wx.setStorageSync('starluck_store', { loggedIn:true, onboarded:true, themeId:'warm-night' })
```

### 3.3 真机预览注意事项

- 微信 H5 部分 CSS 在小程序中不可用（已避免）
- `backdrop-filter` 在低版本 iOS 微信中可能失效，但不影响功能
- `aspect-ratio` 需要基础库 2.20.0+
- 星光粒子在低端机上会自动通过 `prefers-reduced-motion` 降级

---

## 四、与其他版本对照

| 版本 | 文件 | 默认主题 | 特点 |
|------|------|---------|------|
| H5 男用户端 | `app.html` | 深紫银河 | 单文件 SPA |
| 运营后台 | `admin.html` | 深紫银河 | 客服工作台 |
| **微信小程序** ⭐ 本目录 | `mp/` | **暖夜蓝** | 原生小程序，支持主题切换 |

业务规则、文案、Slogan 与 H5 版本完全一致（详见 `Starluck-品牌与开发准则.md`）。

---

## 五、待办与扩展点

| 优先级 | 项 |
|-------|---|
| P0 | 接入后端 API（替换 mock 数据） |
| P0 | 视频通话页 `pages/call`（接入腾讯云 TRTC） |
| P1 | 礼物面板组件 `components/sl-gift-panel` |
| P1 | 邀请页 `pages/invite` + 海报生成 |
| P1 | 动态广场 `pages/feed` |
| P2 | 实名认证 `pages/auth`（接入云人脸） |
| P2 | 首充弹窗 `components/first-recharge` |
| P2 | 推送通知（订阅消息） |

---

## 六、新增主题流程

想加第 4 套主题（如「极地白」「暮色橙」）按以下步骤：

1. **在 `utils/theme.js` 的 THEMES 对象中追加一项**
   ```js
   'polar-white': {
     id: 'polar-white',
     name: '极地白',
     en: 'Polar White',
     desc: '极简白 · 干净纯粹',
     bgDeep: '#f5f7fa',
     bgCard: '#ffffff',
     brand1: '#00aaff',
     // ... 其他 token
   }
   ```

2. **不需要改任何页面代码**——主题中心和设置页会自动出现新主题卡片

3. **如果新主题是浅色调**，需注意 `text1` 等文字色要相应改为深色

---

## 七、版本记录

| 版本 | 日期 | 内容 |
|------|------|------|
| v1.0 | 2026-06-03 | 首版发布：12 个页面 / 4 个组件 / 3 套主题动态切换 / 默认暖夜蓝 |

---

> ✨ Every star shines alone, until it finds another.
> 每颗星星都曾独自闪耀，直到遇见另一颗星。
