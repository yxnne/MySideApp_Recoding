package com.yxnne.mysides.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yxnne.mysides.R;

public class MyFriendActivity extends BaseActivity {
    private MyLeftSildingMenu leftMenu ;
    private ImageView ivShowMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
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
}
