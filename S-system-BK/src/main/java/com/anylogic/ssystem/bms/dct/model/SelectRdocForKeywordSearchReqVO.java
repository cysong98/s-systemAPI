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


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class SelectRdocForKeywordSearchReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀ID")
    @Size(max=35)
    private String loginuserid;                      // 비밀I D

    @ApiModelProperty("부서ID")
    @Size(max=35)
    private String deptid;                         // 부서ID

    @ApiModelProperty("요청 구분")
    @Size(max=3)
    private String userreqtype;                      // 요청 구분

    @ApiModelProperty("키워드")
    @Size(max=500)
    private String keyword;                        // 키워드

    @ApiModelProperty("현재페이지")
    private int pageNum;                         //  현재페이지

    @ApiModelProperty("페이지당열갯수")
    private int pageSize;                      //  페이지당열갯수
}

