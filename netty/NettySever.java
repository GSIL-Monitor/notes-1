package com.whxx.emc.gateway.mq.forward.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * @author yandixuan
 */
@Slf4j
@Component
public class NettySever {

    @Resource
    private NettyServerHandler nettyServerHandler;

    @Value("${forward.tcp.port:9765}")
    private int port;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @PostConstruct
    public void start() throws InterruptedException {

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.INFO))
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel sc) {
                    //默认心跳30s
                    sc.pipeline().addLast(new ReadTimeoutHandler(62));
                    sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("$$".getBytes())));
                    sc.pipeline().addLast(new StringDecoder(Charset.forName("gbk")));
                    sc.pipeline().addLast(new StringEncoder(Charset.forName("gbk")));
                    sc.pipeline().addLast(nettyServerHandler);
                }
            });
        ChannelFuture future = b.bind(port).sync();
        if (future.isSuccess()) {
            log.info("netty服务器在[{}]端口启动监听", port);
        }
    }

    @PreDestroy
    public void stop() {
        if (null != workerGroup) {
            workerGroup.shutdownGracefully();
        }
        if (null != bossGroup) {
            bossGroup.shutdownGracefully();
        }
    }
}
