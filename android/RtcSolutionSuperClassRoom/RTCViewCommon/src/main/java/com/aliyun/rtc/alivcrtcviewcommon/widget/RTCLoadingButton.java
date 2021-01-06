package com.aliyun.rtc.alivcrtcviewcommon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.aliyun.player.alivcrtcviewcommon.R;
import com.aliyun.rtc.alivcrtcviewcommon.view.LoadingDrawable;

public class RTCLoadingButton extends android.support.v7.widget.AppCompatTextView {

    private int mTextSize = R.dimen.alivc_rtc_size_text_18;
    private int mTextColor = R.color.alivc_rtc_color_text_white;
    private int mBackgroundColor = R.drawable.alivc_rtc_btn_bg_selector;
    private LoadingDrawable mLoadingDrawable;

    public RTCLoadingButton(Context context) {
        super(context);
        init(context);
    }

    public RTCLoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RTCLoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        initView();
        initLoading();
    }

    private void initView(){
        setTextColor(getResources().getColor(mTextColor));
        setBackground(getResources().getDrawable(mBackgroundColor));
        setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(mTextSize));
    }

    private void initLoading(){
        mLoadingDrawable = new LoadingDrawable();
    }

    public void showLoading(){
        int width = getWidth();
        int height = getHeight();
        int left = width / 2 - height;
        int right = width / 2;
        mLoadingDrawable.setBounds(left, 0, right, height);
        setCompoundDrawables(mLoadingDrawable, null, null, null);
    }

    public void hideLoading(){
        setCompoundDrawables(null, null, null, null);
    }

}
