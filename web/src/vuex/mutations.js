// 更改 Vuex 的 store 中的状态的唯一方法是提交 mutation
// this.$store.commit(method, params)


export default {
  //修改公共参数
  hvuex(state, v) {
    var obj = Object.assign({...state}, v);
    for (const key in obj) {
      state[key]=obj[key];
    }
    hsetStore("hvuex",JSON.stringify(state));
  }
}

