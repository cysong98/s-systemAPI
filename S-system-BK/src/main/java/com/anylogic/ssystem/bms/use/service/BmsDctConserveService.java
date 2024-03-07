/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.service;

import com.anylogic.ssystem.bms.dct.model.SelectMgmtRegiListReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectMgmtRegiListResVO;
import com.anylogic.ssystem.bms.use.model.*;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface BmsDctConserveService {

    /* 존안 비밀관리기록부 목록 조회 */
    public PageInfo<SelectMgmtRegiListResVO> selectUseConserveMgmtList(SelectUserReqestsListReqVO param);

    /* 이관 비밀관리기록부 목록 조회 */
    public PageInfo<SelectMgmtRegiListResVO> selectUseTransMgmtList(SelectUserReqestsListReqVO param);

}
