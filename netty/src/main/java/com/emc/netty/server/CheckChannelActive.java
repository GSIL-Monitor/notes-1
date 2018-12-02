package com.emc.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * tcp连接检测
 *
 * @package: com.whxx.emc.tcp.handler
 * @ClassName: CheckChannelActive.java
 * @author: 彭浩
 * @date: 2018/10/17 8:45
 * @version: 1.0
 */
@Component
@Slf4j
public class CheckChannelActive {

    @Autowired
    private ChannelManagement channelManagement;

    final Timer timer = new HashedWheelTimer();

    /**
     * 设备连接后30s验证是否已注册，未注册主动断开连接
     *
     * @param ctx
     */
    public void checkChannelActive(ChannelHandlerContext ctx) {
        timer.newTimeout(timeout -> {
            if(!channelManagement.checkCtx(ctx)) {
                ctx.writeAndFlush(MessageWrapper.tcpMsgWrapper("register timeout!"));
                if(ctx.channel().isActive()) {
                    ctx.channel().close();
                }
            }
        }, 30, TimeUnit.SECONDS);

    }


}
