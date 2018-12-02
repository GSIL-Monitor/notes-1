package com.emc.netty.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class RegisterTopicReqDTO {
    private String username;
    private Map<String,String> topics;
}
