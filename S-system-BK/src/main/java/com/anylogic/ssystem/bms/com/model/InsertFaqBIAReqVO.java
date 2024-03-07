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
import java.util.List;

import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class InsertFaqBIAReqVO extends AnyXCommonVO implements Serializable {
    // bms_com_faq
    @ApiModelProperty("FAQ아이디") private String faqid;      // FAQ아이디
    @ApiModelProperty("제목") private String title;           // 제목
    @ApiModelProperty("내용") private String cont;            // 내용
    @ApiModelProperty("게시시작일") private String sdate;     // 게시시작일
    @ApiModelProperty("게시종료일") private String edate;     // 게시종료일
//    @ApiModelProperty("부서아이디") private String deptid;    // 부서아이디
//    @ApiModelProperty("부서명") private String deptname;      // 부서명
    @ApiModelProperty("부서아이디+부서명") private List<InsertFaqAuthorReqVO> deptlist;
    @ApiModelProperty("조회수") private double selcnt;// 조회수
    @Builder.Default
    @ApiModelProperty("확인여부") private double repcnt = -999999;// 확인여부
    @ApiModelProperty("등록자아이디") private String inid;    // 등록자아이디
    @ApiModelProperty("등록자명") private String inname;      // 등록자명
    @ApiModelProperty("등록일시") private String indt;        // 등록일시
    @ApiModelProperty("수정자아이디") private String upid;    // 수정자아이디
    @ApiModelProperty("수정자명") private String upname;      // 수정자명
    @ApiModelProperty("수정일시") private String updt;        // 수정일시
    @ApiModelProperty("첨부파일여부") private String fileyn;  // 첨부파일여부
    @ApiModelProperty("미사용") private String cnfmyn;        // 미사용
    @ApiModelProperty("미사용") private String rmk;           // 미사용

}

