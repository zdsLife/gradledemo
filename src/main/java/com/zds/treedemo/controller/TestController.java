package com.zds.treedemo.controller;

import com.zds.treedemo.annotation.MonitorRequest;
import com.zds.treedemo.annotation.Result;
import com.zds.treedemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/18 17:18
 */

@RestController
public class TestController {

    @Autowired
    private UserService userService;

    /**
     * 添加了自定义注解@MonitorRequest
     *
     * @return
     */
    @MonitorRequest
    @RequestMapping(value = "/test")
    @Result
    public Object test() {
        return "method be added @MonitorRequest";
    }

    /**
     * 没有添加自定义注解
     *
     * @return
     */
    @RequestMapping(value = "test2")
    @Result
    public Object test2() {
        return "method not be added @MonitorRequest";
    }


    /**
     * 没有添加自定义注解
     *
     * @return
     */
    @RequestMapping(value = "transaction")
    @Result
    public Object testTransactional() {
        userService.addUser();
        return "testTransactional";
    }


}
