package com.yxnne.mysides.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.yxnne.mysides.R;
import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.biz.PrivateChatBizAsyncTask;
import com.yxnne.mysides.entity.PrivateChatEntity;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;

import org.jivesoftware.smack.packet.Message;

import java.util.Vector;

public class PrivateChatActivity extends AppCompatActivity {
    private String friendUser;
    private TextView tvFriendName,tvBack;
    private EditText etMsgBody;
    private Button btnSend;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private BroadcastReceiver showPrivateMsgReceiver;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
        tvBack = (TextView) findViewById(R.id.tv_private_chat_back);
        etMsgBody = (EditText) findViewById(R.id.et_private_body);
        btnSend = (Button) findViewById(R.id.btn_private_chat_send);
        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutChatContent);

        if (friendUser.contains("@")) {
            String friendUsername = friendUser.substring(0,
                    friendUser.indexOf("@"));
            tvFriendName.setText(friendUsername);
        }
    }

    @Override
    protected void onDestroy() {

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
        //tv back to back
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.finish
                finish();
                //2.保存对话 //TODO
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
                Vector<Message> vector = PrivateChatEntity.map.get(friendUser);
                for (Message message : vector) {
                    String from = message.getFrom();
                    String body = message.getBody();
                    //LogGenerator.getInstance().printprintLog("ShowPrivateMsgReceiver", from + ":" + body);
                    View view = null;
                    // 判断是我说的还是好友说的
                    if (YApplication.currentUser.equals(from)) {//我说的
                        view = View.inflate(context, R.layout.right_chat_item, null);
                    } else {
                        // 好友说的用left.xml来显示
                        view = View.inflate(context, R.layout.left_chat_item, null);
                    }

                    // 把right.xml或left.xml放到linearLayout中
                    linearLayout.addView(view);
                    // 给textview设置显示的聊天内容
                    TextView tvText = (TextView) view
                            .findViewById(R.id.tv_text);
                    tvText.setText(body);

                    handler.post(new Runnable() {//这个执行的时间靠后些
                        @Override
                        public void run() {
                            moveUp();
                        }
                    });
                    // 从vector移出message
                   // moveUp();
                    vector.remove(message);
                }
            } catch (Exception e) {
                LogGenerator.getInstance().printError(e);
            }
        }

        /**
         * 将scrollView向上移动
         */
        private void moveUp() {
            // 向上移
            int scrollViewHeight = scrollView.getHeight();
            int linearLayoutHeight = linearLayout.getHeight();
            LogGenerator.getInstance()
                    .printprintLog("向上移", "scrollViewHeight=" + scrollViewHeight
                    + ",linearLayoutHeight=" + linearLayoutHeight);
            if (linearLayoutHeight > scrollViewHeight) {
                int y = linearLayoutHeight - scrollViewHeight;
                scrollView.scrollTo(0, y);
            }
        }
    }
}
