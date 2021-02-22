package com.zds.treedemo.controller;

import com.zds.treedemo.annotation.MonitorRequest;
import com.zds.treedemo.annotation.Result;
import com.zds.treedemo.utils.TagCategoryTreeNodeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/11 18:14
 */
@RestController
@RequestMapping("rest")
public class RestTarget {

    @GetMapping(value = "test")
    @Result
//    public String test() {
//        return "test";
//    }
    public Object test() {
        return "test";
    }

    @Result
    @MonitorRequest
    @GetMapping("tree/{id}")
    public Object getTreeNode(@PathVariable Integer id) {
        return TagCategoryTreeNodeUtil.getInstance().getTagCategoryDOTree(id);
    }

    @Result
    @MonitorRequest
    @GetMapping("para")
    public Object getTreeNode(String a) {
        System.out.println("----------------->>>>>>"+a);
        return "有意思";
    }


}
