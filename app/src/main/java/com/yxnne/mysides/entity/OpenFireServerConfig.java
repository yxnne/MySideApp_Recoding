package com.yxnne.mysides.entity;

/**
 * config entity
 * Created by Administrator on 2016/10/28.
 */

public class OpenFireServerConfig {
    private String serviceName;
    private int port;
    private String ip;

    public OpenFireServerConfig(String serviceName, int port, String ip) {
        this.serviceName = serviceName;
        this.port = port;
        this.ip = ip;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "OpenFireServerConfig{" +
                "serviceName='" + serviceName + '\'' +
                ", port=" + port +
                ", ip='" + ip + '\'' +
                '}';
    }
}
