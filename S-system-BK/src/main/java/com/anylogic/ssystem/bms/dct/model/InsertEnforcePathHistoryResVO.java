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

@ApiModel("insertEnforcePathHistory")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertEnforcePathHistoryResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("시행비밀 ID") private String enfdocid;                // 시행비밀 ID
    @ApiModelProperty("처리경로이력 ID") private String pathhisid;           // 처리경로이력 ID
    @ApiModelProperty("처리경로이력 일련번호") private BigDecimal pathhisseq;    // 처리경로이력 일련번호
    @ApiModelProperty("처리자 ID") private String acterid;                   // 처리자 ID
    @ApiModelProperty("처리자 이름") private String actername;               // 처리자 이름
    @ApiModelProperty("처리자 구분") private String actergubun;              // 처리자 구분
    @ApiModelProperty("부서 ID") private String deptid;                      // 부서 ID
    @ApiModelProperty("부서 이름") private String deptname;                  // 부서 이름
    @ApiModelProperty("부서 이름 상세") private String deptnamedesc;         // 부서 이름 상세
    @ApiModelProperty("직위직급 이름") private String dutyname;              // 직위직급 이름
    @ApiModelProperty("직위 ID") private String positionid;                  // 직위 ID
    @ApiModelProperty("직급 ID") private String gradeid;                     // 직급 ID
    @ApiModelProperty("등록일") private String indt;                         // 등록일
}

