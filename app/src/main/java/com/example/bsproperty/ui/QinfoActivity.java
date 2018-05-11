package com.example.bsproperty.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.WordBean;
import com.example.bsproperty.bean.WordWebListBean;
import com.example.bsproperty.fragment.UserFragment01;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QinfoActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private ArrayList<WordBean> mData = new ArrayList<>();
    private MyAdapter adapter;
    private int mType;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("搜索结果");
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyAdapter(mContext, R.layout.item_word, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent = new Intent(mContext, WordInfoActivity.class);
                intent.putExtra("data", mData.get(position));
                startActivity(intent);
            }
        });
        rvList.setAdapter(adapter);

    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_qinfo;
    }

    @Override
    protected void loadData() {
        String key = getIntent().getStringExtra("key");
        mType = getIntent().getIntExtra("type", 0);
        OkHttpTools.sendPost(mContext, ApiManager.SEARCH_LIST)
                .addParams("key", key)
                .addParams("type", mType + "")
                .build()
                .execute(new BaseCallBack<WordWebListBean>(mContext, WordWebListBean.class) {
                    @Override
                    public void onResponse(WordWebListBean wordWebListBean) {
                        mData = wordWebListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        this.finish();
    }


    private class MyAdapter extends BaseAdapter<WordBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<WordBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, WordBean wordBean, int position) {
            if (mType == 0) {
                holder.setText(R.id.tv_word, wordBean.getWordEn());
                holder.setText(R.id.tv_info, "解释：" + wordBean.getWordCh());
            } else {
                holder.setText(R.id.tv_word, wordBean.getWordCh());
                holder.setText(R.id.tv_info, "英文：" + wordBean.getWordEn());
            }

        }
    }

}
