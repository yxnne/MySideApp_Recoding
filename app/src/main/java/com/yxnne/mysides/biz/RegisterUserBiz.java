package com.yxnne.mysides.biz;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.entity.UserEntity;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;

import org.jivesoftware.smack.AccountManager;

import java.util.HashMap;


/**
 * 注册新用户
 * Created by Administrator on 2016/10/15.
 */

public class RegisterUserBiz {
    /**
     * 注册方法，异步
     *
     * @param handler 计划用handler机制进行异步消息传输
     * @param userEntity 用户实体
     */

    public void register(final Handler handler, final UserEntity userEntity) {
        new Thread() {
            public void run() {
                int status = Const.STATUS_REGIST_OK;
                try {
                    // 帐户管理
                    AccountManager accountManager =
                            YApplication.instance.getXMPPConnection().getAccountManager();
                    // 呢称必须放到map中
                    HashMap<String, String> map = new HashMap<>();
                    // 放的是呢称，key必须是name map.get("name")
                    map.put("name", userEntity.getNickName());
                    accountManager.createAccount(userEntity.getUsername(),
                            userEntity.getPassword(), map);
                } catch (Exception e) {
                    //处理错误，如果有错误，设置状态码
                    status = Const.STATUS_REGIST_FAILED;
                    LogGenerator.getInstance().printMsg("regist failed");
                    LogGenerator.getInstance().printError(e);
                    if(e.getMessage().equals("conflict(409)")){//这个表示冲突
                        status = Const.STATUS_REGIST_CONFLICT;
                    }
                } finally {//发消息
                    //1,得到message
                    Message message = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Const.STATUS_KEY, status);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        }.start();

    }
}