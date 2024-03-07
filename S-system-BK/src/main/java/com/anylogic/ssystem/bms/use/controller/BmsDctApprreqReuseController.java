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
import com.anylogic.ssystem.bms.use.model.SelectUseReqRecycleListReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqRecycleApproveReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqRecycleRejectReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseDraftRecycleListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseProcRecycleListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseTempRecycleListReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseTempRecycleApproveReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseCompleteRecycleListReqVO;
import com.anylogic.ssystem.bms.use.model.InsertReUseReqReqVO;
import com.anylogic.ssystem.bms.use.service.BmsDctApprreqReuseService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"use"})
@RestController
@RequestMapping("/bms/use/bmsdctapprreqreuse")
public class BmsDctApprreqReuseController {

    @Autowired
    private BmsDctApprreqReuseService bmsDctApprreqReuseService;


    /* 비밀문서사용요청 > 재사용요청 > 재사용요청서 등록 */
    @PostMapping(value = "/insertReUseReq")
    public ResponseEntity insertReUseReq
    (
        @Valid @RequestBody InsertReUseReqReqVO param
    ) 
    {
        int res = 0;
        try {
            res = bmsDctApprreqReuseService.insertReUseReq(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


}
