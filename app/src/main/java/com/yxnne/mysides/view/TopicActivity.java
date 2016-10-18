package com.yxnne.mysides.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yxnne.mysides.R;

public class TopicActivity extends BaseActivity {
    private ImageView ivShowMenu;
    private MyLeftSildingMenu leftMenu ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        //添加menu
        leftMenu  = new MyLeftSildingMenu(this,R.layout.slidingmenu_left_layout);
        findViews();
        addListners();
    }

    private void addListners() {
        ivShowMenu.setOnClickListener(new MyOnclickListner());
    }

    private void findViews() {
        ivShowMenu = (ImageView) findViewById(R.id.iv_topic_showMenu);
    }

    /**
     * 点击监听器
     */
    private class MyOnclickListner implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_topic_showMenu:
                    leftMenu.showMenu();
                    break;
            }
        }
    }
}
