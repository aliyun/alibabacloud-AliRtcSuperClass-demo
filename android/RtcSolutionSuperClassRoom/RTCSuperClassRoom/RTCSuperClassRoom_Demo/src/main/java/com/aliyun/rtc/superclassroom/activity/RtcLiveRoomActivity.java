package com.aliyun.rtc.superclassroom.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alivc.rtc.AliRtcEngine;
import com.alivc.rtc.AliRtcRemoteUserInfo;
import com.aliyun.rtc.alivcrtcviewcommon.listener.OnTipsDialogListener;
import com.aliyun.rtc.alivcrtcviewcommon.widget.RTCDialogHelper;
import com.aliyun.rtc.superclassroom.R;
import com.aliyun.rtc.superclassroom.activity.base.BaseActivity;
import com.aliyun.rtc.superclassroom.adapter.StudentListAdapter;
import com.aliyun.rtc.superclassroom.bean.AlivcVideoStreamInfo;
import com.aliyun.rtc.superclassroom.bean.RTCRoleType;
import com.aliyun.rtc.superclassroom.listener.ControlListener;
import com.aliyun.rtc.superclassroom.rtc.RTCSuperClassCallback;
import com.aliyun.rtc.superclassroom.rtc.RTCSuperClassImpl;
import com.aliyun.rtc.superclassroom.utils.OrientationDetector;
import com.aliyun.rtc.superclassroom.utils.SPUtil;
import com.aliyun.rtc.superclassroom.utils.ScreenUtil;
import com.aliyun.rtc.superclassroom.utils.ThreadUtils;
import com.aliyun.rtc.superclassroom.utils.UIHandlerUtil;
import com.aliyun.rtc.superclassroom.view.ControlView;
import com.aliyun.svideo.common.utils.DensityUtils;
import com.aliyun.svideo.common.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RtcLiveRoomActivity extends BaseActivity implements ControlListener, RTCSuperClassCallback {

    private static final String TAG = RtcLiveRoomActivity.class.getSimpleName();

    //顶部的填充view
    private FrameLayout mTopView;
    //底部的填充view
    private FrameLayout mBottomView;
    //控制按钮view
    private ControlView mControlView;
    //本地预览的父view
    private FrameLayout mLocalCameraView;
    //老师画面的展示
    private FrameLayout mTeacherContainView;
    //显示某某某正在发言的tv
    private TextView mSpeakingUserTv;
    //是否在连麦老师
    private boolean isConnectTeacher = false;
    //学生画面列表
    private RecyclerView mRcyStudentList;
    //学生画面的数据
    private List<AlivcVideoStreamInfo> mAlivcVideoStreamInfos;
    private StudentListAdapter mStudentListAdapter;
    //方向传感器
    private OrientationDetector mOrientationDetector;
    //当前方向 用来设置画面是往左旋转还有往右旋转
    private int mCurrentPosition = 0;
    //自动旋转相关
    private boolean mIsLand = false; // 是否是横屏
    private boolean mClick = false; // 是否点击
    private boolean mClickLand = true; // 点击进入横屏
    private boolean mClickPort = true; // 点击进入竖屏
    //弹框
    private RTCDialogHelper mRtcDialogHelper,mRtcErrorDialogHelper;
    //房间号
    private String mTeacherChannelId,mStudentChannelId;
    //记录当前小组说话人的信息，userID和isSpeaking
    private Map<String,Boolean> mSpeakingUserMap;


    @Override
    public int getLayoutId() {
        return R.layout.rtc_live_room_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        setListensers();
        initStudentList();
        mSpeakingUserMap = new HashMap<>();
        mTeacherChannelId = SPUtil.getInstance().getString(SPUtil.LOGIN_CLASS_CODE,"");
        mStudentChannelId = mTeacherChannelId + SPUtil.getInstance().getString(SPUtil.LOGIN_GROUP,"");
        mControlView.setRoomCode(mTeacherChannelId);
        //A组_姓名_学生
        String userName = SPUtil.getInstance().getString(SPUtil.LOGIN_GROUP,"")+getString(R.string.alivc_superclass_string_group_name)
                            +SPUtil.getInstance().getString(SPUtil.LOGIN_NAME,"")+getString(R.string.alivc_superclass_string_student);
        //String userName = SPUtil.getInstance().getString(SPUtil.LOGIN_NAME,"");
        RTCSuperClassImpl.sharedInstance().login(mTeacherChannelId,userName);
        RTCSuperClassImpl.sharedInstance().registerCallBack(this);
        RTCSuperClassImpl.sharedInstance().login(mStudentChannelId,userName);
        //开启方向传感器的监听
        initOrientationDetector();
        if (mOrientationDetector != null && mOrientationDetector.canDetectOrientation()) {
            mOrientationDetector.enable();
        }
    }


    /**
     * 绑定view
     */
    private void findViews() {
        mTopView = findViewById(R.id.rtc_super_class_view_top);
        mBottomView = findViewById(R.id.rtc_super_class_view_bottom);
        mControlView = findViewById(R.id.rtc_super_class_view_control);
        mTeacherContainView = findViewById(R.id.rtc_super_class_main_view);
        mSpeakingUserTv = findViewById(R.id.super_class_speaking_name);
        mLocalCameraView = findViewById(R.id.rtc_super_class_preview);
        mRcyStudentList = findViewById(R.id.super_class_rcy_student_list);
    }

    /**
     * 设置监听
     */
    private void setListensers(){
        mControlView.setmControlListener(this);
        mTeacherContainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击主页面时，隐藏或者显示控制按钮view
                if(mControlView.isShow()){
                    mControlView.hideControlView();
                    mTopView.setVisibility(View.GONE);
                    mBottomView.setVisibility(View.GONE);
                } else {
                    mControlView.showControlView();
                    mTopView.setVisibility(View.VISIBLE);
                    mBottomView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 自动旋转监听
     */
    private void initOrientationDetector() {
        mOrientationDetector = new OrientationDetector(RtcLiveRoomActivity.this);
        mOrientationDetector.setOrientationChangedListener(new OrientationDetector.OrientationChangedListener() {
            @Override
            public void onOrientationChanged() {
                int orientation = mOrientationDetector.getmOrientation();
                mCurrentPosition = 0;
                if ((orientation >= 35) && (orientation < 135)) {
                    mCurrentPosition = 90;
                }
                if ((orientation >= 200) && (orientation < 335)) {
                    mCurrentPosition = 270;
                }
                // 如果当前“自动旋转屏幕”未开启，则reture
                if(!ScreenUtil.isScreenAutoRotate(RtcLiveRoomActivity.this)){
                    return;
                }
                //此处的代码是解决手动旋转屏幕后，手机的自动旋转会失效的问题
                // 设置竖屏 “自动旋转屏幕”开启
                if (((orientation >= 0) && (orientation <= 30)) || (orientation >= 330)) {
                    if (mClick) {
                        if (mIsLand && !mClickLand) {
                            return;
                        } else {
                            mClickPort = true;
                            mClick = false;
                            mIsLand = false;
                        }
                    } else {
                        if (mIsLand) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            mIsLand = false;
                            mClick = false;
                        }
                    }
                } else if (mCurrentPosition == 90 || mCurrentPosition == 270) {
                    // 设置横屏 “自动旋转屏幕”开启
                    if (mClick) {
                        if (!mIsLand && !mClickPort) {
                            return;
                        } else {
                            mClickLand = true;
                            mClick = false;
                            mIsLand = true;
                        }
                    } else {
                        if (!mIsLand) {
                            if(mCurrentPosition == 90){
                                //右侧旋转
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                            } else {
                                //左侧旋转
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            }
                            mIsLand = true;
                            mClick = false;
                        }
                    }
                }
            }
        });
    }

    /**
     *
     * 旋转时的处理
     * @param config
     */
    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        if(config.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            //切换到竖屏
            //本地预览的画面移动到左下角
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(DensityUtils.dip2px(RtcLiveRoomActivity.this,120), DensityUtils.dip2px(RtcLiveRoomActivity.this,180));
            lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mLocalCameraView.setLayoutParams(lp1);
            lp1.setMargins(0,60,0,18);

            //"xxx正在发言" 放在学生列表画面的下面 水平居中
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(DensityUtils.dip2px(RtcLiveRoomActivity.this,120),DensityUtils.dip2px(RtcLiveRoomActivity.this,180));
            lp2.addRule(RelativeLayout.BELOW,R.id.super_class_rcy_student_list);
            lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mSpeakingUserTv.setLayoutParams(lp2);

            //学生列表横向展示
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRcyStudentList.setLayoutManager(linearLayoutManager);
            //传递竖屏方向给SDK
            RTCSuperClassImpl.sharedInstance().setDeviceOrientationMode(AliRtcEngine.AliRtcOrientationMode.AliRtcOrientationModePortrait);
        }else{
            //切换到横屏
            //本地预览画面移动到画面右侧，垂直居中
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(DensityUtils.dip2px(RtcLiveRoomActivity.this,120),DensityUtils.dip2px(RtcLiveRoomActivity.this,180));
            lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            //lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            //lp1.setMargins(0,60,17,0);
            lp1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            lp1.setMargins(0,0,17,0);
            mLocalCameraView.setLayoutParams(lp1);

           //"xxx正在发言" 放在主页的顶部展示 水平居中
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(DensityUtils.dip2px(RtcLiveRoomActivity.this,120),DensityUtils.dip2px(RtcLiveRoomActivity.this,180));
            lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp2.addRule(RelativeLayout.BELOW,0);
            //lp2.setMargins(0,15,0,0);
            mSpeakingUserTv.setLayoutParams(lp2);

            //学生列表竖向展示
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRcyStudentList.setLayoutManager(linearLayoutManager);
            //传递SDK数据当前屏幕为横屏
            if(mCurrentPosition == 90){
                RTCSuperClassImpl.sharedInstance().setDeviceOrientationMode(AliRtcEngine.AliRtcOrientationMode.AliRtcOrientationModeLandscapeRight);
            } else {
                RTCSuperClassImpl.sharedInstance().setDeviceOrientationMode(AliRtcEngine.AliRtcOrientationMode.AliRtcOrientationModeLandscapeLeft);
            }
        }
    }

    /**
     * 初始化学生列表
     */
    private void initStudentList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRcyStudentList.setLayoutManager(linearLayoutManager);
        if (mAlivcVideoStreamInfos == null) {
            mAlivcVideoStreamInfos = new ArrayList<>();
        }
        mStudentListAdapter = new StudentListAdapter(this, mAlivcVideoStreamInfos);
        mRcyStudentList.setAdapter(mStudentListAdapter);
    }

    /**
     * 将本地或远端预览信息添加到学生列表，并刷新显示
     * @param s
     * @param aliRtcVideoTrack
     * @param isFirst  是否是放在第一位
     */
    private void addVideoStreamInfo(String channelId,String s, AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack,AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack, boolean isFirst) {
        final AlivcVideoStreamInfo alivcVideoStreamInfo = createAlivcVideoStreamInfo(channelId,s, aliRtcVideoTrack, aliRtcAudioTrack, false);
        //如果当前学生列表已经存在添加的数据，那就是流的类型变了，刷新预览
        if (!mAlivcVideoStreamInfos.contains(alivcVideoStreamInfo)) {
            mAlivcVideoStreamInfos.add(isFirst ? 0 : mAlivcVideoStreamInfos.size(), alivcVideoStreamInfo);
        } else {
            mAlivcVideoStreamInfos.set(mAlivcVideoStreamInfos.indexOf(alivcVideoStreamInfo), alivcVideoStreamInfo);
        }
        UIHandlerUtil.getInstance().postRunnable(new Runnable() {
            @Override
            public void run() {
                mStudentListAdapter.notifyItemChanged(mAlivcVideoStreamInfos.indexOf(alivcVideoStreamInfo));
            }
        });
    }

    /**
     * 创建AlivcVideoStreamInfo对象
     *
     * 本地预览才需要muteLocalCamera数据
     * @param s 用户id
     * @param aliRtcVideoTrack 视频流类型
     * @param isLocalStream 是否为本地预览
     * @return 封装的视频流信息
     */
    private AlivcVideoStreamInfo createAlivcVideoStreamInfo(String channelId,String s, AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack,AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack, boolean isLocalStream) {
        AliRtcRemoteUserInfo userInfo = RTCSuperClassImpl.sharedInstance().getUserInfo(channelId,s);
        return new AlivcVideoStreamInfo.Builder()
                .setUserId(s)
                .setAliRtcVideoTrack(aliRtcVideoTrack)
                .setAliRtcAudioTrack(aliRtcAudioTrack)
                .setUserName(userInfo != null ? userInfo.getDisplayName() : "")
                .setLocalStream(isLocalStream)
                .setChannelId(channelId)
                .setIsTeacher(RTCSuperClassImpl.sharedInstance().isTeacherChannel(channelId))
                .setAliVideoCanvas(new AliRtcEngine.AliVideoCanvas())
                .build();
    }


    /**
     * 销毁view
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOrientationDetector != null) {
            mOrientationDetector.disable();
        }
        RTCDialogHelper.getInstance().release();
        RTCSuperClassImpl.sharedInstance().logout();
        RTCSuperClassImpl.sharedInstance().destorySharedInstance();
    }

    @Override
    public void onBackPressed() {
        leaveRoomClick();
    }

    /**
     * 控制器---切换摄像头
     */
    @Override
    public void switchCameraClick() {
        RTCSuperClassImpl.sharedInstance().switchCamera();
    }

    /**
     * 控制器---复制
     */
    @Override
    public void copyTitleClick(String title) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", title);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtils.showInCenter(RtcLiveRoomActivity.this,getResources().getString(R.string.alivc_superclass_string_copy_success));
    }

    /**
     * 控制器---离开
     */
    @Override
    public void leaveRoomClick() {
        //提示展示
        mRtcDialogHelper = RTCDialogHelper.getInstance();
        mRtcDialogHelper.setTitle(getString(R.string.alivc_superclass_string_leave_class));
        mRtcDialogHelper.setTipsTitle(getString(R.string.alivc_superclass_string_leave_class_message));
        mRtcDialogHelper.setConfirmText(getString(R.string.alivc_superclass_string_dialog_confirm));
        mRtcDialogHelper.setCancelText(getString(R.string.alivc_superclass_string_dialog_cancel));
        mRtcDialogHelper.setOnTipsDialogListener(new OnTipsDialogListener() {
            @Override
            public void onCancel() {
                mRtcDialogHelper.hideAll();
            }
            @Override
            public void onComfirm() {
                mRtcDialogHelper.hideAll();
                finish();
            }
        });
        if(!RtcLiveRoomActivity.this.isFinishing()){
            mRtcDialogHelper.showCustomTipsView(RtcLiveRoomActivity.this);
        }
    }

    /**
     * 当rtc sdk报错时弹出
     */
    private void showRtcErrorDialog() {
        //提示展示
        mRtcErrorDialogHelper = RTCDialogHelper.getInstance();
        mRtcErrorDialogHelper.setTitle(getString(R.string.alivc_superclass_string_title_dialog_tip));
        mRtcErrorDialogHelper.setTipsTitle(getString(R.string.alivc_superclass_string_error_rtc_normal));
        mRtcErrorDialogHelper.setConfirmText(getString(R.string.alivc_superclass_string_confrim_btn));
        mRtcErrorDialogHelper.setOnTipsDialogListener(new OnTipsDialogListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onComfirm() {
                finish();
            }
        });
        if(!RtcLiveRoomActivity.this.isFinishing()){
            mRtcErrorDialogHelper.showCustomTipsView(RtcLiveRoomActivity.this);
            mRtcErrorDialogHelper.hideCancelText();
        }

    }

    /**
     * 控制器---静音
     */
    @Override
    public void muteClick(boolean isMute) {
        RTCSuperClassImpl.sharedInstance().muteLocalMic(isConnectTeacher ? mTeacherChannelId : mStudentChannelId,isMute);
    }

    /**
     * 控制器---开关摄像头
     */
    @Override
    public void openCameraClick(boolean isOpen) {
        if(isOpen && !RTCSuperClassImpl.sharedInstance().isPreview(isConnectTeacher ? mTeacherChannelId : mStudentChannelId)){
            mLocalCameraView.setVisibility(View.VISIBLE);
            RTCSuperClassImpl.sharedInstance().startPreview(mLocalCameraView);
            RTCSuperClassImpl.sharedInstance().muteLocalCamera(isConnectTeacher ? mTeacherChannelId : mStudentChannelId,false);
        } else if(!isOpen){
            RTCSuperClassImpl.sharedInstance().stopPreview();
            RTCSuperClassImpl.sharedInstance().muteLocalCamera(isConnectTeacher ? mTeacherChannelId : mStudentChannelId,true);
            mLocalCameraView.removeAllViews();
            mLocalCameraView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 控制器---连麦老师
     */
    @Override
    public void connectTeacherClick(boolean isConnect) {
        if(!isConnectTeacher && isConnect){
            //连麦老师
            isConnectTeacher = true;
            mControlView.mute(false);
            mControlView.openCamera(true);
            RTCSuperClassImpl.sharedInstance().stopPublish(mStudentChannelId);
            RTCSuperClassImpl.sharedInstance().startPublish(mTeacherChannelId);
            openCameraClick(true);
        } else if(!isConnect && isConnectTeacher){
            //连麦小组
            isConnectTeacher = false;
            mControlView.mute(false);
            mControlView.openCamera(true);
            RTCSuperClassImpl.sharedInstance().stopPublish(mTeacherChannelId);
            RTCSuperClassImpl.sharedInstance().startPublish(mStudentChannelId);
            openCameraClick(true);
        }
    }

    /**
     * 控制器---小组列表
     */
    @Override
    public void teamMembersClick() {
        Intent intent = new Intent(RtcLiveRoomActivity.this,RtcTeamMembersListActivity.class);
        intent.putExtra(RtcTeamMembersListActivity.INTENT_LIST, (Serializable) RTCSuperClassImpl.sharedInstance().getRemoteUserList(mStudentChannelId));
        startActivity(intent);
    }


    /**
     * 控制器---旋转方向
     */
    @Override
    public void singleRotateClick(boolean isHorizontal) {
        mClick = true;
        if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
            //切换竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mIsLand = false;
            mClickPort = false;
        }else{
            //切换横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mIsLand = true;
            mClickLand = false;
        }
    }
    /**
     * 控制器---返回控制栏的自动隐藏
     */
    @Override
    public void hideViews() {
        mTopView.setVisibility(View.GONE);
        mBottomView.setVisibility(View.GONE);
    }


    //SDK的回调

    /**
     * 有人加入房间
     * @param channelId
     * @param userId    用户id
     */
    @Override
    public void onJoin(String channelId, String userId) {
    }

    /**
     * 有人退出房间
     * @param channelId
     * @param userId    用户id
     */
    @Override
    public void onLeave(String channelId, String userId) {
    }

    @Override
    public void onOccurError(String channelId, int error) {
        ToastUtils.showInCenter(RtcLiveRoomActivity.this,getString(R.string.alivc_superclass_string_error_code)+error);
    }

    @Override
    public void onOccurWarning(String channelId, int error) {
        ToastUtils.showInCenter(RtcLiveRoomActivity.this,getString(R.string.alivc_superclass_string_error_code)+error);
    }

    /**
     *
     *   被服务器踢出或者频道关闭时回调
     * @param channelId
     */
    @Override
    public void onRoomDestroy(String channelId) {
        mControlView.setIsTeacherLogin(false);
        mControlView.showEmptyView();
        mTeacherContainView.removeAllViews();
    }

    @Override
    public void onSDKError(String channelId, int error) {
        //销毁activity
        showRtcErrorDialog();
    }

    /**
     * 加入频道结果回调
     */
    @Override
    public void onJoinChannelResult(String channelId, int result) {
        if(RTCSuperClassImpl.sharedInstance().isTeacherChannel(channelId) && result == 0){
            mControlView.hideEmptyView();
            openCameraClick(true);
        }
    }

    @Override
    public void onLeaveChannelResult(String channelId, int result) {

    }

    @Override
    public void onNetworkQualityChanged(String channelId, String userId, AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality, AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality1) {

    }

    /**
     *
     * 用户关闭摄像头回调
     * @param channelId
     * @param userId
     * @param mute
     */
    @Override
    public void onUserVideoMuted(String channelId, String userId, boolean mute) {
        for(AlivcVideoStreamInfo info:mAlivcVideoStreamInfos){
            if(info.getUserId().equals(userId)){
                info.setMuteVideo(mute);
                mStudentListAdapter.notifyItemChanged(mAlivcVideoStreamInfos.indexOf(info),1);
                break;
            }
        }
    }

    /**
     * 用户静音麦克风回调
     * @param channelId
     * @param userId
     * @param mute
     */
    @Override
    public void onUserAudioMuted(String channelId, String userId, boolean mute) {
        for(AlivcVideoStreamInfo info:mAlivcVideoStreamInfos){
            if(info.getUserId().equals(userId)){
                info.setMuteAudio(mute);
                mStudentListAdapter.notifyItemChanged(mAlivcVideoStreamInfos.indexOf(info),1);
                break;
            }
        }
    }

    @Override
    public void onSubscribeResult(String channelId, String userId, int result, AliRtcEngine.AliRtcVideoTrack videoTrack, AliRtcEngine.AliRtcAudioTrack audioTrack) {

    }

    /**
     *
     * 用户是否在说话回调
     * @param channelId
     * @param userId     用户ID
     * @param isSpeaking 是否正在说话
     */
    @Override
    public void onUserSpeaking(String channelId, String userId, boolean isSpeaking) {
        //展示小组成员和助教的发言 TextView;
        StringBuilder builder = new StringBuilder();
        if(!RTCSuperClassImpl.sharedInstance().isTeacherChannel(channelId) && RTCSuperClassImpl.sharedInstance().getRoleType(channelId,userId) != RTCRoleType.RTCROLE_TEACHER){
            mSpeakingUserMap.put(userId,isSpeaking);
            for(Map.Entry<String, Boolean> entry : mSpeakingUserMap.entrySet()){
                if(entry.getValue()){
                    builder.append(RTCSuperClassImpl.sharedInstance().getUserInfo(channelId,entry.getKey()).getDisplayName()+"  ");
                }
            }
            mSpeakingUserTv.setText(TextUtils.isEmpty(builder.toString().trim()) ? "" : builder.toString() + getString(R.string.alivc_superclass_string_is_speaking));
        }


        //刷新列表展示（蓝色方框）
        for(AlivcVideoStreamInfo info:mAlivcVideoStreamInfos){
            if(info.getUserId().equals(userId)){
                info.setSpeaking(isSpeaking);
                mStudentListAdapter.notifyItemChanged(mAlivcVideoStreamInfos.indexOf(info),1);
                break;
            }
        }
    }

    /**
     *
     * 远端用户发布音视频流变化通知
     * @param channelId
     * @param userId     用户ID
     * @param audioTrack 订阅成功的音频流
     * @param videoTrack 订阅成功的视频流
     */
    @Override
    public void onRemoteTraceAvaliable(final String channelId, final String userId, AliRtcEngine.AliRtcAudioTrack audioTrack, AliRtcEngine.AliRtcVideoTrack videoTrack) {
        //学生端的流且不是助教，直接忽略   （若只显示老师端的流，则需要打开）
        //if(!RTCSuperClassImpl.sharedInstance().isTeacherChannel(channelId) && RTCSuperClassImpl.sharedInstance().getRoleType(channelId,userId) != RTCRoleType.RTCROLE_ASSISTANT){
        //    return;
        //}

        //如果流都处于关闭状态，则删除画面列表的显示
        if(audioTrack == AliRtcEngine.AliRtcAudioTrack.AliRtcAudioTrackNo && videoTrack == AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackNo){
            AlivcVideoStreamInfo alivcVideoStreamInfo = createAlivcVideoStreamInfo(channelId,userId, videoTrack, audioTrack,false);
            if (mAlivcVideoStreamInfos.contains(alivcVideoStreamInfo)) {
                int position = mAlivcVideoStreamInfos.indexOf(alivcVideoStreamInfo);
                mAlivcVideoStreamInfos.remove(position);
                mStudentListAdapter.notifyItemRemoved(position);
                mStudentListAdapter.notifyItemRangeChanged(position,mAlivcVideoStreamInfos.size()-position);
            } else if(RTCSuperClassImpl.sharedInstance().getRoleType(channelId,userId) == RTCRoleType.RTCROLE_TEACHER){
                //关闭老师画面
                mControlView.setIsTeacherLogin(false);
                mControlView.showEmptyView();
                mTeacherContainView.removeAllViews();
            }
            return;
        }

        //订阅老师的视频流（在主页面显示）
        if(RTCSuperClassImpl.sharedInstance().getRoleType(channelId,userId) == RTCRoleType.RTCROLE_TEACHER && RTCSuperClassImpl.sharedInstance().isTeacherChannel(channelId)){
            mControlView.setIsTeacherLogin(true);
            mControlView.hideEmptyView();
            RTCSuperClassImpl.sharedInstance().startPlay(channelId,userId,mTeacherContainView,videoTrack);
        }


        //助教和同学的小画面显示（相机流和屏幕流）
        if (RTCSuperClassImpl.sharedInstance().getRoleType(channelId,userId) != RTCRoleType.RTCROLE_TEACHER && !RTCSuperClassImpl.sharedInstance().isSelfUser(userId)) {
            addVideoStreamInfo(channelId,userId, videoTrack, audioTrack,false);
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RTCSuperClassImpl.sharedInstance().configRemoteCameraTrack(channelId,userId, false, true,true);
                }
            },300);
        } else if(RTCSuperClassImpl.sharedInstance().getRoleType(channelId,userId) == RTCRoleType.RTCROLE_TEACHER && videoTrack == AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackBoth){
            //大画面老师显示屏幕流时，小画面增加老师的相机流
            addVideoStreamInfo(channelId,userId, AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera,  audioTrack,true);
            RTCSuperClassImpl.sharedInstance().configRemoteCameraTrack(channelId,userId, true, true,true);
        } else if(RTCSuperClassImpl.sharedInstance().getRoleType(channelId,userId) == RTCRoleType.RTCROLE_TEACHER && videoTrack != AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackBoth){
            //大画面老师关闭屏幕流时，删除小画面列表的老师的相机流
            AlivcVideoStreamInfo alivcVideoStreamInfo = createAlivcVideoStreamInfo(channelId,userId, videoTrack, audioTrack,false);
            if (mAlivcVideoStreamInfos.contains(alivcVideoStreamInfo)) {
                int position = mAlivcVideoStreamInfos.indexOf(alivcVideoStreamInfo);
                mAlivcVideoStreamInfos.remove(position);
                mStudentListAdapter.notifyDataSetChanged();
            }
        }

    }
}
