package com.zds.treedemo.service.impl;

import com.zds.treedemo.domain.SysLogDO;
import com.zds.treedemo.domain.UserDO;
import com.zds.treedemo.mapper.SysLogMapper;
import com.zds.treedemo.mapper.UserMapper;
import com.zds.treedemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/2/1 13:59
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object addUser() {
        try {
            userMapper.insertUseGeneratedKeys(UserDO.builder().name("zds").build());
        } catch (Exception e) {
            System.out.println("=========>>addUser" + e);
        }
        return "success";
    }

//    @Transactional(rollbackFor = Exception.class)
    void addSysLog() throws Exception{
//        try {
            // 日志事件名称非空
            sysLogMapper.insertUseGeneratedKeys(SysLogDO.builder()
//                    .name("测试事务")
                    .build());
            throw new NullPointerException();
//        } catch (Exception e) {
//            System.out.println("=========>>addSysLog" + e);
//        }
    }
}
