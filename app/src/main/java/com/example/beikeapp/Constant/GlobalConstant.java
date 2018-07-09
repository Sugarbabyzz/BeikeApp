package com.example.beikeapp.Constant;

/**
 * Created by m1821 on 2018/4/10.
 */

public class GlobalConstant {
    // 约定的http请求返回值, 与servlet中相同
    public static final String FLAG_FAILURE = "300";
    public static final String FLAG_SUCCESS = "200";
    public static final String FLAG_YES = "100";
    public static final String FLAG_NO = "400";

    public static final String ID_TEACHER = "teacher";
    public static final String ID_STUDENT = "student";
    public static final String ID_PARENT = "parent";

    /**
     * 根路径
     * 所有servlet均在BeikeServer下
     */
    public static final String URL_BASIC = "http://47.94.253.65:8080/BeikeServer";



    /**
     * 注册部分
     * 三个身份统一
     */
    public static final String URL_REGISTER_BASIC = URL_BASIC + "/Register";

    // 查重
    public static final String URL_TEST_EXISTENCE = URL_REGISTER_BASIC + "/TestExistence";

    // 验证学生邀请码
    public static final String URL_CHECK_CODE_STUDENT = URL_REGISTER_BASIC + "/Student/CheckCode";

    // 验证老师邀请码
    public static final String URL_CHECK_CODE_PARENT = URL_REGISTER_BASIC +"/Parent/CheckCode";


    /**
     * 登陆部分
     * 三个身份统一
     */
    public static final String URL_LOGIN = URL_BASIC + "/Login";


    /**
     * Profile 部分
     */
    private static final String URL_PROFILE_BASIC = URL_BASIC + "/Profile";

    // 获取个人信息(除头像外)
    public static final String URL_GET_INFO = URL_PROFILE_BASIC + "/GetInfo";

    // 获取个人头像
    public static final String URL_GET_PHOTO = URL_PROFILE_BASIC + "/GetPhoto";

    // 更改个人信息(除头像外)
    public static final String URL_CHANGE_INFO = URL_PROFILE_BASIC + "/ChangeInfo";

    // 更改个人头像
    public static final String URL_CHANGE_PHOTO = URL_PROFILE_BASIC + "/UploadPhoto";

}
