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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel
public class CommFileVO {
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
    private List<CommFileVO> listDelFile;            // 첨부파일 삭제리스트

    @ApiModelProperty(value = "첨부파일 복사리스트") 
    private List<InsertTempListReqVO> fileList;   // 첨부파일 다건 복사처리 용도

    //BMS_DCT_COMM_FILE
    @ApiModelProperty(value = "문서 구분")
    private String docgubun;
    @ApiModelProperty(value = "파일 ID")
    private String fileid;
    @ApiModelProperty(value = "파일 일련번호")
    private int fileseq;
    @ApiModelProperty(value = "파일구분")
    private String filegubun;
    @ApiModelProperty(value = "파일 WIDTH")
    private String filewidth;
    @ApiModelProperty(value = "파일 HEIGHT")
    private String fileheight;
    @ApiModelProperty(value = "파일제목")
    private String filetitle;
    @ApiModelProperty(value = "파일크기")
    private long filesize;
    @ApiModelProperty(value = "비밀등급 마크표시여부")
    private String markflag;
}

