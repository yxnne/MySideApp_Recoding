package com.yxnne.mysides.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 通用工具类
 * Created by Administrator on 2016/10/13.
 */

public class CommenTools{
    private static ProgressDialog progressDialog;

    /**
     * 取消对话框
     */
    public static void cancelProgressDialog(){
        progressDialog.cancel();
        progressDialog = null;
    }
    /**
     * 显示一个进度对话框
     * @param context 上下文
     * @param msg 显示消息
     */
    public static void showProgressDialog(Context context,String msg){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(msg);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }
    }



    /**
     * get App versionCode 升级用
     * @param context 上下文
     * @return 版本号
     */
    public static String getVersionCode(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            versionCode = packageInfo.versionCode+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * get App versionName 给用户看
     * @param context 上下文
     * @return 版本号
     */
    public static String getVersionName(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}


