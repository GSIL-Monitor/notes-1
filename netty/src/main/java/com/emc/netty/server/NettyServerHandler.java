package com.emc.netty.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.emc.netty.dto.RegisterTopicReqDTO;
import com.emc.netty.dto.RegisterTopicResDTO;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

import static com.emc.netty.client.CommType.*;

/**
 * @author yandixuan
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Resource
    private ChannelManagement channelManagement;

    @Resource
    private CheckChannelActive checkChannelActive;

    /**
     *netty服务端：开启服务端等待连接 ——》 收到连接等待客户端验证及订阅消息 ——》 验证成功（不成功或验证失败关闭连接）——》存储用户订阅信息及通道信息Map<username,<appIds,ctx>>
     *服务端接受连接，注册订阅信息，心跳信息，忽略其它信息
     * @param ctx 通道对象
     * @param msg 客户端发送的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        CommDTO commDTO=parseFromMsg(msg,ctx);
        switch (commDTO.getType()){
            case A:
                log.info("注册信息：{}",msg);
                registerTopic((RegisterTopicReqDTO)commDTO.getData(),ctx);
                break;
            case B:
                break;
            case D:
                break;
            default:
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("发生异常---{}", ctx);
        channelManagement.removeCtx(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("收到连接---{}", ctx);
        super.channelActive(ctx);
        //设备连接后30s验证是否已注册，未注册主动断开连接
   //     checkChannelActive.checkChannelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开连接---{}", ctx);
        super.channelInactive(ctx);
        channelManagement.removeCtx(ctx);
    }

    private void registerTopic(RegisterTopicReqDTO registerData,ChannelHandlerContext ctx){
        RegisterTopicResDTO topicResDTO=null;
        try{
            topicResDTO=checkUserTopic(registerData);
        }catch (Exception e){
            ctx.writeAndFlush(MessageWrapper.objectWrapper(new CommDTO(A,"server error!")));
            log.warn("网络连接失败，无法验证注册信息！");
            ctx.close();
        }
        if(topicResDTO==null){
            ctx.writeAndFlush(MessageWrapper.objectWrapper(new CommDTO(A,"username error!")));
            log.warn("用户名错误，注册失败！");
            ctx.close();
        }else{
            Set<String> topicsOk=topicResDTO.getTopicOk();
            if(!CollectionUtils.isEmpty(topicsOk)){
                channelManagement.addCtx(topicResDTO.getUserId(),topicsOk,ctx);
            }
            ctx.writeAndFlush(MessageWrapper.objectWrapper(new CommDTO(A,topicResDTO)));
        }
    }

    private RegisterTopicResDTO checkUserTopic(RegisterTopicReqDTO registerData) {
        RegisterTopicResDTO resDTO=new RegisterTopicResDTO();
        if(registerData.getUsername().equals("NYJ")){
            resDTO.setUserId("10001");
        }else {
            return null;
        }
        Map<String, String> topics = registerData.getTopics();
        Set<String> topicOk=new HashSet<>();
        Map<String,String> topicError=new HashMap<>();
        topics.forEach((k,v)->{
            if(!k.startsWith("app")){
                topicError.put(k,"error appId!");
            }else if(v.startsWith("100")){
                topicOk.add(k);
            }else {
                topicError.put(k,"error secret!");
            }
        });
        resDTO.setTopicError(topicError);
        resDTO.setTopicOk(topicOk);
        return resDTO;

    }

    private CommDTO parseFromMsg(Object msg,ChannelHandlerContext ctx){
        CommDTO commDTO=new CommDTO();
        try{
            commDTO = JSON.parseObject(msg.toString(),CommDTO.class);
            if(!StringUtils.isEmpty(commDTO.getData())){
                JSONObject jsonObject =JSON.parseObject(commDTO.getData().toString());
                RegisterTopicReqDTO registerTopic=new RegisterTopicReqDTO();
                registerTopic.setUsername(jsonObject.getString("username"));
                String topics = jsonObject.get("topics").toString();
                Map<String,String> topicMap=JSON.parseObject(topics,Map.class);
                registerTopic.setTopics(topicMap);
                commDTO.setData(registerTopic);
            }
        }catch (Exception e){
            //消息格式错误
            ctx.writeAndFlush(MessageWrapper.objectWrapper(new CommDTO(A,"format error!")));
            log.warn("服务端发送了错误的消息格式！");
            ctx.close();
            commDTO.setType(D);
        }
        return commDTO;
    }




}
