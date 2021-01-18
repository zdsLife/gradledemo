package com.zds.treedemo.aspect;

import cn.hutool.json.JSON;
import com.zds.treedemo.annotation.AppController;
import com.zds.treedemo.domain.TbLogVisit;
import com.zds.treedemo.service.ActionService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/18 17:57
 */
public class AppControllerAspect {
    //注入Service用于把日志保存数据库，实际项目入库采用队列做异步
    @Resource
    private ActionService actionService;
    //日志工厂获取日志对象
    static Logger logger = LoggerFactory.getLogger(AppControllerAspect.class);

    /**
     * ThreadLocal多线程环境下，创建多个副本，各自执行，互不干扰
     */

    //startTime存放开始时间
    ThreadLocal<Map<String, Long>> startTime = new ThreadLocal<>();
    //count存放方法被调用的次数O 使用volatile利用它的三大特性：保证可见性（遵守JMM的可见性），不保证原子性，禁止指令重排
    volatile ThreadLocal<Map<String, Long>> count = new ThreadLocal<>();
    //timeConsuming存放方法总耗时
    ThreadLocal<Map<String, Long>> timeConsuming = new ThreadLocal<>();
    //fromType存放渠道
    ThreadLocal<Map<String, String>> fromType = new ThreadLocal<>();
    //tbLogVisit日志访问对象
    ThreadLocal<TbLogVisit> tbLogVisit = new ThreadLocal<>();

    //Controller层切点
    @Pointcut("@annotation(com.zds.treedemo.annotation.AppController)")
    public void controllerAspectse() {
    }

    //前置通知  用于拦截Controller层记录用户的操作
    @Before("controllerAspectse()")
    public void before(JoinPoint pjp) {
        //初始化
        TbLogVisit tbLogVisit = this.tbLogVisit.get();
        tbLogVisit = new TbLogVisit();
        Map<String, Long> countMap = this.count.get();
        countMap = new HashMap<>();
        this.count.set(countMap);
        Map<String, Long> timeConsumingMap = this.timeConsuming.get();
        timeConsumingMap = new HashMap<>();
        this.timeConsuming.set(timeConsumingMap);
        Map<String, String> fromTypeMap = this.fromType.get();
        fromTypeMap = new HashMap<>();
        this.fromType.set(fromTypeMap);
        Map<String, Long> map = new HashMap<>();
        map.put("startTime", System.currentTimeMillis());
        this.startTime.set(map);
        logger.info("==============前置通知开始:记录用户的操作==============");
        Date currentTime =new Date();
        logger.info("请求开始时间:" + currentTime);
        tbLogVisit.setVisitStartTime(new Date());
        String resultString = "";
        // 是否打日志 默认打
        boolean isLog = true;
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            AppController appController = signature.getMethod().getAnnotation(AppController.class);
            //是否开启日志打印
            isLog = appController.isLog();
            if (isLog) {
                //开始打印日志
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                HttpSession session = request.getSession();
                String api = pjp.getTarget().getClass().getName() + "." + pjp.getSignature().getName();
                logger.info("请求API：" + api);
                tbLogVisit.setVisitApi(api);

                String methodDescription = getControllerMethodDescription(pjp);
                logger.info("方法描述：" + methodDescription);
                tbLogVisit.setVisitDescription(methodDescription);

                String ipAddress = InetAddress.getLocalHost().toString().substring(InetAddress.getLocalHost().toString().lastIndexOf("/") + 1);
                logger.info("请求ip：" + ipAddress);
                tbLogVisit.setVisitIpAddress(ipAddress);

                String hostName = InetAddress.getLocalHost().getHostName();
                logger.info("机器名：" + hostName);
                tbLogVisit.setVisitHostName(hostName);

                Enumeration<?> enu = request.getParameterNames();
                String params = "{";
                while (enu.hasMoreElements()) {
                    String paraName = (String) enu.nextElement();
                    params += "\"" + paraName + "\":\"" + request.getParameter(paraName) + "\",";
                }
                String methodParams = params + "}";
                String substring = methodParams.substring(0, methodParams.length() - 2);
                substring = substring + "}";
                logger.info("方法参数：" + substring);
                tbLogVisit.setVisitParams(substring);

                StringBuffer url = request.getRequestURL();
                logger.info("URL：" + url);
                tbLogVisit.setVisitUrl(String.valueOf(url));
            }
        } catch (Exception e) {
            StackTraceElement stackTraceElement2 = e.getStackTrace()[2];
            String reason = "异常：【" +
                    "类名：" + stackTraceElement2.getClassName() + ";" +
                    "文件：" + stackTraceElement2.getFileName() + ";" + "行：" +
                    stackTraceElement2.getLineNumber() + ";" + "方法："
                    + stackTraceElement2.getMethodName() + "】";
            //记录本地异常日志
            logger.error("==============前置通知异常:记录访问异常信息==============");
            String message = e.getMessage() + "|" + reason;
            logger.error("异常信息：", message);
            tbLogVisit.setVisitThrowingErro(message);
            tbLogVisit.setVisitResult("请求发生异常，异常信息：" + message);
        } finally {
            this.tbLogVisit.set(tbLogVisit);
        }
    }

    @Around("controllerAspectse()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        String result = JSON.class.cast(pjp.proceed()).toString();
        logger.info("请求结果：" + result);
        TbLogVisit tbLogVisit = this.tbLogVisit.get();
        tbLogVisit.setVisitResult(result);
        this.tbLogVisit.set(tbLogVisit);
        return "success";
    }


    /**
     * 对Controller下面的方法执行后进行切入，统计方法执行的次数和耗时情况
     * 注意，这里的执行方法统计的数据不止包含Controller下面的方法，也包括环绕切入的所有方法的统计信息
     *
     * @param jp
     */

    @AfterReturning("controllerAspectse()")
    public void afterMehhod(JoinPoint jp) {
        logger.info("==============方法执行完成==============");
        TbLogVisit tbLogVisit = this.tbLogVisit.get();
        try {
            //获取方法名
            String methodName = jp.getSignature().getName();
            //开始统计数量与耗时
            if (count.get().get(methodName) == null) {
                //第一次赋值为0
                count.get().put(methodName, 0L);
            }
            //使用原子整型进行增值
            AtomicLong atomicInteger = new AtomicLong(count.get().get(methodName));
            //加一 这里暂时不解决ABA问题，仅保证原子性 解决了volatile不保证原子性的问题 getAndIncrement()先返回再加1,incrementAndGet()先加1再返回
            long increment = atomicInteger.incrementAndGet();
            //然后增加新值
            count.get().replace(methodName, increment);
            Long end = System.currentTimeMillis();
            Long total = end - startTime.get().get("startTime");
            logger.info("执行总耗时为：" + total);
            if (timeConsuming.get().containsKey(methodName)) {
                timeConsuming.get().replace(methodName, total);
            } else {
                timeConsuming.get().put(methodName, (total));
            }
            tbLogVisit = this.tbLogVisit.get();
            tbLogVisit.setVisitTimeConsuming(String.valueOf(total));
            Date endTime = new Date();
            logger.info("请求结束时间:" + endTime);
            tbLogVisit.setVisitEndTime(new Date());
            /**
             * 从原来的map中将最后的连接点方法给移除了，替换成最终的，避免连接点方法多次进行叠加计算，
             * 由于原来的map受ThreadLocal的保护，这里不支持remove，因此，需要单开一个map进行数据交接
             */
            //重新new一个map
            Map<String, Long> map = new HashMap<>();
            for (Map.Entry<String, Long> entry : timeConsuming.get().entrySet()) {
                if (entry.getKey().equals(methodName)) {
                    map.put(methodName, total);
                } else {
                    map.put(entry.getKey(), entry.getValue());
                }
            }
            for (Map.Entry<String, Long> entry : count.get().entrySet()) {
                for (Map.Entry<String, Long> entry2 : map.entrySet()) {
                    if (entry.getKey().equals(entry2.getKey())) {
                        Long num = entry.getValue();
                        logger.info("调用次数：" + num);
                        tbLogVisit.setVisitNum(num);
                    }
                }
            }
            //这里的渠道暂时写死
            Map<String, String> stringMap = fromType.get();
            Map<String, String> fromMap;
            if (stringMap != null) {
                fromMap = stringMap;
            } else {
                fromMap = new HashMap<>();
                fromMap.put(methodName, "个人开发电商平台");
            }
        } catch (Exception e) {
            StackTraceElement stackTraceElement2 = e.getStackTrace()[2];
            String reason = "异常：【" +
                    "类名：" + stackTraceElement2.getClassName() + ";" +
                    "文件：" + stackTraceElement2.getFileName() + ";" + "行：" +
                    stackTraceElement2.getLineNumber() + ";" + "方法："
                    + stackTraceElement2.getMethodName() + "】";
            //记录本地异常日志
            logger.error("==============通知异常:记录访问异常信息==============");
            String message = e.getMessage() + "|" + reason;
            logger.error("异常信息：", message);
            tbLogVisit.setVisitThrowingErro(message);
            tbLogVisit.setVisitResult("请求发生异常！！！");
        } finally {
            this.tbLogVisit.set(tbLogVisit);
            //添加日志信息入库
            actionService.insertLogVisit(this.tbLogVisit.get());
        }
    }


    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(AppController.class).description();
                    break;
                }
            }
        }
        return description;
    }

}
