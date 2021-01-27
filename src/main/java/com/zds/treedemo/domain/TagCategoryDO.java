package com.zds.treedemo.domain;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * @author ：zhudongsheng
 * @description：TODO 标签分类的实体（这里是规定 0 级为标签体系 目前只有四级标签分类）
 * @date ：2020/11/18 11:35
 */

@Data
@Table(name = "tag_category")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagCategoryDO {
    /**
     * 标签分类表主键id
     */
    private Integer id;

    /**
     * 标签分类编码
     */
    private String code;

    /**
     * 标签分类名
     */
    private String name;

    /**
     * 上级标签分类的主键id 维持层级关系
     */
    private Integer parentId;

    /**
     * 标签分类所属的分级id
     */
    private Integer level;

    /**
     * 备注
     */
    private String note;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    /**
     * 标签分类所属的标签体系id
     */
    private Integer sysId;

    /**
     * 当前标签分类的子分类集合
     */
    List<TagCategoryDO> children;

    public static void main(String[] args) {
        HttpUtil.createServer(8888)
                // 返回JSON数据测试
                .addAction("/restTest", (request, response) ->
                        response.write("{\"id\": 1, \"msg\": \"OK\"}", ContentType.JSON.toString())
                ).start();
    }
}
