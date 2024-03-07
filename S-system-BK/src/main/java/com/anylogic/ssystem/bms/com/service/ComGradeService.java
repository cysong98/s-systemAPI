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
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface ComGradeService {

    /* 조직관리 > 직위/직급관리 > 직급관리 목록 조회 */
    public PageInfo<SelectGradeListResVO> selectGradeList(SelectGradeListReqVO param);

    /* 조직관리 > 직위/직급관리 > 직급 삭제 */
    public List<Integer> deleteGrade(List<DeleteGradeReqVO> param);

    /* 조직관리 > 직위/직급관리 > 직급 상세 조회 */
    public GetGradeDetailResVO getGradeDetail(GetGradeDetailReqVO param);

    /* 조직관리 > 직위/직급관리 > 직급 등록 */
    public int insertGrade(InsertGradeReqVO param);

    /* 조직관리 > 직위/직급관리 > 직급 수정 */
    public int updateGrade(UpdateGradeReqVO param);

}
