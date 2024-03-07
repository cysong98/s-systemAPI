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

import com.anylogic.ssystem.bms.use.model.InsertComingTransApproveBIAReqVO;
import com.anylogic.ssystem.bms.use.model.InsertTransWaitReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateTransWaitReqVO;

import java.util.List;

import java.util.*;


public interface BmsDctApprreqTransService {

    /* 예고문도래목록 > 내부이관 > 내부이관요청서 등록 */
    public int insertComingTransApproveBIA(InsertComingTransApproveBIAReqVO param);

    /* 예고문도래목록 > 이관연기 > 이관연기요청서 등록 */
    public int insertComingTransDelayBIA(InsertComingTransApproveBIAReqVO param);

    /* 예고문도래목록 > 이관대기 > 이관대기요청서 등록 */
    public int insertTransWait(InsertTransWaitReqVO param);

}
