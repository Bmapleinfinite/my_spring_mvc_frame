package com.springway.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableGlobalException {
    // 启动全局处理异常的注解
    // 若使用，需要告知要用于处理异常的类
    String value();
}
