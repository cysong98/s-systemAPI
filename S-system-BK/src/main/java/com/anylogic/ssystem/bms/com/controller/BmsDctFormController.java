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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.com.service.BmsDctFormService;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/bmsdctform")
public class BmsDctFormController {

    @Autowired
    private BmsDctFormService bmsDctFormService;


    /* 시스템관리 > 서식관리 > 서식 목록 조회 */
    @GetMapping("/selectFormList")
    public ResponseEntity selectFormList
    (
        @Valid SelectFormListReqVO param
    ) throws Exception 
    {
        log.debug("bmsDctForm selectFormList" + param.toString());

        return ResponseEntity.ok(bmsDctFormService.selectFormList(param));
    }


    /* 시스템관리 > 서식관리 > 서식 삭제 */
    @DeleteMapping(value = "/deleteForm")
    public ResponseEntity deleteForm
    (
        @Valid @RequestBody List<DeleteFormReqVO> params
    )
    {
        log.debug("BmsDctForm deleteForm" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = bmsDctFormService.deleteForm(params);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 시스템관리 > 서식관리 > 서식 상세 조회 */
    @GetMapping("/getFormDetail")
    public ResponseEntity getFormDetail
    (
        @Valid GetFormDetailReqVO param
        
    ) throws Exception 
    {
        log.debug("bmsDctForm getFormDetail" + param.toString());

        return ResponseEntity.ok(bmsDctFormService.getFormDetail(param));
    }


    /* 시스템관리 > 서식관리 > 서식 등록 */
    @PostMapping(value = "/insertFormBIA", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertFormBIA (@RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                                            @RequestPart("requiredInsertFormBIA") InsertFormBIAReqVO param)
    {
        log.debug("bmsDctForm insertFormBIA" + param.toString());

        int res = 0;
        try {
            res = bmsDctFormService.insertFormBIA(multipartFiles, param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 시스템관리 > 서식관리 > 서식 수정 */
    @PutMapping(value = "/updateFormBIA", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateFormBIA(@RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                                           @RequestPart("requiredUpdateFormBIA") UpdateFormBIAReqVO param)
    {
        log.debug("bmsDctForm updateFormBIA" + param.toString());

        int res = 0;
        try {
            res = bmsDctFormService.updateFormBIA(multipartFiles, param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }

}
