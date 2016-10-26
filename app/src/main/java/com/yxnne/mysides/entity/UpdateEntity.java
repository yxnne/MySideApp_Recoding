package com.yxnne.mysides.entity;

import java.io.Serializable;

/**
 * 升级信息实体类
 */
public class UpdateEntity implements Serializable{
    private String status,version,changeLog,apkUrl;

    public UpdateEntity(String status, String version, String changeLog,
                        String apkUrl) {
        super();
        this.status = status;
        this.version = version;
        this.changeLog = changeLog;
        this.apkUrl = apkUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    @Override
    public String toString() {
        return "status : "+status+"/n"
                +"version : "+version+"/n"
                + "changeLog : "+changeLog+"/n"
                +"apkUrl : "+apkUrl+"/n";
    }
}
