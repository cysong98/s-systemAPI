/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.trn.mapper;

import com.anylogic.ssystem.bms.trn.model.*;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;


@Mapper
public interface BmsTrnReportMapper {

    /* 인계인수 > 처리할 인계인수서 > 처리할 인계인수서 목록 조회 */
    List<SelectTransProcToDoListResVO> selectTransProcToDoList(SelectTransProcToDoListReqVO param) throws RuntimeException;

    /* 처리할 인계인수서 승인 처리 */
    int updateTrnRepotBIA1(UpdateTrnRepotBIA1ReqVO param) throws RuntimeException;

    /* 처리할 인계인수서 반려 처리 */
    int updateTrnRepotBIA2(UpdateTrnRepotBIA2ReqVO param) throws RuntimeException;

    /* 인계인수 > 요청한 인계인수서 > 요청한 인계인수서 목록 조회 */
    List<SelectTrnReqListResVO> selectTrnReqList(SelectTrnReqListReqVO param) throws RuntimeException;

    /* 인계인수 > 처리한 인계인수서 > 처리한 인계인수서 목록 조회 */
    List<BmsTrnCompleteListResVO> bmsTrnCompleteList(BmsTrnCompleteListReqVO param) throws RuntimeException;

    /* 인계인수 > 인수현황함 > 인수현황함 목록 조회 */
    List<BmsTrnCurrentListResVO> bmsTrnCurrentList(BmsTrnCurrentListReqVO param) throws RuntimeException;

    /* 인계인수서 등록 */
    int insertTrnReport(InsertTrnStepReqVO param) throws RuntimeException;

    /* 인계자 & 인수자 정보 조회 */
    List<SelectTrnReqListResVO> selectTrnOverList(SelectTrnReqListReqVO param) throws RuntimeException;

    /* 인계인수 대상 목록 조회 */
    List<SelectTrnReqListResVO> selectTrnObjectList(SelectTrnReqListReqVO param) throws RuntimeException;
}
