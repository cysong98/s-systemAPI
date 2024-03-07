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
public class SelectdctRdocReqVO extends AnyXCommonVO implements Serializable {

    @ApiModelProperty("user ID")
    @Size(max=35)
    private String userid;                     // user ID

    @ApiModelProperty("담당자 ID")
    @Size(max=35)
    private String authorid;                     // 담당자 ID

    @ApiModelProperty("비밀 ID")
    @Size(max=35)
    private String docid;                        // 비밀 ID

    @ApiModelProperty("유형")
    @Size(max=1)
    private String kind;                //  생샌문서or접수문서

    @ApiModelProperty("종류")
    @Size(max=1)
    private String regirecvtype;                     // 종류

    @ApiModelProperty("비밀등급")
    @Size(max=1)
    private String seclevel;                     // 비밀등급

    @ApiModelProperty("문서번호")
    @Size(max=500)
    private String docno;                //  문서번호

    @ApiModelProperty("관리번호")
    @Size(max=30)
    private String mgmtno;                       // 관리번호

    @ApiModelProperty("비밀명")
    @Size(max=500)
    private String secttl;                       // 비밀명

    @ApiModelProperty("사본번호")
    @Size(max=10)
    private String copyno;                       // 사본번호

    @ApiModelProperty("담당자")
    @Size(max=100)
    private String authorname;                   // 담당자

    @ApiModelProperty("담당부서")
    @Size(max=256)
    private String deptname;                   // 담당부서

    @ApiModelProperty("startDt")
    private String startDt;                      // startDt

    @ApiModelProperty("endDt")
    private String endDt;                        // endDt

    @ApiModelProperty("현재페이지")
    private int pageNum;                         //  현재페이지

    @ApiModelProperty("페이지당열갯수")
    private int pageSize;                      //  페이지당열갯수

}

