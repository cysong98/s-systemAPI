/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.service;

import com.anylogic.ssystem.bms.com.model.*;

import java.util.List;

import com.anylogic.ssystem.common.file.model.ImageVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


public interface ComOrgDetailInfoService {

    /* 조직관리 > 서명인관리 > 서명인 삭제 */
    public List<Integer> deleteSignBIA(List<DeleteSignBIAReqVO> param);

    /* 조직관리 > 서명인관리 > 서명인 등록 */
    public int insertSignBIA(List<MultipartFile> multipartFiles, InsertSignBIAReqVO param);

    /* 조직관리 > 서명인관리 > 서명인 수정 */
    public int updateSignBIA(List<MultipartFile> multipartFiles, UpdateSignBIAReqVO param);

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 삭제 */
    public List<Integer> deleteLogoBIA(List<DeleteLogoBIAReqVO> param);

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 등록 */
    public int insertLogoBIA(List<MultipartFile> multipartFiles, InsertLogoBIAReqVO param);

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 수정 */
    public int updateLogoBIA(List<MultipartFile> multipartFiles, UpdateLogoBIAReqVO param);
}
