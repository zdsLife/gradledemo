package com.zds.treedemo.domain.vo;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class RoleVO extends PageQueryParam {

    private String title;

    private Integer key;

    private List<RoleVO> children;

    private Boolean unPagination;

    private String rolename;

    private String rid;

    private String pids;
}
