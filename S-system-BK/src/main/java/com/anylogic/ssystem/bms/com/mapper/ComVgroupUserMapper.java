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
public interface ComVgroupUserMapper {

    /* 사용자 권한 보유 목록 조회 */
    List<SelectVgroupUserResVO> selectVgroupUser(SelectVgroupUserReqVO param) throws RuntimeException;

    /* 사용자 권한 삭제 */
    int deleteUserVgroup(DeleteUserVgroupReqVO param) throws RuntimeException;

    /* 사용자 권한 등록 */
    int insertUserVgroup(InsertUserVgroupReqVO param) throws RuntimeException;

}
