package com.zds.treedemo.service.impl;

import com.zds.treedemo.domain.Role;
import com.zds.treedemo.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/12 11:49
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Override
    public List<Role> getAll() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.builder().id(1).rolename("一级").parentId(0).build());
        roles.add(Role.builder().id(2).rolename("二级1").parentId(1).build());
        roles.add(Role.builder().id(3).rolename("一级2").parentId(1).build());

        return roles;
    }
}
