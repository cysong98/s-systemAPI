/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.controller;

import java.util.*;

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
import com.anylogic.ssystem.bms.dct.model.SelectTaskInfoTempReqVO;
import com.anylogic.ssystem.bms.dct.service.BmsTaskInfomationTempService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"dct"})
@RestController
@RequestMapping("/bms/dct/bmstaskinfomationtemp")
public class BmsTaskInfomationTempController {

    @Autowired
    private BmsTaskInfomationTempService bmsTaskInfomationTempService;


    /* 임시저장된 관련근거 조회 */
    @GetMapping("/selectTaskInfoTemp")
    public ResponseEntity selectTaskInfoTemp
    (
        @Valid SelectTaskInfoTempReqVO param
    ) throws Exception 
    {
        log.debug("bmsTaskInfomationTemp selectTaskInfoTemp" + param.toString());

        return ResponseEntity.ok(bmsTaskInfomationTempService.selectTaskInfoTemp(param));
    }


    /* 기안완료된 관련근거 조회 */
    @GetMapping("/selectTaskInfo")
    public ResponseEntity selectTaskInfo
    (
        @Valid SelectTaskInfoTempReqVO param
    ) throws Exception
    {
        log.debug("bmsTaskInfomationTemp selectTaskInfo" + param.toString());

        return ResponseEntity.ok(bmsTaskInfomationTempService.selectTaskInfo(param));
    }

}
