package com.zds.treedemo.domain;

import com.zds.treedemo.enums.ResCodeEnum;
import lombok.Data;

@Data
public class QueryData {

    private int total;

    private String code;

    private Object data;

    private String message;

    public QueryData(int total, Object data) {
        this.total = total;
        this.data = data;
    }

    public QueryData(int total, String code, Object data) {
        this.total = total;
        this.code = code;
        this.data = data;
    }

    public QueryData(int total, String code, Object data, String message) {
        this.total = total;
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public QueryData(String code, Object data) {
        this.code = code;
        this.data = data;
    }

    public static QueryData success() {
        return new QueryData(ResCodeEnum.SUCCESS.getCode(), ResCodeEnum.SUCCESS.getMessage());
    }

    public static QueryData success(Object data) {
        return new QueryData(ResCodeEnum.SUCCESS.getCode(), data);
    }

    public static QueryData success(int total, Object data) {
        return new QueryData(total, ResCodeEnum.SUCCESS.getCode(), data, ResCodeEnum.SUCCESS.getMessage());
    }

    public static QueryData error() {
        return error(ResCodeEnum.FAIL.getCode(), ResCodeEnum.FAIL.getMessage());
    }

    public static QueryData error(ResCodeEnum resCodeEnum) {
        return error(resCodeEnum.getCode(), resCodeEnum.getMessage());
    }

    public static QueryData error(String code, Object data) {
        QueryData queryData = new QueryData(code, data);
        queryData.setMessage(data.toString());
        return queryData;
    }

    public QueryData() {
    }


}
