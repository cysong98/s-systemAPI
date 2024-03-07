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

@ApiModel("insertRecv")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertRecvResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀 ID") private String docid;                       // 비밀 ID
    @ApiModelProperty("수신처 ID") private String receiverid;                // 수신처 ID
    @ApiModelProperty("임시 FLAG") private String tempflag;                  // 임시 FLAG
    @ApiModelProperty("일련번호") private BigDecimal seq;                        // 일련번호
    @ApiModelProperty("구분") private String gubun;                          // 구분
    @ApiModelProperty("부서 ID") private String deptid;                      // 부서 ID
    @ApiModelProperty("부서 이름") private String deptname;                  // 부서 이름
    @ApiModelProperty("참조부서 ID") private String refdeptid;               // 참조부서 ID
    @ApiModelProperty("참조부서 이름") private String refdeptname;           // 참조부서 이름
    @ApiModelProperty("우편번호") private String zipcode;                    // 우편번호
    @ApiModelProperty("주소정보") private String address;                    // 주소정보
    @ApiModelProperty("전화번호") private String phonenumber;                // 전화번호
    @ApiModelProperty("FAX번호") private String faxnumber;                   // FAX번호
    @ApiModelProperty("수신자 기호") private String recsymbol;               // 수신자 기호
    @ApiModelProperty("등록일") private String indt;                         // 등록일
    @ApiModelProperty("등록자 ID") private String inid;                      // 등록자 ID
    @ApiModelProperty("처리일") private String actdt;                        // 처리일
    @ApiModelProperty("처리상태") private String actstatus;                  // 처리상태
    @ApiModelProperty("처리자 이름") private String actername;               // 처리자 이름
    @ApiModelProperty("처리자 부서이름") private String acterdeptname;       // 처리자 부서이름
    @ApiModelProperty("처리자 전화번호") private String acterphonenumber;    // 처리자 전화번호
    @ApiModelProperty("시스템유형") private String systemtype;               // 시스템유형
    @ApiModelProperty("유통망 종류") private String relaytype;               // 유통망 종류
    @ApiModelProperty("부서조직 구분") private String deptorgflag;           // 부서조직 구분
    @ApiModelProperty("배포처 ID") private String distdeptid;                // 배포처 ID
    @ApiModelProperty("배포처명") private String distdeptname;               // 배포처명
    @ApiModelProperty("친전여부") private String distplflag;                 // 친전여부
    @ApiModelProperty("일련번호2") private BigDecimal seq2;                      // 일련번호2
    @ApiModelProperty("배부처리상태") private String distactstatus;          // 배부처리상태
    @ApiModelProperty("추가발송횟수") private BigDecimal addsendcnt;             // 추가발송횟수
    @ApiModelProperty("배부처리일자") private String distactdt;              // 배부처리일자
    @ApiModelProperty("") private String copyprotflag;                       //
    @ApiModelProperty("") private String copyprotdt;                         //
    @ApiModelProperty("") private String copynextlevel;                      //
    @ApiModelProperty("") private String copyreclass;                        //
    @ApiModelProperty("") private String copyprotcond;                       //
    @ApiModelProperty("") private BigDecimal seq3;                               //
    @ApiModelProperty("") private String readflag;                           //
    @ApiModelProperty("") private String readerid;                           //
    @ApiModelProperty("") private String readdt;                             //
}

