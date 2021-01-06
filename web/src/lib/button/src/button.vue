<template>
  <button
    class="hui-button"
    @click="handleClick"
    :disabled="buttonDisabled || loading"
    :autofocus="autofocus"
    :type="nativeType"
    :class="[
      type ? 'hui-button--' + type : '',
      buttonSize ? 'hui-button--' + buttonSize : '',
      {
        'is-disabled': buttonDisabled,
        'is-loading': loading,
        'is-plain': plain,
        'is-round': round,
        'is-circle': circle
      }
    ]"
  >
    <i class="hui-icon-loading" v-if="loading"></i>
    <i :class="icon" v-if="icon && !loading"></i>
    <slot name="icon"></slot>
    <span v-if="$slots.default">
      <slot></slot>
    </span>
  </button>
</template>
<script>
export default {
  name: "HuiButton",

  inject: {
    elForm: {
      default: ""
    },
    elFormItem: {
      default: ""
    }
  },

  props: {
    type: {
      type: String,
      default: "default"
    },
    size: String,
    icon: {
      type: String,
      default: ""
    },
    nativeType: {
      type: String,
      default: "button"
    },
    loading: Boolean,
    disabled: Boolean,
    plain: Boolean,
    autofocus: {
      type: Boolean,
      default: false
    },
    round: {
      type: Boolean,
      default: true
    },
    circle: Boolean
  },

  computed: {
    _elFormItemSize() {
      return (this.elFormItem || {}).elFormItemSize;
    },
    buttonSize() {
      return this.size || this._elFormItemSize || (this.$ELEMENT || {}).size;
    },
    buttonDisabled() {
      return this.disabled || (this.elForm || {}).disabled;
    }
  },

  methods: {
    handleClick(evt) {
      this.$emit("click", evt);
    }
  }
};
</script>
<style lang="scss" rel="stylesheet/scss">
@import "../../theme-default/button.scss";
</style>