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

import com.anylogic.ssystem.bms.dct.model.InsertDocSendInfoReqVO;
import com.anylogic.ssystem.bms.dct.model.InsertOpinionReqVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import com.anylogic.ssystem.bms.dct.model.SelectSendinfoReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectSendinfoResVO;


@Mapper
public interface BmsDctDocSendinfoMapper {

    /* 비밀 배포카드 팝업 > 발송의견 조회 */
    List<SelectSendinfoResVO> selectSendinfo(SelectSendinfoReqVO param) throws RuntimeException;

    /* 발송의견 등록 */
    int insertDctDocSendinfo(InsertDocSendInfoReqVO param) throws RuntimeException;

}
