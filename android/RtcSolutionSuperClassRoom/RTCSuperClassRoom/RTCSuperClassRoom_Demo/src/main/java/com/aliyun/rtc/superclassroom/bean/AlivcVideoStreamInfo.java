package com.aliyun.rtc.superclassroom.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.alivc.rtc.AliRtcEngine;
import com.alivc.rtc.device.utils.StringUtils;

public class AlivcVideoStreamInfo implements Parcelable {

    private String userId;
    private String userName;
    private String channelId;
    private AliRtcEngine.AliVideoCanvas aliVideoCanvas;
    private boolean isLocalStream;
    private boolean isTeacher;
    private boolean isSpeaking;
    private boolean isMuteVideo;
    private boolean isMuteAudio;
    private boolean muteLocalMic, muteLocalCamera;
    private AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack;
    private AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack;

    private AlivcVideoStreamInfo(Builder builder) {
        if (builder != null) {
            this.userId = builder.userId;
            this.userName = builder.userName;
            this.channelId = builder.channelId;
            this.aliVideoCanvas = builder.aliVideoCanvas;
            this.isTeacher = builder.isTeacher;
            this.isLocalStream = builder.isLocalStream;
            this.isSpeaking = builder.isSpeaking;
            this.isMuteVideo = builder.isMuteVideo;
            this.isMuteAudio = builder.isMuteAudio;
            this.muteLocalCamera = builder.muteLocalCamera;
            this.muteLocalMic = builder.muteLocalMic;
            this.aliRtcVideoTrack = builder.aliRtcVideoTrack;
            this.aliRtcAudioTrack = builder.aliRtcAudioTrack;
        }
    }

    protected AlivcVideoStreamInfo(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        channelId = in.readString();
        isSpeaking = in.readByte() != 0;
        isMuteVideo = in.readByte() != 0;
        isMuteAudio = in.readByte() != 0;
        isLocalStream = in.readByte() != 0;
        isTeacher = in.readByte() != 0;
        muteLocalMic = in.readByte() != 0;
        muteLocalCamera = in.readByte() != 0;
    }

    public static final Creator<AlivcVideoStreamInfo> CREATOR = new Creator<AlivcVideoStreamInfo>() {
        @Override
        public AlivcVideoStreamInfo createFromParcel(Parcel in) {
            return new AlivcVideoStreamInfo(in);
        }

        @Override
        public AlivcVideoStreamInfo[] newArray(int size) {
            return new AlivcVideoStreamInfo[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public AliRtcEngine.AliVideoCanvas getAliVideoCanvas() {
        return aliVideoCanvas;
    }

    public void setAliVideoCanvas(AliRtcEngine.AliVideoCanvas aliVideoCanvas) {
        this.aliVideoCanvas = aliVideoCanvas;
    }

    public boolean isMuteVideo() {
        return isMuteVideo;
    }

    public void setMuteVideo(boolean muteVideo) {
        isMuteVideo = muteVideo;
    }

    public boolean isMuteAudio() {
        return isMuteAudio;
    }

    public void setMuteAudio(boolean muteAudio) {
        isMuteAudio = muteAudio;
    }

    public boolean isSpeaking() {
        return isSpeaking;
    }

    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }

    public boolean isMuteLocalMic() {
        return muteLocalMic;
    }

    public void setMuteLocalMic(boolean muteLocalMic) {
        this.muteLocalMic = muteLocalMic;
    }

    public boolean isMuteLocalCamera() {
        return muteLocalCamera;
    }

    public void setMuteLocalCamera(boolean muteLocalCamera) {
        this.muteLocalCamera = muteLocalCamera;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public boolean isLocalStream() {
        return isLocalStream;
    }

    public void setLocalStream(boolean localStream) {
        isLocalStream = localStream;
    }

    public AliRtcEngine.AliRtcVideoTrack getAliRtcVideoTrack() {
        return aliRtcVideoTrack;
    }

    public void setAliRtcVideoTrack(AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack) {
        this.aliRtcVideoTrack = aliRtcVideoTrack;
    }

    public AliRtcEngine.AliRtcAudioTrack getAliRtcAudioTrack() {
        return aliRtcAudioTrack;
    }

    public void setAliRtcAudioTrack(AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack) {
        this.aliRtcAudioTrack = aliRtcAudioTrack;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof AlivcVideoStreamInfo && StringUtils.equals(((AlivcVideoStreamInfo) obj).userId, this.userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(channelId);
        dest.writeByte((byte) (isTeacher ? 1 : 0));
        dest.writeByte((byte) (isMuteVideo ? 1 : 0));
        dest.writeByte((byte) (isMuteAudio ? 1 : 0));
        dest.writeByte((byte) (isSpeaking ? 1 : 0));
        dest.writeByte((byte) (isLocalStream ? 1 : 0));
        dest.writeByte((byte) (muteLocalMic ? 1 : 0));
        dest.writeByte((byte) (muteLocalCamera ? 1 : 0));
    }

    public static class Builder {
        private String userId;
        private String userName;
        private String channelId;
        private AliRtcEngine.AliVideoCanvas aliVideoCanvas;
        private boolean isSpeaking;
        private boolean isTeacher;
        private boolean isMuteVideo;
        private boolean isMuteAudio;
        private boolean isLocalStream;
        private boolean muteLocalMic, muteLocalCamera;
        private AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack;
        private AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack;

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }
        public Builder setChannelId(String channel) {
            this.channelId = channel;
            return this;
        }

        public Builder setSpeaking(boolean speaking) {
            isSpeaking = speaking;
            return this;
        }
        public Builder setIsTeacher(boolean teacher) {
            isTeacher = teacher;
            return this;
        }
        public Builder setMuteVideo(boolean mute) {
            isMuteVideo = mute;
            return this;
        }
        public Builder setMuteAudio(boolean mute) {
            isMuteAudio = mute;
            return this;
        }

        public Builder setAliVideoCanvas(AliRtcEngine.AliVideoCanvas aliVideoCanvas) {
            this.aliVideoCanvas = aliVideoCanvas;
            return this;
        }

        public Builder setLocalStream(boolean localStream) {
            isLocalStream = localStream;
            return this;
        }

        public Builder setMuteLocalMic(boolean muteLocalMic) {
            this.muteLocalMic = muteLocalMic;
            return this;
        }

        public Builder setAliRtcVideoTrack(AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack) {
            this.aliRtcVideoTrack = aliRtcVideoTrack;
            return this;
        }

        public Builder setAliRtcAudioTrack(AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack) {
            this.aliRtcAudioTrack = aliRtcAudioTrack;
            return this;
        }

        public Builder setMuteLocalCamera(boolean muteLocalCamera) {
            this.muteLocalCamera = muteLocalCamera;
            return this;
        }

        public AlivcVideoStreamInfo build() {
            return new AlivcVideoStreamInfo(this);
        }
    }
}
