/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.mapper;

import com.anylogic.ssystem.bms.com.model.InsertRealtimeLogReqVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import com.anylogic.ssystem.bms.com.model.SelectRealtimeLogReqVO;
import com.anylogic.ssystem.bms.com.model.SelectRealtimeLogResVO;


@Mapper
public interface ComRealtimeLogMapper {

    /* 시스템관리 > 실시간접속현황 > 실시간접속현황 목록 조회 */
    List<SelectRealtimeLogResVO> selectRealtimeLog(SelectRealtimeLogReqVO param) throws RuntimeException;

    /* 실시간접속현황 등록 */
    public int insertRealtimeLog(InsertRealtimeLogReqVO param) throws RuntimeException;

    /* 실시간접속현황 수정 */
    public int updateRealtimeLog(InsertRealtimeLogReqVO param) throws RuntimeException;

}
