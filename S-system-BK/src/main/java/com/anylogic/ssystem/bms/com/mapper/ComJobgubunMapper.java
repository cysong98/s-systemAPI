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
public interface ComJobgubunMapper {

    /* 조직관리 > 직위/직급관리 > 직위관리 목록 조회 */
    List<SelectJobgubunListResVO> selectJobgubunList(SelectJobgubunListReqVO param) throws RuntimeException;

    /* 조직관리 > 직위/직급관리 > 직위 삭제 */
    int deleteJobgubun(DeleteJobgubunReqVO param) throws RuntimeException;

    /* 조직관리 > 직위/직급관리 > 직위 상세 조회 */
    GetJobgubunDetailResVO getJobgubunDetail(GetJobgubunDetailReqVO param) throws RuntimeException;

    /* 조직관리 > 직위/직급관리 > 직위 등록 */
    int insertJobgubun(InsertJobgubunReqVO param) throws RuntimeException;

    /* 조직관리 > 직위/직급관리 > 직위 수정 */
    int updateJobgubun(UpdateJobgubunReqVO param) throws RuntimeException;

}
