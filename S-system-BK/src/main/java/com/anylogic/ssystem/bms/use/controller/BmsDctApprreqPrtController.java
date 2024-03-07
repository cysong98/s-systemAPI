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
import com.anylogic.ssystem.bms.use.model.SelectUseReqPrintListReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqPrintApproveReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseReqPrintRejectReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseDraftPrintListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseProcPrintListReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseTempPrintListReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseTempPrintApproveReqVO;
import com.anylogic.ssystem.bms.use.model.SelectUseCompletePrintListReqVO;
import com.anylogic.ssystem.bms.use.model.UpdateUseCompletePrintFinishBIAReqVO;
import com.anylogic.ssystem.bms.use.model.InsertUseDigitalPrintReqVO;
import com.anylogic.ssystem.bms.use.model.InsertUseNonDigitalPrintReqVO;
import com.anylogic.ssystem.bms.use.service.BmsDctApprreqPrtService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"use"})
@RestController
@RequestMapping("/bms/use/bmsdctapprreqprt")
public class BmsDctApprreqPrtController {

    @Autowired
    private BmsDctApprreqPrtService bmsDctApprreqPrtService;


    /* 비밀문서사용요청 > 인쇄요청 > 전자 인쇄요청서 등록 */
    @PostMapping(value = "/insertUseDigitalPrint")
    public ResponseEntity insertUseDigitalPrint
    (
        @Valid @RequestBody InsertUseDigitalPrintReqVO param
    )
    {
        int res = 0;
        try {
            res = bmsDctApprreqPrtService.insertUseDigitalPrint(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* (반영X) 비밀문서사용요청 > 인쇄요청 > 비전자 인쇄요청서 등록 */
    @PostMapping(value = "/insertUseNonDigitalPrint")
    public ResponseEntity insertUseNonDigitalPrint
    (
        @Valid @RequestBody InsertUseDigitalPrintReqVO param
    )
    {
        int res = 0;
        try {
            res = bmsDctApprreqPrtService.insertUseNonDigitalPrint(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


}
