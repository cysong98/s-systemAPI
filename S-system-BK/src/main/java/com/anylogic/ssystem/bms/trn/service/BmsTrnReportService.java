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

import com.anylogic.ssystem.bms.trn.model.SelectTransProcListCountReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectTransProcListCountResVO;
import java.util.List;
import com.anylogic.ssystem.bms.trn.model.SelectTransRecvCountReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectTransRecvCountResVO;
import com.anylogic.ssystem.bms.trn.model.SelectComTransProcListReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectComTransProcListResVO;
import com.anylogic.ssystem.bms.trn.model.SelectTransProcToDoListReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectTransProcToDoListResVO;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.trn.model.UpdateTrnRepotBIA1ReqVO;
import com.anylogic.ssystem.bms.trn.model.UpdateTrnRepotBIA2ReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectTransProcDeptToDoListReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectTransProcDeptToDoListResVO;
import com.anylogic.ssystem.bms.trn.model.SelectTrnReqListReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectTrnReqListResVO;
import com.anylogic.ssystem.bms.trn.model.BmsTrnCompleteListReqVO;
import com.anylogic.ssystem.bms.trn.model.BmsTrnCompleteListResVO;
import com.anylogic.ssystem.bms.trn.model.BmsTrnCurrentListReqVO;
import com.anylogic.ssystem.bms.trn.model.BmsTrnCurrentListResVO;
import com.anylogic.ssystem.bms.trn.model.UpdateTrnRepotBIAReqVO;

import java.util.*;


public interface BmsTrnReportService {

    /* 인계인수 > 처리할 인계인수서 > 처리할 인계인수서 목록 조회 */
    public PageInfo<SelectTransProcToDoListResVO> selectTransProcToDoList(SelectTransProcToDoListReqVO param);

    /* 처리할 인계인수서 승인 처리 */
    public int updateTrnRepotBIA1(UpdateTrnRepotBIA1ReqVO param);

    /* 처리할 인계인수서 반려 처리 */
    public int updateTrnRepotBIA2(UpdateTrnRepotBIA2ReqVO param);

    /* 인계인수 > 요청한 인계인수서 > 요청한 인계인수서 목록 조회 */
    public PageInfo<SelectTrnReqListResVO> selectTrnReqList(SelectTrnReqListReqVO param);

    /* 인계인수 > 처리한 인계인수서 > 처리한 인계인수서 목록 조회 */
    public PageInfo<BmsTrnCompleteListResVO> bmsTrnCompleteList(BmsTrnCompleteListReqVO param);

    /* 인계인수 > 인수현황함 > 인수현황함 목록 조회 */
    public PageInfo<BmsTrnCurrentListResVO> bmsTrnCurrentList(BmsTrnCurrentListReqVO param);

    /* 인계자 & 인수자 정보 조회 */
    public List<SelectTrnReqListResVO> selectTrnOverList(SelectTrnReqListReqVO param);

    /* 인계인수 대상 목록 조회 */
    public List<SelectTrnReqListResVO> selectTrnObjectList(SelectTrnReqListReqVO param);

}
