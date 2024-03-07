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
public class InsertOffSendAllReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_offsend_req
    @ApiModelProperty("비밀 ID") private String docid;            // 비밀 ID
    @ApiModelProperty("수신처 ID") private String receiverid;     // 수신처 ID
    @ApiModelProperty("사번번호") private String copyno;          // 사번번호
    @ApiModelProperty("파일 ID") private String fileid;           // 파일 ID
    @ApiModelProperty("변환여부") private String tempflag;        // 변환여부
    @ApiModelProperty("구분") private String gubun;               // 구분
    @ApiModelProperty("파일제목") private String filetitle;       // 파일제목
    @ApiModelProperty("파일고유이름") private String sfilename;   // 파일고유이름
    @ApiModelProperty("파일사이즈") private BigDecimal filesize;// 파일사이즈
    @ApiModelProperty("파일순번") private BigDecimal fileseq;// 파일순번

}

