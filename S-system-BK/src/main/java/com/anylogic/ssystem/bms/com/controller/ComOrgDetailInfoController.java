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
import com.anylogic.ssystem.bms.com.service.ComOrgDetailInfoService;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/comorgdetailinfo")
public class ComOrgDetailInfoController {

    @Autowired
    private ComOrgDetailInfoService comOrgDetailInfoService;

    /* 조직관리 > 서명인관리 > 서명인 삭제 */
    @DeleteMapping(value = "/deleteSignBIA")
    public ResponseEntity deleteSignBIA
    (
        @Valid @RequestBody List<DeleteSignBIAReqVO> params
    )
    {
        log.debug("ComOrgDetailInfo deleteSignBIA" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = comOrgDetailInfoService.deleteSignBIA(params);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 서명인관리 > 서명인 등록 */
    @PostMapping(value = "/insertSignBIA", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertSignBIA(@RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                                            @RequestPart("requiredInsertSignBIA") InsertSignBIAReqVO param)
    {
        log.debug("comOrgDetailInfo insertSignBIA" + param.toString());

        int res = 0;
        try {
            res = comOrgDetailInfoService.insertSignBIA(multipartFiles, param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 서명인관리 > 서명인 수정 */
    @PutMapping(value = "/updateSignBIA", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateSignBIA(@RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                                            @RequestPart("requiredUpdateSignBIA") UpdateSignBIAReqVO param)
    {
        log.debug("comOrgDetailInfo updateSignBIA" + param.toString());

        int res = 0;
        try {
            res = comOrgDetailInfoService.updateSignBIA(multipartFiles, param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 심볼/로고관리 > 심볼/로고 삭제 */
    @DeleteMapping(value = "/deleteLogoBIA")
    public ResponseEntity deleteLogoBIA
    (
            @Valid @RequestBody List<DeleteLogoBIAReqVO> params
    )
    {
        log.debug("ComOrgDetailInfo deleteLogoBIA" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = comOrgDetailInfoService.deleteLogoBIA(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 심볼/로고관리 > 심볼/로고 등록 */
    @PostMapping(value = "/insertLogoBIA", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertLogoBIA(@RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                                           @RequestPart("requiredInsertLogoBIA") InsertLogoBIAReqVO param)
    {
        log.debug("comOrgDetailInfo insertLogoBIA" + param.toString());

        int res = 0;
        try {
            res = comOrgDetailInfoService.insertLogoBIA(multipartFiles, param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 심볼/로고관리 > 심볼/로고 수정 */
    @PutMapping(value = "/updateLogoBIA", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateLogoBIA(@RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                                           @RequestPart("requiredUpdateLogoBIA") UpdateLogoBIAReqVO param)
    {
        log.debug("comOrgDetailInfo updateLogoBIA" + param.toString());

        int res = 0;
        try {
            res = comOrgDetailInfoService.updateLogoBIA(multipartFiles, param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }
}
