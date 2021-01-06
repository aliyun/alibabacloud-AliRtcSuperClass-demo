package com.aliyun.rtc.superclassroom.listener;

/**
 * 控制页面点击回调
 */
public interface ControlListener {
    void switchCameraClick();
    void copyTitleClick(String title);
    void leaveRoomClick();
    void muteClick(boolean isMute);
    void openCameraClick(boolean isOpen);
    void connectTeacherClick(boolean isConnect);
    void teamMembersClick();
    void singleRotateClick(boolean isHorizontal);
    void hideViews();
}
