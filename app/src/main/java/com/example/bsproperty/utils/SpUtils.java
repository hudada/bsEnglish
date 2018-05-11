package com.example.bsproperty.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.bsproperty.bean.UserBean;
import com.example.bsproperty.bean.WordListBean;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by yezi on 2018/1/29.
 */

public class SpUtils {

    private static final String SP_NAME = "sp_name";
    private static final String ABOUT_USER = "about_user";
    private static final String ABOUT_WORD = "about_word";

    private static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static void setUserBean(Context context, UserBean userBean) {
        if (userBean != null) {
            getSp(context).edit().putString(ABOUT_USER, new Gson().toJson(userBean)).apply();
        }
    }

    public static UserBean getUserBean(Context context) {
        String user = getSp(context).getString(ABOUT_USER, "");
        if (!TextUtils.isEmpty(user)) {
            return new Gson().fromJson(user, UserBean.class);
        } else {
            return null;
        }
    }

    public static void setWordList(Context context, WordListBean list) {
        if (list != null) {
            getSp(context).edit().putString(ABOUT_WORD, new Gson().toJson(list)).apply();
        }
    }

    public static WordListBean getWordList(Context context) {
        String user = getSp(context).getString(ABOUT_WORD, "");
        if (!TextUtils.isEmpty(user)) {
            return new Gson().fromJson(user, WordListBean.class);
        } else {
            return null;
        }
    }

    public static boolean cleanUserBean(Context context) {
        return getSp(context).edit().putString(ABOUT_USER, "").commit();
    }
}
