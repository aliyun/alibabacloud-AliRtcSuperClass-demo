package com.aliyun.rtc.alivcrtcviewcommon.widget;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Window;

import com.aliyun.rtc.alivcrtcviewcommon.view.LoadingView;

public class RTCLoadingDialogHelper {

    private static RTCLoadingDialogHelper mInstance;

    private RTCLoadingDialog mLoadingDialog;

    private RTCLoadingDialogHelper(){

    }

    public static RTCLoadingDialogHelper getInstance(){
        if(mInstance == null){
            synchronized (RTCLoadingDialogHelper.class){
                if(mInstance == null){
                    mInstance = new RTCLoadingDialogHelper();
                }
            }
        }
        return mInstance;
    }

    public synchronized void showLoadingView(Context context){
        if(mLoadingDialog == null){
            mLoadingDialog = new RTCLoadingDialog(context);
        }

        if(!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public synchronized void hideLoadingView(){
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void release(){
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }
}
