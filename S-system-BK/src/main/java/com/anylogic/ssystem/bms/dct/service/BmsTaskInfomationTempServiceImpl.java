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
import com.anylogic.ssystem.bms.dct.mapper.BmsTaskInfomationTempMapper;
import com.anylogic.ssystem.bms.dct.model.SelectTaskInfoTempReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectTaskInfoTempResVO;
import java.util.*;



@Slf4j
@RequiredArgsConstructor
@Service("BmsTaskInfomationTempService")
@Transactional
public class BmsTaskInfomationTempServiceImpl implements BmsTaskInfomationTempService {


    @Autowired
    private BmsTaskInfomationTempMapper bmsTaskInfomationTempMapper;


    /* 임시저장된 관련근거 조회 */
    @Override
    public List<SelectTaskInfoTempResVO> selectTaskInfoTemp(SelectTaskInfoTempReqVO param){
        List<SelectTaskInfoTempResVO> resultListVO = new ArrayList<SelectTaskInfoTempResVO>();
        try {
            resultListVO = bmsTaskInfomationTempMapper.selectTaskInfoTemp(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 기안완료된 관련근거 조회 */
    @Override
    public List<SelectTaskInfoTempResVO> selectTaskInfo(SelectTaskInfoTempReqVO param){
        List<SelectTaskInfoTempResVO> resultListVO = new ArrayList<SelectTaskInfoTempResVO>();
        try {
            resultListVO = bmsTaskInfomationTempMapper.selectTaskInfo(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

}
