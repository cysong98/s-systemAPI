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
import com.anylogic.ssystem.bms.dct.model.InsertPathReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectDctPathReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectDctPathResVO;
import com.anylogic.ssystem.bms.dct.model.UpdatePathReqVO;


@Mapper
public interface BmsDctPathMapper {

    /* 결재 경로 등록 */
    int insertPath(InsertPathReqVO param) throws RuntimeException;

    /* 결재선 경로 조회 */
    List<SelectDctPathResVO> selectDctPath(SelectDctPathReqVO param) throws RuntimeException;

    /* 결재 경로 수정 */
    int updatePath(UpdatePathReqVO param) throws RuntimeException;

}
