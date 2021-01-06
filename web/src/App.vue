<template>
  <div id="app">
    <router-view></router-view>
    <component :is="$store.state.isShowSetting"></component>
  </div>
</template>

<script>
  import RtcEngine from "./core/rtc-engine";
  export default {
    data() {
      return {};
    },
    name: "app",
    components: {},
    created() {
      window.rtcEngine = RtcEngine.instance;
      RtcEngine.instance.isSupport().then(re => {
        hvuex({
          supportInfo: re
        })
      });
    }
  };
</script>

<style lang="scss">
  #app {
    font-family: "Avenir", Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
  }
  .mirrorMode {
    transform: rotateY(180deg);
  }
</style>
