package com.example.bsproperty.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.BookBean;
import com.example.bsproperty.bean.BookInfoBean;
import com.example.bsproperty.bean.ComentBean;
import com.example.bsproperty.bean.ComentListBean;
import com.example.bsproperty.bean.ShareObjBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WenInfoActivity extends BaseActivity {


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
    @BindView(R.id.et_reply)
    EditText etReply;
    @BindView(R.id.btn_reply)
    Button btnReply;
    private ArrayList<ComentBean> mData = new ArrayList<>();
    private WenInfoActivity.MyAdapter adapter;
    private BookBean bookBean;
    private LayoutInflater inflater;
    private boolean isLike;

    @Override
    protected void initView(Bundle savedInstanceState) {

        bookBean = (BookBean) getIntent().getSerializableExtra("data");
        inflater = LayoutInflater.from(mContext);


        tvTitle.setText("文章详情");
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyAdapter(mContext, R.layout.item_reply, mData);
        adapter.setmHeadView(R.layout.head_wen, new BaseAdapter.OnInitHead() {
            @Override
            public void onInitHeadData(View headView, Object o) {
                ((TextView) headView.findViewById(R.id.tv_name)).setText(bookBean.getTitle());
                LinearLayout linearLayout = (LinearLayout) headView.findViewById(R.id.ll_page);
                initPage(linearLayout);
                initLike(headView.findViewById(R.id.ll_like_click), ((ImageView) headView.findViewById(R.id.btn_xin)),
                        ((TextView) headView.findViewById(R.id.tv_xin)));
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

    private void initLike(final View viewById, final ImageView viewById1, final TextView viewById2) {
        OkHttpTools.sendPost(mContext, ApiManager.CHECK)
                .addParams("uid", MyApplication.getInstance().getUserBean().getId() + "")
                .addParams("bid", bookBean.getId() + "")
                .build()
                .execute(new BaseCallBack<ShareObjBean>(mContext, ShareObjBean.class) {
                    @Override
                    public void onResponse(ShareObjBean shareObjBean) {
                        if (shareObjBean.getData() == null) {
                            isLike = false;
                            viewById1.setImageResource(R.drawable.ic_favorite_white_24dp);
                        } else {
                            isLike = true;
                            viewById1.setImageResource(R.drawable.ic_favorite_red_300_24dp);
                        }
                        viewById2.setText("(" + bookBean.getLikeSum() + ")");
                        viewById.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OkHttpTools.sendPost(mContext, ApiManager.ADD_OR_SUB)
                                        .addParams("uid", MyApplication.getInstance().getUserBean().getId() + "")
                                        .addParams("bid", bookBean.getId() + "")
                                        .build()
                                        .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                                            @Override
                                            public void onResponse(BaseResponse baseResponse) {
                                                int like = bookBean.getLikeSum();

                                                if (isLike) {
                                                    isLike = false;
                                                    viewById1.setImageResource(R.drawable.ic_favorite_white_24dp);
                                                    like--;
                                                    bookBean.setLikeSum(like);
                                                } else {
                                                    isLike = true;
                                                    viewById1.setImageResource(R.drawable.ic_favorite_red_300_24dp);
                                                    like++;
                                                    bookBean.setLikeSum(like);
                                                }
                                                viewById2.setText("(" + bookBean.getLikeSum() + ")");
                                            }
                                        });
                            }
                        });
                    }
                });
    }

    private void initPage(LinearLayout linearLayout) {
        linearLayout.removeAllViews();
        for (final BookInfoBean bookInfoBean : bookBean.getBookInfoBeans()) {
            final View view = inflater.inflate(R.layout.item_wen_page, null, false);
            ((TextView) view.findViewById(R.id.tv_page)).setText(bookInfoBean.getMsg());
            ((TextView) view.findViewById(R.id.tv_page1)).setText("解释：" + bookInfoBean.getInfo());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.findViewById(R.id.tv_page1).setVisibility(View.VISIBLE);
                }
            });
            view.findViewById(R.id.ibtn_play).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.mSpeechSynthesizer.speak(bookInfoBean.getMsg());
                }
            });
            linearLayout.addView(view);
        }
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_wen_info;
    }

    @Override
    protected void loadData() {

        loadWebData();
    }

    private void loadWebData() {

        OkHttpTools.sendPost(mContext, ApiManager.COMMENT_LIST)
                .addParams("id", bookBean.getId() + "")
                .addParams("type", 0 + "")
                .build()
                .execute(new BaseCallBack<ComentListBean>(mContext, ComentListBean.class) {
                    @Override
                    public void onResponse(ComentListBean comentListBean) {
                        mData.clear();
                        mData = comentListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }


    @OnClick({R.id.btn_back, R.id.btn_reply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_reply:
                String et_reply = etReply.getText().toString().trim();
                if (TextUtils.isEmpty(et_reply)) {
                    showToast(etReply.getHint().toString());
                    return;
                }
                OkHttpTools.sendPost(mContext, ApiManager.COMMENT_ADD)
                        .addParams("id", bookBean.getId() + "")
                        .addParams("uid", MyApplication.getInstance().getUserBean().getId() + "")
                        .addParams("type", "0")
                        .addParams("msg", et_reply)
                        .build()
                        .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                            @Override
                            public void onResponse(BaseResponse baseResponse) {
                                showToast("评论成功");
                                etReply.setText("");
                                loadWebData();
                            }
                        });
                break;
        }
    }

    private class MyAdapter extends BaseAdapter<ComentBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<ComentBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, ComentBean comentBean, int position) {
            holder.setText(R.id.tv_name, "用户名：" + comentBean.getUserBean().getUserName());
            holder.setText(R.id.tv_total, "时间：" + MyApplication.format.format(comentBean.getTime()));
            holder.setText(R.id.tv_username, "评论：" + comentBean.getMsg());
        }
    }
}
