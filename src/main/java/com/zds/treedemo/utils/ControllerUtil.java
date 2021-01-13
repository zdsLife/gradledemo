package com.zds.treedemo.utils;

import com.github.pagehelper.PageInfo;
import com.zds.treedemo.domain.QueryData;
import com.zds.treedemo.enums.ResCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ControllerUtil<T> {

    public QueryData getQueryData(List<T> list) {
        PageInfo page = PageInfo.of(list);
        return new QueryData((int) page.getTotal(), ResCodeEnum.SUCCESS.getCode(), list);
    }

    public static QueryData getSuccess() {
        return new QueryData(ResCodeEnum.SUCCESS.getCode(), ResCodeEnum.SUCCESS.getMessage());
    }

    public static QueryData getOperationSuccess() {
        return new QueryData(ResCodeEnum.OPERATION_SUCCESS.getCode(), ResCodeEnum.OPERATION_SUCCESS.getMessage());
    }

    public static QueryData getSuccessObject(Object o) {
        return new QueryData(ResCodeEnum.SUCCESS.getCode(), o);
    }

    public static QueryData getQueryDataAuto(List list, int currentPage, int pageSize) {
        if (CollectionUtils.isEmpty(list)) {
            return new QueryData(0, ResCodeEnum.SUCCESS.getCode(), new ArrayList());
        }
        log.info("currentPage {}, pageSize {}", currentPage, pageSize);
        int start = (currentPage - 1) * pageSize;
        start = start > list.size() || start < 0 ? 0 : start;
        int end = Math.min((start + pageSize), list.size());
        log.info("start {}, end {}", start, end);
        return new QueryData(list.size(), ResCodeEnum.SUCCESS.getCode(), list.subList(start, end));
    }

}
