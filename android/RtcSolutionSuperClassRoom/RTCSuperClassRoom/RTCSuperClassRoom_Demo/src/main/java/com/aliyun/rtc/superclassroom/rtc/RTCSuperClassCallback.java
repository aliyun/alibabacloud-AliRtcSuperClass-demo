package com.aliyun.rtc.superclassroom.rtc;

import com.alivc.rtc.AliRtcEngine;

public interface RTCSuperClassCallback {

   /**
     * 用户上线通知
     *
     * @param userId 用户id
     */
    void onJoin(String channelId, String userId);

    /**
     * 用户下线通知
     * @param channelId  频道号
     * @param userId 用户id
     */
    void onLeave(String channelId, String userId);

    /**
     * sdk报错
     *
     * @param channelId  频道号
     */
    void onOccurError(String channelId, int error);

    /**
     * sdk警告
     *
     * @param channelId  频道号
     */
    void onOccurWarning(String channelId, int error);

    /**
     * 房间被销毁的回调
     * @param channelId  频道号
     */
    void onRoomDestroy(String channelId);

    /**
     * sdk报错,需要销毁实例
     * @param channelId  频道号
     */
    void onSDKError(String channelId, int error);

    /**
     * 加入房间通知
     * @param channelId  频道号
     * @param result 0为成功 反之失败
     */
    void onJoinChannelResult(String channelId, int result);

    /**
     * 离开房间通知
     * @param channelId  频道号
     */
    void onLeaveChannelResult(String channelId, int result);

     /**
      * 网络状态回调
      *
      * @param channelId  频道号
      * @param aliRtcNetworkQuality1 下行网络质量
      * @param aliRtcNetworkQuality  上行网络质量
      * @param userId                     String  用户ID
      */
    void onNetworkQualityChanged(String channelId, String userId, AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality, AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality1);

    /**
     * 用户muteVideo通知
     * @param channelId  频道号
     */
    void onUserVideoMuted(String channelId, String userId, boolean mute);

    /**
     * 用户muteAudio通知
     * @param channelId  频道号
     */
    void onUserAudioMuted(String channelId, String userId, boolean mute);

    /**
     *
     * 订阅成功
     * @param channelId  频道号
     * @param userId  用户ID
     * @param result  0表示订阅成功，非0表示失败
     * @param videoTrack     订阅成功的视频流
     * @param audioTrack     订阅成功的音频流
     */
    void onSubscribeResult(String channelId, String userId, int result, AliRtcEngine.AliRtcVideoTrack videoTrack, AliRtcEngine.AliRtcAudioTrack audioTrack);

    /**
     *
     * 用户是否在说话
     * @param channelId  频道号
     * @param userId  用户ID
     * @param isSpeaking 是否正在说话
     */
    void onUserSpeaking(String channelId, String userId, boolean isSpeaking);

     /**
      *
      * 当订阅情况发生变化时，返回这个消息 onSubscribeChangedNotify
      * @param channelId  频道号
      * @param userId  用户ID
      * @param videoTrack     订阅成功的视频流
      * @param audioTrack     订阅成功的音频流
      */
    void onRemoteTraceAvaliable(String channelId, String userId, AliRtcEngine.AliRtcAudioTrack audioTrack, AliRtcEngine.AliRtcVideoTrack videoTrack);


}
