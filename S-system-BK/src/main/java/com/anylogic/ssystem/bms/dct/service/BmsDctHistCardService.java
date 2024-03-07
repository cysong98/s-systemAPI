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

import com.anylogic.ssystem.bms.dct.model.GetHistCardInfoReqVO;
import com.anylogic.ssystem.bms.dct.model.GetHistCardInfoResVO;
import com.anylogic.ssystem.bms.dct.model.UpdateDctHistCardReqVO;

import java.util.*;


public interface BmsDctHistCardService {

    /* 비밀 이력 팝업 조회 */
    public List<GetHistCardInfoResVO> getHistCardInfo(GetHistCardInfoReqVO param);

}
