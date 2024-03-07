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
import java.util.List;

import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class UpdateGwanBIAReqVO extends AnyXCommonVO implements Serializable {
    // com_org_gwaninfo
    @ApiModelProperty("조직 ID") private String orgid;                // 조직 ID
    @ApiModelProperty("일련번호") private BigDecimal seq;             // 일련번호
    @ApiModelProperty("관리구분") private String managegubun;         // 관리구분
    @ApiModelProperty("파일 ID") private String fileid;               // 파일 ID
    @ApiModelProperty("관인이름") private String gwaninname;          // 관인이름
    @ApiModelProperty("등록구분") private String ingubun;             // 등록구분
    @ApiModelProperty("입력일") private String regdt;                 // 입력일
    @ApiModelProperty("최초일") private String firstdt;               // 최초일
    @ApiModelProperty("입력사유") private String regreason;           // 입력사유
    @ApiModelProperty("관리조직 ID") private String manageorgid;      // 관리조직 ID
    @ApiModelProperty("입력비고") private String regetc;              // 입력비고
    @ApiModelProperty("폐기일") private String deletedt;              // 폐기일
    @ApiModelProperty("폐기사유") private String deletereason;        // 폐기사유
    @ApiModelProperty("폐기자 조직 ID") private String deleteorgid;   // 폐기자 조직 ID
    @ApiModelProperty("폐기자 직급") private String deletegrade;      // 폐기자 직급
    @ApiModelProperty("폐기자 ID") private String deleteuserid;       // 폐기자 ID
    @ApiModelProperty("폐기 비고") private String deleteetc;          // 폐기 비고
    @ApiModelProperty("등록자 ID") private String inid;               // 등록자 ID
    @ApiModelProperty("등록일") private String indt;                  // 등록일
    @ApiModelProperty("수정자 ID") private String upid;               // 수정자 ID
    @ApiModelProperty("수정일") private String updt;                  // 수정일
    @ApiModelProperty("비고") private String etc;
    @ApiModelProperty("관인크기가로") private String filewidth;
    @ApiModelProperty("관인크기세로") private String fileheight;
    @ApiModelProperty() private List<Object> fileInfo;
}

