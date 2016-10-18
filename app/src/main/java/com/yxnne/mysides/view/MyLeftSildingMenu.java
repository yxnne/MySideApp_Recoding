package com.yxnne.mysides.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yxnne.mysides.R;

/**
 * 左侧的SlidingMenu
 * Created by Administrator on 2016/10/18.
 */

public class MyLeftSildingMenu {
    private SlidingMenu mSlidingMenu;
    private Activity mActivity;
    Button[] btnArray = new Button[7];
    Class[] classArray = {
            TopicActivity.class, NearbyTopicActivity.class,
            TopicActivity.class, MyFriendActivity.class,
            MyMsgActivity.class, MyProfileActivity.class,
            SettingActivity.class };

    /**
     * 传进来Activty用于绑定
     * @param a 绑定activty
     * @param resId layout View 的 ResId
     */
    MyLeftSildingMenu(final Activity a , int resId){
        mSlidingMenu = new SlidingMenu(a);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        //mSlidingMenu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.attachToActivity(a, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        View view = View.inflate(a, resId,null);
        mSlidingMenu.setMenu(view);
        //关联button 和 相应的activity
        findBtnViews(a);
        //设置监听器 完成跳转
        for(int i = 0; i < btnArray.length ; i++){
            final int flag = i;
            btnArray[flag].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    a.startActivity(new Intent(a,classArray[flag]));
                }
            });
        }

    }

    private void findBtnViews(Activity a) {
        btnArray[0] = (Button) a.findViewById(R.id.btn_leftmenu_allTopic);
        btnArray[1] = (Button) a.findViewById(R.id.btn_leftmenu_nearbyTopic);
        btnArray[2] = (Button) a.findViewById(R.id.btn_leftmenu_allTopic);
        btnArray[3] = (Button) a.findViewById(R.id.btn_leftmenu_my_friend);
        btnArray[4] = (Button) a.findViewById(R.id.btn_leftmenu_my_msg);
        btnArray[5] = (Button) a.findViewById(R.id.btn_leftmenu_my_res);
        btnArray[6] = (Button) a.findViewById(R.id.btn_leftmenu_setting);
    }


    public void showMenu(){
        mSlidingMenu.showMenu();
    }
}
