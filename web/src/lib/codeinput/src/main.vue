<template>
  <div class="hui-code-input">
    <span
      class="hui-code-input-label" v-if="focused||aCodes">{{title}}</span>
    <input
      ref="huiCode"
      id="huiCode"
      type="tel"
      :disabled="disable"
      :maxlength="codeLength"
      v-model="aCodes"
      @focus="focused = true"
      @blur="focused = false"
      :placeholder="placeholder"
      :class="{'on':focused||aCodes}"
      autocomplete="off"
    />
    <label
      v-show="focused||aCodes"
      for="huiCode"
      class="line"
      v-for="(item,index) in codeLength"
      :key="index"
      :class="{'animated': focused && cursorIndex === index}"
      v-text="codeArr[index]"
    ></label>
  </div>
</template>
<script>
export default {
  name: "HuiCodeInput",
  props: {
    codeLength: {
      type: Number,
      default: 5
    },
    placeholder: {
      type: String,
      default: ""
    },
    value: {
      type: String,
      default: ""
    },
    disable: Boolean,
    title: String,

  },
  data() {
    return {
      aCodes: this.value,
      telDisabled: false,
      focused: false
    };
  },
  model: {
    prop: "value", //绑定的值，通过父组件传递
    event: "update" //自定义名 可以随便改
  },
  computed: {
    codeArr() {
      this.$emit("update", this.aCodes);
      return this.aCodes ? this.aCodes.split("") : "";
    },
    cursorIndex() {
      return this.aCodes ? this.aCodes.length: 0;
    }
  },
   watch: {
      aCodes(newVal, oldVal) {
        if ((newVal.replace(/[^\d]/g, "") != newVal || (newVal && newVal.length > this.codeLength))) {
          this.aCodes = oldVal;
          return false;
        }
        this.$emit("update", newVal);
      },
      value(newVal, oldVal) {
        this.aCodes = newVal;
      }
    }  
};
</script>
<style lang="scss">
@import "../../theme-default/codeinput.scss";
</style>
