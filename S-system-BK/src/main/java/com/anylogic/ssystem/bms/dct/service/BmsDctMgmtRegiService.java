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


public interface BmsDctMgmtRegiService {

    /* 비밀관리기록부 목록 조회 */
    public PageInfo<SelectMgmtRegiListResVO> selectMgmtRegiList(SelectMgmtRegiListReqVO param);

    /* 인계인수 건수별 팝업 조회 (미완료/생산/접수/일반) */
    public SelectMgmtRegiListResVO selectMgmtRegiCnt(SelectMgmtRegiCntReqVO param);

    /* 타부서 목록 조회 (본인 부서 제외) */
    public List<SelectOtherDeptResVO> selectOtherDept(SelectOtherDeptReqVO param);

    /* 비밀관리기록부 팝업 조회 (재분류/파기/이관/존안 정보) */
    public GetMgmtRegiResVO getMgmtRegi(GetMgmtRegiReqVO param);

    /* 인계인수 대상 목록 조회 */
    public List<GetMgmtRegiResVO> selectMgmtRegiNonPageList(GetMgmtRegiReqVO param);

    /* DOCID로 MGMTID 조회 */
    public GetMgmtidByDocidResVO getMgmtidByDocid(GetMgmtidByDocidReqVO param);

}
