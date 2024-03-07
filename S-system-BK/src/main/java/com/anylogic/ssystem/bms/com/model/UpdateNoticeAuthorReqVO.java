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
import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class UpdateNoticeAuthorReqVO extends AnyXCommonVO implements Serializable {
    // bms_com_notice_author
    @ApiModelProperty("공지사항아이디") private String ntcid; // 공지사항아이디
    @ApiModelProperty("부서아이디") private String deptid;    // 부서아이디
    @ApiModelProperty("부서명") private String deptname;      // 부서명

}

