package com.client.demo.message;

import com.emc.subscribe.client.SubscribeClientBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MsgClient {

    @PostConstruct
    public void init(){
        new SubscribeClientBuilder()
                .setIp("127.0.0.1").setPort(9765)
                .setMaxFrameLength(102400)
                .setUsername("NYJ")
                .setTopic("appTest","1000a")
                .setTopic("apdf","sdfsa")
                .setTopic("appTest1","aabc")
                .setMessageHandler(new MyMessageHandler()).build().reconnect();
    }

}
