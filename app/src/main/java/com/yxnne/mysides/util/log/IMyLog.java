package com.yxnne.mysides.util.log;

/**
 * Created by Administrator on 2016/10/13.
 */

public interface IMyLog {
    String TAG_MSG = "yxnne_app_msg-->";
    String TAG_ERROR = "yxnne_app_error-->";

    void printMsg(String msg);
    void printLog(String tag,String msg);
    void printError(String err);
    void printError(Exception e);

}
