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

import java.lang.String;

import java.io.Serializable;

import lombok.*;

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
public class UpdateCardBIAReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_rdoc
    @ApiModelProperty("비밀I D") private String docid;                            // 비밀I D
    @ApiModelProperty("비밀명") private String docttl;                            // 비밀명
    @ApiModelProperty("구분") private String gubun;                               // 구분
    @ApiModelProperty("키워드") private String keyword;                           // 키워드
    @ApiModelProperty("담당자 ID") private String authorid;                       // 담당자 ID
    @ApiModelProperty("담당자 이름") private String authorname;                   // 담당자 이름
    @ApiModelProperty("담당자직위직급") private String authordutyname;            // 담당자직위직급
    @ApiModelProperty("담당자직위 ID") private String authorpositionid;           // 담당자직위 ID
    @ApiModelProperty("담당자직급 ID") private String authorgradeid;              // 담당자직급 ID
    @ApiModelProperty("담당자부서 ID") private String authordeptid;               // 담당자부서 ID
    @ApiModelProperty("담당자부서이름") private String authordeptname;            // 담당자부서이름
    @ApiModelProperty("담당자부서이름상세") private String authordeptnamedesc;    // 담당자부서이름상세
    @ApiModelProperty("최종작성자 ID") private String lastauthorid;               // 최종작성자 ID
    @ApiModelProperty("최종작성자 이름") private String lastauthorname;           // 최종작성자 이름
    @ApiModelProperty("상황") private String state;                               // 상황
    @ApiModelProperty("비밀일련번호") private BigDecimal docnoseq;          // 비밀일련번호
    @ApiModelProperty("공개") private String open;                                // 공개
    @ApiModelProperty("공개근거") private String openbasis;                       // 공개근거
    @ApiModelProperty("공개사유") private String openreason;                      // 공개사유
    @ApiModelProperty("열람범위유형") private String readrangetype;               // 열람범위유형
    @ApiModelProperty("열람제한유형") private String readlimittype;               // 열람제한유형
    @ApiModelProperty("열람제한일자") private String readlimitdate;               // 열람제한일자
    @ApiModelProperty("삭제FLAG") private String delflag;                         // 삭제FLAG
    @ApiModelProperty("임시FLAG") private String tempflag;                        // 임시FLAG
    @ApiModelProperty("등록일") private String indt;                              // 등록일
    @ApiModelProperty("보고일자") private String reportdt;                        // 보고일자
    @ApiModelProperty("종료일") private String enddt;                             // 종료일
    @ApiModelProperty("수정일") private String updt;                              // 수정일
    @ApiModelProperty("수정자 ID") private String upid;                           // 수정자 ID
    @ApiModelProperty("비밀출처") private String docfrom;                         // 비밀출처
    @ApiModelProperty("인계FLAG") private String moveflag;                        // 인계FLAG
    @ApiModelProperty("수신FLAG") private String rcvsflag;                        // 수신FLAG
    @ApiModelProperty("이관FLAG") private String transflag;                       // 이관FLAG
    @ApiModelProperty("유형FLAG") private String typeflag;                        // 유형FLAG
    @ApiModelProperty("비밀보안FLAG") private String protectflag;                 // 비밀보안FLAG
    @ApiModelProperty("설정FLAG") private String setupflag;                       // 설정FLAG
    @ApiModelProperty("원 비밀 FLAG") private String oridocid;                    // 원 비밀 FLAG
    @ApiModelProperty("원 일련번호") private BigDecimal oriseq;             // 원 일련번호
    @ApiModelProperty("일괄처리 일련번호") private BigDecimal batchseq;     // 일괄처리 일련번호
    @ApiModelProperty("종이FLAG") private String paperflag;                       // 종이FLAG
    @ApiModelProperty("본문FLAG") private String maindocflag;                     // 본문FLAG
    @ApiModelProperty("첨부일련번호") private BigDecimal attachseq;         // 첨부일련번호
    @ApiModelProperty("시행구분") private String enfgubun;                        // 시행구분
    @ApiModelProperty("긴급도") private String emergency;                         // 긴급도
    @ApiModelProperty("DRM구분") private String drmgubun;                         // DRM구분
    @ApiModelProperty("기록물등록구분") private String paperrecgubun;             // 기록물등록구분
    @ApiModelProperty("특수기록물") private String paperrecspecial;               // 특수기록물
    @ApiModelProperty("기록물형태") private String paperrectypes;                 // 기록물형태
    @ApiModelProperty("마이그레이션여부") private String migflag;                 // 마이그레이션여부
    @ApiModelProperty("추가발송상태") private String addsendstate;                // 추가발송상태
    @ApiModelProperty("추가발송횟수") private BigDecimal addsendcnt;        // 추가발송횟수
    @ApiModelProperty("보고서종류") private String docgroup;                      // 보고서종류
    @ApiModelProperty("가제목") private String faketitle;                         // 가제목
    @ApiModelProperty("") private String papersendflag;                           //
    @ApiModelProperty("") private String filelendflag;                            //

    @ApiModelProperty("발송의견") private String sendopinion;                            //발송의견
    @ApiModelProperty("비밀등급") private String seclevel;                  // seclevel ( 비밀등급 )
    // bms_dct_summary table
    @ApiModelProperty("") private String docgubun;                   // docgubun        ( 비밀구분 )
    @ApiModelProperty("") private String summary;                    // summary         ( 문서취지 )

    @ApiModelProperty("로그인유저아이디") private String loginuserid;                  //loginuserid
    @ApiModelProperty("로그인유저이름") private String loginusername;                  //loginusername

    // bms_dct_attr table
    @ApiModelProperty("") private String secgubun;                  // secgubun ( 비밀구분 )
//    @ApiModelProperty("") private String seclevel;                  // seclevel ( 비밀등급 )
    @ApiModelProperty("") private String prsrvterm;                 // prsrvterm ( 보존기간 )
    @ApiModelProperty("") private String orgnprotdt;                // orgnprotdt ( 원본보호기간 )
    @ApiModelProperty("") private String orgnnextlevel;             // orgnnextlevel ( 원본차기비밀등급 )
    @ApiModelProperty("") private String orgnreclass;               // orgnreclass ( 원본재분류 )
    @ApiModelProperty("") private String copyprotdt;                // copyprotdt ( 사본보호기간 )
    @ApiModelProperty("") private String copyreclass;               // copyreclass ( 사본재분류 )
    @ApiModelProperty("") private String copynextlevel;             // copynextlevel ( 사본차기비밀등급 )
    @ApiModelProperty("") private String mgmtno;                    // 관리번호
    @ApiModelProperty("") private String docno;                    // 문서번호
    @ApiModelProperty("") private String copyno;                    // 사본번호



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

    @ApiModelProperty("") private List<InsertEnforceRecvReqVO> recvlist;                           //수신리스트
    @ApiModelProperty("") private String recvrefdeptname;  //수신부서내 팀이름
    @ApiModelProperty("") private String recvseq; //수신순서
    @ApiModelProperty("") private String receiverid; //수신부서id
    @ApiModelProperty("") private String apprrecvdeptname;  //
    @ApiModelProperty("") private String apprrecvrefdeptname; //
    @ApiModelProperty("") private String apprrecvseq; //
    @ApiModelProperty("") private String distdeptname;              // distdeptname ( 발신기관명 )
    @ApiModelProperty("") private String distname;              // distname ( 발신명의 )

    // bms_dct_opinion
    @ApiModelProperty("") private String opinionpathorder;          // pathorder ( 경로순서 )
    @ApiModelProperty("") private String opinion;                   // opinion ( 의견 )

//    @ApiModelProperty("") private String deptname;
    @ApiModelProperty("") private String seq;
    @ApiModelProperty("") private String refdeptname;
    @ApiModelProperty("") private String inid;
    @ApiModelProperty("") private List<UpdateCardBIAReqVO> takeoverList;                           //
    @ApiModelProperty("") private List<UpdateCardBIAReqVO> selectList;

    @ApiModelProperty("") private List<InsertSendRecvReqVO> rcvsList; //관리정보 - 열람권자 리스트
    @ApiModelProperty("") private String rcvsdeptid;
    @ApiModelProperty("") private String rcvdeptname;
    @ApiModelProperty("") private String rcvdutyname;
    @ApiModelProperty("") private String rcvsdeptnamedesc;
    @ApiModelProperty("") private String rcvsinseq;
    @ApiModelProperty("") private String rcvsuserid;
    @ApiModelProperty("") private String rcvsusername;

    // bms_dct_appr_recv
    @ApiModelProperty("") private List<InsertTempListReqVO> recvMultiList;   // 수신지정 다건처리 용도
//    @ApiModelProperty("") private String receiverid;                    // receiverid ( 수신처ID )
    @ApiModelProperty("") private String apprrecvtempflag;              // tempflag ( 임시FLAG )
//    @ApiModelProperty("") private String apprrecvseq;                   // seq ( 일련번호 )
    @ApiModelProperty("") private String apprrecvgubun;                 // gubun ( 구분 )
//    @ApiModelProperty("") private String apprrecvdeptname;              // deptname ( 수신부서명 )
//    @ApiModelProperty("") private String apprrecvrefdeptname;          // refdeptname ( 참조부서이름 )

    // bms_dct_rdoc_rcvs
    @ApiModelProperty("") private String rcvdeptid;              // deptid ( 부서ID )

    @ApiModelProperty("") private List<UpdateCardBIAReqVO> getUserLoginData;
    @ApiModelProperty("") private String username;

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
//    @ApiModelProperty("사용자명") private String username;          // 사용자명
    @ApiModelProperty("이력상태") private String histstatus;        // 이력상태
    @ApiModelProperty("기타") private String etc;                   // 기타
    @ApiModelProperty("") private String recvdeptname;

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

    //com_vgroup_user
    @ApiModelProperty("가상그룹아이디") private String vgroupid;           // 사용자아이디
//    @ApiModelProperty("사용자아이디") private String userid;              // 사용자아이디
    @ApiModelProperty("부서아이디") private String orgid;                // 부서아이디
    @ApiModelProperty("가상그룹유형") private String vgrouptype;           // 사용자아이디
//    @ApiModelProperty("등록자아이디") private String inid;                // 등록자아이디
//    @ApiModelProperty("등록일시") private String indt;                    // 등록일시
//    @ApiModelProperty("수정자아이디") private String upid;                // 수정자아이디
//    @ApiModelProperty("수정일시") private String updt;                    // 수정일시

    //bms_dct_enf_history
    @ApiModelProperty("처리구분") private String actgubun;           // 처리구분

    // 재발송 구분자
    @ApiModelProperty("") private String isresend;
    // 재발송시 발송안함 구분자
    @ApiModelProperty("") private String isNotSend;
    // 사본에 대한 정보
    @ApiModelProperty("") private List<UpdateCardBIAReqVO> copyparams;
    @ApiModelProperty("") private String copyOrder;

    //bms_dct_relay_his
    @ApiModelProperty("") private String extype;
    @ApiModelProperty("") private int retry;
    @ApiModelProperty("") private String exstatus;

    // 기안문 저장용 url
    @ApiModelProperty("") private String drafturl;
    @ApiModelProperty("") private String draftdocttl;

    // bms_dct_enf_doc
    @ApiModelProperty("시행비밀 ID") private String enfdocid;                            // 시행비밀 ID

    //발송형태 구분자 (offLineSend)
    @ApiModelProperty("") private String judgeParam;

    @ApiModelProperty("") private String sendOrgNameData;                           //발신기관명
    @ApiModelProperty("") private String sendNameData;                              //발신기관명의
    @ApiModelProperty("") private String proddocnum;                                //생산문서번호

    @ApiModelProperty("비밀I D") private String resdocid;                            // 비밀I D
}

