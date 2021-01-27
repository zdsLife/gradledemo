package com.zds.treedemo.service.impl;

import cn.hutool.json.JSONObject;
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
    @Scheduled(cron = "0/20 * * * * ?")
    public List<TagCategoryDO> selectAll() {
        log.info("====================测试spring的定时器的执行是否正常====================");
        return tagCategoryMapper.selectAll();
    }

    public static void main(String[] args) {
        String jsonDemo = "{\"event\":\"$ViewPage\",\"event_name\":\"web浏览页面\",\"properties\":{\"page_name\":\"首页\",\"page_id\":\"index\",\"$screen_height\":680,\"$screen_width\":1280,\"$user_agent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36\"},\"actionType\":1,\"project\":\"usertag\",\"time\":1611731997222,\"distinct_id\":\"zhudongsheng@kungeek.com\"}";

        JSONObject object = new JSONObject(jsonDemo);
        String event = object.getStr("event");
        System.out.println("event============>" + event);
        System.out.println("==================================================");

        String properties = object.getStr("properties");
        System.out.println("properties============>" + properties);
        JSONObject propertiesJson = new JSONObject(properties);

        String screen_width = propertiesJson.getStr("$screen_width");
        System.out.println("screen_width============>" + screen_width);

        String screen_height = propertiesJson.getStr("$screen_height");
        System.out.println("screen_height============>" + screen_height);

        String page_id = propertiesJson.getStr("page_id");
        System.out.println("page_id============>" + page_id);

        String page_name = propertiesJson.getStr("page_name");
        System.out.println("page_name============>" + page_name);




    }
}
