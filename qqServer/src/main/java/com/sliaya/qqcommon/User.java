package com.sliaya.qqcommon;

import java.io.Serializable;

/**
 * 用户类
 *
 * 网络传输需要实现序列化接口
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;      // 用户名id

    private String password;    // 用户密码

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
