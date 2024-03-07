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
import java.util.*;
import com.anylogic.ssystem.bms.dct.model.SelectTaskInfoTempReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectTaskInfoTempResVO;


@Mapper
public interface BmsTaskInfomationTempMapper {

    /* 임시저장된 관련근거 조회 */
    List<SelectTaskInfoTempResVO> selectTaskInfoTemp(SelectTaskInfoTempReqVO param) throws RuntimeException;

    /* 기안완료된 관련근거 조회 */
    List<SelectTaskInfoTempResVO> selectTaskInfo(SelectTaskInfoTempReqVO param) throws RuntimeException;

    /* 관련근거 파일 조회 */
    List<SelectTaskInfoTempResVO> selectTaskInfoJoinFile(SelectTaskInfoTempReqVO param) throws RuntimeException;

}
