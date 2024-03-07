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
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;
import com.anylogic.ssystem.bms.com.mapper.ComUserinfoMapper;
import com.anylogic.ssystem.bms.login.model.ResOauthDto;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.ComRealtimeLogMapper;

import java.math.BigDecimal;
import java.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;



@Slf4j
@RequiredArgsConstructor
@Service("ComRealtimeLogService")
@Transactional
public class ComRealtimeLogServiceImpl implements ComRealtimeLogService {

    @Autowired
    private ComRealtimeLogMapper comRealtimeLogMapper;

    @Autowired
    private ComUserinfoMapper comUserinfoMapper;


    /* 시스템관리 > 실시간접속현황 > 실시간접속현황 목록 조회 */
    @Override
    public PageInfo<SelectRealtimeLogResVO> selectRealtimeLog(SelectRealtimeLogReqVO param) {
        PageInfo<SelectRealtimeLogResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(comRealtimeLogMapper.selectRealtimeLog(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 실시간접속현황 등록 */
    @Override
    public int insertRealtimeLog(InsertRealtimeLogReqVO param) {
        int res = 0;
        try {
            GetUserDetailResVO vo = comUserinfoMapper.getUserDetail(GetUserDetailReqVO.builder()
                                                                        .userid(param.getUserid())
                                                                        .build())
                                                                        .get(0);
            param.setDeptid(vo.getDeptid());
            param.setDeptname(vo.getDeptname());
            param.setUsername(vo.getUsername());
            param.setFlag("Y");

            List<SelectRealtimeLogResVO> list = comRealtimeLogMapper.selectRealtimeLog(SelectRealtimeLogReqVO.builder()
                                                                                                .rtid(param.getRtid())
                                                                                                .build());
            if(!list.isEmpty())
                param.setRtseq(list.get(0).getRtseq() + 1.0f);
            else
                param.setRtseq(0);
            res = comRealtimeLogMapper.insertRealtimeLog(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* 실시간접속현황 수정 */
    @Override
    public int updateRealtimeLog(InsertRealtimeLogReqVO param) {
        int res = 0;
        try {
            List<SelectRealtimeLogResVO> list = new ArrayList<>();
            if(param.getUserid() != null) {
                list = comRealtimeLogMapper.selectRealtimeLog(SelectRealtimeLogReqVO.builder()
                                                                    .userid(param.getUserid())
                                                                    .token(param.getToken())
                                                                    .build());
            }

            if(!list.isEmpty()) {
                if ((param.getFlag().equals("N") && list.get(0).getFlag().equals("Y")) ||
                        (param.getFlag().equals("Y") && list.get(0).getFlag().equals("N"))) {}
                else {
                    param.setFlag("X");
                }
            }

            res = comRealtimeLogMapper.updateRealtimeLog(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }
}
