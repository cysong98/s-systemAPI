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

import com.anylogic.ssystem.bms.com.model.SelectUserVgroupReqVO;
import com.anylogic.ssystem.bms.dct.model.*;
import com.anylogic.ssystem.bms.trn.model.SelectMgmtRegiListCountReqVO;
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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.dct.service.BmsDctMgmtRegiService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"dct"})
@RestController
@RequestMapping("/bms/dct/bmsdctmgmtregi")
public class BmsDctMgmtRegiController {

    @Autowired
    private BmsDctMgmtRegiService bmsDctMgmtRegiService;


    /* 비밀관리기록부 목록 조회 */
    @GetMapping("/selectMgmtRegiList")
    public ResponseEntity selectMgmtRegiList
    (
        @Valid SelectMgmtRegiListReqVO param
    ) throws Exception 
    {
        log.debug("bmsDctMgmtRegi selectMgmtRegiList" + param.toString());

        return ResponseEntity.ok(bmsDctMgmtRegiService.selectMgmtRegiList(param));
    }


    /* 인계인수 건수별 팝업 조회 (미완료/생산/접수/일반) */
    @GetMapping("/selectMgmtRegiCnt")
    public ResponseEntity selectMgmtRegiCnt
    (
        @Valid SelectMgmtRegiCntReqVO param
    ) throws Exception {
        return ResponseEntity.ok(bmsDctMgmtRegiService.selectMgmtRegiCnt(param));
    }


    /* 타부서 목록 조회 (본인 부서 제외) */
    @GetMapping("/selectOtherDept")
    public ResponseEntity selectOtherDept
    (
        @Valid SelectOtherDeptReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctMgmtRegiService.selectOtherDept(param));
    }


    /* 비밀관리기록부 팝업 조회 (재분류/파기/이관/존안 정보) */
    @GetMapping("/getMgmtRegi")
    public ResponseEntity getMgmtRegi
    (
        @Valid GetMgmtRegiReqVO param
        
    ) throws Exception 
    {
        log.debug("bmsDctMgmtRegi getMgmtRegi" + param.toString());

        return ResponseEntity.ok(bmsDctMgmtRegiService.getMgmtRegi(param));
    }


    /* 인계인수 대상 목록 조회 */
    @GetMapping("/selectMgmtRegiNonPageList")
    public ResponseEntity selectMgmtRegiNonPageList
    (
        @Valid GetMgmtRegiReqVO param

    ) throws Exception
    {
        log.debug("bmsDctMgmtRegi selectMgmtRegiNonPageList" + param.toString());

        return ResponseEntity.ok(bmsDctMgmtRegiService.selectMgmtRegiNonPageList(param));
    }


    /* DOCID로 MGMTID 조회 */
    @GetMapping("/getMgmtidByDocid")
    public ResponseEntity getMgmtidByDocid
    (
        @Valid GetMgmtidByDocidReqVO param

    ) throws Exception
    {
        log.debug("bmsDctMgmtRegi getMgmtidByDocid" + param.toString());

        return ResponseEntity.ok(bmsDctMgmtRegiService.getMgmtidByDocid(param));
    }

}
