package com.example.bsproperty.bean;

import java.util.ArrayList;

public class StudyListBean extends BaseResponse {

    private ArrayList<StudyBean> data;

    public ArrayList<StudyBean> getData() {
        return data;
    }

    public void setData(ArrayList<StudyBean> data) {
        this.data = data;
    }
}
