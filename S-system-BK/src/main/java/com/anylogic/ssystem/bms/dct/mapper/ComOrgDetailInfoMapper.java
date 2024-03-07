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
import com.anylogic.ssystem.bms.dct.model.SelectGawninReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectGawninResVO;
import com.anylogic.ssystem.bms.dct.model.SelectSignReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectSignResVO;


@Mapper
public interface ComOrgDetailInfoMapper {

    /* 관인 팝업 조회 */
    List<SelectGawninResVO> selectGawnin(SelectGawninReqVO param) throws RuntimeException;

    /* 서명인 팝업 조회 */
    List<SelectSignResVO> selectSign(SelectSignReqVO param) throws RuntimeException;

}
