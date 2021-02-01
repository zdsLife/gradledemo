package com.zds.treedemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/2/1 13:51
 */
@Data
@Table(name = "sys_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDO {

    /**
     * 用户主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户主名称 必选项
     */
    private String name;

}
