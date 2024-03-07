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

@ApiModel("updateUseCompletePrintFinishBIA")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUseCompletePrintFinishBIAResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("요청 ID") private String apprreqid;                 // 요청 ID
    @ApiModelProperty("관리번호") private String mgmtid;                   // 관리번호
    @ApiModelProperty("요청인쇄부수") private double reqprtcnt;            // 요청인쇄부수
    @ApiModelProperty("요청파기예정일") private String reqdestructdt;      // 요청파기예정일
    @ApiModelProperty("승인인쇄부수") private double grantprtcnt;          // 승인인쇄부수
    @ApiModelProperty("승인파기예정일") private String grantdestructdt;    // 승인파기예정일
    @ApiModelProperty("인쇄부수") private double prtcnt;                   // 인쇄부수
    @ApiModelProperty("인쇄일시") private String prtdt;                    // 인쇄일시
    @ApiModelProperty("파기부수") private double destructcnt;              // 파기부수
    @ApiModelProperty("파기일시") private String destructdt;               // 파기일시
    @ApiModelProperty("승인여부") private String grantflag;                // 승인여부
}

