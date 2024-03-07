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

import com.anylogic.ssystem.bms.use.model.InsertComingCnsrvApproveBIAReqVO;
import java.util.List;

import java.util.*;


public interface BmsDctApprreqCnsrvService {

    /* 예고문도래목록 > 존안요청 > 존안요청서 등록*/
    public int insertComingCnsrvApproveBIA(InsertComingCnsrvApproveBIAReqVO param);

}
