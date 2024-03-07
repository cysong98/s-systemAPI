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
import com.anylogic.ssystem.bms.com.service.BmsCodeService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/bmscode")
public class BmsCodeController {

    @Autowired
    private BmsCodeService bmsCodeService;


    /* 시스템관리 > 코드관리 > 코드 목록 조회 */
    @GetMapping("/selectCodeList")
    public ResponseEntity selectCodeList
    (
        @Valid SelectCodeListReqVO param
    ) throws Exception 
    {
        log.debug("bmsCode selectCodeList" + param.toString());

        return ResponseEntity.ok(bmsCodeService.selectCodeList(param));
    }


    /* 수신지정팝업 > 외부 > 수신자기호 조회 */
    @GetMapping("/selectReceiveMarkCodeList")
    public ResponseEntity selectReceiveMarkCodeList
    (
        @Valid SelectReceiveMarkCodeListReqVO param
    ) throws Exception
    {
        log.debug("bmsCode selectReceiveMarkCodeList" + param.toString());

        return ResponseEntity.ok(bmsCodeService.selectReceiveMarkCodeList(param));
    }


    /* 비전자비밀접수 > 보관함/단번호 조회 */
    @GetMapping("/selectOfflineCabinetInfo")
    public ResponseEntity selectOfflineCabinetInfo
    (
            @Valid SelectReceiveMarkCodeListReqVO param
    ) throws Exception
    {
        log.debug("bmsCode selectOfflineCabinetInfo" + param.toString());

        return ResponseEntity.ok(bmsCodeService.selectOfflineCabinetInfo(param));
    }


    /* 시스템관리 > 코드관리 > 코드 삭제 */
    @DeleteMapping(value = "/deleteCode")
    public ResponseEntity deleteCode
    (
            @Valid @RequestBody List<DeleteCodeReqVO> params
    ) 
    {
        log.debug("bmsCode deleteCode" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = bmsCodeService.deleteCode(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 시스템관리 > 코드관리 > 코드 등록 */
    @PostMapping(value = "/insertCode")
    public ResponseEntity insertCode
    (
        @Valid @RequestBody InsertCodeReqVO param
    ) 
    {
        log.debug("bmsCode insertCode" + param.toString());
        int res = 0;
        try {
            res = bmsCodeService.insertCode(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 시스템관리 > 코드관리 > 코드 수정 팝업 > 상세조회 */
    @GetMapping("/getCodeDetail")
    public ResponseEntity getCodeDetail
    (
        @Valid GetCodeDetailReqVO param
        
    ) throws Exception 
    {
        log.debug("bmsCode getCodeDetail" + param.toString());

        return ResponseEntity.ok(bmsCodeService.getCodeDetail(param));
    }


    /* 시스템관리 > 코드관리 > 코드 수정 팝업 > 수정 */
    @PutMapping(value = "/updateCode")
    public ResponseEntity updateCode
    (
        @Valid @RequestBody UpdateCodeReqVO param
    ) 
    {
        log.debug("bmsCode updateCode" + param.toString());
        int res = 0;
        try {
            res = bmsCodeService.updateCode(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


}
