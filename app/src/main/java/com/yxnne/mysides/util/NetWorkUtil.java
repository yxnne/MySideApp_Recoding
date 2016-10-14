package com.yxnne.mysides.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yxnne.mysides.R;

/**
 * 一般网络工具
 * Created by Administrator on 2016/10/14.
 */

public class NetWorkUtil {
    public static void checkAndOpenNetworkSetting(final Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null){//表示没网
            //显示Dialog
            final AlertDialog.Builder dialogBuilder =
                    new AlertDialog.Builder(context);
            dialogBuilder.setMessage(context.getResources().getString(R.string.no_network));
                    //.setPositiveButton("").setNegativeButton("");
            dialogBuilder.setPositiveButton(context.getResources().getString(R.string.open),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //打开系统设置
                            Intent intent = new Intent(
                                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            context.startActivity(intent);
                        }
                    });
            dialogBuilder.setNegativeButton(context.getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            dialogBuilder.show();
        }
    }
}
