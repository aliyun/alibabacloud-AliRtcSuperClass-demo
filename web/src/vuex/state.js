export default {
  version: "",
  classNum: "",// 教室号
  userName: '',// 用户名
  userId: null,// userId
  myUserInfo:{},
  role: null,// 角色
  isShowSetting: false,// 是否显示配置项
  isPublish: false,
  isPreview: false,
  isPublishScreen: false, //屏幕分享
  isSwitchScreen: false,// 是否切换大小流
  userList: [],// 连麦成员
  roomUserList:[],// 房间列表
  groupUserList:[],// 小组频道连麦用户
  supportInfo: {},
  isSpeakList:[],// 是否在说话
  currentChannel:null, // 当前房间
  checkGroup:[],// 
  groupId:null,// 
  groupList: [
    { name: "A组", value: "A" },
    { name: "B组", value: "B" },
    { name: "C组", value: "C" },
    { name: "D组", value: "D" },
    { name: "E组", value: "E" },
    { name: "F组", value: "F" },
    { name: "G组", value: "G" },
    { name: "H组", value: "H" },
    { name: "I组", value: "I" },
    { name: "J组", value: "J" }],
}
