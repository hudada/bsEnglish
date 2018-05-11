package com.example.bsproperty.bean;

import java.util.ArrayList;

public class VoiceListBean extends BaseResponse {


    private ArrayList<VoiceBean> data;

    public ArrayList<VoiceBean> getData() {
        return data;
    }

    public void setData(ArrayList<VoiceBean> data) {
        this.data = data;
    }
}
