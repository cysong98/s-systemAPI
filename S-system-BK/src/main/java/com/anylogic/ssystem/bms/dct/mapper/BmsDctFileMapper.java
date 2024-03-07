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
import com.anylogic.ssystem.bms.dct.model.InsertFileReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectDctFileReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectDctFileResVO;


@Mapper
public interface BmsDctFileMapper {

    /* 파일 등록 */
    int insertFile(InsertFileReqVO param) throws RuntimeException;

    /* 기안문, 붙임 파일 조회 */
    List<SelectDctFileResVO> selectDctFile(SelectDctFileReqVO param) throws RuntimeException;

    /* 발송 실패 오프라인 인쇄시 파일 조회 */
    List<SelectDctFileResVO> selectSendFileList(SelectDctFileReqVO param) throws RuntimeException;

    /* 붙임 파일만 조회 */
    List<Map<String, Object>> selectDctFileDFT02(Map<String, Object> param) throws RuntimeException;

    /* 파일 조회*/
    List<SelectDctFileResVO> selectCommonDctFile(SelectDctFileReqVO param) throws RuntimeException;
}
