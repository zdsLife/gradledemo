package com.zds.treedemo.domain.vo;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/26 17:01
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhudongsheng
 * @Description: TODO 日志活跃趋势
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogActiveTrendDO {
    /**
     * 日期格式输出 按天yyyy-mm-dd   yyyy-mm
     */
    private String dayOrMonth;

    /**
     * 用户访问量
     */
    private Integer uv;

    /**
     * 用户点击量(m目前指的是标签筛选点击)
     */
    private Integer qv;
}
