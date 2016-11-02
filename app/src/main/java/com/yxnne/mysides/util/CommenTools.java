package com.yxnne.mysides.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.yxnne.mysides.util.log.LogGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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

    /**
     * 向SD卡写入
     * @param context 上下文
     * @param path 路径
     * @param fileName 文件名
     * @param data 数据
     */
    public static void writeToSdcard(Context context, String path,
                                     String fileName, byte[] data) {
        // 判断sdcard有没有
        String sdcard_state = Environment.getExternalStorageState();
        // 判断SD卡是否存在，并且是否具有读写权限
        if (Environment.MEDIA_MOUNTED.equals(sdcard_state)) {
            FileOutputStream fileOutputStream = null;
            // 有sdcard
            // 判断文件夹有没有
            try {
                File fileDirectory = new File(path);
                if (!fileDirectory.exists()) {
                    // 没有创建
                    fileDirectory.mkdirs();
                }

                File file = new File(fileDirectory, fileName);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
            } catch (Exception e) {
                LogGenerator.getInstance().printError(e);
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                        LogGenerator.getInstance().printMsg("write to sdcard OK!");
                    }
                } catch (Exception e2) {
                    LogGenerator.getInstance().printError(e2);
                }
            }
        }
    }

    /**
     * 读取SD卡
     * @param filePathName 文件名
     * @return byte数组
     */
    public static byte[] readSdcard(String filePathName) {
        FileInputStream fileInputStream = null;
        byte[] data = null;
        try {
            fileInputStream = new FileInputStream(filePathName);
            int size = fileInputStream.available();
            data = new byte[size];
            fileInputStream.read(data);
        } catch (Exception e) {
            LogGenerator.getInstance().printError(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    LogGenerator.getInstance().printError(e);

                }
            }

        }
        return data;
    }
}


