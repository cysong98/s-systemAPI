/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import java.io.Serializable;
import java.lang.String;

import com.anylogic.ssystem.common.model.AnyXCommonVO;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class DeleteSignBIASelectFileReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("파일 ID") private String fileid;                   // 파일 ID
    @ApiModelProperty("파일이름") private String cfilename;            // 파일이름
    @ApiModelProperty("파일 고유이름") private String sfilename;            // 파일 고유이름
    @ApiModelProperty("파일크기") private String filesize;                 // 파일크기
    @ApiModelProperty("저장아이디") private String saveid;                 // 저장아이디
    @ApiModelProperty("파일WIDTH") private String filewidth;              // 파일WIDTH
    @ApiModelProperty("파일HEIGHT") private String fileheight;            // 파일HEIGHT
    @ApiModelProperty("등록자 ID") private String inid;                   // 등록자 ID
    @ApiModelProperty("등록일시") private String indt;                    // 등록일시
    @ApiModelProperty("수정자 ID") private String upid;                   // 수정자 ID
    @ApiModelProperty("수정일시") private String updt;                    // 수정일시
    @ApiModelProperty("파일 URL") private String fileurl;                // 파일 URL
    @ApiModelProperty("파일경로") private String filepath;                // 파일경로
}

