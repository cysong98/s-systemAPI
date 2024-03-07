/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.mapper;

import com.anylogic.ssystem.bms.com.model.*;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;


@Mapper
public interface ComOrganizationinfoMapper {

    /* LDAP 연동된 부서 삭제 */
    int deleteLDAPDept() throws RuntimeException;

    /* LDAP 부서 등록 전 중복 체크 */
    int checkLDAPDeptid(Map<String, Object> param) throws RuntimeException;

    /* 부서 정보 TREE 조회 */
    List<SelectOrganizationListResVO> selectOrganizationList(SelectOrganizationListReqVO param) throws RuntimeException;

    /* 조직관리 > 부서/사용자관리 > 부서 삭제 */
    int deleteOrganizationBIA(DeleteOrganizationBIAReqVO param) throws RuntimeException;

    /* 조직관리 > 부서/사용자관리 > 부서 상세 조회 */
    GetOrganizationDetailResVO getOrganizationDetail(GetOrganizationDetailReqVO param) throws RuntimeException;

    /* 조직관리 > 부서/사용자관리 > 부서 등록 */
    int insertOrganizationBIA(InsertOrganizationBIAReqVO param) throws RuntimeException;

    /* 조직관리 > 부서/사용자관리 > 부서 수정 */
    int updateOrganizationBIA(UpdateOrganizationBIAReqVO param) throws RuntimeException;

    /* 조직관리 > 서명인관리 > 서명인 목록 조회 */
    List<SelectSignListResVO> selectSignList(SelectSignListReqVO param);

    /* 조직관리 > 서명인관리 > 서명인 상세 조회 */
    GetSignDetailResVO getSignDetail(GetSignDetailReqVO param);

    /* 조직관리 > 서명인관리 > 서명인 상세 > 서명인 이미지 목록 조회 */
    List<GetSignDetailSelectFileResVO> getSignDetailSelectFile(GetSignDetailSelectFileReqVO param);

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 목록 조회 */
    List<SelectLogoListResVO> selectLogoList(SelectLogoListReqVO param);

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 상세 조회 */
    GetLogoDetailResVO getLogoDetail(GetLogoDetailReqVO param);

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 상세 조회 > 심볼/로고 이미지 목록 조회 */
    List<GetLogoDetailSelectFileResVO> getLogoDetailSelectFile(GetLogoDetailSelectFileReqVO param);
}
