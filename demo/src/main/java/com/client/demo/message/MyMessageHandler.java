package com.client.demo.message;

import com.alibaba.fastjson.JSON;
import com.emc.subscribe.handler.AbstractMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class MyMessageHandler extends AbstractMessageHandler {
    @Override
    public void onMessage(Object o) {
        System.out.println("在这里处理消息："+o);
    }
}
