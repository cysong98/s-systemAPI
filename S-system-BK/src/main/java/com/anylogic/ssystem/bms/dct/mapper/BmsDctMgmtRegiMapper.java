/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.mapper;

import com.anylogic.ssystem.bms.dct.model.*;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;


@Mapper
public interface BmsDctMgmtRegiMapper {

    /* 비밀관리기록부 등록 */
    int insertMgmtRegi(InsertMgmtRegiReqVO param) throws RuntimeException;

    /* 비밀관리기록부 목록 조회 */
    List<SelectMgmtRegiListResVO> selectMgmtRegiList(SelectMgmtRegiListReqVO param) throws RuntimeException;

    /* 인계인수 건수별 팝업 조회 (미완료/생산/접수/일반) */
    int selectMgmtRegiCnt(SelectMgmtRegiCntReqVO param) throws RuntimeException;

    /* 타부서 목록 조회 (본인 부서 제외) */
    List<SelectOtherDeptResVO> selectOtherDept(SelectOtherDeptReqVO param) throws RuntimeException;

    /* 비밀관리기록부 팝업 조회 (재분류/파기/이관/존안 정보) */
    GetMgmtRegiResVO getMgmtRegi(GetMgmtRegiReqVO param) throws RuntimeException;

    /* 인계인수 대상 목록 조회 */
    List<GetMgmtRegiResVO> selectMgmtRegiNonPageList(GetMgmtRegiReqVO param) throws RuntimeException;

    /* DOCID로 MGMTID 조회 */
    GetMgmtidByDocidResVO getMgmtidByDocid(GetMgmtidByDocidReqVO param) throws RuntimeException;

}
