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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.lang.String;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import java.io.Serializable;
import java.math.BigDecimal;

import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class InsertGwanOrgDetailReqVO extends AnyXCommonVO implements Serializable {
    // com_org_detail_info
    @ApiModelProperty("조직 ID") private String orgid;              // 조직 ID
    @ApiModelProperty("일련번호") private BigDecimal seq;     // 일련번호
    @ApiModelProperty("관리구분") private String managegubun;       // 관리구분
    @ApiModelProperty("파일 ID") private String fileid;             // 파일 ID
    @ApiModelProperty("비고") private String etc;                   // 비고
    @ApiModelProperty("사용여부") private String isuse;             // 사용여부
    @ApiModelProperty("기본여부") private String isdefault;         // 기본여부
    @ApiModelProperty("등록자 ID") private String inid;             // 등록자 ID
    @ApiModelProperty("등록일") private String indt;                // 등록일
    @ApiModelProperty("수정자 ID") private String upid;             // 수정자 ID
    @ApiModelProperty("수정일") private String updt;                // 수정일

}

