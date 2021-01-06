package com.aliyun.rtc.superclassroom.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.rtc.superclassroom.R;
import com.aliyun.rtc.superclassroom.listener.ControlListener;
import com.aliyun.rtc.superclassroom.utils.SPUtil;
import com.aliyun.rtc.superclassroom.utils.StringUtil;
import com.aliyun.svideo.common.utils.FastClickUtil;
import com.aliyun.svideo.common.utils.ToastUtils;


/**
 * 页面控制按钮类
 */
public class ControlView extends RelativeLayout implements View.OnClickListener {

    private static final int WHAT_HIDE = 0;
    private static final int DELAY_TIME = 5 * 1000; //5秒后隐藏

    /**
     * 当前是否是显示状态
     */
    private boolean isShow = true;
    /**
     * 课程尚未开始画面是否显示
     */
    private boolean isEmptyViewShow = true;

    /**
     * 当前是否静音
     */
    private boolean isMute = false;

    /**
     * 摄像头是否开启
     */
    private boolean isCameraOpen = true;

    /**
     * 是否连接老师
     */
    private boolean isConnectTeacher = false;

    /**
     * 是否是竖屏
     */
    private boolean isPortrait = true;



    /**
     *  顶部布局
     * */
    private LinearLayout mTopView;
    private ImageView mTopSwicthCamera;
    private TextView mTopTitle;
    private ImageView mTopCopyTitle;
    private TextView mTopLeave;

    /**
     *  底部布局
     * */
    private LinearLayout mBottomView;
    private TextView mBottomMute;
    private TextView mBottomCamera;
    private TextView mBottomConnect;
    private TextView mBottomGroup;
    /**
     *  中间布局
     * */
    private TextView mMiddleMute;
    private ImageView mMiddleRotate;

    /**
     * 课程未开始显示view
     */
    private LinearLayout mEmptyView;


    private ControlListener mControlListener;


    private Context mContext;
    //记录老师是否在线
    private boolean isTeacherLogin;

    public void setmControlListener(ControlListener mControlListener) {
        this.mControlListener = mControlListener;
    }

    public boolean isShow() {
        return isShow;
    }

    public boolean isMute() {
        return isMute;
    }

    public boolean isCameraOpen() {
        return isCameraOpen;
    }

    public boolean isConnectTeacher() {
        return isConnectTeacher;
    }

    public ControlView(@NonNull Context context) {
        this(context, null);
    }

    public ControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            //切换到竖屏
            //修改布局文件
            isPortrait = true;
            initView();
        }else{
            //切换到横屏
            //修改布局文件
            isPortrait = false;
            initView();
        }
    }

    private void initView() {
        removeAllViews();
        View view = LayoutInflater.from(mContext).inflate(R.layout.rtc_group_control_view, this, true);
        view.setBackgroundColor(Color.TRANSPARENT);

        mTopSwicthCamera = findViewById(R.id.rtc_super_class_control_switch_camera);
        mTopTitle = findViewById(R.id.rtc_super_class_control_title);
        mTopCopyTitle = findViewById(R.id.rtc_super_class_control_copy);
        mTopLeave = findViewById(R.id.rtc_super_class_control_leave);

        mBottomMute = findViewById(R.id.rtc_super_class_control_mute);
        mBottomCamera = findViewById(R.id.rtc_super_class_control_camera);
        mBottomConnect = findViewById(R.id.rtc_super_class_control_connect);
        mBottomGroup = findViewById(R.id.rtc_super_class_control_group);

        mMiddleMute = findViewById(R.id.rtc_super_class_control_single_mute);
        mMiddleRotate = findViewById(R.id.rtc_super_class_control_rotate);

        mTopView = findViewById(R.id.rtc_super_class_control_top);
        mBottomView = findViewById(R.id.rtc_super_class_control_bottom);

        mEmptyView = findViewById(R.id.rtc_super_class_control_empty);
        if(isEmptyViewShow){
            showEmptyView();
        } else {
            hideEmptyView();
        }
        mTopSwicthCamera.setVisibility(INVISIBLE);
        mTopSwicthCamera.setOnClickListener(this);
        mTopCopyTitle.setOnClickListener(this);
        mTopLeave.setOnClickListener(this);
        mBottomMute.setOnClickListener(this);
        mBottomCamera.setOnClickListener(this);
        mBottomConnect.setOnClickListener(this);
        mBottomGroup.setOnClickListener(this);
        mMiddleMute.setOnClickListener(this);
        mMiddleRotate.setOnClickListener(this);
        mMiddleRotate.setImageResource(isPortrait ? R.drawable.alivc_superclass_byn_rotate_horizontal : R.drawable.alivc_superclass_byn_rotate_vertical);
        hideDelayed();
        setRoomCode(SPUtil.getInstance().getString(SPUtil.LOGIN_CLASS_CODE,""));
        openCamera(isCameraOpen);
        mute(isMute);
        connectTeacher(isConnectTeacher);
    }


    /**
     * @param code 设置标题
     */
    public void setRoomCode(String code){
//        mTopTitle.setText(StringUtil.getRoomCode(code));
        mTopTitle.setText("教室码："+code);
    }


    public void showEmptyView(){
        isEmptyViewShow = true;
        mEmptyView.setVisibility(VISIBLE);
    }
    public void hideEmptyView(){
        isEmptyViewShow = false;
        mEmptyView.setVisibility(GONE);
    }

    public void showControlView(){
        isShow = true;
        mTopView.setVisibility(VISIBLE);
        mBottomView.setVisibility(VISIBLE);
        mMiddleMute.setVisibility(GONE);
        mMiddleRotate.setVisibility(VISIBLE);
        mTopView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alivc_slide_in_from_top));
        mBottomView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alivc_slide_in_from_bottom));
        mMiddleMute.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alivc_hide_anim));
        mMiddleRotate.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alivc_show_anim));
        hideDelayed();
    }
    public void hideControlView(){
        isShow = false;
        if( mHideHandler.hasMessages(WHAT_HIDE)){
            mHideHandler.removeMessages(WHAT_HIDE);
        }
        mTopView.setVisibility(GONE);
        mBottomView.setVisibility(GONE);
        mMiddleMute.setVisibility(VISIBLE);
        mMiddleRotate.setVisibility(GONE);
        mTopView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alivc_slide_out_to_top));
        mBottomView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alivc_slide_out_to_bottom));
        mMiddleMute.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alivc_show_anim));
        mMiddleRotate.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alivc_hide_anim));
        if(mControlListener != null){
            mControlListener.hideViews();
        }
    }


    /**
     * 隐藏类
     */
    private class HideHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideControlView();
        }
    }

    private HideHandler mHideHandler = new HideHandler();

    private void hideDelayed() {
        if( mHideHandler.hasMessages(WHAT_HIDE)){
            mHideHandler.removeMessages(WHAT_HIDE);
        }
        mHideHandler.sendEmptyMessageDelayed(WHAT_HIDE, DELAY_TIME);
    }

    public void setIsTeacherLogin(boolean isTeacherLogin){
        this.isTeacherLogin = isTeacherLogin;
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if(viewId == R.id.rtc_super_class_control_switch_camera){
//            顶部切换摄像头
            if(mControlListener != null){
                mControlListener.switchCameraClick();
            }
        } else if(viewId == R.id.rtc_super_class_control_copy){
//            顶部复制
            if(mControlListener != null){
                mControlListener.copyTitleClick(SPUtil.getInstance().getString(SPUtil.LOGIN_CLASS_CODE,""));
            }

        } else if(viewId == R.id.rtc_super_class_control_leave){
//            顶部离开
            if(mControlListener != null){
                mControlListener.leaveRoomClick();
            }
        } else if(viewId == R.id.rtc_super_class_control_mute || viewId == R.id.rtc_super_class_control_single_mute){
//            底部静音和单独静音
            isMute = !isMute;
            mute(isMute);
            if(mControlListener != null){
                mControlListener.muteClick(isMute);
            }
        } else if(viewId == R.id.rtc_super_class_control_camera){
//            底部开关摄像头
            isCameraOpen = !isCameraOpen;
            openCamera(isCameraOpen);
            if(mControlListener != null){
                mControlListener.openCameraClick(isCameraOpen);
            }
        } else if(viewId == R.id.rtc_super_class_control_connect){
//            底部连麦老师
//            防止快速点击
            if(FastClickUtil.isFastClick()){
                ToastUtils.showInCenter(getContext(),getContext().getString(R.string.alivc_superclass_string_message_fast_click));
                return;
            }
            //老师不在时不能连麦
            if(!isTeacherLogin){
                ToastUtils.showInCenter(getContext(),getContext().getString(R.string.alivc_superclass_string_message_wait_teacher));
                return;
            }

            isConnectTeacher = !isConnectTeacher;
            connectTeacher(isConnectTeacher);
            if(mControlListener != null){
                mControlListener.connectTeacherClick(isConnectTeacher);
            }
        } else if(viewId == R.id.rtc_super_class_control_group){
//            底部小组成员
            if(mControlListener != null){
                mControlListener.teamMembersClick();
            }
        } else if(viewId == R.id.rtc_super_class_control_rotate){
//            中间旋转
            if(mControlListener != null){
                mControlListener.singleRotateClick(isPortrait);
            }
        }
    }

    public void connectTeacher(boolean isConnectTeacher){
        if(isConnectTeacher){
            Drawable  dra= getResources().getDrawable(R.drawable.alivc_superclass_byn_connect_close3x);
            dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
            mBottomConnect.setCompoundDrawables(null, dra, null, null);
            mBottomConnect.setText(getResources().getString(R.string.alivc_superclass_string_cancel_connect_teacher));
        } else {
            Drawable  dra= getResources().getDrawable(R.drawable.alivc_superclass_byn_connect_open3x);
            dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
            mBottomConnect.setCompoundDrawables(null, dra, null, null);
            mBottomConnect.setText(getResources().getString(R.string.alivc_superclass_string_connect_teacher));
        }

    }
    public void openCamera(boolean isCameraOpen){
        if(isCameraOpen){
            Drawable  dra= getResources().getDrawable(R.drawable.alivc_superclass_byn_camera_open3x);
            dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
            mBottomCamera.setCompoundDrawables(null, dra, null, null);
            mBottomCamera.setText(getResources().getString(R.string.alivc_superclass_string_camera_close));
            mTopSwicthCamera.setVisibility(VISIBLE);
        } else {
            Drawable  dra= getResources().getDrawable(R.drawable.alivc_superclass_byn_camera_close3x);
            dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
            mBottomCamera.setCompoundDrawables(null, dra, null, null);
            mBottomCamera.setText(getResources().getString(R.string.alivc_superclass_string_camera_open));
            mTopSwicthCamera.setVisibility(INVISIBLE);
        }
    }

    /**
     * 静音操作
     */
    public void mute(boolean isMute) {
        if(isMute){
            Drawable  dra= getResources().getDrawable(R.drawable.alivc_superclass_byn_mute_disable3x);
            dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
            mBottomMute.setCompoundDrawables(null, dra, null, null);
            mBottomMute.setText(getResources().getString(R.string.alivc_superclass_string_mute_cancel));

            Drawable drawable = getResources().getDrawable(R.drawable.alivc_superclass_byn_unmute_icon);
            drawable.setBounds( 0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
            mMiddleMute.setCompoundDrawables(null, drawable, null, null);
            mMiddleMute.setText(getResources().getString(R.string.alivc_superclass_string_mute_cancel));
        } else {
            Drawable  dra= getResources().getDrawable(R.drawable.alivc_superclass_byn_mute_able3x);
            dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
            mBottomMute.setCompoundDrawables(null, dra, null, null);
            mBottomMute.setText(getResources().getString(R.string.alivc_superclass_string_mute));

            Drawable drawable = getResources().getDrawable(R.drawable.alivc_superclass_byn_mute_icon);
            drawable.setBounds( 0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
            mMiddleMute.setCompoundDrawables(null, drawable, null, null);
            mMiddleMute.setText(getResources().getString(R.string.alivc_superclass_string_mute));
        }
    }
}
