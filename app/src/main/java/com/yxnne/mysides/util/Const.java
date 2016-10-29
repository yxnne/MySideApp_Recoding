package com.yxnne.mysides.util;

import android.os.Environment;

/**
 * 常量设置
 * Created by Administrator on 2016/10/14.
 */

public class Const {
    /*Actions*/
    public static final String ACTION_LOGIN_RESAULT = "com.yxnne.myside.LOGIN";
    public static final String ACTION_SEND_ADD_FRIEND = "com.yxnne.myside.ADD_FRIEND";
    public static final String ACTION_UPDATE_MY_MSG ="com.yxnne.myside.UPDATE_MY_MSG";
    public static final String ACTION_SEND_PRIVATE_CHAT_MSG = "com.yxnne.myside.SEND_PRIVATE_CHAT_MSG";

    public static final String STATUS_KEY = "key_status";

    /*int --- 登录相关的业务常量 01*/
    //登录成功
    public static final int STATUS_LOGIN_OK = 0x01001;
    //登录失败
    public static final int STATUS_LOGIN_FAILURE = 0x01002;
    //连接失败
    public static final int STATUS_CONNECT_FAILURE = 0x01003;
    //已经登录，或未退出
    public static final int STATUS_ALREADY_LOGGIN = 0x01004;
    /*int --- 网络相关常量02*/
    //网络状态
    public static final int TYPE_NETWORK_NONE = 0x02001;
    public static final int TYPE_NETWORK_WIFI = 0x02002;
    public static final int TYPE_NETWORK_MOBILE = 0x02003;
    /*int --- 注册相关 03*/
    public static final int STATUS_REGIST_OK = 0x03001;
    public static final int STATUS_REGIST_FAILED = 0x03002;
    public static final int STATUS_REGIST_CONFLICT = 0x03003;
    /*String --- 目录相关常量*/
    //sdcard根目录
    public static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    //APP根目录
    public static final String APP_DATA_ROOT = SDCARD_ROOT+"/mySide";//mnt/sdcard/mySide
    //APP DWONLOAD_PATH
    public static final String DWONLOAD_PATH = APP_DATA_ROOT+"/download";
    //IMAGE_PATH
    public static final String IMAGE_PATH = APP_DATA_ROOT+"/image";
    //AUDIO_PATH
    public static final String AUDIO_PATH = APP_DATA_ROOT+"/audio";


}
