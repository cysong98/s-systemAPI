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
import com.anylogic.ssystem.bms.use.model.SelectUseReqViewListReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqViewApproveReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqViewRejectReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseDraftViewListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseProcViewListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseTempViewListReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseTempViewApproveReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseCompleteViewListReqVO;
import com.anylogic.ssystem.bms.use.model.InsertUseDigitalViewReqReqVO;
import com.anylogic.ssystem.bms.use.model.InsertUseNonDigitalViewReqReqVO;
import com.anylogic.ssystem.bms.use.service.BmsDctApprreqReadService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"use"})
@RestController
@RequestMapping("/bms/use/bmsdctapprreqread")
public class BmsDctApprreqReadController {

    @Autowired
    private BmsDctApprreqReadService bmsDctApprreqReadService;


    /* 비밀문서사용요청 > 열람요청 > 전자 열람요청서 등록 */
    @PostMapping(value = "/insertUseDigitalViewReq")
    public ResponseEntity insertUseDigitalViewReq
    (
        @Valid @RequestBody InsertUseDigitalViewReqReqVO param
    ) 
    {
        int res = 0;
        try {
            res = bmsDctApprreqReadService.insertUseDigitalViewReq(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 비밀문서사용요청 > 열람요청 > 비전자 열람요청서 등록 */
    @PostMapping(value = "/insertUseNonDigitalViewReq")
    public ResponseEntity insertUseNonDigitalViewReq
    (
        @Valid @RequestBody InsertUseDigitalViewReqReqVO param
    ) 
    {
        int res = 0;
        try {
            res = bmsDctApprreqReadService.insertUseNonDigitalViewReq(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


}
