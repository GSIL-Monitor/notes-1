package com.api.wechat.entity.menu;

/**
 * Author   NieYinjun
 * Date     2018/8/16 16:30
 * TAG
 */
public class Button {

    private String name;//所有一级菜单、二级菜单都共有一个相同的属性，那就是name

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}