import RTCClient from "./rtc-client"
export default class RtcEngine {
  constructor() {
    this._channelInstance = {
      _clinet: new RTCClient()
    }; // 房间实例对象
    this._currentCamera = null; // 指定摄像头设备
    this._currentAudioCapture = null; // 指定麦克风设备
    this._callback = {};// 回调记录
    this._currentChannel = null; // 当前房间
  }
  /**
   * 获取实例
   */
  static get instance() {
    if (!RtcEngine._instance) {
      RtcEngine._instance = new RtcEngine();
    }
    return RtcEngine._instance;
  }
  /**
   * 获取预览状态
   */
  get isPreview() {
    return this._channelInstance._clinet.isPreview;
  }
  /**
   * 获取推流状态
   * @param {*} channel 
   */
  isPublish(channel) {
    if (this._checkChannel(channel, 2)) {
      return this._channelInstance[channel].isPublish;
    }
    return false;
  }
  /**
   * 获取屏幕分享推流状态
   * @param {*} channel 
   */
  isPublishScreen(channel) {
    if (this._checkChannel(channel, 2)) {
      return this._channelInstance[channel].isPublishScreen;
    }
    return false;
  }

  /**
   * 检测浏览器是否支持RTC SDK
   */
  async isSupport() {
    return this._channelInstance._clinet.isSupport();
  }
  /**
    * 获取设备信息
    */
  async getDevices() {
    return this._channelInstance._clinet.getDevices();
  }
  /**
   * 指定摄像头
   * @param {*} deviceId 
   */
  currentCamera(deviceId) {
    if (!deviceId) { return false }
    this._currentCamera = deviceId;
    this._channelInstance._clinet.currentCamera(deviceId);
    return true;
  }
  /**
   * 指定麦克风
   * @param {*} deviceId 
   */
  currentAudioCapture(deviceId) {
    if (!deviceId) { return false }
    this._currentAudioCapture = deviceId;
    this._channelInstance._clinet.currentAudioCapture(deviceId);
    return true;
  }
  /**
   * 开始预览
   * @param {*} video 
   */
  async startPreview(video) {
    return this._channelInstance._clinet.startPreview(video);
  }
  /**
   * 停止预览
   * @param {*} video 
   */
  async stopPreview(video) {
    return this._channelInstance._clinet.stopPreview(video);
  }
  /**
   * 设置是否自动推流和自动订阅 默认自动推流自动订阅
   * @param {*} channel 
   * @param {*} autoPub 
   * @param {*} autoSub 
   */
  setAutoPublishSubscribe(channel, autoPub, autoSub) {
    if (this._checkChannel(channel)) {
      console.warn("setAutoPublishSubscribe");
      return this._channelInstance[channel].setAutoPublishSubscribe(autoPub, autoSub);
    }
    return false;
  }
  /**
   * 注册回调
   * @param {*} channel 
   * @param {*} callback 
   */
  registerCallBack(channel, callback) {
    if (!this._callback.hasOwnProperty(channel)) {
      this._callback[channel] = [];
    }
    this._callback[channel] = [callback];
  }
  /**
   * 加入房间
   * @param {*} channel 
   * @param {*} userName 
   */
  async login(channel, userName) {
    if (!this._channelInstance.hasOwnProperty(channel)) {
      this._channelInstance[channel] = new RTCClient();
      this._initInstance(channel);
    }
    return this._channelInstance[channel].login(channel, userName);
  }
  /**
   * 开始推流
   * @param {*} channel 
   */
  async startPublish(channel){
    let re = this._checkChannel(channel);
    if(await re){
      return this._channelInstance[channel].startPublish();
    }
    return this._checkChannel(channel);
  }
  /**
   * 停止推流
   * @param {*} channel 
   */
  async stopPublish(channel) {
    let re = this._checkChannel(channel);
    if(await re){
      return this._channelInstance[channel].stopPublish();
    }
    return re;
  }
  /**
   * 设置是否停止发布本地音频
   * @param {*} channel 
   * @param {*} enable 
   */
  muteLocalMic(channel, enable) {
    if (this._checkChannel(channel, 2)) {
      return this._channelInstance[channel].muteLocalMic(enable);
    }
    return false;
  }
  /**
   * 设置是否停止发布本地视频
   * @param {*} channel 
   * @param {*} enable 
   */
  muteLocalCamera(channel, enable) {
    if (this._checkChannel(channel, 2)) {
      return this._channelInstance[channel].muteLocalCamera(enable);
    }
    return false;
  }
  /**
   * 切换开启和停止屏幕流
   * @param {*} channel 
   * @param {*} enable 
   */
  async switchPublishScreen(channel, enable) {
    if (await this._checkChannel(channel)) {
      return this._channelInstance[channel].switchPublishScreen(enable);
    }
    return this._checkChannel(channel);
  }
  /**
   * 取消订阅
   * @param {*} channel 
   * @param {*} userId 
   */
  async unSubScribe(channel,userId){
    if (await this._checkChannel(channel)) {
      return this._channelInstance[channel].unSubScribe(userId);
    }
    return this._checkChannel(channel);
  }
  /**
   * 订阅
   * @param {*} channel 
   * @param {*} userId 
   */
  async subscribe(channel,userId){
    if (await this._checkChannel(channel)) {
      return this._channelInstance[channel].subscribe(userId);
    }
    return this._checkChannel(channel);
  }
  /**
   * 设置远端渲染
   * @param {*} channel 
   * @param {*} userId 
   * @param {*} video 
   * @param {*} streamType 
   */
  setDisplayRemoteVideo(channel, userId, video, streamType) {
    if (this._checkChannel(channel, 2)) {
      return this._channelInstance[channel].setDisplayRemoteVideo(userId, video, streamType);
    }
    return false;
  }
  /**
   * 获取判断用户列表
   * @param {*} channel 
   * @return { array | boolean }
   */
  getUserList(channel) {
    if (this._checkChannel(channel, 2)) {
      return this._channelInstance[channel].getUserList();
    }
    return false;
  }
  /**
   * 获取用户信息
   * @param {*} channel 
   * @param {*} userId 
   */
  getUserInfo(channel, userId) {
    if (this._checkChannel(channel, 2)) {
      return this._channelInstance[channel].getUserInfo(userId);
    }
    return false;
  }
  /**
   * 离开房间  修改去掉channel
   */
  async logout() {
    for (const key in this._channelInstance) {
      if(this._channelInstance[key].isInCall){
         this._channelInstance[key].leaveChannel();
      }
    }
  }
  /**
   * 初始化 RTCClient
   */
  _initInstance(channel) {
    if (this._currentAudioCapture) {
      this._channelInstance[channel].currentAudioCapture(this._currentAudioCapture)
    }
    if (this._currentCamera) {
      this._channelInstance[channel].currentCamera(this._currentCamera)
    }
    // console.error(this._channelInstance[channel]);
    this._channelInstance[channel].registerCallBack((eventName, data) => {
      this._callback[channel].forEach(func => {
        func(channel, eventName, data);
      });
    });
  }
  /**
   * 检测房间是否存在
   * @param {*} channel 
   * @param {*} type 
   */
  _checkChannel(channel, type = 1) {
    if (type == 1) {
      return new Promise((resolve, reject) => {
        if (!this._channelInstance.hasOwnProperty(channel)) {
          this._channelInstance[channel] = new RTCClient();
          this._initInstance(channel);
          resolve(true);
        } else {
          resolve(true);
        }
      })
    } else {
      if (!this._channelInstance.hasOwnProperty(channel)) {
        this._channelInstance[channel] = new RTCClient();
        this._initInstance(channel);
        return true;
      }
      return true;
    }
  }
}