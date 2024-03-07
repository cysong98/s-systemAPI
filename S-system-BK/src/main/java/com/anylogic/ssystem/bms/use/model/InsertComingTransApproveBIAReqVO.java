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
import java.util.List;

import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class InsertComingTransApproveBIAReqVO extends AnyXCommonVO implements Serializable {

    // 1) bms_dct_apprreq table
    @ApiModelProperty("결재요청 ID") private String apprreqid;                // 결재요청 ID
    @ApiModelProperty("요청타입") private String reqtype;                     // 요청타입
    @ApiModelProperty("요청일자") private String reqdt;                       // 요청일자
    @ApiModelProperty("요청제목") private String reqttl;                      // 요청제목
    @ApiModelProperty("요청등록일자") private String reqregdt;                // 요청등록일자
    @ApiModelProperty("요청사유") private String reqcontents;                 // 요청사유
    @ApiModelProperty("요청자 ID") private String reqid;                      // 요청자 ID
    @ApiModelProperty("요청자명") private String reqname;                     // 요청자명
    @ApiModelProperty("요청자 부서 ID") private String reqdeptid;             // 요청자 부서 ID
    @ApiModelProperty("요청자 부서명") private String reqdeptname;            // 요청자 부서명
    @ApiModelProperty("진행상태") private String reqstatus;                   // 진행상태
    @ApiModelProperty("결재완료일자") private String apprenddt;               // 결재완료일자
    @ApiModelProperty("비밀관리자 확인일자") private String adminconfirmdt;   // 비밀관리자 확인일자
    @ApiModelProperty("요청승인 여부") private String grantflag;              // 요청승인 여부
    @ApiModelProperty("요청시현물 확인여부") private String reqrfidcheck;     // 요청시현물 확인여부
    @ApiModelProperty("요청시현물 확인일시") private String reqrfidcheckdt;   // 요청시현물 확인일시
    @ApiModelProperty("최종시현물 확인여부") private String finalrfidcheck;   // 최종시현물 확인여부
    @ApiModelProperty("최종시현물 확인일시") private String finalrfidcheckdt; // 최종시현물 확인일시
    @ApiModelProperty("승인예약 요청일") private String reservedate;          // 승인예약 요청일
    @ApiModelProperty("파기 설정일") private String destructreserve;          // 파기 설정일
    @ApiModelProperty("신규") private String isnewrule;                       // 신규

    // 2) bms_dct_apprreq_path table
    @ApiModelProperty("") private List<InsertComingTransApproveBIAReqVO> pathList; // 경로 테이블 다건처리 용도
    @ApiModelProperty("요청경로순번") private BigDecimal apprreqseq;   // 요청경로순번
    @ApiModelProperty("결재유형") private String apprtype;            // 결재유형
    @ApiModelProperty("결재처리구분") private String apprgubun;       // 결재처리구분
    @ApiModelProperty("결재요청상태") private String apprstatus;      // 결재요청상태
    @ApiModelProperty("결재자 ID") private String apprid;             // 결재자 ID
    @ApiModelProperty("결재자명") private String apprname;            // 결재자명
    @ApiModelProperty("결재자부서 ID") private String apprdeptid;     // 결재자부서 ID
    @ApiModelProperty("결재자 부서명") private String apprdeptname;   // 결재자 부서명
    @ApiModelProperty("결재자 직위직급명") private String apprdutyname;// 결재자 직위직급명
    @ApiModelProperty("결재자 직위 ID") private String apprposid;     // 결재자 직위 ID
    @ApiModelProperty("결재자 직급 ID") private String apprgrdid;     // 결재자 직급 ID
    @ApiModelProperty("결재자 의견") private String appropinion;      // 결재자 의견
    @ApiModelProperty("전자서명") private String electsign;           // 전자서명
    @ApiModelProperty("등록자 ID") private String rgstid;             // 등록자 ID
    @ApiModelProperty("등록자명") private String rgstname;            // 등록자명
    @ApiModelProperty("등록일시") private String rgstdt;              // 등록일시
    @ApiModelProperty("수정자 ID") private String updtid;             // 수정자 ID
    @ApiModelProperty("수정자명") private String updtname;            // 수정자명
    @ApiModelProperty("수정일시") private String updtdt;              // 수정일시

    // 3) bms_dct_apprreq_trans table
    @ApiModelProperty("") private List<InsertComingTransApproveBIAReqVO> transList; // 이관요청 다건처리 용도
    @ApiModelProperty("관리번호") private String mgmtid;                    // 관리번호
    @ApiModelProperty("이관일시") private String transdt;                   // 이관일시
    @ApiModelProperty("요청이관연장일시") private String reqtransnextdt;                   // 요청이관연장일시
    @ApiModelProperty("승인이관연장일시") private String granttransnextdt;                   // 승인이관연장일시

    // 3-1) bms_dct_mgmt_regi table
    @ApiModelProperty("") private String secstatus;

    // 4) bms_dct_hist_card table
    @ApiModelProperty("비밀 ID") private String[] docid;              // 비밀 ID
    @ApiModelProperty("이력 일시") private String histdt;           // 이력 일시
    @ApiModelProperty("이력 순번") private BigDecimal histseq;// 이력 순번
    @ApiModelProperty("이력구분") private String histgubun;         // 이력구분
    @ApiModelProperty("소속코드") private String deptid;            // 소속코드
    @ApiModelProperty("소속명") private String deptname;            // 소속명
    @ApiModelProperty("직위직급명") private String dutyname;        // 직위직급명
    @ApiModelProperty("사용자 ID") private String userid;           // 사용자 ID
    @ApiModelProperty("사용자명") private String username;          // 사용자명
    @ApiModelProperty("이력상태") private String histstatus;        // 이력상태
    @ApiModelProperty("기타") private String etc;                   // 기타
    @ApiModelProperty("전자서명 ID") private String electsignid;    // 전자서명 ID
}

