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

import com.anylogic.ssystem.bms.trn.model.SelectTransStepReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectTransStepResVO;
import org.apache.ibatis.annotations.Mapper;
import com.anylogic.ssystem.bms.trn.model.InsertTrnStepReqVO;
import com.anylogic.ssystem.bms.trn.model.UpdateTrnStepReqVO;

import java.util.List;
import java.util.Map;


@Mapper
public interface BmsTrnStepMapper {

    /* 인계인수 결재경로 조회 */
    List<SelectTransStepResVO> selectTransStep(SelectTransStepReqVO param) throws RuntimeException;

    /* 인계인수서 승인요청 - 결재정보 등록 */
    int insertTrnStep(InsertTrnStepReqVO param) throws RuntimeException;

    /* 인계인수서 승인/반려처리- 결재정보 수정 */
    int updateTrnStep(UpdateTrnStepReqVO param) throws RuntimeException;

    /* 인계인수 대상/결재정보 삭제 */
    int deleteTempList(Map<String, Object> param) throws RuntimeException;
}
