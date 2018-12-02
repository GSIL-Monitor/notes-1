package com.emc.subscribe.client;

import com.emc.subscribe.handler.MessageHandler;
import io.netty.handler.logging.LogLevel;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SubscribeClientBuilder {

    /**最大接收长度*/
    private int maxFrameLength;
    /**消息结束符*/
    private String lineSeparator;
    /**编码方式*/
    private String charsetName;
    /**日志级别*/
    private LogLevel logLevel;
    /**服务端ip*/
    private String ip;
    /**服务端端口*/
    private int port;
    /**消息处理对象*/
    private MessageHandler messageHandler;
    /**订阅用户名*/
    private String username;
    /**订阅消息主题及密钥*/
    private Map<String, String> topics;
    /**长连接保持心跳时间 （大于10 秒）*/
    private int heartBeatTime;

    public SubscribeClient build(){
        checkParam();
        return SubscribeClient.instance(ip,port)
                .updateDefaultParam(maxFrameLength,lineSeparator,charsetName,logLevel,heartBeatTime)
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
    /**
     * 设置最大读取长度（默认：10kb）
     * @param maxFrameLength 最大读取长度
     * @return 当前对象
     */
    public SubscribeClientBuilder setMaxFrameLength(int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
        return this;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    /**
     * 设置消息结束分隔符（默认：$$）
     * @param lineSeparator 消息结束分隔符
     * @return 当前对象
     */
    public SubscribeClientBuilder setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
        return this;
    }

    public String getCharsetName() {
        return charsetName;
    }

    /**
     * 设置编码方式（默认：GBK）
     * @param charsetName 编码方式
     * @return 当前对象
     */
    public SubscribeClientBuilder setCharsetName(String charsetName) {
        this.charsetName = charsetName;
        return this;
    }

    public String getIp() {
        return ip;
    }

    /**
     * 设置服务端IP
     * @param ip 服务端IP
     * @return 当前对象
     */
    public SubscribeClientBuilder setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return port;
    }

    /**
     * 设置服务端端口
     * @param port 服务端端口
     * @return 当前对象
     */
    public SubscribeClientBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    /**
     *  设置消息处理对象
     * @param messageHandler 消息处理对象
     * @return 当前对象
     */
    public SubscribeClientBuilder setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        return this;
    }

    public String getUsername() {
        return username;
    }

    /**
     *  设置用户名
     * @param username 用户名
     * @return 当前对象
     */
    public SubscribeClientBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public Map<String, String> getTopics() {
        return topics;
    }

    /**
     * 设置订阅主题
     * @param topics 主题
     * @return 当前对象
     */
    public SubscribeClientBuilder setTopics(Map<String, String> topics) {
        this.topics.putAll(topics);
        return this;
    }

    public SubscribeClientBuilder setTopic(String appId,String appSecret){
        this.topics.put(appId,appSecret);
        return this;
    }

    /**
     * 设置日志级别 （默认：INFO）
     * @param logLevel 日志级别
     * @return 当前对象
     */
    public SubscribeClientBuilder setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public int getHeartBeatTime() {
        return heartBeatTime;
    }

    /**
     * 设置长连接心跳时间 大于10秒才生效（默认：60s）
     * @param heartBeatTime 心跳时间
     * @return 当前对象
     */
    public SubscribeClientBuilder setHeartBeatTime(int heartBeatTime) {
        this.heartBeatTime = heartBeatTime;
        return this;
    }
}
