package com.zds.treedemo.utils;

import com.zds.treedemo.domain.TagCategoryDO;
import com.zds.treedemo.service.TagCategoryService;
import com.zds.treedemo.service.impl.TagCategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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

    // key 为标签分类的主键 值为标签分类的对象
    private Map<Integer, TagCategoryDO> cachedTagCategoryDOs;

    @Autowired
    private TagCategoryService tagCategoryService;

    private Set<Integer> tagCategoryIds = new HashSet();
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
        all.stream().forEach(item -> {
//            System.out.println("----->" + item);
        });
        // 获取所有的标签分类 以键值对保存
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
        // 从所有标签分类中获取 指定键的对象
        TagCategoryDO node = cachedTagCategoryDOs.get(cid);
        tagCategoryIds.add(cid);
        TagCategoryDO tagCategoryTree = TagCategoryDO.builder()
                .id(node.getId())
                .name(node.getName())
                .level(node.getLevel())
                .children(new ArrayList<>())
                .build();
        List<TagCategoryDO> childTreeNodes = new ArrayList<>();
        for (Map.Entry<Integer, TagCategoryDO> item : cachedTagCategoryDOs.entrySet()) {
            if (item.getValue().getParentId().equals(cid)) {
                // 获取传入的这个id 作为parentId 的子分类
                childTreeNodes.add(TagCategoryDO.builder()
                        .id(item.getValue().getId())
                        .name(item.getValue().getName())
                        .build());
            }
        }
        //遍历所有子分类
        for (TagCategoryDO child : childTreeNodes) {
            //递归 给子分类设置子标签分类
            TagCategoryDO n = recursiveTree(child.getId());
//            System.out.println("====<<<<<"+n);
            tagCategoryIds.add(n.getId());
            tagCategoryTree.getChildren().add(n);
        }
        return tagCategoryTree;
    }


    /**
     * 递归方法，如果当前集合能找到下级元素，则继续递归，否则则停止递归
     *
     * @param userTagCategoryDOIds 当前用户包含的所有tagCategoryId + 所有的子tagCategoryId
     * @param allSubordinate       当前用户的所有子角色名
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
