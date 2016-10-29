package com.yxnne.mysides.util;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.yxnne.mysides.R;
import com.yxnne.mysides.entity.OpenFireServerConfig;
import com.yxnne.mysides.entity.TomcatServerConfig;
import com.yxnne.mysides.util.log.LogGenerator;

/**
 * when app start ,read xml to get the config info
 * Created by Administrator on 2016/10/28.
 */

public class ReadConfigUtil {

    public static OpenFireServerConfig getOpenFireConfig(Context context){
        String ip = "";
        int port = 0;
        String serviceName = "";
        try {
            XmlResourceParser xmlParser =
                    //这个东西直接返回了XmlResourceParser 对象
                    context.getResources().getXml(R.xml.connection_config);
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
        LogGenerator.getInstance().printMsg("openfire parsed xml reault is:" + ip + "-" + port + "-" + serviceName);
        return new OpenFireServerConfig(serviceName,port,ip);
    }
    public static TomcatServerConfig getTomcatConfig(Context context){
        String ip = "";
        int port = 0;
        try {
            XmlResourceParser xmlParser =
                    //这个东西直接返回了XmlResourceParser 对象
                    context.getResources().getXml(R.xml.tomcat_server_config);
            int eventType = xmlParser.getEventType();
            while (XmlResourceParser.END_DOCUMENT != eventType) {
                switch (eventType) {
                    case XmlResourceParser.START_TAG:
                        if ("ip".equals(xmlParser.getName())) {
                            ip = xmlParser.nextText();
                        } else if ("port".equals(xmlParser.getName())) {
                            port = Integer.parseInt(xmlParser.nextText());

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
        LogGenerator.getInstance().printMsg("tomcat parsed xml reault is:" + ip + "-" + port );
        return new TomcatServerConfig(ip,port);
    }
}
