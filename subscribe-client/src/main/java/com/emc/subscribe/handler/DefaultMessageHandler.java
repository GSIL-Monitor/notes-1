package com.emc.subscribe.handler;

import com.alibaba.fastjson.JSON;
import com.emc.subscribe.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMessageHandler extends AbstractMessageHandler {
    private static final Logger log=LoggerFactory.getLogger(DefaultMessageHandler.class);

    @Override
    public void onMessage(Object msg) {
        log.info("----------------收到消息-----------------");
        MessageDTO messageDTO= JSON.parseObject(msg.toString(),MessageDTO.class);
        log.info("消息条数：{}",messageDTO.getTotal());
        log.info("产品ID ：{}",messageDTO.getProductId());
        log.info("消息内容：{}",messageDTO.getList());

    }
}
