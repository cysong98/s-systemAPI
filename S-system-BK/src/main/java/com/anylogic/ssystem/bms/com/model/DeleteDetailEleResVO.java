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

@ApiModel("deleteDetail")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteDetailEleResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("") private String userid;              // 
    @ApiModelProperty("") private String email;               // 
    @ApiModelProperty("") private String duty;                // 
    @ApiModelProperty("") private String homepage;            // 
    @ApiModelProperty("") private String officephone;         // 
    @ApiModelProperty("") private String officefax;           // 
    @ApiModelProperty("") private String officezip;           // 
    @ApiModelProperty("") private String officeaddr;          // 
    @ApiModelProperty("") private String officeaddrdetail;    // 
    @ApiModelProperty("") private String mobilephone;         // 
    @ApiModelProperty("") private String homephone;           // 
    @ApiModelProperty("") private String homezip;             // 
    @ApiModelProperty("") private String homeaddr;            // 
    @ApiModelProperty("") private String homeaddrdetail;      // 
    @ApiModelProperty("") private String fileid;              // 
    @ApiModelProperty("") private String inid;                // 
    @ApiModelProperty("") private String indt;                // 
    @ApiModelProperty("") private String upid;                // 
    @ApiModelProperty("") private String updt;                // 
    @ApiModelProperty("") private String isemail;             // 
    @ApiModelProperty("") private String sendemail;           // 
}

