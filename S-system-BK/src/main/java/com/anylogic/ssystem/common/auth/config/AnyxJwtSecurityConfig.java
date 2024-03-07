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

import com.anylogic.ssystem.common.auth.jwt.AnyxRoleApplicationchecker;
import com.anylogic.ssystem.common.auth.jwt.JwtAuthenticateFilter;
import com.anylogic.ssystem.common.auth.jwt.JwtExceptionFilter;
import com.anylogic.ssystem.common.auth.jwt.TokenProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AnyxJwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;
    private AnyxRoleApplicationchecker anyxRoleApplicationchecker;
    public AnyxJwtSecurityConfig(TokenProvider tokenProvider, AnyxRoleApplicationchecker anyxRoleApplicationchecker) {
        this.tokenProvider = tokenProvider;
        this.anyxRoleApplicationchecker = anyxRoleApplicationchecker;
    }

    @Override
    public void configure(HttpSecurity http) {
        if (tokenProvider.isActive()) {
            JwtAuthenticateFilter jwtAuthenticateFilter = new JwtAuthenticateFilter(tokenProvider, anyxRoleApplicationchecker);
            JwtExceptionFilter jwtExceptionFilter = new JwtExceptionFilter();
            http.addFilterBefore(jwtAuthenticateFilter, UsernamePasswordAuthenticationFilter.class);
            http.addFilterBefore(jwtExceptionFilter, JwtAuthenticateFilter.class);                                                                                                                                                           
        }
    }
}

