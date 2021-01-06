import Vue from "vue";
import App from "./App.vue";
import router from "./router/index";
import store from "./vuex/index";

//自定义组件(components文件夹下)
import "./components/global.js";

import "./assets/js/filter.js";
// eslint-disable-next-line no-undef
store.state.config = AppConfig;

Vue.config.productionTip = false;

// import './plugins/element.js'
import './assets/js/hui-design.js'
import {
  Pagination } from "element-ui"
  Vue.use(Pagination);

window.hv=new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
