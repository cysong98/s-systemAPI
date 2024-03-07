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

import com.anylogic.ssystem.bms.use.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.anylogic.ssystem.bms.use.service.BmsDctMgmtRegiService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"use"})
@RestController
@RequestMapping("/bms/use/bmsdctmgmtregi")
public class BmsDctMgmtRegiController {

    @Autowired
    private BmsDctMgmtRegiService bmsDctMgmtRegiService;

    /* 예고문 도래 알림 목록 조회 */
    @GetMapping("/selectAlert")
    public ResponseEntity selectAlert
    (
        @Valid SelectAlertReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctMgmtRegiService.selectAlert(param));
    }


    /* 비밀문서사용요청 > 열람요청 > 열람요청 목록 조회 */
    @GetMapping("/selectUseDigitalView")
    public ResponseEntity selectUseDigitalView
    (
        @Valid SelectUserReqestsListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctMgmtRegiService.selectUseDigitalView(param));
    }


    /* 비밀문서사용요청 > 재사용요청 > 재사용요청 목록 조회 */
    @GetMapping("/selectReUse")
    public ResponseEntity selectReUse
    (
        @Valid SelectUserReqestsListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctMgmtRegiService.selectReUse(param));
    }


    /* 비밀문서사용요청 > 인쇄요청 > 인쇄요청 목록 조회 */
    @GetMapping("/selectUsePrint")
    public ResponseEntity selectUsePrint
    (
        @Valid SelectUserReqestsListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctMgmtRegiService.selectUsePrint(param));
    }


    /* 비밀문서사용요청 > 반출요청 > 반출요청 목록 조회 */
    @GetMapping("/selectUseTakeOut")
    public ResponseEntity selectUseTakeOut
    (
        @Valid SelectUserReqestsListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctMgmtRegiService.selectUseTakeOut(param));
    }


    /* 예고문도래목록 목록 조회 */
    @GetMapping("/selectComingList")
    public ResponseEntity selectComingList
    (
        @Valid SelectComingListReqVO param
    ) throws Exception 
    {
        return ResponseEntity.ok(bmsDctMgmtRegiService.selectComingList(param));
    }


    /* 각 요청서의 요청내용 > 요청된 문서들 정보 조회 */
    @GetMapping("/getDctMgmtRegi")
    public ResponseEntity getDctMgmtRegi
    (
        @Valid GetDctMgmtRegiReqVO param

    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctMgmtRegiService.getDctMgmtRegi(param));
    }


    /* 각 요청서의 요청내용 > 요청된 문서들 정보 조회 - 예고문정보 포함 */
    @GetMapping("/getDctMgmtRegiAttr")
    public ResponseEntity getDctMgmtRegiAttr
    (
        @Valid GetDctMgmtRegiReqVO param

    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctMgmtRegiService.getDctMgmtRegiAttr(param));
    }


    /* 각 요청서의 요청내용 > 요청된 문서들 정보 조회 - 첨부파일 포함 */
    @GetMapping("/getDctMgmtRegiPrint")
    public ResponseEntity getDctMgmtRegiPrint
    (
        @Valid GetDctMgmtRegiReqVO param

    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctMgmtRegiService.getDctMgmtRegiPrint(param));
    }

}
