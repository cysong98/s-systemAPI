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


import com.anylogic.ssystem.bms.com.mapper.ComJobgubunMapper;
import com.anylogic.ssystem.bms.com.model.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;

import java.math.BigDecimal;
import java.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Slf4j
@RequiredArgsConstructor
@Service("ComJobgubunService")
@Transactional
public class ComJobgubunServiceImpl implements ComJobgubunService {

    @Autowired
    private ComJobgubunMapper comJobgubunMapper;

    /* 조직관리 > 직위/직급관리 > 직위관리 목록 조회 */
    @Override
    public PageInfo<SelectJobgubunListResVO> selectJobgubunList(SelectJobgubunListReqVO param){
        PageInfo<SelectJobgubunListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(comJobgubunMapper.selectJobgubunList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 조직관리 > 직위/직급관리 > 직위 삭제 */
    @Override
    public List<Integer> deleteJobgubun(List<DeleteJobgubunReqVO> params){
        List<Integer> res = new ArrayList<Integer>();
        try {
            for(DeleteJobgubunReqVO param : params) {
                res.add(comJobgubunMapper.deleteJobgubun(param));
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* 조직관리 > 직위/직급관리 > 직위 상세 조회 */
    @Override
    public GetJobgubunDetailResVO getJobgubunDetail(GetJobgubunDetailReqVO param){
        GetJobgubunDetailResVO resGetJobgubunDetail;
        try {
            resGetJobgubunDetail = comJobgubunMapper.getJobgubunDetail(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetJobgubunDetail == null){
            resGetJobgubunDetail = new GetJobgubunDetailResVO();
        }
        return resGetJobgubunDetail;
    }


    /* 조직관리 > 직위/직급관리 > 직위 등록 */
    @Override
    public int insertJobgubun(InsertJobgubunReqVO param){
        int resInsertMenuBIA = 0;
        try {
            resInsertMenuBIA = comJobgubunMapper.insertJobgubun(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resInsertMenuBIA;
    }


    /* 조직관리 > 직위/직급관리 > 직위 수정 */
    @Override
    public int updateJobgubun(UpdateJobgubunReqVO param){
        int resUpdateMenuBIA = 0;
        try {
            resUpdateMenuBIA = comJobgubunMapper.updateJobgubun(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateMenuBIA;
    }

}