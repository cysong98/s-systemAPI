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

import com.anylogic.ssystem.common.auth.exception.error.NoneAuthorizedMemberException;
import com.anylogic.ssystem.common.auth.service.AnyxMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AnyxRoleApplicationchecker {
    private AnyxMemberService anyxMemberService;

    public AnyxRoleApplicationchecker(AnyxMemberService anyxMemberService) {
        this.anyxMemberService = anyxMemberService;
    }
    public void check(String uri, Authentication authentication) {
//        HashMap<String, Object> authRole = new HashMap<>();
        List<String> authRole = new ArrayList<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            List<String> ps = anyxParseRole(authority.getAuthority());
            List<String> psRole = ps.stream()
                    .filter(s->s.contains("ROLE"))
                    .map(s->s.replace("ROLE_",""))
                    .collect(Collectors.toList());
            authRole.addAll(psRole);
            List<String> psLevel = ps.stream()
                    .filter(s->s.contains("LEVEL"))
                    .collect(Collectors.toList());
        }
        User user = (User)authentication.getPrincipal();
        HashMap<String, Object> getRole = anyxMemberService.getMemberRoleApplication(uri, user.getUsername(), authRole);
        if (getRole == null) {
            throw new NoneAuthorizedMemberException();
        }
    }

    public List<String> anyxParseRole(String role) {
         return Arrays.asList(role.split("[.]"));
    }
}

