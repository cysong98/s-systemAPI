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
import com.anylogic.ssystem.common.model.AnyXCommonVO;

@ApiModel("selectSendRecvList")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectSendRecvListResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("수발신일련번호") private String sndrcvseq;     // 수발신일련번호
    @ApiModelProperty("기관 ID") private String orgid;                // 기관 ID
    @ApiModelProperty("기관 이름") private String orgname;            // 기관 이름
    @ApiModelProperty("수발신 구분") private String sndrcvgubun;      // 수발신 구분
    @ApiModelProperty("문서부서 ID") private String docnodeptid;      // 문서부서 ID
    @ApiModelProperty("문서부서명") private String docnodeptname;     // 문서부서명
    @ApiModelProperty("문서번호") private String docnoseq;            // 문서번호
    @ApiModelProperty("수발문서제목") private String sndrcvttl;       // 수발문서제목
    @ApiModelProperty("비밀등급") private String seclevel;            // 비밀등급
    @ApiModelProperty("사본번호") private String copyno;              // 사본번호
    @ApiModelProperty("수량") private double qty;                     // 수량
    @ApiModelProperty("수발신 일시") private String sndrcvdt;         // 수발신 일시
    @ApiModelProperty("발신자 부서 ID") private String snddeptid;     // 발신자 부서 ID
    @ApiModelProperty("발신자 부서명") private String snddeptname;    // 발신자 부서명
    @ApiModelProperty("발신자 ID") private String snduserid;          // 발신자 ID
    @ApiModelProperty("발신자명") private String sndusername;         // 발신자명
    @ApiModelProperty("수신자 부서 ID") private String rcvdeptid;     // 수신자 부서 ID
    @ApiModelProperty("수신자 부서명") private String rcvdeptname;    // 수신자 부서명
    @ApiModelProperty("수신자 ID") private String rcvuserid;          // 수신자 ID
    @ApiModelProperty("수신자명") private String rcvusername;         // 수신자명
    @ApiModelProperty("등록자 ID") private String rgstid;             // 등록자 ID
    @ApiModelProperty("등록자명") private String rgstname;            // 등록자명
    @ApiModelProperty("등록일시") private String rgstdt;              // 등록일시
    @ApiModelProperty("수정자 ID") private String updtid;             // 수정자 ID
    @ApiModelProperty("수정자명") private String updtname;            // 수정자명
    @ApiModelProperty("수정일시") private String updtdt;              // 수정일시
    @ApiModelProperty("관리번호") private String mgmtno;              // 관리번호
    @ApiModelProperty("비밀 ID") private String docid;                // 비밀 ID
}

