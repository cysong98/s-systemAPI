/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.model;

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
import com.anylogic.ssystem.common.model.AnyXCommonVO;

@ApiModel("insertPrtFile")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertPrtFileResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("요청 ID") private String apprreqid;           // 요청 ID
    @ApiModelProperty("관리번호") private String mgmtid;             // 관리번호
    @ApiModelProperty("첨부순서") private double fleorder;           // 첨부순서
    @ApiModelProperty("구분") private String gubun;                  // 구분
    @ApiModelProperty("첨부 ID") private String fleid;               // 첨부 ID
    @ApiModelProperty("파일 고유 이름") private String sfilename;    // 파일 고유 이름
    @ApiModelProperty("첨부명") private String flettl;               // 첨부명
    @ApiModelProperty("첨부크기") private double flesize;            // 첨부크기
    @ApiModelProperty("승인여부") private String grantflag;          // 승인여부
}

