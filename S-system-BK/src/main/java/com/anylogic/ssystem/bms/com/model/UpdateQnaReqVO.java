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
public class UpdateQnaReqVO extends AnyXCommonVO implements Serializable {
    // bms_com_qna
    @ApiModelProperty("아이디") private String qnaid;     // 아이디
    @ApiModelProperty("상위아이디") private String refid;  // 상위아이디
    @ApiModelProperty("그룹") private BigDecimal thread;      // 그룹
    @ApiModelProperty("순서") private BigDecimal pos;         // 순서
    @ApiModelProperty("깊이") private BigDecimal depth;       // 깊이
    @ApiModelProperty("제목") private String title;       // 제목
    @ApiModelProperty("내용") private String cont;        // 내용
    @ApiModelProperty("첨부파일여부") private String fileyn;// 첨부파일여부
    @ApiModelProperty("조회수") private BigDecimal selcnt;     // 조회수
    @ApiModelProperty("답변수") private BigDecimal repcnt;     // 답변수
    @ApiModelProperty("등록자아이디") private String inid;// 등록자아이디
    @ApiModelProperty("등록자명") private String inname;  // 등록자명
    @ApiModelProperty("등록일시") private String indt;    // 등록일시
    @ApiModelProperty("수정자아이디") private String upid;// 수정자아이디
    @ApiModelProperty("수정자명") private String upname;  // 수정자명
    @ApiModelProperty("수정일시") private String updt;    // 수정일시

}

