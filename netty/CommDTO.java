package com.whxx.emc.gateway.mq.forward.netty;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author NieYinjun
 * @date 2018/11/29 16:28
 * @tag 此DTO用来作为tcp推送消息的DTO
 */
@Data
@Accessors(chain = true)
public class CommDTO {
    private String type;
    private Object data;
}
