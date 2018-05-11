package com.example.bsproperty.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.MusicObjBean;
import com.example.bsproperty.bean.StudyBean;
import com.example.bsproperty.bean.StudyListBean;
import com.example.bsproperty.bean.VoiceBean;
import com.example.bsproperty.fragment.UserFragment02;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.utils.DenstityUtils;
import com.example.bsproperty.view.FileProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;

public class StudyListActivity extends BaseActivity {

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

    private ArrayList<StudyBean> mData = new ArrayList<>();
    private MyAdapter adapter;
    private StudyBean studyBean;
    private FileProgressDialog dialog;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("资料下载");
        dialog = new FileProgressDialog(mContext);

        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyAdapter(mContext, R.layout.item_study, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("下载")
                        .setMessage("是否下载资源")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                studyBean = mData.get(position);
                                PermissionGen.with((Activity) mContext)
                                        .addRequestCode(522)
                                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        ).request();
                            }
                        }).show();

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
        return R.layout.activity_study_list;
    }

    @Override
    protected void loadData() {
        OkHttpTools.sendPost(mContext, ApiManager.STUDY_LIST)
                .build()
                .execute(new BaseCallBack<StudyListBean>(mContext, StudyListBean.class) {
                    @Override
                    public void onResponse(StudyListBean studyListBean) {
                        mData = studyListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

    private class MyAdapter extends BaseAdapter<StudyBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<StudyBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, StudyBean studyBean, int position) {
            holder.setText(R.id.tv_name, studyBean.getName());
        }
    }


    @PermissionSuccess(requestCode = 522)
    private void ok1() {
        String tip;
        try {
            tip = studyBean.getPath().split("\\.")[1];
        } catch (Exception e) {
            tip = "txt";
        }

        OkHttpUtils.get().url(ApiManager.STUDY_PATH + studyBean.getPath())
                .build()
                .execute(new FileCallBack(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/bsstudy",
                        studyBean.getName() + "." + tip) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {
                        dialog.dismiss();
                        showToast("下载成功");
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        dialog.setPor(progress);
                    }
                });
    }


    @PermissionFail(requestCode = 522)
    private void showTip2() {
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("权限申请");
        builder.setMessage("在设置-应用-权限 中开启相关权限");

        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
