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
import java.math.BigDecimal;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class InsertTempListReqVO extends AnyXCommonVO implements Serializable {
    // 공통 사용
    @ApiModelProperty("") private String resdocid;                     // docid
    @ApiModelProperty("") private String resuserid;                    // userid
    @ApiModelProperty("") private String resasktype;                    // asktype
    @ApiModelProperty("") private String askTemp;                    // asktemp

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
    @ApiModelProperty("") private String papersendflag;              // papersendflag   (  )
    @ApiModelProperty("") private String filelendflag;               // filelendflag    (  )

    // bms_dct_summary table
    @ApiModelProperty("") private String docgubun;                   // docgubun        ( 비밀구분 )
    @ApiModelProperty("") private String summary;                    // summary         ( 문서취지 )

    // bms_dct_attr table
    @ApiModelProperty("") private String secgubun;                  // secgubun ( 비밀구분 )
    @ApiModelProperty("") private String seclevel;                  // seclevel ( 비밀등급 )
    @ApiModelProperty("") private String prsrvterm;                 // prsrvterm ( 보존기간 )
    @ApiModelProperty("") private String orgnprotdt;                // orgnprotdt ( 원본보호기간 )
    @ApiModelProperty("") private String orgnnextlevel;             // orgnnextlevel ( 원본차기비밀등급 )
    @ApiModelProperty("") private String orgnreclass;               // orgnreclass ( 원본재분류 )
    @ApiModelProperty("") private String copyprotdt;                // copyprotdt ( 사본보호기간 )
    @ApiModelProperty("") private String copyreclass;               // copyreclass ( 사본재분류 )
    @ApiModelProperty("") private String copynextlevel;             // copynextlevel ( 사본차기비밀등급 )
    @ApiModelProperty("") private String docno;

    // bms_dct_path
    @ApiModelProperty("") private List<InsertTempListReqVO> pathList;   // 경로정보 다건처리 용도
    @ApiModelProperty("") private String pathorder;                 // pathorder ( 구분 : 경로순서 )
    @ApiModelProperty("") private String pathstate;                 // state ( 상황 )
    @ApiModelProperty("") private String asktype;                   // asktype ( 요청유형 )
    @ApiModelProperty("") private String acttype;                   // acttype ( 처리유형 )
    @ApiModelProperty("") private String objid;                    // objid ( 객체ID )
    @ApiModelProperty("") private String objname;                    // objname ( 객체이름 )
    @ApiModelProperty("") private String deptid;                    // deptid ( 부서ID )
    @ApiModelProperty("") private String deptname;                  // deptname ( 부서이름 )
    @ApiModelProperty("") private String dutyname;                  // dutyname ( 직위직급이름 )
    @ApiModelProperty("") private String dutynamedesc;                // dutyname ( 직위직급이름 상세)
    @ApiModelProperty("") private int mainver;                   // mainver ( 메인버전)
    @ApiModelProperty("") private String actapprdt;                   // actapprdt ( 처리경로시간)
    @ApiModelProperty("") private String electsignid;               // electsignid ( 전자서명ID )

    // bms_dct_opinion
    @ApiModelProperty("") private String opinionpathorder;          // pathorder ( 경로순서 )
    @ApiModelProperty("") private String opinion;                   // opinion ( 의견 )

    // bms_dct_appr_recv
    @ApiModelProperty("") private List<InsertTempListReqVO> recvList;   // 수신지정 다건처리 용도
    @ApiModelProperty("") private String receiverid;                    // receiverid ( 수신처ID )
    @ApiModelProperty("") private String apprrecvtempflag;              // tempflag ( 임시FLAG )
    @ApiModelProperty("") private String apprrecvseq;                   // seq ( 일련번호 )
    @ApiModelProperty("") private String apprrecvgubun;                 // gubun ( 구분 )
    @ApiModelProperty("") private String apprrecvdeptname;              // deptname ( 수신부서명 )
    @ApiModelProperty("") private String apprrecvrefdeptname;          // refdeptname ( 참조부서이름 )
    @ApiModelProperty("") private String sendOrgNameData;              // distdeptname ( 발신기관명 )
    @ApiModelProperty("") private String sendNameData;              // distname ( 발신명의 )
    @ApiModelProperty("") private String distdeptname;              // distdeptname ( 발신기관명 )
    @ApiModelProperty("") private String distname;              // distname ( 발신명의 )
    @ApiModelProperty("") private String recsymbol;              // recsymbol ( 수신자기호 )
    @ApiModelProperty("") private String relaytype;              // relaytype ( 유통망종류 )

    // bms_dct_rdoc_rcvs
    @ApiModelProperty("") private List<InsertTempListReqVO> rcvsList;   // 열람지정 다건처리 용도
    @ApiModelProperty("") private String rcvsuserid;                // userid ( 사용자ID )
    @ApiModelProperty("") private String rcvsusername;              // username ( 사용자이름 = 열람권자 )
    @ApiModelProperty("") private String rcvdeptid;              // deptid ( 부서ID )
    @ApiModelProperty("") private String rcvdeptname;              // deptname ( 부서이름 )
    @ApiModelProperty("") private String rcvdutyname;              // dutyname ( 직위직급이름 )
    @ApiModelProperty("") private String rcvsinseq;              // inseq ( 등록일련번호 )

    // bms_task_information_temp
    @ApiModelProperty("") private List<InsertTempListReqVO> refList;   // 관련근거 다건처리 용도
    @ApiModelProperty("") private String infomationid;               // infomationid ( 정보출처 ID )
    @ApiModelProperty("") private String resultid;                   // resultid ( 실적 ID )
    @ApiModelProperty("") private String infotext;                   // infotext ( 정보출처 제목 )
    @ApiModelProperty("") private String attachid;                   // attachid ( 첨부파일 ID )
    @ApiModelProperty("") private String infotype;                   // infotype ( 정보출처유형 )
    @ApiModelProperty("") private String deleteyn;                   // deleteyn ( 삭제여부 )
    @ApiModelProperty("") private String infotitle;                  // infotitle ( 정보출처 제목 )
    @ApiModelProperty("") private String fileexist;                  // 관련근거 첨부파일 여부
    @ApiModelProperty("") private String[] delRef;                   // 관련근거 삭제용

    // bms_dct_hist_card
    @ApiModelProperty("이력 일시") private String histdt;           // 이력 일시
    @ApiModelProperty("이력 순번") private BigDecimal histseq;      // 이력 순번
    @ApiModelProperty("결제요청 ID") private String apprreqid;      // 결제요청 ID
    @ApiModelProperty("이력구분") private String histgubun;         // 이력구분
    @ApiModelProperty("사용자 ID") private String userid;           // 사용자 ID
    @ApiModelProperty("사용자명") private String username;          // 사용자명
    @ApiModelProperty("이력상태") private String histstatus;        // 이력상태
    @ApiModelProperty("기타") private String etc;                   // 기타

    // bms_dct_file
    @ApiModelProperty("") private List<InsertTempListReqVO> fileList;   // 첨부파일 다건처리 용도
    @ApiModelProperty("") private List<InsertTempListReqVO> draftfile;   // 본문파일
    @ApiModelProperty("첨부 구분") private String filegubun;         // 파일 구분
    @ApiModelProperty("첨부 ID") private String fleid;            // 첨부 ID
    @ApiModelProperty("파일고유이름") private String sfilename;   // 파일고유이름
    @ApiModelProperty("첨부명") private String flettl;            // 첨부명
    @ApiModelProperty("첨부크기") private BigDecimal flesize;// 첨부크기
    @ApiModelProperty("첨부순서") private BigDecimal fleorder;// 첨부순서
    @ApiModelProperty("비밀등급표시여부") private String markflag;// 비밀등급표시여부
    @ApiModelProperty("기존 문서의 구분") private String origubun;// 기존 문서의 구분
    @ApiModelProperty("비전자 첨부구분") private String isviewtype;// 비전자 첨부구분
    @ApiModelProperty("첨부 url") private String fleurl ;       // 첨부 url
    @ApiModelProperty("첨부 경로") private String flepath;      // 첨부 경로

    @ApiModelProperty("첨부 삭제") private String[] delFileids;      // 기존 첨부파일 삭제 용도

    @ApiModelProperty("") private List<SelectTaskInfoTempReqVO> taskInfoFileList; //관련근거리스트중 첨부파일리스트

    // bms_dct_sndrcv_regi
    @ApiModelProperty("수발신일련번호") private String sndrcvseq;       // 수발신일련번호
    @ApiModelProperty("기관 ID") private String orgid;                  // 기관 ID
    @ApiModelProperty("기관 이름") private String orgname;              // 기관 이름
    @ApiModelProperty("수발신 구분") private String sndrcvgubun;        // 수발신 구분
    @ApiModelProperty("문서부서 ID") private String docnodeptid;        // 문서부서 ID
    @ApiModelProperty("문서부서명") private String docnodeptname;       // 문서부서명
    //    @ApiModelProperty("문서번호") private String docnoseq;              // 문서번호
    @ApiModelProperty("수발문서제목") private String sndrcvttl;         // 수발문서제목
    //    @ApiModelProperty("비밀등급") private String seclevel;              // 비밀등급
    @ApiModelProperty("사본번호") private String copyno;                // 사본번호
    @ApiModelProperty("수량") private BigDecimal qty;             // 수량
    @ApiModelProperty("수발신 일시") private String sndrcvdt;           // 수발신 일시
    @ApiModelProperty("발신자 부서 ID") private String snddeptid;       // 발신자 부서 ID
    @ApiModelProperty("발신자 부서명") private String snddeptname;      // 발신자 부서명
    @ApiModelProperty("발신자 ID") private String snduserid;            // 발신자 ID
    @ApiModelProperty("발신자명") private String sndusername;           // 발신자명
    //    @ApiModelProperty("수신자 부서 ID") private String rcvdeptid;       // 수신자 부서 ID
//    @ApiModelProperty("수신자 부서명") private String rcvdeptname;      // 수신자 부서명
    @ApiModelProperty("수신자 ID") private String rcvuserid;            // 수신자 ID
    @ApiModelProperty("수신자명") private String rcvusername;           // 수신자명
    @ApiModelProperty("등록자 ID") private String rgstid;               // 등록자 ID
    @ApiModelProperty("등록자명") private String rgstname;              // 등록자명
    @ApiModelProperty("등록일시") private String rgstdt;                // 등록일시
    @ApiModelProperty("수정자 ID") private String updtid;               // 수정자 ID
    @ApiModelProperty("수정자명") private String updtname;              // 수정자명
    @ApiModelProperty("수정일시") private String updtdt;                // 수정일시
    @ApiModelProperty("관리번호") private String mgmtno;                // 관리번호
//    @ApiModelProperty("비밀 ID") private String docid;                  // 비밀 ID


    // 기안문
    @ApiModelProperty("") private String drafturl;
    @ApiModelProperty("") private String draftdocttl;

    // 재사용 등록
    @ApiModelProperty("") private String isreuse;

    //접수 문서처리 구분용도
    @ApiModelProperty("") private String judgeParam;

    // 재사용 문서처리 결재/1인결재 구분용도
    @ApiModelProperty("") private String isOneApprove;

    // 비전자문서 접수
    @ApiModelProperty("") private String proddocnum; // 생산문서번호
    @ApiModelProperty("") private String senddate; // 시행일자(발신일)
    @ApiModelProperty("") private String docpagenum; // 자료량(쪽수)
    @ApiModelProperty("") private String doctype; // 자료유형
    @ApiModelProperty("") private String mediatype; // 매체유형
    @ApiModelProperty("") private String locker; // 보관함
    @ApiModelProperty("") private String unitnum; // 단번호

    // 생산 비전자문서 등록
    @ApiModelProperty("") private String mgmtid; // 관리번호

}
