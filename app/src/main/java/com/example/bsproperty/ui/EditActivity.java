package com.example.bsproperty.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.UserObjBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditActivity extends BaseActivity

{
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_nic)
    EditText etNic;
    @BindView(R.id.et_age)
    EditText etAge;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("资料修改");
        btnRight.setText("确认");
        btnRight.setVisibility(View.VISIBLE);
        tvName.setText(MyApplication.getInstance().getUserBean().getUserName());
        etNic.setText(MyApplication.getInstance().getUserBean().getName());
        etAge.setText(MyApplication.getInstance().getUserBean().getAge()+"");
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_edit;
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_back, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_right:
                String nic = etNic.getText().toString().trim();
                String age = etAge.getText().toString().trim();
                OkHttpTools.sendPost(mContext, ApiManager.USER_CHANGE)
                        .addParams("id", MyApplication.getInstance().getUserBean().getId() + "")
                        .addParams("nic", nic)
                        .addParams("age", age + "")
                        .build()
                        .execute(new BaseCallBack<UserObjBean>(mContext, UserObjBean.class) {
                            @Override
                            public void onResponse(UserObjBean userObjBean) {
                                showToast("修改成功");
                                MyApplication.getInstance().setUserBean(userObjBean.getData());
                                SpUtils.setUserBean(mContext, userObjBean.getData());
                                finish();
                            }
                        });
                break;
        }
    }
}
