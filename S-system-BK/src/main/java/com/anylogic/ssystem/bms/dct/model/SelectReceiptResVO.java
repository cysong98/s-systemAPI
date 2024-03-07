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

@ApiModel("selectReceipt")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectReceiptResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀 ID") private String docid;                                // 비밀 ID
    @ApiModelProperty("사본번호") private String copyno;                              // 사본번호
    @ApiModelProperty("송증영수증 제목") private String invrcpttl;                    // 송증영수증 제목
    @ApiModelProperty("비밀등급") private String seclevel;                            // 비밀등급
    @ApiModelProperty("관리번호") private String mgmtno;                              // 관리번호
    @ApiModelProperty("문서번호") private String docno;                               // 문서번호
    @ApiModelProperty("수신부서명") private String recdeptname;                       // 수신부서명
    @ApiModelProperty("참조부서명") private String refdeptname;                       // 참조부서명
    @ApiModelProperty("발신자부처 ID") private String sndminiid;                      // 발신자부처 ID
    @ApiModelProperty("발신자 부처명") private String sndmininame;                    // 발신자 부처명
    @ApiModelProperty("발신자 부서 ID") private String snddeptid;                     // 발신자 부서 ID
    @ApiModelProperty("발신자 부서명") private String snddeptname;                    // 발신자 부서명
    @ApiModelProperty("발신자 ID") private String sndid;                              // 발신자 ID
    @ApiModelProperty("발신자명") private String sndname;                             // 발신자명
    @ApiModelProperty("발신자 직급직위명") private String snddutyname;                // 발신자 직급직위명
    @ApiModelProperty("발신일자") private String snddt;                               // 발신일자
    @ApiModelProperty("발신자전자서명") private String sndelectsignid;                // 발신자전자서명
    @ApiModelProperty("수신사 부처 ID") private String rcvminiid;                     // 수신사 부처 ID
    @ApiModelProperty("수신자 부처명") private String rcvmininame;                    // 수신자 부처명
    @ApiModelProperty("수신자 부서 ID") private String rcvdeptid;                     // 수신자 부서 ID
    @ApiModelProperty("수신자 부서명") private String rcvdeptname;                    // 수신자 부서명
    @ApiModelProperty("수신자 ID") private String rcvid;                              // 수신자 ID
    @ApiModelProperty("수신자명") private String rcvname;                             // 수신자명
    @ApiModelProperty("수신자 직급직위명") private String rcvdutyname;                // 수신자 직급직위명
    @ApiModelProperty("수신일자") private String rcvdt;                               // 수신일자
    @ApiModelProperty("수신자 전자서명") private String rcvelectsignid;               // 수신자 전자서명
    @ApiModelProperty("수발비밀 ID") private String sndrcvdocid;                      // 수발비밀 ID
    @ApiModelProperty("실패사유") private String failreason;                          // 실패사유
    @ApiModelProperty("붙임파일 수량") private BigDecimal qty;                            // 붙임파일 수량
    @ApiModelProperty("송증영수증형태") private String invrcpttype;                   // 송증영수증형태
    @ApiModelProperty("등록자 ID") private String rgstid;                             // 등록자 ID
    @ApiModelProperty("등록자명") private String rgstname;                            // 등록자명
    @ApiModelProperty("등록일자") private String rgstdt;                              // 등록일자
    @ApiModelProperty("수정자 ID") private String updtid;                             // 수정자 ID
    @ApiModelProperty("수정자명") private String updtname;                            // 수정자명
    @ApiModelProperty("수정일자") private String updtdt;                              // 수정일자
    @ApiModelProperty("송증시점확인정보파일 ID") private String sndverifyfileid;      // 송증시점확인정보파일 ID
    @ApiModelProperty("영수증시점확인정보파일 ID") private String rcvverifyfileid;    // 영수증시점확인정보파일 ID
    @ApiModelProperty("수신처 ID") private String receiverid;                         // 수신처 ID

    @ApiModelProperty("수신처 ID") private String acceptid;                            //접수자id(처리과 비밀관리자)
    @ApiModelProperty("수신처 ID") private String acceptname;                          //접수자명(처리과 비밀관리자)
    @ApiModelProperty("수신처 ID") private String acceptdutyname;                      //접수자직위/직급(처리과 비밀관리자)
    @ApiModelProperty("수신처 ID") private String acceptdeptname;                      //접수자부서(처리과 비밀관리자)
}

