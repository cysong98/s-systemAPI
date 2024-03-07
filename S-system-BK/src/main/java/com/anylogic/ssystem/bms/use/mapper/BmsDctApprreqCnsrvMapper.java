/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import com.anylogic.ssystem.bms.use.model.InsertComingCnsrvApproveBIAReqVO;


@Mapper
public interface BmsDctApprreqCnsrvMapper {

    /* 존안 요청내용 등록 */
    int insertComingCnsrvApproveBIA(InsertComingCnsrvApproveBIAReqVO param) throws RuntimeException;

    /* 각 요청서 승인/반려 처리 - 승인한 존안정보 수정 */
    int updateUseReqMapCnsrv(HashMap<String, Object> param) throws RuntimeException;

    /* 각 요청서 상세화면 조회 - 존안할 문서 정보 */
    List<HashMap<String, Object>> selectUseReqCnsrvMapList(HashMap<String, Object> param) throws RuntimeException;


}
