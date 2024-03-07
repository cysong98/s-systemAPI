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
public class InsertRecvInvrcptReqVO extends AnyXCommonVO implements Serializable {
    // 공통 사용
    @ApiModelProperty("") private String resdocid;                     // docid
    @ApiModelProperty("") private String resuserid;                    // userid
    @ApiModelProperty("") private String resasktype;                    // asktype
    @ApiModelProperty("") private String askTemp;                    // asktemp

    // bms_dct_enf_doc
    @ApiModelProperty("시행비밀 ID") private String enfdocid;                     // 시행비밀 ID
    @ApiModelProperty("비밀 ID") private String docid;                            // 비밀 ID
    @ApiModelProperty("시행구분") private String enfgubun;                        // 시행구분
    @ApiModelProperty("유통망 비밀 ID") private String relaydocid;                // 유통망 비밀 ID
    @ApiModelProperty("수신처 ID") private String receiverid;                     // 수신처 ID
    @ApiModelProperty("수신부서 ID") private String recvdeptid;                   // 수신부서 ID
    @ApiModelProperty("비밀제목") private String doctitle;                        // 비밀제목
    @ApiModelProperty("키워드") private String keyword;                           // 키워드
    @ApiModelProperty("비밀상태") private String docstatus;                       // 비밀상태
    @ApiModelProperty("승인비밀번호") private String apprdocnum;                  // 승인비밀번호
    @ApiModelProperty("접수비밀번호 일련번호") private BigDecimal acceptdocseq;// 접수비밀번호 일련번호
    @ApiModelProperty("접수일") private String acceptdt;                          // 접수일
    @ApiModelProperty("접수자 ID") private String acceptid;                       // 접수자 ID
    @ApiModelProperty("접수자 이름") private String acceptname;                   // 접수자 이름
    @ApiModelProperty("접수자 직위직급이름") private String acceptdutyname;       // 접수자 직위직급이름
    @ApiModelProperty("접수자 직위 ID") private String acceptgradeid;             // 접수자 직위 ID
    @ApiModelProperty("접수자 직급 ID") private String acceptpositionid;          // 접수자 직급 ID
    @ApiModelProperty("접수자 부서 ID") private String acceptdeptid;              // 접수자 부서 ID
    @ApiModelProperty("접수자 부서 이름") private String acceptdeptname;          // 접수자 부서 이름
    @ApiModelProperty("접수자 부서이름상세") private String acceptdeptnamedesc;   // 접수자 부서이름상세
    @ApiModelProperty("처리일련번호") private BigDecimal actseq;            // 처리일련번호
    @ApiModelProperty("공개") private String open;                                // 공개
    @ApiModelProperty("공개근거") private String openbasis;                       // 공개근거
    @ApiModelProperty("공개사유") private String openreason;                      // 공개사유
    @ApiModelProperty("열람범위유형") private String readrangetype;               // 열람범위유형
    @ApiModelProperty("수신일") private String recvdt;                            // 수신일
    @ApiModelProperty("배부 FLAG") private String distributeflag;                 // 배부 FLAG
    @ApiModelProperty("과제카드 지정여부") private String setcardflag;            // 과제카드 지정여부
    @ApiModelProperty("공유의견 FLAG") private String shareopnflag;               // 공유의견 FLAG
    @ApiModelProperty("인계 FLAG") private String moveflag;                       // 인계 FLAG
    @ApiModelProperty("종이비밀 FLAG") private String paperdocflag;               // 종이비밀 FLAG
    @ApiModelProperty("종이본문 FLAG") private String paperbodyflag;              // 종이본문 FLAG
    @ApiModelProperty("첨부개수") private BigDecimal attcount;              // 첨부개수
    @ApiModelProperty("삭제 FLAG") private String delflag;                        // 삭제 FLAG
    @ApiModelProperty("유통망 종류") private String relaytype;                    // 유통망 종류
    @ApiModelProperty("시스템 유형") private String systemtype;                   // 시스템 유형
    @ApiModelProperty("본문변환FLAG") private String convbodyflag;                // 본문변환FLAG
    @ApiModelProperty("DRM구분") private String drmgubun;                         // DRM구분
    @ApiModelProperty("최초열람ID") private String firstreadid;                   // 최초열람ID
    @ApiModelProperty("최초열람일") private String firstreaddt;                   // 최초열람일
    @ApiModelProperty("등록자 ID") private String inid;                           // 등록자 ID
    @ApiModelProperty("등록일") private String indt;                              // 등록일
    @ApiModelProperty("수정자 ID") private String upid;                           // 수정자 ID
    @ApiModelProperty("수정일") private String updt;                              // 수정일
    @ApiModelProperty("비밀배부일련번호") private BigDecimal distributedocseq;// 비밀배부일련번호
    @ApiModelProperty("비밀배부기관 ID") private String distributeorgid;          // 비밀배부기관 ID
    @ApiModelProperty("종이비밀결재자 ID") private String paperappid;             // 종이비밀결재자 ID
    @ApiModelProperty("종이비밀결재자 이름") private String paperappname;         // 종이비밀결재자 이름
    @ApiModelProperty("종이비밀결재자 부서이름") private String paperappdeptname; // 종이비밀결재자 부서이름
    @ApiModelProperty("종이비밀결제자 직위직급이름") private String paperappdutyname;// 종이비밀결제자 직위직급이름
    @ApiModelProperty("열람제한유형") private String readlimittype;               // 열람제한유형
    @ApiModelProperty("열람제한일자") private String readlimitdate;               // 열람제한일자
    @ApiModelProperty("비밀보안 FLAG") private String protectflag;                // 비밀보안 FLAG
    @ApiModelProperty("업무담당확인자 ID") private String chargerid;              // 업무담당확인자 ID
    @ApiModelProperty("업무담당확인자 부서 ID") private String chargerdeptid;     // 업무담당확인자 부서 ID
    @ApiModelProperty("최초배부일") private String distributedt;                  // 최초배부일
    @ApiModelProperty("기록물등록구분") private String paperrecgubun;             // 기록물등록구분
    @ApiModelProperty("특수기록물") private String paperrecspecial;               // 특수기록물
    @ApiModelProperty("기록물형태") private String paperrectypes;                 // 기록물형태
    @ApiModelProperty("비밀승인코드") private String apprdocnumcode;              // 비밀승인코드
    @ApiModelProperty("이관여부") private String transflag;                       // 이관여부
    @ApiModelProperty("기록요약") private String recordersummary;                 // 기록요약
    @ApiModelProperty("긴급도") private String emergency;                         // 긴급도
    @ApiModelProperty("추가발송 회수") private BigDecimal addsendcnt;       // 추가발송 회수
    @ApiModelProperty("보고서종류") private String docgroup;                      // 보고서종류
    @ApiModelProperty("가제목") private String faketitle;                         // 가제목

    // bms_dct_invrcpt
    @ApiModelProperty("사본번호") private String copyno;                              // 사본번호
    @ApiModelProperty("송증영수증 제목") private String invrcpttl;                    // 송증영수증 제목
    @ApiModelProperty("비밀등급") private String seclevel;                            // 비밀등급
    @ApiModelProperty("관리번호") private String mgmtno;                              // 관리번호
    @ApiModelProperty("문서번호") private String docno;                               // 문서번호
    @ApiModelProperty("수신부서명") private String recdeptname;                       // 수신부서명
    @ApiModelProperty("참조부서명") private String refdeptname;                       // 참조부서명
    @ApiModelProperty("발신자부처 ID") private String sndminiid;                      // 발신자부처 ID
    @ApiModelProperty("발신자 부처명") private String sndmininame;                    // 발신자 부처명
    @ApiModelProperty("발신자 부서 ID") private String snddeptid;                     // 발신자 부서 ID
    @ApiModelProperty("발신자 부서명") private String snddeptname;                    // 발신자 부서명
    @ApiModelProperty("발신자 ID") private String sndid;                              // 발신자 ID
    @ApiModelProperty("발신자명") private String sndname;                             // 발신자명
    @ApiModelProperty("발신자 직급직위명") private String snddutyname;                // 발신자 직급직위명
    @ApiModelProperty("발신일자") private String snddt;                               // 발신일자
    @ApiModelProperty("발신자전자서명") private String sndelectsignid;                // 발신자전자서명
    @ApiModelProperty("수신사 부처 ID") private String rcvminiid;                     // 수신사 부처 ID
    @ApiModelProperty("수신자 부처명") private String rcvmininame;                    // 수신자 부처명
    @ApiModelProperty("수신자 부서 ID") private String rcvdeptid;                     // 수신자 부서 ID
    @ApiModelProperty("수신자 부서명") private String rcvdeptname;                    // 수신자 부서명
    @ApiModelProperty("수신자 ID") private String rcvid;                              // 수신자 ID
    @ApiModelProperty("수신자명") private String rcvname;                             // 수신자명
    @ApiModelProperty("수신자 직급직위명") private String rcvdutyname;                // 수신자 직급직위명
    @ApiModelProperty("수신일자") private String rcvdt;                               // 수신일자
    @ApiModelProperty("수신자 전자서명") private String rcvelectsignid;               // 수신자 전자서명
    @ApiModelProperty("수발비밀 ID") private String sndrcvdocid;                      // 수발비밀 ID
    @ApiModelProperty("실패사유") private String failreason;                          // 실패사유
    @ApiModelProperty("붙임파일 수량") private BigDecimal qty;                            // 붙임파일 수량
    @ApiModelProperty("송증영수증형태") private String invrcpttype;                   // 송증영수증형태
    @ApiModelProperty("등록자 ID") private String rgstid;                             // 등록자 ID
    @ApiModelProperty("등록자명") private String rgstname;                            // 등록자명
    @ApiModelProperty("등록일자") private String rgstdt;                              // 등록일자
    @ApiModelProperty("수정자 ID") private String updtid;                             // 수정자 ID
    @ApiModelProperty("수정자명") private String updtname;                            // 수정자명
    @ApiModelProperty("수정일자") private String updtdt;                              // 수정일자
    @ApiModelProperty("송증시점확인정보파일 ID") private String sndverifyfileid;      // 송증시점확인정보파일 ID
    @ApiModelProperty("영수증시점확인정보파일 ID") private String rcvverifyfileid;    // 영수증시점확인정보파일 ID

    // 로그인유저 정보
    @ApiModelProperty("") private String authorid;                  // authorid ( 담당자ID )
    @ApiModelProperty("") private String authorname;                // authorname ( 담당자이름 )
    @ApiModelProperty("") private String authordutyname;            // authordutyname ( 담당자직위직급 )
    @ApiModelProperty("") private String authorpositionid;          // authorpositionid ( 담당자직위ID )
    @ApiModelProperty("") private String authorgradeid;             // authorgradeid ( 담당자직급ID )
    @ApiModelProperty("") private String authordeptid;              // authordeptid ( 담당자부서ID )
    @ApiModelProperty("") private String authordeptname;            // authordeptname ( 담당자부서이름 )
    @ApiModelProperty("") private String authordeptnamedesc;        // authordeptnamedesc ( 담당자부서이름상세 )
}

