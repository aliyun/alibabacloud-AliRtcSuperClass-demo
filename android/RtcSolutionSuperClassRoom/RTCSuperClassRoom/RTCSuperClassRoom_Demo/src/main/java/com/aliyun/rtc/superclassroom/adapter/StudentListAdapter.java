package com.aliyun.rtc.superclassroom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alivc.rtc.AliRtcEngine;
import com.aliyun.rtc.superclassroom.R;
import com.aliyun.rtc.superclassroom.bean.AlivcVideoStreamInfo;
import com.aliyun.rtc.superclassroom.rtc.RTCSuperClassImpl;

import org.webrtc.sdk.SophonSurfaceView;

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<AlivcVideoStreamInfo> mAlivcVideoStreamInfos;

    public StudentListAdapter(Context context, List<AlivcVideoStreamInfo> alivcVideoStreamInfos) {
        mContext = context;
        mAlivcVideoStreamInfos = alivcVideoStreamInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rtc_student_list, parent, false);
        return new StudentListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
        ((StudentListHolder) holder).bindView(position,payloads);
    }

    @Override
    public int getItemCount() {
        return mAlivcVideoStreamInfos == null ? 0 : mAlivcVideoStreamInfos.size();
    }


    private class StudentListHolder extends RecyclerView.ViewHolder {

        private final FrameLayout mRlContainer;
        private final TextView mTvStudentName;
        private final ImageView mIvMuteLocalMic;
        private final LinearLayout mCameraCloseView;
        private final RelativeLayout mMainView;

        private StudentListHolder(View view) {
            super(view);
            mRlContainer = view.findViewById(R.id.alivc_big_interactive_class_rl_student_preview_container);
            mTvStudentName = view.findViewById(R.id.alivc_big_interactive_class_tv_student_name);
            mIvMuteLocalMic = view.findViewById(R.id.alivc_big_interactive_class_iv_student_mutelocalmic);
            mCameraCloseView = view.findViewById(R.id.rtc_super_class_camera_close);
            mMainView = view.findViewById(R.id.rtc_super_class_item_main);
        }

        private void bindView(final int position,final List payloads) {
            AlivcVideoStreamInfo alivcVideoStreamInfo = null;
            if (position < mAlivcVideoStreamInfos.size()) {
                alivcVideoStreamInfo = mAlivcVideoStreamInfos.get(position);
            }
            if (alivcVideoStreamInfo == null) {
                return;
            }
            if(payloads.isEmpty()){
                //复用，防止重复添加
                mTvStudentName.setText(alivcVideoStreamInfo.getUserName());
                mMainView.setBackgroundColor(alivcVideoStreamInfo.isSpeaking()? mContext.getResources().getColor(R.color.alivc_superclass_color_user_name_bg) : mContext.getResources().getColor(R.color.alivc_superclass_color_bg_gray));
                mIvMuteLocalMic.setImageResource(alivcVideoStreamInfo.getAliRtcAudioTrack() == AliRtcEngine.AliRtcAudioTrack.AliRtcAudioTrackMic ?  R.drawable.alivc_superclass_byn_list_unmute_icon:R.drawable.alivc_superclass_byn_list_mute_icon);
                if(alivcVideoStreamInfo.getAliRtcVideoTrack() == AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackNo){
                    mRlContainer.setVisibility(View.GONE);
                    mCameraCloseView.setVisibility(View.VISIBLE);
                } else {
                    mRlContainer.setVisibility(View.VISIBLE);
                    mCameraCloseView.setVisibility(View.GONE);
                }
                SophonSurfaceView sophonSurfaceView = alivcVideoStreamInfo.getAliVideoCanvas().view;
                if (sophonSurfaceView == null) {
                    sophonSurfaceView = new SophonSurfaceView(itemView.getContext());
                    sophonSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
                    // true 在最顶层，会遮挡一切view
                    sophonSurfaceView.setZOrderOnTop(false);
                    //true 如已绘制SurfaceView则在surfaceView上一层绘制。
                    sophonSurfaceView.setZOrderMediaOverlay(true);
                    alivcVideoStreamInfo.getAliVideoCanvas().view = sophonSurfaceView;
                    //设置渲染模式,一共有四种
                    alivcVideoStreamInfo.getAliVideoCanvas().renderMode = AliRtcEngine.AliRtcRenderMode.AliRtcRenderModeFill;
                    mRlContainer.removeAllViews();
                    mRlContainer.addView(sophonSurfaceView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                    displayStream(alivcVideoStreamInfo);
                } else {
                    Log.d("spoonsurfaceview====","已经添加并开始预览就只切换展示的spoonsurfaceview");
                    //已经添加并开始预览就只切换展示的spoonsurfaceview
                    if (sophonSurfaceView.getParent() != null) {
                        ((ViewGroup) sophonSurfaceView.getParent()).removeView(sophonSurfaceView);
                    }
                    // true 在最顶层，会遮挡一切view
                    sophonSurfaceView.setZOrderOnTop(false);
                    //true 如已绘制SurfaceView则在surfaceView上一层绘制。
                    sophonSurfaceView.setZOrderMediaOverlay(true);
                    mRlContainer.removeAllViews();
                    mRlContainer.addView(sophonSurfaceView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                }
            } else {
                //这里可以刷新item的局部view，防止画面预览view出现黑屏抖动
                int type= (int) payloads.get(0);
                if(type == 1){
                    mMainView.setBackgroundColor(alivcVideoStreamInfo.isSpeaking()? mContext.getResources().getColor(R.color.alivc_superclass_color_user_name_bg) : mContext.getResources().getColor(R.color.alivc_superclass_color_bg_gray));
                    mIvMuteLocalMic.setImageResource(alivcVideoStreamInfo.isMuteAudio() ? R.drawable.alivc_superclass_byn_list_mute_icon : R.drawable.alivc_superclass_byn_list_unmute_icon);
                    if(alivcVideoStreamInfo.isMuteVideo()){
                        mCameraCloseView.setVisibility(View.VISIBLE);
                    } else {
                        mCameraCloseView.setVisibility(View.GONE);
                    }

                }

            }
        }
    }

    /**
     * 展示远端流
     */
    private void displayStream(final AlivcVideoStreamInfo alivcVideoStreamInfo) {
        AliRtcEngine.AliVideoCanvas aliVideoCanvas = alivcVideoStreamInfo.getAliVideoCanvas();
        if (aliVideoCanvas != null) {
            AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack = alivcVideoStreamInfo.getAliRtcVideoTrack() == AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackBoth ? AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackScreen : alivcVideoStreamInfo.getAliRtcVideoTrack();
            RTCSuperClassImpl.sharedInstance().setRemoteViewConfig(alivcVideoStreamInfo.getChannelId(),aliVideoCanvas, alivcVideoStreamInfo.getUserId(), aliRtcVideoTrack);
        }
    }

}
