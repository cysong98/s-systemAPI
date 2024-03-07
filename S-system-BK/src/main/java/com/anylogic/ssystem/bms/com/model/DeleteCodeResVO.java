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

@ApiModel("deleteCode")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteCodeResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("코드 ID") private String codeid;               // 코드 ID
    @ApiModelProperty("코드이름") private String codename;            // 코드이름
    @ApiModelProperty("코드분류 ID") private String tcodeid;          // 코드분류 ID
    @ApiModelProperty("사용 FLAG") private String usedflag;           // 사용 FLAG
    @ApiModelProperty("설명") private String description;             // 설명
    @ApiModelProperty("등록자 ID") private String inid;               // 등록자 ID
    @ApiModelProperty("등록일") private String indt;                  // 등록일
    @ApiModelProperty("수정자 ID") private String upid;               // 수정자 ID
    @ApiModelProperty("수정일") private String updt;                  // 수정일
    @ApiModelProperty("변경 FLAG") private String modifyflag;         // 변경 FLAG
    @ApiModelProperty("화면표시 FLAG") private String displayflag;    // 화면표시 FLAG
    @ApiModelProperty("확인 FLAG") private String ackflag;            // 확인 FLAG
    @ApiModelProperty("추가확인 FLAG") private String expackflag;     // 추가확인 FLAG
}

