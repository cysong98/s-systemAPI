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

import com.anylogic.ssystem.bms.trn.service.BmsTrnObjectService;
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
import com.anylogic.ssystem.bms.trn.model.InsertTrnStepReqVO;
import com.anylogic.ssystem.bms.trn.model.UpdateTrnStepReqVO;
import com.anylogic.ssystem.bms.trn.service.BmsTrnStepService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"trn"})
@RestController
@RequestMapping("/bms/trn/bmstrnstep")
public class BmsTrnStepController {

    @Autowired
    private BmsTrnStepService bmsTrnStepService;
    @Autowired
    private BmsTrnObjectService bmsTrnObjectService;

    /* 인계인수서 등록(승인요청) */
    @PostMapping(value = "/insertTrnStep")
    public ResponseEntity insertTrnStep
    (
        @Valid @RequestBody InsertTrnStepReqVO param
    ) 
    {
        int res = 0;
        try {
            res = bmsTrnObjectService.selectTrnObjectList(param);
            if(res != -1)
                res = bmsTrnStepService.insertTrnStep(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }
}
