package com.yxnne.mysides;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import com.loopj.android.http.AsyncHttpClient;
import com.yxnne.mysides.entity.OpenFireServerConfig;
import com.yxnne.mysides.entity.PrivateChatEntity;
import com.yxnne.mysides.entity.TomcatServerConfig;
import com.yxnne.mysides.util.Const;
import com.yxnne.mysides.util.ReadConfigUtil;
import com.yxnne.mysides.util.EmojiFaceDataUtil;
import com.yxnne.mysides.util.log.LogGenerator;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
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
    private OpenFireServerConfig openFireConfig;
    private TomcatServerConfig tomcatConfig;
    public static String tomcatBaseAdress = null;
    /*静态实例引用*/
    public static YApplication instance;
    /*开始时间*/
    // private long appStartTime;
    private XMPPConnection xmppConn;
    //当前网络类型
    public static int network_type= Const.TYPE_NETWORK_WIFI;
    //
    public static ArrayList<Activity> activities = new ArrayList<>();
    //异步http任务类
    public static AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    //当前用户
    public static String currentUser;
    /**
     * 是添加好友的结果 是添加内容
     */
    public static ArrayList<Object> listMsg = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        //appStartTime = System.currentTimeMillis();
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
        return openFireConfig.getServiceName();
    }

    public XMPPConnection getXMPPConnection() {
        return xmppConn;
    }
    //暂时未用到
    public OpenFireServerConfig getOpenFireConfig() {
        return openFireConfig;
    }
    //暂时未用到
    public TomcatServerConfig getTomcatConfig() {
        return tomcatConfig;
    }

    /**
     * 读取res/xml/connection_config.xml并解析
     */
    private void readConnectionConfig() {
        openFireConfig = ReadConfigUtil.getOpenFireConfig(this);
        tomcatConfig = ReadConfigUtil.getTomcatConfig(this);
        // http://192.168.31.145:8080/MysideServer/servlet/ApkUpdateServlet";
        tomcatBaseAdress = "http://"+tomcatConfig.getIp()+":"
                +tomcatConfig.getPort()+"/MysideServer/";
        LogGenerator.getInstance().printMsg(tomcatBaseAdress);
    }

    /**
     * 链接openfire服务器
     */
    private boolean connectedOnce;/*已经连了一次*/

    public void connect2OpenfireServer() {
        if (openFireConfig.getPort() == 0
                || openFireConfig.getIp() == null
                || openFireConfig.getServiceName() == null) {
            LogGenerator.getInstance().printError("Parsed config xml has trouble!");
            return;
        }
        new Thread() {
            @Override
            public void run() {
                //第三个参数是服务器名
                ConnectionConfiguration cf = new ConnectionConfiguration(
                        openFireConfig.getIp(), openFireConfig.getPort(), openFireConfig.getServiceName());
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

            doAddFriend(packet);

            doPrivateChat(packet);
            //updateMyMsg("testUser " + "agree add");
            //关于添加好友的判断好像有些问题

        }

        /**
         * 检查是否是加好友的消息并且处理 ,这里这个功能有问题
         * @param packet 数据包
         */
        private void doAddFriend(Packet packet){
            if (packet instanceof Presence)
            {
                Presence presence=(Presence) packet;
                Presence.Type type=presence.getType();
                String user=presence.getFrom();
                if (type== Presence.Type.unsubscribe)
                {
                    LogGenerator.getInstance().printprintLog("添加好友", user+"不同意");
                    updateMyMsg("testUser " + "agree add");
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
         * 检查是否是私聊消息并处理
         * @param packet 数据包
         */
        private void doPrivateChat(Packet packet) {
            if (packet instanceof Message)
            {
                Message message = (Message) packet;
                Message.Type type = message.getType();
                //判断是不是私聊
                if (type==Message.Type.chat)
                {
                    //a@admin:aaaaa，这是发送消息的格式 不过不在这里监听
                    //回复消息的形式，例如
                    //kobe@115.159.189.43/Spark 2.6.3:ssdsds
                    //kobe@115.159.189.43 这个是好友用户名，使用软件/Spark 2.6.3
                    //ssdsds 这个是内容
                    String from = message.getFrom();
                    if (from.contains("/"))
                    {
                        from = from.substring(0, from.indexOf("/"));
                    }
                    PrivateChatEntity.addMessage(message, from);
                    LogGenerator.getInstance().printprintLog("Message----->",message.toString());
                    //发广播通知activity显示
                    Intent intent = new Intent(Const.ACTION_SEND_PRIVATE_CHAT_MSG);
                    YApplication.instance.sendBroadcast(intent);
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

