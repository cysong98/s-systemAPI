/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.controller;

import java.util.*;

import com.anylogic.ssystem.bms.com.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.anylogic.ssystem.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.com.service.ComRealtimeLogService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/comrealtimelog")
public class ComRealtimeLogController {

    @Autowired
    private ComRealtimeLogService comRealtimeLogService;


    /* 시스템관리 > 실시간접속현황 > 실시간접속현황 목록 조회 */
    @GetMapping("/selectRealtimeLog")
    public ResponseEntity selectRealtimeLog
    (
        @Valid SelectRealtimeLogReqVO param
    ) throws Exception 
    {
        log.debug("comRealtimeLog selectRealtimeLog" + param.toString());

        return ResponseEntity.ok(comRealtimeLogService.selectRealtimeLog(param));
    }


    /* 실시간접속현황 등록 */
    @PostMapping(value = "/insertRealtimeLog")
    public ResponseEntity insertRealtimeLog (@Valid @RequestBody InsertRealtimeLogReqVO param) {
        SelectRealtimeLogResVO resVO = null;
        try {
            List<SelectRealtimeLogResVO> list = comRealtimeLogService.selectRealtimeLog(SelectRealtimeLogReqVO.builder()
                                                                                            .userid(param.getUserid())
                                                                                            .token(param.getToken())
                                                                                            .pageNum(1)
                                                                                            .pageSize(10)
                                                                                            .build())
                                                                                            .getList();
            if(!list.isEmpty())
                resVO = list.get(0);

            if(resVO == null) {
                List<SelectRealtimeLogResVO> templist = comRealtimeLogService.selectRealtimeLog(SelectRealtimeLogReqVO.builder()
                                                                                                        .userid(param.getUserid())
                                                                                                        .flag("Y")
                                                                                                        .pageNum(1)
                                                                                                        .pageSize(10)
                                                                                                        .build())
                                                                                                        .getList();
                if(!templist.isEmpty())
                    resVO = templist.get(0);

                 if(resVO != null)
                    comRealtimeLogService.updateRealtimeLog(InsertRealtimeLogReqVO.builder()
                                                                    .rtid(resVO.getRtid())
                                                                    .rtseq(resVO.getRtseq())
                                                                    .flag("N")
                                                                    .build());
                comRealtimeLogService.insertRealtimeLog(param);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(resVO == null)
            resVO = new SelectRealtimeLogResVO();
        return ResponseEntity.ok(resVO);
    }


    /* 실시간접속현황 수정 */
    @PutMapping(value = "/updateRealtimeLog")
    public ResponseEntity updateRealtimeLog (@Valid @RequestBody InsertRealtimeLogReqVO param)
    {
        int res = 0;
        try {
            res = comRealtimeLogService.updateRealtimeLog(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }
}
