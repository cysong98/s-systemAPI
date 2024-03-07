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
import com.anylogic.ssystem.bms.use.model.SelectUseReqPrintListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseReqPrintListResVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqPrintApproveReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqPrintRejectReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseDraftPrintListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseDraftPrintListResVO;
import com.anylogic.ssystem.bms.use.model.SelectUseProcPrintListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseProcPrintListResVO;
import com.anylogic.ssystem.bms.use.model.SelectUseTempPrintListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseTempPrintListResVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseTempPrintApproveReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseCompletePrintListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseCompletePrintListResVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseCompletePrintFinishBIAReqVO;
import com.anylogic.ssystem.bms.use.model.InsertUseDigitalPrintReqVO;
import com.anylogic.ssystem.bms.use.model.InsertUseNonDigitalPrintReqVO;


@Mapper
public interface BmsDctApprreqPrtMapper {

    /* 전자 인쇄 요청내용 등록 */
    int insertUseDigitalPrint(InsertUseDigitalPrintReqVO param) throws RuntimeException;

    /* (반영X) 비전자 인쇄 요청내용 등록 */
    int insertUseNonDigitalPrint(InsertUseDigitalPrintReqVO param) throws RuntimeException;

    /* 각 요청서 승인/반려 처리 - 승인한 인쇄정보 수정 */
    int updateUseReqMapPrint(HashMap<String, Object> param) throws RuntimeException;

    /* 각 요청서 상세화면 조회 - 인쇄할 문서 정보 */
    List<HashMap<String, Object>> selectUseReqPrintMapList(HashMap<String, Object> param) throws RuntimeException;

    /* 파기예정일자 조회 */
    SelectUseReqPrintListResVO getReqdesturcdtByid(SelectUseReqPrintListReqVO param) throws RuntimeException;

}
