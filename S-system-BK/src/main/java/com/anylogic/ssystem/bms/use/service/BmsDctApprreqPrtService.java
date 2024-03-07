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

import com.anylogic.ssystem.bms.use.model.UpdateUseCompletePrintFinishBIAReqVO;
import com.anylogic.ssystem.bms.use.model.InsertUseDigitalPrintReqVO;
import com.anylogic.ssystem.bms.use.model.InsertUseNonDigitalPrintReqVO;

import java.util.*;


public interface BmsDctApprreqPrtService {

    /* 비밀문서사용요청 > 인쇄요청 > 전자 인쇄요청서 등록 */
    public int insertUseDigitalPrint(InsertUseDigitalPrintReqVO param);

    /* (반영X) 비밀문서사용요청 > 인쇄요청 > 비전자 인쇄요청서 등록 */
    public int insertUseNonDigitalPrint(InsertUseDigitalPrintReqVO param);

}
