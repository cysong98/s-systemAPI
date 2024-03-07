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

@ApiModel("updateUserBIA")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserBIAResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("사용자아이디") private String userid;              // 사용자아이디
    @ApiModelProperty("로그인아이디") private String loginid;             // 로그인아이디
    @ApiModelProperty("사용자명") private String username;                // 사용자명
    @ApiModelProperty("부서아이디") private String deptid;                // 부서아이디
    @ApiModelProperty("부서명") private String deptname;                  // 부서명
    @ApiModelProperty("지역번호") private String residentno;              // 지역번호
    @ApiModelProperty("직위") private String position;                    // 직위
    @ApiModelProperty("직급") private String grade;                       // 직급
    @ApiModelProperty("비밀번호") private String password;                // 비밀번호
    @ApiModelProperty("사용자순서") private double userorder;             // 사용자순서
    @ApiModelProperty("임용구분") private String imgubun;                 // 임용구분
    @ApiModelProperty("존폐여부") private String isdeleted;               // 존폐여부
    @ApiModelProperty("겸직여부") private String isconcurrent;            // 겸직여부
    @ApiModelProperty("직위상세") private String positiondetail;          // 직위상세
    @ApiModelProperty("승인비밀번호") private String approvalpassword;    // 승인비밀번호
    @ApiModelProperty("등록자아이디") private String inid;                // 등록자아이디
    @ApiModelProperty("등록일시") private String indt;                    // 등록일시
    @ApiModelProperty("수정자아이디") private String upid;                // 수정자아이디
    @ApiModelProperty("수정일시") private String updt;                    // 수정일시
}

