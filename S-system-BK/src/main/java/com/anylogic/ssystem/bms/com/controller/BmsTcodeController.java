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
import com.anylogic.ssystem.bms.com.model.SelectCodeCategoryListReqVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.com.model.DeleteCodeCategoryReqVO;
import com.anylogic.ssystem.bms.com.model.InsertCodeCategoryReqVO;
import com.anylogic.ssystem.bms.com.model.GetCodeCategoryDetailReqVO;
import com.anylogic.ssystem.bms.com.model.UpdateCodeCategoryReqVO;
import com.anylogic.ssystem.bms.com.service.BmsTcodeService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/bmstcode")
public class BmsTcodeController {

    @Autowired
    private BmsTcodeService bmsTcodeService;


    /* 시스템관리 > 코드관리 > 코드분류 목록 조회 */
    @GetMapping("/selectCodeCategoryList")
    public ResponseEntity selectCodeCategoryList
    (
        @Valid SelectCodeCategoryListReqVO param
    ) throws Exception 
    {
        log.debug("bmsTcode selectCodeCategoryList" + param.toString());

        return ResponseEntity.ok(bmsTcodeService.selectCodeCategoryList(param));
    }


    /* 시스템관리 > 코드관리 > 코드분류 삭제 */
    @DeleteMapping(value = "/deleteCodeCategory")
    public ResponseEntity deleteCodeCategory
    (
        @Valid @RequestBody List<DeleteCodeCategoryReqVO> params
    ) 
    {
        log.debug("bmsTcode deleteCodeCategory" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = bmsTcodeService.deleteCodeCategory(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 시스템관리 > 코드관리 > 코드분류 등록 */
    @PostMapping(value = "/insertCodeCategory")
    public ResponseEntity insertCodeCategory
    (
        @Valid @RequestBody InsertCodeCategoryReqVO param
    ) 
    {
        log.debug("bmsTcode insertCodeCategory" + param.toString());

        int res = 0;
        try {
            res = bmsTcodeService.insertCodeCategory(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 시스템관리 > 코드관리 > 코드분류 상세 조회 */
    @GetMapping("/getCodeCategoryDetail")
    public ResponseEntity getCodeCategoryDetail
    (
        @Valid GetCodeCategoryDetailReqVO param
        
    ) throws Exception 
    {
        log.debug("bmsTcode getCodeCategoryDetail" + param.toString());

        return ResponseEntity.ok(bmsTcodeService.getCodeCategoryDetail(param));
    }


    /* 시스템관리 > 코드관리 > 코드분류 수정 */
    @PutMapping(value = "/updateCodeCategory")
    public ResponseEntity updateCodeCategory
    (
        @Valid @RequestBody UpdateCodeCategoryReqVO param
    ) 
    {
        log.debug("bmsTcode updateCodeCategory" + param.toString());

        int res = 0;
        try {
            res = bmsTcodeService.updateCodeCategory(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


}
