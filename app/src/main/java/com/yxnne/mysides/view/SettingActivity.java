package com.yxnne.mysides.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yxnne.mysides.R;
import com.yxnne.mysides.YApplication;

public class SettingActivity extends BaseActivity {
    private ImageView ivShowMenu;
    private MyLeftSildingMenu leftMenu ;
    private Button btnExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        leftMenu  = new MyLeftSildingMenu(this,R.layout.slidingmenu_left_layout);
        //addIvShowMenuBtn();
        findViews();
        addListners();
    }

    private void addListners() {
        ivShowMenu.setOnClickListener(new MyOnClickListner());
        btnExit.setOnClickListener(new MyOnClickListner());
    }

    private void findViews() {
        ivShowMenu = (ImageView) findViewById(R.id.iv_settings_showMenu);
        btnExit = (Button) findViewById(R.id.btn_setting_exit_app);
    }

    private class MyOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_settings_showMenu:
                    leftMenu.showMenu();
                    break;
                case R.id.btn_setting_exit_app:
                    YApplication.instance.exitApp();
                    break;
            }
        }
    }
}
