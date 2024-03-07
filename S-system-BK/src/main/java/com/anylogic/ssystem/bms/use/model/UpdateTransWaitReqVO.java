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
public class UpdateTransWaitReqVO extends AnyXCommonVO implements Serializable {

    // bms_dct_rdoc table
    @ApiModelProperty("") private String docid; // 비밀문서ID
    @ApiModelProperty("") private String readrangetype; // 열람범위유형
    @ApiModelProperty("") private String open; // 공개여부
    @ApiModelProperty("") private String readlimittype; // 열람제한유형
    @ApiModelProperty("") private String transflag; // 이관flag

}

