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
public interface ComGradeMapper {

    /* 조직관리 > 직위/직급관리 > 직급관리 목록 조회 */
    List<SelectGradeListResVO> selectGradeList(SelectGradeListReqVO param) throws RuntimeException;

    /* 조직관리 > 직위/직급관리 > 직급 삭제 */
    int deleteGrade(DeleteGradeReqVO param) throws RuntimeException;

    /* 조직관리 > 직위/직급관리 > 직급 상세 조회 */
    GetGradeDetailResVO getGradeDetail(GetGradeDetailReqVO param) throws RuntimeException;

    /* 조직관리 > 직위/직급관리 > 직급 등록 */
    int insertGrade(InsertGradeReqVO param) throws RuntimeException;

    /* 조직관리 > 직위/직급관리 > 직급 수정 */
    int updateGrade(UpdateGradeReqVO param) throws RuntimeException;

}
