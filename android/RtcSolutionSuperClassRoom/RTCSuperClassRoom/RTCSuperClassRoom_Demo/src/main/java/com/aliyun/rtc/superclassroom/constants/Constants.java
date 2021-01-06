package com.aliyun.rtc.superclassroom.constants;


import com.aliyun.rtc.superclassroom.BuildConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量列表
 */
public class Constants {

    public static final String NEW_TOKEN_PARAMS_KEY_USERID = "user";
    public static final String NEW_TOKEN_PARAMS_KEY_ROLEID = "roleId";
    public static final String NEW_TOKEN_PARAMS_KEY_USERIDS = "userIds";
    public static final String NEW_TOKEN_PARAMS_KEY_CHANNELID = "channelId";
    private static List<String> mGroupList;


    /**
     * server端的请求域名，需要用户自己替换成自己server端的域名
     */
    /**
     * 获取鉴权信息
     */
    private static final String URL_RTC_AUTH = "";


    static {
        /**
         * 小组
         * */
        mGroupList = new ArrayList<>();
        mGroupList.add("A 组");
        mGroupList.add("B 组");
        mGroupList.add("C 组");
        mGroupList.add("D 组");
        mGroupList.add("E 组");
        mGroupList.add("F 组");
        mGroupList.add("G 组");
        mGroupList.add("H 组");
        mGroupList.add("I 组");
        mGroupList.add("J 组");
    }

    public static List<String> getGroupList() {
        return mGroupList;
    }

    public static String getUrlRtcAuth() {
        return URL_RTC_AUTH;
    }

}
