package com.yxnne.mysides.entity;

import java.io.Serializable;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.packet.Message;

/**
 * 聊天信息对象
 */
public class PrivateChatEntity implements Serializable{

    //线程安全 使用Vector 和 ConcurrentHashMap
    public static ConcurrentHashMap<String, Vector<Message>> map = new ConcurrentHashMap<>();

    public static void addMessage(Message message, String... params) {
        //把数据放到实体类
        Vector<Message> vector = PrivateChatEntity.map.get(params[0]);
        if (vector == null)
        {
            vector = new Vector<Message>();
            PrivateChatEntity.map.put(params[0], vector);
        }
        vector.add(message);
    }
}
