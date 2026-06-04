Component({
  options: { addGlobalClass: true, multipleSlots: true },
  properties: {
    type: { type: String, value: 'primary' },   // primary / gold / ghost
    size: { type: String, value: 'normal' },     // normal / small
    block: { type: Boolean, value: false },
    disabled: { type: Boolean, value: false },
    loading: { type: Boolean, value: false }
  },
  methods: {
    onTap(e) {
      if (this.data.disabled || this.data.loading) return
      this.triggerEvent('tap', e.detail)
    }
  }
})
