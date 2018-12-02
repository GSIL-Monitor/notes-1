package com.emc.netty.client; /**
 * Author   NieYinjun
 * Date     2018/9/22 21:10
 * TAG
 */
import com.alibaba.fastjson.JSON;
import com.emc.netty.dto.RegisterTopicReqDTO;
import com.emc.netty.dto.RegisterTopicResDTO;
import com.emc.netty.server.CommDTO;
import com.emc.netty.server.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static com.emc.netty.client.CommType.*;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log=LoggerFactory.getLogger(ClientHandler.class);
    private MessageHandler messageHandler;
    private String username;
    private Map<String,String> topics;

    ClientHandler(String username,Map<String,String> topics,MessageHandler messageHandler) {
        super();
        this.username=username;
        this.topics=topics;
        this.messageHandler=messageHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        log.info("已成功连接到服务端，准备发送注册信息。。。");
        //连接成功，订阅消息
        CommDTO commDTO=new CommDTO();
        commDTO.setType(A);
        RegisterTopicReqDTO reqDTO=new RegisterTopicReqDTO();
        reqDTO.setUsername(username);
        reqDTO.setTopics(topics);
        commDTO.setData(reqDTO);
        ctx.writeAndFlush(MessageWrapper.objectWrapper(commDTO));

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        log.info("读取信息。。。");
        parseFromMsg(msg, ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("读取完成。。。");
        super.channelReadComplete(ctx);
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
                log.info("订阅成功：{}",registerTopicResDTO.getTopicOk());
                log.warn("订阅失败：{}",registerTopicResDTO.getTopicError());
                if(!CollectionUtils.isEmpty(registerTopicResDTO.getTopicOk())){
                    new Thread(()-> sendHeartBeat(ctx)).start();
                }
            }catch (Exception e){
                log.error("注册失败：{}",commDTO.getData());
                ctx.close();
            }
        }else if(commDTO.getType().equals(C)){
            if(messageHandler!=null){
                messageHandler.message(commDTO.getData());
            }
        }
    }

    private void sendHeartBeat(ChannelHandlerContext ctx) {
        final CommDTO heartbeatCommDTO=new CommDTO(B);
        while(true){
            ctx.writeAndFlush(MessageWrapper.objectWrapper(heartbeatCommDTO));
            try{
                Thread.sleep(60000);
            }catch (Exception e){ }
        }
    }
}