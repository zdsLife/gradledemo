package com.zds.treedemo.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/27 17:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysLogActiveTrendVO {
    /**
     * 传入的时间间隔
     */
    private String totalDate;

    /**
     * 用户访问量
     */
    private Integer totalUv;

    /**
     * 用户查询量
     */
    private Integer totalQv;

    /**
     * 输入的时间段内的 每天 或者月 uv qv
     */
    List<LogActiveTrendDO> logTrendList;
}
