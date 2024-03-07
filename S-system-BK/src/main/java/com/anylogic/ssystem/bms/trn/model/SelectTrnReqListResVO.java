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

@ApiModel("selectTrnReqList")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectTrnReqListResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("인계인수 일련번호") private String transferid;       // 인계인수 일련번호
    @ApiModelProperty("제목") private String title;                         // 제목
    @ApiModelProperty("인계인수사유") private String reason;                // 인계인수사유
    @ApiModelProperty("인계인수 대상구분") private String objgubun;         // 인계인수 대상구분
    @ApiModelProperty("요청일자") private String reqdate;                   // 요청일자
    @ApiModelProperty("요청기한") private String reqterm;                   // 요청기한
    @ApiModelProperty("기안자명") private String requsername;               // 기안자명
    @ApiModelProperty("기안자 ID") private String requserid;                // 기안자 ID
    @ApiModelProperty("기안자 부서명") private String reqdeptname;          // 기안자 부서명
    @ApiModelProperty("기안자 부서 ID") private String reqdeptid;           // 기안자 부서 ID
    @ApiModelProperty("기안자 직위직급") private String reqposition;        // 기안자 직위직급
    @ApiModelProperty("최종결재완료여부") private String apprstatus;        // 최종결재완료여부
    @ApiModelProperty("진행상태") private String status;                    // 진행상태
    @ApiModelProperty("등록자 ID") private String inid;                     // 등록자 ID
    @ApiModelProperty("등록일") private String indt;                        // 등록일
    @ApiModelProperty("첨부여부") private String isattach;                  // 첨부여부
    @ApiModelProperty("비밀관리자 여부") private String ismanage;           // 비밀관리자 여부
    @ApiModelProperty("동일부서 여부") private String isinside;             // 동일부서 여부
    @ApiModelProperty("대리인계 부서 ID") private String managedeptid;      // 대리인계 부서 ID
    @ApiModelProperty("부서장 인계인수 여부") private String ischiefyn;     // 부서장 인계인수 여부
    @ApiModelProperty("대리인계 사용자 ID") private String manageuserid;    // 대리인계 사용자 ID
    @ApiModelProperty("") private String incmemo;                           // 
    @ApiModelProperty("") private String objectid;                              // 
    @ApiModelProperty("") private String objectname;                            // 
    @ApiModelProperty("") private String objecttype;                            // 
    @ApiModelProperty("") private String pjtcardid;                             // 
    @ApiModelProperty("") private String pjtcardname;                           // 
    @ApiModelProperty("") private String paperflag;                             // 
    @ApiModelProperty("") private String procdate;                              // 
    @ApiModelProperty("") private String rfidflag;                              // 
    @ApiModelProperty("") private String rfiddate;                              // 
    @ApiModelProperty("") private String mgmtno;                                // 
    @ApiModelProperty("") private String seclevel;                              // 
    @ApiModelProperty("") private String reportdt;                              // 
    @ApiModelProperty("") private String docnoseq;                              // 
    @ApiModelProperty("") private String prerfidflag;                           // 
    @ApiModelProperty("") private String prerfiddate;                           // 
    @ApiModelProperty("") private String secstatus;                             // 
    @ApiModelProperty("") private BigDecimal apprseq;                                   //
    @ApiModelProperty("") private String apprusername;                              // 
    @ApiModelProperty("") private String appruserid;                                // 
    @ApiModelProperty("") private String apprdeptname;                              // 
    @ApiModelProperty("") private String apprdeptid;                                // 
    @ApiModelProperty("") private String apprposition;                              // 
    @ApiModelProperty("") private String apprcode;                                  // 
    @ApiModelProperty("") private String apprdate;                                  // 
    @ApiModelProperty("") private String apprreason;                                // 
    @ApiModelProperty("") private String state;                                     // 
    @ApiModelProperty("") private String electsignid;                               //
    @ApiModelProperty("") private String docno;                               //
    @ApiModelProperty("") private String copyno;                               //
    @ApiModelProperty("") private String mgmtid;                               //
    @ApiModelProperty("최종인수담당자") private String finalTakeOver;               // 최종인수담당자
}

