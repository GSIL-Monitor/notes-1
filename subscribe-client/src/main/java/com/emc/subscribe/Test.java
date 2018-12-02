package com.emc.subscribe;

import com.emc.subscribe.client.SubscribeClientBuilder;

public class Test {
    public static void main(String[] args) {
        new SubscribeClientBuilder()
                .setIp("127.0.0.1").setPort(9765)
                .setMaxFrameLength(102400)
                .setHeartBeatTime(60000)
                .setUsername("NYJ")
                .setTopic("appTest","1000a")
                .setTopic("apdf","sdfsa")
                .setTopic("appTest1","aabc")
                //    .setMessageHandler(messageHandler)
                .build().reconnect();
    }

}
