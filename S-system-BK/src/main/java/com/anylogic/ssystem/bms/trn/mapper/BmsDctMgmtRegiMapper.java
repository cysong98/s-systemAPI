/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.trn.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import com.anylogic.ssystem.bms.trn.model.SelectMgmtRegiSecStatusReClasscontCountReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectMgmtRegiSecStatusReClasscontCountResVO;
import com.anylogic.ssystem.bms.trn.model.SelectSendListCountReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectSendListCountResVO;
import com.anylogic.ssystem.bms.trn.model.SelectDctNotCompleteListReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectDctNotCompleteListResVO;
import com.anylogic.ssystem.bms.trn.model.SelectDctProdMgmtCardListReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectDctProdMgmtCardListResVO;
import com.anylogic.ssystem.bms.trn.model.SelectReClassMgmgCardListReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectReClassMgmgCardListResVO;
import com.anylogic.ssystem.bms.trn.model.UpdateMgmtRegiReqVO;


@Mapper
public interface BmsDctMgmtRegiMapper {

    /* 인계인수시 비밀관리기록부 상태값 수정 */
    int updateMgmtRegi(UpdateMgmtRegiReqVO param) throws RuntimeException;

}
