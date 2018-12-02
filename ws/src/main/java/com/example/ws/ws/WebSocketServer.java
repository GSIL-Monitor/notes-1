package com.example.ws.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;


@ServerEndpoint("/ws/{deviceId}")
@Component
@Slf4j
public class WebSocketServer {
    //页面测试客户端列表
    private static final Map<String,Session> testClients=new HashMap<>();

    //用户标识
    private static final String CLIENT_ID = "deviceId";

    /**
     * 收到连接后验证设备id，并存储session
     * @param session
     * @throws Exception
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("deviceId") String deviceId) {
        System.out.println("新开启了一个webSocket连接" + session.getId());
        log.info("received connection...");
        //String deviceId=getDeviceId(session);
        if(!StringUtils.isEmpty(deviceId)){
            testClients.put(deviceId,session);
            sendMsgToClient(session,"成功建立socket连接");
            log.info("deviceId:" + deviceId);
            log.info("session:" + session);
        }
    }
    /**
     * 收到客户端发送的消息
     * @param session
     * @param message
     */
    @OnMessage
    public String onMessage(String message, Session session) {
        log.info("read message...");
        for(String device:testClients.keySet()){
            if(testClients.get(device).equals(session)){
                log.info("message from {}：{}",device,message);
                return "hello,"+device;
            }
        }
        return null;
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("webSocket连接关闭：sessionId:"+session.getId() + "关闭原因是："+reason.getReasonPhrase() + "code:"+reason.getCloseCode());
        testClients.remove(getDeviceId(session));
    }


    @OnError
    public void onError(Session session,Throwable t) {
        t.printStackTrace();
        testClients.remove(getDeviceId(session));
    }





    /**
     * 发送消息给制定客户端
     * @param deviceId
     * @param message
     * @return
     */
    public boolean sendMsgToClient(String deviceId,String message){
        log.info("message to {}:{}",deviceId,message);
        Session session=testClients.get(deviceId);
        if(session==null || !session.isOpen()){
            log.info("client not found : {}",deviceId);
            return false;
        }
        return sendMsgToClient(session,message);
    }

    /**
     * 发送消息给制定客户端
     * @param session
     * @param message
     * @return
     */
    public boolean sendMsgToClient(Session session,String message){
        try{
            session.getBasicRemote().sendText(message);
            log.info("send success!");
            return true;
        }catch (Exception e){
            log.error("send error : {}",e.getMessage());
            return false;
        }
    }


    private String getDeviceId(Session session){
        for(String deviceId:testClients.keySet()){
            if(testClients.get(deviceId).equals(session)){
                return deviceId;
            }
        }
        return null;
    }
}