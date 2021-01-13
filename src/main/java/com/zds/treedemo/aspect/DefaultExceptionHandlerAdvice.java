package com.zds.treedemo.aspect;

import com.zds.treedemo.domain.QueryData;
import com.zds.treedemo.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandlerAdvice {

    @ExceptionHandler(value = Exception.class)
    public QueryData handlerException(Exception e) {
        return ExceptionUtil.handlerException(e);
    }

}
