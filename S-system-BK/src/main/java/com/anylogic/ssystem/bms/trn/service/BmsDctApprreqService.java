/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.trn.service;

import com.anylogic.ssystem.bms.trn.model.InsertApprreqReqVO;
import com.anylogic.ssystem.bms.trn.model.InsertTrnStepReqVO;

import java.util.*;


public interface BmsDctApprreqService {

    /* 인계인수서 임시저장 */
    public int insertApprreq(InsertTrnStepReqVO param);

}
