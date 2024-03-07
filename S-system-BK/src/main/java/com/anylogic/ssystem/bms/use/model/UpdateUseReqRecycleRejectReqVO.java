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
import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class UpdateUseReqRecycleRejectReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_apprreq_reuse
    @ApiModelProperty("요청 ID") private String apprreqid;                      // 요청 ID
    @ApiModelProperty("관리번호") private String mgmtid;                        // 관리번호
    @ApiModelProperty("요청 재사용 시작일시") private String reqreusebegindt;   // 요청 재사용 시작일시
    @ApiModelProperty("요청 재사용 만료일시") private String reqreuseenddt;     // 요청 재사용 만료일시
    @ApiModelProperty("승인 재사용 시작일시") private String grantreusebegindt; // 승인 재사용 시작일시
    @ApiModelProperty("승인 재사용 만료일시") private String grantreuseenddt;   // 승인 재사용 만료일시
    @ApiModelProperty("승인여부") private String grantflag;                     // 승인여부

}

