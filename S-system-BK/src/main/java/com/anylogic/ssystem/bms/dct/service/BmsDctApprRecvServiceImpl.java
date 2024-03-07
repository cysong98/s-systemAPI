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
import com.anylogic.ssystem.bms.dct.mapper.BmsDctApprRecvMapper;
import com.anylogic.ssystem.bms.dct.model.GetDctRecvReqVO;
import com.anylogic.ssystem.bms.dct.model.GetDctRecvResVO;
import java.util.*;



@Slf4j
@RequiredArgsConstructor
@Service("BmsDctApprRecvService")
@Transactional
public class BmsDctApprRecvServiceImpl implements BmsDctApprRecvService {

    @Autowired
    private BmsDctApprRecvMapper bmsDctApprRecvMapper;

    /* 수신처 목록 조회 */
    @Override
    public List<GetDctRecvResVO> getDctRecv(GetDctRecvReqVO param){
        List<GetDctRecvResVO> resultListVO = new ArrayList<GetDctRecvResVO>();
        try {
            resultListVO = bmsDctApprRecvMapper.getDctRecv(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 원본 문서의 수신처 목록 조회 */
    @Override
    public List<GetDctRecvResVO> getDctOriRecv(GetDctRecvReqVO param){
        List<GetDctRecvResVO> resultListVO = new ArrayList<GetDctRecvResVO>();
        try {
            resultListVO = bmsDctApprRecvMapper.getDctOriRecv(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


}
