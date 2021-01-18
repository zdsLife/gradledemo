package com.zds.treedemo.annotation;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * @author admin
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})//作用在参数和方法上
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Documented//表明这个注解应该被 javadoc工具记录
@ResponseBody//响应时转JSON格式
public @interface AppController {

    /**
     * 业务描述
     *
     * @return
     */
    String description() default "";

    /**
     * 是否打日志 默认打
     */
    boolean isLog() default true;
}
