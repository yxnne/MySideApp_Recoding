package com.yxnne.mysides.biz;


import android.content.Intent;

import com.yxnne.mysides.YApplication;
import com.yxnne.mysides.entity.UserEntity;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;

/**
 * Created by Administrator on 2016/10/14.
 * 实现登录的业务
 */

public class LoginBiz {
    /**
     * 实现登录
     *
     * @param userEntity
     */
    public void login(final UserEntity userEntity) {
        //可能阻塞，故开线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogGenerator.getInstance().printMsg("in login");
                int loginStatus = 0;
                try {
                    if (false == YApplication.instance.getXMPPConnection().isConnected()) {
                        //链接失败的原因，1，可能是尚未连上，2，就是链接失败了
                        if (YApplication.instance.isConnectedOnce())//连了一次了，就是没连上
                        {
                            //设置状态值
                            loginStatus = Const.STATUS_CONNECT_FAILURE;
                        }else{//一次连接还未完成
                            LogGenerator.getInstance().printMsg("login when the server is not be connected!");
                            //同步机制 为了防止尚未链接到server就点击了登录
                            synchronized (YApplication.instance) {
                                YApplication.instance.wait();
                            }
                        }
                    }
                    LogGenerator.getInstance().printMsg("connect to server is done,but resault?!");
                    //如果链接成功
                    if(YApplication.instance.getXMPPConnection().isConnected()){
                        //登录
                        YApplication.instance.getXMPPConnection().login(userEntity.getUsername(),
                                userEntity.getPassword());
                        if ( YApplication.instance.getXMPPConnection().isAuthenticated()){
                            loginStatus = Const.STATUS_LOGIN_OK;
                            LogGenerator.getInstance().printMsg("login is OK");
                        }else{
                            loginStatus = Const.STATUS_LOGIN_FAILURE;
                            LogGenerator.getInstance().printMsg("login failed");
                        }

                    }else{
                        loginStatus = Const.STATUS_CONNECT_FAILURE;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogGenerator.getInstance().printError(e);
                }finally {
                    //根据状态发送广播
                    Intent intent = new Intent(Const.ACTION_LOGIN_RESAULT);
                    intent.putExtra(Const.LOGIN_STATUS,loginStatus);
                    YApplication.instance.sendBroadcast(intent);
                }

            }
        }).start();
    }
}

