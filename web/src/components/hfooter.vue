<template>
  <div class="hfooter">
    <div class="function" :class="{'student':$store.state.role==1}">
      <span class="vsersion">{{$store.state.version}}</span>
      <div class="mic" :class="{'on':isMuteLocalMic}" @click="muteLocalMic">
        <i :style="isMuteLocalMic ? 'background-image:url('+  micOff +')' : 'background-image:url('+  micOn +')'"></i>
        <span>{{isMuteLocalMic?'取消静音':'静音'}}</span>
      </div>
      <div class="camera" :class="{'on':isMuteLocalCamera}" @click="muteLocalCamera">
        <i :style="isMuteLocalCamera ? 'background-image:url('+  cameraOff +')' : 'background-image:url('+  cameraOn +')'"></i>
        <span>{{isMuteLocalCamera?'打开摄像头':'关闭摄像头'}}</span>
      </div>
      <div class="switchRole" :class="{'on':$store.state.currentChannel==$store.state.classNum}" v-if="$store.state.role==1" @click="studentLianMaiTeacher">
        <i :style="$store.state.currentChannel==$store.state.classNum? 'background-image:url('+ switchOff +')' : 'background-image:url('+  switchOn +')'"></i>
        <span>{{$store.state.currentChannel==$store.state.classNum?'断开老师':'连麦老师'}}</span>
      </div>
      <div class="screen" @click="publishScreen" v-if="$store.state.supportInfo.browser!='Safari' && $store.state.role==0">
        <i :style="'background-image:url(' + screenshare + ')'"></i>
        <span>{{$store.state.isPublishScreen?'取消屏幕共享':'屏幕共享'}}</span>
      </div>
      <div class="leaveclass" @click="exitRoom">
        <i name="icon" :style="isTeacherInitState&&$store.state.role==0 ? 'background-image:url('+ leave +')' : 'background-image:url('+ end +');background-color: rgba(245,34,45,0.15)'"></i>
        <span>{{isTeacherInitState&&$store.state.role==0?'结束课程':'离开教室'}}</span>
      </div>
    </div>
  </div>
</template>

<script>
  import RtcEngine from "../core/rtc-engine";
  import Util from "../core/utils/utils";
  import Role from '../core/data/role';
  export default {
    data() {
      return {
        isTeacherInitState: false, // 教师初始状态
        isMuteLocalMic: false, //
        isMuteLocalCamera: false, //
        micOn: require("../assets/img/mic-on.png"),
        micOff: require("../assets/img/mic-off.png"),
        cameraOn: require("../assets/img/camera-on.png"),
        cameraOff: require("../assets/img/camera-off.png"),
        start: require("../assets/img/start.png"),
        stop: require("../assets/img/stop.png"),
        switchOn: require("../assets/img/switch-on.png"),
        switchOff: require("../assets/img/switch-off.png"),
        screenshare: require("../assets/img/screen.png"),
        share: require("../assets/img/share.png"),
        leave: require("../assets/img/leave.png"),
        end: require("../assets/img/end.png")
      };
    },
    created() {},
    methods: {
      // 设置是否停止发布本地音频流
      muteLocalMic() {
        if (!this.$store.state.isPublish) {
          Util.toast("未推流");
          return false;
        }
        RtcEngine.instance.muteLocalMic(this.$store.state.currentChannel, !this.isMuteLocalMic);
        this.isMuteLocalMic = !this.isMuteLocalMic;
      },
      // 设置是否停止发布本地视频流
      async muteLocalCamera() {
        if (!this.$store.state.isPublish) {
          Util.toast("未推流");
          return false;
        }
        RtcEngine.instance.muteLocalCamera(this.$store.state.currentChannel, !this.isMuteLocalCamera);
        this.isMuteLocalCamera = !this.isMuteLocalCamera;
        if (this.$store.state.role == Role.ROLE_TEACHER) {
          if (this.isMuteLocalCamera) {
            await RtcEngine.instance.stopPreview(this.$store.state.currentChannel);
          } else {
            await RtcEngine.instance.startPreview(document.getElementById("localVideo"));
          }
          hvuex({
            isPreview: RtcEngine.instance.isPreview
          })
        }
      },
      // 推屏幕流
      publishScreen() {
        if (!this.$store.state.isPublish) {
          Util.toast("未推流");
          return false;
        }
        RtcEngine.instance.switchPublishScreen(this.$store.state.currentChannel).then(() => {
          hvuex({
            isPublishScreen: RtcEngine.instance.isPublishScreen(this.$store.state.currentChannel)
          });
        });
      },
      // 退出教室
      exitRoom() {
        if (this.$store.state.role == Role.ROLE_STUDENT) {
          this.$confirm("离开后，您可以用此教室码再次进入教室。", "您确定要离开教室吗？").then(() => {
            Util.exitRoom();
          }).catch(err => {})
        } else if (this.$store.state.role == Role.ROLE_TEACHER) {
          this.$confirm("结束课程后无法再次回到同一个教室，需要老师重新创建", "您确定要离开教室吗？").then(() => {
            Util.exitRoom();
          }).catch(err => {})
        } else {
          this.$confirm("离开后，您可以用此教室码再次进入教室。", "您确定要离开教室吗？").then(() => {
            Util.exitRoom();
          }).catch(err => {})
        }
      },
      // 连接老师
      studentLianMaiTeacher() {
        if (!this.checkTeacher()) {
          Util.toast("老师不在课堂");
          return false;
        }
        this.isMuteLocalMic = false;
        this.isMuteLocalCamera = false;
        RtcEngine.instance.stopPublish(this.$store.state.currentChannel).then(() => {
          hvuex({
            isPublishScreen: RtcEngine.instance.isPublishScreen(this.$store.state.currentChannel),
            currentChannel: this.$store.state.currentChannel == this.$store.state.classNum ? this.$store.state.classNum + this.$store.state.groupId : this.$store.state.classNum
          }).then(() => {
            RtcEngine.instance.startPublish(this.$store.state.currentChannel);
          });
        })
      },
      // 判断老师是否在教室
      checkTeacher() {
        let userList = RtcEngine.instance.getUserList(this.$store.state.classNum);
        let index = userList.findIndex(item => {
          return item.displayName.indexOf("_老师") > -1
        });
        if (index > -1) {
          return true;
        }
        return false;
      }
    },
    watch: {
      "$store.state.isPublish" (n, o) {
        if (!n) {
          this.isMuteLocalMic = false;
          this.isMuteLocalCamera = false;
        }
      }
    }
  };
</script>

<style lang="scss">
  .hfooter {
    position: fixed;
    left: 0;
    bottom: 0;
    width: 100%;
    height: vh(104);
    width: vw(1024);
    background-color: #f8f8f8;
    box-sizing: border-box;
    padding: vh(17) vw(32);
    #share {
      position: fixed;
      bottom: vh(-1111);
    }
    .function {
      margin-left: vw(238);
      display: flex;
      .vsersion {
        position: absolute;
        left: vw(10);
      }
       :hover {
        i {
          background-color: #bfbfbf;
        }
      }
      .on {
        i {
          background-color: #333333;
        }
      }
      .initstate {
        i {
          background-color: #013ebe;
        }
        i:hover {
          background-color: #0036a6;
        }
      }
      div {
        text-align: center;
        margin-right: vw(24);
        cursor: pointer;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        i {
          width: vw(100);
          height: vh(48);
          border-radius: vh(24);
          border-radius: 24px;
          background-color: #e5e5e5;
          margin-bottom: 12px;
          cursor: pointer;
          background-repeat: no-repeat;
          background-position: center center;
          background-size: vh(40) vh(40);
        }
        span {
          font-size: vh(16);
          line-height: vh(22);
          background-color: #f8f8f8;
          color: #2f2f2f;
        }
      }
      .startclass p {
        background-color: #013ebe;
        &:hover {
          background-color: #0036a6;
        }
      }
      .leaveclass {
        i {
          width: vh(48);
          height: vh(48);
          border-radius: 50%;
        }
      }
      .footer-setting {
        position: absolute;
        right: 0;
      }
    }
    .student {
      margin-left: vw(208);
    }
  }
</style>