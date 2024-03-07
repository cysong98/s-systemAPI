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


import com.anylogic.ssystem.common.auth.jwt.TokenProvider;
import com.anylogic.ssystem.common.auth.jwt.handler.JwtAccessDeniedHandler;
import com.anylogic.ssystem.common.auth.jwt.handler.JwtAuthenticationEntryPoint;
import com.anylogic.ssystem.common.auth.jwt.AnyxRoleApplicationchecker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;

// 추가적인 시큐리티 설정을 위해 WebSecurityConfigurer를 implements 하거나 WebSecurityConfigurerAdapter를 extends하는 방법이 있는데
// 여기서는 WebSecurityConfigurerAdapter를 extends 하는 방법 사용
@Configuration
@EnableWebSecurity // 기본적인 웹보안을 활성화하겠다
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션 사용을 위해 선언
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final AnyxRoleApplicationchecker anyxRoleApplicationchecker;
//    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 생성자 통해 스프링 빈 주입받는다.
    public SecurityConfig(
            TokenProvider tokenProvider,
//            CorsFilter corsFilter,
            AnyxRoleApplicationchecker anyxRoleApplicationchecker,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.anyxRoleApplicationchecker = anyxRoleApplicationchecker;
//        this.corsFilter = corsFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    // BCryptPasswordEncoder 라는 패스워드 인코더 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/h2/**"
                        ,"/favicon.ico"
                        ,"/error"
                );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        if (tokenProvider.isActive()) {
             httpSecurity
                    .httpBasic().disable()
                    .cors()
                    .and()
                    // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                    .csrf().disable()

//                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                    .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 우리가 만든 클래스로 인증 실패 핸들링
                    .accessDeniedHandler(jwtAccessDeniedHandler) // 커스텀 인가 실패 핸들링

                    // enable h2-console // embedded h2를 위한 설정
//                .and()
//                .headers()
//                .frameOptions()
//                .sameOrigin()

                    // 세션을 사용하지 않기 때문에 STATELESS로 설정
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                    // spring rest docs 경로
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("/docs/*").permitAll() // 문서 접근 권한을 설정합니다.

                    // api 경로
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/auth/**").permitAll() // 로그인 경로
//                     .antMatchers("/api/auth/signin").permitAll() // 로그인 경로
//                     .antMatchers("/api/auth/signout").permitAll() // 로그인 경로
//                    .antMatchers("/api/auth/token/refresh").permitAll() // 로그인 경로
                    .anyRequest().authenticated() // 나머지 경로는 jwt 인증 해야함
//                    .authorizeRequests()
//                    .antMatchers("/api/hello").permitAll() // /api/hello
//                    .antMatchers("/api/user").hasRole("ADMIN") // /api/hello
//                    .antMatchers("/api/hello").permitAll() // /api/hello
//                    .antMatchers("/api/user/signup").permitAll() // 회원가입 경로는 인증없이 호출 가능
//                    .antMatchers("/api/*").permitAll() // 토큰 갱신 API 도 인증 없이 호출

                    .and()
                    .apply(new AnyxJwtSecurityConfig(tokenProvider, anyxRoleApplicationchecker)); // JwtSecurityConfig 적용

        } else {
            httpSecurity
                    // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                    .csrf().disable()

                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                    // spring rest docs 경로
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("/docs/*").permitAll() // 문서 접근 권한을 설정합니다.
//
//                    // api 경로
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("*").permitAll() // /api/hello
                    ;
        }
    }
}


