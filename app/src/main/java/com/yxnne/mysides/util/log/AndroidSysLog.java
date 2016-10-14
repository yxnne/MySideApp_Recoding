package com.yxnne.mysides.util.log;

import android.util.Log;

/**
 * Created by Administrator on 2016/10/14.
 */


public class AndroidSysLog implements IMyLog {

    @Override
    public void printMsg(String msg) {
        Log.i(TAG_MSG,msg);
    }

    @Override
    public void printLog(String tag, String msg) {
        Log.i(tag, msg);
    }

    @Override
    public void printError(String err) {
        Log.e(TAG_ERROR,err);
    }

    @Override
    public void printError(Exception e) {
        Log.e(TAG_ERROR,e.getMessage(),e);
    }
}
