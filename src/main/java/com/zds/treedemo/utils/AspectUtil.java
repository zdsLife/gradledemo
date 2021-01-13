package com.zds.treedemo.utils;

import com.github.pagehelper.PageInfo;
import com.zds.treedemo.annotation.PageAnn;
import com.zds.treedemo.annotation.Result;
import com.zds.treedemo.domain.QueryData;
import com.zds.treedemo.domain.vo.PageQueryParam;
import com.zds.treedemo.enums.LogLevelEnum;
import com.zds.treedemo.enums.ResCodeEnum;
import com.zds.treedemo.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

@Slf4j
public class AspectUtil {

    public static Object dealResult(ProceedingJoinPoint proceedingJoinPoint, String userName) throws Throwable {
        Date enterTime = new Date();
        Object[] args = proceedingJoinPoint.getArgs();

        Signature signature = proceedingJoinPoint.getSignature();
        Method method = ((MethodSignature) signature).getMethod();

        boolean printLog = true;
        boolean printResultLog = true;
        // 对是否需要打印结果赋值
        if (null != method && method.isAnnotationPresent(PageAnn.class)) {
            PageAnn ann = method.getAnnotation(PageAnn.class);
            printLog = ann.printLog();
            printResultLog = ann.printReturnLog();
        } else if (null != method && method.isAnnotationPresent(Result.class)) {
            Result ann = method.getAnnotation(Result.class);
            printLog = ann.printLog();
            printResultLog = ann.printReturnLog();
        }

        printOriginParam(args, printLog);

        Exception exception = null;
        Object object = null;
        try {
            object = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            exception = e;
        }

        QueryData result = getResult(method, object, args);

        Date endTime = new Date();
        Long diff = endTime.getTime() - enterTime.getTime();
        if (!printLog) {
            return handleNonePrint(exception, result);
        }

        log.info("### 方法 {}\n\t请求时间: {}，完成时间： {}，处理用时：{}ms\n\t操作人 {}\n\t参数：{}\n\t",
                signature, enterTime, endTime, diff, userName, args);
        handleException(exception);
        printReturnInfo(printResultLog, result);
        return result;
    }

    private static void handleException(Exception exception) throws Exception {
        if (null == exception) {
            return;
        }
        if (exception instanceof SystemException && LogLevelEnum.INFO.equals(((SystemException) exception).getLogLevel())) {
            log.info("出现异常:", exception);
        } else {
            log.error("出现异常:", exception);
        }
        throw exception;
    }

    /**
     * 打印原始参数
     * @param args
     * @param printLog
     */
    private static void printOriginParam(Object[] args, boolean printLog) {
        if (printLog) {
            // 如果需要打印日志，打印
            log.info("参数：{}", args);
        }
    }

    /**
     * 打印返回信息
     * @param printResultLog
     * @param result
     */
    private static void printReturnInfo(boolean printResultLog, QueryData result) {
        if (printResultLog) {
            log.info("返回结果 {}", result);
        }
    }

    /**
     * 处理不打印日志的情况
     * @param exception
     * @param result
     * @return
     * @throws Exception
     */
    private static Object handleNonePrint(Exception exception, QueryData result) throws Exception {
        // 如果不需打印日志
        if (null != exception) {
            // 如果存在异常，直接抛出
            throw exception;
        }
        // 否则直接返回结果
        return result;
    }

    private static QueryData getResult(Method method, Object object, Object[] args) {
        if (null != method && method.isAnnotationPresent(PageAnn.class)) {
            PageQueryParam pageQueryParam = (PageQueryParam) args[0];
            return ControllerUtil.getQueryDataAuto((List) object, pageQueryParam.getPageIndex(), pageQueryParam.getPageSize());
        }
        int total = 0;
        if (object instanceof List) {
            PageInfo page = PageInfo.of((List) object);
            total = (int) page.getTotal();
        }
        return new QueryData(total, ResCodeEnum.SUCCESS.getCode(), object,ResCodeEnum.SUCCESS.getMessage());
    }

}
