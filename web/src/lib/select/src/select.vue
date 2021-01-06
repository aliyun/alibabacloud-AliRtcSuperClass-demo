<template>
  <div class="hui-select" v-clickoutside="hideMenu">
    <input type="text" class="hui-select-input" v-model="lab" @focus.stop="menuVisibleOnFocus=true" readonly>
    <i class="iconfont icon-xiangxiajiantou"></i>
    <ul class="hui-select-ul" v-if="menuVisibleOnFocus">
      <slot></slot>
    </ul>
  </div>
</template>

<script>
import Clickoutside from "../../utils/clickoutside";
  export default {
    name: "HuiSelect",
    data() {
      return {
        val: this.value,
        lab: this.label,
        menuVisibleOnFocus: false
      }
    },
    directives:{Clickoutside},
    props: ['value', 'label'],
    model: {
      prop: "value", //绑定的值，通过父组件传递
      event: "update" //自定义名 可以随便改
    },
    methods: {
      hideMenu() {
        setTimeout(() => {
          this.menuVisibleOnFocus = false;
        }, 50);
      },
      setVal(valObj) {
        this.val = valObj.label ? valObj.label : valObj.value;
        this.lab = valObj.label;
        this.$emit("update", valObj.value ? valObj.value : valObj.label);
        this.$emit("change", valObj.value ? valObj.value : valObj.label);
        this.hideMenu();
      }
    },
    watch: {
      value(newVal, oldVal) {
        this.val = newVal;
      },
      label(newVal, oldVal) {
        this.lab = newVal;
      }
    }
  }
</script>

<style lang="scss">
  @import "../../theme-default/select.scss";
</style>