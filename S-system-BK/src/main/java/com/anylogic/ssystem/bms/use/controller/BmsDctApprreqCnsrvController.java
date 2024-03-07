/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.controller;

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
import com.anylogic.ssystem.bms.use.model.InsertComingCnsrvApproveBIAReqVO;
import com.anylogic.ssystem.bms.use.service.BmsDctApprreqCnsrvService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"use"})
@RestController
@RequestMapping("/bms/use/bmsdctapprreqcnsrv")
public class BmsDctApprreqCnsrvController {

    @Autowired
    private BmsDctApprreqCnsrvService bmsDctApprreqCnsrvService;


    /* 예고문도래목록 > 존안요청 > 존안요청서 등록*/
    @PostMapping(value = "/insertComingCnsrvApproveBIA")
    public ResponseEntity insertComingCnsrvApproveBIA
    (
        @Valid @RequestBody InsertComingCnsrvApproveBIAReqVO param
    ) 
    {
        int res = 0;
        try {
            res = bmsDctApprreqCnsrvService.insertComingCnsrvApproveBIA(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


}

