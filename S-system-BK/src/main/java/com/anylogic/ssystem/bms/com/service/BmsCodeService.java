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

import com.github.pagehelper.PageInfo;

import java.util.*;


public interface BmsCodeService {


    /* 시스템관리 > 코드관리 > 코드 목록 조회 */
    public PageInfo<SelectCodeListResVO> selectCodeList(SelectCodeListReqVO param);

    /* 수신지정팝업 > 외부 > 수신자기호 조회 */
    public List<SelectCodeListResVO> selectReceiveMarkCodeList(SelectReceiveMarkCodeListReqVO param);

    /* 비전자비밀접수 > 보관함/단번호 조회 */
    public List<SelectCodeListResVO> selectOfflineCabinetInfo(SelectReceiveMarkCodeListReqVO param);

    /* 시스템관리 > 코드관리 > 코드 삭제 */
    public List<Integer> deleteCode(List<DeleteCodeReqVO> param);

    /* 시스템관리 > 코드관리 > 코드 등록 */
    public int insertCode(InsertCodeReqVO param);

    /* 시스템관리 > 코드관리 > 코드 수정 팝업 > 상세조회 */
    public GetCodeDetailResVO getCodeDetail(GetCodeDetailReqVO param);

    /* 시스템관리 > 코드관리 > 코드 수정 팝업 > 수정 */
    public int updateCode(UpdateCodeReqVO param);

}
