package com.aliyun.rtc.superclassroom.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.rtc.superclassroom.R;
import com.aliyun.rtc.superclassroom.activity.base.BaseActivity;
import com.aliyun.rtc.superclassroom.adapter.RtcTeamMembersAdapter;
import com.aliyun.rtc.superclassroom.bean.RTCUserInfo;
import com.aliyun.rtc.superclassroom.bean.TeamMembersParam;
import com.aliyun.rtc.superclassroom.rtc.RTCSuperClassImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class RtcTeamMembersListActivity extends BaseActivity implements View.OnClickListener {

    public final static String INTENT_LIST = "intent_list";
    private RecyclerView mRecyclerView;
    private RtcTeamMembersAdapter mRtcTeamMembersAdapter;
    private ImageView mBackImage;
    private TextView mTeamTitleTv;
    private ArrayList<RTCUserInfo> rtcUserInfos;



    @Override
    public int getLayoutId() {
        return R.layout.rtc_team_members_list_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rtcUserInfos =  (ArrayList<RTCUserInfo>) getIntent().getSerializableExtra(INTENT_LIST);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {

        mBackImage = findViewById(R.id.alivc_superclass_team_back);
        mTeamTitleTv = findViewById(R.id.alivc_superclass_team_title);
        mRecyclerView = findViewById(R.id.rtc_super_class_team_recycle);
        mTeamTitleTv.setText(getResources().getText(R.string.alivc_superclass_string_team_members)+"（"+rtcUserInfos.size()+"）");
        mBackImage.setOnClickListener(this);
        mRtcTeamMembersAdapter = new RtcTeamMembersAdapter();
        mRtcTeamMembersAdapter.setDataList(rtcUserInfos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(RtcTeamMembersListActivity.this));
        mRecyclerView.setAdapter(mRtcTeamMembersAdapter);
        mRecyclerView.setItemAnimator(null);
        //去除下拉阴影
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }






    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.alivc_superclass_team_back){
            onBackPressed();
        }
    }
}
