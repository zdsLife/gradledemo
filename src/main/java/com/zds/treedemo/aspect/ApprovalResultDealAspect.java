package com.zds.treedemo.aspect;

import com.zds.treedemo.utils.AspectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ApprovalResultDealAspect {

    @Pointcut("(execution(* com.zds.treedemo.controller.*.*.*(..)) || execution(* com.zds.treedemo.controller.*.*(..))) && " +
            "(@annotation(com.zds.treedemo.annotation.Result)) || @annotation(com.zds.treedemo.annotation.PageAnn))")
    public void resultDealService() {
    }

    /**
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("resultDealService()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return AspectUtil.dealResult(proceedingJoinPoint, "UserUtil.getUserName()");
    }


}
