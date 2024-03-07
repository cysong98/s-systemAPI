/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.controller;

import java.util.*;

import com.anylogic.ssystem.bms.com.model.*;
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
import com.anylogic.ssystem.bms.com.service.ComOrganizationinfoService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/comorganizationinfo")
public class ComOrganizationinfoController {

    @Autowired
    private ComOrganizationinfoService comOrganizationinfoService;


    /* 부서 정보 TREE 조회 */
    @GetMapping("/selectOrganizationList")
    public ResponseEntity selectOrganizationList
    (
        @Valid SelectOrganizationListReqVO param
    ) throws Exception 
    {
        log.debug("comOrganizationinfo selectOrganizationList" + param.toString());

        return ResponseEntity.ok(comOrganizationinfoService.selectOrganizationList(param));
    }


    /* 조직관리 > 부서/사용자관리 > 부서 삭제 */
    @DeleteMapping(value = "/deleteOrganizationBIA")
    public ResponseEntity deleteOrganizationBIA
    (
        @Valid @RequestBody List<DeleteOrganizationBIAReqVO> params
    )
    {
        log.debug("ComOrganizationinfo deleteOrganizationBIA" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = comOrganizationinfoService.deleteOrganizationBIA(params);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 부서/사용자관리 > 부서 상세 조회 */
    @GetMapping("/getOrganizationDetail")
    public ResponseEntity getOrganizationDetail
    (
        @Valid GetOrganizationDetailReqVO param
        
    ) throws Exception 
    {
        log.debug("comOrganizationinfo getOrganizationDetail" + param.toString());

        return ResponseEntity.ok(comOrganizationinfoService.getOrganizationDetail(param));
    }


    /* 조직관리 > 부서/사용자관리 > 부서 등록 */
    @PostMapping(value = "/insertOrganizationBIA")
    public ResponseEntity insertOrganizationBIA
    (
        @Valid @RequestBody InsertOrganizationBIAReqVO param
    ) 
    {
        log.debug("comOrganizationinfo insertOrganizationBIA" + param.toString());

        int res = 0;
        try {
            res = comOrganizationinfoService.insertOrganizationBIA(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 부서/사용자관리 > 부서 수정 */
    @PutMapping(value = "/updateOrganizationBIA")
    public ResponseEntity updateOrganizationBIA
    (
        @Valid @RequestBody UpdateOrganizationBIAReqVO param
    ) 
    {
        log.debug("comOrganizationinfo updateOrganizationBIA" + param.toString());

        int res = 0;
        try {
            res = comOrganizationinfoService.updateOrganizationBIA(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 부서/사용자관리 > 최상위부서 관리 팝업 > 부서 권한 수정 */
    @PostMapping(value = "/changeToporgVgroup")
    public ResponseEntity changeToporgVgroup(@Valid @RequestBody ChangeToporgVgroupReqVO param)
    {
        int res = 0;
        try {
            res = comOrganizationinfoService.changeToporgVgroup(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 서명인관리 > 서명인 목록 조회 */
    @GetMapping("/selectSignList")
    public ResponseEntity selectSignList
    (
            @Valid SelectSignListReqVO param
    ) throws Exception
    {
        log.debug("comUserinfo selectSignList" + param.toString());

        return ResponseEntity.ok(comOrganizationinfoService.selectSignList(param));
    }


    /* 조직관리 > 서명인관리 > 서명인 상세 조회 */
    @GetMapping(value = "/getSignDetail")
    public ResponseEntity getSignDetail(@Valid GetSignDetailReqVO param) {
        log.debug("ComUserinfo getSignDetail" + param.toString());

        return ResponseEntity.ok(comOrganizationinfoService.getSignDetail(param));
    }


    /* 조직관리 > 심볼/로고관리 > 심볼/로고 목록 조회 */
    @GetMapping("/selectLogoList")
    public ResponseEntity selectLogoList
    (
            @Valid SelectLogoListReqVO param
    ) throws Exception
    {
        log.debug("comUserinfo selectLogoList" + param.toString());

        return ResponseEntity.ok(comOrganizationinfoService.selectLogoList(param));
    }


    /* 조직관리 > 심볼/로고관리 > 심볼/로고 상세 조회 */
    @GetMapping(value = "/getLogoDetail")
    public ResponseEntity getLogoDetail(@Valid GetLogoDetailReqVO param) {
        log.debug("ComUserinfo getLogoDetail" + param.toString());

        return ResponseEntity.ok(comOrganizationinfoService.getLogoDetail(param));
    }
}
