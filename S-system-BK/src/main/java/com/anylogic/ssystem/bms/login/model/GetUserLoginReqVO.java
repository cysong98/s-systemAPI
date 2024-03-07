/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.login.model;

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
public class GetUserLoginReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("")
    @Size(max=35)
    private String userid;                       //

    @ApiModelProperty
    private String jwt;

    @ApiModelProperty("") private String updt;
    @ApiModelProperty("") private String deptname;
    @ApiModelProperty("") private String positiondetail;
    @ApiModelProperty("") private String inid;
    @ApiModelProperty("") private String residentno;
    @ApiModelProperty("") private String imgubun;
    @ApiModelProperty("") private String position;
    @ApiModelProperty("") private String isdeleted;
    @ApiModelProperty("") private String upid;
    @ApiModelProperty("") private String grade;
    @ApiModelProperty("") private String deptid;
    @ApiModelProperty("") private String approvalpassword;
    @ApiModelProperty("") private String userorder;
    @ApiModelProperty("") private String indt;
    @ApiModelProperty("") private String password;
    @ApiModelProperty("") private String username;
    @ApiModelProperty("") private String loginid;
    @ApiModelProperty("") private String isconcurrent;


}

