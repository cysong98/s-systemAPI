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

import javax.validation.constraints.Size;
import java.io.Serializable;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class SelectPathInfoResVO extends AnyXCommonVO implements Serializable {

    @ApiModelProperty("결재자명")
    private String apprname;                    // 결재자명

    @ApiModelProperty("결재유형")
    private String apprtype;                    // 결재유형

    @ApiModelProperty("요청경로순번")
    private String apprreqseq;                    // 요청경로순번

}

