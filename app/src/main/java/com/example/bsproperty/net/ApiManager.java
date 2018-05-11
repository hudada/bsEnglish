package com.example.bsproperty.net;

/**
 * Created by yezi on 2018/1/27.
 */

public class ApiManager {

    private static final String HTTP = "http://";
    private static final String IP = "192.168.55.103";
    private static final String PROT = ":8080";
    private static final String HOST = HTTP + IP + PROT;
    private static final String API = "/api";
    private static final String USER = "/user";
    private static final String SEARCH = "/search";
    private static final String BOOK = "/book";
    private static final String LIKE = "/like";
    private static final String COMMENT = "/comment";
    private static final String VOICE = "/voice";
    private static final String STUDY = "/study";

    public static final String VOICE_PATH = HOST + API + VOICE;
    public static final String STUDY_PATH = HOST + API + STUDY;

    public static final String REGISTER = HOST + API + USER + "/register";
    public static final String LOGIN = HOST + API + USER + "/login";
    public static final String USER_CHANGE = HOST + API + USER + "/change";

    public static final String SEARCH_LIST = HOST + API + SEARCH + "/list";

    public static final String BOOK_LIST = HOST + API + BOOK + "/list";

    public static final String CHECK = HOST + API + LIKE + "/check";
    public static final String ADD_OR_SUB = HOST + API + LIKE + "/add";
    public static final String LIKE_LIST = HOST + API + LIKE + "/list";

    public static final String COMMENT_LIST = HOST + API + COMMENT + "/list";
    public static final String COMMENT_ADD = HOST + API + COMMENT + "/add";
    public static final String COMMENT_MY_LIST = HOST + API + COMMENT + "/mylist";

    public static final String VOICE_LIST = HOST + API + VOICE + "/list";
    public static final String VOICE_MY_LIST = HOST + API + VOICE + "/mylist";
    public static final String VOICE_ADD = HOST + API + VOICE + "/add";

    public static final String STUDY_LIST = HOST + API + STUDY + "/list";
}
