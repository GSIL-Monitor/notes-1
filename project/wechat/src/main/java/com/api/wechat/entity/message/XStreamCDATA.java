package com.api.wechat.entity.message;

/**
 * Author   NieYinjun
 * Date     2018/8/15 14:00
 * TAG  XML的CDATA验证类
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface XStreamCDATA {
}