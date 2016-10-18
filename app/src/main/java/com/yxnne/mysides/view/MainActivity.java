package com.yxnne.mysides.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yxnne.mysides.R;
import com.yxnne.mysides.util.CommenTools;

public class MainActivity extends BaseActivity {
    TextView tvVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //得到版本名，在gradle里面配置
        tvVersionName = (TextView) findViewById(R.id.tv_activity_main_versionName);
        String strVersionName = "v"+CommenTools.getVersionName(this);
        tvVersionName.setText(strVersionName);
        //3秒后跳转
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });
            }
        }).start();


    }
}