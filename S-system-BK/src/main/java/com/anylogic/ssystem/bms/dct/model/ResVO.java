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

@ApiModel("")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("") private String docid;                 // 
    @ApiModelProperty("") private String docttl;                // 
    @ApiModelProperty("") private String gubun;                 // 
    @ApiModelProperty("") private String keyword;               // 
    @ApiModelProperty("") private String authorid;              // 
    @ApiModelProperty("") private String authorname;            // 
    @ApiModelProperty("") private String authordutyname;        // 
    @ApiModelProperty("") private String authorpositionid;      // 
    @ApiModelProperty("") private String authorgradeid;         // 
    @ApiModelProperty("") private String authordeptid;          // 
    @ApiModelProperty("") private String authordeptname;        // 
    @ApiModelProperty("") private String authordeptnamedesc;    // 
    @ApiModelProperty("") private String lastauthorid;          // 
    @ApiModelProperty("") private String lastauthorname;        // 
    @ApiModelProperty("") private String state;                 // 
    @ApiModelProperty("") private BigDecimal docnoseq;              //
    @ApiModelProperty("") private String open;                  // 
    @ApiModelProperty("") private String openbasis;             // 
    @ApiModelProperty("") private String openreason;            // 
    @ApiModelProperty("") private String readrangetype;         // 
    @ApiModelProperty("") private String readlimittype;         // 
    @ApiModelProperty("") private String readlimitdate;         // 
    @ApiModelProperty("") private String delflag;               // 
    @ApiModelProperty("") private String tempflag;              // 
    @ApiModelProperty("") private String indt;                  // 
    @ApiModelProperty("") private String reportdt;              // 
    @ApiModelProperty("") private String enddt;                 // 
    @ApiModelProperty("") private String updt;                  // 
    @ApiModelProperty("") private String upid;                  // 
    @ApiModelProperty("") private String docfrom;               // 
    @ApiModelProperty("") private String moveflag;              // 
    @ApiModelProperty("") private String rcvsflag;              // 
    @ApiModelProperty("") private String transflag;             // 
    @ApiModelProperty("") private String typeflag;              // 
    @ApiModelProperty("") private String protectflag;           // 
    @ApiModelProperty("") private String setupflag;             // 
    @ApiModelProperty("") private String oridocid;              // 
    @ApiModelProperty("") private BigDecimal oriseq;                // 
    @ApiModelProperty("") private BigDecimal batchseq;              // 
    @ApiModelProperty("") private String paperflag;             // 
    @ApiModelProperty("") private String maindocflag;           // 
    @ApiModelProperty("") private BigDecimal attachseq;             // 
    @ApiModelProperty("") private String enfgubun;              // 
    @ApiModelProperty("") private String emergency;             // 
    @ApiModelProperty("") private String drmgubun;              // 
    @ApiModelProperty("") private String paperrecgubun;         // 
    @ApiModelProperty("") private String paperrecspecial;       // 
    @ApiModelProperty("") private String paperrectypes;         // 
    @ApiModelProperty("") private String migflag;               // 
    @ApiModelProperty("") private String addsendstate;          // 
    @ApiModelProperty("") private BigDecimal addsendcnt;            // 
    @ApiModelProperty("") private String docgroup;              // 
    @ApiModelProperty("") private String faketitle;             // 
    @ApiModelProperty("") private String papersendflag;         // 
    @ApiModelProperty("") private String filelendflag;          // 
}

