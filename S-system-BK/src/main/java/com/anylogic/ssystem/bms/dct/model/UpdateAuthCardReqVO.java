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

import com.anylogic.ssystem.common.model.AnyXCommonVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("selectDctFile")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateAuthCardReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀ID") private String docid;                           // 비밀 ID
    @ApiModelProperty("권한부여일시") private String authgrantdt;                 // 권한부여일시
    @ApiModelProperty("권한카드순번") private int authseq;                 // 권한카드순번
    @ApiModelProperty("결재요청ID") private String apprreqid;               	    // 결재요청ID
    @ApiModelProperty("권한구분") private String authgubun;               		// 권한구분
    @ApiModelProperty("권한목적") private String authgoal;                		// 권한목적
    @ApiModelProperty("권한인가여부") private String authgrantflag;           	// 권한인가여부
    @ApiModelProperty("권한요청일시") private String authreqdt;               	// 권한요청일시
    @ApiModelProperty("권한만료일시") private String authexpiredt;            	// 권한만료일시
    @ApiModelProperty("인쇄비밀파기예정일자") private String prtdestructplandt;     // 인쇄비밀파기예정일자
    @ApiModelProperty("인쇄비밀실제파기일자") private String prtdestructdt;         // 인쇄비밀실제파기일자
    @ApiModelProperty("요청자소속ID") private String reqdeptid;               	// 요청자소속ID
    @ApiModelProperty("요청자소속명") private String reqdeptname;             	// 요청자소속명
    @ApiModelProperty("요청자ID") private String reqid;                   		// 요청자ID
    @ApiModelProperty("요청자명") private String reqname;                 		// 요청자명
    @ApiModelProperty("비밀담당자소속ID") private String authordeptid;             // 비밀담당자소속ID
    @ApiModelProperty("비밀담당자소속명") private String authordeptname;          	// 비밀담당자소속명
    @ApiModelProperty("비밀담당자ID") private String authorid;                	// 비밀담당자ID
    @ApiModelProperty("비밀담당자명") private String authorname;              	// 비밀담당자명
    @ApiModelProperty("비밀관리자(결재자)소속코드") private String apprdeptid;       // 비밀관리자(결재자)소속코드
    @ApiModelProperty("비밀관리자(결재자)소속명") private String apprdeptname;       // 비밀관리자(결재자)소속명
    @ApiModelProperty("비밀관리자(결재자)ID") private String apprid;                // 비밀관리자(결재자)ID
    @ApiModelProperty("비밀관리자(결재자)명") private String apprname;              // 비밀관리자(결재자)명
    @ApiModelProperty("회수일시") private String revokedt;                		 // 회수일시
    @ApiModelProperty("회수자ID") private String revokeid;                		 // 회수자ID
    @ApiModelProperty("회수자명") private String revokename;              		 // 회수자명
}

