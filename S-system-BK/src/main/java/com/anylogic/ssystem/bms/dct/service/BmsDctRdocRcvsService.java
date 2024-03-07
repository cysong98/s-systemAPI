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

import com.anylogic.ssystem.bms.dct.model.*;

import java.util.List;

import java.util.*;


public interface BmsDctRdocRcvsService {

    /* 열람권자 조회 */
    public List<SelectDctReaderResVO> selectDctReader(SelectDctReaderReqVO param);

    /* 재사용/인쇄/반출 시 열람권한 보유중인지 체크 */
    public HasViewingPermissionResVO hasViewingPermission(HasViewingPermissionReqVO param);

}
