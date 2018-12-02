package com.emc.netty.dto;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class RegisterTopicResDTO {
    private String userId;
    private Set<String> topicOk;
    private Map<String,String> topicError;
}
