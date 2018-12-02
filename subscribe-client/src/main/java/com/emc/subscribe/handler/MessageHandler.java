package com.emc.subscribe.handler;

import com.emc.subscribe.dto.RegisterTopicResDTO;

import java.util.Set;

public interface MessageHandler {

    void onConnected();

    void onSubscribeSuccess( RegisterTopicResDTO registerTopicResDTO);

    void onSubscribeFailed(Object errorMsg);

    void onDisconnected();

    void onMessage(Object msg);
}
