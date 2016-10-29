package com.yxnne.mysides.biz;

import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.util.log.LogGenerator;

import org.jivesoftware.smack.packet.Message;

/**
 * private chat biz
 * user chat to his friend
 * Created by Administrator on 2016/10/29.
 */

public class PrivateChatBiz {
    /**
     * send msg to someone
     * @param friendUser friend
     * @param body msg
     */
    public void sendMessage(final String friendUser, final String body) {
        new Thread() {
            @Override
            public void run() {
                try {
                    int threadId = (int) this.getId();
                    LogGenerator.getInstance().printprintLog(
                            "PrivateChatBiz",
                            "threadId=" + threadId + ",body="+ body);
                    Message message = new Message();

                    message.setTo(friendUser);
                    message.setBody(body);
                    message.setType(Message.Type.chat);
                    message.setFrom(YApplication.currentUser);
                    YApplication.instance.getXMPPConnection().sendPacket(message);
                } catch (Exception e) {
                    LogGenerator.getInstance().printError(e);
                }
            }
        }.start();

    }
}
