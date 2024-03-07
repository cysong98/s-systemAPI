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

import com.anylogic.ssystem.bms.com.model.SelectCodeCategoryListReqVO;
import com.anylogic.ssystem.bms.com.model.SelectCodeCategoryListResVO;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.com.model.DeleteCodeCategoryReqVO;
import com.anylogic.ssystem.bms.com.model.InsertCodeCategoryReqVO;
import com.anylogic.ssystem.bms.com.model.GetCodeCategoryDetailReqVO;
import com.anylogic.ssystem.bms.com.model.GetCodeCategoryDetailResVO;
import com.anylogic.ssystem.bms.com.model.UpdateCodeCategoryReqVO;

import java.util.*;


public interface BmsTcodeService {


    /* 시스템관리 > 코드관리 > 코드분류 목록 조회 */
    public PageInfo<SelectCodeCategoryListResVO> selectCodeCategoryList(SelectCodeCategoryListReqVO param);

    /* 시스템관리 > 코드관리 > 코드분류 삭제 */
    public List<Integer> deleteCodeCategory(List<DeleteCodeCategoryReqVO> param);

    /* 시스템관리 > 코드관리 > 코드분류 등록 */
    public int insertCodeCategory(InsertCodeCategoryReqVO param);

    /* 시스템관리 > 코드관리 > 코드분류 수정 팝업 > 상세조회 */
    public GetCodeCategoryDetailResVO getCodeCategoryDetail(GetCodeCategoryDetailReqVO param);

    /* 시스템관리 > 코드관리 > 코드분류 수정 팝업 > 수정 */
    public int updateCodeCategory(UpdateCodeCategoryReqVO param);

}
