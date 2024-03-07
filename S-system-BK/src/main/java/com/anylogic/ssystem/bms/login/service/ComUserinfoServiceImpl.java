/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.login.service;


import com.anylogic.ssystem.bms.login.model.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.login.mapper.ComUserinfoMapper;

import java.util.*;



@Slf4j
@RequiredArgsConstructor
@Service("ComUserinfoService")
@Transactional
public class ComUserinfoServiceImpl implements ComUserinfoService {

    @Autowired
    private ComUserinfoMapper comUserinfoMapper;

    @Autowired
    private ShieldgateReqIdgpService shieldgateReqIdgpService;

    @Autowired
    private ShieldgateValidateService shieldgateValidateService;

    @Override
    public GetUserLoginResVO getUserLogin(GetUserLoginReqVO param) {

        GetUserLoginResVO resGetUserLogin = null;
        try {
//            ResOauthDto resOauthDto = shieldgateReqIdgpService.getOauthTokenByClientCredential();
//            List<ResOauthPublicKeyDto> publicKeyList = shieldgateReqIdgpService.getCompanyPublicKeyAllList(resOauthDto.getAccess_token());
//            for(ResOauthPublicKeyDto publicKey : publicKeyList) {
//                shieldgateValidateService.setPublicKey(publicKey.getCompanyId(), publicKey.getCompanyRSAPubKey());
//            }
//            boolean validatedToken = true;
//            if(param.getJwt() != null)
//                validatedToken = shieldgateValidateService.validatedToken(param.getJwt());
//            if(validatedToken) {
                resGetUserLogin = comUserinfoMapper.getUserLogin(GetUserLoginReqVO.builder()
                                                                    .userid(param.getUserid())
                                                                    .build());
//            }
//            else {
//                resGetUserLogin = new GetUserLoginResVO();
//            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resGetUserLogin;
    }

    // 부재 등록&해제
    @Override
    public int updateAbsence(UpdateAbsenceReqVO param) {
        int res = 0;
        try {
            res = comUserinfoMapper.updateAbsence(param);
        } catch (Exception e) {
            throw new AnyXException(e);
        }
        return res;
    }

}
