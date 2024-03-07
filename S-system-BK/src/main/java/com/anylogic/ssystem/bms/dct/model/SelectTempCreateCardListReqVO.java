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
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class SelectTempCreateCardListReqVO extends AnyXCommonVO implements Serializable {
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
    @ApiModelProperty("") private String copynextlevel;             // copynextlevel ( 사본차기비밀등급 )

    // bms_dct_path
    @ApiModelProperty("") private String pathorder;                 // pathorder ( 구분 : 경로순서 )
    @ApiModelProperty("") private String pathstate;                 // pathstate ( 상황 )
    @ApiModelProperty("") private String asktype;                   // asktype ( 요청유형 )
    @ApiModelProperty("") private String deptid;                    // deptid ( 부서ID )
    @ApiModelProperty("") private String deptname;                  // deptname ( 부서이름 )
    @ApiModelProperty("") private String dutyname;                  // dutyname ( 직위직급이름 )
    @ApiModelProperty("") private String electsignid;               // electsignid ( 전자서명ID )
    @ApiModelProperty("") private List<SelectDctPathResVO> pathlist;                 // pathlist

    // bms_dct_opinion
    @ApiModelProperty("") private String opinion;                   // opinion ( 의견 )

    // bms_dct_doc_sendinfo
    @ApiModelProperty("") private String sendinfodocgubun;          // sendinfodocgubun ( 문서구분 )
    @ApiModelProperty("") private String sendinfotempflag;          // sendinfotempflag ( 문서구분 )
    @ApiModelProperty("") private String sendorgname;               // sendorgname ( 발신기관명 )
    @ApiModelProperty("") private String sendname;                  // sendname ( 발신명 )
    @ApiModelProperty("") private List<SelectSendinfoResVO> sendinfolist; //sendinfolist

    // bms_dct_appr_recv
    @ApiModelProperty("") private String receiverid;                // receiverid ( 수신처ID )
    @ApiModelProperty("") private String apprrecvtempflag;          // tempflag ( 임시FLAG )
    @ApiModelProperty("") private String apprrecvdeptname;          // deptname ( 수신부서명 )
    @ApiModelProperty("") private List<GetDctRecvResVO> recvlist;                 // recvlist

    // bms_dct_rdoc_rcvs
    @ApiModelProperty("") private String rcvsuserid;                // rcvsuserid ( 사용자명 )
    @ApiModelProperty("") private String rcvsusername;              // rcvsusername ( 사용자이름 = 열람권자 )
    @ApiModelProperty("") private List<SelectDctReaderResVO> rcvslist;   //rcvslist

    // bms_task_infomation_temp
    @ApiModelProperty("") private String resultid;                // resultid (  )

    @ApiModelProperty("") private String loginuserid;

    @ApiModelProperty("") private String enfdocid;
    @ApiModelProperty("") private String vgroupid;
    @ApiModelProperty("") private String orgid;
    @ApiModelProperty("") private String chargerid;

}

