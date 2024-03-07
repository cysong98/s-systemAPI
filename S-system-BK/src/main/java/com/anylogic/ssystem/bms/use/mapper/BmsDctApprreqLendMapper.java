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
import com.anylogic.ssystem.bms.use.model.SelectUseReqLendListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseReqLendListResVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqLendApproveReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqLendRejectReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseDraftLendListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseDraftLendListResVO;
import com.anylogic.ssystem.bms.use.model.SelectUseProcLendListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseProcLendListResVO;
import com.anylogic.ssystem.bms.use.model.SelectUseTempLendListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseTempLendListResVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseTempLendApproveReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseCompleteLendListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseCompleteLendListResVO;
import com.anylogic.ssystem.bms.use.model.InsertUseTakeOutReqVO;


@Mapper
public interface BmsDctApprreqLendMapper {

    /* 반출 요청내용 등록 */
    int insertUseTakeOut(InsertUseTakeOutReqVO param) throws RuntimeException;

    /* 각 요청서 승인/반려 처리 - 승인한 반출정보 수정 */
    int updateUseReqMapLend(HashMap<String, Object> param) throws RuntimeException;

    /* 반출실행 - 반출일자 수정 */
    int updateLenddt(HashMap<String, Object> param) throws RuntimeException;

    /* 반납실행 - 반납일자 수정 */
    int updateLendBackdt(HashMap<String, Object> param) throws RuntimeException;

    /* 각 요청서 상세화면 조회 - 반출할 문서 정보 */
    List<HashMap<String, Object>> selectUseReqLendMapList(HashMap<String, Object> param) throws RuntimeException;
}
