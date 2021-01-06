import { Message, MessageBox } from 'element-ui';
import RtcEngine from "../rtc-engine";
import http from '../http/http';
import Role from '../data/role';
export default class Util {
    /**
     * 获取推流状态
     * @param {*} v 
     * @return {boolean} 
     */
    static getPublishState(v) {
        let arr = v.streamConfigs.filter(re => { return re.state == "active" });
        return arr.length > 0 ? true : false;
    }
    /**
     * 显示信息
     * @param {*} v 
     */
    static toast(v) {
        Message(v);
    }
    /**
     * 复制
     * @param {*} id 
     */
    static hCopy(id) {
        if (id) {
            try {
                var range = document.createRange();
                var tar = document.getElementById(id);
                range.selectNodeContents(tar);
                var selection = window.getSelection();
                selection.removeAllRanges();
                selection.addRange(range);
                document.execCommand('copy');
                selection.removeAllRanges();
            } catch (error) {
                console.log(error);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }
    /**
     * 
     * @param {*} id 
     */
    static inputCopy(id) {
        try {
            var Url2 = document.getElementById(id);
            Url2.select(); // 选择对象
            document.execCommand("Copy");
            return true;
        } catch (error) {
            console.error(error);
            return false;
        }
    }
    /**
     * 开始推流
     * @param {*} channel
     */
    static async startPublish(channel) {
        return RtcEngine.instance.startPublish(channel);
    }
    /**
  * 获取浏览器地址栏参数
  * @param {*} url 
  * @param {*} name 
  */
    static getUrlParam(name) {
        let url = window.location.href;
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
        let urlArr = url.split("?");
        if (urlArr.length > 1) {
            url = "?" + urlArr[1];
            var r = url.substr(1).match(reg);
            if (r != null) return decodeURIComponent(r[2]); return null;
        } else {
            return null;
        }
    }
    /**
     * 判断是否是移动设备
     */
    static isMobile() {
        var userAgentInfo = navigator.userAgent;
        var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");
        var flag = false;
        for (var v = 0; v < Agents.length; v++) {
            if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = true; break; }
        }
        return flag;
    }
    /**
     * 退出房间
     */
    static exitRoom() {
        RtcEngine.instance
            .logout()
            .finally(() => {
                hvuex({ isPublishScreen: false, isPublish: false, isPreview: RtcEngine.instance.isPreview });
                hv.$router.push("/");
            });
    }
    /**
     * 显示远端用户
     * @param {*} data 
     */
    static showRemoteVideo(data) {
        let video = document.getElementById(data.userId);
        let subUserId = document.getElementById("localVideo").getAttribute("subUserId");
        if (subUserId && subUserId == data.userId) {
            video = document.getElementById("localVideo");
        }
        RTCClient.instance.setDisplayRemoteVideo(data.userId, video, data.code);
        if (subUserId && subUserId == data.userId) {
            setTimeout(() => {
                document.getElementById(data.userId).srcObject = document.getElementById("localVideo").srcObject;
            }, 100);
        }
    }
    /**
     * 
     * @param {*} code 
     */
    static onByeMessage(code) {
        console.log(code);
        let messageTxt = "";
        if (code == 1) {
            messageTxt = "10分钟体验时间已到";
        } else if (code == 2) {
            messageTxt = "10分钟体验时间已到";
        } else {
            messageTxt = "同一个用户ID在其他端登录";
        }
        hv.$alert(messageTxt, "", {
            confirmButtonText: '确定',
            callback: action => {
                hv.$router.push("/");
            }
        });
    }
    /**
     * 显示错误
     */
    static showErrorMsg(data) {
        let resmsg = "";
        switch (data.errorCode) {
            case 10000:
                resmsg += "设备未知错误";
                break;
            case 10001:
                resmsg += "未找到音频设备";
                break;
            case 10002:
                resmsg += "未找到视频设备";
                break;
            case 10003:
                resmsg += "浏览器禁用音频设备";
                break;
            case 10004:
                resmsg += "浏览器禁用视频设备";
                break;
            case 10005:
                resmsg += "系统禁用音频设备";
                break;
            case 10006:
                resmsg += "系统禁用视频设备";
                break;
            case 10010:
                resmsg += "屏幕共享未知错误";
                break;
            case 10011:
                {
                    resmsg += "屏幕共享被禁用";
                    hvuex({ isPublishScreen: false });
                }
                break;
            case 10012:
                resmsg += "屏幕共享已取消";
                hvuex({ isPublishScreen: false });
                break;
            case 10201:
                resmsg += "自动播放失败";
                break;
            case 10300:
                break;
            default:
                break;
        }
        resmsg ? Util.toast(resmsg) : Util.toast(data);
    }
    /**
     * 根据角色Id获取角色名称
    */
    static getRoleNameByRole(val) {
        switch (val) {
            case 0:
                return "老师";
            case 1:
                return "学生";
            case 2:
                return "助教";
        }
    }
    /**
     * 根据UserId获取角色名称
     * @param {*} channel 
     * @param {*} userId 
     */
    static getRoleIdByUserId(channel, userId) {
        let userInfo = RtcEngine.instance.getUserInfo(channel, userId);
        if (userInfo.displayName) {
            if (userInfo.displayName.indexOf("_老师") > -1) {
                return Role.ROLE_TEACHER;
            } else if (userInfo.displayName.indexOf("_助教") > -1) {
                return Role.ROLE_ASSISTANT;
            } else if (userInfo.displayName.indexOf("_学生") > -1) {
                return Role.ROLE_STUDENT;
            }
        }
    }
    /**
     * 登录重试
     * @param {*} channel 
     * @param {*} userName 
     */
    static loginRetry(channel, userName) {
        MessageBox.confirm('频道加入失败', '', {
            confirmButtonText: '退出频道',
            cancelButtonText: '重试',
            type: 'warning'
        }).then(() => {
            Util.exitRoom();
        }).catch(() => {
            RtcEngine.instance.login(channel, userName).catch(err => {
                Util.loginRetry(channel, userName);
            })
        });
    }
}
