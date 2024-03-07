/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.ssystem.common.file.model;

import com.anylogic.ssystem.bms.dct.model.InsertTempListReqVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.*;
import java.sql.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel
public class AttachFileVO{
//    @ApiModelProperty(value = "첨부파일ID")
//    @NotEmpty
//    private int attachFileId;                    // 첨부파일ID
//
//    @ApiModelProperty(value = "참조ID1")
//    @Size(max=255)
//    private String refId1;                       // 참조ID1
//
//    @ApiModelProperty(value = "참조ID2")
//    @Size(max=255)
//    private String refId2;                       // 참조ID2
//
//    @ApiModelProperty(value = "참조ID3")
//    @Size(max=255)
//    private String refId3;                       // 참조ID3
//
//    @ApiModelProperty(value = "파일명")
//    @Size(max=1024)
//    private String fileName;                     // 파일명
//
//    @ApiModelProperty(value = "원본 파일명")
//    @Size(max=1024)
//    private String fileOriginName;               // 원본 파일명
//
//    @ApiModelProperty(value = "자료 구분 코멘트")
//    @Size(max=200)
//    private String comments;                     // 자료 구분 코멘트
//
//    @ApiModelProperty(value = "파일 저장 경로")
//    @Size(max=1024)
//    private String filePath;                     // 파일 저장 경로
//
//    @ApiModelProperty(value = "테이블명")
//    @Size(max=100)
//    private String tableName;                    // 테이블명
//
//    @ApiModelProperty(value = "파일 URL")
//    @Size(max=1024)
//    private String fileUrl;                      // 파일 URL
//
//    @ApiModelProperty(value = "생성일자")
//    private Date createDt;                       // 생성일자
//
//    @ApiModelProperty(value = "삭제 여부")
//    private String delYn;                        // 삭제 여부
//
//    @ApiModelProperty(value = "참조ID 시퀀스")
//    @NotEmpty
//    private int refIdSeq;                        // 참조ID 시퀀스
//
//    @ApiModelProperty(value = "첨부파일 코멘트")
//    private List<IndexCommentVO> indexComment;           // 첨부파일 코멘트
//
//    @ApiModelProperty(value = "첨부파일 삭제리스트")
//    private List<AttachFileVO> listDelFile;            // 첨부파일 삭제리스트

    @ApiModelProperty(value = "비밀ID")
    private String docid;

    @ApiModelProperty(value = "첨부ID")
    private String fleid;

    @ApiModelProperty(value = "파일고유이름")
    private String sfilename;

    @ApiModelProperty(value = "파일URL")
    private String fleurl;

    @ApiModelProperty(value = "파일Path")
    private String flepath;

    @ApiModelProperty(value = "구분")
    private String gubun;

    @ApiModelProperty(value = "첨부명")
    private String flettl;

    @ApiModelProperty(value = "첨부크기")
    private long flesize;

    @ApiModelProperty(value = "첨부순서")
    private int fleorder;

    @ApiModelProperty(value = "등록일")
    private String indt;

    @ApiModelProperty(value = "등록자ID")
    private String inid;

    @ApiModelProperty(value = "기존 문서의 구분")
    private String origubun;

    @ApiModelProperty(value = "기존 문서의 비밀ID")
    private String oridocid;

    @ApiModelProperty(value = "비전자 첨부구분")
    private String isviewtype;

    @ApiModelProperty(value = "첨부파일 삭제리스트")
    private List<AttachFileVO> listDelFile;            // 첨부파일 삭제리스트

    @ApiModelProperty(value = "첨부파일 복사리스트") 
    private List<InsertTempListReqVO> fileList;   // 첨부파일 다건 복사처리 용도

    @ApiModelProperty(value = "PDF 변환 결과 응답문")
    private String responseStr; //자유변환 PDF변환 API 응답문자

    @ApiModelProperty(value = "전체 파일 개수")
    private double total;

    @ApiModelProperty
    private double progressOffset;
}

