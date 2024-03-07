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
public class InsertEnforceHistoryReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_enf_history
    @ApiModelProperty("시행비밀 ID") private String enfdocid;                   // 시행비밀 ID
    @ApiModelProperty("처리일련번호") private BigDecimal actseq;          // 처리일련번호
    @ApiModelProperty("처리자 ID") private String acterid;                      // 처리자 ID
    @ApiModelProperty("처리자 이름") private String actername;                  // 처리자 이름
    @ApiModelProperty("처리자 직위직급이름") private String acterdutyname;      // 처리자 직위직급이름
    @ApiModelProperty("처리자 직위 ID") private String acterpositionid;         // 처리자 직위 ID
    @ApiModelProperty("처리자 직급 ID") private String actergradeid;            // 처리자 직급 ID
    @ApiModelProperty("처리자 부서 ID") private String acterdeptid;             // 처리자 부서 ID
    @ApiModelProperty("처리자 부서 이름") private String acterdeptname;         // 처리자 부서 이름
    @ApiModelProperty("처리자 부서 이름 상세") private String acterdeptnamedesc;// 처리자 부서 이름 상세
    @ApiModelProperty("처리구분") private String actgubun;                      // 처리구분
    @ApiModelProperty("처리일") private String actdt;                           // 처리일
    @ApiModelProperty("처리의견") private String actopinion;                    // 처리의견
    @ApiModelProperty("기록관 ID") private String recid;                        // 기록관 ID
    @ApiModelProperty("수신자 이름") private String recname;                    // 수신자 이름
    @ApiModelProperty("수신자 직위직급") private String recdutyname;            // 수신자 직위직급
    @ApiModelProperty("수신자 직위 ID") private String recpositionid;           // 수신자 직위 ID
    @ApiModelProperty("수신자 직급 ID") private String recgradeid;              // 수신자 직급 ID
    @ApiModelProperty("수신자 부서 ID") private String recdeptid;               // 수신자 부서 ID
    @ApiModelProperty("수신자 부서 이름") private String recdeptname;           // 수신자 부서 이름
    @ApiModelProperty("수신자 부서 상세이름") private String recdeptnamedesc;   // 수신자 부서 상세이름
    @ApiModelProperty("처리경로이력 ID") private String pathhisid;              // 처리경로이력 ID
    @ApiModelProperty("전자서명 ID") private String electsignid;                // 전자서명 ID

}

