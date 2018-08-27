package com.example.demo.pack;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;

/**
 * Author   NieYinjun
 * Date     2018/8/27 20:25
 * TAG
 */
public abstract class AbstractPay<T extends PayBack> implements Pay<PayBack>{
    protected String url;
    protected MediaType mediaType;
    private Class<T> t;
    AbstractPay(String url,MediaType mediaType,Class<T> backClazz){
        this.t=backClazz;
        this.url=url;
        this.mediaType=mediaType;
    }
    public void setUrl(String url){
        this.url=url;
    }

    public String getUrl() {
        return url;
    }


    public T doPay() {
       return  doPost(url);
    }

    private <T> T doPost(String url ){
        Object payBack=null;
        if ("prepay".equals(url)) {
            String jsonString="{\"code\":\"200\",\"msg\":\"获取成功\",\"data\":{\"name\":\"zhangsan\",\"age\":15}}";
            JSONObject result = JSONObject.parseObject(jsonString);
            JSONObject data = result.getJSONObject("data");
            payBack=result.getObject("data",t);
        }
        return (T)payBack;
    }


}
