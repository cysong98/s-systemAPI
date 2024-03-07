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

import com.anylogic.ssystem.bms.com.mapper.ComVgroupUserMapper;
import com.anylogic.ssystem.bms.com.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.anylogic.ssystem.bms.com.service.ComUserinfoService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"com"})
@RestController
@RequestMapping("/bms/com/comuserinfo")
public class ComUserinfoController {

    @Autowired
    private ComUserinfoService comUserinfoService;

    @Autowired
    private ComVgroupUserMapper comVgroupUserMapper;

    /* 사용 요청 시 (파일반출/존안/이관) 담당자 정보 조회 */
    @GetMapping("/selectFindManager")
    public ResponseEntity selectFindManager
    (
            @Valid SelectFindManagerReqVO param
    ) throws Exception
    {
        log.debug("comUserinfo selectFindManager" + param.toString());

        return ResponseEntity.ok(comUserinfoService.selectFindManager(param));
    }


    /* 조직관리 > 부서/사용자관리 > 사용자 목록 조회 */
    @GetMapping("/selectUserList")
    public ResponseEntity selectUserList
    (
        @Valid SelectUserListReqVO param
    ) throws Exception 
    {
        log.debug("comUserinfo selectUserList" + param.toString());

        return ResponseEntity.ok(comUserinfoService.selectUserList(param));
    }


    /* 조직관리 > 부서/사용자관리 > 사용자 삭제 */
    @DeleteMapping(value = "/deleteUserBIA")
    public ResponseEntity deleteUserBIA
    (
        @Valid @RequestBody List<DeleteUserBIAReqVO> params
    )
    {
        log.debug("ComUserinfo deleteUserBIA" + params.toString());

        List<Integer> res = new ArrayList<>();
        try {
            res = comUserinfoService.deleteUserBIA(params);
        } catch (Exception e) { 
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 조직관리 > 부서/사용자관리 > 사용자 상세 조회 */
    @GetMapping("/getUserDetail")
    public ResponseEntity getUserDetail
    (
        @Valid GetUserDetailReqVO param
        
    ) throws Exception 
    {
        log.debug("comUserinfo getUserDetail" + param.toString());

        return ResponseEntity.ok(comUserinfoService.getUserDetail(param));
    }


    /* 조직관리 > 부서/사용자관리 > 사용자 등록 > 아이디 중복확인 */
    @GetMapping("/checkUserId")
    public ResponseEntity checkUserId
    (
        @Valid CheckUserIdReqVO param

    ) throws Exception
    {
        log.debug("comUserinfo checkUserId" + param.toString());

        return ResponseEntity.ok(comUserinfoService.checkUserId(param));
    }


    /* 조직관리 > 부서/사용자관리 > 사용자 등록 */
    @PostMapping(value = "/insertUserBIA")
    public ResponseEntity insertUserBIA
    (
        @Valid @RequestBody InsertUserBIAReqVO param
    )
    {
        int res = 0;
        List<CheckInsertUserVgroup> resVO = new ArrayList<>();
        try {
            // 권한종류 체크
            for(SelectUserVgroupResVO vo : param.getVgrouplist()) {
                List<SelectVgroupUserResVO> list = comVgroupUserMapper.selectVgroupUser(SelectVgroupUserReqVO.builder()
                                                                                            .vgroupid(vo.getVgroupid())
                                                                                            .build());
                if(!list.isEmpty()) {
                    String vgroupid = vo.getVgroupid();
                    for(SelectVgroupUserResVO element : list) {
                        /*
                         *  파일 반출 담당자, 이관관리부서장, 존안관리부서장, 문서과 비밀관리자
                         *  조직 전체를 통틀어 한 명만 존재해야하는 권한들
                         */
                        if(vgroupid.equals("6010") || vgroupid.equals("8255") || vgroupid.equals("8015") || vgroupid.equals("5210")) {
                            GetUserDetailResVO userVO = comUserinfoService.getUserDetail(GetUserDetailReqVO.builder()
                                                                                        .userid(element.getUserid())
                                                                                        .build())
                                                                                        .get(0);
                            resVO.add(CheckInsertUserVgroup.builder()
                                            .vgroupid(vo.getVgroupid())
                                            .vgroupname(vo.getVgroupname())
                                            .userid(userVO.getUserid())
                                            .deptname(userVO.getDeptname())
                                            .build());
                        }
                        /*
                         *  처리과 비밀관리자,
                         *  부서 내에서 한 명만 존재해야하는 권한들
                         */
                        else if(vgroupid.equals("5220")) {
                            if(param.getDeptid().equals(element.getOrgid())) {
                                GetUserDetailResVO userVO = comUserinfoService.getUserDetail(GetUserDetailReqVO.builder()
                                                                                            .userid(element.getUserid())
                                                                                            .build())
                                                                                            .get(0);
                                resVO.add(CheckInsertUserVgroup.builder()
                                            .vgroupid(vo.getVgroupid())
                                            .vgroupname(vo.getVgroupname())
                                            .userid(userVO.getUserid())
                                            .deptname(userVO.getDeptname())
                                            .build());
                            }
                        }
                    }
                }
            }

            if(resVO.isEmpty())
                res = comUserinfoService.insertUserBIA(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(resVO.isEmpty())
            return ResponseEntity.ok(res);
        else
            return ResponseEntity.ok(resVO);
    }


    /* 조직관리 > 부서/사용자관리 > 사용자 수정 */
    @PutMapping(value = "/updateUserBIA")
    public ResponseEntity updateUserBIA
    (
        @Valid @RequestBody UpdateUserBIAReqVO param
    ) 
    {
        int res = 0;
        List<CheckInsertUserVgroup> resVO = new ArrayList<>();
        try {
            // 권한종류 체크
            for(SelectUserVgroupResVO vo : param.getVgrouplist()) {
                List<SelectVgroupUserResVO> list = comVgroupUserMapper.selectVgroupUser(SelectVgroupUserReqVO.builder()
                                                                                            .vgroupid(vo.getVgroupid())
                                                                                            .build());
                if(!list.isEmpty()) {
                    String vgroupid = vo.getVgroupid();
                    for(SelectVgroupUserResVO element : list) {
                        /*
                         *  파일 반출 담당자, 이관관리부서장, 존안관리부서장, 문서과 비밀관리자
                         *  조직 전체를 통틀어 한 명만 존재해야하는 권한들
                         */
                        if(vgroupid.equals("6010") || vgroupid.equals("8255") || vgroupid.equals("8015") || vgroupid.equals("5210")) {
                            GetUserDetailResVO userVO = comUserinfoService.getUserDetail(GetUserDetailReqVO.builder()
                                                                                        .userid(element.getUserid())
                                                                                        .build())
                                                                                        .get(0);
                            if(!userVO.getUserid().equals(param.getUserid())) {
                                resVO.add(CheckInsertUserVgroup.builder()
                                                .vgroupid(vo.getVgroupid())
                                                .vgroupname(vo.getVgroupname())
                                                .userid(userVO.getUserid())
                                                .deptname(userVO.getDeptname())
                                                .build());
                            }
                        }
                        /*
                         *  처리과 비밀관리자,
                         *  부서 내에서 한 명만 존재해야하는 권한들
                         */
                        else if(vgroupid.equals("5220")) {
                            if(param.getDeptid().equals(element.getOrgid())) {
                                GetUserDetailResVO userVO = comUserinfoService.getUserDetail(GetUserDetailReqVO.builder()
                                                                                            .userid(element.getUserid())
                                                                                            .build())
                                                                                            .get(0);
                                if(!userVO.getUserid().equals(param.getUserid())) {
                                    resVO.add(CheckInsertUserVgroup.builder()
                                                    .vgroupid(vo.getVgroupid())
                                                    .vgroupname(vo.getVgroupname())
                                                    .userid(userVO.getUserid())
                                                    .deptname(userVO.getDeptname())
                                                    .build());
                                }
                            }
                        }
                    }
                }
            }

            if(resVO.isEmpty())
                res = comUserinfoService.updateUserBIA(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(resVO.isEmpty())
            return ResponseEntity.ok(res);
        else
            return ResponseEntity.ok(resVO);
    }

}
