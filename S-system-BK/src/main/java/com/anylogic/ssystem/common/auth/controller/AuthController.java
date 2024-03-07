/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.common.auth.controller;

import com.anylogic.ssystem.common.auth.dto.CommonResponse;
import com.anylogic.ssystem.common.auth.dto.RequestAuth;
import com.anylogic.ssystem.common.auth.dto.ResponseAuth;
import com.anylogic.ssystem.common.auth.jwt.JwtAuthenticateFilter;
import com.anylogic.ssystem.common.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    // 생성자주입
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin") // Account 인증 API
    public ResponseEntity<CommonResponse> authorize(@RequestBody RequestAuth.Login loginDto) {

        ResponseAuth.Token token = authService.signin(loginDto.getMemberId(), loginDto.getPassword());

        // response header 에도 넣고 응답 객체에도 넣는다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticateFilter.AUTHORIZATION_HEADER, "Bearer " + token.getAccessToken());

        // 응답
        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(token)
                .build();

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/signout") // Account 인증 API
    public ResponseEntity<CommonResponse> logout(@RequestBody RequestAuth.Logout logoutDto) {

        authService.signout(logoutDto.getMemberId());

        // 응답
        CommonResponse response = CommonResponse.builder()
                .success(true)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/token/refresh") // 리프레시 토큰을 활용한 액세스 토큰 갱신
    public ResponseEntity<CommonResponse> refreshToken(@RequestBody RequestAuth.Refresh refreshDto) {

        ResponseAuth.Token token = authService.refreshToken(refreshDto.getToken());

        // response header 에도 넣고 응답 객체에도 넣는다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticateFilter.AUTHORIZATION_HEADER, "Bearer " + token.getAccessToken());

        // 응답
        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(token)
                .build();

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    //리프레시토큰 만료 API
    //-> 해당 계정의 가중치를 1 올린다. 그럼 나중에 해당 리프레시 토큰으로 갱신 요청이 들어와도 받아들여지지 않음
    @DeleteMapping("/token/refresh/{username}")
    public ResponseEntity<CommonResponse> authorize(@PathVariable String username) {
        authService.invalidateRefreshTokenByUsername(username);
        // 응답
        CommonResponse response = CommonResponse.builder()
                .success(true)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
// 계정 탈퇴 구현 시 계정을 삭제하지 않고 비활성화 시켜야한다. field activated
}


