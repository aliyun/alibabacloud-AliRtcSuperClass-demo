package com.aliyun.rtc.superclassroom.rtc;


import android.view.ViewGroup;

import com.alivc.rtc.AliRtcEngine;
import com.alivc.rtc.AliRtcRemoteUserInfo;
import com.aliyun.rtc.superclassroom.bean.RTCUserInfo;

import java.util.List;

public abstract class BaseRTCSuperClass {


    /**
     * 获取单例
     */
    public static BaseRTCSuperClass sharedInstance() {
        return RTCSuperClassImpl.sharedInstance();
    }

    /**
     * 销毁实例
     */
    public abstract void destorySharedInstance();

    /**
     * 登录
     *
     * @param channelId 房间号
     * @param userName 昵称
     */
    public abstract void login(String channelId,  String userName);


    /**
     * 登出
     */
    public abstract void logout();


    /**
     * 切换是否停止发布本地视频
     * @param channelId  频道号
     * @param isMute 是否停止发布
     */
    public abstract void muteLocalCamera(String channelId,boolean isMute);


    /**
     * 切换是否停止发布本地音频
     *  @param channelId  频道号
     *  @param isMute 是否停止发布
     */
    public abstract void muteLocalMic(String channelId,boolean isMute);


    /**
     * 设置摄像头
     */
    public abstract void switchCamera();


    /**
     * 获取用户信息
     * @param channelId  频道号
     * @param userId 用户id
     */
    public abstract AliRtcRemoteUserInfo getUserInfo(String channelId, String userId);



    /**
     * 注册回调
     */
    public abstract void registerCallBack(RTCSuperClassCallback rtcSuperClassCallback);



    /**
     * 开始预览
     * @param viewGroup  显示画面的view
     */
    public abstract void startPreview(ViewGroup viewGroup);


    /**
     * 停止预览
     */
    public abstract void stopPreview();


    /**
     * 开始推流
     * @param channelId  频道号
     */
    public abstract void startPublish(String channelId);


    /**
     * 停止推流
     * @param channelId  频道号
     */
    public abstract void stopPublish(String channelId);

    /**
     * 设置远端画面
     * @param channelId  频道号
     * @param canvas canvas对象
     * @param track track对象
     */
    public abstract void setRemoteViewConfig(String channelId, AliRtcEngine.AliVideoCanvas canvas, String uid, AliRtcEngine.AliRtcVideoTrack track);

    /**
     * 获取远端用户列表
     * @param channelId  频道号
     */
    public abstract List<RTCUserInfo> getRemoteUserList(String channelId);

    /**
     * 是否入会
     * @param channelId  频道号
     */
    public abstract boolean isInCall(String channelId);

    /**
     * 是否预览
     * @param channelId  频道号
     */
    public abstract boolean isPreview(String channelId);
}

