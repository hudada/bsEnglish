package com.example.bsproperty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.SongBean;
import com.example.bsproperty.bean.WordListBean;
import com.example.bsproperty.ui.QinfoActivity;
import com.example.bsproperty.ui.ReadActivity;
import com.example.bsproperty.ui.UserMainActivity;
import com.example.bsproperty.ui.WordInfoActivity;
import com.example.bsproperty.utils.Player;
import com.example.bsproperty.utils.SpUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wdxc1 on 2018/3/21.
 */

public class UserFragment01 extends BaseFragment {


    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_lishi)
    TextView tvLishi;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.et_q)
    EditText etQ;
    @BindView(R.id.btn_q)
    Button btnQ;
    @BindView(R.id.rb_yh)
    RadioButton rbYh;
    @BindView(R.id.rb_hy)
    RadioButton rbHy;
    @BindView(R.id.rv_listls)
    RecyclerView rvListls;
    @BindView(R.id.rg_list)
    RadioGroup rgList;
    private ArrayList<WordListBean.Word> mData;
    private MyAdapter adapter;
    private int currPosition = -1;
    private int mType;

    @Override
    public void onResume() {
        super.onResume();
        loadWebData();

        if (SpUtils.getWordList(mContext) != null) {
            mData = SpUtils.getWordList(mContext).getData();
        }

        if (mData != null && mData.size() > 0) {
            adapter = new MyAdapter(mContext, R.layout.item_lishiword, mData);
            adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, Object item, int position) {
                    Intent intent = new Intent(getContext(), QinfoActivity.class);
                    intent.putExtra("key", mData.get(position).getWord());
                    intent.putExtra("type", mData.get(position).getType());
                    startActivity(intent);
                }
            });
            rvListls.setAdapter(adapter);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mContext != null) {
            loadWebData();
        }
    }

    private void loadWebData() {
//        mData.clear();
//        long id;
//        if (MyApplication.getInstance().getUserBean() == null) {
//            id = -1;
//        } else {
//            id = MyApplication.getInstance().getUserBean().getId();
//        }
//        OkHttpTools.sendGet(mContext, ApiManager.SONG_LIST)
//                .addParams("uid", id + "")
//                .build()
//                .execute(new BaseCallBack<SongListBean>(mContext, SongListBean.class) {
//                    @Override
//                    public void onResponse(SongListBean songListBean) {
//                        mData = songListBean.getData();
//                        adapter.notifyDataSetChanged(mData);
//                    }
//                });
    }

    @Override
    protected void loadData() {

    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("单词查询");
        btnRight.setText("读一读");
        btnRight.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.GONE);
        rvListls.setLayoutManager(new GridLayoutManager(mContext, 3));
        rgList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int tag = Integer.parseInt(mRootView.findViewById(checkedId).getTag().toString());
                mType = tag;
            }
        });
        ((RadioButton) rgList.getChildAt(0)).setChecked(true);
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_user01;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btn_right, R.id.btn_q, R.id.rb_yh, R.id.rb_hy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                startActivity(new Intent(getContext(), ReadActivity.class));
                break;
            case R.id.btn_q:
                String et_q = etQ.getText().toString().trim();
                if (TextUtils.isEmpty(et_q)) {
                    showToast("请输入要查询的内容");
                    return;
                }
                WordListBean wordListBean;
                if (SpUtils.getWordList(mContext) == null) {
                    wordListBean = new WordListBean();
                } else {
                    wordListBean = SpUtils.getWordList(mContext);
                }
                ArrayList<WordListBean.Word> list = wordListBean.getData();
                if (list == null) {
                    list = new ArrayList<>();
                }
                WordListBean.Word word = new WordListBean.Word();
                word.setWord(et_q);
                word.setType(mType);
                list.add(word);
                wordListBean.setData(list);
                SpUtils.setWordList(mContext, wordListBean);
                Intent intent = new Intent(getContext(), QinfoActivity.class);
                intent.putExtra("key", et_q);
                intent.putExtra("type", mType);
                startActivity(intent);
                break;
        }
    }

    private class MyAdapter extends BaseAdapter<WordListBean.Word> {

        public MyAdapter(Context context, int layoutId, ArrayList<WordListBean.Word> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, WordListBean.Word word, int position) {
            holder.setText(R.id.tv_word, "# " + word.getWord()+" ");
        }
    }


}
