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


import com.anylogic.ssystem.bms.use.model.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.use.mapper.BmsDctMgmtRegiMapper;

import java.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Slf4j
@RequiredArgsConstructor
@Service("BmsDctMgmtRegiService")
@Transactional
public class BmsDctMgmtRegiServiceImpl implements BmsDctMgmtRegiService {

    @Autowired
    private BmsDctMgmtRegiMapper bmsDctMgmtRegiMapper;

    /* 예고문 도래 알림 목록 조회 */
    @Override
    public List<SelectAlertResVO> selectAlert(SelectAlertReqVO param) {
        List<SelectAlertResVO> resultListVO = new ArrayList<SelectAlertResVO>();
        try {
            PageHelper.clearPage();
            resultListVO = bmsDctMgmtRegiMapper.selectAlert(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 비밀문서사용요청 > 열람요청 > 열람요청 목록 조회 */
    @Override
    public PageInfo<SelectUseDigitalViewResVO> selectUseDigitalView(SelectUserReqestsListReqVO param){
        PageInfo<SelectUseDigitalViewResVO> resultListVO = null;
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
            resultListVO = PageInfo.of(bmsDctMgmtRegiMapper.selectUseDigitalView(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 비밀문서사용요청 > 재사용요청 > 재사용요청 목록 조회 */
    @Override
    public PageInfo<SelectReUseResVO> selectReUse(SelectUserReqestsListReqVO param){
        PageInfo<SelectReUseResVO> resultListVO = null;
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
            resultListVO = PageInfo.of(bmsDctMgmtRegiMapper.selectReUse(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 비밀문서사용요청 > 인쇄요청 > 인쇄요청 목록 조회 */
    @Override
    public PageInfo<SelectUsePrintResVO> selectUsePrint(SelectUserReqestsListReqVO param){
        PageInfo<SelectUsePrintResVO> resultListVO = null;
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
            resultListVO = PageInfo.of(bmsDctMgmtRegiMapper.selectUsePrint(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 비밀문서사용요청 > 반출요청 > 반출요청 목록 조회 */
    @Override
    public PageInfo<SelectUseTakeOutResVO> selectUseTakeOut(SelectUserReqestsListReqVO param){
        PageInfo<SelectUseTakeOutResVO> resultListVO = null;
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
            resultListVO = PageInfo.of(bmsDctMgmtRegiMapper.selectUseTakeOut(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 예고문도래목록 목록 조회 */
    @Override
    public PageInfo<SelectComingListResVO> selectComingList(SelectComingListReqVO param){
        PageInfo<SelectComingListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(bmsDctMgmtRegiMapper.selectComingList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 각 요청서의 요청내용 > 요청된 문서들 정보 조회 */
    @Override
    public List<GetDctMgmtRegiResVO> getDctMgmtRegi(GetDctMgmtRegiReqVO param){
        List<GetDctMgmtRegiResVO> resGetDctMgmtRegi;
        try {
            resGetDctMgmtRegi = bmsDctMgmtRegiMapper.getDctMgmtRegi(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resGetDctMgmtRegi;
    }

    /* 각 요청서의 요청내용 > 요청된 문서들 정보 조회 - 예고문정보 포함 */
    @Override
    public List<GetDctMgmtRegiAttrResVO> getDctMgmtRegiAttr(GetDctMgmtRegiReqVO param){
        List<GetDctMgmtRegiAttrResVO> resGetDctMgmtRegiAttr;
        try {
            resGetDctMgmtRegiAttr = bmsDctMgmtRegiMapper.getDctMgmtRegiAttr(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resGetDctMgmtRegiAttr;
    }

    /* 각 요청서의 요청내용 > 요청된 문서들 정보 조회 - 첨부파일 포함 */
    @Override
    public List<GetDctMgmtRegiResVO> getDctMgmtRegiPrint(GetDctMgmtRegiReqVO param){
        List<GetDctMgmtRegiResVO> resGetDctMgmtRegi;
        try {
            resGetDctMgmtRegi = bmsDctMgmtRegiMapper.getDctMgmtRegiPrint(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resGetDctMgmtRegi;
    }

}
