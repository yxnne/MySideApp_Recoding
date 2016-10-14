package com.yxnne.mysides;

import android.app.Application;
import android.content.res.XmlResourceParser;

import com.yxnne.mysides.util.log.LogGenerator;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

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

    @Override
    public void onCreate() {
        super.onCreate();
        appStartTime = System.currentTimeMillis();
        LogGenerator.getInstance().printMsg("APP Start");
        instance = this;
        readConnectionConfig();
        connect2OpenfireServer();
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

    private void connect2OpenfireServer() {
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
                xmppConn = new XMPPTCPConnection(cf);
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

