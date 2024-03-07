/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.anylogic.ssystem.bms.dct.model.InsertEnforcePathHistoryReqVO;


@Mapper
public interface BmsDctEnfPathHistoryMapper {

    /* 접수 처리 시 시행비밀 경로 이력 등록 */
    int insertEnforcePathHistory(InsertEnforcePathHistoryReqVO param) throws RuntimeException;

}
