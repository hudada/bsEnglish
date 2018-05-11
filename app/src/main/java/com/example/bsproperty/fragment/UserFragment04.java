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
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.BookBean;
import com.example.bsproperty.bean.BookListBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.ui.StudyListActivity;
import com.example.bsproperty.ui.WenInfoActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by wdxc1 on 2018/3/21.
 */

public class UserFragment04 extends BaseFragment {
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
    private MyAdapter adapter;
    private int currPosition = -1;

    @Override
    public void onResume() {
        super.onResume();
        loadWebData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && mContext != null) {
            loadWebData();
        }
    }

    private void loadWebData() {
        mData.clear();
        OkHttpTools.sendPost(mContext, ApiManager.BOOK_LIST)
                .build()
                .execute(new BaseCallBack<BookListBean>(mContext, BookListBean.class) {
                    @Override
                    public void onResponse(BookListBean bookListBean) {
                        mData = bookListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("美文分享");
        btnBack.setVisibility(View.GONE);
        btnRight.setText("资料下载");
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, StudyListActivity.class));
            }
        });
        btnRight.setVisibility(View.VISIBLE);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyAdapter(mContext, R.layout.item_wen, mData);
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
    public int getRootViewId() {
        return R.layout.fragment_user02;
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
