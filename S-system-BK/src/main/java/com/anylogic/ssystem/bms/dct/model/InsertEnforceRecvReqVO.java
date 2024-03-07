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
public class InsertEnforceRecvReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_enf_recv
    @ApiModelProperty("비밀 ID") private String docid;                       // 비밀 ID
    @ApiModelProperty("이전 수신부서이름") private String recvdeptnameOld;   // 이전 수신부서이름
    @ApiModelProperty("경유부서이름") private String viadeptname;            // 경유부서이름
    @ApiModelProperty("수신부서이름") private String recvdeptname;           // 수신부서이름

    @ApiModelProperty("배포부서이름") private String distsdeptname;          // 배포부서이름
    @ApiModelProperty("배포여부") private String distctrlflag;               // 배포여부
    @ApiModelProperty("발송이력 ID") private String sendhisid;               // 발송이력 ID

    @ApiModelProperty("") private List<InsertEnforceRecvReqVO> recvlist;   //시행정보 - 수신자 리스트                       //
    @ApiModelProperty("") private String recvrefdeptname;  //수신부서내 팀이름
    @ApiModelProperty("") private String recvseq; //수신순서
    @ApiModelProperty("") private String receiverid; //수신부서id
    @ApiModelProperty("가상그룹아이디")
    private String vgroupid;                                 // 사용자아이디

    @ApiModelProperty("사용자아이디")
    private String userid;                                   // 사용자아이디

    @ApiModelProperty("부서아이디")
    private String orgid;                                    // 부서아이디

    @ApiModelProperty("가상그룹유형")
    private String vgrouptype;                               // 사용자아이디

    @ApiModelProperty("정렬유형")
    @Size(max=5)
    private String sortDirection;                //  정렬유형

    @ApiModelProperty("정렬항목")
    @Size(max=50)
    private String sortItem;                     //  정렬항목

    @ApiModelProperty("정렬테이블")
    @Size(max=50)
    private String sortIndicator;                //  정렬테이블

    @ApiModelProperty("") private String apprrecvdeptname;  //
    @ApiModelProperty("") private String apprrecvrefdeptname; //
    @ApiModelProperty("") private String apprrecvseq; //

    @ApiModelProperty("") private String deptname;
    @ApiModelProperty("") private String seq;
    @ApiModelProperty("") private String refdeptname;
    @ApiModelProperty("") private String inid;

    @ApiModelProperty("") private String gubun;




}

