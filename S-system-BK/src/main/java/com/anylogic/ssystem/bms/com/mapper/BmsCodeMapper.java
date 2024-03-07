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
public interface BmsCodeMapper {

    /* 시스템관리 > 코드관리 > 코드 목록 조회 */
    List<SelectCodeListResVO> selectCodeList(SelectCodeListReqVO param) throws RuntimeException;

    /* 수신지정팝업 > 외부 > 수신자기호 조회 */
    List<SelectCodeListResVO> selectReceiveMarkCodeList(SelectReceiveMarkCodeListReqVO param) throws RuntimeException;

    /* 비전자비밀접수 > 보관함/단번호 조회 */
    List<SelectCodeListResVO> selectOfflineCabinetInfo(SelectReceiveMarkCodeListReqVO param) throws RuntimeException;

    /* 시스템관리 > 코드관리 > 코드 삭제 */
    int deleteCode(DeleteCodeReqVO param) throws RuntimeException;

    /* 시스템관리 > 코드관리 > 코드 등록 */
    int insertCode(InsertCodeReqVO param) throws RuntimeException;

    /* 시스템관리 > 코드관리 > 코드분류 수정 팝업 > 상세조회 */
    GetCodeDetailResVO getCodeDetail(GetCodeDetailReqVO param) throws RuntimeException;

    /* 시스템관리 > 코드관리 > 코드분류 수정 팝업 > 수정 */
    int updateCode(UpdateCodeReqVO param) throws RuntimeException;

}
