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
import com.anylogic.ssystem.bms.com.model.SelectCodeCategoryListReqVO;
import com.anylogic.ssystem.bms.com.model.SelectCodeCategoryListResVO;
import com.anylogic.ssystem.bms.com.model.DeleteCodeCategoryReqVO;
import com.anylogic.ssystem.bms.com.model.InsertCodeCategoryReqVO;
import com.anylogic.ssystem.bms.com.model.GetCodeCategoryDetailReqVO;
import com.anylogic.ssystem.bms.com.model.GetCodeCategoryDetailResVO;
import com.anylogic.ssystem.bms.com.model.UpdateCodeCategoryReqVO;


@Mapper
public interface BmsTcodeMapper {

    /* 시스템관리 > 코드관리 > 코드분류 목록 조회 */
    List<SelectCodeCategoryListResVO> selectCodeCategoryList(SelectCodeCategoryListReqVO param) throws RuntimeException;

    /* 시스템관리 > 코드관리 > 코드분류 삭제 */
    int deleteCodeCategory(DeleteCodeCategoryReqVO param) throws RuntimeException;

    /* 시스템관리 > 코드관리 > 코드분류 등록 */
    int insertCodeCategory(InsertCodeCategoryReqVO param) throws RuntimeException;

    /* 시스템관리 > 코드관리 > 코드분류 수정 팝업 > 상세조회 */
    GetCodeCategoryDetailResVO getCodeCategoryDetail(GetCodeCategoryDetailReqVO param) throws RuntimeException;

    /* 시스템관리 > 코드관리 > 코드분류 수정 팝업 > 수정 */
    int updateCodeCategory(UpdateCodeCategoryReqVO param) throws RuntimeException;

}
