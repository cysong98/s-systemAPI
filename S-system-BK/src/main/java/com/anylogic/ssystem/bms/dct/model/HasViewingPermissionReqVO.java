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

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class HasViewingPermissionReqVO extends AnyXCommonVO implements Serializable {

    @ApiModelProperty("유저 ID")
    @Size(max=35)
    private String userid;                        // 유저 ID

    @ApiModelProperty("비밀 ID")
    @Size(max=35)
    private String[] docids;                        // 비밀 ID

    @ApiModelProperty("비밀 ID 개수")
    private int docidscnt;                        // 비밀 ID 개수


}

