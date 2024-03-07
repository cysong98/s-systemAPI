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

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;
import java.lang.String;

import java.io.Serializable;
import java.math.BigDecimal;

import com.anylogic.ssystem.common.model.AnyXCommonVO;

@ApiModel("insertPath")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertPathResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀 ID") private String docid;                          // 비밀 ID
    @ApiModelProperty("경로순서") private BigDecimal pathorder;                     // 경로순서
    @ApiModelProperty("상황") private String state;                             // 상황
    @ApiModelProperty("객체 ID") private String objid;                          // 객체 ID
    @ApiModelProperty("객체 유형") private String objtype;                      // 객체 유형
    @ApiModelProperty("객체 이름") private String objname;                      // 객체 이름
    @ApiModelProperty("부서 ID") private String deptid;                         // 부서 ID
    @ApiModelProperty("부서 이름") private String deptname;                     // 부서 이름
    @ApiModelProperty("직위직급 이름") private String dutyname;                 // 직위직급 이름
    @ApiModelProperty("직위직급 이름 상세") private String dutynamedesc;        // 직위직급 이름 상세
    @ApiModelProperty("직위 ID") private String positionid;                     // 직위 ID
    @ApiModelProperty("직급 ID") private String gradeid;                        // 직급 ID
    @ApiModelProperty("요청유형") private String asktype;                       // 요청유형
    @ApiModelProperty("처리유형") private String acttype;                       // 처리유형
    @ApiModelProperty("열람의견") private String readopinion;                   // 열람의견
    @ApiModelProperty("처리경로시간") private String actapprdt;                 // 처리경로시간
    @ApiModelProperty("메인버전") private BigDecimal mainver;                       // 메인버전
    @ApiModelProperty("서브버전") private BigDecimal subver;                        // 서브버전
    @ApiModelProperty("열람일시") private String readdt;                        // 열람일시
    @ApiModelProperty("부재사유") private String absreason;                     // 부재사유
    @ApiModelProperty("등록자 ID") private String inid;                         // 등록자 ID
    @ApiModelProperty("등록일") private String indt;                            // 등록일
    @ApiModelProperty("중요여부") private String starred;                       // 중요여부
    @ApiModelProperty("전결이름") private String appname;                       // 전결이름
    @ApiModelProperty("수정 FLAG") private String updateflag;                   // 수정 FLAG
    @ApiModelProperty("재검토경로순서") private BigDecimal repathorder;             // 재검토경로순서
    @ApiModelProperty("대리지정 FLAG") private String deputyflag;               // 대리지정 FLAG
    @ApiModelProperty("원 객체 ID") private String oriobjid;                    // 원 객체 ID
    @ApiModelProperty("원 객체이름") private String oriobjname;                 // 원 객체이름
    @ApiModelProperty("원 부서 ID") private String orideptid;                   // 원 부서 ID
    @ApiModelProperty("원 부서이름") private String orideptname;                // 원 부서이름
    @ApiModelProperty("원 직위직급이름") private String oridutyname;            // 원 직위직급이름
    @ApiModelProperty("원 직위직급이름상세") private String oridutynamedesc;    // 원 직위직급이름상세
    @ApiModelProperty("원 직위 ID") private String oripositionid;               // 원 직위 ID
    @ApiModelProperty("원 직급 ID") private String origradeid;                  // 원 직급 ID
    @ApiModelProperty("이력정보 FLAG") private String historyflag;              // 이력정보 FLAG
    @ApiModelProperty("보고일자") private String reportdt;                      // 보고일자
    @ApiModelProperty("발의자") private String proposer;                        // 발의자
    @ApiModelProperty("전자서명 ID") private String electsignid;                // 전자서명 ID
}

