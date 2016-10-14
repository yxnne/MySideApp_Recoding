package com.yxnne.mysides.util;

/**
 * 常量设置
 * Created by Administrator on 2016/10/14.
 */

public class Const {

    public static final String ACTION_LOGIN_RESAULT = "com.yxnne.myside.login";
    public static final String LOGIN_STATUS = "login_status";

    //登录结果有三种情况,定义状态码
    /*登录相关的业务常量*/
    //登录成功
    public static final int STATUS_LOGIN_OK = 0x01001;
    //登录失败
    public static final int STATUS_LOGIN_FAILURE = 0x01002;
    //连接失败
    public static final int STATUS_CONNECT_FAILURE = 0x01003;
    //已经登录，或未退出
    public static final int STATUS_ALREADY_LOGGIN = 0x01004;


}
