package com.emc.subscribe.dto;

import java.util.Map;

public class RegisterTopicReqDTO {
    private String username;
    private Map<String,String> topics;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, String> getTopics() {
        return topics;
    }

    public void setTopics(Map<String, String> topics) {
        this.topics = topics;
    }
}
