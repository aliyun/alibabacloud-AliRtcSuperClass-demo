import axios from 'axios';
import config from '../data/config'
export default {
    /**
     * 获取authinfo
     * @param {*} channel
     * @param {*} userName
     */
    getAuthInfo(channel, userName) {
        return new Promise((resolve, reject) => {
            let data = this.GenerateAliRtcAuthInfo(channel, userName);
            data.userName = userName;
            resolve(data);
            return;
            let url = `${config.baseUrl}?channelId=${channel}&user=${userName}&passwd=1234&room=${channel}`;
            //let url = `${config.baseUrl}?channel=${channel}&user=${userName}&passwd=1234`;
            axios.get(url).then(re => {
                console.warn("getAuthInfo",re.data.data);
                re.data.data.channel = channel;
                re.data.data.userName = userName;
                resolve(re.data.data);
            }).catch(err => {
                console.error(`channel:${channel}`, err);
                reject(err);
                alert(err);
            })
        })
    },
    /**
     * 生成鉴权信息
     * @param {*} channelId 
     */
    GenerateAliRtcAuthInfo(channelId) {
        let appId = config.appId;// 修改为自己的appid 该方案仅为开发测试使用，正式上线需要使用服务端的AppServer
        let appKey = config.appKey;// 修改为自己的appkey 该方案仅为开发测试使用，正式上线需要使用服务端的AppServer
        let timestamp = parseInt(new Date().getTime() / 1000 + 48 * 60 * 60);
        let nonce = 'AK-' + 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
            return v.toString(16);
        });
        let userId = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
            return v.toString(16);
        });
        let token = sha256(appId + appKey + channelId + userId + nonce + timestamp);
        return {
            appid:appId,
            userid:userId,
            timestamp:timestamp,
            nonce:nonce,
            token:token,
            gslb: ["https://rgslb.rtc.aliyuncs.com"],
            channel:channelId
        };
    }
}