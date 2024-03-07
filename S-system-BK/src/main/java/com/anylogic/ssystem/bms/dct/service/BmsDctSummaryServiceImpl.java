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


import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctSummaryMapper;
import com.anylogic.ssystem.bms.dct.model.GetDctSummaryReqVO;
import com.anylogic.ssystem.bms.dct.model.GetDctSummaryResVO;
import java.util.*;



@Slf4j
@RequiredArgsConstructor
@Service("BmsDctSummaryService")
@Transactional
public class BmsDctSummaryServiceImpl implements BmsDctSummaryService {


    @Autowired
    private BmsDctSummaryMapper bmsDctSummaryMapper;

    /* 문서취지 조회 */
    @Override
    public GetDctSummaryResVO getDctSummary(GetDctSummaryReqVO param){
        GetDctSummaryResVO resGetDctSummary;
        try {
            resGetDctSummary = bmsDctSummaryMapper.getDctSummary(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetDctSummary == null){
            resGetDctSummary = new GetDctSummaryResVO();
        }
        return resGetDctSummary;
    }

}
