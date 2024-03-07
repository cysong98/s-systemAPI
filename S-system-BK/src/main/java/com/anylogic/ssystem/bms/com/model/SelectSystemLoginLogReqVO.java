/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import java.io.Serializable;
import java.lang.String;

import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class SelectSystemLoginLogReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("사용자아이디")
    @Size(max=35)
    private String userid;                       // 사용자아이디

    @ApiModelProperty("사용자명")
    @Size(max=100)
    private String username;                     // 사용자명

    @ApiModelProperty("부서명")
    @Size(max=256)
    private String deptname;                       // 부서명

    @ApiModelProperty("")
    @Size(max=14)
    private String startDt;                      // 

    @ApiModelProperty("")
    @Size(max=14)
    private String endDt;                        // 

    @ApiModelProperty("현재페이지")
    private int pageNum;                         //  현재페이지

    @ApiModelProperty("페이지당열갯수")
    private int pageSize;                      //  페이지당열갯수

    @ApiModelProperty("")
    private int limitSize;
}

