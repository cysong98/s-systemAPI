/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.service;

import com.anylogic.ssystem.bms.dct.model.SelectDctFileReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectDctFileResVO;
import java.util.List;

import java.util.*;


public interface BmsDctFileService {

    /* 기안문, 붙임 파일 조회 */
    public List<SelectDctFileResVO> selectDctFile(SelectDctFileReqVO param);

    /* 발송 실패 오프라인 인쇄시 파일 조회 */
    public List<SelectDctFileResVO> selectSendFileList(SelectDctFileReqVO param);

    /* 파일 조회 */
    public List<SelectDctFileResVO> selectCommonDctFile(SelectDctFileReqVO param);

}
