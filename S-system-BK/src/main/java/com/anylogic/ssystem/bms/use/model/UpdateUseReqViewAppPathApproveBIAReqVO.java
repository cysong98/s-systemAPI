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
import java.util.HashMap;
import java.util.List;

import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class UpdateUseReqViewAppPathApproveBIAReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_apprreq_path
    @ApiModelProperty("요청 ID") private String apprreqid;            // 요청 ID
    @ApiModelProperty("요청경로순번") private int apprreqseq;// 요청경로순번
    @ApiModelProperty("결재유형") private String apprtype;            // 결재유형
    @ApiModelProperty("결재처리구분") private String apprgubun;       // 결재처리구분
    @ApiModelProperty("결재요청상태") private String apprstatus;      // 결재요청상태
    @ApiModelProperty("결재자 ID") private String apprid;             // 결재자 ID
    @ApiModelProperty("결재자명") private String apprname;            // 결재자명
    @ApiModelProperty("결재자부서 ID") private String apprdeptid;     // 결재자부서 ID
    @ApiModelProperty("결재자 부서명") private String apprdeptname;   // 결재자 부서명
    @ApiModelProperty("결재자 직위직급명") private String apprdutyname;// 결재자 직위직급명
    @ApiModelProperty("결재자 직위 ID") private String apprposid;     // 결재자 직위 ID
    @ApiModelProperty("결재자 직급 ID") private String apprgrdid;     // 결재자 직급 ID
    @ApiModelProperty("결재자 의견") private String appropinion;      // 결재자 의견
    @ApiModelProperty("전자서명") private String electsign;           // 전자서명
    @ApiModelProperty("등록자 ID") private String rgstid;             // 등록자 ID
    @ApiModelProperty("등록자명") private String rgstname;            // 등록자명
    @ApiModelProperty("등록일시") private String rgstdt;              // 등록일시
    @ApiModelProperty("수정자 ID") private String updtid;             // 수정자 ID
    @ApiModelProperty("수정자명") private String updtname;            // 수정자명
    @ApiModelProperty("수정일시") private String updtdt;              // 수정일시

    @ApiModelProperty("승인여부") private String grantflag;
    @ApiModelProperty("요청타입") private String reqtype;
    @ApiModelProperty("리스트") private List<UpdateUseReqViewApproveReqVO> list;
    @ApiModelProperty("리스트") private List<HashMap<String, Object>> maplist;

    @ApiModelProperty("반출구분") private String lendgubun;
    @ApiModelProperty("반출형태") private String lendtype;
    @ApiModelProperty("요청반출일") private String reqlenddt;
    @ApiModelProperty("요청반입예정일") private String reqbackdt;
    @ApiModelProperty("승인반출일") private String grantlenddt;
    @ApiModelProperty("승인반입예정일") private String grantbackdt;

    @ApiModelProperty("승인비밀등급") private String grantseclevel;
    @ApiModelProperty("승인예고문보호기간") private String grantnotiprotterm;
    @ApiModelProperty("승인예고문비밀등급") private String grantnotiprotlevel;
    @ApiModelProperty("승인예고문처리") private String grantnotiact;
    @ApiModelProperty("승인예고문보존기간") private String grantnotiprsrvterm;

    @ApiModelProperty("요청자ID") private String reqid;

}

