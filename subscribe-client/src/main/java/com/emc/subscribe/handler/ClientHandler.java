package com.emc.subscribe.handler; /**
 * Author   NieYinjun
 * Date     2018/9/22 21:10
 * TAG
 */

import com.alibaba.fastjson.JSON;

import com.emc.subscribe.dto.CommDTO;
import com.emc.subscribe.dto.RegisterTopicReqDTO;
import com.emc.subscribe.dto.RegisterTopicResDTO;
import static com.emc.subscribe.enmu.CommType.*;

import com.emc.subscribe.enmu.CommType;
import com.emc.subscribe.wrapper.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log=LoggerFactory.getLogger(ClientHandler.class);
    private MessageHandler messageHandler;
    private int heartbeatTime;
    private String username;
    private Map<String,String> topics;

    public ClientHandler(String username, Map<String,String> topics, MessageHandler messageHandler, int heartbeatTime) {
        super();
        this.username=username;
        this.topics=topics;
        this.messageHandler=messageHandler;
        this.heartbeatTime=heartbeatTime;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        messageHandler.onConnected();
        //连接成功，订阅消息
        CommDTO commDTO=new CommDTO();
        commDTO.setType(CommType.A);
        RegisterTopicReqDTO reqDTO=new RegisterTopicReqDTO();
        reqDTO.setUsername(username);
        reqDTO.setTopics(topics);
        commDTO.setData(reqDTO);
        ctx.writeAndFlush(MessageWrapper.objectWrapper(commDTO));

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        parseFromMsg(msg, ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        messageHandler.onDisconnected();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("发生异常。。。");
        cause.printStackTrace();
        ctx.close();
    }



    private void parseFromMsg(Object msg,ChannelHandlerContext ctx){

        CommDTO commDTO = JSON.parseObject(msg.toString(), CommDTO.class);
        if(commDTO.getType().equals(A)){
            try{
                RegisterTopicResDTO registerTopicResDTO = JSON.parseObject(commDTO.getData().toString(), RegisterTopicResDTO.class);
                messageHandler.onSubscribeSuccess(registerTopicResDTO);
                Set<String> topicOks = registerTopicResDTO.getTopicOk();
                if(topicOks!=null && topicOks.size()>0){
                    new Thread(()-> sendHeartBeat(ctx)).start();
                }
            }catch (Exception e){
                messageHandler.onSubscribeFailed(commDTO.getData());
                ctx.close();
            }
        }else if(commDTO.getType().equals(C)){
            messageHandler.onMessage(commDTO.getData());
        }
    }

    private void sendHeartBeat(ChannelHandlerContext ctx) {
        final CommDTO heartbeatCommDTO=new CommDTO(B);
        while(true){
            ctx.writeAndFlush(MessageWrapper.objectWrapper(heartbeatCommDTO));
            try{
                Thread.sleep(heartbeatTime);
            }catch (Exception e){ }
        }
    }
}