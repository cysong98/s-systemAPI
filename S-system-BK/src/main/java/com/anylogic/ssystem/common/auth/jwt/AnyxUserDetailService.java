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


import com.anylogic.ssystem.common.auth.model.CurrentMember;
import com.anylogic.ssystem.common.auth.model.AnyxMemberDTO;
import com.anylogic.ssystem.common.auth.service.AnyxMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AnyxUserDetailService implements UserDetailsService {
    @Autowired
    private AnyxMemberService anyxMemberService;
    @Autowired
    private CurrentMember currentMember;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AnyxMemberDTO anyxMemberDTO = anyxMemberService.getMemberInfo(username);
        if (anyxMemberDTO == null) throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        currentMember.setMemberId(anyxMemberDTO.getMemberId());
        currentMember.setTokenWeight(anyxMemberDTO.getTokenWeight());
        currentMember.setRoleDTOSet(anyxMemberDTO.getRoleDTOSet());
        return new User(anyxMemberDTO.getMemberId(), anyxMemberDTO.getMemberPassword(),
                CurrentMember.authorities(anyxMemberDTO.getRoleDTOSet())
        );
    }
}

