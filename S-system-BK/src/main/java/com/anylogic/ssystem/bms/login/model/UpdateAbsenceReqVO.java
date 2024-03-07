/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.login.model;

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
public class UpdateAbsenceReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("유저id")
    @Size(max=35)
    private String userid;

    @ApiModelProperty("부재여부")
    @Size(max=1)
    private String absenceyn;

    @ApiModelProperty("대리결재자id")
    @Size(max=35)
    private String absenceid;

    @ApiModelProperty("복귀일")
    @Size(max=10)
    private String absencedt;
}

