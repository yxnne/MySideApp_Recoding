package com.yxnne.mysides.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yxnne.mysides.R;
import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.adapter.MyFriendsExpandableAdapter;
import com.yxnne.mysides.biz.PrivateChatBizAsyncTask;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import java.util.ArrayList;

public class MyFriendActivity extends BaseActivity {
    private MyLeftSildingMenu leftMenu ;
    private ImageView ivShowMenu;
    private TextView tvAdd;
    private ExpandableListView expLvMyFriend;
    private PopupWindow popupWindow;
    private RelativeLayout relativeLayout;
    private MyFriendsExpandableAdapter expAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
        try {
            findView();
            addShowMenuBtn();
            addListner();
            getFriends();
        }catch (Exception e){
            LogGenerator.getInstance().printError(e);
        }

    }

    private void findView() {
        expLvMyFriend = (ExpandableListView) findViewById(R.id.expandable_lv_myfriend);
        tvAdd = (TextView) findViewById(R.id.tv_myfriend_addmore);
        leftMenu  = new MyLeftSildingMenu(this,R.layout.slidingmenu_left_layout);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_myfrined_activivty);
    }
    private void getFriends(){
        XMPPConnection connection = YApplication.instance.getXMPPConnection();
        if(connection.isConnected()){
            LogGenerator.getInstance().printMsg("connection.isConnected()");
            Roster roster = connection.getRoster();
            ArrayList<RosterGroup> listGroup = new ArrayList(roster.getGroups());
            LogGenerator.getInstance().printMsg( listGroup.toString());
            expAdapter = new MyFriendsExpandableAdapter(this, listGroup);
            expLvMyFriend.setAdapter(expAdapter);
        }else{
            LogGenerator.getInstance().printError("no connection,when get friend ");
            //TODO 通知用户，链接失败
        }

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
        // btn add frinend listner
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogGenerator.getInstance().printMsg("press add +");
                try {
                    if (popupWindow == null) {
                        // 显示my_friend_more.xml
                        View view = View.inflate(MyFriendActivity.this,
                                R.layout.popup_myfriend_add_more, null);
                        popupWindow = new PopupWindow(view,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        //这个方法显示在view内
//                        popupWindow.showAtLocation(relativeLayout, Gravity.TOP
//                                | Gravity.RIGHT, 0, relativeLayout.getHeight() + 100);
                        //显示在控件下面
                        popupWindow.showAsDropDown(relativeLayout,0, 0, Gravity.TOP | Gravity.END);

                        Button btnAddNormal = (Button) view
                                .findViewById(R.id.btn_my_friend_more_addFriend_normal);
                        btnAddNormal.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MyFriendActivity.this,
                                        AddFriendActivity.class));
                                closePopupWindow();
                            }
                        });
                    } else {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                } catch (Exception e) {
                    LogGenerator.getInstance().printError(e);
                }

            }
        });

        //expandableListview listner
        expLvMyFriend.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    LogGenerator.getInstance().printMsg("explv click!");
                try {
                    RosterEntry rosterEntry =
                            (RosterEntry) expAdapter.getChild(groupPosition, childPosition);
                    String friendUser=rosterEntry.getUser();
//
//                    for(int i=0;i<100;i++)
//                    {
////                      PrivateChatBiz privateChatBiz = new PrivateChatBiz();
////                      privateChatBiz.sendMessage(friendUser, "你好"+i);
//                        //使用异步任务类更好
//                        PrivateChatBizAsyncTask privateChatTask = new PrivateChatBizAsyncTask();
//                        privateChatTask.execute(friendUser, "你好,你好" + i);
//                    }
                    Intent intent = new Intent(MyFriendActivity.this,
                            PrivateChatActivity.class);
                    intent.putExtra(Const.STATUS_KEY, friendUser);
                    startActivity(intent);
                } catch (Exception e) {
                    LogGenerator.getInstance().printError(e);
                }
                return false;

            }
        });
    }
    private void closePopupWindow(){
        if (popupWindow!=null && popupWindow.isShowing())
        {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(popupWindow!=null && popupWindow.isShowing()){
                popupWindow.dismiss();
                popupWindow = null;
                return true;//这个事件我处理了
            }else{
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
