package com.aliyun.rtc.superclassroom.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class UIHandlerUtil {

    private static Handler mUIHandler;

    private UIHandlerUtil() {
        mUIHandler = new Handler(Looper.getMainLooper());
    }
    private static class UIHandlerUtilInstance {
        private final static UIHandlerUtil INSTANCE = new UIHandlerUtil();
    }
    public static UIHandlerUtil getInstance() {
        return UIHandlerUtilInstance.INSTANCE;
    }

    public void postRunnable(Runnable runnable) {
        if (mUIHandler != null) {
            mUIHandler.post(runnable);
        }
    }

    public void postRunnableDelayed(Runnable runnable, long delayed) {
        if (mUIHandler != null) {
            mUIHandler.postDelayed(runnable, delayed);
        }
    }

    public void sendMessage(Message msg) {
        if (mUIHandler != null && msg != null) {
            mUIHandler.sendMessage(msg);
        }
    }

    public void sendMessageDelayed(Message msg, long delay) {
        if (mUIHandler != null) {
            mUIHandler.sendMessageDelayed(msg, delay);
        }
    }

    public void clearAllMsgAndRunnable() {
        if (mUIHandler != null) {
            mUIHandler.removeCallbacksAndMessages(null);
        }
    }
}
