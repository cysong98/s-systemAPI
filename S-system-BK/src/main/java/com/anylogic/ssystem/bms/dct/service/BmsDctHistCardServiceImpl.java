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


import com.anylogic.ssystem.bms.dct.model.GetHistCardInfoReqVO;
import com.anylogic.ssystem.bms.dct.model.GetHistCardInfoResVO;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctHistCardMapper;
import com.anylogic.ssystem.bms.dct.model.UpdateDctHistCardReqVO;
import java.util.*;



@Slf4j
@RequiredArgsConstructor
@Service("BmsDctHistCardService")
@Transactional
public class BmsDctHistCardServiceImpl implements BmsDctHistCardService {


    @Autowired
    private BmsDctHistCardMapper bmsDctHistCardMapper;

    /* 비밀 이력 팝업 조회 */
    @Override
    public List<GetHistCardInfoResVO> getHistCardInfo(GetHistCardInfoReqVO param){
        List<GetHistCardInfoResVO> getHistCardInfoResVO;
        try {
            getHistCardInfoResVO = bmsDctHistCardMapper.getHistCardInfo(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return getHistCardInfoResVO;
    }

}
