package com.test.effective.pack;

/**
 * Author   NieYinjun
 * Date     2018/8/27 20:25
 * TAG
 */
public abstract class AbstractPay<T,E> implements Pay<T,E>{
    private String url;
    AbstractPay(String url){this.url=url;};
    public void setUrl(String url){
        this.url=url;
    }

    public String getUrl() {
        return url;
    }
}
