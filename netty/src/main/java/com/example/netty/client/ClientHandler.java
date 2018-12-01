package com.example.netty.client; /**
 * Author   NieYinjun
 * Date     2018/9/22 21:10
 * TAG
 */
import com.alibaba.fastjson.JSON;
import com.example.netty.dto.RegisterTopicReqDTO;
import com.example.netty.dto.RegisterTopicResDTO;
import com.example.netty.server.CommDTO;
import com.example.netty.server.MessageWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Map;

import static com.example.netty.client.CommType.*;

@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private MessageHandler messageHandler;
    private String username;
    private Map<String,String> topics;

    public ClientHandler(String username,Map<String,String> topics,MessageHandler messageHandler) {
        super();
        this.username=username;
        this.topics=topics;
        this.messageHandler=messageHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("{}:------------------channelActive-------------------------",new Date());
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("--------------------channelRead------------------------");
        parseFromMsg(msg, ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("-------------------channelReadComplete-------------------------");
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("-----------------exceptionCaught------------------------------");
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
                    new Thread(()->{sendHeartBeat(ctx);}).start();
                }
            }catch (Exception e){
                log.error("注册失败：{}",commDTO.getData());
                ctx.close();
            }
        }else if(commDTO.getType().equals(C)){
            log.info("设备信息！");
            if(messageHandler!=null){
                messageHandler.message(commDTO.getData());
            }
        }
    }

    private void sendHeartBeat(ChannelHandlerContext ctx) {
        ByteBuf heartbeatByteBuf = MessageWrapper.objectWrapper(new CommDTO(B));
        while(true){
            ctx.writeAndFlush(heartbeatByteBuf);
            try{
                Thread.sleep(60000);
            }catch (Exception e){ }
        }
    }
}