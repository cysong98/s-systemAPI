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

import java.io.Serializable;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class InsertTaskInfomationtReqVO extends AnyXCommonVO implements Serializable {

    // bms_task_information_temp
    @ApiModelProperty("") private String infomationid;               // infomationid ( 정보출처 ID )
    @ApiModelProperty("") private String resultid;                   // resultid ( 실적 ID )
    @ApiModelProperty("") private String infotext;                   // infotext ( 정보출처 제목 )
    @ApiModelProperty("") private String attachid;                   // attachid ( 첨부파일 ID )
    @ApiModelProperty("") private String infotype;                   // infotype ( 정보출처유형 )
    @ApiModelProperty("") private String deleteyn;                   // deleteyn ( 삭제여부 )
    @ApiModelProperty("") private String infotitle;                  // infotitle ( 정보출처 제목 )
    @ApiModelProperty("") private String inid;                       // inid ( 등록자 ID )
    @ApiModelProperty("") private String indt;                       // indt ( 등록일)

}

