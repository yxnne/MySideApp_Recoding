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
import com.yxnne.mysides.biz.AddFriendService;
import com.yxnne.mysides.entity.FriendEntity;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;

public class AddFriendActivity extends AppCompatActivity {
    private EditText etUserName,etNickName,etGroup;
    private TextView tvBack,tvSubmit;
    private MyReceiver myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        findViews();
        addListner();
    }

    private void addListner() {
        tvSubmit.setOnClickListener(new MyOnClickListner());
        tvBack.setOnClickListener(new MyOnClickListner());
    }

    @Override
    protected void onResume() {
        super.onResume();
        myReceiver = new MyReceiver();
        this.registerReceiver(myReceiver, new IntentFilter(
                Const.ACTION_SEND_ADD_FRIEND));
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(myReceiver);
    }

    private void findViews() {
        etUserName = (EditText) findViewById(R.id.et_add_friend_username);
        etNickName = (EditText) findViewById(R.id.et_add_friend_nick_name);
        etGroup = (EditText) findViewById(R.id.et_add_friend_group);
        tvBack = (TextView) findViewById(R.id.tv_add_friend_back);
        tvSubmit = (TextView) findViewById(R.id.tv_add_friend_submit);
    }
    private void addANewFriend(){
        try {
            String userName = etUserName.getText().toString();
            String nickName = etNickName.getText().toString();
            String group = etGroup.getText().toString();
            if(TextUtils.isEmpty(userName)){
                Toast.makeText(this,
                        getResources().getString(R.string.err_empty_user2),
                        Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(nickName)){
                Toast.makeText(this,
                        getResources().getString(R.string.err_empty_nickname2),
                        Toast.LENGTH_LONG).show();
                return;
            }
            FriendEntity friendEntity = new FriendEntity(userName,
                    nickName, group);

            Intent intent = new Intent(
                    AddFriendActivity.this, AddFriendService.class);
            //给service 传数据
            intent.putExtra(Const.STATUS_KEY, friendEntity);
            startService(intent);

        } catch (Exception e) {
            LogGenerator.getInstance().printError(e);
        }
    }

    class MyOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int vId = v.getId();
            switch (vId){
                case R.id.tv_add_friend_back:
                    finish();
                    break;
                case R.id.tv_add_friend_submit:
                    addANewFriend();
                    break;
            }
        }
    }
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,
                    getResources().getString(R.string.msg_has_send),
                    Toast.LENGTH_LONG).show();
        }
    }
}
