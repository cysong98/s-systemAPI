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
import com.anylogic.ssystem.bms.use.model.SelectUseReqViewListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseReqViewListResVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqViewApproveReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqViewRejectReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseDraftViewListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseDraftViewListResVO;
import com.anylogic.ssystem.bms.use.model.SelectUseProcViewListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseProcViewListResVO;
import com.anylogic.ssystem.bms.use.model.SelectUseTempViewListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseTempViewListResVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseTempViewApproveReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseCompleteViewListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseCompleteViewListResVO;
import com.anylogic.ssystem.bms.use.model.InsertUseDigitalViewReqReqVO;
import com.anylogic.ssystem.bms.use.model.InsertUseNonDigitalViewReqReqVO;


@Mapper
public interface BmsDctApprreqReadMapper {

    /* 전자 열람 요청내용 등록 */
    int insertUseDigitalViewReq(InsertUseDigitalViewReqReqVO param) throws RuntimeException;

    /* 비전자 열람 요청내용 등록 */
    int insertUseNonDigitalViewReq(InsertUseDigitalViewReqReqVO param) throws RuntimeException;

    /* 각 요청서 승인/반려 처리 - 승인한 열람정보 수정 */
    int updateUseReqMapView(HashMap<String, Object> param) throws RuntimeException;

    /* 각 요청서 상세화면 조회 - 열람할 문서 정보 */
    List<HashMap<String, Object>> selectUseReqViewMapList(HashMap<String, Object> param) throws RuntimeException;

}
