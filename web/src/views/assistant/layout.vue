<template>
  <div class="layout">
    <manage ref="manage" v-show="showIndex"></manage>
    <assistant ref="assistant" v-show="!showIndex" @toUrl="toUrl"></assistant>
  </div>
</template>
<script>
import assistant from "./assistant";
import manage from "./manage";
export default {
  data() {
    return {
      showIndex: true
    };
  },
  methods: {
    toUrl() {
      this.showIndex = !this.showIndex;
      this.$refs.assistant.init();
    }
  },
  components: {
    assistant,
    manage
  }
};
</script>