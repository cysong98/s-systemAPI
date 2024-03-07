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
import com.anylogic.ssystem.bms.com.model.SelectNoticeListReqVO;
import com.anylogic.ssystem.bms.com.model.DeleteNoticeBIAReqVO;
import com.anylogic.ssystem.bms.com.model.GetNoticeDetailReqVO;
import com.anylogic.ssystem.bms.com.model.InsertNoticeBIAReqVO;
import com.anylogic.ssystem.bms.com.model.UpdateNoticeBIAReqVO;
import com.anylogic.ssystem.bms.com.service.BmsComNoticeService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/bmscomnotice")
public class BmsComNoticeController {

    @Autowired
    private BmsComNoticeService bmsComNoticeService;


    /* 게시판관리 > 공지사항 관리 > 공지사항 목록 조회 */
    @GetMapping("/selectNoticeList")
    public ResponseEntity selectNoticeList
    (
        @Valid SelectNoticeListReqVO param
    ) throws Exception 
    {
        log.debug("bmsComNotice selectNoticeList" + param.toString());

        return ResponseEntity.ok(bmsComNoticeService.selectNoticeList(param));
    }


    /* 게시판관리 > 공지사항 관리 > 공지사항 삭제 */
    @DeleteMapping(value = "/deleteNoticeBIA")
    public ResponseEntity deleteNoticeBIA
    (
        @Valid @RequestBody List<DeleteNoticeBIAReqVO> params
    )
    {
        log.debug("BmsComNotice deleteNoticeBIA" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = bmsComNoticeService.deleteNoticeBIA(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 게시판관리 > 공지사항 관리 > 공지사항 상세 조회 */
    @GetMapping("/getNoticeDetail")
    public ResponseEntity getNoticeDetail
    (
        @Valid GetNoticeDetailReqVO param

    ) throws Exception
    {
        log.debug("bmsComNotice getNoticeDetail" + param.toString());

        return ResponseEntity.ok(bmsComNoticeService.getNoticeDetail(param));
    }


    /* 게시판관리 > 공지사항 관리 > 공지사항 등록 */
    @PostMapping(value = "/insertNoticeBIA")
    public ResponseEntity insertNoticeBIA
    (
        @Valid @RequestBody InsertNoticeBIAReqVO param
    )
    {
        log.debug("bmsComNotice insertNoticeBIA" + param.toString());

        int res = 0;
        try {
            res = bmsComNoticeService.insertNoticeBIA(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 게시판관리 > 공지사항 관리 > 공지사항 수정 */
    @PutMapping(value = "/updateNoticeBIA")
    public ResponseEntity updateNoticeBIA
    (
        @Valid @RequestBody UpdateNoticeBIAReqVO param
    )
    {
        log.debug("bmsComNotice updateNoticeBIA" + param.toString());

        int res = 0;
        try {
            res = bmsComNoticeService.updateNoticeBIA(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


}
