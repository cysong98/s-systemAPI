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

import com.anylogic.ssystem.bms.com.model.SelectUserVgroupReqVO;
import com.anylogic.ssystem.bms.com.model.SelectUserVgroupResVO;
import java.util.List;
import com.github.pagehelper.PageInfo;

import java.util.*;


public interface ComVgroupService {

    /* 조직관리 > 부서/사용자관리 > 권한 검색 팝업 > 권한 목록 조회 */
    public PageInfo<SelectUserVgroupResVO> selectUserVgroup(SelectUserVgroupReqVO param);

}
