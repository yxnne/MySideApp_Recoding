package com.yxnne.mysides.biz;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.entity.UpdateEntity;
import com.yxnne.mysides.paser.UpdateJosnParser;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;
import com.yxnne.mysides.view.SettingActivity;

import org.apache.http.Header;

/**
 * Created by Administrator on 2016/10/26.
 */

public class UpdateBiz {


    public void getVersion(final Handler handler)
    {
        try {
            //HttpClient
            //企业用ASyncHttpClient多些，
            //AsyncHttpClient就异步http联网框架
            String url="http://192.168.31.145:8080/MysideServer/servlet/ApkUpdateServlet";
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
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}
