package com.client.demo.message;

import com.emc.subscribe.client.SubscribeClient;
import com.emc.subscribe.client.SubscribeClientBuilder;
import com.emc.subscribe.handler.MessageHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class MsgClient {
    @Resource
    private MessageHandler messageHandler;

    @PostConstruct
    public void init(){
        new SubscribeClientBuilder()
                .setIp("127.0.0.1").setPort(9765)
                .setMaxFrameLength(102400)
                .setHeartBeatTime(60000)
                .setUsername("NYJ")
                .setTopic("appTest","1000a")
                .setTopic("apdf","sdfsa")
                .setTopic("appTest1","aabc")
           //     .setMessageHandler(messageHandler)
                .build().reconnect();
    }

}
