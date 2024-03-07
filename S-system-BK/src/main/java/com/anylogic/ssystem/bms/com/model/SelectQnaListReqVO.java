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
import java.math.BigDecimal;

import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class SelectQnaListReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("아이디")
    @Size(max=35)
    private String refid;                        // 아이디

    @ApiModelProperty("제목")
    @Size(max=35)
    private String title;                        // 제목

    @ApiModelProperty("등록자명")
    private String inname;                      // 등록자명

    @ApiModelProperty("그룹")
    private BigDecimal thread;                       // 그룹

    @ApiModelProperty("순서")
    private BigDecimal pos;                         // 순서

    @ApiModelProperty("깊이")
    private BigDecimal depth;                       // 깊이

    @ApiModelProperty("startDt")
    private String startDt;                      // startDt

    @ApiModelProperty("endDt")
    private String endDt;                        // endDt

    @ApiModelProperty("현재페이지")
    private int pageNum;                         //  현재페이지

    @ApiModelProperty("페이지당열갯수")
    private int pageSize;                      //  페이지당열갯수

    @ApiModelProperty("정렬유형")
    @Size(max=5)
    private String sortDirection;                //  정렬유형
}

