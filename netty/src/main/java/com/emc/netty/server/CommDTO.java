package com.emc.netty.server;

import com.emc.netty.client.CommType;
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
    private CommType type;
    private Object data;

    public CommDTO() { }
    public CommDTO(CommType type){
        this.type=type;
    }
    public CommDTO(CommType type, Object data) {
        this.type = type;
        this.data = data;
    }
}
