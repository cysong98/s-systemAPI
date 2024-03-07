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
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectOrganizationInfoReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectSymbolReqVO;
import com.anylogic.ssystem.bms.dct.service.ComOrganizationinfoService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"dct"})
@RestController
@RequestMapping("/bms/dct/comorganizationinfo")
public class ComOrganizationinfoController {

    @Autowired
    private ComOrganizationinfoService comOrganizationinfoService;


    /* 관리자쪽 제외 팝업 > 부서 TREE 조회 */
    @GetMapping("/selectOrganization")
    public ResponseEntity selectOrganization
    (
        @Valid SelectOrganizationReqVO param
    ) throws Exception
    {
        log.debug("comOrganizationinfo selectOrganization" + param.toString());

        return ResponseEntity.ok(comOrganizationinfoService.selectOrganization(param));
    }


    /* LDAP 연동된 부서 정보 조회 */
    @GetMapping("/selectOrganizationLDAP")
    public ResponseEntity selectOrganizationLDAP
    (
        @Valid SelectOrganizationReqVO param
    ) throws Exception
    {
        log.debug("comOrganizationinfo selectOrganizationLDAP" + param.toString());

        return ResponseEntity.ok(comOrganizationinfoService.selectOrganizationLDAP(param));
    }


    /* 부서장 정보 조회 */
    @GetMapping("/selectOrganizationInfo")
    public ResponseEntity selectOrganizationInfo
    (
        @Valid SelectOrganizationInfoReqVO param
    ) throws Exception 
    {
        log.debug("comOrganizationinfo selectOrganizationInfo" + param.toString());

        return ResponseEntity.ok(comOrganizationinfoService.selectOrganizationInfo(param));
    }


    /* 대리 결재자 정보 조회 */
    @GetMapping("/selectManagerInfo")
    public ResponseEntity selectManagerInfo
    (
        @Valid SelectOrganizationInfoReqVO param
    ) throws Exception
    {
        log.debug("comOrganizationinfo selectManagerInfo" + param.toString());

        return ResponseEntity.ok(comOrganizationinfoService.selectManagerInfo(param));
    }


    /* 로고/심볼 팝업 조회 */
    @GetMapping("/selectSymbol")
    public ResponseEntity selectSymbol
    (
        @Valid SelectSymbolReqVO param
    ) throws Exception 
    {
        log.debug("comOrganizationinfo selectSymbol" + param.toString());

        return ResponseEntity.ok(comOrganizationinfoService.selectSymbol(param));
    }


}
