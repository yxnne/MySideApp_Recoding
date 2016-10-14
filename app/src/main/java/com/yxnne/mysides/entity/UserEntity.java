package com.yxnne.mysides.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/14.
 * 用户实体
 */

public class UserEntity implements Serializable {
    private String username;
    private String password;
    private String nickName;

    public UserEntity(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public UserEntity(String username, String password, String nickName) {
        this(username,password);
        this.nickName = nickName;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getNickName() {
        return nickName;
    }
}
