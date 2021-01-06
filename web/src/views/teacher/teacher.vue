<template>
  <div class="teacher">
    <hheader></hheader>
    <div class="teacher-box">
      <div class="video-div">
        <video autoplay :class="{'mirrorMode':mirrorModeState}" poster="../../assets/img/教师-学生的摄像头已关闭.png" id="localVideo"></video>
      </div>
      <div class="user-div">
        <userlist></userlist>
      </div>
    </div>
    <hfooter></hfooter>
  </div>
</template>

<script>
  import RtcEngine from "../../core/rtc-engine";
import Util from '../../core/utils/utils';
  export default {
    data() {
      return {};
    },
    created() {
      hvuex({
        isSwitchScreen: false
      });
    },
    mounted() {
      this.$nextTick(() => {
        this.registerCallBack();
        RtcEngine.instance.startPreview(document.getElementById("localVideo"));
        hvuex({
          currentChannel: this.$store.state.classNum
        })
        RtcEngine.instance.login(this.$store.state.classNum, this.$store.state.userName).then(re => {
          hvuex({
              isPublish: true
          });
        })
        window.rtcEngine = RtcEngine;
      });
    },
    computed: {
      mirrorModeState() {
        if (this.$store.state.isSwitchScreen) {
          return false;
        } else {
          if (!this.$store.state.isPreview) {
            return false;
          }
        }
        return true;
      }
    },
    methods: {
      init() {},
      // 注册回调
      registerCallBack() {
        RtcEngine.instance.registerCallBack(this.$store.state.classNum, (channel, eventName, data) => {
          if (eventName == "onJoin") {
            this.updateUserList(channel);
          } else if (eventName == "onPublisher") {
            this.updateUserList(channel);
          } else if (eventName == "onSubscribeResult") {
            RtcEngine.instance.setDisplayRemoteVideo(channel, data.userId, document.getElementById(data.userId), data.code);
          } else if (eventName == "onUserAudioMuted") {
            this.updateUserList(channel);
          } else if (eventName == "onUserVideoMuted") {
            this.updateUserList(channel);
          } else if (eventName == "onUnPublisher") {
            this.updateUserList(channel);
          } else if (eventName == "onLeave") {
            this.updateUserList(channel);
          } else if (eventName == "onBye") {
            this.$router.push("/");
          } else if (eventName == "onError") {
            Util.showErrorMsg(data);
          } else if (eventName == "onAudioLevel") {
            let list = [];
            data.forEach(element => {
              if (element.level >= 60) {
                list.push(element.userId);
              }
            });
            hvuex({
              isSpeakList: list
            })
          }
        })
      },
      // 更新小组用户列表
      updateUserList(channel) {
        let list = RtcEngine.instance.getUserList(channel);
        if (list) {
          hvuex({
            userList: list.filter(v => {
              return v.streamConfigs.length > 0
            })
          });
          hvuex({
            roomUserList: list
          });
        }
      }
    }
  };
</script>

<style lang="scss">
  .teacher {
    .teacher-box {
      width: 100%;
      display: flex;
      .video-div {
        flex: 1;
        height: vh(576);
        video {
          height: 100%;
          width: 100%;
          background-color: black;
        }
      }
    }
  }
</style>