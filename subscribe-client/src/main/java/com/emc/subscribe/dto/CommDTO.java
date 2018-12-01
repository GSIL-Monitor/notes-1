package com.emc.subscribe.dto;

import com.emc.subscribe.enmu.CommType;

/**
 * @author NieYinjun
 * @date 2018/11/29 16:28
 * @tag 此DTO用来作为tcp推送消息的DTO
 */

public class CommDTO {
    private CommType type;
    private Object data;

    public CommType getType() {
        return type;
    }

    public void setType(CommType type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public CommDTO() { }
    public CommDTO(CommType type){
        this.type=type;
    }
    public CommDTO(CommType type, Object data) {
        this.type = type;
        this.data = data;
    }
}
