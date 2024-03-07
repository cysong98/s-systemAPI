/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import com.anylogic.ssystem.bms.dct.model.InsertRecvReqVO;
import com.anylogic.ssystem.bms.dct.model.GetDctRecvReqVO;
import com.anylogic.ssystem.bms.dct.model.GetDctRecvResVO;


@Mapper
public interface BmsDctApprRecvMapper {

    /* 수신처 등록 */
    int insertRecv(InsertRecvReqVO param) throws RuntimeException;

    /* 수신처 목록 조회 */
    List<GetDctRecvResVO> getDctRecv(GetDctRecvReqVO param) throws RuntimeException;

    /* 원본 문서의 수신처 목록 조회 */
    List<GetDctRecvResVO> getDctOriRecv(GetDctRecvReqVO param) throws RuntimeException;

}
