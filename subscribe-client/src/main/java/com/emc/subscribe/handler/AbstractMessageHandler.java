package com.emc.subscribe.handler;

import com.emc.subscribe.dto.RegisterTopicResDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMessageHandler implements MessageHandler{
    private static final Logger log=LoggerFactory.getLogger(AbstractMessageHandler.class);

    public void onConnected(){
        log.info("已成功连接到服务端，准备发送注册信息。。。");
    }

    public void onSubscribeSuccess( RegisterTopicResDTO registerTopicResDTO){
        log.info("订阅成功：{}",registerTopicResDTO.getTopicOk());
        log.warn("订阅失败：{}",registerTopicResDTO.getTopicError());
    }

    public void onSubscribeFailed(Object errorMsg){
        log.error("注册失败：{}",errorMsg);
    }
    public void onDisconnected(){
        log.info("断开连接。。。");
    }
    public abstract void onMessage(Object msg);

}
