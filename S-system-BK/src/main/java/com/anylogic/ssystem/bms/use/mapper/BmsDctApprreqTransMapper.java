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
import com.anylogic.ssystem.bms.use.model.InsertComingTransApproveBIAReqVO;


@Mapper
public interface BmsDctApprreqTransMapper {

    /* 내부이관, 이관대기 요청내용 등록 */
    int insertComingTransApproveBIA(InsertComingTransApproveBIAReqVO param) throws RuntimeException;

    /* 이관연기 요청내용 등록 */
    int insertComingTransDelayBIA(InsertComingTransApproveBIAReqVO param) throws RuntimeException;

    /* 각 요청서 승인/반려 처리 - 승인한 이관대기정보 수정 */
    int updateUseReqMapTransWait(HashMap<String, Object> param) throws RuntimeException;

    /* 각 요청서 승인/반려 처리 - 승인한 내부이관, 이관연기정보 수정 */
    int updateUseReqMapTrans(HashMap<String, Object> param) throws RuntimeException;

    /* 이관대기 요청서 승인/반려 처리 - 승인한 열람범위 정보 수정 */
    int updateUseReqMapTransRdoc(HashMap<String, Object> param) throws RuntimeException;

    /* 각 요청서 상세화면 조회 - 이관대기, 이관연기, 내부이관할 문서 정보 */
    List<HashMap<String, Object>> selectUseReqTransMapList(HashMap<String, Object> param) throws RuntimeException;

}
