/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.service;

import com.anylogic.ssystem.bms.com.model.*;

import java.util.List;
import com.github.pagehelper.PageInfo;

import java.util.*;


public interface ComOrganizationinfoService {

    /* 부서 정보 TREE 조회 */
    public PageInfo<SelectOrganizationListResVO> selectOrganizationList(SelectOrganizationListReqVO param);

    /* 조직관리 > 부서/사용자관리 > 부서 삭제 */
    public List<Integer> deleteOrganizationBIA(List<DeleteOrganizationBIAReqVO> param);

    /* 조직관리 > 부서/사용자관리 > 부서 상세 조회 */
    public GetOrganizationDetailResVO getOrganizationDetail(GetOrganizationDetailReqVO param);

    /* 조직관리 > 부서/사용자관리 > 부서 등록 */
    public int insertOrganizationBIA(InsertOrganizationBIAReqVO param);

    /* 조직관리 > 부서/사용자관리 > 부서 수정 */
    public int updateOrganizationBIA(UpdateOrganizationBIAReqVO param);

    /* 조직관리 > 부서/사용자관리 > 최상위부서 관리 팝업 > 부서 권한 수정 */
    public int changeToporgVgroup(ChangeToporgVgroupReqVO param);

    /* 조직관리 > 서명인관리 > 서명인 목록 조회 */
    public PageInfo<SelectSignListResVO> selectSignList(SelectSignListReqVO param);

    /* 조직관리 > 서명인관리 > 서명인 상세 조회 */
    public GetSignDetailResVO getSignDetail(GetSignDetailReqVO param);

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 목록 조회 */
    public PageInfo<SelectLogoListResVO> selectLogoList(SelectLogoListReqVO param);

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 상세 조회 */
    public GetLogoDetailResVO getLogoDetail(GetLogoDetailReqVO param);
}
