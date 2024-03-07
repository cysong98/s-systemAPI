/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.trn.service;


import com.anylogic.ssystem.bms.dct.model.InsertHistoryReqVO;
import com.anylogic.ssystem.bms.login.model.GetUserLoginReqVO;
import com.anylogic.ssystem.bms.login.model.GetUserLoginResVO;
import com.anylogic.ssystem.bms.trn.model.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.trn.mapper.BmsTrnObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;



@Slf4j
@RequiredArgsConstructor
@Service("BmsTrnObjectService")
@Transactional
public class BmsTrnObjectServiceImpl implements BmsTrnObjectService {

    @Autowired
    private BmsTrnObjectMapper bmsTrnObjectMapper;

    /* 인계인수 대상 목록 조회*/
    @Override
    public int selectTrnObjectList(InsertTrnStepReqVO param) {
        int res = -1;
        for(InsetTrnObjectReqVO vo : param.getSelectList()) {
            SelectTrnObjectListResVO resVO = bmsTrnObjectMapper.selectTrnObjectList(SelectTrnObjectListReqVO.builder()
                                                                                            .objectid(vo.getObjectid())
                                                                                            .build());
            // 반려(MGT99) or 인수완료(MGT52)
            if(resVO != null) {
                if(resVO.getSecstatus().equals("MGT99") || resVO.getSecstatus().equals("MGT52")) {
                    res = 1;
                    break;
                }
            }
            else
                res = 0;
        }
        return res;
    }

}
