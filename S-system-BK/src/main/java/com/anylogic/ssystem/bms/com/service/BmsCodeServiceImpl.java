/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.service;


import com.anylogic.ssystem.bms.com.model.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.BmsCodeMapper;

import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service("BmsCodeService")
@Transactional
public class BmsCodeServiceImpl implements BmsCodeService {


    @Autowired
    private BmsCodeMapper bmsCodeMapper;

    /* 시스템관리 > 코드관리 > 코드 목록 조회 */
    @Override
    public PageInfo<SelectCodeListResVO> selectCodeList(SelectCodeListReqVO param){
        PageInfo<SelectCodeListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(bmsCodeMapper.selectCodeList(param));

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 수신지정팝업 > 외부 > 수신자기호 조회 */
    @Override
    public List<SelectCodeListResVO> selectReceiveMarkCodeList(SelectReceiveMarkCodeListReqVO param) {
        List<SelectCodeListResVO> resultListVO = null;
        try {
            resultListVO = bmsCodeMapper.selectReceiveMarkCodeList(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 비전자비밀접수 > 보관함/단번호 조회 */
    @Override
    public List<SelectCodeListResVO> selectOfflineCabinetInfo(SelectReceiveMarkCodeListReqVO param) {
        List<SelectCodeListResVO> resultListVO = null;
        try {
            resultListVO = bmsCodeMapper.selectOfflineCabinetInfo(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 시스템관리 > 코드관리 > 코드 삭제 */
    @Override
    public List<Integer> deleteCode(List<DeleteCodeReqVO> params){
        List<Integer> res = new ArrayList<>();
        try {
            for (DeleteCodeReqVO param : params) {
                int ares = bmsCodeMapper.deleteCode(param);
                res.add(ares);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* 시스템관리 > 코드관리 > 코드 등록 */
    @Override
    public int insertCode(InsertCodeReqVO param){
        int resInsertCode = 0;
        try {
            resInsertCode = bmsCodeMapper.insertCode(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertCode;
    }


    /* 시스템관리 > 코드관리 > 코드 수정 팝업 > 상세조회 */
    @Override
    public GetCodeDetailResVO getCodeDetail(GetCodeDetailReqVO param){
        GetCodeDetailResVO resGetCodeDetail;
        try {
            resGetCodeDetail = bmsCodeMapper.getCodeDetail(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetCodeDetail == null){
            resGetCodeDetail = new GetCodeDetailResVO();
        }
        return resGetCodeDetail;
    }


    /* 시스템관리 > 코드관리 > 코드 수정 팝업 > 수정 */
    @Override
    public int updateCode(UpdateCodeReqVO param){
        int resUpdateCode = 0;
        try {
            resUpdateCode = bmsCodeMapper.updateCode(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateCode;
    }


}
