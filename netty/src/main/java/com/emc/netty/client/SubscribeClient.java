package com.emc.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Map;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SubscribeClient {
    private static final Logger log=LoggerFactory.getLogger(SubscribeClient.class);
    //默认接收最大数据大小，10KB
    private final int DEFAULT_MAX_FRAME_LENGTH = 102400 ;
    //一条消息结束，默认结束符号
    private final String DEFAULT_LINE_SEPARATOR = "$$" ;
    //默认编码方式
    private final String DEFAULT_CHARSET_NAME = "GBK" ;
    //默认日志级别
    private final LogLevel DEFAULT_LOG_LEVEL=LogLevel.INFO;

    private int maxFrameLength;
    private String lineSeparator;
    private String charsetName;
    private LogLevel logLevel;
    private String ip;
    private int port;
    private MessageHandler messageHandler;
    private String username;
    private Map<String, String> topics;
    private Bootstrap b;
    private ChannelFuture cf ;
    private static volatile SubscribeClient singleton;


    private SubscribeClient(String ip,int port){
        this.ip=ip;
        this.port=port;
    }

    static SubscribeClient instance(String ip,int port){
        if(singleton==null){
            synchronized(SubscribeClient.class) {
                if (singleton == null) {
                    singleton = new SubscribeClient(ip,port);
                }
            }
        }
        return singleton;
    }

    //对默认值修改
    SubscribeClient updateDefaultParam(int maxFrameLength,String lineSeparator,String charsetName,LogLevel logLevel){
        if(maxFrameLength>0){
            this.maxFrameLength=maxFrameLength;
        }else {
            this.maxFrameLength=DEFAULT_MAX_FRAME_LENGTH;
        }
        if(lineSeparator==null || lineSeparator.equals("")){
            this.lineSeparator=DEFAULT_LINE_SEPARATOR;
        }else {
            this.lineSeparator=lineSeparator;
        }
        if(charsetName==null || charsetName.equals("")){
            this.charsetName=DEFAULT_CHARSET_NAME;
        }else {
            this.charsetName=charsetName;
        }
        if(logLevel!=null){
            this.logLevel=logLevel;
        }else {
            this.logLevel=DEFAULT_LOG_LEVEL;
        }
        return this;
    }

    //订阅主题
    SubscribeClient subscribe(String username,Map<String,String> topics){
        this.username=username;
        this.topics=topics;
        return this;
    }

    //添加消息处理对象
    SubscribeClient setMessageHandler(MessageHandler messageHandler){
        this.messageHandler=messageHandler;
        return this;
    }

    SubscribeClient initialize(){
        EventLoopGroup group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(logLevel))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc){
                        //sc.pipeline().addLast(new ReadTimeoutHandler(62));
                        sc.pipeline().addLast(new DelimiterBasedFrameDecoder(maxFrameLength,Unpooled.copiedBuffer(lineSeparator.getBytes())));
                        sc.pipeline().addLast(new StringEncoder(Charset.forName(charsetName)));
                        sc.pipeline().addLast(new StringDecoder(Charset.forName(charsetName)));
                        sc.pipeline().addLast(new ClientHandler(username,topics,messageHandler));  //客户端业务处理类
                    }
                });
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public void connect(){
        try {
            this.cf = b.connect(ip, port).sync();
            log.info("远程服务器已经连接, 可以进行数据交换..");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void reconnect(){
        //如果管道没有被开启或者被关闭了，那么重连
        if(this.cf == null){
            this.connect();
        }
        if(!this.cf.channel().isActive()){
            this.connect();
        }
    }

    public int getMaxFrameLength() {
        return maxFrameLength;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public String getUsername() {
        return username;
    }

    public ChannelFuture getCf() {
        return cf;
    }
}
