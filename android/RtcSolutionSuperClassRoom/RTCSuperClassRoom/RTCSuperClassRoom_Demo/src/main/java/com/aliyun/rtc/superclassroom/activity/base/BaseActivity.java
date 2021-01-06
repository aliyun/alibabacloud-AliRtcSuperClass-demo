package com.aliyun.rtc.superclassroom.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.aliyun.rtc.alivcrtcviewcommon.widget.RTCLoadingDialogHelper;
import com.aliyun.rtc.superclassroom.utils.NetWatchdogUtils;
import com.aliyun.rtc.superclassroom.utils.PermissionUtil;
import com.aliyun.rtc.superclassroom.utils.ScreenUtil;
import com.aliyun.rtc.superclassroom.utils.UIHandlerUtil;
import com.aliyun.svideo.common.utils.ToastUtils;

public abstract class BaseActivity extends AppCompatActivity implements NetWatchdogUtils.NetChangeListener, PermissionUtil.PermissionGrantedListener {

    private NetWatchdogUtils mNetWatchdogUtils;
    private String[] permissions = new String[]{
            PermissionUtil.PERMISSION_CAMERA,
            PermissionUtil.PERMISSION_WRITE_EXTERNAL_STORAGE,
            PermissionUtil.PERMISSION_RECORD_AUDIO,
            PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //适配水滴屏
        int layoutId = getLayoutId();
        View inflate = LayoutInflater.from(this).inflate(layoutId, null);
        int statusBarHeight = ScreenUtil.getStatusBarHeight(this);
        inflate.setPadding(0, statusBarHeight, 0, 0);
        setContentView(inflate);
    }

    public abstract int getLayoutId();

    @Override
    protected void onStart() {
        super.onStart();
        PermissionUtil.requestPermissions(this, permissions, PermissionUtil.PERMISSION_REQUEST_CODE, this);
        if (mNetWatchdogUtils == null) {
            //添加网络监听
            mNetWatchdogUtils = new NetWatchdogUtils(this);
            mNetWatchdogUtils.setNetChangeListener(this);
            mNetWatchdogUtils.startWatch();
        }
    }

    @Override
    public void onWifiTo4G() {

    }

    @Override
    public void on4GToWifi() {

    }

    @Override
    public void onReNetConnected(boolean isReconnect) {

    }

    @Override
    public void onNetUnConnected() {
//        Intent intent = new Intent(this, NetWorkErrorActivity.class);
//        startActivity(intent);
    }

    protected void showLoading() {
        if (!isFinishing()) {
            RTCLoadingDialogHelper.getInstance().showLoadingView(this);
        }
    }

    protected void hideLoading() {
        RTCLoadingDialogHelper.getInstance().hideLoadingView();
    }

    public void showToastInCenter(final String msg) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            ToastUtils.showInCenter(this, msg);
        } else {
            UIHandlerUtil.getInstance().postRunnable(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showInCenter(BaseActivity.this, msg);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RTCLoadingDialogHelper.getInstance().release();
        if (mNetWatchdogUtils != null) {
            mNetWatchdogUtils.stopWatch();
            mNetWatchdogUtils.setNetChangeListener(null);
            mNetWatchdogUtils = null;
        }
    }

    @Override
    public void onPermissionGranted() {

    }

    @Override
    public void onPermissionCancel() {

    }
}