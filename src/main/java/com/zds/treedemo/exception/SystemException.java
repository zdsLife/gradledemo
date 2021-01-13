package com.zds.treedemo.exception;

import com.zds.treedemo.enums.LogLevelEnum;
import com.zds.treedemo.enums.ResCodeEnum;
import lombok.Getter;

/**
 * @Description
 * @Author qinlei@kungeek.com
 * @Date 2019/7/10 11:57
 */
public class SystemException extends RuntimeException {

    @Getter
    private String code;

    @Getter
    private LogLevelEnum logLevel;

    public SystemException(String message) {
        super(message);
        this.code = ResCodeEnum.ERROR_10005.getCode();
        this.logLevel = LogLevelEnum.INFO;
    }

    public SystemException(String code, String message) {
        super(message);
        this.code = code;
        this.logLevel = LogLevelEnum.INFO;
    }

    public SystemException(String code, String message, LogLevelEnum logLevel) {
        super(message);
        this.code = code;
        this.logLevel = logLevel;
    }
}
