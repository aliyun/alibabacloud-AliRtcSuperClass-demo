package com.aliyun.rtc.superclassroom.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SPUtil {

    private static final String SP_NAME = "VIDEO_ROOM_SP";

//    登录页面
//     名字
    public static final String LOGIN_NAME = "LOGIN_NAME";
//    教室码
    public static final String LOGIN_CLASS_CODE = "LOGIN_CLASS_CODE";
//    小组
    public static final String LOGIN_GROUP = "LOGIN_GROUP";
//    小组序号
    public static final String LOGIN_GROUP_INDEX = "LOGIN_GROUP_INDEX";

    private SharedPreferences sp;

    private SPUtil() {
        sp = ApplicationContextUtil.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static SPUtil getInstance() {
        return SPUtilInstance.INSTANCE;
    }

    private static class SPUtilInstance {
        private static final SPUtil INSTANCE = new SPUtil();
    }

    public void putString(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        if (sp != null) {
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(key, value);
            edit.apply();
        }
    }

    public String getString(String key, String defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String string = null;
        if (sp != null) {
            string = sp.getString(key, defaultValue);
        }
        return string;
    }

    public void putInt(String key, int value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (sp != null) {
            SharedPreferences.Editor edit = sp.edit();
            edit.putInt(key, value);
            edit.apply();
        }
    }

    public int getInt(String key, int defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        int value = defaultValue;
        if (sp != null) {
            value = sp.getInt(key, defaultValue);
        }
        return value;
    }

    public void putBoolean(String key, boolean value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (sp != null) {
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean(key, value);
            edit.apply();
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        boolean b = false;

        if (sp != null && !TextUtils.isEmpty(key)) {
            b = sp.getBoolean(key, defaultValue);
        }
        return b;
    }
}
