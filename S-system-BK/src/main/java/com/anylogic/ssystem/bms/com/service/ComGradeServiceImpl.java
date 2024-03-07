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


import com.anylogic.ssystem.bms.com.mapper.ComGradeMapper;
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
@Service("ComGradeService")
@Transactional
public class ComGradeServiceImpl implements ComGradeService {

    @Autowired
    private ComGradeMapper comGradeMapper;

    /* 조직관리 > 직위/직급관리 > 직급관리 목록 조회 */
    @Override
    public PageInfo<SelectGradeListResVO> selectGradeList(SelectGradeListReqVO param){
        PageInfo<SelectGradeListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(comGradeMapper.selectGradeList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 조직관리 > 직위/직급관리 > 직급 삭제 */
    @Override
    public List<Integer> deleteGrade(List<DeleteGradeReqVO> params){
        List<Integer> res = new ArrayList<Integer>();
        try {
            for(DeleteGradeReqVO param : params) {
                res.add(comGradeMapper.deleteGrade(param));
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* 조직관리 > 직위/직급관리 > 직급 상세 조회 */
    @Override
    public GetGradeDetailResVO getGradeDetail(GetGradeDetailReqVO param){
        GetGradeDetailResVO resGetGradeDetail;
        try {
            resGetGradeDetail = comGradeMapper.getGradeDetail(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetGradeDetail == null){
            resGetGradeDetail = new GetGradeDetailResVO();
        }
        return resGetGradeDetail;
    }


    /* 조직관리 > 직위/직급관리 > 직급 등록 */
    @Override
    public int insertGrade(InsertGradeReqVO param){
        int resInsertMenuBIA = 0;
        try {
            resInsertMenuBIA = comGradeMapper.insertGrade(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resInsertMenuBIA;
    }


    /* 조직관리 > 직위/직급관리 > 직급 수정 */
    @Override
    public int updateGrade(UpdateGradeReqVO param){
        int resUpdateMenuBIA = 0;
        try {
            resUpdateMenuBIA = comGradeMapper.updateGrade(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateMenuBIA;
    }

}