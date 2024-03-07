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

@ApiModel("selectSendinfo")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectSendinfoResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("비밀 ID") private String docid;                             // 비밀 ID
    @ApiModelProperty("문서 구분") private String docgubun;                        // 문서 구분
    @ApiModelProperty("임시 FLAG") private String tempflag;                        // 임시 FLAG
    @ApiModelProperty("발신기관 이름") private String sendorgname;                 // 발신기관 이름
    @ApiModelProperty("발신이름") private String sendname;                         // 발신이름
    @ApiModelProperty("캠페인상부") private String campaignhead;                   // 캠페인상부
    @ApiModelProperty("캠페인하부") private String campaigntail;                   // 캠페인하부
    @ApiModelProperty("발송일시") private String senddt;                           // 발송일시
    @ApiModelProperty("발송의견") private String sendopinion;                      // 발송의견
    @ApiModelProperty("발송자 ID") private String senderid;                        // 발송자 ID
    @ApiModelProperty("발송자 이름") private String sendername;                    // 발송자 이름
    @ApiModelProperty("발송자 직위직급") private String senderdutyname;            // 발송자 직위직급
    @ApiModelProperty("발송자 직위 ID") private String senderpositionid;           // 발송자 직위 ID
    @ApiModelProperty("발송자 직급 ID") private String sendergradeid;              // 발송자 직급 ID
    @ApiModelProperty("발송자 부서 ID") private String senderdeptid;               // 발송자 부서 ID
    @ApiModelProperty("발송자 부서이름") private String senderdeptname;            // 발송자 부서이름
    @ApiModelProperty("발송자 부서상세이름") private String senderdeptnamedesc;    // 발송자 부서상세이름
    @ApiModelProperty("주소정보") private String address;                          // 주소정보
    @ApiModelProperty("우편번호") private String zipcode;                          // 우편번호
    @ApiModelProperty("홈페이지") private String homepage;                         // 홈페이지
    @ApiModelProperty("전화번호") private String phonenumber;                      // 전화번호
    @ApiModelProperty("FAX번호") private String faxnumber;                         // FAX번호
    @ApiModelProperty("이메일주소") private String email;                          // 이메일주소
    @ApiModelProperty("수신처 표시이름") private String recvdisplayname;           // 수신처 표시이름
    @ApiModelProperty("수신처 표시명 FLAG") private String recvdisplayflag;        // 수신처 표시명 FLAG
    @ApiModelProperty("경유부서이름") private String viadeptname;                  // 경유부서이름
    @ApiModelProperty("시행수신처범위") private String enfrecvrange;               // 시행수신처범위
    @ApiModelProperty("발송유형") private String sendtype;                         // 발송유형
    @ApiModelProperty("관인날인종류") private String sealsigntype;                 // 관인날인종류
    @ApiModelProperty("관인날인명") private String sealsignname;                   // 관인날인명
    @ApiModelProperty("발신기관 ID") private String sendorgid;                     // 발신기관 ID
    @ApiModelProperty("FAX 발송요청일시") private String faxsndreqdt;              // FAX 발송요청일시
    @ApiModelProperty("문서 FLAG") private String circdocflag;                     // 문서 FLAG
    @ApiModelProperty("배포처 통제") private String distctrlflag;                  // 배포처 통제
    @ApiModelProperty("") private String distplflag;                               //
    @ApiModelProperty("비밀관리 ID") private String mgmtid;                            // 비밀관리 ID
    @ApiModelProperty("관리번호") private String mgmtno;                               // 관리번호
    @ApiModelProperty("비밀등급") private String seclevel;                             // 비밀등급
    @ApiModelProperty("비밀일련번호") private BigDecimal mgmtseq;                          // 비밀일련번호
    @ApiModelProperty("생산/접수 연도") private String regirecvyear;                   // 생산/접수 연도
    @ApiModelProperty("생산/접수 구분") private String regirecvgubun;                  // 생산/접수 구분
    @ApiModelProperty("생산/접수 일자") private String regirecvdt;                     // 생산/접수 일자
    @ApiModelProperty("생산/접수 발신처 ID") private String regirecvsendid;            // 생산/접수 발신처 ID
    @ApiModelProperty("생산/접수 발신처명") private String regirecvsendname;           // 생산/접수 발신처명
    @ApiModelProperty("생산/접수 형태") private String regirecvtype;                   // 생산/접수 형태
    @ApiModelProperty("비밀제목") private String secttl;                               // 비밀제목
    @ApiModelProperty("비밀형태") private BigDecimal sectype;                              // 비밀형태
    @ApiModelProperty("첨부물개수") private BigDecimal attchcnt;                           // 첨부물개수
    @ApiModelProperty("사본번호") private String copyno;                               // 사본번호
    @ApiModelProperty("예고문보호기간") private String notiprotterm;                   // 예고문보호기간
    @ApiModelProperty("예고문보존기간") private String notiprsrvterm;                  // 예고문보존기간
    @ApiModelProperty("처리부서 ID") private String deptid;                            // 처리부서 ID
    @ApiModelProperty("처리부서명") private String deptname;                           // 처리부서명
    @ApiModelProperty("비밀형태") private String readrangetype;                        // 비밀형태
    @ApiModelProperty("열람범위") private String secstatus;                            // 열람범위
    @ApiModelProperty("처리담당자 ID") private String authorid;                        // 처리담당자 ID
    @ApiModelProperty("처리담당자명") private String authorname;                       // 처리담당자명
    @ApiModelProperty("처리담당자 직위직급명") private String authordutyname;          // 처리담당자 직위직급명
    @ApiModelProperty("처리담당자 직위 ID") private String authorposid;                // 처리담당자 직위 ID
    @ApiModelProperty("처리담당자 직급 ID") private String authorgrdid;                // 처리담당자 직급 ID
    @ApiModelProperty("재분류일자") private String reclassdt;                          // 재분류일자
    @ApiModelProperty("재분류내용") private String reclasscont;                        // 재분류내용
    @ApiModelProperty("재분류근거") private String reclassbasis;                       // 재분류근거
    @ApiModelProperty("재분류이전 관리번호") private String reclassmgtno;              // 재분류이전 관리번호
    @ApiModelProperty("재분류이전 관리 ID") private String reclassmgmtid;              // 재분류이전 관리 ID
    @ApiModelProperty("재분류코드") private String reclasslevel;                       // 재분류코드
    @ApiModelProperty("이관일자") private String transdt;                              // 이관일자
    @ApiModelProperty("이관근거") private String transbasis;                           // 이관근거
    @ApiModelProperty("파기일자") private String destructdt;                           // 파기일자
    @ApiModelProperty("파기근거") private String destructbasis;                        // 파기근거
    @ApiModelProperty("존안여부") private String cnsrvflag;                            // 존안여부
    @ApiModelProperty("존안일자") private String cnsrvdt;                              // 존안일자
    @ApiModelProperty("존안근거") private String cnsrvbasis;                           // 존안근거
    @ApiModelProperty("보관장소") private String custdyplace;                          // 보관장소
    @ApiModelProperty("RFID사용여부") private String rfidtagflag;                      // RFID사용여부
    @ApiModelProperty("삭선여부") private String delflag;                              // 삭선여부
    @ApiModelProperty("등재자 ID") private String rgstid;                              // 등재자 ID
    @ApiModelProperty("등재자명") private String rgstname;                             // 등재자명
    @ApiModelProperty("등재일시") private String rgstdt;                               // 등재일시
    @ApiModelProperty("수정자 ID") private String updtid;                              // 수정자 ID
    @ApiModelProperty("수정자명") private String updtname;                             // 수정자명
    @ApiModelProperty("수정일시") private String updtdt;                               // 수정일시
    @ApiModelProperty("이전시스템 관리번호") private String legacymgmtno;              // 이전시스템 관리번호
    @ApiModelProperty("비고") private String note;                                     // 비고
    @ApiModelProperty("이전시스템 문서번호") private String legacydocno;               // 이전시스템 문서번호
    @ApiModelProperty("등록일시") private String indt;                                 // 등록일시
    @ApiModelProperty("등록구분기타") private String sectypeetc;                       // 등록구분기타
    @ApiModelProperty("실제목") private String realtitle;                              // 실제목
    @ApiModelProperty("존안보존일자") private String cnsrvterm;                        // 존안보존일자
    @ApiModelProperty("합철 관련 MGMT ID") private String aggregatemgmtid;             // 합철 관련 MGMT ID
    @ApiModelProperty("합철 일자") private String aggregatedt;                         // 합철 일자
    @ApiModelProperty("") private String destructconfirm;                              //
    @ApiModelProperty("") private String destructconfirmdt;                            //
    @ApiModelProperty("") private String transwaitdt;                                  //
    @ApiModelProperty("") private String sendinfodocgubun;                            //
    @ApiModelProperty("") private String sendinfotempflag;                                  //
}

