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

import com.anylogic.ssystem.bms.dct.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface BmsDctEnfPathMapper {

    /* 시행 경로 등록 */
    int insertEnforcePath(InsertEnforcePathReqVO param) throws RuntimeException;

    /* 시행 경로 조회 */
    List<SelectDctEnfPathResVO> selectDctEnfPath(SelectDctEnfPathReqVO param) throws RuntimeException;

    /* 시행 경로 삭제 */
    int deleteEnfPathList(String param) throws RuntimeException;

    /* actseq 조회 */
    int getActSeq();

    /* actseq2 조회 */
    int getActSeq2();

}
