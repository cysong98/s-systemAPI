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

import com.anylogic.ssystem.common.model.AnyXCommonVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class UpdatePrtCntReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("결재요청 ID") private String apprreqid;                // 결재요청 ID
    @ApiModelProperty("관리ID") private String mgmtid;                     // 관리ID
    @ApiModelProperty("구분") private String gubun;                     // 구분
    @ApiModelProperty("파일순서") private int fleorder;                     // 파일순서
}

