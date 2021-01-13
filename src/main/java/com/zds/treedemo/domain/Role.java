package com.zds.treedemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Integer id;

    private String rolename;

    private Integer parentId;

    @Builder.Default
    private List<Role> children = new ArrayList<>();

    private List<Integer> pids;
}
