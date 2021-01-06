<template>
  <div class="student">
    <hheader></hheader>
    <div class="student-box" id="student-box">
      <div class="video-div">
        <video autoplay :class="{'mirrorMode':mirrorModeState}" poster="../../assets/img/教师-学生的摄像头已关闭.png" id="localVideo"></video>
      </div>
      <div class="user-div on">
        <userlist></userlist>
      </div>
    </div>
    <hfooter></hfooter>
  </div>
</template>

<script>
  import RtcEngine from "../../core/rtc-engine";
  import Util from "../../core/utils/utils";
  import Role from '../../core/data/role';
  export default {
    data() {
      return {
        teacherList: [],
        gropList: []
      };
    },
    created() {
      hvuex({
        isSwitchScreen: true
      });
      RtcEngine.instance.setAutoPublishSubscribe(this.$store.state.classNum, false, true);
      this.registerCallBack(this.$store.state.classNum);
      RtcEngine.instance.login(this.$store.state.classNum, this.$store.state.userName);
      hvuex({
        currentChannel: this.$store.state.classNum + this.$store.state.groupId,
        myUserInfo:{ userId:"myUserId", displayName:"我自己", streamConfigs:[] }
      });
      RtcEngine.instance.setAutoPublishSubscribe(this.$store.state.classNum + this.$store.state.groupId, false, true);
      this.registerCallBack(this.$store.state.classNum + this.$store.state.groupId);
    },
    mounted() {
      this.$nextTick(() => {
        RtcEngine.instance.login(this.$store.state.classNum + this.$store.state.groupId, this.$store.state.userName).then(() => {
          RtcEngine.instance.startPreview(document.getElementById("myUserId"));
          Util.startPublish(this.$store.state.classNum + this.$store.state.groupId).then(() => {
            hvuex({
              isPublish: true
            });
          });
        })
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
      /**
       * 注册回调
       * @param {*} channel
       */
      registerCallBack(channel) {
        RtcEngine.instance.registerCallBack(channel, (room, eventName, data) => {
          console.log(room, eventName, data);
          if (eventName == "onJoin" || eventName == "onPublisher" || eventName == "onUnPublisher" || eventName == "onLeave" || eventName == "onUserVideoMuted") {
            this.updateUserList(channel);
          } else if (eventName == "onSubscribeResult") {
            this.setDisplayRemoteVideo(channel, data);
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
      // 更新用户列表
      updateUserList(channel) {
        if (channel == this.$store.state.classNum) {
          let userList = RtcEngine.instance.getUserList(channel).filter(v => {
            return v.streamConfigs.length > 0 && v.displayName.indexOf("_老师") == -1;
          })
          hvuex({
            userList: userList
          });
        } else {
          hvuex({
            roomUserList: RtcEngine.instance.getUserList(channel),
            groupUserList: RtcEngine.instance.getUserList(channel).filter(v => {
              return v.streamConfigs.length > 0;
            })
          })
        }
      },
      // 显示远端视频
      setDisplayRemoteVideo(channel, data) {
        let roleId = Util.getRoleIdByUserId(channel, data.userId);
        console.error(channel, data, roleId);
        if (roleId == Role.ROLE_TEACHER) {
          RtcEngine.instance.setDisplayRemoteVideo(channel, data.userId, document.getElementById("localVideo"), data.code);
        } else {
          RtcEngine.instance.setDisplayRemoteVideo(channel, data.userId, document.getElementById(data.userId), data.code);
        }
      }
    }
  };
</script>

<style lang="scss">
  .student {
    .student-box {
      width: 100%;
      display: flex;
      height: vh(720-40-104);
      .video-div {
        flex: 1;
        height: vh(576);
        background-color: black;
        video {
          height: 100%;
          width: 100%;
        }
      }
      .on {
        width: 206px;
      }
    }
  }
</style>