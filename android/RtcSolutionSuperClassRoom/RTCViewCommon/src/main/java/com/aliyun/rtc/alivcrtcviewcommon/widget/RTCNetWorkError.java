package com.aliyun.rtc.alivcrtcviewcommon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aliyun.player.alivcrtcviewcommon.R;

public class RTCNetWorkError extends FrameLayout implements View.OnClickListener {

    private TextView mRetryTextView;

    private OnNetWorkErrorListener mOnNetWorkErrorListener;

    public RTCNetWorkError(Context context) {
        super(context);
        init(context);
    }

    public RTCNetWorkError(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RTCNetWorkError(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        initView(context);
        initListener();
    }

    private void initView(Context context){
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_rtc_network_error_view, this,true);
        mRetryTextView = inflate.findViewById(R.id.tv_retry);
    }

    private void initListener(){
        mRetryTextView.setOnClickListener(this);
    }

    public interface OnNetWorkErrorListener{
        void onRetry();
    }

    public void setNetWorkErrorListener(OnNetWorkErrorListener listener){
        this.mOnNetWorkErrorListener = listener;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_retry){
            if(mOnNetWorkErrorListener != null){
                mOnNetWorkErrorListener.onRetry();
            }
        }
    }
}
