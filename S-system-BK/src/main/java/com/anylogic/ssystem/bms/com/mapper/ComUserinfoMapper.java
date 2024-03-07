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
public interface ComUserinfoMapper {

    /* 사용 요청 시 (파일반출/존안/이관) 담당자 정보 조회 */
    List<SelectFindManagerResVO> selectFindManager(SelectFindManagerReqVO param) throws RuntimeException;

    /* 조직관리 > 부서/사용자관리 > 사용자 목록 조회 */
    List<SelectUserListResVO> selectUserList(SelectUserListReqVO param) throws RuntimeException;

    /* 조직관리 > 부서/사용자관리 > 사용자 삭제 */
    int deleteUserBIA(DeleteUserBIAReqVO param) throws RuntimeException;

    /* 조직관리 > 부서/사용자관리 > 사용자 상세 조회 */
    List<GetUserDetailResVO> getUserDetail(GetUserDetailReqVO param) throws RuntimeException;

    /* 조직관리 > 부서/사용자관리 > 사용자 등록 > 아이디 중복확인 */
    CheckUserIdResVO checkUserId(CheckUserIdReqVO param) throws RuntimeException;

    /* 조직관리 > 부서/사용자관리 > 사용자 등록 */
    int insertUserBIA(InsertUserBIAReqVO param) throws RuntimeException;

    /* 조직관리 > 부서/사용자관리 > 사용자 수정 */
    int updateUserBIA(UpdateUserBIAReqVO param) throws RuntimeException;

}
