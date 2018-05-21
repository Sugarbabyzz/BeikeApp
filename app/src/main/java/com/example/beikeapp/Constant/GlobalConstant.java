package com.example.beikeapp.Constant;

/**
 * Created by m1821 on 2018/4/10.
 */

public class GlobalConstant {
    // http请求返回值, 与servlet中对应
    public static final String FLAG_FAILURE = "300";
    public static final String FLAG_SUCCESS = "200";
    public static final String FLAG_YES = "100";
    public static final String FLAG_NO = "400";

    public static final String ID_TEACHER = "teacher";
    public static final String ID_STUDENT = "student";
    public static final String ID_PARENT = "parent";

    public static final String URL_PROFILE_BASIC = "http://47.94.253.65:8080/ProfileServlet/";

    public static final String URL_GET_GENERAL_INFO = URL_PROFILE_BASIC + "GetGeneralInfoServlet";

    public static final String URL_CHANGE_PROFILE_INFO = URL_PROFILE_BASIC + "ChangeInfoServlet";

    public static final String URL_CHANGE_PROFILE_PHOTO = URL_PROFILE_BASIC + "UploadImageServlet";

    public static final String URL_GET_PROFILE_PHOTO = URL_PROFILE_BASIC + "GetImageServlet";

}
