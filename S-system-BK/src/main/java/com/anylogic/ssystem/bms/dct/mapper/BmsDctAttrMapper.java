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

import com.anylogic.ssystem.bms.dct.model.*;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;


@Mapper
public interface BmsDctAttrMapper {

    /* 비밀속성 등록 */
    int insertAttribute(InsertAttributeReqVO param) throws RuntimeException;

    /* 비전자 비밀속성 관리정보 등록 */
    int insertAttrAdd(InsertAttrAddReqVO param) throws RuntimeException;

    /* 비밀속성 수정 */
    int updateDctAttr(UpdateDctAttrReqVO param) throws RuntimeException;

    /* 비밀 속성 상세 조회 */
    GetDctAttrResVO getDctAttr(GetDctAttrReqVO param) throws RuntimeException;

    /* 비전자 비밀 속성 상세 조회 */
    GetDctAttrAddResVO getDctAttrAdd(GetDctAttrAddReqVO param) throws RuntimeException;

}
