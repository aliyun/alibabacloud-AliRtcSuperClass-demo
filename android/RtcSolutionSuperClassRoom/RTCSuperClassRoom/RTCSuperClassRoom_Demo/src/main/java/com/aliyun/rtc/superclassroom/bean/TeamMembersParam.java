package com.aliyun.rtc.superclassroom.bean;

/**
 * 小组成员列表的参数
 */
public class TeamMembersParam {
    private String name;
    private boolean isMicOn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMicOn() {
        return isMicOn;
    }

    public void setMicOn(boolean micOn) {
        isMicOn = micOn;
    }
}
