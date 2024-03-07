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


//@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class SelectReferenceListReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀ID")
    @Size(max=35)
    private String docid = "";                        // 비밀ID

    @ApiModelProperty("비밀명")
    private String docttl = "";                        // 비밀명

    @ApiModelProperty("담당자 이름")
    private String authorname = "";                        // 담당자 이름

    @ApiModelProperty("담당자 부서이름")
    private String authordeptname = "";                        // 담당자 부서이름

    @ApiModelProperty("로그인유저ID")
    private String userid = "";                        // 로그인유저ID

}

