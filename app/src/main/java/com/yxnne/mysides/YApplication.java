package com.yxnne.mysides;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.XmlResourceParser;

import com.loopj.android.http.AsyncHttpClient;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.log.LogGenerator;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;

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
    /**
     * 是添加好友的结果 是添加内容
     */
    public static ArrayList<Object> listMsg = new ArrayList<Object>();

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

    public String getServiceName() {
        return serviceName;
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
                registerListenerInterceptor();
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


    public void registerListenerInterceptor() {
        // 4 让框架中的接口指向实现类
        AllPacketListener allPacketListener = new AllPacketListener();
        // null:只要接收到服务器返回的内容就执行allPacketListener
        xmppConn.addPacketListener(allPacketListener, null);
        AllPacketInterceptor allPacketInterceptor = new AllPacketInterceptor();
        xmppConn.addPacketInterceptor(allPacketInterceptor, null);
    }

    /**
     * server 返回消息的监听
     */
    class AllPacketListener implements PacketListener {

        @Override
        public void processPacket(Packet packet) {
            // packet 数据包 发给服务器的数据，接收的数据
            // 服务器通过网络发过来的是xml,
            // xmppConnection.socket收到信息，
            // 把信息放到packet,调接口packetListener
            String objectInfo = packet.toString();
            String xml = packet.toXML();
            LogGenerator.getInstance().printprintLog(
                    "AllPacketListener", "对象：" + objectInfo + "\nxml内容 " + xml
            );
            //TODO Test the function add friends
            LogGenerator.getInstance().printMsg("tobe test add friends");
            updateMyMsg("testUser " + "agree add");
            //关于添加好友的判断好像有些问题
            if (packet instanceof Presence)
            {
                Presence presence=(Presence) packet;
                Presence.Type type=presence.getType();
                String user=presence.getFrom();
                if (type== Presence.Type.unsubscribe)
                {
                    LogGenerator.getInstance().printprintLog("添加好友", user+"不同意");

                }
            }

            //数据放的是我的所有好友
            if (packet instanceof RosterPacket)
            {
                RosterPacket rosterPacket=(RosterPacket) packet;
                //所有好友
                ArrayList<RosterPacket.Item> list =
                        new ArrayList<>(rosterPacket.getRosterItems());
                for (RosterPacket.Item item:list)
                {
                    RosterPacket.ItemType itemType=  item.getItemType();
                    String user=item.getUser();
                    if (itemType== RosterPacket.ItemType.both)
                    {
                        LogGenerator.getInstance().printprintLog("添加好友", user+"不同意");
                    }
                }
            }
        }

        /**
         * 更新消息
         * @param data 数据
         */
        private void updateMyMsg(Object data) {
            listMsg.add(data);
            Intent intent = new Intent(Const.ACTION_UPDATE_MY_MSG);
            sendBroadcast(intent);
        }

    }

    /**
     * 向server发消息
     */
    class AllPacketInterceptor implements PacketInterceptor {
        // 客户端发给服务器
        @Override
        public void interceptPacket(Packet packet) {
            // TODO Auto-generated method stub
            String objectInfo = packet.toString();
            String xml = packet.toXML();
            LogGenerator.getInstance().printprintLog(
                    "AllPacketInterceptor", "对象：" + objectInfo + "xml内容 " + xml
            );
        }

    }
}

