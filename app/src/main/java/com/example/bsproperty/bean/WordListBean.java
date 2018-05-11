package com.example.bsproperty.bean;

import java.util.ArrayList;

/**
 * Created by wdxc1 on 2018/5/11.
 */

public class WordListBean {
    private ArrayList<Word> data;

    public ArrayList<Word> getData() {
        return data;
    }

    public void setData(ArrayList<Word> data) {
        this.data = data;
    }

    public static class Word{
        private String word;
        private int type;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
