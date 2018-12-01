package com.example.netty;

import com.example.netty.client.SubscribeClientBuilder;

public class Test {
    public static void main(String[] args) {
        new SubscribeClientBuilder()
                .setIp("127.0.0.1").setPort(9765)
                .setMaxFrameLength(102400)
                .setUsername("NYJ")
                .setTopic("appTest","1000a")
                .setTopic("apdf","sdfsa")
                .setTopic("appTest1","aabc")
                .setMessageHandler(System.out::println).build().reconnect();
        //todo 用户订阅时，需要判断消息发送的类型为2才能tcp推送。
    }

}


