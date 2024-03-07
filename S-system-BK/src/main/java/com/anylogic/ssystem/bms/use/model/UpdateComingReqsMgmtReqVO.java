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


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class UpdateComingReqsMgmtReqVO extends AnyXCommonVO implements Serializable {

    // bms_dct_mgmt_regi table
    @ApiModelProperty("") private String mgmtid;
    @ApiModelProperty("") private String secstatus;
    @ApiModelProperty("") private String updtid;
    @ApiModelProperty("") private String updtname;
    @ApiModelProperty("") private String updtdt;

}

