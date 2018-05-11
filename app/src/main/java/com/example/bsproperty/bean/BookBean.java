package com.example.bsproperty.bean;

import java.io.Serializable;
import java.util.List;

public class BookBean implements Serializable {

    private Long id;
    private String title;
    private int likeSum;
    private List<BookInfoBean> bookInfoBeans;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLikeSum() {
        return likeSum;
    }

    public void setLikeSum(int likeSum) {
        this.likeSum = likeSum;
    }

    public List<BookInfoBean> getBookInfoBeans() {
        return bookInfoBeans;
    }

    public void setBookInfoBeans(List<BookInfoBean> bookInfoBeans) {
        this.bookInfoBeans = bookInfoBeans;
    }

}
