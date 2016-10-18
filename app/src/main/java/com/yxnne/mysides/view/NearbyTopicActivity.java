package com.yxnne.mysides.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yxnne.mysides.R;

public class NearbyTopicActivity extends AppCompatActivity {
    private ImageView ivNearbyShowMenu;
    private MyLeftSildingMenu leftMenu ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_topic);
        leftMenu  = new MyLeftSildingMenu(this,R.layout.slidingmenu_left_layout);
        addIvShowMenuBtn();
    }
    private void addIvShowMenuBtn() {
        ivNearbyShowMenu = (ImageView) findViewById(R.id.iv_nearby_showMenu);
        ivNearbyShowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftMenu.showMenu();
            }
        });
    }
}
