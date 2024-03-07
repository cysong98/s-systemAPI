/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.mapper;

import com.anylogic.ssystem.bms.com.model.*;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;


@Mapper
public interface ComOrgDetailInfoMapper {

    /* COM_ORG_DETAIL_INFO 마지막 시퀀스 번호 획득 */
    GetOrgDetailInfoSeqResVO getOrgDetailInfoSeq(GetOrgDetailInfoSeqReqVO param) throws RuntimeException;

    /* =============================== 관인관리 =============================== */

    /* 조직관리 > 관인관리 > 관인 디테일 삭제 */
    int deleteGwanOrgDetail(DeleteGwanOrgDetailReqVO param) throws RuntimeException;

    /* 조직관리 > 관인관리 > 관인 디테일 등록 */
    int insertGwanOrgDetail(InsertGwanOrgDetailReqVO param) throws RuntimeException;

    /* 조직관리 > 관인관리 > 관인 디테일 수정 */
    int updateGwanOrgDetail(UpdateGwanOrgDetailReqVO param) throws RuntimeException;

    /* =============================== 서명인관리 =============================== */

    /* 조직관리 > 서명인관리 > 서명인 삭제 */
    int deleteSignBIA(DeleteSignBIAReqVO param) throws RuntimeException;

    /* 조직관리 > 서명인관리 > 삭제할 서명인 이미지 목록 조회 */
    List<DeleteSignBIASelectFileResVO> deleteSignBIASelectFile(DeleteSignBIASelectFileReqVO param);

    /* 조직관리 > 서명인관리 > 서명인 이미지 삭제 */
    int deleteComImage(DeleteComImageReqVO param) throws RuntimeException;

    /* 조직관리 > 서명인관리 > 서명인 등록 */
    int insertSignBIA(InsertSignBIAReqVO param) throws RuntimeException;

    /* 조직관리 > 서명인관리 > 서명인 이미지 등록 */
    int insertComImage(InsertComImageReqVO param) throws RuntimeException;

    /* 조직관리 > 서명인관리 > 서명인 수정 */
    int updateSignBIA(UpdateSignBIAReqVO param) throws RuntimeException;

    /* 파일 경로 조회 */
    String getFilePath(String fileName);

    /* =============================== 로고관리 =============================== */

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 삭제 */
    int deleteLogoBIA(DeleteLogoBIAReqVO param) throws RuntimeException;

    /* 조직관리 > 심볼/로고관리 > 삭제할 심볼/로고 이미지 목록 조회 */
    List<DeleteLogoBIASelectFileResVO> deleteLogoBIASelectFile(DeleteLogoBIASelectFileReqVO param);

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 등록 */
    int insertLogoBIA(InsertLogoBIAReqVO param) throws RuntimeException;

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 수정 */
    int updateLogoBIA(UpdateLogoBIAReqVO param) throws RuntimeException;

}
