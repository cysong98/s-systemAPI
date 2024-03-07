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

import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import com.anylogic.ssystem.bms.com.model.DeleteOrganizationDetailReqVO;
import com.anylogic.ssystem.bms.com.model.InsertOrganizationDetailReqVO;
import com.anylogic.ssystem.bms.com.model.UpdateOrganizationDetailReqVO;


@Mapper
public interface ComOrgDetailMapper {

    /* 조직관리 > 부서/사용자관리 > 부서 디테일 삭제 */
    int deleteOrganizationDetail(DeleteOrganizationDetailReqVO param) throws RuntimeException;

    /* 조직관리 > 부서/사용자관리 > 부서 디테일 등록 */
    int insertOrganizationDetail(InsertOrganizationDetailReqVO param) throws RuntimeException;

    /* 조직관리 > 부서/사용자관리 > 부서 디테일 수정 */
    int updateOrganizationDetail(UpdateOrganizationDetailReqVO param) throws RuntimeException;

}
