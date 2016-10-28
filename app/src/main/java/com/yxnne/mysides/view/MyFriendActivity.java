package com.yxnne.mysides.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yxnne.mysides.R;

public class MyFriendActivity extends BaseActivity {
    private MyLeftSildingMenu leftMenu ;
    private ImageView ivShowMenu;
    private TextView tvAdd;
    private ListView lvMyFrinends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
        findView();
        addShowMenuBtn();
        addListner();
    }

    private void findView() {
        lvMyFrinends = (ListView) findViewById(R.id.lv_myfriend);
        tvAdd = (TextView) findViewById(R.id.tv_myfriendc_add);
        leftMenu  = new MyLeftSildingMenu(this,R.layout.slidingmenu_left_layout);
    }

    private void addShowMenuBtn() {
        ivShowMenu = (ImageView) findViewById(R.id.iv_myfriends_showMenu);
        ivShowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftMenu.showMenu();
            }
        });
    }
    private void addListner() {
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyFriendActivity.this,
                        AddFriendActivity.class));
            }
        });
    }
}
