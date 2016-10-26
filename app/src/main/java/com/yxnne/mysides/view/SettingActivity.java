package com.yxnne.mysides.view;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.yxnne.mysides.R;
import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.biz.UpdateBiz;
import com.yxnne.mysides.entity.UpdateEntity;
import com.yxnne.mysides.util.CommenTools;
import com.yxnne.mysides.util.Const;

public class SettingActivity extends BaseActivity {
    private ImageView ivShowMenu;
    private MyLeftSildingMenu leftMenu;
    private Button btnExit, btnUpdate;
    public static final int MSG_SHOW_VERSION = 1;
    public static final int MSG_INSTALL_APK = 2;
    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int msgId = msg.what;
            Bundle bundle = msg.getData();
            switch (msgId) {
                case MSG_SHOW_VERSION:
                    UpdateEntity updateEntity = (UpdateEntity) bundle
                            .getSerializable(Const.STATUS_KEY);
                    showVersion(updateEntity);
                    break;

                case MSG_INSTALL_APK:
                    break;
            }
        }

        private void showVersion(UpdateEntity updateEntity) {
            // 判断updateEntity是否为空
            if (updateEntity == null) {
                Toast.makeText(SettingActivity.this,
                        getResources().getString(R.string.err_update_fail),
                        Toast.LENGTH_LONG).show();
            } else {
                // 判断有没有新版本
                String currentVersion = CommenTools
                        .getVersionName(SettingActivity.this);
                if (Double.parseDouble(currentVersion) >= Double
                        .parseDouble(updateEntity.getVersion())) {
                    Toast.makeText(SettingActivity.this,
                            getResources().getString(R.string.err_update_current_is_new),
                            Toast.LENGTH_LONG).show();

                } else {
                    // 显示dialog
                    AlertDialog.Builder dialog = new AlertDialog.Builder(
                            SettingActivity.this);
                    dialog.setMessage(getResources().getString(R.string.version_update) + updateEntity.getVersion() + "\n"
                            + updateEntity.getChangeLog() + "\n" + getResources().getString(R.string.msg_marry_choose_to_update));
                    dialog.setPositiveButton(getResources().getString(R.string.btn_dailog_update_now),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }
                            });
                    dialog.setNegativeButton(getResources().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    dialog.cancel();
                                }
                            });
                    dialog.show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        leftMenu = new MyLeftSildingMenu(this, R.layout.slidingmenu_left_layout);
        //addIvShowMenuBtn();
        findViews();
        addListners();
    }

    private void addListners() {
        ivShowMenu.setOnClickListener(new MyOnClickListner());
        btnExit.setOnClickListener(new MyOnClickListner());
        btnUpdate.setOnClickListener(new MyOnClickListner());
    }

    private void findViews() {
        ivShowMenu = (ImageView) findViewById(R.id.iv_settings_showMenu);
        btnExit = (Button) findViewById(R.id.btn_setting_exit_app);
        btnUpdate = (Button) findViewById(R.id.btn_setting_update_app);
    }

    /**
     * 调用一个更新App的业务类
     */
    private void getUpdateApp() {
        UpdateBiz updateBiz = new UpdateBiz();
        updateBiz.getVersion(handler);
    }

    private class MyOnClickListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_settings_showMenu:
                    leftMenu.showMenu();
                    break;
                case R.id.btn_setting_exit_app:
                    YApplication.instance.exitApp();
                    break;
                case R.id.btn_setting_update_app:
                    getUpdateApp();
                    break;
            }
        }
    }
}
