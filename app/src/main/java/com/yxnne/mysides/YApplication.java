package com.yxnne.mysides;

import android.app.Activity;
import android.app.Application;
import android.content.res.XmlResourceParser;

import com.loopj.android.http.AsyncHttpClient;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/14.
 *  me.biubiubiu.justifytext.library
 */

public class YApplication extends Application {
    /*链接openfire的相关参数*/
    private String serviceName;
    private int port;
    private String ip;
    /*静态实例引用*/
    public static YApplication instance;
    /*开始时间*/
    private long appStartTime;
    private XMPPConnection xmppConn;
    //当前网络类型
    public static int network_type= Const.TYPE_NETWORK_WIFI;
    //
    public static ArrayList<Activity> activities = new ArrayList<>();
    //异步http任务类
    public static AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    @Override
    public void onCreate() {
        super.onCreate();
        appStartTime = System.currentTimeMillis();
        LogGenerator.getInstance().printMsg("APP Start");
        instance = this;
        readConnectionConfig();
        connect2OpenfireServer();
    }
    /**
     * 程序退出的方法
     */
    public void exitApp(){
        // 关闭所有activity
        for (Activity activity : activities) {
            activity.finish();
        }
        // 断开与服务器的连接
        xmppConn.disconnect();
        //结束进程
        System.exit(0);
    }

    public XMPPConnection getXMPPConnection() {
        return xmppConn;
    }

    /**
     * 读取res/xml/connection_config.xml并解析
     */
    private void readConnectionConfig() {
        try {
            XmlResourceParser xmlParser =
                    //这个东西直接返回了XmlResourceParser 对象
                    this.getResources().getXml(R.xml.connection_config);
            int eventType = xmlParser.getEventType();
            while (XmlResourceParser.END_DOCUMENT != eventType) {
                switch (eventType) {
                    case XmlResourceParser.START_TAG:
                        if ("ip".equals(xmlParser.getName())) {
                            ip = xmlParser.nextText();
                        } else if ("port".equals(xmlParser.getName())) {
                            port = Integer.parseInt(xmlParser.nextText());

                        } else if ("serviceName".equals(xmlParser.getName())) {
                            serviceName = xmlParser.nextText();
                        }
                        break;
                    case XmlResourceParser.END_TAG:
                        break;
                    default:
                        break;
                }
                //别忘了
                eventType = xmlParser.next();
            }
            xmlParser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogGenerator.getInstance().printMsg("parsed xml reault is" + ip + "-" + port + "-" + serviceName);
    }

    /**
     * 链接openfire服务器
     */
    private boolean connectedOnce;/*已经连了一次*/

    public void connect2OpenfireServer() {
        if (port == 0 || ip == null || serviceName == null) {
            LogGenerator.getInstance().printError("Parsed config xml has trouble!");
            return;
        }
        new Thread() {
            @Override
            public void run() {
                //第三个参数是服务器名
                ConnectionConfiguration cf = new ConnectionConfiguration(ip, port, serviceName);
                //设置安全类型,这个必须要有
                cf.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
                //
                SASLAuthentication.supportSASLMechanism("PLAIN",0);
                xmppConn = new XMPPConnection(cf);
                try {
                    xmppConn.connect();
                    //同步机制 为了防止尚未链接到server就点击了登录
                    synchronized (YApplication.instance) {
                        YApplication.instance.notify();
                        LogGenerator.getInstance().printMsg("connect is done,notify");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogGenerator.getInstance().printError(e);
                }
                LogGenerator.getInstance().printMsg(
                        "连接结果 =" + xmppConn.isConnected());
                connectedOnce = true;
            }
        }.start();
    }

    public boolean isConnectedOnce() {
        return connectedOnce;
    }
}

