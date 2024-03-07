/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.common.auth.jwt;


import com.anylogic.ssystem.common.auth.exception.error.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class JwtAuthenticateFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;
    private AnyxRoleApplicationchecker anyxRoleApplicationchecker;

    public JwtAuthenticateFilter(TokenProvider tokenProvider, AnyxRoleApplicationchecker anyxRoleApplicationchecker) {
        this.tokenProvider = tokenProvider;
        this.anyxRoleApplicationchecker = anyxRoleApplicationchecker;
    }

    // 실제 필터링 로직은 doFilter 안에 들어가게 된다. GenericFilterBean을 받아 구현
    // Dofilter는 토큰의 인증정보를 SecurityContext 안에 저장하는 역할 수행
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        if (requestTokenHeader == null) {
            // JWT Authorize HEAD가 없는 경우
        } else {
            // JWT 점검 필요
            String jwt = resolveToken(httpServletRequest);
            String requestURI = httpServletRequest.getRequestURI();

            // 유효성 검증
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // 토큰에서 유저네임, 권한을 뽑아 스프링 시큐리티 유저를 만들어 Authentication 반환
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                // 해당 스프링 시큐리티 유저를 시큐리티 건텍스트에 저장, 즉 디비를 거치지 않음
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
                anyxRoleApplicationchecker.check(requestURI, authentication);
            } else {
                log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
                throw new InvalidTokenException();
            }

        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // 헤더에서 토큰 정보를 꺼내온다.
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

