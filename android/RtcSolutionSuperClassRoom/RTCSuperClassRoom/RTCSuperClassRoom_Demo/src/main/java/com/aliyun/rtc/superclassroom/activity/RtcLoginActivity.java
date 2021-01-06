package com.aliyun.rtc.superclassroom.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.rtc.superclassroom.TextWatcherUtil;
import com.aliyun.rtc.superclassroom.R;
import com.aliyun.rtc.superclassroom.activity.base.BaseActivity;
import com.aliyun.rtc.superclassroom.listener.UserInputListener;
import com.aliyun.rtc.superclassroom.utils.SPUtil;
import com.aliyun.rtc.superclassroom.utils.UIHandlerUtil;
import com.aliyun.rtc.superclassroom.view.LoadingDrawable;
import com.aliyun.svideo.common.utils.ToastUtils;


/**
 * 登录页
 */
public class RtcLoginActivity extends BaseActivity implements View.OnClickListener {
    public final static String GROUP_NAME = "group_name";
    public final static String GROUP_POSITION = "group_position";
    private final int mRequestCode = 1001;
    //记录选择的小组位置，便于下次进入后选中该位置
    private int mGroupPosition = -1;

    /**
     * 是否输入了名字、教室码、小组
     *
     * */
    private boolean isHasName;
    private boolean isHasClassCode;
    private boolean isHasGroup;


    private RelativeLayout mRelativeLayout;
    private EditText mNameEdit;
    private EditText mClassCodeEdit;
    private TextView mGroupTv;
    private TextView mEnterRoomTv;

    @Override
    public int getLayoutId() {
        return R.layout.rtc_superclass_login_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupPosition = SPUtil.getInstance().getInt(SPUtil.LOGIN_GROUP_INDEX,-1);
        initView();

    }

    /**
     * 初始化view
     */
    private void initView() {
        mRelativeLayout = findViewById(R.id.alivc_superclass_back);
        mNameEdit = findViewById(R.id.alivc_superclass_edittext_channelid);
        mClassCodeEdit = findViewById(R.id.alivc_superclass_edittext_classcode);
        mGroupTv = findViewById(R.id.alivc_superclass_edittext_group);
        mEnterRoomTv = findViewById(R.id.alivc_superclass_tv_enter_class);
        TextWatcherUtil watcherUtil = new TextWatcherUtil();
        watcherUtil.setUserInputListener(new UserInputListener() {
            @Override
            public void getUserName(boolean hasName) {
                isHasName = hasName;
                checkEnterEnable();

            }

            @Override
            public void getClassCode(boolean hasClassCode) {
                isHasClassCode = hasClassCode;
                checkEnterEnable();
            }
        });
        //给名字和教室码输入添加监听
        watcherUtil.addClassCodeTextWatch(mClassCodeEdit);
        watcherUtil.addNameTextWatch(mNameEdit);
        mEnterRoomTv.setOnClickListener(this);
        mRelativeLayout.setOnClickListener(this);
        mGroupTv.setOnClickListener(this);
        mNameEdit.setText(SPUtil.getInstance().getString(SPUtil.LOGIN_NAME,""));
        mClassCodeEdit.setText(SPUtil.getInstance().getString(SPUtil.LOGIN_CLASS_CODE,""));
        mGroupTv.setText(TextUtils.isEmpty(SPUtil.getInstance().getString(SPUtil.LOGIN_GROUP,"")) ? "" :
                SPUtil.getInstance().getString(SPUtil.LOGIN_GROUP,"") +  getString(R.string.alivc_superclass_string_group_end));
        isHasGroup = !TextUtils.isEmpty(mGroupTv.getText().toString());
        checkEnterEnable();
    }


    /**
     * 检查进入教室按钮是否可点击
     */
    private void checkEnterEnable(){
        if(isHasName && isHasClassCode && isHasGroup){
            mEnterRoomTv.setEnabled(true);
        } else {
            mEnterRoomTv.setEnabled(false);
        }
    }

    /**
     * 开始通话按钮显示loading画面
     */
    private void showLoadingView() {
        mEnterRoomTv.setText(R.string.alivc_superclass_string_enter_loading);
        LoadingDrawable loadingDrawable = new LoadingDrawable();
        int width = mEnterRoomTv.getWidth();
        int height = mEnterRoomTv.getHeight();
        int left = width / 2 - height;
        int right = width / 2;
        loadingDrawable.setBounds(left, 0, right, height);
        mEnterRoomTv.setCompoundDrawables(loadingDrawable, null, null, null);
    }

    /**
     * 隐藏loading画面
     */
    private void hideLoadingView() {
        UIHandlerUtil.getInstance().postRunnable(new Runnable() {
            @Override
            public void run() {
                mEnterRoomTv.setText(R.string.alivc_superclass_string_enter_classroom);
                mEnterRoomTv.setCompoundDrawables(null, null, null, null);
            }
        });
    }

    /**
     * 隐藏键盘
     */
    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.alivc_superclass_tv_enter_class){
            //进入教室
            showLoadingView();
            SPUtil.getInstance().putString(SPUtil.LOGIN_NAME,mNameEdit.getText().toString());
            SPUtil.getInstance().putString(SPUtil.LOGIN_CLASS_CODE,mClassCodeEdit.getText().toString().replaceAll(" ",""));
            SPUtil.getInstance().putString(SPUtil.LOGIN_GROUP,mGroupTv.getText().toString().substring(0,1));
            Intent intent = new Intent(RtcLoginActivity.this, RtcLiveRoomActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.alivc_superclass_back){
            //点击背景隐藏键盘
            hideSoftInput();
        } else if(id == R.id.alivc_superclass_edittext_group){
            //点击小组跳转到小组列表
            Intent intent = new Intent(RtcLoginActivity.this, RtcGroupListActivity.class);
            intent.putExtra(GROUP_POSITION,mGroupPosition);
            startActivityForResult(intent,mRequestCode);
        }
    }

    /**
     *
     * 从小组列表返回
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == mRequestCode){
            if(resultCode == RESULT_OK){
                isHasGroup = true;
                mGroupTv.setText(data.getStringExtra(GROUP_NAME));
                mGroupPosition = data.getIntExtra(GROUP_POSITION,-1);
                SPUtil.getInstance().putInt(SPUtil.LOGIN_GROUP_INDEX,mGroupPosition);
            } else {
                isHasGroup = false;
                ToastUtils.showInCenter(RtcLoginActivity.this,getString(R.string.alivc_superclass_string_not_choose));
            }
            checkEnterEnable();
        }

    }
}
