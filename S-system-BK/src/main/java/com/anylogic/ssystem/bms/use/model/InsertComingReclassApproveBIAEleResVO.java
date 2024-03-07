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

@ApiModel("insertComingReclassApproveBIA")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertComingReclassApproveBIAEleResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("요청 ID") private String apprreqid;                        // 요청 ID
    @ApiModelProperty("관리 ID") private String mgmtid;                           // 관리 ID
    @ApiModelProperty("재분류구분") private String reclassgubun;                  // 재분류구분
    @ApiModelProperty("요청비밀등급") private String reqseclevel;                 // 요청비밀등급
    @ApiModelProperty("요청예고문보호기간") private String reqnotiprotterm;       // 요청예고문보호기간
    @ApiModelProperty("요청예고문비밀등급") private String reqnotiprotlevel;      // 요청예고문비밀등급
    @ApiModelProperty("요청예고문처리") private String reqnotiact;                // 요청예고문처리
    @ApiModelProperty("요청예고문보존기간") private String reqprsrvterm;          // 요청예고문보존기간
    @ApiModelProperty("승인비밀등급") private String grantseclevel;               // 승인비밀등급
    @ApiModelProperty("승인예고문보호기간") private String grantnotiprotterm;     // 승인예고문보호기간
    @ApiModelProperty("승인예고문비밀등급") private String grantnotiprotlevel;    // 승인예고문비밀등급
    @ApiModelProperty("승인예고문처리") private String grantnotiact;              // 승인예고문처리
    @ApiModelProperty("승인예고문보존기간") private String grantnotiprsrvterm;    // 승인예고문보존기간
    @ApiModelProperty("승인여부") private String grantflag;                       // 승인여부
    @ApiModelProperty("새로운 관리번호") private String newmgmtno;                // 새로운 관리번호
    @ApiModelProperty("요청시현물 존재여부") private String reqrfidexist;         // 요청시현물 존재여부
    @ApiModelProperty("최종시현물 존재여부") private String finalrfidexist;       // 최종시현물 존재여부
    @ApiModelProperty("") private String secstatus;                               // 
}

