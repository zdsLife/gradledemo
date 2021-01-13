package com.zds.treedemo.utils;

import com.zds.treedemo.domain.Role;
import com.zds.treedemo.domain.vo.RoleVO;
import com.zds.treedemo.service.RoleService;
import com.zds.treedemo.service.impl.RoleServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 单例角色树工具类
 * 第一次获取实例会从数据库中加载所有角色数据到内存中
 */
@Slf4j
public enum RoleTreeNodeUtil {
    /**
     * 单实例
     */
    INSTANCE;

    private Map<Integer, Role> cachedRoles;
    private RoleService tRoleService;
    /**
     * 读写锁，控制读写互斥
     */
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * 构造方法，构造时从数据库中获取所有的数据放在缓存中
     */
    RoleTreeNodeUtil() {
        cachedRoles = new HashMap<>();
        tRoleService = SpringFactoryUtil.getBean(RoleServiceImpl.class);
        List<Role> all = tRoleService.getAll();
        all.forEach(item -> cachedRoles.put(item.getId(), item));
    }

    public static RoleTreeNodeUtil getInstance() {
        return INSTANCE;
    }

    /**
     * 刷新map中的缓存数据
     */
    public void refresh() {
        readWriteLock.writeLock().lock();
        try {
            List<Role> all = tRoleService.getAll();
            cachedRoles.clear();
            all.forEach(item -> cachedRoles.put(item.getId(), item));
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public RoleVO getRoleTree(int cid) {
        readWriteLock.readLock().lock();
        try {
            return this.recursiveTree(cid);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * 从map缓存的数据中，递归生成树
     *
     * @param cid 指定的根节点id
     * @return 树
     */
    private RoleVO recursiveTree(int cid) {
        Role node = cachedRoles.get(cid);
        RoleVO roleTree = RoleVO.builder()
                .key(node.getId())
                .children(new ArrayList<>())
                .build();
        List<RoleVO> childTreeNodes = new ArrayList<>();
        for (Map.Entry<Integer, Role> item : cachedRoles.entrySet()) {
            if (item.getValue().getParentId().equals(cid)) {
                childTreeNodes.add(RoleVO.builder()
                        .key(item.getValue().getId())
                        .title(item.getValue().getRolename())
                        .build());
            }
        }
        //遍历子节点
        for (RoleVO child : childTreeNodes) {
            //递归
            RoleVO n = recursiveTree(child.getKey());
            roleTree.getChildren().add(n);
        }
        return roleTree;
    }

    /**
     * 递归从缓存中获取所有的下级角色，用于从库中获取所有拥有下级角色的用户名
     * @param role 可变长参数，若给定，且递归获取到的下级角色非空时，以次给定的角色名过滤
     */
    public List<Map<String, String>> getAllSubordinateUsername(String... role) {
        readWriteLock.readLock().lock();
        try {
            List<String> allSubordinate = new ArrayList<>();
            List<Integer> userRoleIds = new ArrayList<>();
            //递归获取所有子角色名称
            recursiveList(userRoleIds, allSubordinate);
            if (allSubordinate.isEmpty()) {
                return new ArrayList<>();
            }
            return null;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * 递归方法，如果当前集合能找到下级元素，则继续递归，否则则停止递归
     *
     * @param userRoleIds    当前用户包含的所有roleId + 所有的子roleId
     * @param allSubordinate 当前用户的所有子角色名
     */
    private void recursiveList(List<Integer> userRoleIds, List<String> allSubordinate) {
        boolean flag = false;
        for (Map.Entry<Integer, Role> item : cachedRoles.entrySet()) {
            if (!allSubordinate.contains(item.getValue().getRolename()) && userRoleIds.contains(item.getValue().getParentId())) {
                allSubordinate.add(item.getValue().getRolename());
                userRoleIds.add(item.getValue().getId());
                flag = true;
            }
        }
        if (flag) {
            recursiveList(userRoleIds, allSubordinate);
        }
    }
}
