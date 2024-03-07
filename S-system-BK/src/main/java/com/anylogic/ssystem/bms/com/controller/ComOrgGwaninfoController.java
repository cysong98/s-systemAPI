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
import com.anylogic.ssystem.bms.com.model.SelectGwanListReqVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.com.model.DeleteGwanBIAReqVO;
import com.anylogic.ssystem.bms.com.model.GetGwanDetailReqVO;
import com.anylogic.ssystem.bms.com.model.InsertGwanBIAReqVO;
import com.anylogic.ssystem.bms.com.model.UpdateGwanBIAReqVO;
import com.anylogic.ssystem.bms.com.service.ComOrgGwaninfoService;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/comorggwaninfo")
public class ComOrgGwaninfoController {

    @Autowired
    private ComOrgGwaninfoService comOrgGwaninfoService;


    /* 조직관리 > 관인관리 > 관인 목록 */
    @GetMapping("/selectGwanList")
    public ResponseEntity selectGwanList
    (
        @Valid SelectGwanListReqVO param
    ) throws Exception 
    {
        log.debug("comOrgGwaninfo selectGwanList" + param.toString());

        return ResponseEntity.ok(comOrgGwaninfoService.selectGwanList(param));
    }


    /* 조직관리 > 관인관리 > 관인 삭제 */
    @DeleteMapping(value = "/deleteGwanBIA")
    public ResponseEntity deleteGwanBIA
    (
        @Valid @RequestBody List<DeleteGwanBIAReqVO> params
    )
    {
        log.debug("ComOrgGwaninfo deleteGwanBIA" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = comOrgGwaninfoService.deleteGwanBIA(params);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 관인관리 > 관인 상세 조회 */
    @GetMapping("/getGwanDetail")
    public ResponseEntity getGwanDetail
    (
        @Valid GetGwanDetailReqVO param
        
    ) throws Exception 
    {
        log.debug("comOrgGwaninfo getGwanDetail" + param.toString());

        return ResponseEntity.ok(comOrgGwaninfoService.getGwanDetail(param));
    }


    /* 조직관리 > 관인관리 > 관인 등록 */
    @PostMapping(value = "/insertGwanBIA", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertGwanBIA(@RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                                           @RequestPart("requiredInsertGwanBIA") InsertGwanBIAReqVO param)
    {
        log.debug("ComOrgGwaninfo insertGwanBIA" + param.toString());

        int res = 0;
        try {
            res = comOrgGwaninfoService.insertGwanBIA(multipartFiles, param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 관인관리 > 관인 수정 */
    @PutMapping(value = "/updateGwanBIA", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateGwanBIA(@RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                                           @RequestPart("requiredUpdateGwanBIA") UpdateGwanBIAReqVO param)
    {
        log.debug("comOrgGwaninfo updateGwanBIA" + param.toString());

        int res = 0;
        try {
            res = comOrgGwaninfoService.updateGwanBIA(multipartFiles, param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


}
