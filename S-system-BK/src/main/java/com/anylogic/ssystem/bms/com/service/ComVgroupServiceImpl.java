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


import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.ComVgroupMapper;
import com.anylogic.ssystem.bms.com.model.SelectUserVgroupReqVO;
import com.anylogic.ssystem.bms.com.model.SelectUserVgroupResVO;
import java.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;



@Slf4j
@RequiredArgsConstructor
@Service("ComVgroupService")
@Transactional
public class ComVgroupServiceImpl implements ComVgroupService {


    @Autowired
    private ComVgroupMapper comVgroupMapper;

    /* 조직관리 > 부서/사용자관리 > 권한 검색 팝업 > 권한 목록 조회 */
    @Override
    public PageInfo<SelectUserVgroupResVO> selectUserVgroup(SelectUserVgroupReqVO param){
        PageInfo<SelectUserVgroupResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(comVgroupMapper.selectUserVgroup(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


}
