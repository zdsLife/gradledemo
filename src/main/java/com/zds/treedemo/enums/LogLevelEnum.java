package com.zds.treedemo.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LogLevelEnum {

    /**
     * 日志级别信息
     */
    INFO("INFO", "level info"),

    /**
     * 错误级别的日志信息
     */
    ERROR("ERROR", "level error"),
    ;

    @Getter
    private String code;

    @Getter
    private String desc;
}
