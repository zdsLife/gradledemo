package com.zds.treedemo.exception;


import com.zds.treedemo.enums.LogLevelEnum;

/**
 * 报错
 * @author yulinfu
 */
public class TreeException extends SystemException {

    public TreeException(String message) {
        super(message);
    }

    public TreeException(String code, String message) {
        super(code, message);
    }

    public TreeException(String code, String message, LogLevelEnum logLevel) {
        super(code, message, logLevel);
    }
}


