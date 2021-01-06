<template>
  <transition>
    <div class="hui-message-fade" :class="'hui-message--'+type">
      <i class="iconfont" :class="[{'icon-xinxi1':type=='info'},{'icon-zhengque':type=='success'},{'icon-xinxi':type=='warning'},{'icon-cuowu':type=='error'}]"></i>
      {{message}}</div>
  </transition>
</template>

<script  >
export default {
  data() {
    return {
      time: 3000, // 3秒后消失
      type: "info", // info,success,warning,error
      message: "" //需要提示用户的内容
    };
  },
  methods: {
    close() {
      setTimeout(() => {
        this.$el.remove();
      },this.time);
    }
  },
  mounted() {
     this.close();
  }
};
</script>
<style lang="scss">
@import "../../theme-default/message.scss";
</style>