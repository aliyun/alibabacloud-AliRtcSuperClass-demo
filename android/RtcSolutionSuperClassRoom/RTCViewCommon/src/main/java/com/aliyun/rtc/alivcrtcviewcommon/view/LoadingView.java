package com.aliyun.rtc.alivcrtcviewcommon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.player.alivcrtcviewcommon.R;
import com.aliyun.rtc.alivcrtcviewcommon.listener.OnTipsDialogListener;

public class LoadingView extends LinearLayout {

    private View mInflater;

    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context){
        mInflater = LayoutInflater.from(context).inflate(R.layout.layout_rtc_loading_view, this,true);
        initView();
    }


    private void initView(){
        ImageView ivLoading = mInflater.findViewById(R.id.rtc_iv_loading);
        LoadingDrawable loadingDrawable = new LoadingDrawable();
        ivLoading.setImageDrawable(loadingDrawable);
    }

}
