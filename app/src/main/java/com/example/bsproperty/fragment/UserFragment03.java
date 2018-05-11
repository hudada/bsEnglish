package com.example.bsproperty.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.ui.DownLoadActivity;
import com.example.bsproperty.ui.EditActivity;
import com.example.bsproperty.ui.MyLishiActivity;
import com.example.bsproperty.ui.MyPlayActivity;
import com.example.bsproperty.ui.MyReplyActivity;
import com.example.bsproperty.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wdxc1 on 2018/3/21.
 */

public class UserFragment03 extends BaseFragment {


    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_nic)
    TextView tvNic;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.rl_my_pl)
    RelativeLayout rlMyPl;
    @BindView(R.id.rl_my_xz)
    RelativeLayout rlMyXz;
    @BindView(R.id.rl_my_ly)
    RelativeLayout rlMyLy;
    @BindView(R.id.rl_my_ls)
    RelativeLayout rlMyLs;
    @BindView(R.id.btn_out)
    Button btnOut;

    @Override
    public void onResume() {
        super.onResume();
        tvName.setText(MyApplication.getInstance().getUserBean().getUserName());
        tvNic.setText(MyApplication.getInstance().getUserBean().getName());
        tvAge.setText(MyApplication.getInstance().getUserBean().getAge() + "");
    }

    @Override
    protected void loadData() {
        tvName.setText(MyApplication.getInstance().getUserBean().getUserName());
        tvNic.setText(MyApplication.getInstance().getUserBean().getName());
        tvAge.setText(MyApplication.getInstance().getUserBean().getAge() + "");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("个人中心");
        btnRight.setText("修改");
        btnRight.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.GONE);
    }


    @Override
    public int getRootViewId() {
        return R.layout.fragment_user03;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btn_right, R.id.rl_my_pl, R.id.rl_my_xz, R.id.rl_my_ly, R.id.rl_my_ls, R.id.btn_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                startActivity(new Intent(getContext(), EditActivity.class));
                break;
            case R.id.rl_my_pl:
                startActivity(new Intent(getContext(), MyReplyActivity.class));
                break;
            case R.id.rl_my_xz:
                startActivity(new Intent(getContext(), DownLoadActivity.class));
                break;
            case R.id.rl_my_ly:
                startActivity(new Intent(getContext(), MyPlayActivity.class));
                break;
            case R.id.rl_my_ls:
                startActivity(new Intent(getContext(), MyLishiActivity.class));
                break;
            case R.id.btn_out:
                if (SpUtils.cleanUserBean(mContext)) {
                    System.exit(0);
                }
                break;
        }
    }
}
