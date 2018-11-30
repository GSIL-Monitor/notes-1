package com.whxx.emc.gateway.mq.forward.netty;

import com.whxx.emc.gateway.constant.GatewayConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 消息包装类
 *
 * @package: com.whxx.emc.tcp.wrapper
 * @ClassName: MessageWrapper.java
 * @author: 彭浩
 * @date: 2018/10/15 19:14
 * @version: 1.0
 */
public class MessageWrapper {

    /**
     * tcp报文包装，解决粘包 拆包
     *
     * @param msg    报文内容
     * @param endStr 结束符
     * @return io.netty.buffer.ByteBuf
     */
    public static ByteBuf tcpMsgWrapper(String msg, String endStr) {

        return Unpooled.copiedBuffer((msg + endStr).getBytes());
    }

    /**
     * tcp报文包装，解决粘包 拆包 默认结束符 $$
     *
     * @param msg 报文内容
     * @return io.netty.buffer.ByteBuf
     */
    public static ByteBuf tcpMsgWrapper(String msg) {

        return tcpMsgWrapper(msg, GatewayConstants.TCP_MSG_END_CHARACTER);
    }
}
