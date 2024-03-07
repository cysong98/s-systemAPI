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
import com.anylogic.ssystem.bms.dct.model.InsertRecvReqVO;
import com.anylogic.ssystem.bms.dct.model.GetDctRecvReqVO;
import com.anylogic.ssystem.bms.dct.service.BmsDctApprRecvService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"dct"})
@RestController
@RequestMapping("/bms/dct/bmsdctapprrecv")
public class BmsDctApprRecvController {

    @Autowired
    private BmsDctApprRecvService bmsDctApprRecvService;

    /* 수신처 목록 조회 */
    @GetMapping("/getDctRecv")
    public ResponseEntity getDctRecv
    (
        @Valid GetDctRecvReqVO param
    ) throws Exception 
    {
        log.debug("bmsDctApprRecv getDctRecv" + param.toString());

        return ResponseEntity.ok(bmsDctApprRecvService.getDctRecv(param));
    }

    /* 원본 문서의 수신처 목록 조회 */
    @GetMapping("/getDctOriRecv")
    public ResponseEntity getDctOriRecv
    (
        @Valid GetDctRecvReqVO param
    ) throws Exception
    {
        log.debug("bmsDctApprRecv getDctOriRecv" + param.toString());

        return ResponseEntity.ok(bmsDctApprRecvService.getDctOriRecv(param));
    }


}
