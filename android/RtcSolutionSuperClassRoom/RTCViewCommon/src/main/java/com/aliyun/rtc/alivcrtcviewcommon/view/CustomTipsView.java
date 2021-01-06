package com.aliyun.rtc.alivcrtcviewcommon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.player.alivcrtcviewcommon.R;
import com.aliyun.rtc.alivcrtcviewcommon.listener.OnTipsDialogListener;

public class CustomTipsView extends RelativeLayout implements View.OnClickListener {

    private View mInflater;
    private TextView mTitleTextView;
    private TextView mCancelTextView;
    private TextView mConfirmTextView;
    private TextView mTipsTitleTextView;

    private OnTipsDialogListener mOnTipsDialogListener;

    public CustomTipsView(Context context) {
        super(context);
        init(context);
    }

    public CustomTipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mInflater = LayoutInflater.from(context).inflate(R.layout.layout_rtc_custom_tip_view, this,true);
        initView();
        initListener();
    }

    public void setOnTipsDialogListener(OnTipsDialogListener listener){
        this.mOnTipsDialogListener = listener;
    }

    private void initView(){
        mTitleTextView = mInflater.findViewById(R.id.tv_title);
        mCancelTextView = mInflater.findViewById(R.id.tv_cancel);
        mConfirmTextView = mInflater.findViewById(R.id.tv_confirm);
        mTipsTitleTextView = mInflater.findViewById(R.id.tv_tips_title);
    }

    public void setTitle(String title){
        mTitleTextView.setText(title);
    }

    public void setTipsTitle(String title){
        mTipsTitleTextView.setText(title);
    }

    public void setCancelText(String text){
        mCancelTextView.setText(text);
    }

    public void setConfirmText(String text){
        mConfirmTextView.setText(text);
    }

    private void initListener(){
        mCancelTextView.setOnClickListener(this);
        mConfirmTextView.setOnClickListener(this);
    }

    public void hideCancel(){
        if(mCancelTextView != null){
            mCancelTextView.setVisibility(View.GONE);
        }
    }

    public void showCancel(){
        if(mCancelTextView != null){
            mCancelTextView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_confirm){
            if(mOnTipsDialogListener != null){
                mOnTipsDialogListener.onComfirm();
            }
        }else if(view.getId() == R.id.tv_cancel){
            if(mOnTipsDialogListener != null){
                mOnTipsDialogListener.onCancel();
            }
        }
    }
}
