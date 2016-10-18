package com.yxnne.mysides.view;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yxnne.mysides.R;
import com.yxnne.mysides.biz.RegisterUserBiz;
import com.yxnne.mysides.entity.UserEntity;
import com.yxnne.mysides.util.Const;

public class RegisterActivity extends BaseActivity {

    EditText etUsername, etPassword, etConfirmPassword, etNickName;
    TextView tvSubmit,tvBack;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int status = bundle.getInt(Const.STATUS_KEY);
            switch(status){
                case Const.STATUS_REGIST_OK:
                    Toast.makeText(RegisterActivity.this,
                            getResources().getString(R.string.register_user_ok),
                            Toast.LENGTH_LONG).show();
                    RegisterActivity.this.finish();
                    break;
                case Const.STATUS_REGIST_FAILED:
                    Toast.makeText(RegisterActivity.this,
                            getResources().getString(R.string.register_user_failed),
                            Toast.LENGTH_LONG).show();
                    break;
                case Const.STATUS_REGIST_CONFLICT:
                    Toast.makeText(RegisterActivity.this,
                            getResources().getString(R.string.register_user_conflict),
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findVies();
        setListners();
    }

    private void setListners() {
        tvBack.setOnClickListener(new MyOnClickListner());
        tvSubmit.setOnClickListener(new MyOnClickListner());
    }

    private void findVies() {
        etUsername = (EditText) findViewById(R.id.et_register_username);
        etPassword = (EditText) findViewById(R.id.et_register_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_register_confirmPassword);
        etNickName = (EditText) findViewById(R.id.et_register_nickname);
        tvSubmit = (TextView) findViewById(R.id.tv_register_submit);
        tvBack = (TextView) findViewById(R.id.tv_register_toLogin);
    }

    /**
     * 先校验editText的合法性
     * 再调biz
     */
    private void doRegister(){
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String cPassword = etConfirmPassword.getText().toString();
        String nickname = etNickName.getText().toString();
        //先验证空
        boolean hasEmpty = false;
        if(TextUtils.isEmpty(username.trim())){
            hasEmpty = true;
            etUsername.setError(getResources().getString(R.string.err_empty_user));
        }
        if(TextUtils.isEmpty(password)){
            hasEmpty = true;
            etPassword.setError(getResources().getString(R.string.err_empty_pwd));
        }
        if(TextUtils.isEmpty(cPassword)){
            hasEmpty = true;
            etConfirmPassword.setError(getResources().getString(R.string.err_emptycomfirm_pwd));
        }
        if(TextUtils.isEmpty(nickname.trim())){
            hasEmpty = true;
            etNickName.setError(getResources().getString(R.string.err_empty_nickname));
        }
        if(hasEmpty){
            return;
        }else if(!cPassword.equals(password)){//验证确认密码,不相等
            etConfirmPassword.setError(getResources().getString(R.string.err_emptycomfirm_pwd_error));
            return;
        }
        //调业务
        UserEntity userEntity = new UserEntity(username,password,nickname);
        RegisterUserBiz registerBiz = new RegisterUserBiz();
        registerBiz.register(handler,userEntity);

    }

    private class MyOnClickListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            switch (viewId){
                case R.id.tv_register_submit:
                    doRegister();
                    break;
                case R.id.tv_register_toLogin:
                    finish();
                    break;
            }

        }
    }
}
