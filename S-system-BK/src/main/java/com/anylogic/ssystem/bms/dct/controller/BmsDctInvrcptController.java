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

import com.anylogic.ssystem.bms.dct.model.InsertRecvConfirmBIAReqVO;
import com.anylogic.ssystem.bms.dct.model.InsertRecvInvrcptReqVO;
import com.anylogic.ssystem.bms.dct.model.InsertTempListReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.anylogic.ssystem.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.anylogic.ssystem.bms.dct.model.SelectReceiptReqVO;
import com.anylogic.ssystem.bms.dct.service.BmsDctInvrcptService;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"dct"})
@RestController
@RequestMapping("/bms/dct/bmsdctinvrcpt")
public class BmsDctInvrcptController {

    @Autowired
    private BmsDctInvrcptService bmsDctInvrcptService;


    /**
     * selectOne
     * @param //SelectReceiptReqVO
     * return ResponseEntity
     */
    @GetMapping("/selectReceipt")
    public ResponseEntity selectReceipt
    (
            @Valid SelectReceiptReqVO param

    ) throws Exception
    {
        log.debug("bmsDctInvrcpt selectReceipt" + param.toString());

        return ResponseEntity.ok(bmsDctInvrcptService.selectReceipt(param));
    }

    /**
     * insert
     * @param //SelectReceiptReqVO
     * return ResponseEntity
     */
    @PostMapping(value = "/insertReceipt")
    public ResponseEntity insertReceipt
    (
            @Valid @RequestBody SelectReceiptReqVO param
    )
    {
        log.debug("bmsDctInvrcpt insertReceipt" + param.toString());

        int res = 0;
        try {
            res = bmsDctInvrcptService.insertReceipt(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return ResponseEntity.ok(res);
    }

    /* 오프라인발송 문서에 대한 송증/영수증 서명입력, 접수대기처리 */
    @PostMapping(value = "/insertRecvInvrcpt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity insertRecvInvrcpt
    (

//            @Valid @RequestBody InsertRecvInvrcptReqVO param
            @RequestPart("reqInsertRecvInvrcpt") @Valid InsertRecvInvrcptReqVO param
            ,@RequestPart(value = "refAttach", required = false) List<MultipartFile> refAttach

    )
    {
        log.debug("bmsDctInvrcpt insertRecvInvrcpt" + param.toString());

        int res = 0;
        try {
            res = bmsDctInvrcptService.insertRecvInvrcpt(param, refAttach);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return ResponseEntity.ok(res);
    }


}
