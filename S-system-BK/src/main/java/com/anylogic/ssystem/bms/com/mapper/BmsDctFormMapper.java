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
public interface BmsDctFormMapper {

    /* 시스템관리 > 서식관리 > 서식 목록 조회 */
    List<SelectFormListResVO> selectFormList(SelectFormListReqVO param) throws RuntimeException;

    /* 시스템관리 > 서식관리 > 서식 삭제 */
    int deleteForm(DeleteFormReqVO param) throws RuntimeException;

    /* 시스템관리 > 서식관리 > 삭제할 서식파일 목록 조회 */
    List<DeleteFormSelectFileResVO> deleteFormSelectFile(DeleteFormSelectFileReqVO param);

    /* 시스템관리 > 서식관리 > 서식 상세 조회 */
    GetFormDetailResVO getFormDetail(GetFormDetailReqVO param) throws RuntimeException;

    /* 시스템관리 > 서식관리 > 상세 > 서식파일 목록 조회 */
    List<GetFormDetailSelectFileResVO> getFormDetailSelectFile(GetFormDetailSelectFileReqVO param) throws RuntimeException;

    /* 시스템관리 > 서식관리 > 서식 등록 */
    int insertFormBIA(InsertFormBIAReqVO param) throws RuntimeException;

    /* 시스템관리 > 서식관리 > 서식 수정 */
    int updateFormBIA(UpdateFormBIAReqVO param) throws RuntimeException;

}
