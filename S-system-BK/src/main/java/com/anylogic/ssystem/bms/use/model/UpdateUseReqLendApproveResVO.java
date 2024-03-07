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

@ApiModel("updateUseReqLendApprove")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUseReqLendApproveResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("요청 ID") private String apprreqid;                // 요청 ID
    @ApiModelProperty("관리번호") private String mgmtid;                  // 관리번호
    @ApiModelProperty("반출구분") private String lendgubun;               // 반출구분
    @ApiModelProperty("요청반출일") private String reqlenddt;             // 요청반출일
    @ApiModelProperty("요청반입예정일") private String reqbackdt;         // 요청반입예정일
    @ApiModelProperty("승인반출일") private String grantlenddt;           // 승인반출일
    @ApiModelProperty("승인반입예정일") private String grantbackdt;       // 승인반입예정일
    @ApiModelProperty("반출일") private String lenddt;                    // 반출일
    @ApiModelProperty("반입일") private String backdt;                    // 반입일
    @ApiModelProperty("승인여부") private String grantflag;               // 승인여부
    @ApiModelProperty("반출형태") private String lendtype;                // 반출형태
    @ApiModelProperty("반출기관보안관리자") private String lendcharge;    // 반출기관보안관리자
    @ApiModelProperty("반출PC KEY") private String carrypcid;             // 반출PC KEY
    @ApiModelProperty("원본 반출 여부") private String isorgdoc;          // 원본 반출 여부
}

