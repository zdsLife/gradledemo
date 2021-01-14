package com.zds.treedemo.service.impl;

import com.zds.treedemo.domain.TagCategoryDO;
import com.zds.treedemo.mapper.TagCategoryMapper;
import com.zds.treedemo.service.TagCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/14 10:28
 */
@Service
public class TagCategoryServiceImpl implements TagCategoryService {
    @Autowired
    private TagCategoryMapper tagCategoryMapper;

    @Override
    public List<TagCategoryDO> selectAll() {
        return tagCategoryMapper.selectAll();
    }
}
