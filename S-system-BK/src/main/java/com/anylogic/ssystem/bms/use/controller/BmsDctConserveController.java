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

import com.anylogic.ssystem.bms.dct.model.SelectMgmtRegiListReqVO;
import com.anylogic.ssystem.bms.use.model.*;
import com.anylogic.ssystem.bms.use.service.BmsDctConserveService;
import com.anylogic.ssystem.bms.use.service.BmsDctMgmtRegiService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"use"})
@RestController
@RequestMapping("/bms/use/bmsdctconserve")
public class BmsDctConserveController {

    @Autowired
    private BmsDctConserveService bmsDctConserveService;

    /* 존안 비밀관리기록부 목록 조회 */
    @GetMapping("/selectUseConserveMgmtList")
    public ResponseEntity selectUseConserveMgmtList( SelectUserReqestsListReqVO param) throws Exception
    {
        return ResponseEntity.ok(bmsDctConserveService.selectUseConserveMgmtList(param));
    }

    /* 이관 비밀관리기록부 목록 조회 */
    @GetMapping("/selectUseTransMgmtList")
    public ResponseEntity selectUseTransMgmtList( SelectUserReqestsListReqVO param) throws Exception
    {
        return ResponseEntity.ok(bmsDctConserveService.selectUseTransMgmtList(param));
    }


}
