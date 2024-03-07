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

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;
import java.lang.String;

import java.io.Serializable;
import java.math.BigDecimal;

import com.anylogic.ssystem.common.model.AnyXCommonVO;

@ApiModel("selectSendBackReadyList")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectSendBackReadyListResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("시행비밀 ID") private String enfdocid;                            // 시행비밀 ID
    @ApiModelProperty("비밀 ID") private String docid;                                   // 비밀 ID
    @ApiModelProperty("시행구분") private String enfgubun;                               // 시행구분
    @ApiModelProperty("유통망 비밀 ID") private String relaydocid;                       // 유통망 비밀 ID
    @ApiModelProperty("수신처 ID") private String receiverid;                            // 수신처 ID
    @ApiModelProperty("수신부서 ID") private String recvdeptid;                          // 수신부서 ID
    @ApiModelProperty("비밀제목") private String doctitle;                               // 비밀제목
    @ApiModelProperty("키워드") private String keyword;                                  // 키워드
    @ApiModelProperty("비밀상태") private String docstatus;                              // 비밀상태
    @ApiModelProperty("승인비밀번호") private String apprdocnum;                         // 승인비밀번호
    @ApiModelProperty("접수비밀번호 일련번호") private BigDecimal acceptdocseq;              // 접수비밀번호 일련번호
    @ApiModelProperty("접수일") private String acceptdt;                                 // 접수일
    @ApiModelProperty("접수자 ID") private String acceptid;                              // 접수자 ID
    @ApiModelProperty("접수자 이름") private String acceptname;                          // 접수자 이름
    @ApiModelProperty("접수자 직위직급이름") private String acceptdutyname;              // 접수자 직위직급이름
    @ApiModelProperty("접수자 직위 ID") private String acceptgradeid;                    // 접수자 직위 ID
    @ApiModelProperty("접수자 직급 ID") private String acceptpositionid;                 // 접수자 직급 ID
    @ApiModelProperty("접수자 부서 ID") private String acceptdeptid;                     // 접수자 부서 ID
    @ApiModelProperty("접수자 부서 이름") private String acceptdeptname;                 // 접수자 부서 이름
    @ApiModelProperty("접수자 부서이름상세") private String acceptdeptnamedesc;          // 접수자 부서이름상세
    @ApiModelProperty("처리일련번호") private BigDecimal actseq;                             // 처리일련번호
    @ApiModelProperty("공개") private String open;                                       // 공개
    @ApiModelProperty("공개근거") private String openbasis;                              // 공개근거
    @ApiModelProperty("공개사유") private String openreason;                             // 공개사유
    @ApiModelProperty("열람범위유형") private String readrangetype;                      // 열람범위유형
    @ApiModelProperty("수신일") private String recvdt;                                   // 수신일
    @ApiModelProperty("배부 FLAG") private String distributeflag;                        // 배부 FLAG
    @ApiModelProperty("과제카드 지정여부") private String setcardflag;                   // 과제카드 지정여부
    @ApiModelProperty("공유의견 FLAG") private String shareopnflag;                      // 공유의견 FLAG
    @ApiModelProperty("인계 FLAG") private String moveflag;                              // 인계 FLAG
    @ApiModelProperty("종이비밀 FLAG") private String paperdocflag;                      // 종이비밀 FLAG
    @ApiModelProperty("종이본문 FLAG") private String paperbodyflag;                     // 종이본문 FLAG
    @ApiModelProperty("첨부개수") private BigDecimal attcount;                               // 첨부개수
    @ApiModelProperty("삭제 FLAG") private String delflag;                               // 삭제 FLAG
    @ApiModelProperty("유통망 종류") private String relaytype;                           // 유통망 종류
    @ApiModelProperty("시스템 유형") private String systemtype;                          // 시스템 유형
    @ApiModelProperty("본문변환FLAG") private String convbodyflag;                       // 본문변환FLAG
    @ApiModelProperty("DRM구분") private String drmgubun;                                // DRM구분
    @ApiModelProperty("최초열람ID") private String firstreadid;                          // 최초열람ID
    @ApiModelProperty("최초열람일") private String firstreaddt;                          // 최초열람일
    @ApiModelProperty("등록자 ID") private String inid;                                  // 등록자 ID
    @ApiModelProperty("등록일") private String indt;                                     // 등록일
    @ApiModelProperty("수정자 ID") private String upid;                                  // 수정자 ID
    @ApiModelProperty("수정일") private String updt;                                     // 수정일
    @ApiModelProperty("비밀배부일련번호") private BigDecimal distributedocseq;               // 비밀배부일련번호
    @ApiModelProperty("비밀배부기관 ID") private String distributeorgid;                 // 비밀배부기관 ID
    @ApiModelProperty("종이비밀결재자 ID") private String paperappid;                    // 종이비밀결재자 ID
    @ApiModelProperty("종이비밀결재자 이름") private String paperappname;                // 종이비밀결재자 이름
    @ApiModelProperty("종이비밀결재자 부서이름") private String paperappdeptname;        // 종이비밀결재자 부서이름
    @ApiModelProperty("종이비밀결제자 직위직급이름") private String paperappdutyname;    // 종이비밀결제자 직위직급이름
    @ApiModelProperty("열람제한유형") private String readlimittype;                      // 열람제한유형
    @ApiModelProperty("열람제한일자") private String readlimitdate;                      // 열람제한일자
    @ApiModelProperty("비밀보안 FLAG") private String protectflag;                       // 비밀보안 FLAG
    @ApiModelProperty("업무담당확인자 ID") private String chargerid;                     // 업무담당확인자 ID
    @ApiModelProperty("업무담당확인자 부서 ID") private String chargerdeptid;            // 업무담당확인자 부서 ID
    @ApiModelProperty("최초배부일") private String distributedt;                         // 최초배부일
    @ApiModelProperty("기록물등록구분") private String paperrecgubun;                    // 기록물등록구분
    @ApiModelProperty("특수기록물") private String paperrecspecial;                      // 특수기록물
    @ApiModelProperty("기록물형태") private String paperrectypes;                        // 기록물형태
    @ApiModelProperty("비밀승인코드") private String apprdocnumcode;                     // 비밀승인코드
    @ApiModelProperty("이관여부") private String transflag;                              // 이관여부
    @ApiModelProperty("기록요약") private String recordersummary;                        // 기록요약
    @ApiModelProperty("긴급도") private String emergency;                                // 긴급도
    @ApiModelProperty("추가발송 회수") private BigDecimal addsendcnt;                        // 추가발송 회수
    @ApiModelProperty("보고서종류") private String docgroup;                             // 보고서종류
    @ApiModelProperty("가제목") private String faketitle;                                // 가제목
    @ApiModelProperty("비밀구분") private String secgubun;                                   // 비밀구분
    @ApiModelProperty("비밀등급") private String seclevel;                                   // 비밀등급
    @ApiModelProperty("원본보호기간") private String orgnprotdt;                             // 원본보호기간
    @ApiModelProperty("원본차기비밀등급") private String orgnnextlevel;                      // 원본차기비밀등급
    @ApiModelProperty("원본재분류") private String orgnreclass;                              // 원본재분류
    @ApiModelProperty("원본보호조건") private String orgnprotcond;                           // 원본보호조건
    @ApiModelProperty("사본보호기간") private String copyprotdt;                             // 사본보호기간
    @ApiModelProperty("사본차기비밀등급") private String copynextlevel;                      // 사본차기비밀등급
    @ApiModelProperty("사본재분류") private String copyreclass;                              // 사본재분류
    @ApiModelProperty("사본보호조건") private String copyprotcond;                           // 사본보호조건
    @ApiModelProperty("보존기간") private String prsrvterm;                                  // 보존기간
    @ApiModelProperty("문서번호") private String docno;                                      // 문서번호
    @ApiModelProperty("관리번호") private String mgmtno;                                     // 관리번호
    @ApiModelProperty("사본번호") private String copyno;                                     // 사본번호
    @ApiModelProperty("생산부서코드") private String createdeptid;                           // 생산부서코드
    @ApiModelProperty("생산부서명") private String createdeptname;                           // 생산부서명
    @ApiModelProperty("쪽수") private String docpagenum;                                     // 쪽수
    @ApiModelProperty("특별관리여부") private String specialyn;                              // 특별관리여부
    @ApiModelProperty("참고철 바인더 여부") private String binderyn;                         // 참고철 바인더 여부
    @ApiModelProperty("가제목 사용여부") private String secretdocyn;                         // 가제목 사용여부
    @ApiModelProperty("전시여부") private String waryn;                                      // 전시여부
    @ApiModelProperty("신규") private String isnewrule;                                      // 신규
    @ApiModelProperty("이관여부") private String istransrule;                                // 이관여부
    @ApiModelProperty("") private String orgncreateyear;                                     //
    @ApiModelProperty("") private String wargubun;                                           //
    @ApiModelProperty("처리자 ID") private String acterid;                                       // 처리자 ID
    @ApiModelProperty("처리자 이름") private String actername;                                   // 처리자 이름
    @ApiModelProperty("처리자 직위직급이름") private String acterdutyname;                       // 처리자 직위직급이름
    @ApiModelProperty("처리자 직위 ID") private String acterpositionid;                          // 처리자 직위 ID
    @ApiModelProperty("처리자 직급 ID") private String actergradeid;                             // 처리자 직급 ID
    @ApiModelProperty("처리자 부서 ID") private String acterdeptid;                              // 처리자 부서 ID
    @ApiModelProperty("처리자 부서 이름") private String acterdeptname;                          // 처리자 부서 이름
    @ApiModelProperty("처리자 부서 이름 상세") private String acterdeptnamedesc;                 // 처리자 부서 이름 상세
    @ApiModelProperty("처리구분") private String actgubun;                                       // 처리구분
    @ApiModelProperty("처리일") private String actdt;                                            // 처리일
    @ApiModelProperty("처리의견") private String actopinion;                                     // 처리의견
    @ApiModelProperty("기록관 ID") private String recid;                                         // 기록관 ID
    @ApiModelProperty("수신자 이름") private String recname;                                     // 수신자 이름
    @ApiModelProperty("수신자 직위직급") private String recdutyname;                             // 수신자 직위직급
    @ApiModelProperty("수신자 직위 ID") private String recpositionid;                            // 수신자 직위 ID
    @ApiModelProperty("수신자 직급 ID") private String recgradeid;                               // 수신자 직급 ID
    @ApiModelProperty("수신자 부서 ID") private String recdeptid;                                // 수신자 부서 ID
    @ApiModelProperty("수신자 부서 이름") private String recdeptname;                            // 수신자 부서 이름
    @ApiModelProperty("수신자 부서 상세이름") private String recdeptnamedesc;                    // 수신자 부서 상세이름
    @ApiModelProperty("처리경로이력 ID") private String pathhisid;                               // 처리경로이력 ID
    @ApiModelProperty("전자서명 ID") private String electsignid;                                 // 전자서명 ID
    @ApiModelProperty("버전") private BigDecimal version;                                                // 버전
    @ApiModelProperty("결재요청 ID") private String apprreqid;                                       // 결재요청 ID
    @ApiModelProperty("생산/접수 구분") private String regirecvgubun;                                // 생산/접수 구분
    @ApiModelProperty("요청 구분") private String reqtype;                                           // 요청 구분
    @ApiModelProperty("구분") private String gubun;                                                  // 구분
    @ApiModelProperty("RFID 사용 여부") private String rfidtagflag;                                  // RFID 사용 여부
    @ApiModelProperty("비밀명") private String docttl;                                               // 비밀명
    @ApiModelProperty("요약") private String summary;                                                // 요약
    @ApiModelProperty("담당자 ID") private String authorid;                                          // 담당자 ID
    @ApiModelProperty("담당자 이름") private String authorname;                                      // 담당자 이름
    @ApiModelProperty("담당자 직위직급") private String authordutyname;                              // 담당자 직위직급
    @ApiModelProperty("담당자 직위 ID") private String authorpositionid;                             // 담당자 직위 ID
    @ApiModelProperty("담당자 직급 ID") private String authorgradeid;                                // 담당자 직급 ID
    @ApiModelProperty("담당자 부서 ID") private String authordeptid;                                 // 담당자 부서 ID
    @ApiModelProperty("담당자 부서 이름") private String authordeptname;                             // 담당자 부서 이름
    @ApiModelProperty("담당자 부서 이름 상세") private String authordeptnamedesc;                    // 담당자 부서 이름 상세
    @ApiModelProperty("결재자 ID") private String lastauthorid;                                      // 결재자 ID
    @ApiModelProperty("결재자 이름") private String lastauthorname;                                  // 결재자 이름
    @ApiModelProperty("기안자 이름") private String draftername;                                     // 기안자 이름
    @ApiModelProperty("발신기관 이름") private String sendorgname;                                   // 발신기관 이름
    @ApiModelProperty("발송일시") private String senddt;                                             // 발송일시
    @ApiModelProperty("수신처 표시 이름") private String recvdisplayname;                            // 수신처 표시 이름
    @ApiModelProperty("경유부서 이름") private String viadeptname;                                   // 경유부서 이름
    @ApiModelProperty("비밀 형태") private BigDecimal sectype;                                           // 비밀 형태
    @ApiModelProperty("단위과제카드 ID") private String tcardid;                                     // 단위과제카드 ID
    @ApiModelProperty("관리과제카드 ID") private String pcardid;                                     // 관리과제카드 ID
    @ApiModelProperty("기관과제카드 ID") private String ocardid;                                     // 기관과제카드 ID
    @ApiModelProperty("등록자 이름") private String inname;                                          // 등록자 이름
    @ApiModelProperty("기안일자") private String draftdt;                                            // 기안일자
    @ApiModelProperty("결재일자") private String approvedt;                                          // 결재일자
    @ApiModelProperty("결재자직위직급") private String lastauthordutyname;                           // 결재자직위직급
    @ApiModelProperty("결재자직위 ID") private String lastauthorpositionid;                          // 결재자직위 ID
    @ApiModelProperty("결재자 직급 ID") private String lastauthorgradeid;                            // 결재자 직급 ID
    @ApiModelProperty("결재자 부서 ID") private String lastauthordeptid;                             // 결재자 부서 ID
    @ApiModelProperty("결재자 부서 이름") private String lastauthordeptname;                         // 결재자 부서 이름
    @ApiModelProperty("결재자 부서 이름 상세") private String lastauthordeptnamedesc;                // 결재자 부서 이름 상세
    @ApiModelProperty("타입") private String type;                                                   // 타입
    @ApiModelProperty("비밀관리 ID") private String mgmtid;                                          // 비밀관리 ID
    @ApiModelProperty("승인여부") private String confirmflag;                                        // 승인여부
    @ApiModelProperty("단번호") private String cabinetroomno;                                        // 단번호
    @ApiModelProperty("보관함 코드") private String cabinetid;                                       // 보관함 코드
    @ApiModelProperty("등재일자") private String regirecvdt;                                         // 등재일자
    @ApiModelProperty("등록구분기타") private String sectypeetc;                                     // 등록구분기타
    @ApiModelProperty("") private String bsnsSeCd;                                                   //
    @ApiModelProperty("") private String bsnsSmbol;                                                  //
    @ApiModelProperty("") private String hostilegubun;                                               //
    @ApiModelProperty("") private String realexist;                                                  //
    @ApiModelProperty("") private String mediatype;                                                  //
    @ApiModelProperty("") private String mediatypeEtc;                                               //
    @ApiModelProperty("") private String deptname;                                               //
    @ApiModelProperty("") private String sendopinion;                                               //
}

