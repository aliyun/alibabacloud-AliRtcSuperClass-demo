package com.aliyun.rtc.superclassroom.api.net;

import java.util.Map;

import okhttp3.RequestBody;

public class OkHttpCientManager {

    private OkhttpClient okhttpClient;

    private OkHttpCientManager() {
        if (okhttpClient == null) {
            okhttpClient = new OkhttpClient();
        }
    }

    private static final class OkHttpCientManagerInstance {
        private static final OkHttpCientManager INSTANCE = new OkHttpCientManager();
    }

    public static OkHttpCientManager getInstance() {
        return OkHttpCientManagerInstance.INSTANCE;
    }

    public void doGet(String url, Map<String, Object> params, final OkhttpClient.BaseHttpCallBack callBack) {
        if (okhttpClient != null) {
            okhttpClient.doGet(url, params, callBack);
        }
    }

    public void doPost(String url, RequestBody body, final OkhttpClient.BaseHttpCallBack callBack) {
        if (okhttpClient != null) {
            okhttpClient.doPost(url, body, callBack);
        }
    }
}
