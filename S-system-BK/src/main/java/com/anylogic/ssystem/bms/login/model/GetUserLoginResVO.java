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

@ApiModel("getUserLogin")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetUserLoginResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("") private String userid;              // 
    @ApiModelProperty("") private String loginid;             // 
    @ApiModelProperty("") private String username;            // 
    @ApiModelProperty("") private String deptid;              // 
    @ApiModelProperty("") private String deptname;            // 
    @ApiModelProperty("") private String residentno;          // 
    @ApiModelProperty("직급") private String position;        // 직급
    @ApiModelProperty("직위") private String grade;           // 직위
    @ApiModelProperty("") private double userorder;           // 
    @ApiModelProperty("") private String imgubun;             // 
    @ApiModelProperty("") private String isdeleted;           // 
    @ApiModelProperty("") private String isconcurrent;        // 
    @ApiModelProperty("") private String positiondetail;      //

    @ApiModelProperty("") private String absenceyn;      //
    @ApiModelProperty("") private String absenceid;      //
    @ApiModelProperty("") private String absencedt;      //

    @ApiModelProperty("") private String vgroupid;                //

    @ApiModelProperty("") private String orgname;                //
    @ApiModelProperty("") private String parentorgid;                //


}

