/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.model;

import com.anylogic.ssystem.common.model.AnyXCommonVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("insertRelayHis")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertRelayHisReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀ID") private String docid;                           // 비밀 ID
    @ApiModelProperty("수신처ID") private String receiverid;                 //
    @ApiModelProperty("문서순서") private int docorder;                 //
    @ApiModelProperty("부서ID") private String deptid;                 //
    @ApiModelProperty("부서이름") private String deptname;                 //
    @ApiModelProperty("연계상태") private String exstatus;                 //
    @ApiModelProperty("등록일") private String indt;                 //
    @ApiModelProperty("제목") private String title;                 //
    @ApiModelProperty("연계유형") private String extype;                 //
    @ApiModelProperty("재시도") private int retry;                 //
    @ApiModelProperty("오류정보메세지") private String errormsg;                 //
    @ApiModelProperty("첨부ID") private String fleid;                 //
    @ApiModelProperty("처리자이름") private String actername;                 //
    @ApiModelProperty("처리자부서이름") private String acterdeptname;                 //
    @ApiModelProperty("시스템유형") private String systemtype;                 //
    @ApiModelProperty("유통망종류") private String relaytype;                 //
    @ApiModelProperty("처리일") private String actdt;                 //
    @ApiModelProperty("등록자부서ID") private String indeptid;                 //

    @ApiModelProperty("") private String refdeptid;
    @ApiModelProperty("") private String refdeptname;
    @ApiModelProperty("") private String ackreqtype;
    @ApiModelProperty("") private String orgdocid;
    @ApiModelProperty("") private String resultxml;
    @ApiModelProperty("") private String apply;
    @ApiModelProperty("") private String telephone;
    @ApiModelProperty("") private String header;

    @ApiModelProperty("startDt")
    private String startDt;                      // startDt

    @ApiModelProperty("endDt")
    private String endDt;                        // endDt

    @ApiModelProperty("현재페이지")
    private int pageNum;                         //  현재페이지

    @ApiModelProperty("페이지당열갯수")
    private int pageSize;                      //  페이지당열갯수

    @ApiModelProperty("정렬유형")
    @Size(max=5)
    private String sortDirection;                //  정렬유형

    @ApiModelProperty("정렬항목")
    @Size(max=50)
    private String sortItem;                     //  정렬항목

    @ApiModelProperty("정렬테이블")
    @Size(max=50)
    private String sortIndicator;                //  정렬테이블

}

