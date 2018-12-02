package com.emc.subscribe.dto;

import java.util.List;
import java.util.Map;

public class MessageDTO {
    private int total;
    private String productId;
    private List<Map> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<Map> getList() {
        return list;
    }

    public void setList(List<Map> list) {
        this.list = list;
    }
}
