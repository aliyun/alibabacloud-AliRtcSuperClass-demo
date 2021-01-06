<template>
  <div class="hheader">
    <span class="hheader-userName">{{$store.state.role==0?$store.state.userName+"的课堂 | ":''}}</span>教室码：
    <span id="classNum">{{$store.state.classNum}}</span>
    <i class="iconfont icon-fuzhi" @click="copy"></i>
    <span class="classState" v-if="$store.state.role==0&&$store.state.isPublish">上课中</span>
    <span class="hheader-user-name">
        {{$store.state.role|roleNameFilter}}：{{$store.state.userName}}
      </span>
  </div>
</template>

<script>
  import Util from "../core/utils/utils";
  import {
    Message
  } from "element-ui";
  export default {
    data() {
      return {};
    },
    methods: {
      /**
       * 拷贝房间号
       */
      copy() {
        if (Util.hCopy("classNum")) {
          Message.info("复制成功");
        } else {
          Message.error("复制失败");
        }
      }
    }
  };
</script>

<style lang="scss">
  .hheader {
    height: vh(40);
    line-height: vh(40);
    width: vw(1024);
    background-color: #000000;
    text-align: center;
    color: white;
    font-size: vh(22);
    overflow: hidden;
    .hheader-userName {
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        display: inline-block;
        width: vw(100);
        vertical-align: bottom;
    }
    .icon-fuzhi {
      font-size: vh(25);
      cursor: pointer;
      margin-left: vw(5);
    }
    .classState {
      margin-left: vw(20);
      height: vh(26);
      padding: 0 vw(10);
      background: #d2e1ff;
      border-radius: vh(24);
      background-color: #d2e1ff;
      font-size: vh(14);
      color: #013ebe;
      vertical-align: bottom;
    }
    .hheader-user-name {
      float: right;
      margin-right: vw(20);
      line-height: vh(40);
      font-size: vh(12);
      i{
        font-size: vh(25);
         vertical-align: bottom;
      }
    }
  }
</style>