/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.login.controller;

import com.anylogic.ssystem.bms.login.model.*;
import com.anylogic.ssystem.bms.login.service.ShieldgateReqIdgpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.anylogic.ssystem.bms.login.service.ComUserinfoService;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"login"})
@RestController
@RequestMapping("/bms/login/comuserinfo")
public class ComUserinfoController {

    @Autowired
    private ComUserinfoService comUserinfoService;

    @Autowired
    private ShieldgateReqIdgpService shieldgateReqIdgpService;

    /**
     * selectOne
     * @param //GetUserLoginReqVO
     * return ResponseEntity
     */
    @GetMapping("/getUserLogin")
    public ResponseEntity getUserLogin
    (
        @Valid GetUserLoginReqVO param
    ) throws Exception 
    {
        log.debug("comUserinfo getUserLogin" + param.toString());
        return ResponseEntity.ok(comUserinfoService.getUserLogin(param));
    }

    @PutMapping("/updateAbsence")
    public ResponseEntity updateAbsence
    (
        @Valid @RequestBody UpdateAbsenceReqVO param

    ) throws Exception
    {
        int res = 0;
        try {
            res = comUserinfoService.updateAbsence(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    @PostMapping(value = "/clientCredential")
    public ResponseEntity clientCredential
    (
        @Valid @RequestBody ReqOauthDto param
    )
    {
        ResOauthDto res = null;
        try {
            res = shieldgateReqIdgpService.getOauthTokenByClientCredential();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(res);
    }


    @PostMapping(value = "/authorizationCode")
    public ResponseEntity authorizationCode
    (
        @Valid @RequestBody ReqOauthDto param
    )
    {
        ResOauthDto res = null;
        try {
            res = shieldgateReqIdgpService.getOauthTokenByCodeAndRedirectAndExtra(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(res);
    }


    @PostMapping(value = "/refreshToken")
    public ResponseEntity refreshToken
    (
        @Valid @RequestBody ReqOauthDto param
    )
    {
        ResOauthDto res = null;
        try {
            res = shieldgateReqIdgpService.getOauthTokenByRefreshToken(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(res);
    }


    @PostMapping(value = "/logout")
    public ResponseEntity logout
    (
        @Valid @RequestBody TokenLogoutDto param
    )
    {
        int res;
        try {
            res = shieldgateReqIdgpService.logOut(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(res);
    }
}
