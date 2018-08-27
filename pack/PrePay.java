package com.example.demo.pack;

import org.springframework.http.MediaType;

/**
 * Author   NieYinjun
 * Date     2018/8/27 20:31
 * TAG
 */
public class PrePay extends AbstractPay<PrePayBack>{
    public PrePay(String url,MediaType mediaType,Class<PrePayBack> backClazz) {
        super(url,mediaType,backClazz);
    }

    @Override
    public PrePayBack doPay() {
        return super.doPay();
    }

    @Override
    public String toString() {
        return "PrePay{" +
                "url='" + url + '\'' +
                ", mediaType=" + mediaType +
                '}';
    }
}
