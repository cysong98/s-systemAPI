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
import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class InsertRecvRejectOpinionReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_appr_recv_opinion
    @ApiModelProperty("비밀 ID") private String docid;            // 비밀 ID
    @ApiModelProperty("수신처 ID") private String receiverid;     // 수신처 ID
    @ApiModelProperty("사유") private String opinion;             // 사유
    @ApiModelProperty("등록자 ID") private String inid;           // 등록자 ID
    @ApiModelProperty("등록일") private String indt;              // 등록일

}

