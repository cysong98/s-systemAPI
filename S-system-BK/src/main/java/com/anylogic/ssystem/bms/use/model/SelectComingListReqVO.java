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
public class SelectComingListReqVO extends AnyXCommonVO implements Serializable {

    @ApiModelProperty("로그인한 유저ID")
    @Size(max=35)
    private String loginuserid;                    // 로그인한 유저ID

    @ApiModelProperty("로그인한 유저 부서ID")
    @Size(max=35)
    private String loginuserdeptid;                    // 로그인한 유저 부서ID

    @ApiModelProperty("권한ID")
    @Size(max=35)
    private String vgroupid;                      // 권한ID

    @ApiModelProperty("날짜 선택")
    @Size(max=1)
    private String choicedDay;                    // 날짜 선택

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

    @ApiModelProperty("비밀제목")
    @Size(max=500)
    private String secttl;                       // 비밀제목

    @ApiModelProperty("사본번호")
    @Size(max=10)
    private String copyno;                       // 사본번호

    @ApiModelProperty("현재페이지")
    private int pageNum;                         //  현재페이지

    @ApiModelProperty("페이지당열갯수")
    private int pageSize;                      //  페이지당열갯수
}

