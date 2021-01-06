package com.aliyun.rtc.alivcrtcviewcommon.widget;

import android.content.Context;

import com.aliyun.rtc.alivcrtcviewcommon.listener.OnTipsDialogListener;
import com.aliyun.rtc.alivcrtcviewcommon.view.CustomTipsView;

public class RTCDialogHelper {

    private static RTCDialogHelper mInstance;

    private CustomTipsView mCustomTipsView;
    private RTCTipsDialog mRTCTipsDialog;
    private OnTipsDialogListener mOnTipsDialogListener;
    private String mTitle;
    private String mTipsTitle;
    private String mConfirmText;
    private String mCancelText;

    private RTCDialogHelper(){

    }

    public static RTCDialogHelper getInstance(){
        if(mInstance == null){
            synchronized (RTCDialogHelper.class){
                if(mInstance == null){
                    mInstance = new RTCDialogHelper();
                }
            }
        }
        return mInstance;
    }

    public void showCustomTipsView(Context context){
        if(mRTCTipsDialog == null){
            mRTCTipsDialog = new RTCTipsDialog(context);
        }
        if(mCustomTipsView == null){
            mCustomTipsView = new CustomTipsView(context);
            mCustomTipsView.setOnTipsDialogListener(mOnTipsDialogListener);
            mCustomTipsView.setTitle(mTitle);
            mCustomTipsView.setTipsTitle(mTipsTitle);
            mCustomTipsView.setConfirmText(mConfirmText);
            mCustomTipsView.setCancelText(mCancelText);
        }
        mRTCTipsDialog.setContentView(mCustomTipsView);
        mRTCTipsDialog.show();
    }

    public void setTitle(String title){
        this.mTitle = title;
    }

    public void setTipsTitle(String title){
        this.mTipsTitle = title;
    }

    public void setCancelText(String text){
        this.mCancelText = text;
    }

    public void setConfirmText(String text){
        this.mConfirmText = text;
    }

    public void showCancelText(){
        if(mCustomTipsView != null){
            mCustomTipsView.showCancel();
        }
    }

    public void hideCancelText(){
        if(mCustomTipsView != null){
            mCustomTipsView.hideCancel();
        }
    }

    public void hideAll(){
        if(mRTCTipsDialog != null){
            mRTCTipsDialog.dismiss();
        }
        mCustomTipsView = null;
    }

    public void setOnTipsDialogListener(OnTipsDialogListener listener){
        this.mOnTipsDialogListener = listener;
    }

    public boolean isShowing(){
       return  mRTCTipsDialog != null && mRTCTipsDialog.isShowing();
    }
    public void release(){
        mRTCTipsDialog = null;
        mCustomTipsView = null;
        mOnTipsDialogListener = null;
    }

    public void setCanceledOnTouchOutside(boolean enable){
        if (mRTCTipsDialog != null) {
            mRTCTipsDialog.setCanceledOnTouchOutside(enable);
        }
    }
}
