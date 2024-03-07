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


@Mapper
public interface ComUserinfoDetailMapper {

    /* 개인정보 상세 삭제 */
    int deleteUserDetail(DeleteUserDetailReqVO param) throws RuntimeException;

    /* 개인정보 상세 등록 */
    int insertUserDetail(InsertUserDetailReqVO param) throws RuntimeException;

    /* 개인정보 상세 수정 */
    int updateUserDetail(UpdateUserDetailReqVO param) throws RuntimeException;
}
