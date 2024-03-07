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

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;
import java.lang.String;

import java.io.Serializable;
import java.math.BigDecimal;

import com.anylogic.ssystem.common.model.AnyXCommonVO;

@ApiModel("insertFile")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertFileResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀 ID") private String docid;                   // 비밀 ID
    @ApiModelProperty("첨부 ID") private String fleid;                   // 첨부 ID
    @ApiModelProperty("파일고유이름") private String sfilename;          // 파일고유이름
    @ApiModelProperty("구분") private String gubun;                      // 구분
    @ApiModelProperty("첨부명") private String flettl;                   // 첨부명
    @ApiModelProperty("첨부크기") private BigDecimal flesize;                // 첨부크기
    @ApiModelProperty("첨부순서") private BigDecimal fleorder;               // 첨부순서
    @ApiModelProperty("등록일") private String indt;                     // 등록일
    @ApiModelProperty("등록자 ID") private String inid;                  // 등록자 ID
    @ApiModelProperty("비밀등급표시여부") private String markflag;       // 비밀등급표시여부
    @ApiModelProperty("기존 문서의 구분") private String origubun;       // 기존 문서의 구분
    @ApiModelProperty("기존 문서의 비밀 ID") private String oridocid;    // 기존 문서의 비밀 ID
    @ApiModelProperty("비전자 첨부구분") private String isviewtype;      // 비전자 첨부구분
}

