package com.api.wechat.entity.menu;

/**
 * Author   NieYinjun
 * Date     2018/8/16 16:39
 * 描述: 证书信任管理器（用于https请求）
 * 先前我们了解到，创建菜单需要调用二个接口，并且都是https请求，而非http。如果要封装一个通用的请求方法，该方法至少需要具备以下能力：
 * 1）支持HTTPS请求；
 * 2）支持GET、POST两种方式；
 * 3）支持参数提交，也支持无参数的情况；
 * 对于https请求，我们需要一个证书信任管理器，这个管理器类需要自己定义，但需要实现X509TrustManager接口，代码如下：
 * TAG
 */

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class MyX509TrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}