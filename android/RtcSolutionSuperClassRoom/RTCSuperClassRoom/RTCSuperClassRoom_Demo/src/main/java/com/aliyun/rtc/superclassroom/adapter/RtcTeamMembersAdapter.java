package com.aliyun.rtc.superclassroom.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alivc.rtc.AliRtcRemoteUserInfo;
import com.aliyun.rtc.superclassroom.R;
import com.aliyun.rtc.superclassroom.bean.RTCUserInfo;
import com.aliyun.rtc.superclassroom.bean.TeamMembersParam;

import java.util.List;

public class RtcTeamMembersAdapter extends RecyclerView.Adapter {

    private List<RTCUserInfo> mDataList;


    public void setDataList(List<RTCUserInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rtc_team_members_list_item, parent, false);
        GroupHolder groupHolder = new GroupHolder(view);
        return groupHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        position 从 0开始
        final GroupHolder viewHolder= (GroupHolder) holder;
        viewHolder.mTeamName.setText(mDataList.get(position).getUserName());
        if(!mDataList.get(position).isMuteMic()){
            viewHolder.mTeamPic.setImageResource(R.drawable.alivc_superclass_byn_mute_able3x);
        } else {
            viewHolder.mTeamPic.setImageResource(R.drawable.alivc_superclass_byn_mute_disable3x);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private class GroupHolder extends RecyclerView.ViewHolder {
        private TextView mTeamName;
        private ImageView mTeamPic;
        public GroupHolder(View itemView) {
            super(itemView);
            mTeamName = itemView.findViewById(R.id.rtc_super_class_team_name);
            mTeamPic = itemView.findViewById(R.id.rtc_super_class_team_mic);
        }
    }

}
