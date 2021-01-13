package com.zds.treedemo.utils;

import com.github.pagehelper.PageHelper;
import com.zds.treedemo.domain.vo.PageQueryParam;

public class PageUtil {

    public static final int START_PAGE = 1;

    public static void setPagePro(PageQueryParam queryParam) {
        int pageIndex = queryParam.getPageIndex() + 1;
        int pageSize = queryParam.getPageSize();
        PageHelper.startPage(pageIndex, pageSize);
    }
    //临时处理，后面要删掉避免冲突
    public static void setPagePro2(PageQueryParam queryParam) {
        int pageIndex = queryParam.getPageIndex();
        int pageSize = queryParam.getPageSize();
        PageHelper.startPage(pageIndex, pageSize);
    }

}
