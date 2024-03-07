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

import com.anylogic.ssystem.bms.com.model.DeleteSystemLoginLogReqVO;
import com.anylogic.ssystem.bms.com.model.InsertSystemLoginLogReqVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import com.anylogic.ssystem.bms.com.model.SelectSystemLoginLogReqVO;
import com.anylogic.ssystem.bms.com.model.SelectSystemLoginLogResVO;


@Mapper
public interface ComLoginLogMapper {

    /* 시스템관리 > 시스템접속로그 > 시스템접속로그 목록 조회 */
    List<SelectSystemLoginLogResVO> selectSystemLoginLog(SelectSystemLoginLogReqVO param) throws RuntimeException;

    /* 로그인시 시스템접속로그 등록 */
    int insertSystemLoginLog(InsertSystemLoginLogReqVO param) throws RuntimeException;

    /* 시스템접속로그 삭제 */
    int deleteSystemLoginLog(DeleteSystemLoginLogReqVO param) throws RuntimeException;

}
