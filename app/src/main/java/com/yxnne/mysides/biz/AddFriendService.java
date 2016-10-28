package com.yxnne.mysides.biz;

import android.app.IntentService;
import android.content.Intent;

import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.entity.FriendEntity;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;

import org.jivesoftware.smack.Roster;

/**
 * AddFriendService
 * Created by Administrator on 2016/10/27.
 */

public class AddFriendService extends IntentService {
    //need constructor
    public AddFriendService() {
        super("AddFriendService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // IntentService 自动运行在工作线程
            //int threadId = (int) Thread.currentThread().getId();
            // 取数据
            FriendEntity friendEntity = (FriendEntity) intent
                    .getSerializableExtra(Const.STATUS_KEY);
            LogGenerator.getInstance().printMsg(friendEntity.toString());
            // 花名册：1，添加好友，2，得到好友
            Roster roster = YApplication.instance
                    .getXMPPConnection().getRoster();
            // asmack一个用户有三个名称
            // username:a1
            // name:张三 这个是昵称
            // user:全称 a1@yxnne.com
            String user = friendEntity.getUsername() + "@"
                    + YApplication.instance.getServiceName();
            // asmack可以把一个好友放到多个组
            String[] groups = { friendEntity.getGroup() };
            roster.createEntry(user, friendEntity.getName(), groups);
        } catch (Exception e) {
            LogGenerator.getInstance().printError(e);
        } finally {
            Intent i = new Intent(Const.ACTION_SEND_ADD_FRIEND);
            YApplication.instance.sendBroadcast(i);
        }

    }
}
