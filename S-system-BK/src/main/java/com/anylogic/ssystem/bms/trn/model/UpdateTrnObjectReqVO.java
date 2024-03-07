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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.lang.String;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import java.io.Serializable;
import java.math.BigDecimal;

import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class UpdateTrnObjectReqVO extends AnyXCommonVO implements Serializable {
    // bms_trn_object
    @ApiModelProperty("") private String transferid;                //
    @ApiModelProperty("") private String objectid;                  //
    @ApiModelProperty("") private String objectname;                //
    @ApiModelProperty("") private String objecttype;                //
    @ApiModelProperty("") private String pjtcardid;                 //
    @ApiModelProperty("") private String pjtcardname;               //
    @ApiModelProperty("") private String paperflag;                 //
    @ApiModelProperty("") private String status;                    //
    @ApiModelProperty("") private String procdate;                  //
    @ApiModelProperty("") private String rfidflag;                  //
    @ApiModelProperty("") private String rfiddate;                  //
    @ApiModelProperty("") private String mgmtno;                    //
    @ApiModelProperty("") private String seclevel;                  //
    @ApiModelProperty("") private String reportdt;                  //
    @ApiModelProperty("") private String docnoseq;                  //
    @ApiModelProperty("") private String prerfidflag;               //
    @ApiModelProperty("") private String prerfiddate;               //
    @ApiModelProperty("") private String secstatus;                 //
}

