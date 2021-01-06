package com.aliyun.rtc.alivcrtcviewcommon.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;


import com.aliyun.player.alivcrtcviewcommon.R;
import com.aliyun.rtc.alivcrtcviewcommon.view.LoadingView;


public class RTCLoadingDialog extends Dialog {

    public RTCLoadingDialog(@NonNull Context context) {
        this(context, R.style.CustomDialogStyle);
    }

    private RTCLoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(new LoadingView(getContext()));
        setCanceledOnTouchOutside(false);
    }

}
