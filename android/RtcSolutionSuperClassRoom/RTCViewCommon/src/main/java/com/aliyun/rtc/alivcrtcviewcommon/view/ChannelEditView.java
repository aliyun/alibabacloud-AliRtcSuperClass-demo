package com.aliyun.rtc.alivcrtcviewcommon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.aliyun.player.alivcrtcviewcommon.R;
import com.aliyun.svideo.common.utils.DensityUtils;

import java.lang.ref.WeakReference;


public class ChannelEditView extends android.support.v7.widget.AppCompatEditText implements TextWatcher {
    private int maxLenght;
    private boolean focused;
    private int mWidthSize;
    private int mHeightSize;
    private int grapWidth = DensityUtils.dip2px(getContext(), 16);//间隔
    private Paint mPaint;
    private float[] points;
    private int marginLfet;
    private int marginButtom = DensityUtils.dip2px(getContext(), 8);
    private String content;
    private float mLineLenght;
    private boolean mErrorEnable;
    private int mErrorColorBottomLine;
    private boolean mNeedDrawCursor = true;
    private Rect mBound = new Rect();

    private MyHandler mHandler;
    private int mCursorColor = getResources().getColor(R.color.alivc_rtc_cursor_blue);
    private int mBottomLineColor = getResources().getColor(R.color.alivc_rtc_color_text_gray);
    private int mTextColor = Color.BLACK;
    //最后一个数字的宽度
    private int mLastTextWidth;

    public ChannelEditView(Context context) {
        super(context);
        init(context,null);
    }

    public ChannelEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ChannelEditView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        if(attrs != null){
            int attributeCount = attrs.getAttributeCount();
            for (int i = 0; i < attributeCount; i++) {
                String attributeName = attrs.getAttributeName(i);
                if ("maxLength".equals(attributeName)) {
                    String value = attrs.getAttributeValue(i);
                    maxLenght = Integer.parseInt(value);
                }
            }
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RTCEditText);
            int cursorColorResourceId = typedArray.getResourceId(R.styleable.RTCEditText_rtc_cursor_color,R.color.alivc_rtc_cursor_blue);
            mTextColor = typedArray.getResourceId(R.styleable.RTCEditText_rtc_textColor, Color.BLACK);
            mCursorColor = getResources().getColor(cursorColorResourceId);
            typedArray.recycle();
        }

        mHandler = new MyHandler(this);

        initPaint();
        addTextChangedListener(this);
    }

    public void setMaxLength(int maxLength){
        this.maxLenght = maxLength;
    }

    public void setErrorEnable(boolean errorEnable) {
        this.mErrorEnable = errorEnable;
    }

    public void setErrorColorBottomLine(int errorColorBottomLine) {
        mErrorColorBottomLine = errorColorBottomLine;
    }

    private void initLines() {
        points = new float[4 * maxLenght];
        marginLfet = DensityUtils.dip2px(getContext(), 6);
        mLineLenght = (mWidthSize * 1.0f - grapWidth * (maxLenght - 1) - marginLfet) / maxLenght;
        for (int i = 0; i < maxLenght; i++) {
            points[i * 4] = i * mLineLenght + i * grapWidth + marginLfet;
            points[i * 4 + 1] = mHeightSize * 1f;
            points[i * 4 + 2] = points[i * 4] + mLineLenght;
            points[i * 4 + 3] = mHeightSize * 1f;
        }

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(DensityUtils.sp2px(getContext(), 14));
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        mHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        mWidthSize = getSizeBySpecMode(widthMode, mWidthSize);
        mHeightSize = getSizeBySpecMode(heightMode, mHeightSize);
        initLines();
        setBackground(null);
    }

    private int getSizeBySpecMode(int mode, int size) {
        if (mode == MeasureSpec.AT_MOST) {
            size = ((ViewGroup) getParent()).getMeasuredWidth();
        } else if (mode == MeasureSpec.UNSPECIFIED) {
            size = ((ViewGroup) getParent()).getMeasuredWidth();
        }
        return size;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        this.focused = focused;
        if(focused){
            mNeedDrawCursor = true;
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(0,500);
        }else{
            mNeedDrawCursor = false;
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (focused || !TextUtils.isEmpty(content)) {
            mPaint.setStrokeWidth(1);
            mPaint.setColor(mErrorEnable ? mErrorColorBottomLine : mBottomLineColor);
            canvas.drawLines(points, mPaint);
//        }
//        else {
//            mPaint.setColor(mBottomLineColor);
//            mPaint.setStrokeWidth(1);
//            canvas.drawLine(marginLfet, mHeightSize, mWidthSize, mHeightSize, mPaint);
//        }

        for (int i = 0; i < content.length(); i++) {
            mPaint.setStrokeWidth(1);
            mPaint.setColor(mTextColor);
            mPaint.getTextBounds(content, 0, i, mBound);
            canvas.drawText(String.valueOf(content.charAt(i)), points[i * 4] + mLineLenght / 2, mHeightSize - marginButtom - 10, mPaint);
        }

        if(focused && mNeedDrawCursor){
            mPaint.setStrokeWidth(DensityUtils.dip2px(getContext(), 2));
            mPaint.setColor(mCursorColor);
            int length = content.length() * 4;
            if(content.length() == maxLenght){
                length = (content.length() - 1) * 4;
                canvas.drawLine(points[length] + mLineLenght / 2 + mLastTextWidth,mHeightSize - marginButtom - 46,points[length] + mLineLenght / 2 + mLastTextWidth,mHeightSize - marginButtom - 5,mPaint);
            }else{
                canvas.drawLine(points[length] + mLineLenght / 2,mHeightSize - marginButtom - 46,points[length] + mLineLenght / 2,mHeightSize - marginButtom - 5,mPaint);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.content = s.toString();
        if(content.length() == maxLenght && mPaint != null){
            mPaint.getTextBounds(s.toString(), s.toString().length() - 1, s.toString().length(), mBound);
            mLastTextWidth = mBound.right - mBound.left;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setCursorColor(int cursorColor) {
        if(cursorColor > 0){
            this.mCursorColor = getResources().getColor(cursorColor);
        }
    }


    private static class MyHandler extends Handler{

        private WeakReference<ChannelEditView> weakReference;

        public MyHandler(ChannelEditView channelEditView){
            weakReference = new WeakReference<>(channelEditView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChannelEditView channelEditView = weakReference.get();
            if(channelEditView != null){
                channelEditView.mNeedDrawCursor = !channelEditView.mNeedDrawCursor;
                sendEmptyMessageDelayed(0,500);
            }
        }
    }
}
