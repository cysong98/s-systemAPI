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
@RequestMapping("/bms/com/comgrade")
public class ComGradeController {

    @Autowired
    private ComGradeService comGradeService;


    /* 조직관리 > 직위/직급관리 > 직급관리 목록 조회 */
    @GetMapping("/selectGradeList")
    public ResponseEntity selectGradeList
    (
            @Valid SelectGradeListReqVO param
    ) throws Exception
    {
        log.debug("comGrade selectGradeList" + param.toString());

        return ResponseEntity.ok(comGradeService.selectGradeList(param));
    }


    /* 조직관리 > 직위/직급관리 > 직급 삭제 */
    @DeleteMapping(value = "/deleteGrade")
    public ResponseEntity deleteGradeBIA
    (
            @Valid @RequestBody List<DeleteGradeReqVO> params
    )
    {
        log.debug("ComGrade deleteGrade" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = comGradeService.deleteGrade(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 직위/직급관리 > 직급 상세 조회 */
    @GetMapping("/getGradeDetail")
    public ResponseEntity getGradeDetail
    (
            @Valid GetGradeDetailReqVO param

    ) throws Exception
    {
        log.debug("comGrade getGradeDetail" + param.toString());

        return ResponseEntity.ok(comGradeService.getGradeDetail(param));
    }


    /* 조직관리 > 직위/직급관리 > 직급 등록 */
    @PostMapping(value = "/insertGrade")
    public ResponseEntity insertGrade
    (
            @Valid @RequestBody InsertGradeReqVO param
    )
    {
        log.debug("comGrade insertGrade" + param.toString());

        int res = 0;
        try {
            res = comGradeService.insertGrade(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 직위/직급관리 > 직급 수정 */
    @PutMapping(value = "/updateGrade")
    public ResponseEntity updateGrade
    (
            @Valid @RequestBody UpdateGradeReqVO param
    )
    {
        log.debug("comGrade updateGrade" + param.toString());

        int res = 0;
        try {
            res = comGradeService.updateGrade(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


}
