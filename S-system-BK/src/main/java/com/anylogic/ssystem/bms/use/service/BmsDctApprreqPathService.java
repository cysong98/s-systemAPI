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

import com.anylogic.ssystem.bms.use.model.UpdateUseReqLendAppPathApproveBIAReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqLendAppPathRejectBIAReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqRecycleAppPathApproveBIAReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqRecycleAppPathRejectBIAReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqViewAppPathApproveBIAReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqViewAppPathRejectBIAReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqPrintAppPathApproveBIAReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqPrintAppPathRejectBIAReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseTempLendAppPathApproveBIAReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseTempRecycleAppPathApproveBIAReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseTempViewAppPathApproveBIAReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseTempPrintAppPathApproveBIAReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseCompleteLendAppApproveReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseCompleteRecycleAppApproveReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseCompleteViewAppApproveReqVO;

import java.util.*;


public interface BmsDctApprreqPathService {

    /* 각 요청서 승인/반려 처리 */
    public int updateUseReqViewAppPathApproveBIA(UpdateUseReqViewAppPathApproveBIAReqVO param);
}
