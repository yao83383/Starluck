# Starluck · 品牌与开发准则

> 版本：v1.0
> 适用：Starluck 全产品线（H5 / 小程序 / iOS / Android / 桌面）
> 替代：2026-05-31 之前的「一青友人」品牌资产

---

## 一、品牌内核

| 项 | 内容 |
|---|---|
| 中文名 | **小星运** |
| 英文名 | **Starluck** |
| 品牌使命 | 在偌大的宇宙里，你并不需要一直一个人发光 |
| 品牌叙事 | 每个人都是宇宙中一颗孤独漂流的星星，在漫长的人生轨迹里，我们不知道会与谁相遇，也不知道谁会成为照亮自己的人 |
| 核心隐喻 | 两颗孤单的星星偶然相遇 |

### 1.1 主 Slogan（必用）

> **Every star shines alone, until it finds another.**
> **每颗星星都曾独自闪耀，直到遇见另一颗星。**

应用：启动页、官网首屏、应用商店副标题、对外宣传物料。

### 1.2 备选 Slogan 矩阵（按场景）

| 场景 | 英文 | 中文 |
|---|---|---|
| 一句封神 / 朋友圈海报 | Two lonely stars. One shared sky. | 两颗孤独的星星，共享同一片夜空。 |
| 婚恋落地页 | We are all travelers among the stars. Love is finding the one who shares your orbit. | 我们都是星海中的旅人，爱情是找到那个愿意与你同轨运行的人。 |
| 首屏 Hero | Somewhere in the universe, a star is waiting for you. | 宇宙的某个角落，总有一颗星在等你。 |
| 宿命感 / 视频通话场景 | Not everyone is meant to meet. But some stars are born to find each other. | 不是所有人都会相遇，但有些星星注定会找到彼此。 |
| VIP 推广 / 高端定位 | Perhaps the greatest luck in life is finding the star that understands your light. | 人生最大的幸运，莫过于找到那颗读懂你光芒的星星。 |
| 邀请裂变文案 | Billions of stars, one destined encounter. | 亿万颗星辰，只为一次命中注定的相遇。 |

### 1.3 禁用词与替换

| ❌ 旧（一青友人） | ✅ 新（Starluck） |
|---|---|
| 「遇见命中注定」 | 「每颗星，都在等另一颗」 |
| 「一青友人」 | 「Starluck · 小星运」 |
| 「打招呼」 | 「发出星语」 |
| 「钻石 💎」 | 「星屑 ✨」 |
| 「VIP / 会员」 | 「星耀会员」 |
| 「至尊 VIP」 | 「星河至尊」 |
| 「礼物」 | 「星愿礼物」 |
| 「关注」 | 「点亮 TA」 |
| 「视频通话」 | 「同频时刻 / 银河连线」 |
| 「邀请好友」 | 「召唤同伴星」 |
| 「附近的人」 | 「同片星域」 |
| 「在线」 | 「闪耀中」 |
| 「实名认证」 | 「星证认证」 |

---

## 一·五、主题体系

Starluck 设计为**多主题切换架构**，所有主题共享相同的版式、组件、文案，仅色彩系统不同。这意味着：

- 同一份代码模板可派生 N 套视觉皮肤
- 用户可在客户端自由切换主题（未来功能）
- 不同主题对应不同情绪诉求（浪漫/温暖/经典）

### 已注册主题

| 主题 | ID | 主背景 | 品牌主色 | 定位 |
|---|---|---|---|---|
| **深紫银河** ⭐ 默认 | `galaxy-purple` | `#0a0418 → #1f0d3a` | `#a18cff → #ff8ad4` | 高级 · 神秘 · 浪漫 |
| **暖夜蓝** | `warm-night` | `#080a18 → #23264c` | `#ff8ab0 → #7a9eff` | 温暖 · 心动 · 大众 |
| **粉色原版** 归档 | `pink-original` | `#fef6f9 → #f3eaff` | `#ff5a8a → #b66dff` | 历史版本（一青友人） |

### 文件组织

```
项目根/
├─ app.html / admin.html / 产品文档/ui-spec.html  ← 当前生效主题（默认 galaxy-purple）
├─ themes/
│  ├─ index.html                ← 主题切换入口页
│  ├─ galaxy-purple/             ← 深紫银河完整版本
│  │  ├─ app.html
│  │  ├─ admin.html
│  │  └─ ui-spec.html
│  ├─ warm-night/                ← 暖夜蓝完整版本
│  │  ├─ app.html
│  │  ├─ admin.html
│  │  └─ ui-spec.html
│  └─ pink-original/             ← 粉色原版归档
│     ├─ app.html
│     ├─ admin.html
│     └─ ui-spec.html
└─ _backup_pinkversion/          ← 原始备份（历史快照）
```

### 主题切换工作流

1. 浏览器打开 `themes/index.html`
2. 选择目标主题 → 点击「设为当前主题」
3. 系统更新 localStorage 标记
4. （后续将实现）一键将选中主题文件复制到根目录

### 新增主题流程

未来要新增第 4、5 套主题时，遵循：

1. 复制现有主题目录为模板：`themes/new-theme/`
2. 用脚本批量替换色值（保留组件、文案、版式）
3. 在 `themes/index.html` 注册卡片
4. 在本文档「已注册主题」表中追加一行

### 跨主题不变量（主题切换时禁止改动）

| 项 | 原因 |
|---|---|
| 版式 / 间距 / 圆角 | 用户记忆点 |
| 字号层级 | 信息架构稳定 |
| 文案 / Slogan / 品牌名 | 品牌一致性 |
| 组件命名（sl-btn 等） | 工程稳定 |
| 业务规则（钻石比率、VIP 价格等） | 数据稳定 |
| 星光粒子 / 光晕动效 | 品牌核心视觉资产 |

### 跨主题变量（主题切换时可改）

- 7 大色彩 token（背景、卡片、主色 1/2、辅助、VIP 金、危险）
- 阴影颜色
- 渐变方向（135deg / 180deg）
- 头像 8 色色板

---

## 二、色彩系统（方案 B · 深紫 + 银河渐变）

### 2.1 核心色板

| 用途 | 名称 | 色值 | 应用 |
|---|---|---|---|
| **主背景（深）** | 星渊黑紫 | `#0a0418` | 全局底色，启动页 |
| **次背景（深）** | 深空紫 | `#1f0d3a` | 卡片、模态、二级背景 |
| **背景渐变** | 银河渐变 | `linear-gradient(180deg,#1f0d3a 0%,#0a0418 100%)` | 所有页面主背景 |
| **品牌主色** | 银河紫 | `#a18cff` | 按钮、CTA、强调文本 |
| **品牌主色 2** | 星云粉 | `#ff8ad4` | 渐变副色、爱意元素 |
| **品牌渐变** | 银河流光 | `linear-gradient(135deg,#a18cff 0%,#ff8ad4 100%)` | 主 CTA、Logo |
| **辅助色 · 冷** | 星河蓝 | `#7ab5ff` | 链接、信息态、辅助按钮 |
| **辅助色 · 暖** | 星屑粉 | `#ffd6f5` | 标签、轻提示、女性卡 |
| **VIP 金** | 星耀金 | `#ffd966` | VIP 标、奖励、付费高亮 |
| **VIP 渐变** | 金辉渐变 | `linear-gradient(135deg,#ffd966 0%,#ff9a3c 100%)` | VIP CTA |
| **成功色** | 闪耀绿 | `#5eead4` | 在线、已认证、完成态 |
| **危险色** | 警示红 | `#ff5c8a` | 警告、倒计时、提现 |
| **白文本** | 星光白 | `#ffffff` | 主标题 |
| **次文本** | 月光灰 | `rgba(255,255,255,0.7)` | 正文 |
| **三级文本** | 暗河灰 | `rgba(255,255,255,0.4)` | 辅助、占位 |

### 2.2 透明叠加层（毛玻璃）

| 用途 | 值 |
|---|---|
| 卡片背景 | `rgba(180,140,255,0.08)` |
| 边框 | `rgba(180,140,255,0.2)` |
| 悬停高亮 | `rgba(180,140,255,0.15)` |
| 模态遮罩 | `rgba(10,4,24,0.85)` |
| 毛玻璃滤镜 | `backdrop-filter: blur(12px)` |

### 2.3 阴影系统

```css
--shadow-card:   0 8px 24px rgba(10,4,24,0.5);
--shadow-btn:    0 6px 18px rgba(161,140,255,0.4);
--shadow-vip:    0 6px 18px rgba(255,217,102,0.35);
--shadow-glow:   0 0 32px rgba(161,140,255,0.5);   /* 光晕特效 */
```

### 2.4 渐变素材库（直接复制即可）

```css
/* 银河主渐变 */
background: linear-gradient(135deg,#a18cff 0%,#ff8ad4 100%);

/* 深空背景 */
background: radial-gradient(ellipse at 70% 30%,#3a1a52 0%,#1a0a2e 50%,#0a0418 100%);

/* VIP 金辉 */
background: linear-gradient(135deg,#ffd966 0%,#ff9a3c 100%);

/* 星云光晕 */
background: radial-gradient(ellipse at center,rgba(180,140,255,0.3),transparent 70%);
```

---

## 三、视觉语言

### 3.1 粒子星光（必用元素）

所有主屏（启动页、发现页、个人主页、VIP 页等）背景必须叠加星光粒子层。

```css
.starfield::before{
  content:"";
  position:absolute;inset:0;pointer-events:none;
  background-image:
    radial-gradient(1px 1px at 25% 35%,#fff,transparent),
    radial-gradient(2px 2px at 65% 75%,#e0c8ff,transparent),
    radial-gradient(1px 1px at 85% 25%,#fff,transparent),
    radial-gradient(1px 1px at 45% 85%,#ffd6f5,transparent),
    radial-gradient(2px 2px at 15% 55%,#fff,transparent),
    radial-gradient(1px 1px at 75% 15%,#e0c8ff,transparent);
  background-size:280px 280px;
  opacity:.85;
  animation:twinkle 5s ease-in-out infinite;
}
@keyframes twinkle{0%,100%{opacity:.5;}50%{opacity:1;}}
```

### 3.2 渐变光晕（强调焦点）

每屏 1-2 处局部光晕，用于强调焦点位置（用户头像、主 CTA、VIP 横幅）。

```css
.glow{
  background: radial-gradient(ellipse at center,rgba(180,140,255,0.3),transparent 60%);
  filter: blur(20px);
}
```

### 3.3 字号规范

| 层级 | 字号 / 字重 | 应用 |
|---|---|---|
| 巨标题（Hero） | 32-40px / 800 | 启动页 Slogan、品牌名 |
| 大标题 | 24-28px / 800 | 页面主标题、金额 |
| 小标题 | 16-20px / 700 | 模块标题、Tab Active |
| 正文 | 13-14px / 500 | 列表项、表单 |
| 辅助 | 11-12px / 400 | 描述、状态 |
| 微辅助 | 9-10.5px | 协议、时间戳 |

中文字体栈：
```css
font-family:-apple-system,BlinkMacSystemFont,"PingFang SC","Microsoft YaHei",sans-serif;
```

品牌名（Starluck）专属字号建议使用稍带装饰的字重（800），并配合渐变 text-clip：
```css
.brand-name{
  background:linear-gradient(135deg,#e0c8ff,#ffd6f5,#a18cff);
  -webkit-background-clip:text;background-clip:text;color:transparent;
  font-weight:800;letter-spacing:1px;
}
```

### 3.4 圆角与间距

| 元素 | 圆角 | 备注 |
|---|---|---|
| 卡片 | 14-16px | 比原方案略大，更柔和 |
| 按钮（标准） | 22px（胶囊） | — |
| 按钮（小） | 16px | — |
| 输入框 | 12px | — |
| 标签 | 12px（胶囊） | — |
| 模态卡片 | 20px | — |
| 头像 | 50% | 永远全圆 |

栅格间距：4 / 8 / 12 / 16 / 24 / 32（8 倍数）

### 3.5 图标语言

- 优先使用 emoji（兼容性好，免维护）：✨ 🌟 💫 ⭐ 🌙 🌌
- 自定义图标统一使用线条款（stroke 1.5px），描边色 `rgba(255,255,255,0.7)`
- 头像背景默认使用银河紫渐变 + 占位星座符号
- Logo 主图标：✨（六角星，星辰意象）

### 3.6 动效语言

| 动效 | 时长 | 缓动 | 应用 |
|---|---|---|---|
| 星光闪烁 | 5s | ease-in-out infinite | 背景粒子 |
| 光晕呼吸 | 3s | ease-in-out infinite | CTA 按钮、VIP 横幅 |
| 按钮按下 | 150ms | ease | scale(0.96) |
| 卡片入场 | 300ms | ease-out | translateY + opacity |
| 弹窗出现 | 250ms | cubic-bezier(.4,.2,.2,1) | scale + opacity |
| 流星划过 | 1.5s | linear | 限定特殊场景（如送出礼物） |

---

## 四、文案语气准则

### 4.1 整体调性

- **温柔而克制**：避免大喊大叫式营销，所有付费引导用「邀请」「等候」「相遇」等柔性词
- **诗意但不矫情**：每句话都可以朗读，但不能让用户尴尬
- **不卑不亢**：不堆砌「亲」「宝贝」等讨好词，把用户当作"同样在宇宙中漂流的另一颗星"

### 4.2 标准微文案对照表

| 场景 | ❌ 不要写 | ✅ 这样写 |
|---|---|---|
| 加载中 | 加载中... | 银河信号传递中... |
| 空状态 · 消息 | 暂无消息 | 还没有星向你发出信号 |
| 空状态 · 关注 | 暂无关注 | 你还没有点亮任何一颗星 |
| 钻石不足 | 钻石不足请充值 | 你的星屑不够照亮这条消息 |
| 提现成功 | 提现成功 | 星屑已化作真金，正在归途 |
| 实名提示 | 请先实名 | 让宇宙先认识真实的你 |
| 视频通话邀请 | 视频通话 | 邀你共度同频时刻 |
| 拉黑确认 | 确定拉黑此人？ | 让这颗星永远消失在你的星图？ |
| 退出登录 | 确认退出？ | 暂时熄灭你的星光？ |
| 注销账号 | 注销账号 | 永久离开星海 |
| 网络错误 | 网络异常 | 银河信号微弱，请稍后重试 |
| 等待对方回复 | 等待回复... | TA 还在思考如何回应你的光 |

### 4.3 推送通知风格

| 类型 | 文案模板 |
|---|---|
| 新消息 | "{昵称} 向你发出了星语 ✨" |
| 新访客 | "有一颗星驻足在你的轨道上 🌟" |
| 收到礼物 | "{昵称} 为你点亮了一颗 {礼物} 💫" |
| 视频邀请 | "{昵称} 邀你共度同频时刻 🌙" |
| 倒计时催充 | "首充礼包将随流星消逝，仅剩 {时间} ⏳" |

---

## 五、开发准则（强制）

### 5.1 CSS 变量（必须使用，禁止硬编码色值）

任何新建页面/组件，必须基于以下根变量。禁止在组件内出现裸 `#a18cff` `#ff8ad4`。

```css
:root{
  --bg-deep:        #0a0418;
  --bg-card:        #1f0d3a;
  --bg-gradient:    linear-gradient(180deg,#1f0d3a 0%,#0a0418 100%);
  --brand-1:        #a18cff;
  --brand-2:        #ff8ad4;
  --brand-grad:     linear-gradient(135deg,#a18cff,#ff8ad4);
  --brand-cool:     #7ab5ff;
  --brand-warm:     #ffd6f5;
  --vip-gold:       #ffd966;
  --vip-grad:       linear-gradient(135deg,#ffd966,#ff9a3c);
  --success:        #5eead4;
  --danger:         #ff5c8a;
  --text-1:         #ffffff;
  --text-2:         rgba(255,255,255,0.7);
  --text-3:         rgba(255,255,255,0.4);
  --border-1:       rgba(180,140,255,0.2);
  --overlay-card:   rgba(180,140,255,0.08);
  --overlay-mask:   rgba(10,4,24,0.85);
  --shadow-card:    0 8px 24px rgba(10,4,24,0.5);
  --shadow-btn:     0 6px 18px rgba(161,140,255,0.4);
  --shadow-glow:    0 0 32px rgba(161,140,255,0.5);
  --radius-card:    14px;
  --radius-btn:     22px;
  --radius-modal:   20px;
}
```

### 5.2 组件命名规范

- 全局组件以 `sl-` 前缀（Starluck）：`sl-btn`、`sl-card`、`sl-tag`
- 工具类同 Tailwind 风格：`flex`、`gap-12`、`text-2`
- 页面级容器：`page-{name}`，如 `page-login`、`page-discover`

### 5.3 头像与图片占位

- 用户无头像时使用 `av-{1-8}` 渐变占位（沿用原方案，色值见 4.4）
- 任何图片加载失败 fallback 为银河紫渐变 + 用户首字母

### 5.4 头像渐变色板（替代原版）

| 编号 | 渐变 | 适用 |
|---|---|---|
| `av-1` | `linear-gradient(135deg,#a18cff,#ff8ad4)` | 默认 / 女性 |
| `av-2` | `linear-gradient(135deg,#7ab5ff,#5eead4)` | 男性 |
| `av-3` | `linear-gradient(135deg,#ffd966,#ff9a3c)` | VIP |
| `av-4` | `linear-gradient(135deg,#ff5c8a,#ffd966)` | 活跃 |
| `av-5` | `linear-gradient(135deg,#5eead4,#7ab5ff)` | 新人 |
| `av-6` | `linear-gradient(135deg,#ff8ad4,#a18cff)` | — |
| `av-7` | `linear-gradient(135deg,#ff9a3c,#ff5c8a)` | — |
| `av-8` | `linear-gradient(135deg,#7ab5ff,#ff8ad4)` | — |

### 5.5 强制可访问性

- 主背景上正文字色对比度 ≥ 4.5:1（深紫底 + 白色 #fff = 通过）
- 按钮焦点必须有可见 outline（推荐 `2px solid var(--brand-1)`）
- 动画对 `prefers-reduced-motion: reduce` 的用户必须降级（关闭星光闪烁）

```css
@media(prefers-reduced-motion: reduce){
  .starfield::before,.glow{animation:none!important;}
}
```

### 5.6 代码注释品牌化

所有新代码注释，模块/区段分隔符使用 `=== ✨ ===` 风格：

```javascript
/* ============================================================
   ✨ Starluck · 用户认证模块
   ============================================================ */
```

---

## 六、迁移检查清单

替换旧版「一青友人」时，逐项核对：

- [ ] 所有页面背景换为 `var(--bg-gradient)`
- [ ] 主 CTA 按钮换为 `var(--brand-grad)`
- [ ] 所有 `#ff5a8a`、`#b66dff` 全局替换为对应新色
- [ ] Logo 文字 `一青友人` → `Starluck`
- [ ] Slogan `遇见命中注定` → `每颗星，都在等另一颗`
- [ ] 主屏添加 `.starfield` 粒子背景层
- [ ] 钻石 `💎` 改为星屑 `✨`（保留兼容性，UI 文案优先用「星屑」）
- [ ] VIP 文案换为「星耀会员」
- [ ] 「打招呼」按钮文案换为「发出星语」
- [ ] 「附近的人」Tab 换为「同片星域」
- [ ] 推送通知模板更新（见 4.3）
- [ ] App 名称、应用商店截图、宣传海报全部使用新 Slogan
- [ ] 检查 `prefers-reduced-motion` 降级
- [ ] 与原版 `_backup_pinkversion/` 做视觉回归对比

---

## 七、文件清单

| 文件 | 用途 |
|---|---|
| `Starluck-品牌与开发准则.md` | ⭐ 本文档（强制阅读） |
| `themes/index.html` | 🎨 主题中心入口（切换/对比/预览） |
| `themes/galaxy-purple/` | 深紫银河主题完整版本 |
| `themes/warm-night/` | 暖夜蓝主题完整版本 |
| `themes/pink-original/` | 粉色原版（历史归档） |
| `产品文档/ui-spec.html` | 22 屏 UI 交互规格（当前主题） |
| `产品文档/color-options.html` | 三方案对比页（决策记录） |
| `app.html` | 微信 H5 男用户端（当前主题） |
| `admin.html` | 运营后台（当前主题） |
| `_backup_pinkversion/` | 原始备份（历史快照） |

---

## 八、版本记录

| 版本 | 日期 | 内容 |
|---|---|---|
| v0.9 | 2026-05-31 | 原「一青友人」粉紫方案，已备份至 `_backup_pinkversion/` |
| v1.0 | 2026-06-02 | 品牌升级至 Starluck · 小星运，启用深紫银河方案 |
| **v1.1** | **2026-06-02** | **建立多主题体系：新增「暖夜蓝」主题；构建 `themes/` 目录与主题中心入口页** |

---

> Two lonely stars. One shared sky.
> 两颗孤独的星星，共享同一片夜空。
