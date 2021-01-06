package com.aliyun.rtc.superclassroom.api.impl;

/**
 * 获取网络请求的对象实例
 */
public class RTCSuperClassModelFactory {

    public static <T> T createLoader(Class<T> tClass) {
        T t = null;
        try {
            t = tClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static RTCSuperClassApiImpl createRTCVideoLiveApi() {
        return createLoader(RTCSuperClassApiImpl.class);
    }

}
