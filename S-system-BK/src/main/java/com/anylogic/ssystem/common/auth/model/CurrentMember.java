/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.common.auth.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentMember {
    private String memberId;
    private Set<AnyxMemberRoleDTO> roleDTOSet;
    private Long tokenWeight;

    public void clear() {
        this.memberId = null;
        this.roleDTOSet = null;
        this.tokenWeight = null;
    }

    public static List<GrantedAuthority> authorities(Set<AnyxMemberRoleDTO> authorities) {
        return authorities.stream()
                .map(a ->
                    MessageFormat.format("ROLE_{0}.LEVEL_{1}", a.getRoleId(), a.getLevel())
                )
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}

