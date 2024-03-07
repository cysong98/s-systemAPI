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

@ApiModel("insertComingTransApproveBIA")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertComingTransApproveBIAEleResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("요청 ID") private String apprreqid;                    // 요청 ID
    @ApiModelProperty("관리번호") private String mgmtid;                      // 관리번호
    @ApiModelProperty("이관일시") private String transdt;                     // 이관일시
    @ApiModelProperty("요청 이관연장일시") private String reqtransextdt;      // 요청 이관연장일시
    @ApiModelProperty("승인 이관연장일시") private String granttransextdt;    // 승인 이관연장일시
    @ApiModelProperty("승인여부") private String grantflag;                   // 승인여부
    @ApiModelProperty("요청시현물존재여부") private String reqrfidexist;      // 요청시현물존재여부
    @ApiModelProperty("최종현물존재여부") private String finalrfidexist;      // 최종현물존재여부
    @ApiModelProperty("현비밀등급") private String secstatus;                 // 현비밀등급
    @ApiModelProperty("이관여부") private String istransrule;                 // 이관여부
    @ApiModelProperty("") private String transreason;                         // 
}

