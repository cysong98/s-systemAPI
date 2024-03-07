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


import com.anylogic.ssystem.bms.dct.model.GetDctAttrAddReqVO;
import com.anylogic.ssystem.bms.dct.model.GetDctAttrAddResVO;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctAttrMapper;
import com.anylogic.ssystem.bms.dct.model.GetDctAttrReqVO;
import com.anylogic.ssystem.bms.dct.model.GetDctAttrResVO;
import java.util.*;



@Slf4j
@RequiredArgsConstructor
@Service("BmsDctAttrService")
@Transactional
public class BmsDctAttrServiceImpl implements BmsDctAttrService {

    @Autowired
    private BmsDctAttrMapper bmsDctAttrMapper;

    /* 비밀 속성 상세 조회 */
    @Override
    public GetDctAttrResVO getDctAttr(GetDctAttrReqVO param){
        GetDctAttrResVO resGetDctAttr;
        try {
            resGetDctAttr = bmsDctAttrMapper.getDctAttr(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetDctAttr == null){
            resGetDctAttr = new GetDctAttrResVO();
        }
        return resGetDctAttr;
    }


    /* 비전자 비밀 속성 상세 조회 */
    @Override
    public GetDctAttrAddResVO getDctAttrAdd(GetDctAttrAddReqVO param){
        GetDctAttrAddResVO resGetDctAttrAdd;
        try {
            resGetDctAttrAdd = bmsDctAttrMapper.getDctAttrAdd(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetDctAttrAdd == null){
            resGetDctAttrAdd = new GetDctAttrAddResVO();
        }
        return resGetDctAttrAdd;
    }

}
