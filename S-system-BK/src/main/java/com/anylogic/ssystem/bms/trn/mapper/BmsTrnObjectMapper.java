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

import com.anylogic.ssystem.bms.trn.model.SelectTrnObjectListReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectTrnObjectListResVO;
import com.anylogic.ssystem.bms.trn.model.UpdateTrnObjectReqVO;
import org.apache.ibatis.annotations.Mapper;
import com.anylogic.ssystem.bms.trn.model.InsetTrnObjectReqVO;


@Mapper
public interface BmsTrnObjectMapper {

    /* 인계인수 대상 조회*/
    SelectTrnObjectListResVO selectTrnObjectList(SelectTrnObjectListReqVO param) throws RuntimeException;

    /* 인계인수 대상 등록 */
    int insetTrnObject(InsetTrnObjectReqVO param) throws RuntimeException;

    /* 인계인수 대상 수정 */
    int updateTrnObject(UpdateTrnObjectReqVO param) throws RuntimeException;

}
