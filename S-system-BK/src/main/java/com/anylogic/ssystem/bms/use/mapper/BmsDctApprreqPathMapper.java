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

import org.apache.ibatis.annotations.Mapper;
import java.util.*;
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
import com.anylogic.ssystem.bms.use.model.InsertComingReclassAppPathApproveReqVO;
import com.anylogic.ssystem.bms.use.model.InsertComingCnsrvAppPathApproveReqVO;
import com.anylogic.ssystem.bms.use.model.InsertComingDestructAppPathApproveReqVO;
import com.anylogic.ssystem.bms.use.model.InsertComingTransAppPathApproveReqVO;
import com.anylogic.ssystem.bms.use.model.InsertDctApprReqPathReqVO;


@Mapper
public interface BmsDctApprreqPathMapper {

    /* 마지막 결재자인지 체크 */
    int checkPathFinalApprover(UpdateUseReqViewAppPathApproveBIAReqVO param) throws RuntimeException;

    /* 결재자 상태 수정 */
    int updateUseReqViewAppPath(UpdateUseReqViewAppPathApproveBIAReqVO param) throws RuntimeException;

    /* 요청서 결재 경로 등록 */
    int insertDctApprReqPath(InsertDctApprReqPathReqVO param) throws RuntimeException;

}
