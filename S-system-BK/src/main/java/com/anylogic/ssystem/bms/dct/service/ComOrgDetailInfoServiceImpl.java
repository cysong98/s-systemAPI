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
import com.anylogic.ssystem.bms.dct.mapper.ComOrgDetailInfoMapper;
import com.anylogic.ssystem.bms.dct.model.SelectGawninReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectGawninResVO;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import java.util.Map;
import com.anylogic.ssystem.bms.dct.model.SelectSignReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectSignResVO;



@Slf4j
@RequiredArgsConstructor
@Service("ComOrgDetailInfoService")
@Transactional
public class ComOrgDetailInfoServiceImpl implements ComOrgDetailInfoService {
    @Autowired
    private ComOrgDetailInfoMapper comOrgDetailInfoMapper;


    /* 관인 팝업 조회 */
    @Override
    public List<SelectGawninResVO> selectGawnin(SelectGawninReqVO param)  {
        List<SelectGawninResVO> resultListVO = new ArrayList<SelectGawninResVO>();
        try {
            resultListVO = comOrgDetailInfoMapper.selectGawnin(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 서명인 팝업 조회 */
    @Override
    public List<SelectSignResVO> selectSign(SelectSignReqVO param) {
        List<SelectSignResVO> resultListVO = new ArrayList<SelectSignResVO>();
        try {
            resultListVO = comOrgDetailInfoMapper.selectSign(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }
}
