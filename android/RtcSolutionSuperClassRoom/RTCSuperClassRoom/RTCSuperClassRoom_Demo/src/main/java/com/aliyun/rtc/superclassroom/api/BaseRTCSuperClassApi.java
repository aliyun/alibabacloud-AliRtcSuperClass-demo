package com.aliyun.rtc.superclassroom.api;


import com.aliyun.rtc.superclassroom.api.net.OkhttpClient;
import com.aliyun.rtc.superclassroom.bean.IResponse;

/**
 * 网络请求的方法
 */
public abstract class BaseRTCSuperClassApi {

    /**
     * 获取鉴权信息 get请求
     *
     * @param channelId 房间编号
     * @param userId       用户id
     * @param <T>       返回值泛型
     */
    public abstract <T> void getRtcAuth(String channelId, String userId, OkhttpClient.BaseHttpCallBack<IResponse<T>> callBack);

}
