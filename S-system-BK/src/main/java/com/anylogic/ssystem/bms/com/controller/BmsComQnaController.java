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
import com.anylogic.ssystem.bms.com.model.SelectQnaListReqVO;
import com.anylogic.ssystem.bms.com.model.DeleteQnaReqVO;
import com.anylogic.ssystem.bms.com.model.GetQnaDetailReqVO;
import com.anylogic.ssystem.bms.com.model.InsertQnaReqVO;
import com.anylogic.ssystem.bms.com.model.UpdateQnaReqVO;
import com.anylogic.ssystem.bms.com.service.BmsComQnaService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/bmscomqna")
public class BmsComQnaController {

    @Autowired
    private BmsComQnaService bmsComQnaService;


    /* 게시판관리 > Q&A 관리 > Q&A 목록 조회 */
    @GetMapping("/selectQnaList")
    public ResponseEntity selectQnaList
    (
        @Valid SelectQnaListReqVO param
    ) throws Exception 
    {
        log.debug("bmsComQna selectQnaList" + param.toString());

        return ResponseEntity.ok(bmsComQnaService.selectQnaList(param));
    }


    /* 게시판관리 > Q&A 관리 > Q&A 삭제 */
    @DeleteMapping(value = "/deleteQna")
    public ResponseEntity deleteQna
    (
        @Valid @RequestBody List<DeleteQnaReqVO> params
    )
    {
        log.debug("BmsComQna deleteQna" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = bmsComQnaService.deleteQna(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 게시판관리 > Q&A 관리 > Q&A 상세 조회 */
    @GetMapping("/getQnaDetail")
    public ResponseEntity getQnaDetail
    (
        @Valid GetQnaDetailReqVO param

    ) throws Exception
    {
        log.debug("bmsComQna getQnaDetail" + param.toString());

        return ResponseEntity.ok(bmsComQnaService.getQnaDetail(param));
    }


    /* 게시판관리 > Q&A 관리 > Q&A 등록 */
    @PostMapping(value = "/insertQna")
    public ResponseEntity insertQna
    (
        @Valid @RequestBody InsertQnaReqVO param
    )
    {
        log.debug("bmsComQna insertQna" + param.toString());

        int res = 0;
        try {
            res = bmsComQnaService.insertQna(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 게시판관리 > Q&A 관리 > Q&A 수정 */
    @PutMapping(value = "/updateQna")
    public ResponseEntity updateQna
    (
        @Valid @RequestBody UpdateQnaReqVO param
    )
    {
        log.debug("bmsComQna updateQna" + param.toString());

        int res = 0;
        try {
            res = bmsComQnaService.updateQna(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


}
