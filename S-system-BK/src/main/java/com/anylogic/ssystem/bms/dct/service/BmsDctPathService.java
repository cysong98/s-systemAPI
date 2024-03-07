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

import com.anylogic.ssystem.bms.dct.model.SelectDctPathReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectDctPathResVO;
import java.util.List;

import java.util.*;


public interface BmsDctPathService {

    /* 결재선 경로 조회 */
    public List<SelectDctPathResVO> selectDctPath(SelectDctPathReqVO param);

}
