package com.aliyun.rtc.superclassroom.rtc;

import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.alivc.rtc.AliRtcAuthInfo;
import com.alivc.rtc.AliRtcEngine;
import com.alivc.rtc.AliRtcEngineEventListener;
import com.alivc.rtc.AliRtcEngineNotify;
import com.alivc.rtc.AliRtcRemoteUserInfo;
import com.alivc.rtc.device.utils.StringUtils;
import com.aliyun.rtc.superclassroom.api.impl.RTCSuperClassApiImpl;
import com.aliyun.rtc.superclassroom.api.impl.RTCSuperClassModelFactory;
import com.aliyun.rtc.superclassroom.api.net.OkhttpClient;
import com.aliyun.rtc.superclassroom.bean.IResponse;
import com.aliyun.rtc.superclassroom.bean.RTCRoleType;
import com.aliyun.rtc.superclassroom.bean.RTCUserInfo;
import com.aliyun.rtc.superclassroom.bean.RtcOnlineAuthInfo;
import com.aliyun.rtc.superclassroom.utils.ApplicationContextUtil;
import com.aliyun.rtc.superclassroom.utils.MockAliRtcAuthInfo;
import com.aliyun.rtc.superclassroom.utils.SPUtil;
import com.aliyun.rtc.superclassroom.utils.ThreadUtils;
import com.google.gson.JsonObject;

import org.webrtc.ali.Logging;
import org.webrtc.sdk.SophonSurfaceView;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.webrtc.alirtcInterface.ErrorCodeEnum.ERR_ICE_CONNECTION_HEARTBEAT_TIMEOUT;
import static org.webrtc.alirtcInterface.ErrorCodeEnum.ERR_SDK_INVALID_STATE;
import static org.webrtc.alirtcInterface.ErrorCodeEnum.ERR_SESSION_REMOVED;


public class RTCSuperClassImpl extends BaseRTCSuperClass {

    private static final String TAG = RTCSuperClassImpl.class.getSimpleName();
    private AliRtcEngine mTeacherEngine,mStudentEngine,mCurrentEngine;
    private static RTCSuperClassImpl mInstance;
    private RTCSuperClassCallback mRTCSuperClassCallback;
    private String mTeacherChannelId,mStudentChannelId;
    private String mTeacherUserName,mStudentUserName;
    private Handler mUiHandler;
    private String mLocalUserId;
    public static final int JOIN_CHANNEL_FAILD_CODE_BY_BAD_NETWORK = -1;
    public static final int JOIN_CHANNEL_FAILD_CODE_BY_DATA_EMPTY = -2;
    private RTCSuperClassApiImpl mRtcVideoLiveRoomApi;
    private AliRtcEngine.AliVideoCanvas mAliVideoCanvas;
    private boolean exitRoom;
    private Object mAudioVolumeChangeLock = new Object();
    private Map<String,AliRtcEngine> mRtcEngineMap;


    /**
     * 是否开始预览
     */
    private boolean isPreview = false;

    /**
     * 小组成员列表
     */
    private List<RTCUserInfo> userInfoList = new ArrayList<>();

    private RTCSuperClassImpl() {
        mRtcVideoLiveRoomApi = RTCSuperClassModelFactory.createRTCVideoLiveApi();
        mUiHandler = new Handler(Looper.getMainLooper());
        mRtcEngineMap = new HashMap<>();
        initEngine();
        //小组列表增加自己的显示
        RTCUserInfo rtcUserInfo = new RTCUserInfo();
        rtcUserInfo.setUserName(SPUtil.getInstance().getString(SPUtil.LOGIN_NAME,"")+"（我）");
        userInfoList.add(0,rtcUserInfo);
    }

    /***
     * 需要在主线程中调用
     */
    private void initEngine() {
        if (mTeacherEngine == null) {
            AliRtcEngine.setH5CompatibleMode(1);
            mTeacherEngine = AliRtcEngine.getInstance(ApplicationContextUtil.getAppContext());
            //默认开启扬声器
            mTeacherEngine.enableSpeakerphone(true);
            //设置频道模式为互动模式
            mTeacherEngine.setChannelProfile(AliRtcEngine.AliRTCSDK_Channel_Profile.AliRTCSDK_Communication);
            //设置不自动订阅，不自动发布
            mTeacherEngine.setAutoPublishSubscribe(false, false);
            //给相机流设置推流属性
            mTeacherEngine.setVideoProfile(AliRtcEngine.AliRtcVideoProfile.AliRTCSDK_Video_Profile_540_960P_15_1200Kb, AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera);
        }

        if (mStudentEngine == null) {
            AliRtcEngine.setH5CompatibleMode(1);
            mStudentEngine = mTeacherEngine.createChannel(new JsonObject().toString());
            //默认开启扬声器
            mStudentEngine.enableSpeakerphone(true);
            //设置频道模式为互动模式
            mStudentEngine.setChannelProfile(AliRtcEngine.AliRTCSDK_Channel_Profile.AliRTCSDK_Communication);
            //设置不自动订阅，不自动发布
            mStudentEngine.setAutoPublishSubscribe(false, false);
            //给相机流设置推流属性
            mStudentEngine.setVideoProfile(AliRtcEngine.AliRtcVideoProfile.AliRTCSDK_Video_Profile_540_960P_15_1200Kb, AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera);
        }
    }


    public static RTCSuperClassImpl sharedInstance() {
        if (mInstance == null) {
            synchronized (RTCSuperClassImpl.class) {
                if (mInstance == null) {
                    mInstance = new RTCSuperClassImpl();
                }
            }
        }
        return mInstance;
    }


    /**
     * 判断是否是老师端的channel
     * @param channerId
     * @return
     */
    public boolean isTeacherChannel(String channerId){
        if(TextUtils.isEmpty(mTeacherChannelId)){
            return channerId.equals(SPUtil.getInstance().getString(SPUtil.LOGIN_CLASS_CODE,""));
        }
        return channerId.equals(mTeacherChannelId);
    }



    /**
     *
     * 根据传入的userId判断身份
     * @param userId
     * @return
     */
    public RTCRoleType getRoleType(String channelId,String userId){
        AliRtcRemoteUserInfo userInfo = getUserInfo(channelId,userId);
        String displayName = userInfo.getDisplayName();
        RTCRoleType rtcRoleType = RTCRoleType.RTCROLE_STUDENT;
        if(!TextUtils.isEmpty(displayName) && displayName.length() > 2){
            String role = displayName.substring(displayName.length()-2,displayName.length());
            if(StringUtils.equals("老师",role)){
                rtcRoleType = RTCRoleType.RTCROLE_TEACHER;
            } else if(StringUtils.equals("助教",role)){
                rtcRoleType = RTCRoleType.RTCROLE_ASSISTANT;
            } else {
                rtcRoleType = RTCRoleType.RTCROLE_STUDENT;
            }
        }
        return rtcRoleType;
    }

    /**
     *
     * 根据传入的userId判断是否是自己
     * @param userId
     * @return
     */
    public boolean isSelfUser(String userId){
        if(userId.equals(mLocalUserId)){
            return true;
        }
        return false;
    }

    /**
     * 设置设备的方向
     * @param mode
     */
    public void setDeviceOrientationMode(AliRtcEngine.AliRtcOrientationMode mode){
        mTeacherEngine.setDeviceOrientationMode(mode);
    }


    /**
     * 设置是否订阅远端相机流。默认为订阅大流，手动订阅时，需要调用subscribe才能生效。
     * @param userId userid
     * @param master true为优先订阅大流，false为订阅次小流。
     * @param enable true为订阅远端相机流，false为停止订阅远端相机流。
     */
    public void configRemoteCameraTrack(String channelId,String userId, boolean master, boolean enable,boolean screenTrack) {
        mCurrentEngine = mRtcEngineMap.get(channelId);
        if(mCurrentEngine != null){
            mCurrentEngine.configRemoteCameraTrack(userId, master, enable);
            // 订阅远端音频流。
            mCurrentEngine.configRemoteAudio(userId, true);
            // 订阅远端屏幕流。
            mCurrentEngine.configRemoteScreenTrack(userId, screenTrack);
            mCurrentEngine.subscribe(userId);
        }
    }


    /**
     * 声音监听类
     */
    private class AliRtcAudioVolumeObserver extends AliRtcEngine.AliRtcAudioVolumeObserver{
        private String channelId;
        public AliRtcAudioVolumeObserver(String mChannelId) {
            this.channelId = mChannelId;
        }
        @Override
        public void onAudioVolume(List<AliRtcEngine.AliRtcAudioVolume> speakers, int i) {
            synchronized (mAudioVolumeChangeLock) {
                for (final AliRtcEngine.AliRtcAudioVolume aliRtcAudioVolume : speakers) {
                    //0代表的是自己
                    if(StringUtils.equals(aliRtcAudioVolume.mUserId, "0")){
                        continue;
                    }
                    final boolean isSpeaking = aliRtcAudioVolume.mSpeechstate == 1;
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mRTCSuperClassCallback != null) {
                                mRTCSuperClassCallback.onUserSpeaking(channelId,aliRtcAudioVolume.mUserId, isSpeaking);
                            }
                        }
                    });
                }
            }
        }
    }



    @Override
    public void destorySharedInstance() {
        destroy();
        mInstance = null;
        mUiHandler.removeCallbacksAndMessages(null);
        mUiHandler = null;
    }


    /**
     * 登入房间 登入后调用joinChannel
     * @param channelId 房间号
     * @param userName  昵称
     */
    @Override
    public void login(String channelId, String userName) {
        if(isTeacherChannel(channelId)){
            if(!mRtcEngineMap.containsKey(channelId)){
                mRtcEngineMap.put(channelId,mTeacherEngine);
            }
            mTeacherChannelId = channelId;
            mTeacherUserName = userName;
        } else {
            if(!mRtcEngineMap.containsKey(channelId)){
                mRtcEngineMap.put(channelId,mStudentEngine);
            }
            mStudentChannelId = channelId;
            mStudentUserName = userName;
        }
        mCurrentEngine = mRtcEngineMap.get(channelId);
        if(mCurrentEngine != null){
            //设置监听
            mCurrentEngine.setRtcEngineEventListener(new LocalAliRtcEngineEventListener(channelId));
            mCurrentEngine.setRtcEngineNotify(new RemoteAliRtcEngineNotify(channelId));
            //加入channel
            joinChannel(channelId,userName);
        }
        exitRoom = false;
    }

    /**
     * 登出房间，停止订阅
     */
    @Override
    public void logout() {
        exitRoom = true;
        if (mStudentEngine != null && mStudentEngine.isInCall()) {
            mStudentEngine.stopPreview();
            mStudentEngine.leaveChannel();
        }
        if (mTeacherEngine != null && mTeacherEngine.isInCall()) {
            mTeacherEngine.stopPreview();
            mTeacherEngine.leaveChannel();
        }
    }

    @Override
    public void muteLocalCamera(String channelId,boolean isMute) {
        initEngine();
        mCurrentEngine = mRtcEngineMap.get(channelId);
        if(mCurrentEngine != null){
            mCurrentEngine.muteLocalCamera(isMute, AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera);
        }
    }

    @Override
    public void muteLocalMic(String channelId,boolean isMute) {
        //小组列表中，自己的麦克风是否静音
        if(!userInfoList.isEmpty() && userInfoList.size() > 0){
            userInfoList.get(0).setMuteMic(isMute);
        }
        initEngine();
        mCurrentEngine = mRtcEngineMap.get(channelId);
        if(mCurrentEngine != null){
            mCurrentEngine.muteLocalMic(isMute);
        }

    }

    @Override
    public void switchCamera() {
        initEngine();
        mTeacherEngine.switchCamera();
    }

    @Override
    public AliRtcRemoteUserInfo getUserInfo(String channelId,String userId) {
        initEngine();
        mCurrentEngine = mRtcEngineMap.get(channelId);
        if(mCurrentEngine != null){
            return mCurrentEngine.getUserInfo(userId);
        }
        return new AliRtcRemoteUserInfo();


    }

    @Override
    public void registerCallBack(RTCSuperClassCallback rtcSuperClassCallback) {
        this.mRTCSuperClassCallback = rtcSuperClassCallback;
    }

    @Override
    public void startPreview(ViewGroup viewGroup) {
        if (mAliVideoCanvas == null) {
            mAliVideoCanvas = new AliRtcEngine.AliVideoCanvas();
        }
        SophonSurfaceView sophonSurfaceView = mAliVideoCanvas.view;
        if (sophonSurfaceView == null) {
            sophonSurfaceView = new SophonSurfaceView(viewGroup.getContext());
            sophonSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            // true 在最顶层，会遮挡一切view
            sophonSurfaceView.setZOrderOnTop(true);
            //true 如已绘制SurfaceView则在surfaceView上一层绘制。
            sophonSurfaceView.setZOrderMediaOverlay(true);
            mAliVideoCanvas.view = sophonSurfaceView;
            //设置渲染模式,一共有四种
            mAliVideoCanvas.renderMode = AliRtcEngine.AliRtcRenderMode.AliRtcRenderModeAuto;
        } else {
            ViewParent parent = sophonSurfaceView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(sophonSurfaceView);
            }
        }
        viewGroup.removeAllViews();
        viewGroup.addView(sophonSurfaceView);
        initEngine();
        mTeacherEngine.setLocalViewConfig(mAliVideoCanvas, AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera);
        mTeacherEngine.startPreview();
        isPreview = true;
    }

    @Override
    public void stopPreview() {
        mTeacherEngine.stopPreview();
        isPreview = false;
    }

    /**
     * 开启推流
     * @param channelId
     */
    @Override
    public void startPublish(String channelId) {
        mCurrentEngine = mRtcEngineMap.get(channelId);
        if(mCurrentEngine != null){
            mCurrentEngine.configLocalCameraPublish(true);
            mCurrentEngine.configLocalAudioPublish(true);
            //移动端不涉及屏幕分享
            mCurrentEngine.configLocalScreenPublish(false);
            mCurrentEngine.configLocalSimulcast(true, AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera);
            mCurrentEngine.publish();
        }
    }

    /**
     * 停止推流
     * @param channelId
     */
    @Override
    public void stopPublish(String channelId) {
        mCurrentEngine = mRtcEngineMap.get(channelId);
        if(mCurrentEngine != null){
            mCurrentEngine.configLocalCameraPublish(false);
            mCurrentEngine.configLocalAudioPublish(false);
            //移动端不涉及屏幕分享
            mCurrentEngine.configLocalScreenPublish(false);
            mCurrentEngine.configLocalSimulcast(false, AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera);
            mCurrentEngine.publish();
        }
    }

    @Override
    public void setRemoteViewConfig(String channelId,AliRtcEngine.AliVideoCanvas canvas, String uid, AliRtcEngine.AliRtcVideoTrack track) {
        Log.i(TAG, "setRemoteViewConfig: ");
        initEngine();
        mCurrentEngine = mRtcEngineMap.get(channelId);
        if(mCurrentEngine != null){
            mCurrentEngine.setRemoteViewConfig(canvas, uid, track);
        }
    }

    /**
     * 获取远端用户列表，目前只维护子频道的远端成员
     * @param channelId
     * @return
     */
    @Override
    public List<RTCUserInfo> getRemoteUserList(String channelId) {
        if(!isTeacherChannel(channelId)){
            return userInfoList;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isInCall(String channelId) {
        initEngine();
        mCurrentEngine = mRtcEngineMap.get(channelId);
        if(mCurrentEngine != null){
            return mCurrentEngine.isInCall();
        }
        return false;


    }


    @Override
    public boolean isPreview(String channelId) {
        return isPreview;
    }


    /**
     *
     * 开始老师的画面预览
     * @param channelId
     * @param uid
     * @param viewGroup 老师画面的父布局
     * @param aliRtcVideoTrack
     */
    public void startPlay(String channelId,String uid, ViewGroup viewGroup,AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack) {
        AliRtcEngine.AliVideoCanvas mAliVideoCanvas = new AliRtcEngine.AliVideoCanvas();
        SophonSurfaceView sophonSurfaceView = new SophonSurfaceView(viewGroup.getContext());
        sophonSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        // true 在最顶层，会遮挡一切view
        sophonSurfaceView.setZOrderOnTop(false);
        //true 如已绘制SurfaceView则在surfaceView上一层绘制。
        sophonSurfaceView.setZOrderMediaOverlay(false);
        mAliVideoCanvas.view = sophonSurfaceView;
        //设置渲染模式,一共有四种
        mAliVideoCanvas.renderMode = AliRtcEngine.AliRtcRenderMode.AliRtcRenderModeFill;
        //添加LocalView
        viewGroup.removeAllViews();
        viewGroup.addView(mAliVideoCanvas.view);
        initEngine();
        mCurrentEngine = mRtcEngineMap.get(channelId);
        if(mCurrentEngine != null){
            mCurrentEngine.setRemoteViewConfig(mAliVideoCanvas, uid,
                    aliRtcVideoTrack == AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackBoth?AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackScreen:aliRtcVideoTrack);
            mCurrentEngine.configRemoteAudio(uid,true);
            mCurrentEngine.configRemoteScreenTrack(uid,true);
            mCurrentEngine.configRemoteCameraTrack(uid,true,true);
            mCurrentEngine.subscribe(uid);
        }
    }


    /**
     * 销毁SDK
     */
    private void destroy() {
        if (mStudentEngine != null) {
            mStudentEngine.destroyChannel();
            mStudentEngine = null;
        }
        if (mTeacherEngine != null) {
            mTeacherEngine.destroy();
            mTeacherEngine = null;
        }
    }

    /**
     *
     * 加入房间
     * @param channelId
     * @param userName
     */
    private void joinChannel(final String channelId, final String userName){
        initEngine();
        //获取本地鉴权信息
        try {
            String userId = MockAliRtcAuthInfo.createUserId(channelId,userName);
            AliRtcAuthInfo aliRtcAuthInfo = MockAliRtcAuthInfo.mockAuthInfo(channelId,userId);
            if(isTeacherChannel(channelId)){
                mLocalUserId = aliRtcAuthInfo.getUserId();
            }
            mCurrentEngine = mRtcEngineMap.get(channelId);
            if(mCurrentEngine != null){
                mCurrentEngine.joinChannel(aliRtcAuthInfo, userName);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //获取网络鉴权信息
        //mRtcVideoLiveRoomApi.getRtcAuth(channelId, userName, new OkhttpClient.BaseHttpCallBack<IResponse<RtcOnlineAuthInfo>>() {
        //    @Override
        //    public void onSuccess(IResponse<RtcOnlineAuthInfo> data) {
        //        if (data != null && data.getData() != null) {
        //            RtcOnlineAuthInfo rtcAuthInfo = data.getData();
        //            AliRtcAuthInfo aliRtcAuthInfo = createAliRtcAuthInfo(rtcAuthInfo,channelId);
        //            if(isTeacherChannel(channelId)){
        //                mLocalUserId = aliRtcAuthInfo.getUserId();
        //            }
        //            mCurrentEngine = mRtcEngineMap.get(channelId);
        //            if(mCurrentEngine != null){
        //                mCurrentEngine.joinChannel(aliRtcAuthInfo, userName);
        //            }
        //        } else if (mRTCSuperClassCallback != null) {
        //            joinChannelResult(channelId,JOIN_CHANNEL_FAILD_CODE_BY_BAD_NETWORK);
        //        }
        //    }
        //
        //    @Override
        //    public void onError(String errorMsg) {
        //        joinChannelResult(channelId,JOIN_CHANNEL_FAILD_CODE_BY_BAD_NETWORK);
        //    }
        //});
    }

    private void joinChannelResult(final String channelId, final int result) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRTCSuperClassCallback != null) {
                    mRTCSuperClassCallback.onJoinChannelResult(channelId,result);
                }
            }
        });
    }


    /**
     * 生成本地auth
     * @param rtcAuthInfo c\
     * @param channelId
     * @return
     */
    private AliRtcAuthInfo createAliRtcAuthInfo(RtcOnlineAuthInfo rtcAuthInfo,String channelId) {
        if (rtcAuthInfo == null) {
            return null;
        }
        List<String> gslb = rtcAuthInfo.getGslb();
        AliRtcAuthInfo userInfo = new AliRtcAuthInfo();
        //频道ID
        //userInfo.setConferenceId(rtcAuthInfo.getChannelId());
        userInfo.setConferenceId(channelId);
        String appid = rtcAuthInfo.getAppid();
        /* 应用ID */
        userInfo.setAppid(appid);
        /* 随机码 */
        userInfo.setNonce(rtcAuthInfo.getNonce());
        /* 时间戳*/
        userInfo.setTimestamp(rtcAuthInfo.getTimestamp());
        String userid = rtcAuthInfo.getUserid();
        /* 用户ID
         * */
        userInfo.setUserId(userid);
        /* GSLB地址*/
        userInfo.setGslb(gslb.toArray(new String[0]));
        /*鉴权令牌Token*/
        userInfo.setToken(rtcAuthInfo.getToken());
        return userInfo;
    }


    /**
     * 本地用户行为回调
     */
    private class LocalAliRtcEngineEventListener extends AliRtcEngineEventListener{
        private String channelId;

        public LocalAliRtcEngineEventListener(String channelId) {
            this.channelId = channelId;
        }
        /**
         *
         * 推流结果回调
         */
        @Override
        public void onPublishChangedNotify(int result, boolean isPublished) {
            super.onPublishChangedNotify(result, isPublished);
            Logging.d(TAG, "onPublishChangedNotify: result --> " + result+ "----"+isPublished);
        }
        /**
         *
         * 加入频道结果回调
         */
        @Override
        public void onJoinChannelResult(int result) {
            Logging.d(TAG, "onJoinChannelResult: result --> " + result);
            joinChannelResult(channelId,result);
            if(isTeacherChannel(channelId)){
                mTeacherEngine.setVolumeCallbackIntervalMs(500, 3, 1);
                mTeacherEngine.registerAudioVolumeObserver(new AliRtcAudioVolumeObserver(channelId));
            } else {
                startPublish(channelId);
                mStudentEngine.setVolumeCallbackIntervalMs(500, 3, 1);
                mStudentEngine.registerAudioVolumeObserver(new AliRtcAudioVolumeObserver(channelId));
            }
        }

        /**
         * 离开房间的回调
         *
         * @param i 结果码
         */
        @Override
        public void onLeaveChannelResult(final int i) {
            Logging.d(TAG, "onLeaveChannelResult: i --> " + i);
            //不是退出房间，是下麦操作
            if (!exitRoom) {
                return;
            }
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mRTCSuperClassCallback != null) {
                        mRTCSuperClassCallback.onLeaveChannelResult(channelId,i);
                    }
                }
            });

        }

        /**
         * 网络状态变化的回调
         *
         * @param aliRtcNetworkQuality1 下行网络质量
         * @param aliRtcNetworkQuality  上行网络质量
         * @param s                     String  用户ID
         */
        @Override
        public void onNetworkQualityChanged(final String s, final AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality, final AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality1) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mRTCSuperClassCallback != null) {
                        mRTCSuperClassCallback.onNetworkQualityChanged(channelId,s, aliRtcNetworkQuality, aliRtcNetworkQuality1);
                    }
                }
            });
        }

        /**
         * 出现警告的回调
         *
         * @param i 错误码
         */
        @Override
        public void onOccurWarning(final int i) {
            Logging.d(TAG, "onOccurWarning: i --> " + i);
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mRTCSuperClassCallback != null) {
                        mRTCSuperClassCallback.onOccurWarning(channelId,i);
                    }
                }
            });
        }

        /**
         * 出现错误的回调
         *
         * @param error 错误码
         */
        @Override
        public void onOccurError(final int error) {
            Logging.d(TAG, "onOccurError: error --> " + error);
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mRTCSuperClassCallback != null) {
                        switch (error) {
                            case ERR_SDK_INVALID_STATE:
                            case ERR_ICE_CONNECTION_HEARTBEAT_TIMEOUT:
                            case ERR_SESSION_REMOVED:
                                //销毁activity
                                mRTCSuperClassCallback.onSDKError(channelId,error);
                                break;
                            default:
                                //不需要销毁SDK
                                mRTCSuperClassCallback.onOccurError(channelId,error);
                                break;
                        }
                    }
                }
            });
        }

        @Override
        public void onSubscribeChangedNotify(final String uid, final AliRtcEngine.AliRtcAudioTrack audioTrack, final AliRtcEngine.AliRtcVideoTrack videoTrack) {
            super.onSubscribeChangedNotify(uid, audioTrack, videoTrack);
            Logging.d(TAG, "onSubscribeChangedNotify: " + uid);
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mRTCSuperClassCallback != null) {
                        mRTCSuperClassCallback.onSubscribeResult(channelId,uid,1,videoTrack,audioTrack);
                    }
                }
            });
        }

    }

    /**
     * 远端用户行为回调
     */
    private class RemoteAliRtcEngineNotify extends AliRtcEngineNotify{
        private String channelId;

        public RemoteAliRtcEngineNotify(String channelId) {
            this.channelId = channelId;
        }
        /**
         *
         * 用户muteVideo通知回调
         * @param uid
         * @param isMute
         */
        @Override
        public void onUserVideoMuted(final String uid, final boolean isMute) {
            super.onUserVideoMuted(uid, isMute);
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mRTCSuperClassCallback != null) {
                        mRTCSuperClassCallback.onUserVideoMuted(channelId,uid,isMute);
                    }
                }
            });
        }
        /**
         *
         * 用户muteVideo通知回调
         * @param uid
         * @param isMute
         */
        @Override
        public void onUserAudioMuted(final String uid, final boolean isMute) {
            super.onUserVideoMuted(uid, isMute);
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RTCUserInfo rtcUserInfo = new RTCUserInfo();
                    rtcUserInfo.setUserId(uid);
                    rtcUserInfo.setMuteMic(isMute);
                    rtcUserInfo.setUserName(getUserInfo(channelId,uid).getDisplayName());
                    if(userInfoList.contains(rtcUserInfo)){
                        userInfoList.set(userInfoList.indexOf(rtcUserInfo),rtcUserInfo);
                    }
                    if (mRTCSuperClassCallback != null) {
                        mRTCSuperClassCallback.onUserAudioMuted(channelId,uid,isMute);
                    }
                }
            });
        }

        /**
         * 远端用户上线通知
         *
         * @param s userid
         */
        @Override
        public void onRemoteUserOnLineNotify(final String s) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isTeacherChannel(channelId)){
                        RTCUserInfo rtcUserInfo = new RTCUserInfo();
                        rtcUserInfo.setUserId(s);
                        rtcUserInfo.setUserName(getUserInfo(channelId,s).getDisplayName());
                        if(getRoleType(channelId,s) == RTCRoleType.RTCROLE_ASSISTANT){
                            rtcUserInfo.setUserName(getUserInfo(channelId,s).getDisplayName()+"（助教）");
                            userInfoList.add(1,rtcUserInfo);
                        } else {
                            userInfoList.add(rtcUserInfo);
                        }
                    }
                    if (mRTCSuperClassCallback != null) {
                        mRTCSuperClassCallback.onJoin(channelId,s);
                    }
                }
            });
        }

        /**
         * 远端用户下线通知
         *
         * @param s userid
         */
        @Override
        public void onRemoteUserOffLineNotify(final String s) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isTeacherChannel(channelId)){
                        RTCUserInfo rtcUserInfo = new RTCUserInfo();
                        rtcUserInfo.setUserId(s);
                        rtcUserInfo.setUserName(getUserInfo(channelId,s).getDisplayName());
                        if(userInfoList.contains(rtcUserInfo)){
                            userInfoList.remove(rtcUserInfo);
                        }
                    }
                    if (mRTCSuperClassCallback != null) {
                        mRTCSuperClassCallback.onLeave(channelId,s);
                    }
                }
            });
        }

        /**
         * 远端用户发布音视频流变化通知
         *
         * @param s                userid
         * @param aliRtcAudioTrack 音频流
         * @param aliRtcVideoTrack 相机流
         */
        @Override
        public void onRemoteTrackAvailableNotify(final String s, final AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack,
                                                 final AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack) {
            Logging.d(TAG, "onRemoteTrackAvailableNotify: s --> " + channelId+ "-----s="+getUserInfo(channelId,s).getDisplayName());
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //else if(!isTeacherChannel(channelId) && getRoleType(channelId,s) != RTCRoleType.RTCROLE_TEACHER){
                    //订阅学生端的音频流 （若只显示老师端的流，则需要打开）
                    //    configRemoteCameraTrack(channelId,s,false,false,false);
                    //}
                    if (mRTCSuperClassCallback != null) {
                        mRTCSuperClassCallback.onRemoteTraceAvaliable(channelId,s,aliRtcAudioTrack,aliRtcVideoTrack);
                    }
                }
            });
        }

        /**
         * 被服务器踢出或者频道关闭时回调
         */
        @Override
        public void onBye(int i) {
            Logging.d(TAG, "onBye: " + i);
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //用户被踢出房间（被服务器踢出）
                    if (mRTCSuperClassCallback != null) {
                        mRTCSuperClassCallback.onRoomDestroy(channelId);
                    }
                }
            });
        }
    }

}
