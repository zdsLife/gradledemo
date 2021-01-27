package com.zds.treedemo.service.impl;

import com.zds.treedemo.mapper.TagCategoryMapper;
import com.zds.treedemo.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/21 14:31
 */
@Service
@EnableScheduling
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private TagCategoryMapper tagCategoryMapper;
    @Override
//    @Scheduled(cron = "0 0/60 * * * ?")
    @Scheduled(cron = "0/10 * * * * ?")
    public String print() {
        System.out.println("=============================定时器执行成功=============================");
        tagCategoryMapper.selectAll().forEach(item->{
//            System.out.println("---->>>"+item);
        });
        return "ScheduleService test success";
    }
}
