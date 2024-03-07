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


import com.anylogic.ssystem.bms.com.model.SelectUserVgroupReqVO;
import com.anylogic.ssystem.bms.com.model.SelectUserVgroupResVO;
import com.anylogic.ssystem.bms.dct.model.*;
import com.anylogic.ssystem.bms.trn.model.SelectMgmtRegiListCountReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectMgmtRegiListCountResVO;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctMgmtRegiMapper;

import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import java.util.Map;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Slf4j
@RequiredArgsConstructor
@Service("BmsDctMgmtRegiService")
@Transactional
public class BmsDctMgmtRegiServiceImpl implements BmsDctMgmtRegiService {

    @Autowired
    private BmsDctMgmtRegiMapper bmsDctMgmtRegiMapper;

    /* 비밀관리기록부 목록 조회 */
    @Override
    public PageInfo<SelectMgmtRegiListResVO> selectMgmtRegiList(SelectMgmtRegiListReqVO param){
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
            resultListVO = PageInfo.of(bmsDctMgmtRegiMapper.selectMgmtRegiList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 인계인수 건수별 팝업 조회 (미완료/생산/접수/일반) */
    @Override
    public SelectMgmtRegiListResVO selectMgmtRegiCnt(SelectMgmtRegiCntReqVO param){
        SelectMgmtRegiListResVO resSelectMgmtRegiCount = new SelectMgmtRegiListResVO();
        try {
            param.setCountName("totalCount");
            resSelectMgmtRegiCount.setTotalCount(bmsDctMgmtRegiMapper.selectMgmtRegiCnt(param));
            param.setCountName("create5LevelCount");
            resSelectMgmtRegiCount.setCreate5LevelCount(bmsDctMgmtRegiMapper.selectMgmtRegiCnt(param));
            param.setCountName("createOtherCount");
            resSelectMgmtRegiCount.setCreateOtherCount(bmsDctMgmtRegiMapper.selectMgmtRegiCnt(param));
            param.setCountName("receiptCount");
            resSelectMgmtRegiCount.setReceiptCount(bmsDctMgmtRegiMapper.selectMgmtRegiCnt(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resSelectMgmtRegiCount;
    }


    /* 타부서 목록 조회 (본인 부서 제외) */
    @Override
    public List<SelectOtherDeptResVO> selectOtherDept(SelectOtherDeptReqVO param){
        List<SelectOtherDeptResVO> resultListVO = new ArrayList<SelectOtherDeptResVO>();
        try {
            PageHelper.clearPage();
            resultListVO = bmsDctMgmtRegiMapper.selectOtherDept(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 비밀관리기록부 팝업 조회 (재분류/파기/이관/존안 정보) */
    @Override
    public GetMgmtRegiResVO getMgmtRegi(GetMgmtRegiReqVO param){
        GetMgmtRegiResVO resGetMgmtRegi;
        try {
            resGetMgmtRegi = bmsDctMgmtRegiMapper.getMgmtRegi(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetMgmtRegi == null){
            resGetMgmtRegi = new GetMgmtRegiResVO();
        }
        return resGetMgmtRegi;
    }


    /* 인계인수 대상 목록 조회 */
    @Override
    public List<GetMgmtRegiResVO> selectMgmtRegiNonPageList(GetMgmtRegiReqVO param){
        List<GetMgmtRegiResVO> resultListVO = null;
        try {
            PageHelper.clearPage();
            resultListVO = bmsDctMgmtRegiMapper.selectMgmtRegiNonPageList(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* DOCID로 MGMTID 조회 */
    @Override
    public GetMgmtidByDocidResVO getMgmtidByDocid(GetMgmtidByDocidReqVO param){
        GetMgmtidByDocidResVO getMgmtidByDocidResVO;
        try {
            getMgmtidByDocidResVO = bmsDctMgmtRegiMapper.getMgmtidByDocid(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(getMgmtidByDocidResVO == null){
            getMgmtidByDocidResVO = new GetMgmtidByDocidResVO();
        }
        return getMgmtidByDocidResVO;
    }

}
