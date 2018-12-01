package com.client.demo.message;

import com.emc.subscribe.handler.MessageHandler;

public class MyMessageHandler implements MessageHandler {
    @Override
    public void message(Object o) {
        System.out.println(o);
    }
}
