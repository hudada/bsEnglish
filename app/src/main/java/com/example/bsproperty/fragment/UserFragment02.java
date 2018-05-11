package com.example.bsproperty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.SongBean;
import com.example.bsproperty.bean.SongListBean;
import com.example.bsproperty.bean.VoiceBean;
import com.example.bsproperty.bean.VoiceListBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.ui.PlayInfoActivity;
import com.example.bsproperty.ui.SongDetailActivity;
import com.example.bsproperty.utils.Player;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by wdxc1 on 2018/3/21.
 */

public class UserFragment02 extends BaseFragment {
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;

    private ArrayList<VoiceBean> mData = new ArrayList<>();
    private MyAdapter adapter;
    private int currPosition = -1;

    @Override
    public void onResume() {
        super.onResume();
        loadWebData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
    }

    private void loadWebData() {
        mData.clear();
        OkHttpTools.sendPost(mContext, ApiManager.VOICE_LIST)
                .build()
                .execute(new BaseCallBack<VoiceListBean>(mContext, VoiceListBean.class) {
                    @Override
                    public void onResponse(VoiceListBean voiceListBean) {
                        mData = voiceListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("口语广场");
        btnBack.setVisibility(View.GONE);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyAdapter(mContext, R.layout.item_voice, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent = new Intent(getContext(), PlayInfoActivity.class);
                intent.putExtra("data",mData.get(position));
                startActivity(intent);
            }
        });
        rvList.setAdapter(adapter);
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                slList.setRefreshing(false);
            }
        });
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_user02;
    }

    private class MyAdapter extends BaseAdapter<VoiceBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<VoiceBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, VoiceBean voiceBean, int position) {
            holder.setText(R.id.tv_name, voiceBean.getTitle());
            holder.setText(R.id.tv_username, "发布者：" + voiceBean.getName());
            holder.setText(R.id.tv_date, "发布日期：" + MyApplication.format.format(voiceBean.getTime()));
        }
    }

}
