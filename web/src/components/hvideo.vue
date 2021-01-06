<template>
  <div class="nui-video-div" :id="userInfo.userId+'Div'" :uu="$store.state.isSpeakList" :class="{'hivon':$store.state.isSpeakList.indexOf(userInfo.userId)>-1}">
    <img class="avatar" v-show="isMuteVideo()" src="../assets/img/教师-学生的摄像头已关闭.png" alt srcset />
    <video class="nui-video" :id="userInfo.userId"></video>
    <div class="nui-video-footer">
      <span class="user-name">{{userInfo.displayName}}</span>
    </div>
  </div>
</template>

<script>
  export default {
    props: ["userInfo"],
    data() {
      return {}
    },
    methods: {
      isMuteVideo() {
        if (this.userInfo&&Array.isArray(this.userInfo.streamConfigs)) {
          let index = this.userInfo.streamConfigs.findIndex((v) => {
            return v.label == "sophon_video_camera_large" && v.muted
          })
          if (index > -1) {
            return true;
          }
        }
        return false;
      }
    }
  }
</script>

<style lang="scss">
  .nui-video-div {
    margin-bottom: vh(10);
    position: relative;
    height: vh(128);
    width: vh(170.6666667);
    border: vh(4) solid transparent;
    .avatar {
      position: absolute;
      top: 0;
      left: 0;
      height: vh(128);
      width: vh(171);
    }
    .nui-video {
      width: 100%;
      height: 100%;
      background-color: black;
    }
    .nui-video-footer {
      position: absolute;
      bottom: vh(10);
      left: vh(10);
      width: 100%;
      .user-name {
        font-family: PingFangSC-Regular;
        font-size: vh(16);
        color: #FFFFFF;
        text-shadow: 0 vh(2) vh(4) rgba(0, 0, 0, 0.50);
        display: inline-block;
        width: 100%;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
    }
  }
  .hivon{
       border: vh(4) solid rgba(35,207,234,1);
  }
</style>