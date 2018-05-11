package com.example.bsproperty.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.VoiceBean;
import com.example.bsproperty.bean.VoiceListBean;
import com.example.bsproperty.fragment.UserFragment02;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyPlayActivity extends BaseActivity

{

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
    private MyPlayActivity.MyAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("我的录音");
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyPlayActivity.MyAdapter(mContext, R.layout.item_voice, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent = new Intent(mContext, PlayInfoActivity.class);
                intent.putExtra("data", mData.get(position));
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
    protected int getRootViewId() {
        return R.layout.activity_my_play;
    }

    @Override
    protected void loadData() {
        OkHttpTools.sendPost(mContext, ApiManager.VOICE_MY_LIST)
                .addParams("uid", MyApplication.getInstance().getUserBean().getId() + "")
                .build()
                .execute(new BaseCallBack<VoiceListBean>(mContext, VoiceListBean.class) {
                    @Override
                    public void onResponse(VoiceListBean voiceListBean) {
                        mData = voiceListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        this.finish();
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
