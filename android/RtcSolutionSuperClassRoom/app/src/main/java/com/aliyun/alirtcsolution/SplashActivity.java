package com.aliyun.alirtcsolution;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {

    static Handler mHandler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mHandler.postDelayed(new JumpActivityRunnable(SplashActivity.this), 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private static class JumpActivityRunnable implements Runnable {
        WeakReference<Activity> mWeakReference;
        public JumpActivityRunnable(Activity splashActivity) {
            mWeakReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void run() {
            Activity activity = mWeakReference.get();
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
