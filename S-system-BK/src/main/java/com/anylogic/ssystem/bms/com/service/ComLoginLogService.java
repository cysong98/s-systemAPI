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

import com.anylogic.ssystem.bms.com.model.InsertSystemLoginLogReqVO;
import com.anylogic.ssystem.bms.com.model.SelectSystemLoginLogReqVO;
import com.anylogic.ssystem.bms.com.model.SelectSystemLoginLogResVO;
import java.util.List;
import com.github.pagehelper.PageInfo;

import java.util.*;


public interface ComLoginLogService {

    /* 시스템관리 > 시스템접속로그 > 시스템접속로그 목록 조회 */
    public PageInfo<SelectSystemLoginLogResVO> selectSystemLoginLog(SelectSystemLoginLogReqVO param);

    /* 로그인시 시스템접속로그 등록 */
    public int insertSystemLoginLog(InsertSystemLoginLogReqVO param);

    /* 시스템접속로그 삭제 */
    public int deleteSystemLoginLog(InsertSystemLoginLogReqVO param);

}
