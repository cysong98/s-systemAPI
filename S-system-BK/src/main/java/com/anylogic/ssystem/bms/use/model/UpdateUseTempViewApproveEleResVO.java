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

@ApiModel("updateUseTempViewApprove")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUseTempViewApproveEleResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("요청 ID") private String apprreqid;                     // 요청 ID
    @ApiModelProperty("비밀관리 ID") private String mgmtid;                    // 비밀관리 ID
    @ApiModelProperty("요청열람 시작일시") private String reqreadbegindt;      // 요청열람 시작일시
    @ApiModelProperty("요청열람 만료일시") private String reqreadenddt;        // 요청열람 만료일시
    @ApiModelProperty("승인열람 시작일시") private String grantreadbegindt;    // 승인열람 시작일시
    @ApiModelProperty("승인열람 만료일시") private String grantreadenddt;      // 승인열람 만료일시
    @ApiModelProperty("승인여부") private String grantflag;                    // 승인여부
    @ApiModelProperty("열람여부") private String isread;                       // 열람여부
    @ApiModelProperty("열람일시") private String readdt;                       // 열람일시
}

