/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.controller;

import java.util.*;

import com.anylogic.ssystem.bms.dct.model.DeleteTempListReqVO;
import com.anylogic.ssystem.bms.use.model.*;
import com.anylogic.ssystem.bms.use.service.BmsDctApprreqPathService;
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
import com.anylogic.ssystem.bms.use.service.BmsDctApprreqService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"use"})
@RestController
@RequestMapping("/bms/use/bmsdctapprreq")
public class BmsDctApprreqController {

    @Autowired
    private BmsDctApprreqService bmsDctApprreqService;

    @Autowired
    private BmsDctApprreqPathService bmsDctApprreqPathService;


    /* 비밀문서 사용(처리할, 기안한, 처리한, 요청서완료처리) 목록 조회 */
    @GetMapping("/selectUseReqList")
    public ResponseEntity selectUseReqList
    (
            @Valid SelectUseReqListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctApprreqService.selectUseReqList(param));
    }


    /* 각 요청서 상세화면 조회 */
    @GetMapping("/selectUseReqViewDetail")
    public ResponseEntity selectUseReqViewDetail(@Valid SelectUseReqViewDetailReqVO param) throws Exception
    {
        return ResponseEntity.ok(bmsDctApprreqService.selectUseReqViewDetail(param));
    }


    /* 각 요청서 승인/반려 처리 */
    @PutMapping(value = "/updateUseReqViewAppPathApproveBIA")
    public ResponseEntity updateUseReqViewAppPathApproveBIA(@RequestBody UpdateUseReqViewAppPathApproveBIAReqVO param)
    {
        int res = 0;
        try {
            res = bmsDctApprreqPathService.updateUseReqViewAppPathApproveBIA(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 각 요청서 실행 처리 & 요청서회수 & 권한회수 */
    @PutMapping(value = "/updateUseReqViewAppStatusBIA")
    public ResponseEntity updateUseReqViewAppStatusBIA(@Valid @RequestBody UpdateUseReqViewAppApproveReqVO param)
    {
        int res = 0;
        try {
            res = bmsDctApprreqService.updateUseReqViewAppStatusBIA(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* (비밀문서사용처리 > 기안한 요청서 > 요청서회수/권한회수된 요청서 삭제) & (재사용/인쇄/반출 실행 시 열람권한이 없다면 요청서 삭제) */
    @DeleteMapping(value = "/deleteUseReqList")
    public ResponseEntity deleteUseReqList
    (
            @Valid @RequestBody List<DeleteUseReqListReqVO> params
    )
    {
        int res = 0;
        try {
            res = bmsDctApprreqService.deleteUseReqList(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 인쇄실행 팝업 > 인쇄한 부수 체크 */
    @GetMapping(value = "/checkPrtCnt")
    public ResponseEntity checkPrtCnt(@Valid CheckPrtCntReqVO param) throws Exception
    {
        return ResponseEntity.ok(bmsDctApprreqService.checkPrtCnt(param));
    }


    /* 인쇄실행 팝업 > 인쇄부수 수정 */
    @PutMapping(value = "/updatePrtCnt")
    public ResponseEntity updatePrtCnt(@Valid @RequestBody UpdatePrtCntReqVO param)
    {
        int res = 0;
        try {
            res = bmsDctApprreqService.updatePrtCnt(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(res);
    }


    /* 재사용 가능 여부 체크*/
    @GetMapping("/checkIsReusable")
    public ResponseEntity checkIsReusable(@Valid CheckIsReusableReqVO param) throws Exception
    {
        return ResponseEntity.ok(bmsDctApprreqService.checkIsReusable(param));
    }


    /* 비밀관리기록부 팝업 > 재분류 요청자,확인자 정보 조회 */
    @GetMapping("/selectReclassPathInfo")
    public ResponseEntity selectReclassPathInfo(@Valid SelectPathInfoReqVO param) throws Exception
    {
        return ResponseEntity.ok(bmsDctApprreqService.selectReclassPathInfo(param));
    }


    /* 비밀관리기록부 팝업 > 파기 요청자,확인자 정보 조회 */
    @GetMapping("/selectDestructPathInfo")
    public ResponseEntity selectDestructPathInfo(@Valid SelectPathInfoReqVO param) throws Exception
    {
        return ResponseEntity.ok(bmsDctApprreqService.selectDestructPathInfo(param));
    }


    /* 비밀관리기록부 팝업 > 이관 요청자,확인자 정보 조회 */
    @GetMapping("/selectTransPathInfo")
    public ResponseEntity selectTransPathInfo(@Valid SelectPathInfoReqVO param) throws Exception
    {
        return ResponseEntity.ok(bmsDctApprreqService.selectTransPathInfo(param));
    }


    /* 비밀관리기록부 팝업 > 존안 요청자,확인자 정보 조회 */
    @GetMapping("/selectCnsrvPathInfo")
    public ResponseEntity selectCnsrvPathInfo(@Valid SelectPathInfoReqVO param) throws Exception
    {
        return ResponseEntity.ok(bmsDctApprreqService.selectCnsrvPathInfo(param));
    }

}
