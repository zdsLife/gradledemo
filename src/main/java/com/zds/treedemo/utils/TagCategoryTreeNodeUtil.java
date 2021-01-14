package com.zds.treedemo.utils;

import com.zds.treedemo.domain.TagCategoryDO;
import com.zds.treedemo.mapper.TagCategoryMapper;
import com.zds.treedemo.service.TagCategoryService;
import com.zds.treedemo.service.impl.TagCategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 单例角色树工具类
 * 第一次获取实例会从数据库中加载所有标签分类数据到内存中
 */
@Slf4j
public enum TagCategoryTreeNodeUtil {

    /**
     * 单实例
     */
    INSTANCE;

    private Map<Integer, TagCategoryDO> cachedTagCategoryDOs;

    @Autowired
//    private TagCategoryMapper tagCategoryMapper;
    private TagCategoryService tagCategoryService;
    /**
     * 读写锁，控制读写互斥
     */
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * 构造方法，构造时从数据库中获取所有的数据放在缓存中
     */
    TagCategoryTreeNodeUtil() {
        cachedTagCategoryDOs = new HashMap<>();
        tagCategoryService = SpringFactoryUtil.getBean(TagCategoryServiceImpl.class);
        List<TagCategoryDO> all = tagCategoryService.selectAll();
        all.forEach(item -> cachedTagCategoryDOs.put(item.getId(), item));
    }

    public static TagCategoryTreeNodeUtil getInstance() {
        return INSTANCE;
    }

    /**
     * 刷新map中的缓存数据
     */
    public void refresh() {
        readWriteLock.writeLock().lock();
        try {
            List<TagCategoryDO> all = tagCategoryService.selectAll();
            cachedTagCategoryDOs.clear();
            all.forEach(item -> cachedTagCategoryDOs.put(item.getId(), item));
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public TagCategoryDO getTagCategoryDOTree(int cid) {
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
    private TagCategoryDO recursiveTree(int cid) {
        TagCategoryDO node = cachedTagCategoryDOs.get(cid);
        TagCategoryDO tagCategoryTree = TagCategoryDO.builder()
                .id(node.getId())
                .children(new ArrayList<>())
                .build();
        List<TagCategoryDO> childTreeNodes = new ArrayList<>();
        for (Map.Entry<Integer, TagCategoryDO> item : cachedTagCategoryDOs.entrySet()) {
            if (item.getValue().getParentId().equals(cid)) {
                childTreeNodes.add(TagCategoryDO.builder()
                        .id(item.getValue().getId())
                        .name(item.getValue().getName())
                        .build());
            }
        }
        //遍历子节点
        for (TagCategoryDO child : childTreeNodes) {
            //递归
            TagCategoryDO n = recursiveTree(child.getId());
            tagCategoryTree.getChildren().add(n);
        }
        return tagCategoryTree;
    }

    /**
     * 递归从缓存中获取所有的下级角色，用于从库中获取所有拥有下级角色的用户名
     * @param role 可变长参数，若给定，且递归获取到的下级角色非空时，以次给定的角色名过滤
     */
    public List<Map<String, String>> getAllSubordinateUsername(String... role) {
        readWriteLock.readLock().lock();
        try {
            List<String> allSubordinate = new ArrayList<>();
            List<Integer> userTagCategoryDOIds = new ArrayList<>();
            //递归获取所有子角色名称
            recursiveList(userTagCategoryDOIds, allSubordinate);
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
     * @param userTagCategoryDOIds    当前用户包含的所有roleId + 所有的子roleId
     * @param allSubordinate 当前用户的所有子角色名
     */
    private void recursiveList(List<Integer> userTagCategoryDOIds, List<String> allSubordinate) {
        boolean flag = false;
        for (Map.Entry<Integer, TagCategoryDO> item : cachedTagCategoryDOs.entrySet()) {
            if (!allSubordinate.contains(item.getValue().getName()) && userTagCategoryDOIds.contains(item.getValue().getParentId())) {
                allSubordinate.add(item.getValue().getName());
                userTagCategoryDOIds.add(item.getValue().getId());
                flag = true;
            }
        }
        if (flag) {
            recursiveList(userTagCategoryDOIds, allSubordinate);
        }
    }
}
