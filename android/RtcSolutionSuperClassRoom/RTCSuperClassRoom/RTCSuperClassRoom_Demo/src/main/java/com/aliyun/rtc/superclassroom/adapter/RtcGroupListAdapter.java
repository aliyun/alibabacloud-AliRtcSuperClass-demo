package com.aliyun.rtc.superclassroom.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.aliyun.rtc.superclassroom.R;

import java.util.List;

public class RtcGroupListAdapter extends RecyclerView.Adapter {


    private OnSelectedListener mOnSelectedListener;
    private List<String> mDataList;
    private int mSelectedPosition = -1;

    public void setmOnSelectedListener(OnSelectedListener mOnSelectedListener) {
        this.mOnSelectedListener = mOnSelectedListener;
    }

    public void setSelectedPosition(int mSelectedPosition) {
        this.mSelectedPosition = mSelectedPosition;
    }

    public void setDataList(List<String> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rtc_group_list_item, parent, false);
        GroupHolder groupHolder = new GroupHolder(view);
        return groupHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        position 从 0开始
        final GroupHolder viewHolder= (GroupHolder) holder;
        viewHolder.mGroupName.setText(mDataList.get(position));
        if(position == mSelectedPosition){
            viewHolder.mGroupSelect.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mGroupSelect.setVisibility(View.INVISIBLE);
        }
        viewHolder.mGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedPosition = position;
                notifyDataSetChanged();
                if(mOnSelectedListener != null){
                    mOnSelectedListener.onSelected(position,mDataList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private class GroupHolder extends RecyclerView.ViewHolder {
        private TextView mGroupName;
        private ImageView mGroupSelect;
        public GroupHolder(View itemView) {
            super(itemView);
            mGroupName = itemView.findViewById(R.id.rtc_super_class_group_name);
            mGroupSelect = itemView.findViewById(R.id.rtc_super_class_group_select);
        }
    }
    public interface OnSelectedListener {
        void onSelected(int position, String name);
    }

}
