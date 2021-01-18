package com.zds.treedemo.listener;

import com.zds.treedemo.service.ActionService;
import org.apache.logging.log4j.message.MapMessage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/18 18:23
 */
public class LogVisitListener {
    @Autowired
    ActionService actionService;

    public void consumeLogResult(MapMessage mapMessage){

    }
}
