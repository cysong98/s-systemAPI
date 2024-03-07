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


import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.dct.mapper.ComOrganizationinfoMapper;
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationResVO;
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationInfoReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationInfoResVO;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import java.util.Map;
import com.anylogic.ssystem.bms.dct.model.SelectSymbolReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectSymbolResVO;



@Slf4j
@RequiredArgsConstructor
@Service("ComOrganizationinfoService")
@Transactional
public class ComOrganizationinfoServiceImpl implements ComOrganizationinfoService {


    @Autowired
    private ComOrganizationinfoMapper comOrganizationinfoMapper;


    /* 관리자쪽 제외 팝업 > 부서 TREE 조회 */
    @Override
    public List<SelectOrganizationResVO> selectOrganization(SelectOrganizationReqVO param){
        List<SelectOrganizationResVO> resultListVO = new ArrayList<SelectOrganizationResVO>();
        try {
            PageHelper.clearPage();
            resultListVO = comOrganizationinfoMapper.selectOrganization(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* LDAP 연동된 부서 정보 조회 */
    public List<SelectOrganizationResVO> selectOrganizationLDAP(SelectOrganizationReqVO param){
        List<SelectOrganizationResVO> resultListVO = new ArrayList<SelectOrganizationResVO>();
        try {
            PageHelper.clearPage();
            resultListVO = comOrganizationinfoMapper.selectOrganizationLDAP(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 부서장 정보 조회 */
    @Override
    public List<SelectOrganizationInfoResVO> selectOrganizationInfo(SelectOrganizationInfoReqVO param){
        List<SelectOrganizationInfoResVO> resultListVO = new ArrayList<SelectOrganizationInfoResVO>();
        try {
            PageHelper.clearPage();
            resultListVO = comOrganizationinfoMapper.selectOrganizationInfo(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 대리 결재자 정보 조회 */
    @Override
    public List<SelectOrganizationInfoResVO> selectManagerInfo(SelectOrganizationInfoReqVO param){
        List<SelectOrganizationInfoResVO> resultListVO = new ArrayList<SelectOrganizationInfoResVO>();
        try {
            PageHelper.clearPage();
            resultListVO = comOrganizationinfoMapper.selectManagerInfo(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }



    /* 로고/심볼 팝업 조회 */
    @Override
    public List<SelectSymbolResVO> selectSymbol(SelectSymbolReqVO param){
        List<SelectSymbolResVO> resultListVO = new ArrayList<SelectSymbolResVO>();
        try {
            PageHelper.clearPage();
            resultListVO = comOrganizationinfoMapper.selectSymbol(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }
}
