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
public class DocumentProcBIAReqVO extends AnyXCommonVO implements Serializable {
    // 공통 사용
    @ApiModelProperty("") private String resdocid;                     // docid
    @ApiModelProperty("") private String resuserid;                    // userid
    // bms_dct_enf_history
    @ApiModelProperty("시행비밀 ID") private String enfdocid;                   // 시행비밀 ID
    @ApiModelProperty("처리일련번호") private BigDecimal actseq;          // 처리일련번호
    @ApiModelProperty("처리자 ID") private String acterid;                      // 처리자 ID
    @ApiModelProperty("처리자 이름") private String actername;                  // 처리자 이름
    @ApiModelProperty("처리자 직위직급이름") private String acterdutyname;      // 처리자 직위직급이름
    @ApiModelProperty("처리자 직위 ID") private String acterpositionid;         // 처리자 직위 ID
    @ApiModelProperty("처리자 직급 ID") private String actergradeid;            // 처리자 직급 ID
    @ApiModelProperty("처리자 부서 ID") private String acterdeptid;             // 처리자 부서 ID
    @ApiModelProperty("처리자 부서 이름") private String acterdeptname;         // 처리자 부서 이름
    @ApiModelProperty("처리자 부서 이름 상세") private String acterdeptnamedesc;// 처리자 부서 이름 상세
    @ApiModelProperty("처리구분") private String actgubun;                      // 처리구분
    @ApiModelProperty("처리일") private String actdt;                           // 처리일
    @ApiModelProperty("처리의견") private String actopinion;                    // 처리의견
    @ApiModelProperty("기록관 ID") private String recid;                        // 기록관 ID
    @ApiModelProperty("수신자 이름") private String recname;                    // 수신자 이름
    @ApiModelProperty("수신자 직위직급") private String recdutyname;            // 수신자 직위직급
    @ApiModelProperty("수신자 직위 ID") private String recpositionid;           // 수신자 직위 ID
    @ApiModelProperty("수신자 직급 ID") private String recgradeid;              // 수신자 직급 ID
    @ApiModelProperty("수신자 부서 ID") private String recdeptid;               // 수신자 부서 ID
    @ApiModelProperty("수신자 부서 이름") private String recdeptname;           // 수신자 부서 이름
    @ApiModelProperty("수신자 부서 상세이름") private String recdeptnamedesc;   // 수신자 부서 상세이름
    @ApiModelProperty("처리경로이력 ID") private String pathhisid;              // 처리경로이력 ID
    @ApiModelProperty("전자서명 ID") private String electsignid;                // 전자서명 ID

    // bms_dct_rdoc table
    @ApiModelProperty("") private String docid;                     // docid ( 비밀문서 ID )
    @ApiModelProperty("") private String docttl;                    // docttl ( 비밀문서 제목(비밀문서명) )
    @ApiModelProperty("") private String gubun;                     // gubun ( 구분 (DOPT1: 내부결재용, DOPT2 : 시행용(사본)) )
    @ApiModelProperty("") private String keyword;                   // keyword ( 키워드 )
    @ApiModelProperty("") private String authorid;                  // authorid ( 담당자ID )
    @ApiModelProperty("") private String authorname;                // authorname ( 담당자이름 )
    @ApiModelProperty("") private String authordutyname;            // authordutyname ( 담당자직위직급 )
    @ApiModelProperty("") private String authorpositionid;          // authorpositionid ( 담당자직위ID )
    @ApiModelProperty("") private String authorgradeid;             // authorgradeid ( 담당자직급ID )
    @ApiModelProperty("") private String authordeptid;              // authordeptid ( 담당자부서ID )
    @ApiModelProperty("") private String authordeptname;            // authordeptname ( 담당자부서이름 )
    @ApiModelProperty("") private String authordeptnamedesc;        // authordeptnamedesc ( 담당자부서이름상세 )
    @ApiModelProperty("") private String lastauthorid;              // lastauthorid ( 최종작성자ID )
    @ApiModelProperty("") private String lastauthorname;            // lastauthorname ( 최종작성자이름 )
    @ApiModelProperty("") private String state;                     // state ( 상황 )
    @ApiModelProperty("") private String docnoseq;                  // docnoseq ( 비밀일련번호 )
    @ApiModelProperty("") private String open;                      // open ( 공개 )
    @ApiModelProperty("") private String openbasis;                 // openbasis ( 공개근거 )
    @ApiModelProperty("") private String openreason;                // openreason ( 공개사유 )
    @ApiModelProperty("") private String readrangetype;             // readrangetype ( 열람범위유형 )
    @ApiModelProperty("") private String readlimittype;             // readlimittype ( 열람제한유형 )
    @ApiModelProperty("") private String readlimitdate;             // readlimitdate ( 열람제한일자 )
    @ApiModelProperty("") private String delflag;                   // delflag ( 삭제FLAG )
    @ApiModelProperty("") private String tempflag;                  // tempflag ( 임시FLAG )
    @ApiModelProperty("") private String inid;                      // inid ( 등록자ID )
    @ApiModelProperty("") private String indt;                      // indt ( 등록일 )
    @ApiModelProperty("") private String reportdt;                  // reportdt ( 보고일자 )
    @ApiModelProperty("") private String enddt;                     // enddt ( 종료일 )
    @ApiModelProperty("") private String upid;                      // upid ( 수정자ID )
    @ApiModelProperty("") private String updt;                      // updt ( 수정일 )
    @ApiModelProperty("") private String docfrom;                   // docfrom ( 비밀출처 )
    @ApiModelProperty("") private String moveflag;                   // moveflag ( 인계FLAG )
    @ApiModelProperty("") private String rcvsflag;                   // rcvsflag ( 수신FLAG )
    @ApiModelProperty("") private String transflag;                  // transflag ( 이관FLAG )
    @ApiModelProperty("") private String typeflag;                   // typeflag ( 유형FLAG )
    @ApiModelProperty("") private String protectflag;                // protectflag ( 비밀보안FLAG )
    @ApiModelProperty("") private String setupflag;                  // setupflag ( 설정FLAG )
    @ApiModelProperty("") private String oridocid;                   // oridocid ( 원 비밀ID )
    @ApiModelProperty("") private String oriseq;                     // oriseq          ( 원 일련번호 )
    @ApiModelProperty("") private String batchseq;                   // batchseq        ( 일괄처리일련번호 )
    @ApiModelProperty("") private String paperflag;                  // paperflag       ( 종이FLAG )
    @ApiModelProperty("") private String maindocflag;                // maindocflag     ( 본문FLAG )
    @ApiModelProperty("") private String attachseq;                  // attachseq       ( 첨부일련번호 )
    @ApiModelProperty("") private String enfgubun;                   // enfgubun        ( 시행구분 )
    @ApiModelProperty("") private String emergency;                  // emergency       ( 긴급도 )
    @ApiModelProperty("") private String drmgubun;                   // drmgubun        ( DRM구분 )
    @ApiModelProperty("") private String paperrecgubun;              // paperrecgubun   ( 기록물등록구분 )
    @ApiModelProperty("") private String paperrecspecial;            // paperrecspecial ( 특수기록물 )
    @ApiModelProperty("") private String paperrectypes;              // paperrectypes   ( 기록물형태 )
    @ApiModelProperty("") private String migflag;                    // migflag         ( 마이그레이션여부 )
    @ApiModelProperty("") private String addsendstate;               // addsendstate    ( 추가발송상태 )
    @ApiModelProperty("") private String addsendcnt;                 // addsendcnt      ( 추가발송횟수 )
    @ApiModelProperty("") private String docgroup;                   // docgroup        ( 보고서종류(외교형 ) )
    @ApiModelProperty("") private String faketitle;                  // faketitle       ( 가제목 )

    // bms_dct_path
    @ApiModelProperty("") private List<InsertTempListReqVO> pathList;   // 경로정보 다건처리 용도
    @ApiModelProperty("") private String pathorder;                 // pathorder ( 구분 : 경로순서 )
    @ApiModelProperty("") private String pathstate;                 // state ( 상황 )
    @ApiModelProperty("") private String asktype;                   // asktype ( 요청유형 )
    @ApiModelProperty("") private String deptid;                    // deptid ( 부서ID )
    @ApiModelProperty("") private String deptname;                  // deptname ( 부서이름 )
    @ApiModelProperty("") private String dutyname;                  // dutyname ( 직위직급이름 )
    @ApiModelProperty("") private String dutynamedesc;                // dutyname ( 직위직급이름 상세)
    @ApiModelProperty("") private int mainver;                   // mainver ( 메인버전)

    @ApiModelProperty("로그인유저아이디") private String loginuserid;                  //loginuserid
    @ApiModelProperty("로그인유저이름") private String loginusername;                  //loginusername

    @ApiModelProperty("") private List<InsertEnforcePathReqVO> chargerList;   // 담당자 다건처리 용도
    @ApiModelProperty("비밀상태") private String docstatus;

    // bms_dct_rdoc_rcvs
    @ApiModelProperty("열람리스트") private List<InsertTempListReqVO> RcvsList;
    @ApiModelProperty("사용자 ID") private String userid;             // 사용자 ID
    @ApiModelProperty("사용자 이름") private String username;         // 사용자 이름
    @ApiModelProperty("직위 ID") private String positionid;           // 직위 ID
    @ApiModelProperty("직급 ID") private String gradeid;              // 직급 ID
    @ApiModelProperty("부서이름상세") private String deptnamedesc;    // 부서이름상세
    @ApiModelProperty("열람여부") private String isread;              // 열람여부
    @ApiModelProperty("열람일시") private String readdt;              // 열람일시
    @ApiModelProperty("등록일련번호") private String inseq;           // 등록일련번호
    @ApiModelProperty("유형") private String type;                    // 유형
    @ApiModelProperty("접수일") private String acceptdt;              // 접수일
    @ApiModelProperty("") private String begindt;                     //
    @ApiModelProperty("") private String reason;                      //
    @ApiModelProperty("") private String rcvsuserid;

    // bms_dct_hist_card 이력카드
    @ApiModelProperty("") private String apprreqid;
    @ApiModelProperty("") private String histgubun;
    @ApiModelProperty("") private String histstatus;
    @ApiModelProperty("") private String etc;
}

