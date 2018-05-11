package com.example.bsproperty.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.ComentBean;
import com.example.bsproperty.bean.ComentListBean;
import com.example.bsproperty.bean.VoiceBean;
import com.example.bsproperty.fragment.UserFragment02;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.utils.Player;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayInfoActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_like_click)
    LinearLayout llLikeClick;
    @BindView(R.id.btn_play)
    ImageButton btnPlay;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    @BindView(R.id.et_reply)
    EditText etReply;
    @BindView(R.id.btn_reply)
    Button btnReply;
    private ArrayList<ComentBean> mData = new ArrayList<>();
    private PlayInfoActivity.MyAdapter adapter;
    private VoiceBean voiceBean;
    private boolean isPlay;
    private Player player;

    @Override
    protected void initView(Bundle savedInstanceState) {
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new PlayInfoActivity.MyAdapter(mContext, R.layout.item_reply, mData);
        rvList.setAdapter(adapter);
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                slList.setRefreshing(false);
            }
        });
        tvTitle.setText("声音详情");
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_play_info;
    }

    @Override
    protected void loadData() {
        voiceBean = (VoiceBean) getIntent().getSerializableExtra("data");
        tvName.setText(voiceBean.getTitle());
        tvUsername.setText("发布者：" + voiceBean.getName());
        tvDate.setText("发布日期：" + MyApplication.format.format(voiceBean.getTime()));

        player = new Player(ApiManager.VOICE_PATH + voiceBean.getPath(), null, new Player.OnPlayListener() {
            @Override
            public void onLoad(int duration) {
            }

            @Override
            public void onProgress(int position) {
            }

            @Override
            public void onCompletion() {
                btnPlay.setImageResource(R.drawable.ic_play_circle_outline_white_24dp);
            }
        });

        loadWebData();
    }

    private void loadWebData() {

        OkHttpTools.sendPost(mContext, ApiManager.COMMENT_LIST)
                .addParams("id", voiceBean.getId() + "")
                .addParams("type", 1 + "")
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

    @OnClick({R.id.btn_back, R.id.btn_play, R.id.btn_reply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_play:
                if (isPlay) {
                    isPlay = false;
                    player.pause();
                    btnPlay.setImageResource(R.drawable.ic_play_circle_outline_white_24dp);
                } else {
                    isPlay = true;
                    player.play(true);
                    btnPlay.setImageResource(R.drawable.ic_pause_circle_outline_white_24dp);
                }
                break;
            case R.id.btn_reply:
                String et_reply = etReply.getText().toString().trim();
                if (TextUtils.isEmpty(et_reply)) {
                    showToast(etReply.getHint().toString());
                    return;
                }
                OkHttpTools.sendPost(mContext, ApiManager.COMMENT_ADD)
                        .addParams("id", voiceBean.getId() + "")
                        .addParams("uid", MyApplication.getInstance().getUserBean().getId() + "")
                        .addParams("type", "1")
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
