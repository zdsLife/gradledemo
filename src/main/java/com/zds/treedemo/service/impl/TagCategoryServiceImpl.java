package com.zds.treedemo.service.impl;

import com.zds.treedemo.domain.TagCategoryDO;
import com.zds.treedemo.mapper.TagCategoryMapper;
import com.zds.treedemo.service.TagCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/14 10:28
 */
@Service
@Slf4j
public class TagCategoryServiceImpl implements TagCategoryService {
    @Autowired
    private TagCategoryMapper tagCategoryMapper;

    @Override
    @Scheduled(cron = "0/10 * * * * ?")
    public List<TagCategoryDO> selectAll() {
        log.info("====================测试spring的定时器的执行是否正常====================");
        return tagCategoryMapper.selectAll();
    }
}
