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
public class SelectVgroupUserReqVO extends AnyXCommonVO implements Serializable {
    // bms_com_faq_author
    @ApiModelProperty("가상그룹아이디")
    private String vgroupid;                                 // 사용자아이디

    @ApiModelProperty("사용자아이디")
    private String userid;                                   // 사용자아이디

    @ApiModelProperty("부서아이디")
    private String orgid;                                    // 부서아이디

    @ApiModelProperty("가상그룹유형")
    private String vgrouptype;                               // 사용자아이디
}

