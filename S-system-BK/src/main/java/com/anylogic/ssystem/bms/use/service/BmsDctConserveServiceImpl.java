/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.service;


import com.anylogic.ssystem.bms.dct.model.SelectMgmtRegiListReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectMgmtRegiListResVO;
import com.anylogic.ssystem.bms.use.mapper.BmsDctConserveMapper;
import com.anylogic.ssystem.bms.use.mapper.BmsDctMgmtRegiMapper;
import com.anylogic.ssystem.bms.use.model.*;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Service("BmsDctConserveService")
@Transactional
public class BmsDctConserveServiceImpl implements BmsDctConserveService {

    @Autowired
    private BmsDctConserveMapper bmsDctConserveMapper;

    /* 존안 비밀관리기록부 목록 조회 */
    @Override
    public PageInfo<SelectMgmtRegiListResVO> selectUseConserveMgmtList(SelectUserReqestsListReqVO param){
        PageInfo<SelectMgmtRegiListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(param.getStartDt() != null){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(param.getEndDt() != null){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctConserveMapper.selectUseConserveMgmtList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 이관 비밀관리기록부 목록 조회 */
    @Override
    public PageInfo<SelectMgmtRegiListResVO> selectUseTransMgmtList(SelectUserReqestsListReqVO param){
        PageInfo<SelectMgmtRegiListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(param.getStartDt() != null){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(param.getEndDt() != null){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctConserveMapper.selectUseTransMgmtList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }
}
