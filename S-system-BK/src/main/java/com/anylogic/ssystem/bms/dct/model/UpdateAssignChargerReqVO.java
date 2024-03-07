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
public class UpdateAssignChargerReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_enf_path
    @ApiModelProperty("시행비밀 ID") private String enfdocid;         // 시행비밀 ID
    @ApiModelProperty("처리일련번호") private BigDecimal actseq;// 처리일련번호
    @ApiModelProperty("처리자 ID") private String acterid;            // 처리자 ID
    @ApiModelProperty("처리자 이름") private String actername;        // 처리자 이름
    @ApiModelProperty("처리자 구분") private String actergubun;       // 처리자 구분
    @ApiModelProperty("부서 ID") private String deptid;               // 부서 ID
    @ApiModelProperty("부서 이름") private String deptname;           // 부서 이름
    @ApiModelProperty("부서 이름 상세") private String deptnamedesc;  // 부서 이름 상세
    @ApiModelProperty("직위직급 이름") private String dutyname;       // 직위직급 이름
    @ApiModelProperty("직위 ID") private String positionid;           // 직위 ID
    @ApiModelProperty("직급 ID") private String gradeid;              // 직급 ID
    @ApiModelProperty("등록일") private String indt;                  // 등록일
    @ApiModelProperty("등록일자 ID") private String inid;             // 등록일자 ID
    @ApiModelProperty("처리일") private String actdt;                 // 처리일
    @ApiModelProperty("열람일시") private String readdt;              // 열람일시
    @ApiModelProperty("중요여부") private String starred;             // 중요여부
    @ApiModelProperty("접수일") private String acceptdt;              // 접수일

}

