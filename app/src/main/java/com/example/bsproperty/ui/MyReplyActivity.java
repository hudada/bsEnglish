package com.example.bsproperty.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.ComentBean;
import com.example.bsproperty.bean.ComentListBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyReplyActivity extends BaseActivity {

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
    private ArrayList<ComentBean> mData = new ArrayList<>();
    private MyReplyActivity.MyAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("我的评论");
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyReplyActivity.MyAdapter(mContext, R.layout.item_myreply, mData);
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
        return R.layout.activity_my_reply;
    }

    @Override
    protected void loadData() {
        OkHttpTools.sendPost(mContext, ApiManager.COMMENT_MY_LIST)
                .addParams("id", MyApplication.getInstance().getUserBean().getId()+"")
                .build()
                .execute(new BaseCallBack<ComentListBean>(mContext, ComentListBean.class) {
                    @Override
                    public void onResponse(ComentListBean comentListBean) {
                        mData = comentListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        this.finish();
    }

    private class MyAdapter extends BaseAdapter<ComentBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<ComentBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, ComentBean comentBean, int position) {
            if (comentBean.getType()==0){
                holder.setText(R.id.tv_title, "(文章)"+comentBean.getBname());
            }else{
                holder.setText(R.id.tv_title, "(声音)"+comentBean.getVname());
            }
            holder.setText(R.id.tv_total, "时间：" + MyApplication.format.format(comentBean.getTime()));
            holder.setText(R.id.tv_username, "评论：" + comentBean.getMsg());
        }
    }
}
