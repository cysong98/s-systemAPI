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
public class SelectSendCurrentListReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀명")
    @Size(max=500)
    private String docttl;                       // 비밀명

    @ApiModelProperty("담당자 이름")
    @Size(max=100)
    private String authorname;                   // 담당자 이름

    @ApiModelProperty("수신부서이름")
    private String recvdeptname;                 // 수신부서이름

    @ApiModelProperty("startDt")
    private String startDt;                      // startDt

    @ApiModelProperty("종료일")
    @Size(max=14)
    private String endDt;                        // 종료일

    @ApiModelProperty("현재페이지")
    private int pageNum;                         //  현재페이지

    @ApiModelProperty("페이지당열갯수")
    private int pageSize;                      //  페이지당열갯수

    @ApiModelProperty("정렬유형")
    @Size(max=5)
    private String sortDirection;                //  정렬유형

    @ApiModelProperty("정렬항목")
    @Size(max=50)
    private String sortItem;                     //  정렬항목 

    @ApiModelProperty("정렬테이블")
    @Size(max=50)
    private String sortIndicator;                //  정렬테이블

    @ApiModelProperty("유저아이디")
    @Size(max=35)
    private String userid;                //  유저아이디

    @ApiModelProperty("")
    @Size(max=50)
    private String deptname;

    @ApiModelProperty("수신부서") private String orgname; //수신부서
}

