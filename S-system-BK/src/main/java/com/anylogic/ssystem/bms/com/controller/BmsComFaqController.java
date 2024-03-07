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
import com.anylogic.ssystem.bms.com.model.SelectFaqListReqVO;
import com.anylogic.ssystem.bms.com.model.DeleteFaqBIAReqVO;
import com.anylogic.ssystem.bms.com.model.GetFaqDetailReqVO;
import com.anylogic.ssystem.bms.com.model.InsertFaqBIAReqVO;
import com.anylogic.ssystem.bms.com.model.UpdateFaqBIAReqVO;
import com.anylogic.ssystem.bms.com.service.BmsComFaqService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/bmscomfaq")
public class BmsComFaqController {

    @Autowired
    private BmsComFaqService bmsComFaqService;

    /* 게시판관리 > FAQ 관리 > FAQ 목록 조회 */
    @GetMapping("/selectFaqList")
    public ResponseEntity selectFaqList
    (
        @Valid SelectFaqListReqVO param
    ) throws Exception 
    {
        log.debug("bmsComFaq selectFaqList" + param.toString());

        return ResponseEntity.ok(bmsComFaqService.selectFaqList(param));
    }


    /* 게시판관리 > FAQ 관리 > FAQ 삭제 */
    @DeleteMapping(value = "/deleteFaqBIA")
    public ResponseEntity deleteFaqBIA
    (
        @Valid @RequestBody List<DeleteFaqBIAReqVO> params
    )
    {
        log.debug("BmsComFaq deleteFaqBIA" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = bmsComFaqService.deleteFaqBIA(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 게시판관리 > FAQ 관리 > FAQ 상세 조회 */
    @GetMapping("/getFaqDetail")
    public ResponseEntity getFaqDetail
    (
        @Valid GetFaqDetailReqVO param

    ) throws Exception
    {
        log.debug("bmsComFaq getFaqDetail" + param.toString());

        return ResponseEntity.ok(bmsComFaqService.getFaqDetail(param));
    }


    /* 게시판관리 > FAQ 관리 > FAQ 등록 */
    @PostMapping(value = "/insertFaqBIA")
    public ResponseEntity insertFaqBIA
    (
        @Valid @RequestBody InsertFaqBIAReqVO param
    )
    {
        log.debug("bmsComFaq insertFaqBIA" + param.toString());

        int res = 0;
        try {
            res = bmsComFaqService.insertFaqBIA(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 게시판관리 > FAQ 관리 > FAQ 수정 */
    @PutMapping(value = "/updateFaqBIA")
    public ResponseEntity updateFaqBIA
    (
        @Valid @RequestBody UpdateFaqBIAReqVO param
    )
    {
        log.debug("bmsComFaq updateFaqBIA" + param.toString());

        int res = 0;
        try {
            res = bmsComFaqService.updateFaqBIA(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


}
