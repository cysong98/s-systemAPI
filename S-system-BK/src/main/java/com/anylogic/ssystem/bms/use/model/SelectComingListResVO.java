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

@ApiModel("selectComingList")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectComingListResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀관리 ID") private String mgmtid;                      // 비밀관리 ID
    @ApiModelProperty("관리번호") private String mgmtno;                         // 관리번호
    @ApiModelProperty("비밀 ID") private String docid;                           // 비밀 ID
    @ApiModelProperty("생산/접수 형태") private String regirecvtype;             // 생산/접수 형태
    @ApiModelProperty("등록일시") private String indt;                           // 등록일시
    @ApiModelProperty("비밀제목") private String secttl;                         // 비밀제목
    @ApiModelProperty("비밀등급") private String seclevel;                         // 비밀등급
    @ApiModelProperty("사본번호") private String copyno;                         // 사본번호
    @ApiModelProperty("처리담당자명") private String authorname;                 // 처리담당자명
    @ApiModelProperty("열람범위") private String secstatus;                      // 비밀상태
    @ApiModelProperty("") private String protdt;
    @ApiModelProperty("") private String orgnreclass;
    @ApiModelProperty("") private String copyreclass;
    @ApiModelProperty("") private String docno;
    @ApiModelProperty("") private String paperrecgubun;
}

