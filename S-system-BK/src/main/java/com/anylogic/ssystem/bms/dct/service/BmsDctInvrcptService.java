/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.service;

import com.anylogic.ssystem.bms.dct.model.InsertRecvConfirmBIAReqVO;
import com.anylogic.ssystem.bms.dct.model.InsertRecvInvrcptReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectReceiptReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectReceiptResVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import java.util.*;


public interface BmsDctInvrcptService {

    /* 접수대기함, 접수현황함, 송증영수증 접수정보 조회 */
    public SelectReceiptResVO selectReceipt(SelectReceiptReqVO param);

    public int insertReceipt(SelectReceiptReqVO param);

    /* 오프라인발송 문서에 대한 송증/영수증 서명입력, 접수대기처리 */
    public int insertRecvInvrcpt(InsertRecvInvrcptReqVO param, List<MultipartFile> refAttach);

}
