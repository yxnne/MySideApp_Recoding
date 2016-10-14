package com.yxnne.mysides.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;

/**
 * 专门接受网络设置后的系统广播
 * Created by Administrator on 2016/10/15.
 */

public class NetworkStateChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            // 判断有没有网
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                YApplication.network_type = Const.TYPE_NETWORK_NONE;
                LogGenerator.getInstance().printMsg("network closed");

            } else {
                int networkType = activeNetworkInfo.getType();
                if (networkType == ConnectivityManager.TYPE_WIFI) {
                    YApplication.network_type = Const.TYPE_NETWORK_WIFI;
                    LogGenerator.getInstance().printMsg("user open wifi");
                } else if (networkType == ConnectivityManager.TYPE_MOBILE) {
                    if (activeNetworkInfo.isRoaming()) {
                        LogGenerator.getInstance().printMsg("user open mobil");
                    } else {
                        LogGenerator.getInstance().printMsg("user open Roaming mobil");
                    }
                    YApplication.network_type = Const.TYPE_NETWORK_MOBILE;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
