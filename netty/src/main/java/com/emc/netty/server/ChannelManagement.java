package com.emc.netty.server;


import com.alibaba.fastjson.JSON;
import com.emc.netty.client.CommType;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author NieYinjun
 * @date 2018/11/29 13:54
 * @tag
 */
@Component
@Slf4j
public class ChannelManagement {

    private static ConcurrentHashMap<String,TopicsToChannel> map = new ConcurrentHashMap<>();
    /**
     * 检查连接通道是否已经注册
     * @param ctx 通道对象
     * @return  是否注册
     */
    public boolean checkCtx(ChannelHandlerContext ctx) {
        for (String key : map.keySet()) {
            if(map.get(key).getCtx().equals(ctx)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 发送消息到客户端
     * @param clientId  客户端唯一标识ID
     * @param topic 主题（产品ID）
     * @param msg   消息集合
     * @return 处理结果
     */
    public String sendMessage(String clientId,String topic,Object msg){
        TopicsToChannel topicsToChannel=map.get(clientId);
        if(topicsToChannel==null){
            //没有连接
            return "400";
        }else if(!topicsToChannel.getTopics().contains(topic)){
            //没有订阅
            return "300";
        }else {
            try{
                topicsToChannel.getCtx().writeAndFlush(JSON.toJSONString(new CommDTO().setType(CommType.C).setData(msg))+"$$");
                return "200";
            }catch (Exception e){
                return "500";
            }
        }
    }

    /**
     * 添加通道
     * @param clientId 客户端唯一标识ID,如果已经存在该用户通道，则关闭第一个
     * @param topics 订阅主题（订阅产品ID下的设备发送的消息），可以有多个
     * @param ctx 通道对象
     */
    public void addCtx(String clientId,Set<String> topics,ChannelHandlerContext ctx){
        if(map.containsKey(clientId)&& !map.get(clientId).getCtx().equals(ctx)){
            map.remove(clientId).getCtx().close();
        }
        map.put(clientId,new TopicsToChannel().setCtx(ctx).setTopics(topics));
    }

    /**
     * @param ctx 通道对象
     */
    public void removeCtx(ChannelHandlerContext ctx) {
        for (String key : map.keySet()) {
            if(map.get(key).getCtx().equals(ctx)) {
                map.remove(key);
                ctx.close();
                log.info("【{}】已经断开连接！", key);
                break;
            }
        }
    }

    @Data
    @Accessors(chain = true)
    private class TopicsToChannel{
        private Set<String> topics;
        private ChannelHandlerContext ctx;
    }
}
