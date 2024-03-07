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
public class SelectGwanListReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("부서아이디")
    @Size(max=35)
    private String orgid;                       // 부서아이디

    @ApiModelProperty("관인이름")
    @Size(max=100)
    private String gwaninname;                   // 관인이름

    @ApiModelProperty("비고")
    @Size(max=2000)
    private String etc;                          // 비고

    @ApiModelProperty("조직 이름")
    @Size(max=256)
    private String orgname;                      // 조직 이름

    @ApiModelProperty("관리구분")
    @Size(max=5)
    private String managegubun;                  // 관리구분

    @ApiModelProperty("startDt")
    private String startDt;                      // startDt

    @ApiModelProperty("endDt")
    private String endDt;                        // endDt

    @ApiModelProperty("현재페이지")
    private int pageNum;                         //  현재페이지

    @ApiModelProperty("페이지당열갯수")
    private int pageSize;                      //  페이지당열갯수
}