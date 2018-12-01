package com.emc.subscribe.client;

import com.emc.subscribe.handler.MessageHandler;
import io.netty.handler.logging.LogLevel;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SubscribeClientBuilder {

    private int maxFrameLength;
    private String lineSeparator;
    private String charsetName;
    private LogLevel logLevel;
    private String ip;
    private int port;
    private MessageHandler messageHandler;
    private String username;
    private Map<String, String> topics;

    public SubscribeClient build(){
        checkParam();
        return SubscribeClient.instance(ip,port)
                .updateDefaultParam(maxFrameLength,lineSeparator,charsetName,logLevel)
                .subscribe(username,topics)
                .setMessageHandler(messageHandler)
                .initialize();
    }

    private void checkParam(){
        if(ip==null || ip.equals("")||port<1){
            throw new RuntimeException("ip,port error!");
        }else if(topics.size()==0){
            throw new RuntimeException("subscribed topic is empty!");
        }else if(username==null || username.equals("")){
            throw new RuntimeException("username is empty!");
        }

    }
    public SubscribeClientBuilder(){
        this.topics=new HashMap<String,String>();
    }

    public SubscribeClientBuilder(String ip, int port) {
        this();
        this.ip=ip;
        this.port=port;
    }

    public SubscribeClientBuilder(String ip,int port,MessageHandler messageHandler){
        this(ip,port);
        this.messageHandler=messageHandler;
    }

    public int getMaxFrameLength() {
        return maxFrameLength;
    }

    public SubscribeClientBuilder setMaxFrameLength(int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
        return this;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    public SubscribeClientBuilder setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
        return this;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public SubscribeClientBuilder setCharsetName(String charsetName) {
        this.charsetName = charsetName;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public SubscribeClientBuilder setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return port;
    }

    public SubscribeClientBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public SubscribeClientBuilder setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SubscribeClientBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public Map<String, String> getTopics() {
        return topics;
    }

    public SubscribeClientBuilder setTopics(Map<String, String> topics) {
        this.topics.putAll(topics);
        return this;
    }

    public SubscribeClientBuilder setTopic(String appId,String appSecret){
        this.topics.put(appId,appSecret);
        return this;
    }


    public SubscribeClientBuilder setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }
}
