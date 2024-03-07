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
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import java.io.Serializable;
import java.lang.String;
import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class SelectCompleteApprReqListReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("정렬유형")
    @Size(max=5)
    private String sortDirection;                //  정렬유형

    @ApiModelProperty("정렬항목")
    @Size(max=50)
    private String sortItem;                     //  정렬항목 

    @ApiModelProperty("정렬테이블")
    @Size(max=50)
    private String sortIndicator;                //  정렬테이블

}

