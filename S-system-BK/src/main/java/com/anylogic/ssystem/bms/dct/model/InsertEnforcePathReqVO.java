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
import java.math.BigDecimal;
import java.util.List;

import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class InsertEnforcePathReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_enf_path
    @ApiModelProperty("시행비밀 ID") private String enfdocid;         // 시행비밀 ID
    @ApiModelProperty("처리일련번호") private BigDecimal actseq;// 처리일련번호
    @ApiModelProperty("처리자 ID") private String acterid;            // 처리자 ID
    @ApiModelProperty("처리자 이름") private String actername;        // 처리자 이름
    @ApiModelProperty("처리자 구분") private String actergubun;       // 처리자 구분
    @ApiModelProperty("부서 ID") private String deptid;               // 부서 ID
    @ApiModelProperty("부서 이름") private String deptname;           // 부서 이름
    @ApiModelProperty("부서 이름 상세") private String deptnamedesc;  // 부서 이름 상세
    @ApiModelProperty("직위직급 이름") private String dutyname;       // 직위직급 이름
    @ApiModelProperty("직위 ID") private String positionid;           // 직위 ID
    @ApiModelProperty("직급 ID") private String gradeid;              // 직급 ID
    @ApiModelProperty("등록일") private String indt;                  // 등록일
    @ApiModelProperty("등록일자 ID") private String inid;             // 등록일자 ID
    @ApiModelProperty("처리일") private String actdt;                 // 처리일
    @ApiModelProperty("열람일시") private String readdt;              // 열람일시
    @ApiModelProperty("중요여부") private String starred;             // 중요여부
    @ApiModelProperty("접수일") private String acceptdt;              // 접수일
    @ApiModelProperty("수정자 ID") private String upid;

    @ApiModelProperty("") private List<InsertEnforcePathReqVO> chargerList;   // 담당자 다건처리 용도
    @ApiModelProperty("") private String chargerorder;                 // chargerorder ( 구분 : 담당순서 )
    @ApiModelProperty("") private String chargerstate;                 // chargerstate ( 상황 )
    @ApiModelProperty("") private String actopinion;
    @ApiModelProperty("") private String askTemp;                    // asktemp

    // bms_dct_path
    @ApiModelProperty("") private List<InsertEnforcePathReqVO> pathList;   // 경로정보 다건처리 용도
    @ApiModelProperty("") private String pathorder;                 // pathorder ( 구분 : 경로순서 )
    @ApiModelProperty("") private String pathstate;                 // state ( 상황 )
    @ApiModelProperty("") private String asktype;                   // asktype ( 요청유형 )
    @ApiModelProperty("") private String acttype;                   // acttype ( 처리유형 )
    @ApiModelProperty("") private String objid;                    // objid ( 객체ID )
    @ApiModelProperty("") private String objname;                    // objname ( 객체이름 )
//    @ApiModelProperty("") private String deptid;                    // deptid ( 부서ID )
//    @ApiModelProperty("") private String deptname;                  // deptname ( 부서이름 )
//    @ApiModelProperty("") private String dutyname;                  // dutyname ( 직위직급이름 )
    @ApiModelProperty("") private String dutynamedesc;                // dutyname ( 직위직급이름 상세)
    @ApiModelProperty("") private int mainver;                   // mainver ( 메인버전)
    @ApiModelProperty("") private String actapprdt;                   // actapprdt ( 처리경로시간)
    @ApiModelProperty("") private String electsignid;               // electsignid ( 전자서명ID )

    @ApiModelProperty("시행비밀 ID") private String docid;         // 비밀 ID
    @ApiModelProperty("긴급도") private String emergency;
    @ApiModelProperty("비밀제목") private String doctitle;
    @ApiModelProperty("비밀상태") private String docstatus;
    @ApiModelProperty("키워드") private String keyword;
    @ApiModelProperty("state") private String state;
    @ApiModelProperty("담당자 이름") private String recname;
    @ApiModelProperty("") private String actgubun;

    @ApiModelProperty("") private BigDecimal acceptdocseq;
    @ApiModelProperty("") private String acceptdeptid;
    @ApiModelProperty("") private String acceptdeptname;
    @ApiModelProperty("") private String acceptid;
    @ApiModelProperty("") private String acceptname;
    @ApiModelProperty("") private String acceptdutyname;

    @ApiModelProperty("") private String rcvsuserid;
    @ApiModelProperty("") private String rcvsusername;
    @ApiModelProperty("") private int rcvsinseq;
    @ApiModelProperty("") private String rcvdeptid;
    @ApiModelProperty("") private String rcvdeptname;
    @ApiModelProperty("") private String rcvdutyname;

    @ApiModelProperty("") private String invrcpttype;
}

