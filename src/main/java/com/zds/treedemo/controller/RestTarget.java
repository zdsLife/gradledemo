package com.zds.treedemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/11 18:14
 */
@RestController
@RequestMapping("target")
public class RestTarget {

    @GetMapping(value="test")
    public String test() {
        return "test";
    }
}
