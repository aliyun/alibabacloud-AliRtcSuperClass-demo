package com.aliyun.rtc.superclassroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.rtc.superclassroom.R;
import com.aliyun.rtc.superclassroom.activity.base.BaseActivity;
import com.aliyun.rtc.superclassroom.adapter.RtcGroupListAdapter;
import com.aliyun.rtc.superclassroom.constants.Constants;

import java.util.List;


public class RtcGroupListActivity extends BaseActivity implements View.OnClickListener, RtcGroupListAdapter.OnSelectedListener {


    private RecyclerView mRecyclerView;
    private RtcGroupListAdapter mRtcGroupListAdapter;
    private String mSelectedGroup;
    private ImageView mBackImage;
    private TextView mDoneTv;

//    记录选择的小组位置，便于下次进入后选中该位置
    private int mGroupPosition = -1;


    @Override
    public int getLayoutId() {
        return R.layout.rtc_group_list_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        List<String> strings = Constants.getGroupList();
        mGroupPosition = getIntent().getIntExtra(RtcLoginActivity.GROUP_POSITION,-1);
        mSelectedGroup = mGroupPosition >= 0 ? strings.get(mGroupPosition):"";
        mBackImage = findViewById(R.id.alivc_superclass_back);
        mDoneTv = findViewById(R.id.alivc_superclass_done);
        mRecyclerView = findViewById(R.id.rtc_super_class_group_recycle);
        mBackImage.setOnClickListener(this);
        mDoneTv.setOnClickListener(this);
        mDoneTv.setEnabled(TextUtils.isEmpty(mSelectedGroup) ? false : true);

        mRtcGroupListAdapter = new RtcGroupListAdapter();
        mRtcGroupListAdapter.setSelectedPosition(mGroupPosition);
        mRtcGroupListAdapter.setDataList(strings);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(RtcGroupListActivity.this));
        mRecyclerView.setAdapter(mRtcGroupListAdapter);
        mRecyclerView.setItemAnimator(null);
//        去除下拉阴影
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRtcGroupListAdapter.setmOnSelectedListener(this);
    }




    @Override
    public void onBackPressed() {
        if(TextUtils.isEmpty(mSelectedGroup)){
            setResult(RESULT_CANCELED);
        } else {
            Intent data = new Intent();
            data.putExtra(RtcLoginActivity.GROUP_NAME,mSelectedGroup);
            data.putExtra(RtcLoginActivity.GROUP_POSITION,mGroupPosition);
            setResult(RESULT_OK,data);
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.alivc_superclass_back){
            onBackPressed();
        } else if(id == R.id.alivc_superclass_done){
            onBackPressed();
        }
    }

    @Override
    public void onSelected(int position, String name) {
        mDoneTv.setEnabled(true);
        mSelectedGroup = name;
        mGroupPosition = position;
    }
}
