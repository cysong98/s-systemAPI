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

@ApiModel("selectLogoList")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectLogoListResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("조직 ID") private String orgid;                   // 조직 ID
    @ApiModelProperty("조직 이름") private String orgname;               // 조직 이름
    @ApiModelProperty("상위조직 ID") private String parentorgid;         // 상위조직 ID
    @ApiModelProperty("조직유형") private String orgtype;                // 조직유형
    @ApiModelProperty("조직순서") private double orgorder;               // 조직순서
    @ApiModelProperty("최상위조직 ID") private String toporgid;          // 최상위조직 ID
    @ApiModelProperty("화면표시이름") private String displayname;        // 화면표시이름
    @ApiModelProperty("설명 ID") private String descriptionid;           // 설명 ID
    @ApiModelProperty("생성일") private String whencreated;              // 생성일
    @ApiModelProperty("폐지일") private String whendeleted;              // 폐지일
    @ApiModelProperty("존폐여부") private String isdeleted;              // 존폐여부
    @ApiModelProperty("기관장 ID") private String chiefid;               // 기관장 ID
    @ApiModelProperty("기관장 직위") private String chiefposition;       // 기관장 직위
    @ApiModelProperty("하부소속기관유형") private double suborgtype;     // 하부소속기관유형
    @ApiModelProperty("조직종류") private String orgkind;                // 조직종류
    @ApiModelProperty("유통망 종류") private String relaytype;           // 유통망 종류
    @ApiModelProperty("등록자 ID") private String inid;                  // 등록자 ID
    @ApiModelProperty("등록일") private String indt;                     // 등록일
    @ApiModelProperty("수정자 ID") private String upid;                  // 수정자 ID
    @ApiModelProperty("수정일") private String updt;                     // 수정일
    @ApiModelProperty("문서부서 ID") private String docdeptid;           // 문서부서 ID
    @ApiModelProperty("존안지원부서 ID") private String presedeptid;     // 존안지원부서 ID
    @ApiModelProperty("이관관리부서 ID") private String transdeptid;     // 이관관리부서 ID
    @ApiModelProperty("부서 수준") private double orgdepth;              // 부서 수준
    @ApiModelProperty("전자문서시스템") private String docsysteminfo;    // 전자문서시스템
    @ApiModelProperty("오프라인 열람여부") private String readtype;      // 오프라인 열람여부
    @ApiModelProperty("장비관리자 ID") private String belonguserid;      // 장비관리자 ID
    @ApiModelProperty("일련번호") private BigDecimal seq;                         // 일련번호
    @ApiModelProperty("관리구분") private String managegubun;                 // 관리구분
    @ApiModelProperty("파일 ID") private String fileid;                       // 파일 ID
    @ApiModelProperty("비고") private String etc;                             // 비고
    @ApiModelProperty("") private String isuse;
    @ApiModelProperty("") private String isdefault;
    @ApiModelProperty("사용자명") private String username;                // 사용자명
}