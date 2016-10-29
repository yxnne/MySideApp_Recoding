package com.yxnne.mysides.biz;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.entity.UpdateEntity;
import com.yxnne.mysides.paser.UpdateJosnParser;
import com.yxnne.mysides.util.CommenTools;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;
import com.yxnne.mysides.view.SettingActivity;

import org.apache.http.Header;

/**
 * biz to updateapk
 * Created by Administrator on 2016/10/26.
 */

public class UpdateBiz {
    /**
     * 下载.apk文件
     * @param handler 向Activity传递消息
     * @param apkUrl 地址
     */
    public void getApk(final Handler handler, final String apkUrl) {
        YApplication.asyncHttpClient.get(apkUrl, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String fileName = null;
                        try {
                            // 保存到sdcard
                            // apkUrl="http://192.168.31.145:8080/MysideServer/yxnne.apk"
                            int index = apkUrl.lastIndexOf("/");
                            fileName = apkUrl.substring(index + 1);
                            CommenTools.writeToSdcard(YApplication.instance,
                                    Const.DWONLOAD_PATH, fileName, responseBody);

                        } catch (Exception e) {
                            LogGenerator.getInstance().printError(e);
                        } finally {
                            Message message = handler.obtainMessage();
                            message.what = SettingActivity.MSG_INSTALL_APK;
                            // 把apk在sdcard上路径告诉activity
                            Bundle bundle = new Bundle();
                            String path = Const.DWONLOAD_PATH + "/" + fileName;
                            bundle.putString(Const.STATUS_KEY, path);
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        LogGenerator.getInstance().printMsg("onFailure");
                        Message message = handler.obtainMessage();
                        message.what = SettingActivity.MSG_DOWNLOAD_APK_ERROR;
                        //Bundle bundle= new Bundle();
                        // bundle.putSerializable(Const.STATUS_KEY, updateEntity);
                        //message.setData(bundle);
                        handler.sendMessage(message);
                    }
                });
    }

    /**
     * 得到版本信息
     * @param handler 向Activty传递数据
     */
    public void getVersion(final Handler handler)
    {
        try {
            //HttpClient
            //企业用ASyncHttpClient多些，
            //AsyncHttpClient就异步http联网框架
            //server address http://115.159.189.43/
            String url=YApplication.tomcatBaseAdress+"servlet/ApkUpdateServlet";
            LogGenerator.getInstance().printMsg(url);
            YApplication.asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                   // super.onSuccess(statusCode, headers, responseBody);
                    LogGenerator.getInstance().printMsg("onSuccess");
                    String jsonString = new String(responseBody);
                    // LogGenerator.getInstance().printprintLog("UpdateBiz", jsonString);
                    //这里因为server用的dos.writeUTF(jsonString);，所以从第三个开始取子串
                    jsonString=jsonString.substring(2);
                    LogGenerator.getInstance().printprintLog("UpdateBiz", jsonString);
                    UpdateEntity updateEntity = null;
                    try {
                        UpdateJosnParser updateParser = new UpdateJosnParser();
                        updateEntity = updateParser.parser(jsonString);
                        LogGenerator.getInstance().printprintLog("updateEntity", updateEntity.toString());
                    } catch (Exception e) {
                        LogGenerator.getInstance().printError(e);
                    }finally
                    {//还是利用handler去传递消息
                        Message message = handler.obtainMessage();
                        message.what = SettingActivity.MSG_SHOW_VERSION;
                        Bundle bundle= new Bundle();
                        bundle.putSerializable(Const.STATUS_KEY, updateEntity);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                   // super.onFailure(statusCode, headers, responseBody, error);
                    LogGenerator.getInstance().printMsg("onFailure");
                    Message message = handler.obtainMessage();
                    message.what = SettingActivity.MSG_SHOW_VERSION_ERROR;
                    //Bundle bundle= new Bundle();
                    // bundle.putSerializable(Const.STATUS_KEY, updateEntity);
                    //message.setData(bundle);
                    handler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            LogGenerator.getInstance().printError(e);
        }

    }

}
