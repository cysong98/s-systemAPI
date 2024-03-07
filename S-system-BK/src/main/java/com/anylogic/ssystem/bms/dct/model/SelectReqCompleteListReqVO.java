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
public class SelectReqCompleteListReqVO extends AnyXCommonVO implements Serializable {

    @ApiModelProperty("작성자ID")
    private String authorid;                       // 작성자ID

    @ApiModelProperty("유형")
    @Size(max=1)
    private String kind;                //  생샌문서or접수문서

    @ApiModelProperty("구분")
    @Size(max=5)
    private String acttype;                //  결재or1인결재

    @ApiModelProperty("문서번호")
    @Size(max=500)
    private String docno;                //  문서번호

    @ApiModelProperty("제목")
    @Size(max=500)
    private String docttl;                       // 제목

    @ApiModelProperty("상태")
    @Size(max=5)
    private String state;                   // 상태

    @ApiModelProperty("startDt")
    private String startDt;                      // 시작일

    @ApiModelProperty("종료일")
    private String endDt;                        // 종료일

    @ApiModelProperty("현재페이지")
    private int pageNum;                         //  현재페이지

    @ApiModelProperty("페이지당열갯수")
    private int pageSize;                      //  페이지당열갯수

}

