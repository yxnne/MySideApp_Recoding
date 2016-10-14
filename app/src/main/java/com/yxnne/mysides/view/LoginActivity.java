package com.yxnne.mysides.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yxnne.mysides.R;

import com.yxnne.mysides.biz.LoginBiz;
import com.yxnne.mysides.entity.UserEntity;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.NetWorkUtil;

public class LoginActivity extends AppCompatActivity {
    private TextView tvSubmit;
    private EditText etUserName,etPwd;
    LoginReciever mLoginReciever;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //testLoginBiz();
        findViews();
        NetWorkUtil.checkAndOpenNetworkSetting(this);
        setListners();
        mLoginReciever = new LoginReciever();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Const.ACTION_LOGIN_RESAULT);
        registerReceiver(mLoginReciever,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mLoginReciever);
    }

    private void setListners() {
        tvSubmit.setOnClickListener(new MyOnClickListner());
    }

    private void findViews() {
        tvSubmit = (TextView) findViewById(R.id.tv_login_submit);
        etUserName = (EditText) findViewById(R.id.et_login_username);
        etPwd = (EditText) findViewById(R.id.et_login_password);
    }

    /**
     * 监听器
     * 先校验editText的值，再调用biz
     */
    private class MyOnClickListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                String username = etUserName.getText().toString();
                String password = etPwd.getText().toString();

                StringBuilder builder = new StringBuilder();
                if (TextUtils.isEmpty(username)) {
                    builder.append(
                            getResources().getString(R.string.err_empty_user)+"\n");
                }
                if (TextUtils.isEmpty(password)) {
                    builder.append(
                            getResources().getString(R.string.err_empty_pwd));
                }
                if (!TextUtils.isEmpty(builder.toString())) {
                    // 前面的数据检查没有通过
                    Toast.makeText(LoginActivity.this, builder.toString(), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                UserEntity userEntity = new UserEntity(username, password);
                LoginBiz loginBiz = new LoginBiz();
                loginBiz.login(userEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    private void testLoginBiz() {
//        LoginBiz lb = new LoginBiz();
//        lb.login(new UserEntity("q","q"));
//    }

    private class LoginReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int loginStatus = intent.getIntExtra(Const.LOGIN_STATUS,0);
            String resault = "";
            switch (loginStatus){
                //链接server失败
                case Const.STATUS_CONNECT_FAILURE:
                    resault = getResources().
                            getString(R.string.openfire_connection_failed);
                    break;
                //登录失败
                case Const.STATUS_LOGIN_FAILURE:
                    resault = getResources().
                            getString(R.string.login_failed);
                    break;
                //登录成功
                case Const.STATUS_LOGIN_OK:
                    resault = getResources().
                            getString(R.string.login_ok);
                    break;
                case Const.STATUS_ALREADY_LOGGIN:
                    resault = getResources().
                            getString(R.string.already_loggin);
                    break;
            }
            Toast.makeText(LoginActivity.this,resault,Toast.LENGTH_LONG).show();
        }
    }
}
