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
public class SelectUseTakeOutResVO extends AnyXCommonVO implements Serializable {

    @ApiModelProperty("비밀관리 ID") private String mgmtid;
    @ApiModelProperty("관리번호") private String mgmtno;
    @ApiModelProperty("비밀문서 ID") private String docid;
    @ApiModelProperty("") private String regirecvtype;
    @ApiModelProperty("") private String indt;
    @ApiModelProperty("") private String secttl;
    @ApiModelProperty("") private String copyno;
    @ApiModelProperty("") private String authorid;
    @ApiModelProperty("") private String authorname;
    @ApiModelProperty("") private String deptname;
    @ApiModelProperty("") private String lend;
    @ApiModelProperty("") private String reader;
    @ApiModelProperty("") private String readauthexpiredt;
    @ApiModelProperty("") private String grantbackdt;
    @ApiModelProperty("") private String paperrecgubun;

}
