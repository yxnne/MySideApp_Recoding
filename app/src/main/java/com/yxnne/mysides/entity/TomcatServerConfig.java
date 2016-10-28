package com.yxnne.mysides.entity;

/**
 * config entity
 * Created by Administrator on 2016/10/28.
 */

public class TomcatServerConfig {
    private String ip;
    private int port;

    public TomcatServerConfig(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "TomcatServerConfig{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
