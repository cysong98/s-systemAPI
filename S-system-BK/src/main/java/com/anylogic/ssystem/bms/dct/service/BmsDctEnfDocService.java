/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.service;

import com.anylogic.ssystem.bms.dct.model.*;

import java.util.List;
import com.github.pagehelper.PageInfo;

import java.util.*;


public interface BmsDctEnfDocService {

    /* 발송 > 반송함 > 반송함 목록 조회 */
    public PageInfo<SelectReturnListResVO> selectReturnList(SelectReturnListReqVO param);

    /* 접수 > 배부함 > 배부함 목록 조회 */
    public PageInfo<SelectDctDistributeListResVO> selectDctDistributeList(SelectDctDistributeListReqVO param);

    /* 접수 > 접수대기함 > 접수대기함 목록 조회 */
    public PageInfo<SelectReceiptListResVO> selectReceiptList(SelectReceiptListReqVO param);

    /* 접수한 문서 문서처리 시 상태값 수정 */
    public int updateEnfDocRepath(UpdateEnfDocRepathReqVO param);

    /* 접수 > 재지정함 > 재지정함 목록 조회 */
    public PageInfo<SelectReprocessListResVO> selectReprocessList(SelectReprocessListReqVO param);

    /* 비전자 접수 문서 상세 > 접수 정보 조회 */
    public GetReprocessResVO getReprocess(GetReprocessReqVO param);

    /* 접수 > 접수현황함 > 접수현황함 목록 조회 */
    public PageInfo<SelectReceiptListResVO> selectReceiptCurrentList(SelectReceiptListReqVO param) throws RuntimeException;

    /* 접수 > 접수대기함 > 담당정보 조회 */
    public List<SelectDctEnfPathResVO> selectDctEnfPath(SelectDctEnfPathReqVO param) throws RuntimeException;

}
