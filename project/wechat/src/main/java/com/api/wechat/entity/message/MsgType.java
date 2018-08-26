package com.api.wechat.entity.message;

/**
 * Author   NieYinjun
 * Date     2018/8/15 13:59
 * TAG   MsgType的枚举类
 */
public enum MsgType {

    Text("text"),
    Image("image"),
    Music("music"),
    Video("video"),
    Voice("voice"),
    Location("location"),
    Link("link"),

    Event("event");

    private String msgType = "";

    MsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * @return the msgType
     */
    @Override
    public String toString() {
        return msgType;
    }

}