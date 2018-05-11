package com.example.bsproperty.bean;

import java.util.ArrayList;
import java.util.List;

public class BookListBean extends BaseResponse {
    private ArrayList<BookBean> data;

    public ArrayList<BookBean> getData() {
        return data;
    }

    public void setData(ArrayList<BookBean> data) {
        this.data = data;
    }
}
