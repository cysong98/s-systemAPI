/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.common.auth.service;

import com.anylogic.ssystem.common.auth.dto.ResponseAuth;
import com.anylogic.ssystem.common.auth.exception.error.InvalidRefreshTokenException;
import com.anylogic.ssystem.common.auth.exception.error.InvalidSigninDataException;
import com.anylogic.ssystem.common.auth.jwt.AnyxUserDetailService;
import com.anylogic.ssystem.common.auth.jwt.RefreshTokenProvider;
import com.anylogic.ssystem.common.auth.jwt.TokenProvider;
import com.anylogic.ssystem.common.auth.model.CurrentMember;
import com.anylogic.ssystem.common.auth.model.AnyxMemberApplicationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RefreshTokenProvider refreshTokenProvider;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private AnyxMemberService anyxMemberService;
    @Autowired
    private AnyxUserDetailService anyxUserDetailService;
    @Autowired
    private CurrentMember currentMember;

    // username 과 패스워드로 사용자를 인증하여 액세스토큰과 리프레시 토큰을 반환한다.
    @Override
    public ResponseAuth.Token signin(String username, String password) {
        // 받아온 데이터가 정상적인지 확인
        if (username == null || password == null) {
            throw new InvalidSigninDataException();
        }

        // 받아온 유저네임과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
          UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // authenticationToken 객체를 통해 Authentication 객체 생성
        // -> 제외 : 이 과정에서 CustomUserDetailsService 에서 우리가 재정의한 loadUserByUsername 메서드 호출
        //
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 그 객체를 시큐리티 컨텍스트에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = tokenProvider.createToken(authentication);

        // 위에서 loadUserByUsername를 호출하였으므로 AccountAdapter가 시큐리티 컨텍스트에 저장되어 Account 엔티티 정보를 우리는 알 수 있음
        // 유저 정보에서 중치를 꺼내 리프레시 토큰 가중치에 할당, 나중에 액세스토큰 재발급 시도 시 유저정보 가중치 > 리프레시 토큰이라면 실패
        Long tokenWeight = currentMember.getTokenWeight();
        String refreshToken = refreshTokenProvider.createToken(authentication, tokenWeight);

        // 사용자 권한을 가진 응용프로그램 정보를 가져온다
        List<AnyxMemberApplicationDTO> anyxMemberApplicationDTOList = anyxMemberService.selectMemberRoleApplicationById (currentMember.getMemberId());

        // 사용자 로그인 정보를 기록한다.
        anyxMemberService.updateSigninById(username);

        return ResponseAuth.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberApplications(anyxMemberApplicationDTOList)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseAuth.Token refreshToken(String refreshToken) {
        // 먼저 리프레시 토큰을 검증한다.
        if(!refreshTokenProvider.validateToken(refreshToken)) throw new InvalidRefreshTokenException();

        // 리프레시 토큰 값을 이용해 사용자를 꺼낸다.
        // refreshTokenProvider과 TokenProvider는 다른 서명키를 가지고 있기에 refreshTokenProvider를 써야함
        Authentication authentication = refreshTokenProvider.getAuthentication(refreshToken);

        // User 정보 로드
        UserDetails user = anyxUserDetailService.loadUserByUsername(authentication.getName());

        // 사용자 디비 값에 있는 것과 가중치 비교, 디비 가중치가 더 크다면 유효하지 않음
        if(currentMember.getTokenWeight() > refreshTokenProvider.getTokenWeight(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        // 리프레시 토큰에 담긴 값을 그대로 액세스 토큰 생성에 활용한다.
        String accessToken = tokenProvider.createToken(authentication);

        // 사용자 권한을 가진 응용프로그램 정보를 가져온다
        List<AnyxMemberApplicationDTO> anyxMemberApplicationDTOList = anyxMemberService.selectMemberRoleApplicationById (currentMember.getMemberId());

        // 기존 리프레시 토큰과 새로 만든 액세스 토큰을 반환한다.
        return ResponseAuth.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberApplications(anyxMemberApplicationDTOList)
                .build();
    }

    // Account 가중치를 1 올림으로써 해당 username 리프레시토큰 무효화
    @Transactional
    @Override
    public void invalidateRefreshTokenByUsername(String username) {
        anyxMemberService.updateIncreaseTokenWeightById(username);
    }

    @Override
    public void signout(String username) {
        anyxMemberService.updateIncreaseTokenWeightById(username);
    }
}


