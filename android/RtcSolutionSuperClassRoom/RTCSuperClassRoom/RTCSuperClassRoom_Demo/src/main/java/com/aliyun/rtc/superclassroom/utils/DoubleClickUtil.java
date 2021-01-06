package com.aliyun.rtc.superclassroom.utils;

import android.view.View;

public class DoubleClickUtil {

    private static View lastClickView;
    private static long lastClickTime;

    public synchronized static boolean isDoubleClick(View view, long tempTime) {
        boolean doubleClick = false;
        if (lastClickView == null || lastClickTime == 0) {
            lastClickView = view;
        } else if (lastClickView == view && System.currentTimeMillis() - lastClickTime <= tempTime) { //重复点击
            doubleClick = true;
        }
        lastClickTime = System.currentTimeMillis();
        return doubleClick;
    }
}
