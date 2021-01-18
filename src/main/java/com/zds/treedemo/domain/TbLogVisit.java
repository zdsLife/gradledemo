package com.zds.treedemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zhudongsheng
 * @Description: TODO
 * @date 2021/1/18 18:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TbLogVisit {

    private String VisitTimeConsuming;

    private String visitUrl;

    private String visitApi;

    private String visitDescription;

    private String visitIpAddress;

    private String visitHostName;

    private String visitParams;

    private String visitThrowingErro;

    private String visitResult;

    private Long visitNum;

    private Date visitEndTime;

    private Date visitStartTime;

    private Date UpdateTime;

    private Date createTime;






}
