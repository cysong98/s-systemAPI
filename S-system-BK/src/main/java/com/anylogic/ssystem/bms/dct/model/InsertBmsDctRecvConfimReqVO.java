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
public class InsertBmsDctRecvConfimReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_hist_card
    @ApiModelProperty("비밀 ID") private String docid;              // 비밀 ID
    @ApiModelProperty("이력 일시") private String histdt;           // 이력 일시
    @ApiModelProperty("이력 순번") private BigDecimal histseq;// 이력 순번
    @ApiModelProperty("결제요청 ID") private String apprreqid;      // 결제요청 ID
    @ApiModelProperty("이력구분") private String histgubun;         // 이력구분
    @ApiModelProperty("소속코드") private String deptid;            // 소속코드
    @ApiModelProperty("소속명") private String deptname;            // 소속명
    @ApiModelProperty("직위직급명") private String dutyname;        // 직위직급명
    @ApiModelProperty("사용자 ID") private String userid;           // 사용자 ID
    @ApiModelProperty("사용자명") private String username;          // 사용자명
    @ApiModelProperty("이력상태") private String histstatus;        // 이력상태
    @ApiModelProperty("기타") private String etc;                   // 기타
    @ApiModelProperty("전자서명 ID") private String electsignid;    // 전자서명 ID

}

