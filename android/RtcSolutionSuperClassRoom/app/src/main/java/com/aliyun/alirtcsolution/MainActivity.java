package com.aliyun.alirtcsolution;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.rtc.superclassroom.activity.RtcLoginActivity;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRcyModelList;
    private int[] mModelIconList = new int[]{
            R.drawable.icon_audio_live_room
    };

    private int[] mModelNameList = new int[]{
           R.string.string_super_class_name
    };

    private Class[] mModelMainActivitys = new Class[]{
            RtcLoginActivity.class
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        mRcyModelList = findViewById(R.id.rcy_model_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRcyModelList.setLayoutManager(gridLayoutManager);
        mRcyModelList.setAdapter(new ModelAdapter());
    }

    private class ModelAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_model, parent, false);
            return new ModelViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ModelViewHolder) holder).bindView(position);
        }

        @Override
        public int getItemCount() {
            return mModelIconList.length;
        }

        private class ModelViewHolder extends RecyclerView.ViewHolder {
            ImageView ivModelIcon;
            TextView tvModelName;
            public ModelViewHolder(View view) {
                super(view);
                ivModelIcon = view.findViewById(R.id.iv_model_icon);
                tvModelName = view.findViewById(R.id.tv_model_name);
            }

            public void bindView(final int position) {
                ivModelIcon.setImageResource(mModelIconList[position]);
                tvModelName.setText(getString(mModelNameList[position]));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, mModelMainActivitys[position]);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
