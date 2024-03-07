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
import com.anylogic.ssystem.bms.use.model.SelectUseReqRecycleListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseReqRecycleListResVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqRecycleApproveReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqRecycleRejectReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseDraftRecycleListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseDraftRecycleListResVO;
import com.anylogic.ssystem.bms.use.model.SelectUseProcRecycleListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseProcRecycleListResVO;
import com.anylogic.ssystem.bms.use.model.SelectUseTempRecycleListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseTempRecycleListResVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseTempRecycleApproveReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseCompleteRecycleListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseCompleteRecycleListResVO;
import com.anylogic.ssystem.bms.use.model.InsertReUseReqReqVO;


@Mapper
public interface BmsDctApprreqReuseMapper {

    /* 재사용 요청내용 등록 */
    int insertReUseReq(InsertReUseReqReqVO param) throws RuntimeException;

    /* 각 요청서 승인/반려 처리 - 승인한 재사용정보 수정 */
    int updateUseReqMapRecycle(HashMap<String, Object> param) throws RuntimeException;

    /* 각 요청서 상세화면 조회 - 재사용할 문서 정보 */
    List<HashMap<String, Object>> selectUseReqRecycleMapList(HashMap<String, Object> param) throws RuntimeException;

}
