package com.yxnne.mysides.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yxnne.mysides.R;
import com.yxnne.mysides.biz.PrivateChatBizAsyncTask;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;

public class PrivateChatActivity extends AppCompatActivity {
    String friendUser;
    TextView tvFriendName;
    EditText etMsgBody;
    Button btnSend;
    BroadcastReceiver showPrivateMsgReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_private_chat);
            getData();
            findViews();
            addListener();
            showPrivateMsgReceiver = new ShowPrivateMsgReceiver();
            this.registerReceiver(showPrivateMsgReceiver,
                    new IntentFilter(Const.ACTION_SEND_PRIVATE_CHAT_MSG));
        } catch (Exception e) {
            LogGenerator.getInstance().printError(e);
        }
    }
    private void getData() {
        friendUser = this.getIntent().getStringExtra(Const.STATUS_KEY);
    }
    private void findViews() {

        tvFriendName = (TextView) findViewById(R.id.tv_private_chat_friendName);
        etMsgBody = (EditText) findViewById(R.id.et_private_body);
        btnSend = (Button) findViewById(R.id.btn_private_chat_send);
        if (friendUser.contains("@")) {
            String friendUsername = friendUser.substring(0,
                    friendUser.indexOf("@"));
            tvFriendName.setText(friendUsername);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        this.unregisterReceiver(showPrivateMsgReceiver);
    }



    private void addListener() {
        //btn Send:
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String body = etMsgBody.getText().toString();
                    etMsgBody.getText().clear();

                    PrivateChatBizAsyncTask privateChatTask = new PrivateChatBizAsyncTask();
                    privateChatTask.execute(friendUser, body);
                } catch (Exception e) {
                    LogGenerator.getInstance().printError(e);
                }

            }
        });
    }


    /**
     * taskBiz 发送消息成功以后 发送广播过来 这里收 用于更新界面
     */
    class ShowPrivateMsgReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LogGenerator.getInstance().printprintLog("ShowPrivateMsgReceiver", "show a msg");
            } catch (Exception e) {
                LogGenerator.getInstance().printError(e);
            }
        }
    }
}
