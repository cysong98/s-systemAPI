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
import com.anylogic.ssystem.bms.com.service.ComMenuService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/commenu")
public class ComMenuController {

    @Autowired
    private ComMenuService comMenuService;

    /* LEFT MENU 조회 */
    @GetMapping("/selectLeftMenu")
    public ResponseEntity selectLeftMenu
    (
    ) throws Exception
    {
        return ResponseEntity.ok(comMenuService.selectLeftMenu());
    }


    /* 시스템관리 > 메뉴관리 > 메뉴 목록 조회 */
    @GetMapping("/selectMenuList")
    public ResponseEntity selectMenuList
    (
        @Valid SelectMenuListReqVO param
    ) throws Exception 
    {
        log.debug("comMenu selectMenuList" + param.toString());

        return ResponseEntity.ok(comMenuService.selectMenuList(param));
    }


    /* 시스템관리 > 메뉴관리 > 하위메뉴 목록 조회 */
    @GetMapping("/selectSubMenu")
    public ResponseEntity selectSubMenu
    (
            @Valid SelectSubMenuReqVO param
    ) throws Exception
    {
        log.debug("comMenu selectSubMenu" + param.toString());

        return ResponseEntity.ok(comMenuService.selectSubMenu(param));
    }


    /* 시스템관리 > 메뉴관리 > 메뉴 삭제 */
    @DeleteMapping(value = "/deleteMenuBIA")
    public ResponseEntity deleteMenuBIA
    (
        @Valid @RequestBody List<DeleteMenuBIAReqVO> params
    )
    {
        log.debug("ComMenu deleteMenuBIA" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = comMenuService.deleteMenuBIA(params);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 시스템관리 > 메뉴관리 > 메뉴 상세 조회 */
    @GetMapping("/getMenuDetailBIA")
    public ResponseEntity getMenuDetailBIA
    (
        @Valid GetMenuDetailBIAReqVO param
        
    ) throws Exception 
    {
        log.debug("comMenu getMenuDetailBIA" + param.toString());

        return ResponseEntity.ok(comMenuService.getMenuDetailBIA(param));
    }


    /* 시스템관리 > 메뉴관리 > 메뉴 등록 */
    @PostMapping(value = "/insertMenuBIA")
    public ResponseEntity insertMenuBIA
    (
        @Valid @RequestBody InsertMenuBIAReqVO param
    ) 
    {
        log.debug("comMenu insertMenuBIA" + param.toString());

        int res = 0;
        try {
            res = comMenuService.insertMenuBIA(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 시스템관리 > 메뉴관리 > 메뉴 수정 */
    @PutMapping(value = "/updateMenuBIA")
    public ResponseEntity updateMenuBIA
    (
        @Valid @RequestBody UpdateMenuBIAReqVO param
    ) 
    {
        log.debug("comMenu updateMenuBIA" + param.toString());

        int res = 0;
        try {
            res = comMenuService.updateMenuBIA(param);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 시스템관리 > 메뉴관리 > 메뉴 순서변경 */
    @PutMapping(value = "/updateMenuOrder")
    public ResponseEntity updateMenuOrder
    (
        @Valid @RequestBody List<UpdateMenuBIAReqVO> params
    )
    {
        log.debug("comMenu updateMenuOrder");

        List<Integer> res = new ArrayList<>();
        try {
            res = comMenuService.updateMenuOrder(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }
}
