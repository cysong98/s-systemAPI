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

@ApiModel("selectdctRdoc")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectdctRdocResVO extends AnyXCommonVO implements Serializable {
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
    @ApiModelProperty("비밀일련번호") private BigDecimal docnoseq;                    // 비밀일련번호
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
    @ApiModelProperty("원 일련번호") private BigDecimal oriseq;                       // 원 일련번호
    @ApiModelProperty("일괄처리 일련번호") private BigDecimal batchseq;               // 일괄처리 일련번호
    @ApiModelProperty("종이FLAG") private String paperflag;                       // 종이FLAG
    @ApiModelProperty("본문FLAG") private String maindocflag;                     // 본문FLAG
    @ApiModelProperty("첨부일련번호") private BigDecimal attachseq;                   // 첨부일련번호
    @ApiModelProperty("시행구분") private String enfgubun;                        // 시행구분
    @ApiModelProperty("긴급도") private String emergency;                         // 긴급도
    @ApiModelProperty("DRM구분") private String drmgubun;                         // DRM구분
    @ApiModelProperty("기록물등록구분") private String paperrecgubun;             // 기록물등록구분
    @ApiModelProperty("특수기록물") private String paperrecspecial;               // 특수기록물
    @ApiModelProperty("기록물형태") private String paperrectypes;                 // 기록물형태
    @ApiModelProperty("마이그레이션여부") private String migflag;                 // 마이그레이션여부
    @ApiModelProperty("추가발송상태") private String addsendstate;                // 추가발송상태
    @ApiModelProperty("추가발송횟수") private BigDecimal addsendcnt;                  // 추가발송횟수
    @ApiModelProperty("보고서종류") private String docgroup;                      // 보고서종류
    @ApiModelProperty("가제목") private String faketitle;                         // 가제목
    @ApiModelProperty("") private String papersendflag;                           //
    @ApiModelProperty("") private String filelendflag;                            //
    @ApiModelProperty("비밀관리 ID") private String mgmtid;                           // 비밀관리 ID
    @ApiModelProperty("관리번호") private String mgmtno;                              // 관리번호
    @ApiModelProperty("비밀등급") private String seclevel;                            // 비밀등급
    @ApiModelProperty("비밀일련번호") private BigDecimal mgmtseq;                         // 비밀일련번호
    @ApiModelProperty("생산/접수 연도") private String regirecvyear;                  // 생산/접수 연도
    @ApiModelProperty("생산/접수 구분") private String regirecvgubun;                 // 생산/접수 구분
    @ApiModelProperty("생산/접수 일자") private String regirecvdt;                    // 생산/접수 일자
    @ApiModelProperty("생산/접수 발신처 ID") private String regirecvsendid;           // 생산/접수 발신처 ID
    @ApiModelProperty("생산/접수 발신처명") private String regirecvsendname;          // 생산/접수 발신처명
    @ApiModelProperty("생산/접수 형태") private String regirecvtype;                  // 생산/접수 형태
    @ApiModelProperty("비밀제목") private String secttl;                              // 비밀제목
    @ApiModelProperty("비밀형태") private BigDecimal sectype;                             // 비밀형태
    @ApiModelProperty("첨부물개수") private BigDecimal attchcnt;                          // 첨부물개수
    @ApiModelProperty("사본번호") private String copyno;                              // 사본번호
    @ApiModelProperty("예고문보호기간") private String notiprotterm;                  // 예고문보호기간
    @ApiModelProperty("예고문보존기간") private String notiprsrvterm;                 // 예고문보존기간
    @ApiModelProperty("처리부서 ID") private String deptid;                           // 처리부서 ID
    @ApiModelProperty("처리부서명") private String deptname;                          // 처리부서명
    @ApiModelProperty("열람범위") private String secstatus;                           // 열람범위
    @ApiModelProperty("처리담당자 직위 ID") private String authorposid;               // 처리담당자 직위 ID
    @ApiModelProperty("처리담당자 직급 ID") private String authorgrdid;               // 처리담당자 직급 ID
    @ApiModelProperty("재분류일자") private String reclassdt;                         // 재분류일자
    @ApiModelProperty("재분류내용") private String reclasscont;                       // 재분류내용
    @ApiModelProperty("재분류근거") private String reclassbasis;                      // 재분류근거
    @ApiModelProperty("재분류이전 관리번호") private String reclassmgtno;             // 재분류이전 관리번호
    @ApiModelProperty("재분류이전 관리 ID") private String reclassmgmtid;             // 재분류이전 관리 ID
    @ApiModelProperty("재분류코드") private String reclasslevel;                      // 재분류코드
    @ApiModelProperty("이관일자") private String transdt;                             // 이관일자
    @ApiModelProperty("이관근거") private String transbasis;                          // 이관근거
    @ApiModelProperty("파기일자") private String destructdt;                          // 파기일자
    @ApiModelProperty("파기근거") private String destructbasis;                       // 파기근거
    @ApiModelProperty("존안여부") private String cnsrvflag;                           // 존안여부
    @ApiModelProperty("존안일자") private String cnsrvdt;                             // 존안일자
    @ApiModelProperty("존안근거") private String cnsrvbasis;                          // 존안근거
    @ApiModelProperty("보관장소") private String custdyplace;                         // 보관장소
    @ApiModelProperty("RFID사용여부") private String rfidtagflag;                     // RFID사용여부
    @ApiModelProperty("등재자 ID") private String rgstid;                             // 등재자 ID
    @ApiModelProperty("등재자명") private String rgstname;                            // 등재자명
    @ApiModelProperty("등재일시") private String rgstdt;                              // 등재일시
    @ApiModelProperty("수정자 ID") private String updtid;                             // 수정자 ID
    @ApiModelProperty("수정자명") private String updtname;                            // 수정자명
    @ApiModelProperty("수정일시") private String updtdt;                              // 수정일시
    @ApiModelProperty("이전시스템 관리번호") private String legacymgmtno;             // 이전시스템 관리번호
    @ApiModelProperty("비고") private String note;                                    // 비고
    @ApiModelProperty("이전시스템 문서번호") private String legacydocno;              // 이전시스템 문서번호
    @ApiModelProperty("등록구분기타") private String sectypeetc;                      // 등록구분기타
    @ApiModelProperty("실제목") private String realtitle;                             // 실제목
    @ApiModelProperty("존안보존일자") private String cnsrvterm;                       // 존안보존일자
    @ApiModelProperty("합철 관련 MGMT ID") private String aggregatemgmtid;            // 합철 관련 MGMT ID
    @ApiModelProperty("합철 일자") private String aggregatedt;                        // 합철 일자
    @ApiModelProperty("") private String destructconfirm;                             //
    @ApiModelProperty("") private String destructconfirmdt;                           //
    @ApiModelProperty("") private String transwaitdt;                                 //
    @ApiModelProperty("사용자 ID") private String userid;                                 // 사용자 ID
    @ApiModelProperty("사용자 이름") private String username;                             // 사용자 이름
    @ApiModelProperty("직위 ID") private String positionid;                               // 직위 ID
    @ApiModelProperty("직급 ID") private String gradeid;                                  // 직급 ID
    @ApiModelProperty("직위직급이름") private String dutyname;                            // 직위직급이름
    @ApiModelProperty("부서이름상세") private String deptnamedesc;                        // 부서이름상세
    @ApiModelProperty("열람여부") private String isread;                                  // 열람여부
    @ApiModelProperty("열람일시") private String readdt;                                  // 열람일시
    @ApiModelProperty("등록자 ID") private String inid;                                   // 등록자 ID
    @ApiModelProperty("등록일련번호") private String inseq;                               // 등록일련번호
    @ApiModelProperty("유형") private String type;                                        // 유형
    @ApiModelProperty("접수일") private String acceptdt;                                  // 접수일
    @ApiModelProperty("") private String begindt;                                         //
    @ApiModelProperty("") private String reason;                                          //
    @ApiModelProperty("") private String readauthexpiredt;                                          //
    @ApiModelProperty("") private String docno;                                          //
}

