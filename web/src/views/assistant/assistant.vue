<template>
  <div class="assistant">
    <hheader></hheader>
    <div class="assistant-main">
      <div class="teacher-div">
        <hvideo key="000000" :userInfo="$store.state.myUserInfo"></hvideo>
        <hvideo v-for="(v) in teacherUserList" :key="v.userId" :userInfo="v"></hvideo>
      </div>
      <div class="group-div">
        <h3>{{$store.state.checkGroup[currentIndex]}}组（{{checkGrouplist[$store.state.checkGroup[currentIndex]]?checkGrouplist[$store.state.checkGroup[currentIndex]].length:0}}人）
          <hui-button class="group-button" size="small" @click="publish($store.state.checkGroup[currentIndex])" :class="{'on':currentInstance==$store.state.checkGroup[currentIndex]}">
            <i slot="icon" class="group-button-icon" :style="currentInstance==$store.state.checkGroup[currentIndex]? 'background-image:url('+ switchOff +')' : 'background-image:url('+  switchOn +')'"></i> {{currentInstance==$store.state.checkGroup[currentIndex]?'断开连麦':'连麦小组'}}
          </hui-button>
        </h3>
        <div class="group-div-main">
          <hvideo v-for="(sv) in checkGrouplist[$store.state.checkGroup[currentIndex]]" :key="sv.userId" :userInfo="sv"></hvideo>
        </div>
      </div>
      <el-pagination class="nui-pagination" layout="prev, pager, next" :total="$store.state.checkGroup.length" :page-size="1" @current-change="currentChange"></el-pagination>
    </div>
    <hfooter></hfooter>
  </div>
</template>

<script>
  import RtcEngine from "../../core/rtc-engine";
  import Util from '../../core/utils/utils';
   import {
    Message
  } from "element-ui";
  export default {
    data() {
      return {
        teacherUserList: [],
        currentInstance: null,
        joinChannelList: [], // 入会数组
        checkGrouplist: {},
        subscribeList: [],
        beforeCurrentIndex: -1,
        currentIndex: 0,
        isFirst: true,
        switchOn: require("../../assets/img/switch-on.png"),
        switchOff: require("../../assets/img/switch-off.png"),
      }
    },
    created() {},
    mounted() {},
    methods: {
      // 初始化
      init() {
        this.registerCallBack(this.$store.state.classNum, "teacher");
        RtcEngine.instance.setAutoPublishSubscribe(this.$store.state.classNum, false, true);
        RtcEngine.instance.login(this.$store.state.classNum, this.$store.state.userName).then(() => {
          hvuex({
            myUserInfo: {
              userId: "myUserId",
              displayName: "我自己",
              streamConfigs: []
            }
          }).then(() => {
            RtcEngine.instance.startPreview(document.getElementById("myUserId"));
          })
          this.joinChannelList = [...this.$store.state.checkGroup];
          this.joinGroupChannel();
        })
      },
      // 加入小组
      joinGroupChannel() {
        if (this.joinChannelList.length > 0) {
          let element = this.joinChannelList.splice(0, 1);
          let chanenl = this.$store.state.classNum + "" + element;
          this.registerCallBack(chanenl, element);
          RtcEngine.instance.setAutoPublishSubscribe(chanenl, false, false);
          RtcEngine.instance.login(chanenl, this.$store.state.userName).then(() => {}).catch(err => {
            // UI 重试
            Util.loginRetry(chanenl, this.$store.state.userName);
          }).then(() => {
            this.joinGroupChannel();
          });
        }
      },
      // 连麦小组
      async publish(key) {
        if (this.currentInstance == key) {
          // 断开连麦
          RtcEngine.instance.stopPublish(this.$store.state.classNum + key);
          this.currentInstance = null;
          hvuex({
            isPublish: false
          })
        } else {
          if (this.currentInstance) {
            // 断开连麦
            await RtcEngine.instance.stopPublish(this.$store.state.classNum + this.currentInstance);
          }
          RtcEngine.instance.startPublish(this.$store.state.classNum + key).then(() => {
            this.currentInstance = key;
            hvuex({
              isPublish: true,
              currentChannel: this.$store.state.classNum + key
            })
          })
        }
      },
      // 注册回调
      registerCallBack(chanenl, key) {
        RtcEngine.instance.registerCallBack(chanenl, (room, eventName, data) => {
          if (this.$store.state.classNum == room) {
            if (eventName == "onJoin" || eventName == "onPublisher" || eventName == "onUnPublisher" || eventName == "onLeave" || eventName == "onUserVideoMuted") {
              let remoteUserList = RtcEngine.instance.getUserList(room);
              this.teacherUserList = remoteUserList.filter(item => {
                return item.streamConfigs.length > 0
              })
            }
          } else {
            if (eventName == "onJoin" || eventName == "onPublisher" || eventName == "onUnPublisher" || eventName == "onLeave") {
              let list = RtcEngine.instance.getUserList(room);
              list = list.filter(item=>{ return item.streamConfigs && item.streamConfigs.length>0 });
              this.$set(this.checkGrouplist, key, list);
            }
            if (eventName == "onUserVideoMuted") {
              let list = RtcEngine.instance.getUserList(room);
              list = list.filter(item=>{ return item.streamConfigs && item.streamConfigs.length>0 });
              this.$set(this.checkGrouplist, key, list);
            }
            if (eventName == "onPublisher") {
              let index = this.subscribeList.findIndex(item => {
                return item.room == room && item.userId == data.userId
              });
              if (index == -1) {
                this.subscribeList.push({
                  room: room,
                  userId: data.userId
                });
              }
              if (room == this.$store.state.classNum + "" + this.$store.state.checkGroup[this.currentIndex]) {
                RtcEngine.instance.subscribe(room, data.userId).then(() => {
                  RtcEngine.instance.setDisplayRemoteVideo(
                    room,
                    data.userId,
                    document.getElementById(data.userId),
                    1
                  );
                })
              }
            }
            if (eventName == "onUnPublisher") {
              this.deleUserByUserId(room, data.userId);
            }
            if (eventName == "onLeave") {
              this.deleUserByUserId(room, data.userId);
            }
          }
          if (eventName == "onBye") {
            this.deleUserByUserId(room, data.userId);
            Util.exitRoom();
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
          } else if (eventName == "onSubscribeResult") {
            let currentRoom = this.$store.state.classNum + this.$store.state.checkGroup[this.currentIndex]
            if (currentRoom == room || room == this.$store.state.classNum) {
              RtcEngine.instance.setDisplayRemoteVideo(
                room,
                data.userId,
                document.getElementById(data.userId),
                data.code
              );
            }
          }
        })
      },
      // 
      deleUserByUserId(room, userId) {
        index = this.subscribeList.findIndex(item => {
          return item.room == room && item.userId == data.userId
        });
        if (index > -1) {
          this.subscribeList.splice(index, 1);
        }
      },
      // 页面改变
      currentChange(currentIndex) {
        this.currentIndex = currentIndex -1;
      },
      // 分页显示
      changeShow(currentIndex, beforeCurrentIndex) {
        this.$nextTick(() => {
          let beforeCurrentRoom = this.$store.state.classNum + this.$store.state.checkGroup[beforeCurrentIndex];
          let beforeArr = this.subscribeList.filter(item => {
            return item.room == beforeCurrentRoom
          })
          if (Array.isArray(beforeArr)) {
            beforeArr.forEach((v, i) => {
              let userInfo = RtcEngine.instance.getUserInfo(v.room, v.userId);
              let index = userInfo.streamConfigs.findIndex(item => {
                return item.subscribed
              });
              if (index > -1) {
                RtcEngine.instance.unSubScribe(
                  v.room,
                  v.userId
                );
              }
            })
          }
          let currentRoom = this.$store.state.classNum + this.$store.state.checkGroup[currentIndex];
          let arr = this.subscribeList.filter(item => {
            return item.room == currentRoom
          })
          if (Array.isArray(arr)) {
            arr.forEach((v, i) => {
              let userInfo = RtcEngine.instance.getUserInfo(v.room, v.userId);
              if(userInfo.streamConfigs && userInfo.streamConfigs.length>0){
                RtcEngine.instance.subscribe(v.room, v.userId).then(() => {
                setTimeout(() => {
                  RtcEngine.instance.setDisplayRemoteVideo(
                    v.room,
                    v.userId,
                    document.getElementById(v.userId),
                    1
                  );
                }, 100)
              }).catch(err => {
                console.error(err);
                Message.error(err);
              }).then((data) => {})
              }
            })
          }
        })
      }
    },
    watch: {
      currentIndex(newVal, oldVal) {
        this.changeShow(newVal==-1?0:newVal, oldVal);
      }
    }
  }
</script>

<style lang="scss">
  .assistant {
    .teacher-div {
      margin: vh(14) vw(12);
      min-height: vh(156);
      display: flex;
      .nui-video-div {
        margin-right: vw(10);
      }
    }
    .assistant-main {
      overflow-y: auto;
      overflow-x: hidden;
      height: vh(576);
      position: relative;
      .nui-pagination {
        position: absolute;
        bottom: vh(20);
        right: vw(20);
      }
    }
    .group-div {
      margin: vh(14) vw(12);
      border: vh(1) solid #C1C1C1;
      padding: vh(14) 0;
      min-height: vh(156);
      >h3 {
        text-align: center;
        .group-button {
          float: right;
          margin-right: vw(20);
        }
        .group-button-icon {
          min-width: vh(20);
          height: vh(20);
          display: inline-block;
          background-size: 100% 100%;
          vertical-align: middle;
        }
        .on {
          background: #333333;
          color: white;
        }
      }
      .group-div-main {
        padding: vh(14) vw(12);
        display: flex;
        .nui-video-div {
          margin-right: vw(10);
        }
      }
    }
  }
</style>