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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.lang.String;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class UpdateUserDetailReqVO extends AnyXCommonVO implements Serializable {
    // com_userinfo_detail2
    @ApiModelProperty("사용자아이디") private String userid;                  // 사용자아이디
    @ApiModelProperty("비밀취급인가등급") private String secretgrade;          // 비밀취급인가등급
    @ApiModelProperty("사용자아이디") private String email;                   // 이메일
    @ApiModelProperty("비밀취급인가등급") private String mobilephone;          // 전화번호
    @ApiModelProperty("등록자아이디") private String inid;                    // 등록자아이디
    @ApiModelProperty("등록일시") private String indt;                        // 등록일시
    @ApiModelProperty("수정자아이디") private String upid;                    // 수정자아이디
    @ApiModelProperty("수정일시") private String updt;                       // 수정일시
}

