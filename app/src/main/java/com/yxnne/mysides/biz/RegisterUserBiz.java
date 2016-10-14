package com.yxnne.mysides.biz;

import android.accounts.AccountManager;
import android.os.Handler;

import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.entity.UserEntity;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;

/**
 * 注册新用户
 * Created by Administrator on 2016/10/15.
 */

public class RegisterUserBiz {
    /**
     * 注册方法，异步
     * @param handler 计划用handler机制进行异步消息传输
     * @param userEntity 用户实体
     */
    public void register(final Handler handler, final UserEntity userEntity) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
//                AccountManager accountManager =
//                        YApplication.instance.getXMPPConnection().
//                Registration reg = new Registration();
//                reg.setType(IQ.Type.SET);
//                reg.setTo(YApplication.instance.getXMPPConnection().getServiceName());
//                reg.setAttributes();
//                   // reg.
//                reg.setUsername(registerUserName.getText().toString());//注意这里createAccount注册时，参数是username，不是jid，是“@”前面的部分。
//
//                reg.setPassword(registerPassword.getText().toString());
//                reg.addAttribute(“android”, “geolo_createUser_android”);//这边addAttribute不能为空，否则出错。所以做个标志是android手机创建的吧！！！！！
//
//                PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()),
//                        new PacketTypeFilter(IQ.class));
//                PacketCollector collector = YApplication.instance.getXMPPConnection().createPacketCollector(filter);
//
//                    YApplication.instance.getXMPPConnection().sendPacket(reg);
//
//                IQ result = (IQ)collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }.start();


    }
}
