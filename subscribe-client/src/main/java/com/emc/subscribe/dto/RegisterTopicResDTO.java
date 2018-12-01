package com.emc.subscribe.dto;


import java.util.Map;
import java.util.Set;

public class RegisterTopicResDTO {
    private String userId;
    private Set<String> topicOk;
    private Map<String,String> topicError;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getTopicOk() {
        return topicOk;
    }

    public void setTopicOk(Set<String> topicOk) {
        this.topicOk = topicOk;
    }

    public Map<String, String> getTopicError() {
        return topicError;
    }

    public void setTopicError(Map<String, String> topicError) {
        this.topicError = topicError;
    }
}
