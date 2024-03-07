/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;
import java.lang.String;

import java.math.BigDecimal;

import com.anylogic.ssystem.common.model.AnyXCommonVO;

@ApiModel("GetFormDetailSelectFile")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetFormDetailSelectFileResVO extends AnyXCommonVO implements Serializable {
    // bms_dct_form
    @ApiModelProperty("파일 ID") private String fileid;
    @ApiModelProperty("서식 ID") private String formid;
    @ApiModelProperty("서식 제목") private String formtitle;
    @ApiModelProperty("서식 이름") private String formname;
    @ApiModelProperty("서식 크기") private String formsize;
    @ApiModelProperty("서식 순서") private String formorder;
    @ApiModelProperty("등록자 ID") private String inid;
    @ApiModelProperty("등록일시") private String indt;
    @ApiModelProperty("수정자 ID") private String upid;
    @ApiModelProperty("수정일시") private String updt;
    @ApiModelProperty("부서 ID") private String deptid;
    @ApiModelProperty("부서 이름") private String deptname;
    @ApiModelProperty("부서 유형") private String depttype;
    @ApiModelProperty("서식코드1") private String formcode1;
    @ApiModelProperty("서식코드2") private String formcode2;
    @ApiModelProperty("서식코드3") private String formcode3;
    @ApiModelProperty("서식비고") private String formcomment;
    @ApiModelProperty("카테고리 ID") private String categoryid;
    @ApiModelProperty("파일 URL") private String fileurl;
    @ApiModelProperty("파일경로") private String filepath;
}

