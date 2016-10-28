package com.yxnne.mysides.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.yxnne.mysides.R;
import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.adapter.MyMsgAdapter;
import com.yxnne.mysides.util.Const;

public class MyMsgActivity extends BaseActivity {
    private ImageView ivShowMenu;
    private MyLeftSildingMenu leftMenu ;
    private ListView lvMsg;
    private MyMsgAdapter myMsgAdapter;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_msg);
        findViews();
        addShowMenuBtn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myReceiver = new MyReceiver();
        this.registerReceiver(myReceiver, new IntentFilter(
                Const.ACTION_UPDATE_MY_MSG));
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(myReceiver);
    }

    private void findViews() {
        leftMenu  = new MyLeftSildingMenu(this,R.layout.slidingmenu_left_layout);
        lvMsg = (ListView) findViewById(R.id.lv_my_msg);
        myMsgAdapter = new MyMsgAdapter(this, YApplication.listMsg);
        lvMsg.setAdapter(myMsgAdapter);
    }

    private void addShowMenuBtn() {
        ivShowMenu = (ImageView) findViewById(R.id.iv_mymsg_showMenu);
        ivShowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftMenu.showMenu();
            }
        });
    }

    /**
     * BroadcastReceiver to update listView
     */
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            myMsgAdapter.updateData(YApplication.listMsg);
        }
    }
}
