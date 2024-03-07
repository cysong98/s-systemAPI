/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.model;

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

@ApiModel("selectUseTempViewDetail")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectUseTempViewDetailResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("결재요청 ID") private String apprreqid;                   // 결재요청 ID
    @ApiModelProperty("요청타입") private String reqtype;                        // 요청타입
    @ApiModelProperty("요청일자") private String reqdt;                          // 요청일자
    @ApiModelProperty("요청제목") private String reqttl;                         // 요청제목
    @ApiModelProperty("요청등록일자") private String reqregdt;                   // 요청등록일자
    @ApiModelProperty("요청사유") private String reqcontents;                    // 요청사유
    @ApiModelProperty("요청자 ID") private String reqid;                         // 요청자 ID
    @ApiModelProperty("요청자명") private String reqname;                        // 요청자명
    @ApiModelProperty("요청자 부서 ID") private String reqdeptid;                // 요청자 부서 ID
    @ApiModelProperty("요청자 부서명") private String reqdeptname;               // 요청자 부서명
    @ApiModelProperty("진행상태") private String reqstatus;                      // 진행상태
    @ApiModelProperty("결재완료일자") private String apprenddt;                  // 결재완료일자
    @ApiModelProperty("비밀관리자 확인일자") private String adminconfirmdt;      // 비밀관리자 확인일자
    @ApiModelProperty("요청승인 여부") private String grantflag;                 // 요청승인 여부
    @ApiModelProperty("요청시현물 확인여부") private String reqrfidcheck;        // 요청시현물 확인여부
    @ApiModelProperty("요청시현물 확인일시") private String reqrfidcheckdt;      // 요청시현물 확인일시
    @ApiModelProperty("최종시현물 확인여부") private String finalrfidcheck;      // 최종시현물 확인여부
    @ApiModelProperty("최종시현물 확인일시") private String finalrfidcheckdt;    // 최종시현물 확인일시
    @ApiModelProperty("승인예약 요청일") private String reservedate;             // 승인예약 요청일
    @ApiModelProperty("파기 설정일") private String destructreserve;             // 파기 설정일
    @ApiModelProperty("신규") private String isnewrule;                          // 신규
    @ApiModelProperty("비밀관리 ID") private String mgmtid;                          // 비밀관리 ID
    @ApiModelProperty("요청열람 시작일시") private String reqreadbegindt;            // 요청열람 시작일시
    @ApiModelProperty("요청열람 만료일시") private String reqreadenddt;              // 요청열람 만료일시
    @ApiModelProperty("승인열람 시작일시") private String grantreadbegindt;          // 승인열람 시작일시
    @ApiModelProperty("승인열람 만료일시") private String grantreadenddt;            // 승인열람 만료일시
    @ApiModelProperty("열람여부") private String isread;                             // 열람여부
    @ApiModelProperty("열람일시") private String readdt;                             // 열람일시
}

