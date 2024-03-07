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


public interface ComUserinfoService {

    /* 사용 요청 시 (파일반출/존안/이관) 담당자 정보 조회 */
    public List<SelectFindManagerResVO> selectFindManager(SelectFindManagerReqVO param);

    /* 조직관리 > 부서/사용자관리 > 사용자 목록 조회 */
    public PageInfo<SelectUserListResVO> selectUserList(SelectUserListReqVO param);

    /* 조직관리 > 부서/사용자관리 > 사용자 삭제 */
    public List<Integer> deleteUserBIA(List<DeleteUserBIAReqVO> param);

    /* 조직관리 > 부서/사용자관리 > 사용자 상세 조회 */
    public List<GetUserDetailResVO> getUserDetail(GetUserDetailReqVO param);

    /* 조직관리 > 부서/사용자관리 > 사용자 등록 > 아이디 중복확인 */
    public CheckUserIdResVO checkUserId(CheckUserIdReqVO param);

    /* 조직관리 > 부서/사용자관리 > 사용자 등록 */
    public int insertUserBIA(InsertUserBIAReqVO param);

    /* 조직관리 > 부서/사용자관리 > 사용자 수정 */
    public int updateUserBIA(UpdateUserBIAReqVO param);


}
