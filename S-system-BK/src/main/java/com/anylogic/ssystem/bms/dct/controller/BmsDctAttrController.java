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

import com.anylogic.ssystem.bms.dct.model.GetDctAttrAddReqVO;
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
import com.anylogic.ssystem.bms.dct.model.InsertAttributeReqVO;
import com.anylogic.ssystem.bms.dct.model.GetDctAttrReqVO;
import com.anylogic.ssystem.bms.dct.service.BmsDctAttrService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"dct"})
@RestController
@RequestMapping("/bms/dct/bmsdctattr")
public class BmsDctAttrController {

    @Autowired
    private BmsDctAttrService bmsDctAttrService;

    /* 비밀 속성 상세 조회 */
    @GetMapping("/getDctAttr")
    public ResponseEntity getDctAttr
    (
        @Valid GetDctAttrReqVO param
        
    ) throws Exception 
    {
        log.debug("bmsDctAttr getDctAttr" + param.toString());

        return ResponseEntity.ok(bmsDctAttrService.getDctAttr(param));
    }


    /* 비전자 비밀 속성 상세 조회 */
    @GetMapping("/getDctAttrAdd")
    public ResponseEntity getDctAttrAdd
    (
        @Valid GetDctAttrAddReqVO param

    ) throws Exception
    {
        log.debug("bmsDctAttr getDctAttrAdd" + param.toString());

        return ResponseEntity.ok(bmsDctAttrService.getDctAttrAdd(param));
    }

}
