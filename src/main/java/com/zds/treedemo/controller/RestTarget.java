package com.zds.treedemo.controller;

import com.zds.treedemo.annotation.Result;
import com.zds.treedemo.utils.TagCategoryTreeNodeUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("target")
public class RestTarget {

    @GetMapping(value="test")
    public String test() {
        return "test";
    }

    @Result
    @GetMapping("tree/{id}")
    public Object getTreeNode(@PathVariable Integer id) {
        return TagCategoryTreeNodeUtil.getInstance().getTagCategoryDOTree(id);
    }
}
