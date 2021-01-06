<template>
  <div class="assistant-manage">
    <hheader></hheader>
    <h3>请选择要管理的小组，每个助教最多可管理3个小组</h3>
    <ul class="main">
      <li v-for="(v,i) in list" :key="i" :index="index.indexOf(v)" :class="{'on':index.indexOf(v)>-1}" @click="select(v)">
        <span> <i class="iconfont icon-ziyuanldpi"></i> </span> {{v}}组
      </li>
    </ul>
    <footer>
      <hui-button type="primary" size="small" class="submit-button" @click="enterRoom">开始管理</hui-button>
      <hui-button size="small" class="submit-button" @click="exitRoom">离开教室</hui-button>
    </footer>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        index: [],
        list: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J']
      }
    },
    methods: {
      // 选择
      select(v) {
        console.log(v);
        let index = this.index.findIndex(item => {
          return item == v
        });
        if (index > -1) {
          this.index.splice(index, 1);
        } else {
          if (this.index.length >= 3) {
            this.$message("每个助教最多可管理3个小组");
            return false;
          }
          this.index.push(v);
        }
      },
      // 开始管理 
      enterRoom() {
        if (this.index.length == 0) {
          this.$message.error("请选择管理的小组");
          return false;
        }
        hvuex({
          checkGroup: this.index
        });
        this.$parent.toUrl();
      },
      // 退出教室
      exitRoom() {
        this.$router.push("/");
      }
    }
  }
</script>

<style lang="scss">
  .assistant-manage {
    h3 {
      text-align: center;
      font-size: vh(14);
      margin: vh(32) 0 vh(24) 0;
    }
    .main {
      display: grid;
      margin: 0 vw(71) 0;
      grid-template-columns: 1fr 1fr 1fr 1fr;
      grid-row-gap: vh(32);
      justify-items: center;
      li {
        border: vh(2) solid #C1C1C1;
        border-radius: vh(8);
        width: vw(196);
        height: vh(136);
        text-align: center;
        line-height: vh(136);
        background-color: white;
        font-size: vh(24);
        position: relative;
        user-select: none;
        span {
          visibility: hidden;
          position: absolute;
          right: vw(17);
          top: vh(21);
          line-height: 1;
        }
      }
      .on {
        border: vh(2) solid #013EBE;
        background-color: #F6F9FF;
        span {
          visibility: visible;
        }
      }
    }
    footer {
      position: fixed;
      bottom: 0;
      width: 100%;
      height: vh(69);
      line-height: vh(69);
      text-align: right;
      background: #F7F7F7;
      box-shadow: 0 vh(-2) vh(4) 0 rgba(0, 0, 0, 0.10);
      background-color: #F7F7F7;
      .hui-button {
        margin-right: vw(16);
      }
    }
  }
</style>