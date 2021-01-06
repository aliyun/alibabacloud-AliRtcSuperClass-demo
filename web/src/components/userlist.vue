<template>
  <div class="user-list">
    <div class="title">
      <p :class="{'on':showModel==0}" @click="showModel=0">连麦列表</p>
      <p v-if="$store.state.role == 0" :class="{'on':showModel==1}" @click="showModel=1">课堂成员
        ({{$store.state.role == 0?$store.state.roomUserList.length+1:$store.state.roomUserList.length}})</p>
      <p v-else :class="{'on':showModel==1}" @click="showModel=1">小组成员({{$store.state.roomUserList.length+1}})</p>
    </div>
    <ul class="list-video" v-show="showModel==0">
      <hvideo v-if="$store.state.role == 1"  key="000000" :userInfo="$store.state.myUserInfo"></hvideo>
      <hvideo v-for="(v) in $store.state.userList" :key="v.hvideo" :userInfo="v"></hvideo>
      <hvideo v-for="(v) in $store.state.groupUserList" :key="'group'+v.hvideo" :userInfo="v"></hvideo>
    </ul>
    <ul class="list-txt" v-show="showModel==1">
      <li>
        <p class="onlyname">{{$store.state.userName}}(我自己)</p>
      </li>
      <li class="li-header">助教</li>
      <li v-for="(v,i) in userSort($store.state.roomUserList).assistant" :key="'assistant'+i">
        <p class="onlyname">{{v.displayName}}</p>
      </li>
      <li class="li-header" v-show="($store.state.role == 1 && $store.state.groupId == 'A')||$store.state.role == 0">A组</li>
      <li v-for="(v,i) in userSort($store.state.roomUserList).A" :key="'A'+i">
        <p class="onlyname">{{v.displayName}}</p>
      </li>
      <li class="li-header" v-show="($store.state.role == 1 && $store.state.groupId == 'B')||$store.state.role == 0">B组</li>
      <li v-for="(v,i) in userSort($store.state.roomUserList).B" :key="'B'+i">
        <p class="onlyname">{{v.displayName}}</p>
      </li>
      <li class="li-header" v-show="($store.state.role == 1 && $store.state.groupId == 'C')||$store.state.role == 0">C组</li>
      <li v-for="(v,i) in userSort($store.state.roomUserList).C" :key="'C'+i">
        <p class="onlyname">{{v.displayName}}</p>
      </li>
      <li class="li-header" v-show="($store.state.role == 1 && $store.state.groupId == 'D')||$store.state.role == 0">D组</li>
      <li v-for="(v,i) in userSort($store.state.roomUserList).D" :key="'D'+i">
        <p class="onlyname">{{v.displayName}}</p>
      </li>
      <li class="li-header" v-show="($store.state.role == 1 && $store.state.groupId == 'E')||$store.state.role == 0">E组</li>
      <li v-for="(v,i) in userSort($store.state.roomUserList).E" :key="'E'+i">
        <p class="onlyname">{{v.displayName}}</p>
      </li>
      <li class="li-header" v-show="($store.state.role == 1 && $store.state.groupId == 'F')||$store.state.role == 0">F组</li>
      <li v-for="(v,i) in userSort($store.state.roomUserList).F" :key="'F'+i">
        <p class="onlyname">{{v.displayName}}</p>
      </li>
      <li class="li-header" v-show="($store.state.role == 1 && $store.state.groupId == 'G')||$store.state.role == 0">G组</li>
      <li v-for="(v,i) in userSort($store.state.roomUserList).G" :key="'G'+i">
        <p class="onlyname">{{v.displayName}}</p>
      </li>
      <li class="li-header" v-show="($store.state.role == 1 && $store.state.groupId == 'H')||$store.state.role == 0">H组</li>
      <li v-for="(v,i) in userSort($store.state.roomUserList).H" :key="'H'+i">
        <p class="onlyname">{{v.displayName}}</p>
      </li>
      <li class="li-header" v-show="($store.state.role == 1 && $store.state.groupId == 'I')||$store.state.role == 0">I组</li>
      <li v-for="(v,i) in userSort($store.state.roomUserList).I" :key="'I'+i">
        <p class="onlyname">{{v.displayName}}</p>
      </li>
      <li class="li-header" v-show="($store.state.role == 1 && $store.state.groupId == 'J')||$store.state.role == 0">J组</li>
      <li v-for="(v,i) in userSort($store.state.roomUserList).J" :key="'J'+i">
        <p class="onlyname">{{v.displayName}}</p>
      </li>
    </ul>
  </div>
</template>

<script>
  import RTCClient from "../core/rtc-client";
  import Util from '../core/utils/utils';
  export default {
    data() {
      return {
        showModel: 0,
        myUserId: null,
        subUserId: null
      };
    },
    mounted() {
      this.$nextTick(() => {})
    },
    methods: {
      /**
       * 组员分类
       */
      userSort(roomUserList) {
        let roomUserListSort = {
          assistant: [],
          A: [],
          B: [],
          C: [],
          D: [],
          E: [],
          F: [],
          G: [],
          H: [],
          I: [],
          J: []
        }
        roomUserList.forEach((v, i) => {
          if (v.displayName.indexOf("_助教") > -1 || v.displayName.indexOf("_老师") > -1) {
            roomUserListSort.assistant.push(v);
          } else if (v.displayName.indexOf("A组_") > -1) {
            roomUserListSort.A.push(v);
          } else if (v.displayName.indexOf("B组_") > -1) {
            roomUserListSort.B.push(v);
          } else if (v.displayName.indexOf("C组_") > -1) {
            roomUserListSort.C.push(v);
          } else if (v.displayName.indexOf("D组_") > -1) {
            roomUserListSort.D.push(v);
          } else if (v.displayName.indexOf("E组_") > -1) {
            roomUserListSort.E.push(v);
          } else if (v.displayName.indexOf("F组_") > -1) {
            roomUserListSort.F.push(v);
          } else if (v.displayName.indexOf("G组_") > -1) {
            roomUserListSort.G.push(v);
          } else if (v.displayName.indexOf("H组_") > -1) {
            roomUserListSort.H.push(v);
          } else if (v.displayName.indexOf("I组_") > -1) {
            roomUserListSort.I.push(v);
          } else if (v.displayName.indexOf("J组_") > -1) {
            roomUserListSort.J.push(v);
          }
        });
        return roomUserListSort;
      }
    }
  };
</script>

<style lang="scss">
  .user-list {
    //width: vw(218);
    width: 210px;
    height: vh(720-40-104); //position: fixed;
    overflow-x: hidden;
    overflow-y: auto;
    top: 40px;
    right: 0;
    bottom: 104px;
    padding: 0 13px;
    box-sizing: border-box;
    .title {
      margin: 12px 0 12px 0;
      font-size: 14px;
      display: flex;
      text-align: center;
      cursor: pointer;
      p {
        position: relative;
        flex: 1;
        padding: 10px 0;
      }
      p::after {
        display: block;
        content: "";
        height: 1px;
        background-color: black;
        margin-top: 20px;
      }
      .on::after {
        height: 4px;
        background-color: #013ebe;
        margin-top: 18px;
      }
    }
    .list-video {
      overflow-x: scroll;
      height: vh(720-40-104-80);
      li {
        position: relative;
        $height: 126px; // 31.1vh;
        height: $height;
        overflow: hidden;
        margin-bottom: 10px;
        .avatar {
          position: absolute;
          text-align: center;
          top: 0;
          width: 100%;
          line-height: $height;
          bottom: 0;
          color: #333333;
          background-image: url("../assets/img/教师-学生的摄像头已关闭.png");
          background-position: center;
          background-size: contain;
          background-repeat: no-repeat;
          background-color: rgba(89, 89, 89, $alpha: 1);
          img {
            display: none;
          }
        }
        video {
          position: absolute;
          top: 0;
          width: 100%;
          height: $height;
          background-color: transparent;
        }
        .name {
          position: absolute;
          bottom: 10px;
          left: 10px;
          font-size: 18px;
          color: white;
        }
      }
    }
    .list-txt {
      .li-header {
        text-indent: 20px;
        background-color: #C1C1C1;
        opacity: 0.38;
        height: vh(24);
        line-height: vh(24);
        margin: vh(5) 0;
        color: #2F2F2F;
        font-size: vh(12);
      }
      .onlyname{
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
      p {
        line-height: 48px;
        cursor: pointer;
        text-indent: 10px;
      }
      p:hover {
        background-color: #d2e1ff;
      }
    }
  }
</style>