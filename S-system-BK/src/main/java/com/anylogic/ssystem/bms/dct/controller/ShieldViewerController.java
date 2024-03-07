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

import com.anylogic.ssystem.bms.com.model.InsertGwanBIAReqVO;
import com.anylogic.ssystem.bms.dct.model.CheckDocReqVO;
import com.anylogic.ssystem.bms.dct.model.RegisterDocReqVO;
import com.anylogic.ssystem.bms.dct.model.RegisterDocResVO;
import com.anylogic.ssystem.bms.dct.service.ShieldViewerReqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"dct"})
@RestController
@RequestMapping("/shieldviewer")
public class ShieldViewerController {

    @Autowired
    private ShieldViewerReqService shieldViewerReqService;


    /**
     * insert
     * @param //RegisterDocReqVO
     * return ResponseEntity
     */
    @PostMapping(value = "/registerDocByFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerDocByFile(@RequestPart(value = "files", required = false) MultipartFile mf,
                                               @RequestPart("requiredInsertShieldViewer") RegisterDocReqVO param)
    {
        log.debug("ShieldViewer registerDocByFile" + param.toString());

        RegisterDocResVO res;
        try {
            res = shieldViewerReqService.registerDocByFile(mf, param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(res);
    }


    /**
     * select
     * @param //CheckDocReqVO
     * return ResponseEntity
     */
    @GetMapping("/checkDocConvert")
    public ResponseEntity checkDocConvert
    (
        @Valid CheckDocReqVO param
    ) throws Exception
    {
        log.debug("shieldViewer viewDocument" + param.toString());

        return ResponseEntity.ok(shieldViewerReqService.checkDocConvert(param));
    }
}
