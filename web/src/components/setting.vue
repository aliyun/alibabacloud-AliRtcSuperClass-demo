<template>
  <div class="setting">
    <p class="title">高级设置</p>
    <video autoplay class="video mirrorMode" id="testVideo"></video>
    <div class="hform-div">
      <span>摄像头</span>
      <hui-select v-model="currentCamera" @change="switchCamera" :label="currentCameraName">
        <hui-option v-for="(v,i) in videoDevices" :key="i" :value="v.deviceId" :label="v.label"></hui-option>
      </hui-select>
    </div>
    <div class="hform-div">
      <span>麦克风</span>
      <hui-select v-model="currentAudioCapture" @change="switchtAudioCapture" :label="currentAudioCaptureName">
        <hui-option v-for="(v,i) in audioDevices" :key="i" :value="v.deviceId" :label="v.label"></hui-option>
      </hui-select>
    </div>
    <div class="mic-show">
      <i class="iconfont icon-maikefeng"></i>
      <meter high="0.25" max="1" :value="level"></meter>
    </div>
    <hui-button type="primary" class="submit-button" @click="submit">完成设置</hui-button>
  </div>
</template>

<script>
  import RtcEngine from "../core/rtc-engine";
  export default {
    data() {
      return {
        level: 0,
        currentCamera: null,
        currentCameraName: null,
        currentAudioCapture: null,
        currentAudioCaptureName: null,
        audioDevices: [],
        videoDevices: [],
        instance: new WebrtcCheck()
      };
    },
    mounted() {
      this.$nextTick(() => {
        this.getDevices();
      });
    },
    methods: {
      // 预览
      startPreview() {
        RtcEngine.instance.startPreview(document.getElementById("testVideo"));
      },
      // 获取设备
      getDevices() {
        RtcEngine.instance.getDevices().then(re => {
          this.audioDevices = re.audioDevices;
          this.videoDevices = re.videoDevices;
          try {
            this.currentAudioCapture = this.audioDevices[0].deviceId;
            this.currentAudioCaptureName = this.audioDevices[0].label;
            this.testMic();
          } catch (error) {}
          this.currentCamera = this.videoDevices[0].deviceId;
          this.currentCameraName = this.videoDevices[0].label
          RtcEngine.instance.currentCamera(this.videoDevices[0].deviceId);
          RtcEngine.instance.currentAudioCapture(this.audioDevices[0].deviceId);
          this.startPreview();
        })
      },
      /**
       * 测试麦克风
       */
      testMic() {
        this.instance.startAudioInputLevel(re => {
           this.level = re;
        });
      },
      /**
       * 切换摄像头
       */
      switchCamera(deviceId) {
        RtcEngine.instance.stopPreview().then(() => {
          RtcEngine.instance.currentCamera(deviceId);
          this.startPreview();
        })
      },
      /**
       * 切换麦克风
       */
      switchtAudioCapture(deviceId) {
        RtcEngine.instance.stopPreview().then(() => {
          RtcEngine.instance.currentAudioCapture(deviceId);
          this.startPreview();
        })
      },
      /**
       * 完成设置
       */
      submit() {
        this.instance.stopAudioInputLevel();
        RtcEngine.instance.stopPreview().then(() => {
          hvuex({
            isShowSetting: ""
          });
        })
      }
    }
  };
</script>

<style lang="scss">
  .setting {
    z-index: 100;
    position: fixed;
    height: 100vh;
    width: vw(515);
    right: 0;
    top: 0;
    box-sizing: border-box;
    padding: vw(40) vw(155) vw(40) vw(58);
    background-color: white;
    box-shadow: vw(-5) 0 vw(5) rgba(136, 136, 136, 0.5);
    .title {
      font-size: vh(24);
      line-height: vh(33);
      color: #181818;
      font-weight: 800;
    }
    .video {
      margin: vh(12) 0 0;
      background-color: #d8d8d8;
      height: vh(163); // width: vh(302);
    }
    .hform-div {
      margin-top: vh(32);
      width: vh(302); // border-bottom: vw(1) solid $borderColor;
      span {
        font-size: vh(12);
        line-height: vh(17);
        color: #888888;
      }
      .hform-div-input {
        appearance: none;
        -moz-appearance: none;
        -webkit-appearance: none;
        outline: none;
        height: vh(32);
        font-size: vh(22);
        color: #181818;
        border: 0;
        background-color: transparent;
        background: url('../assets/img/jiantouxia.png') no-repeat right 5px center;
        background-size: 20px 20px;
        padding-right: vh(45);
        width: 100%;
      }
    }
    .mic-show {
      margin: vh(30) 0;
      .icon-maikefeng {
        font-size: vh(30);
        vertical-align: middle;
      }
      meter {
        width: vh(280);
      }
    }
    .submit-button {
      margin-top: vh(40);
      width: vh(302);
    }
  }
</style>