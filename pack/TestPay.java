package com.example.demo;

import com.example.demo.pack.PrePay;
import com.example.demo.pack.PrePayBack;
import org.springframework.http.MediaType;

/**
 * Author   NieYinjun
 * Date     2018/8/27 20:34
 * TAG
 */
public class TestPay {
    public static void main(String[] args) {
        PrePay prePay=new PrePay("prepay",MediaType.APPLICATION_JSON_UTF8,PrePayBack.class);

        PrePayBack prePayBack = prePay.doPay();
        System.out.println(prePayBack);

    }
}
