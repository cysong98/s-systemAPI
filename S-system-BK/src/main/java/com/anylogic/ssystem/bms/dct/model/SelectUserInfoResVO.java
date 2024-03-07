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

@ApiModel("selectUserInfo")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectUserInfoResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("") private String userid;              // 
    @ApiModelProperty("") private String loginid;             // 
    @ApiModelProperty("") private String username;            // 
    @ApiModelProperty("") private String deptid;              // 
    @ApiModelProperty("") private String deptname;            // 
    @ApiModelProperty("") private String residentno;          // 
    @ApiModelProperty("") private String position;            // 
    @ApiModelProperty("") private String grade;               // 
    @ApiModelProperty("") private String password;            // 
    @ApiModelProperty("") private BigDecimal userorder;           //
    @ApiModelProperty("") private String imgubun;             // 
    @ApiModelProperty("") private String isdeleted;           // 
    @ApiModelProperty("") private String isconcurrent;        // 
    @ApiModelProperty("") private String positiondetail;      // 
    @ApiModelProperty("") private String approvalpassword;    // 
    @ApiModelProperty("") private String inid;                // 
    @ApiModelProperty("") private String indt;                // 
    @ApiModelProperty("") private String upid;                // 
    @ApiModelProperty("") private String updt;                // 
}

