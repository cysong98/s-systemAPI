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
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


public interface BmsDctFormService {

    /* 시스템관리 > 서식관리 > 서식 목록 조회 */
    public PageInfo<SelectFormListResVO> selectFormList(SelectFormListReqVO param);

    /* 시스템관리 > 서식관리 > 서식 삭제 */
    public List<Integer> deleteForm(List<DeleteFormReqVO> param);

    /* 시스템관리 > 서식관리 > 서식 상세 조회 */
    public GetFormDetailResVO getFormDetail(GetFormDetailReqVO param);

    /* 시스템관리 > 서식관리 > 서식 등록 */
    public int insertFormBIA(List<MultipartFile> multipartFiles, InsertFormBIAReqVO param);

    /* 시스템관리 > 서식관리 > 서식 수정 */
    public int updateFormBIA(List<MultipartFile> multipartFiles, UpdateFormBIAReqVO param);

}
