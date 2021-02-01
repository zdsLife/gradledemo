package com.zds.treedemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/2/1 13:55
 */
@Data
@Table(name = "sys_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysLogDO {
    /**
     * 日志主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 日志事件名称 必选项 mysql 非空
     */
    private String name;
}
