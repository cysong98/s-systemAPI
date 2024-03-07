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

import com.anylogic.ssystem.bms.dct.model.SelectOrganizationReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationResVO;
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationInfoReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationInfoResVO;
import java.util.List;
import com.anylogic.ssystem.bms.dct.model.SelectSymbolReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectSymbolResVO;

import java.util.*;


public interface ComOrganizationinfoService {

    /* 관리자쪽 제외 팝업 > 부서 TREE 조회 */
    public List<SelectOrganizationResVO> selectOrganization(SelectOrganizationReqVO param);

    /* LDAP 연동된 부서 정보 조회 */
    public List<SelectOrganizationResVO> selectOrganizationLDAP(SelectOrganizationReqVO param);

    /* 부서장 정보 조회 */
    public List<SelectOrganizationInfoResVO> selectOrganizationInfo(SelectOrganizationInfoReqVO param);

    /* 대리 결재자 정보 조회 */
    public List<SelectOrganizationInfoResVO> selectManagerInfo(SelectOrganizationInfoReqVO param);

    /* 로고/심볼 팝업 조회 */
    public List<SelectSymbolResVO> selectSymbol(SelectSymbolReqVO param);

}
