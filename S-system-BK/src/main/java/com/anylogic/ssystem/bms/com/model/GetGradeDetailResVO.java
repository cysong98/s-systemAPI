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
import java.util.List;

import com.anylogic.ssystem.common.model.AnyXCommonVO;

@ApiModel("getGradeDetail")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetGradeDetailResVO extends AnyXCommonVO implements Serializable {
    // com_grade
    @ApiModelProperty("직급코드") private String grade;          // 직급코드
    @ApiModelProperty("직급명") private String gradename;        // 직급명
    @ApiModelProperty("직종코드") private String jobtype;        // 직종코드
    @ApiModelProperty("직류코드") private String jobgubun;       // 직류코드
    @ApiModelProperty("등록자아이디") private String inid;        // 등록자아이디
    @ApiModelProperty("등록일시") private String indt;           // 등록일시
    @ApiModelProperty("수정자아이디") private String upid;        // 수정자아이디
    @ApiModelProperty("수정일시") private String updt;           // 수정일시
}

