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
import java.lang.String;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import java.io.Serializable;
import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class InsertAttributeReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_attr
    @ApiModelProperty("비밀 ID") private String docid;                    // 비밀 ID
    @ApiModelProperty("비밀구분") private String secgubun;                // 비밀구분
    @ApiModelProperty("비밀등급") private String seclevel;                // 비밀등급
    @ApiModelProperty("원본보호기간") private String orgnprotdt;          // 원본보호기간
    @ApiModelProperty("원본차기비밀등급") private String orgnnextlevel;   // 원본차기비밀등급
    @ApiModelProperty("원본재분류") private String orgnreclass;           // 원본재분류
    @ApiModelProperty("원본보호조건") private String orgnprotcond;        // 원본보호조건
    @ApiModelProperty("사본보호기간") private String copyprotdt;          // 사본보호기간
    @ApiModelProperty("사본차기비밀등급") private String copynextlevel;   // 사본차기비밀등급
    @ApiModelProperty("사본재분류") private String copyreclass;           // 사본재분류
    @ApiModelProperty("사본보호조건") private String copyprotcond;        // 사본보호조건
    @ApiModelProperty("보존기간") private String prsrvterm;               // 보존기간
    @ApiModelProperty("문서번호") private String docno;                   // 문서번호
    @ApiModelProperty("관리번호") private String mgmtno;                  // 관리번호
    @ApiModelProperty("사본번호") private String copyno;                  // 사본번호
    @ApiModelProperty("생산부서코드") private String createdeptid;        // 생산부서코드
    @ApiModelProperty("생산부서명") private String createdeptname;        // 생산부서명
    @ApiModelProperty("쪽수") private String docpagenum;                  // 쪽수
    @ApiModelProperty("특별관리여부") private String specialyn;           // 특별관리여부
    @ApiModelProperty("참고철 바인더 여부") private String binderyn;      // 참고철 바인더 여부
    @ApiModelProperty("가제목 사용여부") private String secretdocyn;      // 가제목 사용여부
    @ApiModelProperty("전시여부") private String waryn;                   // 전시여부
    @ApiModelProperty("신규") private String isnewrule;                   // 신규
    @ApiModelProperty("이관여부") private String istransrule;             // 이관여부
    @ApiModelProperty("") private String orgncreateyear;                  // 
    @ApiModelProperty("") private String wargubun;                        // 

}

