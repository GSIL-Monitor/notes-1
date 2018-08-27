package com.test.effective.pack;

/**
 * Author   NieYinjun
 * Date     2018/8/27 20:31
 * TAG
 */
public class PrePay extends AbstractPay<String,String> {
    public PrePay(String url) {
        super(url);
    }

    @Override
    public String doPay(String e) {
        return null;
    }
}
