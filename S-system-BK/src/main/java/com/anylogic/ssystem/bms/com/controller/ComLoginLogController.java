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

import com.anylogic.ssystem.bms.com.model.InsertMenuBIAReqVO;
import com.anylogic.ssystem.bms.com.model.InsertRealtimeLogReqVO;
import com.anylogic.ssystem.bms.com.model.InsertSystemLoginLogReqVO;
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
import com.anylogic.ssystem.bms.com.model.SelectSystemLoginLogReqVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.com.service.ComLoginLogService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/comloginlog")
public class ComLoginLogController {

    @Autowired
    private ComLoginLogService comLoginLogService;


    /* 시스템관리 > 시스템접속로그 > 시스템접속로그 목록 조회 */
    @GetMapping("/selectSystemLoginLog")
    public ResponseEntity selectSystemLoginLog (@Valid SelectSystemLoginLogReqVO param) throws Exception
    {
        return ResponseEntity.ok(comLoginLogService.selectSystemLoginLog(param));
    }


    /* 로그인시 시스템접속로그 등록 */
    @PostMapping(value = "/insertSystemLoginLog")
    public ResponseEntity insertSystemLoginLog (@Valid @RequestBody InsertSystemLoginLogReqVO param) {
        int res = 0;
        try {
            res = comLoginLogService.insertSystemLoginLog(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 시스템접속로그 삭제 */
    @DeleteMapping(value = "/deleteSystemLoginLog")
    public ResponseEntity deleteSystemLoginLog(@Valid @RequestBody InsertSystemLoginLogReqVO param)
    {
        int res = 0;
        try {
            res = comLoginLogService.deleteSystemLoginLog(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);

    }
}
