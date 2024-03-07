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
import com.anylogic.ssystem.bms.dct.model.InsertSendRecvReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectSendRecvListReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectSendRecvListResVO;


@Mapper
public interface BmsDctSndrcvRegiMapper {

    /* 비밀수발대장 등록 */
    int insertSendRecv(InsertSendRecvReqVO param) throws RuntimeException;

    /* sndrcv_regi_seq 조회 */
    String getSndrcvseq() throws RuntimeException;

}
