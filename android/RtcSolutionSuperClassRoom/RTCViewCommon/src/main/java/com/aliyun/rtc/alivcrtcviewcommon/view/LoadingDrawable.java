package com.aliyun.rtc.alivcrtcviewcommon.view;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;

public class LoadingDrawable extends Drawable implements Animatable {

    private Paint mPaint;
    private ValueAnimator mValueAnimator;
    /**
     * loading图标圆心
     */
    private Point centerPoint;

    /**
     * loading图标小椭圆rect
     */
    private RectF mBeginRect = new RectF();

    private float mLittleAirWidth;

    private int currentCount = 0;
    /**
     * loading图标小矩形的数量
     */
    private int mMaxcount = 12;

    public LoadingDrawable() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void start() {
        if (mValueAnimator != null && !mValueAnimator.isRunning()) {
            mValueAnimator.start();
        }
    }

    @Override
    public void stop() {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
    }

    @Override
    public boolean isRunning() {
        return mValueAnimator != null && mValueAnimator.isRunning();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int startAlpha = 255;
        for (int i = 0; i < mMaxcount; i++) {
            int alpha;
            if (i == currentCount) {
                alpha = startAlpha;
            } else if (i < currentCount) {
                alpha = startAlpha * (currentCount - i) / mMaxcount;
            } else {
                alpha = startAlpha * (mMaxcount - (i - currentCount)) / mMaxcount;
            }
            mPaint.setAlpha(alpha);
            if (i == 0) {
                canvas.drawRoundRect(mBeginRect, mLittleAirWidth / 2, mLittleAirWidth / 2, mPaint);
            } else {
                canvas.save();
                canvas.rotate(30 * i, centerPoint.x, centerPoint.y);
                canvas.drawRoundRect(mBeginRect, mLittleAirWidth / 2, mLittleAirWidth / 2, mPaint);
                canvas.restore();
            }
        }
        currentCount = currentCount > 11 ? 0 : currentCount + 1;
    }

    @Override
    public void setAlpha(int alpha) {
        if (mPaint != null) {
            mPaint.setAlpha(alpha);
        }
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        if (mPaint != null) {
            mPaint.setColorFilter(colorFilter);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        int min = Math.min(bounds.width(), bounds.height());
        int circleRadius = min / 4;
        centerPoint = new Point(bounds.left + bounds.width() / 2, bounds.top + bounds.height() / 2);
        mLittleAirWidth = (int) ((Math.PI * 2 * circleRadius) / (mMaxcount * 2));
        //计算loading图标小椭圆rect
        mBeginRect.left = centerPoint.x - mLittleAirWidth / 2;
        mBeginRect.right = centerPoint.x + mLittleAirWidth / 2;
        mBeginRect.top = centerPoint.y - circleRadius;
        mBeginRect.bottom = centerPoint.y - (circleRadius >> 1);
        startAnimation();
    }

    private void startAnimation() {
        mValueAnimator = ValueAnimator.ofInt(mMaxcount);
        mValueAnimator.setDuration(1000);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int lastValue = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value != lastValue) {
                    invalidateSelf();
                }
                lastValue = value;
            }
        });
        mValueAnimator.start();
    }
}
