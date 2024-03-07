/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.trn.model;

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

@ApiModel("updateTrnStep")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateTrnStepResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("") private String transferid;      // 
    @ApiModelProperty("") private BigDecimal apprseq;         //
    @ApiModelProperty("") private String apprusername;    // 
    @ApiModelProperty("") private String appruserid;      // 
    @ApiModelProperty("") private String apprdeptname;    // 
    @ApiModelProperty("") private String apprdeptid;      // 
    @ApiModelProperty("") private String apprposition;    // 
    @ApiModelProperty("") private String apprcode;        // 
    @ApiModelProperty("") private String apprstatus;      // 
    @ApiModelProperty("") private String apprdate;        // 
    @ApiModelProperty("") private String apprreason;      // 
    @ApiModelProperty("") private String inid;            // 
    @ApiModelProperty("") private String indt;            // 
    @ApiModelProperty("") private String state;           // 
    @ApiModelProperty("") private String electsignid;     // 
}

