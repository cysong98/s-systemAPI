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
import com.anylogic.ssystem.bms.com.service.ComJobgubunService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.anylogic.ssystem.bms.com.service.ComGradeService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/comjobgubun")
public class ComJobgubunController {

    @Autowired
    private ComJobgubunService comJobgubunService;


    /* 조직관리 > 직위/직급관리 > 직위관리 목록 조회 */
    @GetMapping("/selectJobgubunList")
    public ResponseEntity selectJobgubunList
    (
            @Valid SelectJobgubunListReqVO param
    ) throws Exception
    {
        log.debug("comJobgubun selectJobgubunList" + param.toString());

        return ResponseEntity.ok(comJobgubunService.selectJobgubunList(param));
    }


    /* 조직관리 > 직위/직급관리 > 직위 삭제 */
    @DeleteMapping(value = "/deleteJobgubun")
    public ResponseEntity deleteJobgubun
    (
            @Valid @RequestBody List<DeleteJobgubunReqVO> params
    )
    {
        log.debug("ComJobgubun deleteJobgubun" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = comJobgubunService.deleteJobgubun(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 직위/직급관리 > 직위 상세 조회 */
    @GetMapping("/getJobgubunDetail")
    public ResponseEntity getJobgubunDetail
    (
            @Valid GetJobgubunDetailReqVO param

    ) throws Exception
    {
        log.debug("comJobgubun getJobgubunDetail" + param.toString());

        return ResponseEntity.ok(comJobgubunService.getJobgubunDetail(param));
    }


    /* 조직관리 > 직위/직급관리 > 직위 등록 */
    @PostMapping(value = "/insertJobgubun")
    public ResponseEntity insertJobgubun
    (
            @Valid @RequestBody InsertJobgubunReqVO param
    )
    {
        log.debug("comJobgubun insertJobgubun" + param.toString());

        int res = 0;
        try {
            res = comJobgubunService.insertJobgubun(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 직위/직급관리 > 직위 수정 */
    @PutMapping(value = "/updateJobgubun")
    public ResponseEntity updateJobgubun
    (
            @Valid @RequestBody UpdateJobgubunReqVO param
    )
    {
        log.debug("comJobgubun updateJobgubun" + param.toString());

        int res = 0;
        try {
            res = comJobgubunService.updateJobgubun(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


}
