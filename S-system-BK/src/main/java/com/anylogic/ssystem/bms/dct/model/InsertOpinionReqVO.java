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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class InsertOpinionReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_opinion
    @ApiModelProperty(value = "")
    @Size(max=35)
    @NotEmpty
    private String docid;                        //

    @ApiModelProperty(value = "")
    @NotEmpty
    private BigDecimal pathorder;                    //

    @ApiModelProperty(value = "")
    @Size(max=4000)
    private String opinion;                      //

}

