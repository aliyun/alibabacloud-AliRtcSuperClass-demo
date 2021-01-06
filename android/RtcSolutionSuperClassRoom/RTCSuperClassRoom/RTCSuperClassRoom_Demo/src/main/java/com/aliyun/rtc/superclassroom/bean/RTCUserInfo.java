package com.aliyun.rtc.superclassroom.bean;

import android.support.annotation.Nullable;

import com.alivc.rtc.device.utils.StringUtils;

import java.io.Serializable;

public class RTCUserInfo implements Serializable {
    private String userName;
    private String userId;
    private boolean muteMic;


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

    public boolean isMuteMic() {
        return muteMic;
    }

    public void setMuteMic(boolean muteMic) {
        this.muteMic = muteMic;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof RTCUserInfo && StringUtils.equals(((RTCUserInfo) obj).userId, this.userId);
    }
}
