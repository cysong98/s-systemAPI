/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.trn.controller;

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
import com.anylogic.ssystem.bms.trn.model.SelectTransProcListCountReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectTransRecvCountReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectComTransProcListReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectTransProcToDoListReqVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.trn.model.UpdateTrnRepotBIA1ReqVO;
import com.anylogic.ssystem.bms.trn.model.UpdateTrnRepotBIA2ReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectTransProcDeptToDoListReqVO;
import com.anylogic.ssystem.bms.trn.model.SelectTrnReqListReqVO;
import com.anylogic.ssystem.bms.trn.model.BmsTrnCompleteListReqVO;
import com.anylogic.ssystem.bms.trn.model.BmsTrnCurrentListReqVO;
import com.anylogic.ssystem.bms.trn.model.UpdateTrnRepotBIAReqVO;
import com.anylogic.ssystem.bms.trn.service.BmsTrnReportService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"trn"})
@RestController
@RequestMapping("/bms/trn/bmstrnreport")
public class BmsTrnReportController {

    @Autowired
    private BmsTrnReportService bmsTrnReportService;


    /* 인계인수 > 처리할 인계인수서 > 처리할 인계인수서 목록 조회 */
    @GetMapping("/selectTransProcToDoList")
    public ResponseEntity selectTransProcToDoList
    (
        @Valid SelectTransProcToDoListReqVO param
    ) throws Exception 
    {
        log.debug("bmsTrnReport selectTransProcToDoList" + param.toString());

        return ResponseEntity.ok(bmsTrnReportService.selectTransProcToDoList(param));
    }


    /* 처리할 인계인수서 승인 처리 */
    @PutMapping(value = "/updateTrnRepotBIA1")
    public ResponseEntity updateTrnRepotBIA1
    (
        @Valid @RequestBody UpdateTrnRepotBIA1ReqVO param
    ) 
    {
        log.debug("bmsTrnReport updateTrnRepotBIA1" + param.toString());

        int res = 0;
        try {
            res = bmsTrnReportService.updateTrnRepotBIA1(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 처리할 인계인수서 반려 처리 */
    @PutMapping(value = "/updateTrnRepotBIA2")
    public ResponseEntity updateTrnRepotBIA2
    (
        @Valid @RequestBody UpdateTrnRepotBIA2ReqVO param
    ) 
    {
        log.debug("bmsTrnReport updateTrnRepotBIA2" + param.toString());

        int res = 0;
        try {
            res = bmsTrnReportService.updateTrnRepotBIA2(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 인계인수 > 요청한 인계인수서 > 요청한 인계인수서 목록 조회 */
    @GetMapping("/selectTrnReqList")
    public ResponseEntity selectTrnReqList
    (
        @Valid SelectTrnReqListReqVO param
    ) throws Exception 
    {
        log.debug("bmsTrnReport selectTrnReqList" + param.toString());

        return ResponseEntity.ok(bmsTrnReportService.selectTrnReqList(param));
    }


    /* 인계인수 > 처리한 인계인수서 > 처리한 인계인수서 목록 조회 */
    @GetMapping("/bmsTrnCompleteList")
    public ResponseEntity bmsTrnCompleteList
    (
        @Valid BmsTrnCompleteListReqVO param
    ) throws Exception 
    {
        log.debug("bmsTrnReport bmsTrnCompleteList" + param.toString());

        return ResponseEntity.ok(bmsTrnReportService.bmsTrnCompleteList(param));
    }


    /* 인계인수 > 인수현황함 > 인수현황함 목록 조회 */
    @GetMapping("/bmsTrnCurrentList")
    public ResponseEntity bmsTrnCurrentList
    (
        @Valid BmsTrnCurrentListReqVO param
    ) throws Exception 
    {
        log.debug("bmsTrnReport bmsTrnCurrentList" + param.toString());

        return ResponseEntity.ok(bmsTrnReportService.bmsTrnCurrentList(param));
    }


    /* 인계자 & 인수자 정보 조회 */
    @GetMapping("/selectTrnOverList")
    public ResponseEntity selectTrnOverList
    (
        @Valid SelectTrnReqListReqVO param
    ) throws Exception
    {
        log.debug("bmsTrnReport selectTrnOverList" + param.toString());

        return ResponseEntity.ok(bmsTrnReportService.selectTrnOverList(param));
    }


    /* 인계인수 대상 목록 조회 */
    @GetMapping("/selectTrnObjectList")
    public ResponseEntity selectTrnObjectList
    (
            @Valid SelectTrnReqListReqVO param
    ) throws Exception
    {
        log.debug("bmsTrnReport selectTrnObjectList" + param.toString());

        return ResponseEntity.ok(bmsTrnReportService.selectTrnObjectList(param));
    }


}
