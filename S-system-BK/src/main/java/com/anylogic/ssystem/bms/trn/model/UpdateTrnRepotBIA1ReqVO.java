/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.trn.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import java.io.Serializable;
import java.lang.String;
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
public class UpdateTrnRepotBIA1ReqVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("최종결재완료여부")
    @Size(max=5)
    private String apprstatus;                   // 최종결재완료여부

    @ApiModelProperty("진행상태")
    @Size(max=5)
    private String status;                       // 진행상태

    // bms_trn_step
    @ApiModelProperty("") private String transferid;                  //
    @ApiModelProperty("") private BigDecimal apprseq;           //
    @ApiModelProperty("") private String apprusername;                //
    @ApiModelProperty("") private String appruserid;                  //
    @ApiModelProperty("") private String apprdeptname;                //
    @ApiModelProperty("") private String apprdeptid;                  //
    @ApiModelProperty("") private String apprposition;                //
    @ApiModelProperty("") private String apprcode;                    //
//    @ApiModelProperty("") private String apprstatus;                  //
    @ApiModelProperty("") private String apprdate;                    //
    @ApiModelProperty("") private String apprreason;                  //
    @ApiModelProperty("") private String inid;                        //
    @ApiModelProperty("") private String indt;                        //
    @ApiModelProperty("") private String state;                       //
    @ApiModelProperty("") private String electsignid;                 //
    @ApiModelProperty("") private String appstatus;                 //

    // bms_dct_mgmt_regi
    @ApiModelProperty("비밀관리 ID") private String mgmtid;                     // 비밀관리 ID
    @ApiModelProperty("관리번호") private String mgmtno;                        // 관리번호
    @ApiModelProperty("비밀 ID") private String docid;                          // 비밀 ID
    @ApiModelProperty("비밀등급") private String seclevel;                      // 비밀등급
    @ApiModelProperty("비밀일련번호") private BigDecimal mgmtseq;         // 비밀일련번호
    @ApiModelProperty("생산/접수 연도") private String regirecvyear;            // 생산/접수 연도
    @ApiModelProperty("생산/접수 구분") private String regirecvgubun;           // 생산/접수 구분
    @ApiModelProperty("생산/접수 일자") private String regirecvdt;              // 생산/접수 일자
    @ApiModelProperty("생산/접수 발신처 ID") private String regirecvsendid;     // 생산/접수 발신처 ID
    @ApiModelProperty("생산/접수 발신처명") private String regirecvsendname;    // 생산/접수 발신처명
    @ApiModelProperty("생산/접수 형태") private String regirecvtype;            // 생산/접수 형태
    @ApiModelProperty("비밀제목") private String secttl;                        // 비밀제목
    @ApiModelProperty("비밀형태") private BigDecimal sectype;             // 비밀형태
    @ApiModelProperty("첨부물개수") private BigDecimal attchcnt;          // 첨부물개수
    @ApiModelProperty("사본번호") private String copyno;                        // 사본번호
    @ApiModelProperty("예고문보호기간") private String notiprotterm;            // 예고문보호기간
    @ApiModelProperty("예고문보존기간") private String notiprsrvterm;           // 예고문보존기간
    @ApiModelProperty("처리부서 ID") private String deptid;                     // 처리부서 ID
    @ApiModelProperty("처리부서명") private String deptname;                    // 처리부서명
    @ApiModelProperty("비밀형태") private String readrangetype;                 // 비밀형태
    @ApiModelProperty("열람범위") private String secstatus;                     // 열람범위
    @ApiModelProperty("처리담당자 ID") private String authorid;                 // 처리담당자 ID
    @ApiModelProperty("처리담당자명") private String authorname;                // 처리담당자명
    @ApiModelProperty("처리담당자 직위직급명") private String authordutyname;   // 처리담당자 직위직급명
    @ApiModelProperty("처리담당자 직위 ID") private String authorposid;         // 처리담당자 직위 ID
    @ApiModelProperty("처리담당자 직급 ID") private String authorgrdid;         // 처리담당자 직급 ID
    @ApiModelProperty("재분류일자") private String reclassdt;                   // 재분류일자
    @ApiModelProperty("재분류내용") private String reclasscont;                 // 재분류내용
    @ApiModelProperty("재분류근거") private String reclassbasis;                // 재분류근거
    @ApiModelProperty("재분류이전 관리번호") private String reclassmgtno;       // 재분류이전 관리번호
    @ApiModelProperty("재분류이전 관리 ID") private String reclassmgmtid;       // 재분류이전 관리 ID
    @ApiModelProperty("재분류코드") private String reclasslevel;                // 재분류코드
    @ApiModelProperty("이관일자") private String transdt;                       // 이관일자
    @ApiModelProperty("이관근거") private String transbasis;                    // 이관근거
    @ApiModelProperty("파기일자") private String destructdt;                    // 파기일자
    @ApiModelProperty("파기근거") private String destructbasis;                 // 파기근거
    @ApiModelProperty("존안여부") private String cnsrvflag;                     // 존안여부
    @ApiModelProperty("존안일자") private String cnsrvdt;                       // 존안일자
    @ApiModelProperty("존안근거") private String cnsrvbasis;                    // 존안근거
    @ApiModelProperty("보관장소") private String custdyplace;                   // 보관장소
    @ApiModelProperty("RFID사용여부") private String rfidtagflag;               // RFID사용여부
    @ApiModelProperty("삭선여부") private String delflag;                       // 삭선여부
    @ApiModelProperty("등재자 ID") private String rgstid;                       // 등재자 ID
    @ApiModelProperty("등재자명") private String rgstname;                      // 등재자명
    @ApiModelProperty("등재일시") private String rgstdt;                        // 등재일시
    @ApiModelProperty("수정자 ID") private String updtid;                       // 수정자 ID
    @ApiModelProperty("수정자명") private String updtname;                      // 수정자명
    @ApiModelProperty("수정일시") private String updtdt;                        // 수정일시
    @ApiModelProperty("이전시스템 관리번호") private String legacymgmtno;       // 이전시스템 관리번호
    @ApiModelProperty("비고") private String note;                              // 비고
    @ApiModelProperty("이전시스템 문서번호") private String legacydocno;        // 이전시스템 문서번호
    @ApiModelProperty("등록구분기타") private String sectypeetc;                // 등록구분기타
    @ApiModelProperty("실제목") private String realtitle;                       // 실제목
    @ApiModelProperty("존안보존일자") private String cnsrvterm;                 // 존안보존일자
    @ApiModelProperty("합철 관련 MGMT ID") private String aggregatemgmtid;      // 합철 관련 MGMT ID
    @ApiModelProperty("합철 일자") private String aggregatedt;                  // 합철 일자
    @ApiModelProperty("") private String destructconfirm;                       //
    @ApiModelProperty("") private String destructconfirmdt;                     //
    @ApiModelProperty("") private String transwaitdt;                           //

    //인계정보,인수정보,인계인수목록 List
    @ApiModelProperty("") private List<InsertTrnStepReqVO> handoverList;                           //
    @ApiModelProperty("") private List<InsertTrnStepReqVO> takeoverList;                           //
    @ApiModelProperty("") private List<InsetTrnObjectReqVO> selectList;                           //
    @ApiModelProperty("") private List<InsetTrnObjectReqVO> selectObjectList;                           //

    // bms_trn_report
    @ApiModelProperty("") private String managedeptid;
//        @ApiModelProperty("") private String apprstatus;
    @ApiModelProperty("") private String isattach;
//        @ApiModelProperty("") private String inid;
    @ApiModelProperty("") private String isinside;
    @ApiModelProperty("") private String reqposition;
    @ApiModelProperty("") private String requsername;
    @ApiModelProperty("") private String ischiefyn;
    @ApiModelProperty("") private String incmemo;
    @ApiModelProperty("") private String reqdate;
    @ApiModelProperty("") private String reason;
    //    @ApiModelProperty("") private String transferid;
    @ApiModelProperty("") private String reqterm;
    @ApiModelProperty("") private String ismanage;
    @ApiModelProperty("") private String reqdeptname;
    @ApiModelProperty("") private String reqdeptid;
    @ApiModelProperty("") private String title;
    @ApiModelProperty("") private String requserid;
    //    @ApiModelProperty("") private String indt;
    @ApiModelProperty("") private String objgubun;
    @ApiModelProperty("") private String manageuserid;

    @ApiModelProperty("") private String reqttl;
    @ApiModelProperty("") private String reqcontents;
    @ApiModelProperty("") private String reqstatus;

    @ApiModelProperty("") private String gojudge;
}

