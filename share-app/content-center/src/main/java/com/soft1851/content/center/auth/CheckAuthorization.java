package com.soft1851.content.center.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xunmi
 * @ClassName CheckAuthorization
 * @Description 鉴权注解
 * @Date 2020/10/13
 * @Version 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuthorization {
    String value();
}
