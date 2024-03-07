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

import com.anylogic.ssystem.bms.dct.model.InsertRecvConfirmBIAReqVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import com.anylogic.ssystem.bms.dct.model.SelectReceiptReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectReceiptResVO;


@Mapper
public interface BmsDctInvrcptMapper {

    /* 접수대기함, 접수현황함, 송증영수증 접수정보 조회 */
    SelectReceiptResVO selectReceipt(SelectReceiptReqVO param) throws RuntimeException;

    /* 송증영수증 등록 */
    int insertReceipt(SelectReceiptResVO param) throws RuntimeException;

    /* 송증영수증 수정 */
    int updateReceipt(SelectReceiptResVO param) throws RuntimeException;

}
