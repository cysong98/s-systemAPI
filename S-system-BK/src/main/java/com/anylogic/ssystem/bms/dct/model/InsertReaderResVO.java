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
import com.anylogic.ssystem.common.model.AnyXCommonVO;

@ApiModel("insertReader")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertReaderResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀 ID") private String docid;                // 비밀 ID
    @ApiModelProperty("사용자 ID") private String userid;             // 사용자 ID
    @ApiModelProperty("사용자 이름") private String username;         // 사용자 이름
    @ApiModelProperty("직위 ID") private String positionid;           // 직위 ID
    @ApiModelProperty("직급 ID") private String gradeid;              // 직급 ID
    @ApiModelProperty("부서 ID") private String deptid;               // 부서 ID
    @ApiModelProperty("부서이름") private String deptname;            // 부서이름
    @ApiModelProperty("직위직급이름") private String dutyname;        // 직위직급이름
    @ApiModelProperty("부서이름상세") private String deptnamedesc;    // 부서이름상세
    @ApiModelProperty("열람여부") private String isread;              // 열람여부
    @ApiModelProperty("등록일") private String indt;                  // 등록일
    @ApiModelProperty("열람일시") private String readdt;              // 열람일시
    @ApiModelProperty("등록자 ID") private String inid;               // 등록자 ID
    @ApiModelProperty("등록일련번호") private String inseq;           // 등록일련번호
    @ApiModelProperty("유형") private String type;                    // 유형
    @ApiModelProperty("접수일") private String acceptdt;              // 접수일
    @ApiModelProperty("") private String begindt;                     //
    @ApiModelProperty("") private String enddt;                       //
    @ApiModelProperty("") private String reason;                      //
}

