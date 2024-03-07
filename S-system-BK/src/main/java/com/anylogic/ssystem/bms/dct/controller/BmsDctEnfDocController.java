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

import com.anylogic.ssystem.bms.dct.model.*;
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
import com.anylogic.ssystem.bms.dct.service.BmsDctEnfDocService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"dct"})
@RestController
@RequestMapping("/bms/dct/bmsdctenfdoc")
public class BmsDctEnfDocController {

    @Autowired
    private BmsDctEnfDocService bmsDctEnfDocService;


    /* 발송 > 반송함 > 반송함 목록 조회 */
    @GetMapping("/selectReturnList")
    public ResponseEntity selectReturnList
    (
        @Valid SelectReturnListReqVO param
    ) throws Exception 
    {
        log.debug("bmsDctEnfDoc selectReturnList" + param.toString());

        return ResponseEntity.ok(bmsDctEnfDocService.selectReturnList(param));
    }


    /* 접수 > 배부함 > 배부함 목록 조회 */
    @GetMapping("/selectDctDistributeList")
    public ResponseEntity selectDctDistributeList
    (
        @Valid SelectDctDistributeListReqVO param
    ) throws Exception 
    {
        log.debug("bmsDctEnfDoc selectDctDistributeList" + param.toString());

        return ResponseEntity.ok(bmsDctEnfDocService.selectDctDistributeList(param));
    }


    /* 접수 > 접수대기함 > 접수대기함 목록 조회 */
    @GetMapping("/selectReceiptList")
    public ResponseEntity selectReceiptList
    (
        @Valid SelectReceiptListReqVO param
    ) throws Exception 
    {
        log.debug("bmsDctEnfDoc selectReceiptList" + param.toString());

        return ResponseEntity.ok(bmsDctEnfDocService.selectReceiptList(param));
    }

    /* 접수한 문서 문서처리 시 상태값 수정 */
    @PutMapping(value = "/updateEnfDocRepath")
    public ResponseEntity updateEnfDocRepath
    (
        @Valid @RequestBody UpdateEnfDocRepathReqVO param
    ) 
    {
        log.debug("bmsDctEnfDoc updateEnfDocRepath" + param.toString());

        int res = 0;
        try {
            res = bmsDctEnfDocService.updateEnfDocRepath(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 접수 > 재지정함 > 재지정함 목록 조회 */
    @GetMapping("/selectReprocessList")
    public ResponseEntity selectReprocessList
    (
        @Valid SelectReprocessListReqVO param
    ) throws Exception 
    {
        log.debug("bmsDctEnfDoc selectReprocessList" + param.toString());

        return ResponseEntity.ok(bmsDctEnfDocService.selectReprocessList(param));
    }


    /* 비전자 접수 문서 상세 > 접수 정보 조회 */
    @GetMapping("/getReprocess")
    public ResponseEntity getReprocess
    (
        @Valid GetReprocessReqVO param
        
    ) throws Exception 
    {
        log.debug("bmsDctEnfDoc getReprocess" + param.toString());

        return ResponseEntity.ok(bmsDctEnfDocService.getReprocess(param));
    }


    /* 접수 > 접수현황함 > 접수현황함 목록 조회 */
    @GetMapping("/selectReceiptCurrentList")
    public ResponseEntity selectReceiptCurrentList
    (
            @Valid SelectReceiptListReqVO param
    ) throws Exception
    {
        log.debug("bmsDctEnfDoc selectReceiptList" + param.toString());

        return ResponseEntity.ok(bmsDctEnfDocService.selectReceiptCurrentList(param));
    }

    /* 접수 > 접수대기함 > 담당정보 조회 */
    @GetMapping("/selectDctEnfPath")
    public ResponseEntity selectDctEnfPath
    (
            @Valid SelectDctEnfPathReqVO param
    ) throws Exception
    {
        log.debug("bmsDctEnfDoc selectDctEnfPath" + param.toString());

        return ResponseEntity.ok(bmsDctEnfDocService.selectDctEnfPath(param));
    }


}
