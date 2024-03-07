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

import com.anylogic.ssystem.common.model.AnyXCommonVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("selectReferenceList")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectReferenceListResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀ID") private String docid;
    @ApiModelProperty("비밀명") private String docttl;
    @ApiModelProperty("작성일자") private String indt;
    @ApiModelProperty("담당자 ID") private String authorid;
    @ApiModelProperty("담당자 이름") private String authorname;
    @ApiModelProperty("담당자직위직급") private String authordutyname;
    @ApiModelProperty("담당자부서이름") private String authordeptname;
    @ApiModelProperty("상황") private String state;
}

