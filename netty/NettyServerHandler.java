package com.whxx.emc.gateway.mq.forward.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whxx.emc.gateway.constant.GatewayConstants;
import com.whxx.emc.gateway.dto.RegisterTopicReqDTO;
import com.whxx.emc.gateway.dto.RegisterTopicResDTO;
import com.whxx.emc.gateway.fegin.ManagerApiService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

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
    private ManagerApiService managerApiService;

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
            case "A":
                registerTopic((RegisterTopicReqDTO)commDTO.getData(),ctx);
                break;
            case "B":
                break;
            default:
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("发生异常---{}", ctx);
        log.error(cause.getMessage(), cause);
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
            topicResDTO=managerApiService.checkUserTopic(registerData);
        }catch (Exception e){
            ctx.writeAndFlush(JSON.toJSONString(new CommDTO().setType("A").setData(GatewayConstants.SERVER_ERROR_MSG))+GatewayConstants.TCP_MSG_END_CHARACTER);
            ctx.close();
        }
        if(topicResDTO==null){
            ctx.writeAndFlush(JSON.toJSONString(new CommDTO().setType("A").setData(GatewayConstants.USER_ERROR_MSG))+GatewayConstants.TCP_MSG_END_CHARACTER);
            ctx.close();
        }else{
            Set<String> topicsOk=topicResDTO.getTopicOk();
            if(!CollectionUtils.isEmpty(topicsOk)){
                channelManagement.addCtx(topicResDTO.getUserId(),topicsOk,ctx);
            }
            ctx.writeAndFlush(JSON.toJSONString(new CommDTO().setType("A").setData(topicResDTO))+GatewayConstants.TCP_MSG_END_CHARACTER);
        }
    }

    private Object getTestMsg(int n){
        Map<String,Object> msgMap=new HashMap<>();
        msgMap.put("productId","app001");
        List<Map> msgList=new ArrayList<>();
        for (int i=0;i<n;i++){
            Map<String,String> map1=new HashMap<>();
            map1.put("deviceId","deivce00"+(i/5));
            map1.put("payload","this is message from device"+i);
            msgList.add(map1);
        }
        msgMap.put("list",msgList);
        return msgMap;
    }
    private CommDTO parseFromMsg(Object msg,ChannelHandlerContext ctx){
        CommDTO commDTO=new CommDTO();
        try{
            commDTO = JSON.parseObject(msg.toString(),CommDTO.class);
            if(!StringUtils.isEmpty(commDTO.getData())){
                JSONObject jsonObject =JSON.parseObject(commDTO.getData().toString());
                RegisterTopicReqDTO registerTopic=new RegisterTopicReqDTO();
                registerTopic.setUsername(jsonObject.getString("username"));
                JSONArray jsonArray=(JSONArray)jsonObject.get("topics");
                if(jsonArray!=null && jsonArray.size()>0){
                    List<Map<String,String>> topicList=new ArrayList<>();
                    for (Object aJsonArray : jsonArray) {
                        JSONObject jo = JSON.parseObject(aJsonArray.toString());
                        Map<String, String> topic = new HashMap<>();
                        topic.put("appId", jo.getString("appId"));
                        topic.put("appSecret", jo.getString("appSecret"));
                        topicList.add(topic);
                    }
                    registerTopic.setTopics(topicList);
                }
                commDTO.setData(registerTopic);
            }
        }catch (Exception e){
            //消息格式错误
            ctx.writeAndFlush(JSON.toJSONString(new CommDTO().setType("A").setData(GatewayConstants.MSG_ERROR_MSG_01))+GatewayConstants.TCP_MSG_END_CHARACTER);
            ctx.close();
            commDTO.setType("D");
        }
        return commDTO;
    }




}
