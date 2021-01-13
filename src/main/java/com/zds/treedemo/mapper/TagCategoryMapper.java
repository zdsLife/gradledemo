package com.zds.treedemo.mapper;


import com.zds.treedemo.domain.TagCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Mapper
public interface TagCategoryMapper extends tk.mybatis.mapper.common.Mapper<TagCategoryDO>, MySqlMapper<TagCategoryDO> {
    /**
     * 由于标签体系 存在标签分类表 作为0级标签分类 所以这里用level = 0 来标识
     */
    Integer TAG_CATEGORY_LEVEL = 0;

    /**
     * 由于标签体系 存在标签分类表 作为0级标签分类 所以这里用 parent_id = 0 来标识
     */
    Integer TAG_CATEGORY_PARENT_ID = 0;

    /**
     * @Description: 给创建实体 显示标签体系下拉菜单那块传值给前端使用
     * @Param:
     * @return:
     */
    default List<TagCategoryDO> getTagSystems() {
        Example example = new Example(TagCategoryDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", TAG_CATEGORY_PARENT_ID);
        criteria.andEqualTo("level", TAG_CATEGORY_LEVEL);
        return this.selectByExample(example);
    }

    default List<TagCategoryDO> getTagCategoryList(Integer sysId) {
        Example example = new Example(TagCategoryDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sysId", sysId);
        return this.selectByExample(example);
    }

}
