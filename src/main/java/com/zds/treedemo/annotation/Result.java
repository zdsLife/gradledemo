package com.zds.treedemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于aop包装返回结果便于前端处理
 *  返回结果类型为{@link com.zds.treedemo.domain.QueryData}
 *  处理aop类为{@link com.zds.treedemo.aspect.ApprovalResultDealAspect}
 * @author yulinfu
 * @date 2018/9/14
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Result {

    /**
     * 是否需要打印日志
     * @return
     */
    boolean printLog() default true;

    /**
     * 是否需要打印返回值日志
     *  默认不打印
     * @return
     */
    boolean printReturnLog() default false;

}
