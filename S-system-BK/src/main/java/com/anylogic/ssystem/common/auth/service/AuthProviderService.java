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


import com.anylogic.ssystem.common.auth.jwt.AnyxUserDetailService;
import com.anylogic.ssystem.common.auth.model.CurrentMember;
import com.anylogic.ssystem.common.auth.model.AnyxMemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthProviderService implements AuthenticationProvider {
    @Autowired
    private AnyxMemberService anyxMemberService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CurrentMember currentMember;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 회원정보 검색후에 비밀번호 확인
        String memberId = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

//        UserDetails userDetails = AnyxUserDetailService.loadUserByUsername(memberId);

        AnyxMemberDTO anyxMemberDTO = anyxMemberService.getMemberInfo(memberId);
        if (anyxMemberDTO == null) {
            throw new BadCredentialsException(memberId);
        }

        if (!passwordEncoder.matches(password, anyxMemberDTO.getMemberPassword())) {
            throw new BadCredentialsException(memberId);
        }
        //        if (!memberDTO.getMemberPassword().equals(password)) {
//            throw new BadCredentialsException(memberId);
//        }
        currentMember.setMemberId(anyxMemberDTO.getMemberId());
        currentMember.setTokenWeight(anyxMemberDTO.getTokenWeight());
        currentMember.setRoleDTOSet(anyxMemberDTO.getRoleDTOSet());
//        currentMember = CurrentMember.builder()
//                .memberId(memberDTO.getMemberId())
//                .roleDTOSet(memberDTO.getRoleDTOSet())
//                .tokenWeight(memberDTO.getTokenWeight())
//                .build();

        return new UsernamePasswordAuthenticationToken(memberId, password,
                anyxMemberDTO.toAuthoritirs(anyxMemberDTO.getRoleDTOSet())
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}

