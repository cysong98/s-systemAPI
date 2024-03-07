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
import com.anylogic.ssystem.bms.com.model.SelectUserVgroupReqVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.com.service.ComVgroupService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/comvgroup")
public class ComVgroupController {

    @Autowired
    private ComVgroupService comVgroupService;


    /* 조직관리 > 부서/사용자관리 > 권한 검색 팝업 > 권한 목록 조회 */
    @GetMapping("/selectUserVgroup")
    public ResponseEntity selectUserVgroup
    (
        @Valid SelectUserVgroupReqVO param
    ) throws Exception 
    {
        log.debug("comVgroup selectUserVgroup" + param.toString());

        return ResponseEntity.ok(comVgroupService.selectUserVgroup(param));
    }


}
