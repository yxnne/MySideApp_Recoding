package com.yxnne.mysides.biz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.dao.MessageDAO;
import com.yxnne.mysides.entity.PrivateChatEntity;
import com.yxnne.mysides.util.CommenTools;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.chat.ChatCommenUtil;
import com.yxnne.mysides.util.chat.ChatPictureUtil;
import com.yxnne.mysides.util.log.LogGenerator;

import org.jivesoftware.smack.packet.Message;

/**
 * private chat biz
 * user chat to his friend
 * Created by Administrator on 2016/10/29.
 */

public class PrivateChatBizAsyncTask extends AsyncTask<String,Integer,Void>{
    Context context;

    public PrivateChatBizAsyncTask(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(String... params) {

        try {
            int threadId = (int) Thread.currentThread().getId();
            LogGenerator.getInstance().printprintLog("PrivateChatBiz", "threadId=" + threadId + ",body="
                    + params[1]);
            //这个用于存储
            Message message = new Message();
            message.setTo(params[0]);
            message.setBody(params[1]);
            message.setType(Message.Type.chat);
            message.setFrom(YApplication.currentUser);
            YApplication.instance.getXMPPConnection().sendPacket(message);

            //消息插入数据库,在插入数据库的时候判断下
            MessageDAO messageDAO = new MessageDAO(context);
            messageDAO.insert(message);

            //添加记录消息
            PrivateChatEntity.addMessage(message, params);
        } catch (Exception e) {
            LogGenerator.getInstance().printError(e);
        }finally {
            //发送广播
            Intent intent = new Intent(Const.ACTION_SEND_PRIVATE_CHAT_MSG);
            YApplication.instance.sendBroadcast(intent);
        }
        return null;
    }
}
