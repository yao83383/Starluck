const { getTheme, themeToStyle } = require('../../utils/theme.js')
const store = require('../../utils/store.js')

Component({
  options: { addGlobalClass: true },
  properties: {
    active: { type: String, value: 'discover' }
  },
  data: {
    themeStyle: ''
  },
  lifetimes: {
    attached() {
      store.init()
      this._applyTheme()
      this._unsub = store.subscribe(() => this._applyTheme())
    },
    detached() {
      if (this._unsub) this._unsub()
    }
  },
  methods: {
    _applyTheme() {
      const s = store.get()
      const theme = getTheme(s.themeId)
      this.setData({ themeStyle: themeToStyle(theme) })
    },
    toDiscover() { if (this.data.active !== 'discover') wx.switchTab({ url: '/pages/discover/discover' }) },
    toFeed()     { if (this.data.active !== 'feed')     wx.switchTab({ url: '/pages/feed/feed' }) },
    toMessages() { if (this.data.active !== 'messages') wx.switchTab({ url: '/pages/messages/messages' }) },
    toMe()       { if (this.data.active !== 'me')       wx.switchTab({ url: '/pages/me/me' }) }
  }
})
