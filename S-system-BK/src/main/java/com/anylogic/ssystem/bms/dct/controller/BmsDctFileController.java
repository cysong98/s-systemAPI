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
import com.anylogic.ssystem.bms.dct.model.InsertFileReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectDctFileReqVO;
import com.anylogic.ssystem.bms.dct.service.BmsDctFileService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"dct"})
@RestController
@RequestMapping("/bms/dct/bmsdctfile")
public class BmsDctFileController {

    @Autowired
    private BmsDctFileService bmsDctFileService;


    /* 기안문, 붙임 파일 조회 */
    @GetMapping("/selectDctFile")
    public ResponseEntity selectDctFile
    (
        @Valid SelectDctFileReqVO param
        
    ) throws Exception 
    {
        log.debug("bmsDctFile selectDctFile" + param.toString());

        return ResponseEntity.ok(bmsDctFileService.selectDctFile(param));
    }


    /* 발송 실패 오프라인 인쇄시 파일 조회 */
    @GetMapping("/selectSendFileList")
    public ResponseEntity selectSendFileList
    (
        @Valid SelectDctFileReqVO param

    ) throws Exception
    {
        log.debug("bmsDctFile selectSendFileList" + param.toString());

        return ResponseEntity.ok(bmsDctFileService.selectSendFileList(param));
    }

    /* 파일 조회 */
    @GetMapping("/selectCommonDctFile")
    public ResponseEntity selectCommonDctFile
    (
            @Valid SelectDctFileReqVO param

    ) throws Exception
    {
        log.debug("bmsDctFile selectDctFile" + param.toString());

        return ResponseEntity.ok(bmsDctFileService.selectCommonDctFile(param));
    }

}
