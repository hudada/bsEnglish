package com.example.bsproperty.bean;

import java.util.ArrayList;

public class ComentListBean extends BaseResponse {

    private ArrayList<ComentBean> data;

    public ArrayList<ComentBean> getData() {
        return data;
    }

    public void setData(ArrayList<ComentBean> data) {
        this.data = data;
    }
}
