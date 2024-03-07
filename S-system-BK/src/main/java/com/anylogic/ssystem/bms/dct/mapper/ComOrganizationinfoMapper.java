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

import com.anylogic.ssystem.bms.com.model.SelectOrganizationListReqVO;
import com.anylogic.ssystem.bms.com.model.SelectOrganizationListResVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationResVO;
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationInfoReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationInfoResVO;
import com.anylogic.ssystem.bms.dct.model.SelectSymbolReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectSymbolResVO;


@Mapper
public interface ComOrganizationinfoMapper {

    /* 관리자쪽 제외 팝업 > 부서 TREE 조회 */
    List<SelectOrganizationResVO> selectOrganization(SelectOrganizationReqVO param) throws RuntimeException;

    /* LDAP 연동된 부서 정보 조회 */
    List<SelectOrganizationResVO> selectOrganizationLDAP(SelectOrganizationReqVO param) throws RuntimeException;

    /* 부서장 정보 조회 */
    List<SelectOrganizationInfoResVO> selectOrganizationInfo(SelectOrganizationInfoReqVO param) throws RuntimeException;

    /* 대리 결재자 정보 조회 */
    List<SelectOrganizationInfoResVO> selectManagerInfo(SelectOrganizationInfoReqVO param) throws RuntimeException;

    /* 로고/심볼 팝업 조회 */
    List<SelectSymbolResVO> selectSymbol(SelectSymbolReqVO param) throws RuntimeException;

}
