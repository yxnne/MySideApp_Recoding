package com.yxnne.mysides.util;

/**
 * 常量设置
 * Created by Administrator on 2016/10/14.
 */

public class Const {

    public static final String ACTION_LOGIN_RESAULT = "com.yxnne.myside.login";
    public static final String STATUS_KEY = "key_status";

    /*登录相关的业务常量 01*/
    //登录成功
    public static final int STATUS_LOGIN_OK = 0x01001;
    //登录失败
    public static final int STATUS_LOGIN_FAILURE = 0x01002;
    //连接失败
    public static final int STATUS_CONNECT_FAILURE = 0x01003;
    //已经登录，或未退出
    public static final int STATUS_ALREADY_LOGGIN = 0x01004;
    /*网络相关常量02*/
    //网络状态
    public static final int TYPE_NETWORK_NONE = 0x02001;
    public static final int TYPE_NETWORK_WIFI = 0x02002;
    public static final int TYPE_NETWORK_MOBILE = 0x02003;
    /*注册相关 03*/
    public static final int STATUS_REGIST_OK = 0x03001;
    public static final int STATUS_REGIST_FAILED = 0x03002;
    public static final int STATUS_REGIST_CONFLICT = 0x03003;


}
