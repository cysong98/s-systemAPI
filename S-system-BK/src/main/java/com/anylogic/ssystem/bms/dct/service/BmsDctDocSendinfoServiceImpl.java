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
import com.anylogic.ssystem.bms.dct.mapper.BmsDctDocSendinfoMapper;
import com.anylogic.ssystem.bms.dct.model.SelectSendinfoReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectSendinfoResVO;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import java.util.Map;



@Slf4j
@RequiredArgsConstructor
@Service("BmsDctDocSendinfoService")
@Transactional
public class BmsDctDocSendinfoServiceImpl implements BmsDctDocSendinfoService {

    @Autowired
    private BmsDctDocSendinfoMapper bmsDctDocSendinfoMapper;


    /* 비밀 배포카드 팝업 > 발송의견 조회 */
    @Override
    public List<SelectSendinfoResVO> selectSendinfo(SelectSendinfoReqVO param){
        List<SelectSendinfoResVO> resultListVO = new ArrayList<SelectSendinfoResVO>();
        try {
            resultListVO = bmsDctDocSendinfoMapper.selectSendinfo(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }
}
