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
import com.anylogic.ssystem.bms.use.model.InsertPrtFileReqVO;


@Mapper
public interface BmsDctApprreqPrtFleMapper {

    /* 인쇄 요청내용 파일정보 등록 */
    int insertPrtFile(InsertPrtFileReqVO param) throws RuntimeException;

}
