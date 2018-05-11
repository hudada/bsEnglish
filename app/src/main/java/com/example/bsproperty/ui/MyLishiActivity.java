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
import com.example.bsproperty.bean.BookBean;
import com.example.bsproperty.bean.BookListBean;
import com.example.bsproperty.fragment.UserFragment04;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyLishiActivity extends BaseActivity {

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
    private ArrayList<BookBean> mData = new ArrayList<>();
    private MyLishiActivity.MyAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("我的历史");
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyLishiActivity.MyAdapter(mContext, R.layout.item_wen, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent = new Intent(mContext, WenInfoActivity.class);
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
        return R.layout.activity_my_lishi;
    }

    @Override
    protected void loadData() {
        OkHttpTools.sendPost(mContext, ApiManager.LIKE_LIST)
                .addParams("uid", MyApplication.getInstance().getUserBean().getId() + "")
                .build()
                .execute(new BaseCallBack<BookListBean>(mContext, BookListBean.class) {
                    @Override
                    public void onResponse(BookListBean bookListBean) {
                        mData = bookListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        this.finish();
    }

    private class MyAdapter extends BaseAdapter<BookBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<BookBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, BookBean bookBean, int position) {
            holder.setText(R.id.tv_name, bookBean.getTitle());
        }
    }
}
