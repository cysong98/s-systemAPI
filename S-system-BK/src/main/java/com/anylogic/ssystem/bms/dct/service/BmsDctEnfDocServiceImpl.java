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


import com.anylogic.ssystem.bms.dct.mapper.BmsDctEnfPathMapper;
import com.anylogic.ssystem.bms.dct.model.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctEnfDocMapper;

import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import java.util.Map;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RequiredArgsConstructor
@Service("BmsDctEnfDocService")
@Transactional
public class BmsDctEnfDocServiceImpl implements BmsDctEnfDocService {

    @Autowired
    private BmsDctEnfDocMapper bmsDctEnfDocMapper;
    @Autowired
    private BmsDctEnfPathMapper bmsDctEnfPathMapper;


    /* 발송 > 반송함 > 반송함 목록 조회 */
    @Override
    public PageInfo<SelectReturnListResVO> selectReturnList(SelectReturnListReqVO param){
        PageInfo<SelectReturnListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctEnfDocMapper.selectReturnList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 접수 > 배부함 > 배부함 목록 조회 */
    @Override
    public PageInfo<SelectDctDistributeListResVO> selectDctDistributeList(SelectDctDistributeListReqVO param){
        PageInfo<SelectDctDistributeListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            param.setDocstatus("EDS02");
            resultListVO = PageInfo.of(bmsDctEnfDocMapper.selectDctDistributeList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 접수 > 접수대기함 > 접수대기함 목록 조회 */
    @Override
    public PageInfo<SelectReceiptListResVO> selectReceiptList(SelectReceiptListReqVO param){
        PageInfo<SelectReceiptListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctEnfDocMapper.selectReceiptList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 접수한 문서 문서처리 시 상태값 수정 */
    @Override
    public int updateEnfDocRepath(UpdateEnfDocRepathReqVO param){
        int resUpdateEnfDocRepath = 0;
        try {
            resUpdateEnfDocRepath = bmsDctEnfDocMapper.updateEnfDocRepath(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateEnfDocRepath;
    }


    /* 접수 > 재지정함 > 재지정함 목록 조회 */
    @Override
    public PageInfo<SelectReprocessListResVO> selectReprocessList(SelectReprocessListReqVO param){
        PageInfo<SelectReprocessListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctEnfDocMapper.selectReprocessList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 비전자 접수 문서 상세 > 접수 정보 조회 */
    @Override
    public GetReprocessResVO getReprocess(GetReprocessReqVO param){
        GetReprocessResVO resGetReprocess;
        try {
            resGetReprocess = bmsDctEnfDocMapper.getReprocess(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetReprocess == null){
            resGetReprocess = new GetReprocessResVO();
        }
        return resGetReprocess;
    }


    /* 접수 > 접수현황함 > 접수현황함 목록 조회 */
    @Override
    public PageInfo<SelectReceiptListResVO> selectReceiptCurrentList(SelectReceiptListReqVO param){
        PageInfo<SelectReceiptListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctEnfDocMapper.selectReceiptCurrentList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 접수 > 접수대기함 > 담당정보 조회 */
    @Override
    public List<SelectDctEnfPathResVO> selectDctEnfPath(SelectDctEnfPathReqVO param){
        List<SelectDctEnfPathResVO> resultListVO = null;
        try {
//            if(!Objects.equals(param.getStartDt(), "")){
//                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
//                param.setStartDt(startDt);
//            }
//            if(!Objects.equals(param.getEndDt(), "")){
//                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
//                param.setEndDt(endDt);
//            }
            resultListVO = bmsDctEnfPathMapper.selectDctEnfPath(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

}
