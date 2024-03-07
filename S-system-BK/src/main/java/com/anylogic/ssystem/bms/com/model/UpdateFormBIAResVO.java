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
import com.anylogic.ssystem.common.model.AnyXCommonVO;

@ApiModel("updateFormBIA")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateFormBIAResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("파일 ID") private String fileid;            // 파일 ID
    @ApiModelProperty("서식 ID") private String formid;            // 서식 ID
    @ApiModelProperty("서식 제목") private String formtitle;       // 서식 제목
    @ApiModelProperty("서식 이름") private String formname;        // 서식 이름
    @ApiModelProperty("서식 크기") private double formsize;        // 서식 크기
    @ApiModelProperty("서식 순서") private String formorder;       // 서식 순서
    @ApiModelProperty("등록자 ID") private String inid;            // 등록자 ID
    @ApiModelProperty("등록일") private String indt;               // 등록일
    @ApiModelProperty("수정자 ID") private String upid;            // 수정자 ID
    @ApiModelProperty("수정일") private String updt;               // 수정일
    @ApiModelProperty("부서 ID") private String deptid;            // 부서 ID
    @ApiModelProperty("부서 이름") private String deptname;        // 부서 이름
    @ApiModelProperty("부서 유형") private String depttype;        // 부서 유형
    @ApiModelProperty("서식코드 1") private String formcode1;      // 서식코드 1
    @ApiModelProperty("서식코드 2") private String formcode2;      // 서식코드 2
    @ApiModelProperty("서식코드 3") private String formcode3;      // 서식코드 3
    @ApiModelProperty("서식비고") private String formcomment;      // 서식비고
    @ApiModelProperty("카테고리 ID") private String categoryid;    // 카테고리 ID
}

