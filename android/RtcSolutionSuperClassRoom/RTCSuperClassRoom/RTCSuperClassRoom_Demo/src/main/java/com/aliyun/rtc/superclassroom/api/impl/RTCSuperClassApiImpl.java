package com.aliyun.rtc.superclassroom.api.impl;


import com.aliyun.rtc.superclassroom.api.BaseRTCSuperClassApi;
import com.aliyun.rtc.superclassroom.api.net.OkHttpCientManager;
import com.aliyun.rtc.superclassroom.api.net.OkhttpClient;
import com.aliyun.rtc.superclassroom.bean.IResponse;
import com.aliyun.rtc.superclassroom.constants.Constants;

import java.util.HashMap;
import java.util.Map;


/**
 * 网络请求方法封装
 */
public class RTCSuperClassApiImpl extends BaseRTCSuperClassApi {

    @Override
    public <T> void getRtcAuth(String channelId, String userId, OkhttpClient.BaseHttpCallBack<IResponse<T>> callBack) {
        String url = Constants.getUrlRtcAuth();
        Map<String, Object> params = new HashMap<>();
        //params.put(Constants.NEW_TOKEN_PARAMS_KEY_USERID, userId);
        //params.put("room", channelId);
        //params.put("passwd", "1234");
        params.put("channelId", channelId);
        OkHttpCientManager.getInstance().doGet(url, params, callBack);
    }
}
