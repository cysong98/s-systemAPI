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


import com.anylogic.ssystem.bms.dct.model.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocRcvsMapper;

import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service("BmsDctRdocRcvsService")
@Transactional
public class BmsDctRdocRcvsServiceImpl implements BmsDctRdocRcvsService {

    @Autowired
    private BmsDctRdocRcvsMapper bmsDctRdocRcvsMapper;

    /* 열람권자 조회 */
    @Override
    public List<SelectDctReaderResVO> selectDctReader(SelectDctReaderReqVO param){
        List<SelectDctReaderResVO> resSelectDctReader;
        try {
            resSelectDctReader = bmsDctRdocRcvsMapper.selectDctReader(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resSelectDctReader;
    }


    /* 재사용/인쇄/반출 시 열람권한 보유중인지 체크 */
    @Override
    public HasViewingPermissionResVO hasViewingPermission(HasViewingPermissionReqVO param){
        HasViewingPermissionResVO hasViewingPermissionResVO;
        try {
            param.setDocidscnt(param.getDocids().length);
            hasViewingPermissionResVO = bmsDctRdocRcvsMapper.hasViewingPermission(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return hasViewingPermissionResVO;
    }


}
