package com.example.bsproperty.bean;


import java.util.ArrayList;

public class WordWebListBean extends BaseResponse {

    private ArrayList<WordBean> data;

    public ArrayList<WordBean> getData() {
        return data;
    }

    public void setData(ArrayList<WordBean> data) {
        this.data = data;
    }
}
