/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.common.auth.config;

import com.anylogic.ssystem.common.auth.jwt.RefreshTokenProvider;
import com.anylogic.ssystem.common.auth.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String accessTokenSecret;
    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenValidityInSeconds;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidityInSeconds;
    @Value("${jwt.active}")
    private Boolean active;

    // 액세스 토큰 발급용, 리프레시 토큰 발급용은 각각 별도의 키와 유효기간을 갖는다.
    @Bean(name = "tokenProvider")
    public TokenProvider tokenProvider() {
        return new TokenProvider(active, accessTokenSecret, accessTokenValidityInSeconds);
    }

    // 리프레시 토큰은 별도의 키를 가지기 떄문에 리프레시 토큰으로는 API 호출 불가
    // 액세스 토큰 재발급 시 검증용
    @Bean(name = "refreshTokenProvider")
    public RefreshTokenProvider refreshTokenProvider() {
        return new RefreshTokenProvider(active, refreshTokenSecret, refreshTokenValidityInSeconds);
    }
}

