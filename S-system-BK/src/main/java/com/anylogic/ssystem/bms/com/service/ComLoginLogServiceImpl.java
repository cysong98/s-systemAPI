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


import com.anylogic.ssystem.bms.com.mapper.ComRealtimeLogMapper;
import com.anylogic.ssystem.bms.com.model.*;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.ComLoginLogMapper;

import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import java.util.Map;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;



@Slf4j
@RequiredArgsConstructor
@Service("ComLoginLogService")
@Transactional
public class ComLoginLogServiceImpl implements ComLoginLogService {

    @Autowired
    private ComLoginLogMapper comLoginLogMapper;
    @Autowired
    private ComRealtimeLogMapper comRealtimeLogMapper;
    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;


    /* 시스템관리 > 시스템접속로그 > 시스템접속로그 목록 조회 */
    @Override
    public PageInfo<SelectSystemLoginLogResVO> selectSystemLoginLog(SelectSystemLoginLogReqVO param){
        PageInfo<SelectSystemLoginLogResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(comLoginLogMapper.selectSystemLoginLog(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 로그인시 시스템접속로그 등록 */
    @Override
    public int insertSystemLoginLog(InsertSystemLoginLogReqVO param){
        int res = 0;
        try {
            param.setOpid("OP" + bmsDctRdocMapper.getSysGuid());
            res = comLoginLogMapper.insertSystemLoginLog(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* 시스템접속로그 삭제 */
    @Override
    public int deleteSystemLoginLog(InsertSystemLoginLogReqVO param) {
        int res = 0;
        try {
            PageHelper.clearPage();
            List<SelectSystemLoginLogResVO> list = comLoginLogMapper.selectSystemLoginLog(SelectSystemLoginLogReqVO.builder()
                                                                                                    .userid(param.getUserid())
                                                                                                    .limitSize(1)
                                                                                                    .build());
            // 페이지가 새로고침된 경우, 잘못 insert된 "로그아웃" 로그를 삭제한다. - SystemLoginLog
            if(!list.isEmpty() && list.get(0).getGubun().equals("로그아웃")) {
                res = comLoginLogMapper.deleteSystemLoginLog(DeleteSystemLoginLogReqVO.builder()
                                                                    .opid(list.get(0).getOpid())
                                                                    .build());
                // flag 값이 잘못 "N"로 변경된 로그를 수정한다. - RealtimeLog
                // flag 값이 "X"로 표기되어 있는 경우, 세션차단된 경우를 말한다.
                List<SelectRealtimeLogResVO> rtlist = comRealtimeLogMapper.selectRealtimeLog(SelectRealtimeLogReqVO.builder()
                                                                                                    .userid(param.getUserid())
                                                                                                    .token(param.getToken())
                                                                                                    .build());
                String customFlag = "Y";
                if(rtlist.get(0).getFlag().equals("X")) {
                    customFlag = "N";
                }
                comRealtimeLogMapper.updateRealtimeLog(InsertRealtimeLogReqVO.builder()
                                                            .userid(param.getUserid())
                                                            .token(param.getToken())
                                                            .flag(customFlag)
                                                            .build());
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }
}
