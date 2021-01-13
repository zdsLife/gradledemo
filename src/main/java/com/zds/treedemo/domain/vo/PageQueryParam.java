package com.zds.treedemo.domain.vo;

import lombok.Data;

/**
 * @author yulinfu
 * @date 2018/9/28
 */
@Data
public class PageQueryParam {

    public static final int DEFAULT_PAGE_SIZE = 1000;

    protected Integer pageIndex = 0;
    protected Integer pageSize = DEFAULT_PAGE_SIZE;
}
