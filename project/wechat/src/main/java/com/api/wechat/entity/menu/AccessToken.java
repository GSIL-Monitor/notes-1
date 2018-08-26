package com.api.wechat.entity.menu;

/**
 * Author   NieYinjun
 * Date     2018/8/16 16:37
 * * 描述: 微信通用接口凭证  </br>
 * TAG
 */

public class AccessToken {
    // 获取到的凭证
    private String token;
    // 凭证有效时间，单位：秒
    private int expiresIn;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}