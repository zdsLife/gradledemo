package com.zds.treedemo.utils;



import com.zds.treedemo.domain.QueryData;
import com.zds.treedemo.enums.ResCodeEnum;
import com.zds.treedemo.exception.SystemException;
import com.zds.treedemo.exception.TreeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.elasticsearch.ElasticsearchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

@Slf4j
public class ExceptionUtil {


    public static QueryData handlerException(Exception e) {
        log.info("出现异常：{}", e.getMessage(), e);
        QueryData res;
        if (e instanceof UndeclaredThrowableException) {
            // Aspect中抛出的Exception会被转为UndeclaredThrowableException
            if (((UndeclaredThrowableException) e).getUndeclaredThrowable() instanceof TreeException) {
                res = handleException((TreeException) ((UndeclaredThrowableException) e).getUndeclaredThrowable());
            } else {
                res = handleException(e);
            }
        } else {
            res = handleException(e);
        }
        return res;
    }

    /**
     * 处理异常
     *
     * @param e
     * @return
     */
    private static QueryData handleException(Exception e) {
        QueryData res = new QueryData();
        if (e instanceof TreeException || e instanceof SystemException) {
            SystemException exception = (SystemException) e;
            res.setCode(exception.getCode());
            res.setMessage(exception.getMessage());
        } else if (e instanceof ElasticsearchException) {
            res.setCode(ResCodeEnum.ERROR_10004.getCode());
            res.setMessage(ResCodeEnum.ERROR_10004.getMessage());
        } else if (e instanceof MethodArgumentNotValidException || e instanceof BindException) {
            res.setCode(ResCodeEnum.ERROR_10006.getCode());
            res.setData(getValidResult(e));
            res.setMessage(ResCodeEnum.ERROR_10006.getMessage());
        } else if (e instanceof AuthorizationException) {
            res.setCode(ResCodeEnum.ERROR_10017.getCode());
            res.setMessage(ResCodeEnum.ERROR_10017.getMessage());
        } else if (e instanceof DuplicateKeyException) {
            res.setCode(ResCodeEnum.ERROR_10018.getCode());
            res.setMessage(ResCodeEnum.ERROR_10018.getMessage());
        } else if (e instanceof IncorrectCredentialsException) {
            res.setCode(ResCodeEnum.ERROR_10030.getCode());
            res.setMessage(ResCodeEnum.ERROR_10030.getMessage());
        } else if (e instanceof AuthenticationException) {
            res.setCode(ResCodeEnum.ERROR_10031.getCode());
            res.setMessage(ResCodeEnum.ERROR_10031.getMessage());
        } else {
            res.setCode(ResCodeEnum.ERROR_10005.getCode());
            res.setData("服务器开小差了，请稍后重试~");
            res.setMessage(ResCodeEnum.ERROR_10005.getMessage());
        }
        return res;
    }

    /**
     * 通过反射获取校验结果，并封装报错信息
     *
     * @param e
     * @return
     */
    private static StringBuffer getValidResult(Exception e) {
        StringBuffer msg = new StringBuffer();
        if (!(e instanceof MethodArgumentNotValidException) && !(e instanceof BindException)) {
            return msg;
        }
        try {
            Method getResultMethod = e.getClass().getMethod("getBindingResult");
            BindingResult validResult = (BindingResult) getResultMethod.invoke(e);
            validResult.getAllErrors().forEach(item -> {
                msg.append(item.getDefaultMessage() + "；");
            });
        } catch (Exception e1) {
            // 此处拦截异常并输出日志，若异常抛出，会使异常从参数校验失败变为反射异常
            log.error("DefaultExceptionHandlerAdvice.getValidResult获取信息校验参数信息失败-----e1:",e1);
        }
        return msg;
    }

}
