/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.service;


import com.anylogic.ssystem.bms.com.mapper.ComUserinfoDetail2Mapper;
import com.anylogic.ssystem.bms.com.mapper.ComUserinfoDetailMapper;
import com.anylogic.ssystem.bms.com.mapper.ComVgroupUserMapper;
import com.anylogic.ssystem.bms.com.model.*;
import com.anylogic.ssystem.bms.login.model.ResOauthDto;
import com.anylogic.ssystem.bms.login.service.ShieldgateReqIdgpService;
import com.anylogic.ssystem.bms.login.service.ShieldgateValidateService;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.ComUserinfoMapper;
import com.anylogic.ssystem.bms.com.model.SelectUserListReqVO;
import com.anylogic.ssystem.bms.com.model.SelectUserListResVO;
import java.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.com.model.DeleteUserBIAReqVO;
import com.anylogic.ssystem.bms.com.model.DeleteUserVgroupReqVO;
import com.anylogic.ssystem.bms.com.model.GetUserDetailReqVO;
import com.anylogic.ssystem.bms.com.model.GetUserDetailResVO;
import com.anylogic.ssystem.bms.com.model.CheckUserIdReqVO;
import com.anylogic.ssystem.bms.com.model.CheckUserIdResVO;
import com.anylogic.ssystem.bms.com.model.InsertUserBIAReqVO;
import com.anylogic.ssystem.bms.com.model.InsertUserVgroupReqVO;
import com.anylogic.ssystem.bms.com.model.UpdateUserBIAReqVO;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@RequiredArgsConstructor
@Service("ComUserinfoService")
@Transactional
public class ComUserinfoServiceImpl implements ComUserinfoService {

    @Autowired
    private ComUserinfoMapper comUserinfoMapper;

    @Autowired
    private ComUserinfoDetailMapper comUserinfoDetailMapper;

    @Autowired
    private ComUserinfoDetail2Mapper comUserinfoDetail2Mapper;

    @Autowired
    private ComVgroupUserMapper comVgroupUserMapper;

    @Autowired
    private ShieldgateReqIdgpService shieldgateReqIdgpService;


    /* 사용 요청 시 (파일반출/존안/이관) 담당자 정보 조회 */
    @Override
    public List<SelectFindManagerResVO> selectFindManager(SelectFindManagerReqVO param){
        List<SelectFindManagerResVO> selectCnsrvUserList;
        try {
            selectCnsrvUserList = comUserinfoMapper.selectFindManager(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return selectCnsrvUserList;
    }


    /* 조직관리 > 부서/사용자관리 > 사용자 목록 조회 */
    @Override
    public PageInfo<SelectUserListResVO> selectUserList(SelectUserListReqVO param){
        PageInfo<SelectUserListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(comUserinfoMapper.selectUserList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 조직관리 > 부서/사용자관리 > 사용자 삭제 */
    @Override
    public List<Integer> deleteUserBIA(List<DeleteUserBIAReqVO> params){
        List<Integer> res = new ArrayList<Integer>();
        try {
            /* IdGP deleteUserInfo */
            ResOauthDto resOauthDto = shieldgateReqIdgpService.getOauthTokenByClientCredential();
            shieldgateReqIdgpService.deleteUserInfo(params, resOauthDto);

            for(DeleteUserBIAReqVO param : params) {
                int ares = comUserinfoMapper.deleteUserBIA(param);
                res.add(ares);
                comUserinfoDetailMapper.deleteUserDetail(DeleteUserDetailReqVO.builder()
                                                                .userid(param.getUserid())
                                                                .build());
                comUserinfoDetail2Mapper.deleteUserSecretgrade(DeleteUserDetailReqVO.builder()
                                                                .userid(param.getUserid())
                                                                .build());
                /* 사용자 권한 삭제 */
                comVgroupUserMapper.deleteUserVgroup(DeleteUserVgroupReqVO.builder()
                                                                .userid(param.getUserid())
                                                                .build());
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* 조직관리 > 부서/사용자관리 > 사용자 상세 조회 */
    @Override
    public List<GetUserDetailResVO> getUserDetail(GetUserDetailReqVO param){
        List<GetUserDetailResVO> resGetUserDetail;
        try {
            resGetUserDetail = comUserinfoMapper.getUserDetail(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resGetUserDetail;
    }


    /* 조직관리 > 부서/사용자관리 > 사용자 등록 > 아이디 중복확인 */
    @Override
    public CheckUserIdResVO checkUserId(CheckUserIdReqVO param){
        CheckUserIdResVO resCheckUserId;
        try {
            resCheckUserId = comUserinfoMapper.checkUserId(param);

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resCheckUserId == null){
            resCheckUserId = new CheckUserIdResVO();
        }
        return resCheckUserId;
    }


    /* 조직관리 > 부서/사용자관리 > 사용자 등록 */
    @Override
    public int insertUserBIA(InsertUserBIAReqVO param){
        int resInsertUserBIA = 0;
        try {
            /* com_userinfo */
            resInsertUserBIA = comUserinfoMapper.insertUserBIA(param);
            InsertUserDetailReqVO vo = InsertUserDetailReqVO.builder()
                                            .userid(param.getUserid())
                                            .secretgrade(param.getSecretgrade())
                                            .email(param.getEmail())
                                            .mobilephone(param.getMobilephone())
                                            .inid(param.getInid())
                                            .indt(param.getIndt())
                                            .build();
            comUserinfoDetailMapper.insertUserDetail(vo);
            comUserinfoDetail2Mapper.insertUserSecretgrade(vo);

            /* com_vgroup_user */
            for(SelectUserVgroupResVO selectUserVgroupResVO : param.getVgrouplist()) {
                comVgroupUserMapper.insertUserVgroup(InsertUserVgroupReqVO.builder()
                                                            .userid(param.getUserid())
                                                            .vgroupid(selectUserVgroupResVO.getVgroupid())
                                                            .orgid(param.getDeptid())
                                                            .vgrouptype("a")
                                                            .indt(param.getIndt())
                                                            .build());
            }

            /* IdGP api - insertUserIdGP */
            ResOauthDto resOauthDto = shieldgateReqIdgpService.getOauthTokenByClientCredential();
            shieldgateReqIdgpService.addUserInfo(param, resOauthDto);
            shieldgateReqIdgpService.updateGroupInfo(UpdateUserBIAReqVO.builder()
                                                            .userid(param.getUserid())
                                                            .deptname(param.getDeptname())
                                                            .build(), resOauthDto);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertUserBIA;
    }


    /* 조직관리 > 부서/사용자관리 > 사용자 수정 */
    @Override
    public int updateUserBIA(UpdateUserBIAReqVO param){
        int resUpdateUserBIA = 0;
        try {
            /* IdGP updateUserInfo */
            ResOauthDto resOauthDto = shieldgateReqIdgpService.getOauthTokenByClientCredential();
            shieldgateReqIdgpService.updateUserInfo(param, resOauthDto);
            shieldgateReqIdgpService.updateGroupInfo(UpdateUserBIAReqVO.builder()
                                                        .userid(param.getUserid())
                                                        .deptname(param.getDeptname())
                                                        .build(), resOauthDto);
            if(param.isPwChanged()) {
                GetUserDetailResVO vo = comUserinfoMapper.getUserDetail(GetUserDetailReqVO.builder()
                                                                            .userid(param.getUserid())
                                                                            .build())
                                                                            .get(0);
                shieldgateReqIdgpService.updateUserPassword(vo, param, resOauthDto);
            }

            /* com_userinfo */
            resUpdateUserBIA = comUserinfoMapper.updateUserBIA(param);
            UpdateUserDetailReqVO vo = UpdateUserDetailReqVO.builder()
                                            .userid(param.getUserid())
                                            .secretgrade(param.getSecretgrade())
                                            .email(param.getEmail())
                                            .mobilephone(param.getMobilephone())
                                            .inid(param.getInid())
                                            .indt(param.getIndt())
                                            .upid(param.getUpid())
                                            .updt(param.getUpdt())
                                            .build();
            comUserinfoDetailMapper.updateUserDetail(vo);
            comUserinfoDetail2Mapper.updateUserSecretgrade(vo);
            /* com_vgroup_user */
            if(param.getVgrouplist() != null) {
                if(param.getVgrouplist().isEmpty()) {
                    comVgroupUserMapper.deleteUserVgroup(DeleteUserVgroupReqVO.builder()
                                                                .userid(param.getUserid())
                                                                .build());
                }
                else {
                    comVgroupUserMapper.deleteUserVgroup(DeleteUserVgroupReqVO.builder()
                                                                .userid(param.getUserid())
                                                                .build());
                    for (SelectUserVgroupResVO selectUserVgroupResVO : param.getVgrouplist()) {
                        comVgroupUserMapper.insertUserVgroup(InsertUserVgroupReqVO.builder()
                                                                .vgroupid(selectUserVgroupResVO.getVgroupid())
                                                                .userid(param.getUserid())
                                                                .orgid(param.getDeptid())
                                                                .vgrouptype("a")
                                                                .indt(param.getIndt())
                                                                .build());
                    }
                }
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateUserBIA;
    }
}
