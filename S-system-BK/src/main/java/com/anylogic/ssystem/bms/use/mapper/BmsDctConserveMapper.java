/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.mapper;

import com.anylogic.ssystem.bms.dct.model.SelectMgmtRegiListReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectMgmtRegiListResVO;
import com.anylogic.ssystem.bms.use.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BmsDctConserveMapper {

    /* 존안 비밀관리기록부 목록 조회 */
    List<SelectMgmtRegiListResVO> selectUseConserveMgmtList(SelectUserReqestsListReqVO param) throws RuntimeException;

    /* 이관 비밀관리기록부 목록 조회 */
    List<SelectMgmtRegiListResVO> selectUseTransMgmtList(SelectUserReqestsListReqVO param) throws RuntimeException;

}
