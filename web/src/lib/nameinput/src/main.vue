<template>
  <div class="hui-name-input">
    <span
      class="hui-name-input-label" v-if="focused||val">{{title}}</span>
    <input
      type="text"
      v-model="val"
      @focus="focused = true"
      @blur="focused = false"
      :placeholder="focused?'':placeholder"
      :class="{'on':focused||val}"
      autocomplete="off"
    />
  </div>
</template>
<script>
export default {
  name: "HuiNameInput",
  props: {
    placeholder: {
      type: String,
      default: ""
    },
    title: String,
    value: [String,Number]
  },
  data() {
    return {
      val: this.value,
      focused: false
    };
  },
  model: {
    prop: "value", //绑定的值，通过父组件传递
    event: "update" //自定义名 可以随便改
  },
  watch:{
    val(newVal,oldVal){
       this.$emit("update",newVal);
    }
  }
};
</script>
<style lang="scss">
@import "../../theme-default/nameinput.scss";
</style>
