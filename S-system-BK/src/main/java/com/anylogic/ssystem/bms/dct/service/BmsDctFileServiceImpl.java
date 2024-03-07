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
import com.anylogic.ssystem.bms.dct.mapper.BmsDctFileMapper;
import com.anylogic.ssystem.bms.dct.model.SelectDctFileReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectDctFileResVO;
import java.util.*;



@Slf4j
@RequiredArgsConstructor
@Service("BmsDctFileService")
@Transactional
public class BmsDctFileServiceImpl implements BmsDctFileService {


    @Autowired
    private BmsDctFileMapper bmsDctFileMapper;

    /* 기안문, 붙임 파일 조회 */
    @Override
    public List<SelectDctFileResVO> selectDctFile(SelectDctFileReqVO param){
        List<SelectDctFileResVO> resSelectDctFile;
        try {
            resSelectDctFile = bmsDctFileMapper.selectDctFile(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resSelectDctFile;
    }

    /* 발송 실패 오프라인 인쇄시 파일 조회 */
    @Override
    public List<SelectDctFileResVO> selectSendFileList(SelectDctFileReqVO param){
        List<SelectDctFileResVO> resSelectSendFileList;
        try {
            resSelectSendFileList = bmsDctFileMapper.selectSendFileList(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resSelectSendFileList;
    }

    /* 파일 조회 */
    @Override
    public List<SelectDctFileResVO> selectCommonDctFile(SelectDctFileReqVO param){
        List<SelectDctFileResVO> resSelectDctFile;
        try {
            resSelectDctFile = bmsDctFileMapper.selectCommonDctFile(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resSelectDctFile;
    }

}
