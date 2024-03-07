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

@ApiModel("selectMgmtRegiList")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectMgmtRegiListResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀관리 ID") private String mgmtid;                              // 비밀관리 ID
    @ApiModelProperty("관리번호") private String mgmtno;                                 // 관리번호
    @ApiModelProperty("비밀 ID") private String docid;                                   // 비밀 ID
    @ApiModelProperty("비밀등급") private String seclevel;                               // 비밀등급
    @ApiModelProperty("생산/접수 일자") private String regirecvdt;                       // 생산/접수 일자
    @ApiModelProperty("생산/접수 일자") private String regirecvtype;                       // 생산/접수 타입
    @ApiModelProperty("비밀제목") private String secttl;                                 // 비밀제목
    @ApiModelProperty("비밀형태") private BigDecimal sectype;                                // 비밀형태
    @ApiModelProperty("비밀형태") private String secstatus;                                // 비밀상태
    @ApiModelProperty("사본번호") private String copyno;                                 // 사본번호
    @ApiModelProperty("예고문보호기간") private String notiprotterm;                     // 예고문보호기간
    @ApiModelProperty("예고문보존기간") private String notiprsrvterm;                    // 예고문보존기간
    @ApiModelProperty("처리담당자명") private String authorname;                         // 처리담당자명
    @ApiModelProperty("부서명") private String deptname;                         // 부서명
    @ApiModelProperty("존안여부") private String cnsrvflag;                              // 존안여부
    @ApiModelProperty("존안일자") private String cnsrvdt;                                // 존안일자
    @ApiModelProperty("승인존안일자") private String grantcnsrvdt;                                // 승인존안일자
    @ApiModelProperty("RFID사용여부") private String rfidtagflag;                        // RFID사용여부
    @ApiModelProperty("삭선여부") private String delflag;                                // 삭선여부
    @ApiModelProperty("등록일시") private String indt;                                   // 등록일시
    @ApiModelProperty("이관일자") private String transdt;                                   // 이관일자
    @ApiModelProperty("존안보존일자") private String cnsrvterm;                          // 존안보존일자
    @ApiModelProperty("합철 관련 MGMT ID") private String aggregatemgmtid;               // 합철 관련 MGMT ID
    @ApiModelProperty("") private String destructconfirm;                                //
    @ApiModelProperty("") private String note;                                //
    @ApiModelProperty("") private String paperrecgubun;

    //bms_dct_attr
    @ApiModelProperty("문서번호") private String docno;                                //
    @ApiModelProperty("보존기간") private String prsrvterm;                                //
    @ApiModelProperty("원본재분류") private String orgnreclass;                                //
    @ApiModelProperty("사본재분류") private String copyreclass;                                //
    @ApiModelProperty("비밀구분") private String secgubun;                                //


    @ApiModelProperty("권한여부") private String auth;                                //
    @ApiModelProperty("열람권만만료일") private String readauthexpiredt;                                //

    @ApiModelProperty("") private int totalCount;
    @ApiModelProperty("") private int create5LevelCount;
    @ApiModelProperty("") private int createOtherCount;
    @ApiModelProperty("") private int receiptCount;

}

