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

import com.anylogic.ssystem.common.model.AnyXCommonVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class UpdateDctAttrReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_rdoc
    @ApiModelProperty("비밀ID") private String docid;                            // 비밀I D

    @ApiModelProperty("관리번호") private String mgmtno;                            // 관리번호

    @ApiModelProperty("사본번호") private String copyno;
}

