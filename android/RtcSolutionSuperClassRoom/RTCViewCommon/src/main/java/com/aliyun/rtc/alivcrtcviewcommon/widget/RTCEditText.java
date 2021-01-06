package com.aliyun.rtc.alivcrtcviewcommon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.aliyun.player.alivcrtcviewcommon.R;
import com.aliyun.rtc.alivcrtcviewcommon.view.ChannelEditView;

/**
 * RTC可编辑输入框
 */
public class RTCEditText extends TextInputLayout implements TextWatcher, View.OnFocusChangeListener {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_NUMBER = 1;

    private OnTextChangedListener mOnTextChangedListener;
    private OnFocusChangeListener mOnFocusChangeListener;

    public RTCEditText(Context context) {
        super(context);
        init(context, null);
    }

    public RTCEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RTCEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RTCEditText);
        int type = typedArray.getInteger(R.styleable.RTCEditText_rtc_type, TYPE_NORMAL);
        String hint = typedArray.getString(R.styleable.RTCEditText_rtc_hint);
        int textsize = typedArray.getResourceId(R.styleable.RTCEditText_rtc_textSize, R.dimen.alivc_common_font_14);
        int maxLine = typedArray.getInteger(R.styleable.RTCEditText_rtc_maxLines, 1);
        int maxLength = typedArray.getInteger(R.styleable.RTCEditText_rtc_maxLength, Integer.MAX_VALUE);
        int textcolor = typedArray.getResourceId(R.styleable.RTCEditText_rtc_textColor, R.color.alivc_common_bg_black);
        int width = typedArray.getResourceId(R.styleable.RTCEditText_rtc_width, R.dimen.alivc_common_width_group_300);
        int height = typedArray.getResourceId(R.styleable.RTCEditText_rtc_height, R.dimen.alivc_common_height_group_40);
        int cursorColor = typedArray.getResourceId(R.styleable.RTCEditText_rtc_cursor_color,R.color.alivc_rtc_cursor_blue);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.width = getResources().getDimensionPixelSize(width);
        params.height = getResources().getDimensionPixelSize(height);


        if (type == TYPE_NORMAL) {
            EditText mEditText = new EditText(context, attrs);
            mEditText.setHint(hint);
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(textsize));
            mEditText.setMaxLines(maxLine);
            mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
            mEditText.setTextColor(getResources().getColor(textcolor));

            mEditText.addTextChangedListener(this);
            mEditText.setOnFocusChangeListener(this);

            addView(mEditText,params);
        } else if(type == TYPE_NUMBER){
            ChannelEditView mChannelEditView = new ChannelEditView(context, attrs);
            mChannelEditView.setHint(hint);
            mChannelEditView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(textsize));
            mChannelEditView.setMaxLines(maxLine);
            mChannelEditView.setMaxLength(maxLength);
            mChannelEditView.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
            mChannelEditView.setTextColor(getResources().getColor(textcolor));
            mChannelEditView.setCursorColor(cursorColor);
            mChannelEditView.setInputType(InputType.TYPE_CLASS_NUMBER);

            mChannelEditView.addTextChangedListener(this);
            mChannelEditView.setOnFocusChangeListener(this);

            addView(mChannelEditView,params);
        }

        typedArray.recycle();
    }

    public interface OnTextChangedListener{
        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);
    }

    public void addTextChangeListener(OnTextChangedListener listener){
        this.mOnTextChangedListener = listener;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(mOnTextChangedListener != null){
            mOnTextChangedListener.beforeTextChanged(s,start,count,after);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(mOnTextChangedListener != null){
            mOnTextChangedListener.onTextChanged(s,start,before,count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(mOnTextChangedListener != null){
            mOnTextChangedListener.afterTextChanged(s);
        }
    }

    public interface OnFocusChangeListener{
        void onFocusChange(View view,boolean b);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener){
        this.mOnFocusChangeListener = listener;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(mOnFocusChangeListener != null){
            mOnFocusChangeListener.onFocusChange(view,b);
        }
    }
}
