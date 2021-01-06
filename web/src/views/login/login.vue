<template>
  <div class="login" @click="hideSetting">
    <header class="login-header">
      <i class="iconfont icon-rtcyinshipintongxin"></i> 音视频通信 {{$store.state.version}}
      <i class="iconfont icon-shezhi" @click.stop="showSetting"></i>
    </header>
    <div class="login-card">
      <div class="login-card-left">
        <img src="../../assets/img/超级小班课插图.png" alt srcset />
      </div>
      <div class="login-card-right">
        <p class="title">超级小班课</p>
        <div class="button-group">
          <hui-button @click="swicthRole(0)" class="button-group-left" :class="role==0?'on':role==1?'loff':''" :round="false">老师</hui-button>
          <hui-button @click="swicthRole(1)" class="button-group-center" :class="role==1?'on':'off'" :round="false">学生</hui-button>
          <hui-button @click="swicthRole(2)" class="button-group-right" :class="role==2?'on':role==1?'roff':''" :round="false">助教</hui-button>
        </div>
        <div class="form">
          <hui-name-input class="form-input-class" placeholder="您的姓名" v-model="userName" title="姓名"></hui-name-input>
          <hui-code-input class="form-input-class" title="教室码" v-model="classNum" :disable="role==0" placeholder="教室码（5位数字）"></hui-code-input>
          <span class="name-span" v-show="groupId&&role==1">小组</span>
          <hui-select v-model="groupId" class="form-input-class" v-show="role==1" :label="$store.state.groupList[0].name">
            <hui-option v-for="(v,i) in $store.state.groupList" :key="i" :value="v.value" :label="v.name" :v="v">{{v.name}}</hui-option>
          </hui-select>
        </div>
        <hui-button type="primary" class="submit-button" :disabled="!classNum||classNum.length!=5||!userName" @click="onSubmit">进入教室</hui-button>
      </div>
    </div>
  </div>
</template>

<script>
  import Util from "../../core/utils/utils";
  import Role from '../../core/data/role';
  export default {
    data() {
      return {
        role: 0,
        userName: "",
        classNum: Math.random().toFixed(5).slice(-5),
        groupId: this.$store.state.groupList[0].value
      };
    },
    created() {
      hvuex({
        role: this.role
      });
    },
    mounted() {
      this.$nextTick(() => {})
    },
    methods: {
      /**
       * 登陆
       */
      onSubmit() {
        hvuex({
          role: this.role,
          classNum: this.classNum,
          userName: this.role === Role.ROLE_STUDENT ? this.groupId + "组_" + this.userName + "_" + Util.getRoleNameByRole(this.role) : this.userName + "_" + Util.getRoleNameByRole(this.role),
          groupId: this.groupId
        });
        if (this.role == Role.ROLE_TEACHER) {
          this.$router.push("/teacher");
        } else if (this.role == Role.ROLE_STUDENT) {
          this.$router.push("/student");
        } else {
          this.$router.push("/assistant");
        }
      },
      /**
       * 显示配置页
       */
      showSetting() {
        hvuex({
          isInRTC: false,
          isShowSetting: "setting"
        });
      },
      /**
       * 显示配置页
       */
      hideSetting() {
        hvuex({
          isShowSetting: ""
        });
      },
      swicthRole(n) {
        this.role = n;
        hvuex({
          role: n
        });
      }
    },
    watch: {
      role(newVal, oldVal) {
        newVal == 0 ? this.classNum = Math.random().toFixed(5).slice(-5) : this.classNum = "";
      }
    }
  };
</script>

<style lang="scss">
  .login {
    height: 100vh;
    overflow: hidden;
    position: relative;
    .login-header {
      margin: vh(32) vw(47) 0;
      font-size: vh(20);
      .icon-rtcyinshipintongxin {
        font-size: vh(40);
        vertical-align: middle;
        margin-right: vw(5);
      }
      .icon-shezhi {
        font-size: vh(31);
        cursor: pointer;
        float: right;
      }
    }
    .login-card {
      margin: vh(91) vw(110) 0;
      display: flex;
      .login-card-left {
        width: vw(380);
        img {
          width: 80%;
          height: auto;
          top: vh(-20);
          position: relative;
        }
      }
      .login-card-right {
        flex: 1;
        .title {
          font-size: vh(45);
          line-height: vh(62);
          letter-spacing: vw(2);
        }
        .button-group {
          margin-top: vh(42);
          width: vw(306);
          display: flex;
          .hui-button {
            flex: 1;
            background: #FFFFFF;
            border: 2px solid #C1C1C1;
            transition: 0s;
            color: #555555;
            span {
              font-size: vh(18) !important;
            }
          }
          .hui-button:nth-child(1) {
            border-top-left-radius: vh(24);
            border-bottom-left-radius: vh(24);
          }
          .hui-button:nth-child(3) {
            border-top-right-radius: vh(24);
            border-bottom-right-radius: vh(24);
          }
          .on {
            border: vh(2) solid #013ebe !important;
            background-color: #F6F9FF;
            color: #013ebe;
          }
          .loff {
            border-right: 0;
          }
          .off {
            border-left: 0;
            border-right: 0;
          }
          .roff {
            border-left: 0;
          }
        }
        .form {
          margin-top: vh(28);
          .form-input-class {
            margin-top: vh(25);
            width: vw(306);
          }
          .name-span {
            font-size: 12px;
            line-height: 2.36111vh;
            color: #888888;
          }
          .hui-select {
            margin-top: vh(15);
          }
        }
        .submit-button {
          margin-top: vh(30);
          width: vw(306);
        }
      }
    }
  }
</style>