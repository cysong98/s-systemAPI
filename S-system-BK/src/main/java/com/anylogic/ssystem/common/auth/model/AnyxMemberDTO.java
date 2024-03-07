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
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnyxMemberDTO {
    private Long anyxMemberSeq;
    private String memberId;
    private String memberName;
    private String memberPassword;
    private String phoneNumber;
    private Date registerDate;
    private Date lastLoginTimestamp;
    private Boolean inactiveYn;
    private Boolean delYn;
//    private String customerId;
//    private String customerName;
//    private Long xscanCustomerSeq;
    private Set<AnyxMemberRoleDTO> roleDTOSet;
    private Long tokenWeight;

    public Collection<? extends GrantedAuthority> toAuthoritirs (Set<AnyxMemberRoleDTO> authority) {
//        Set<String> auths = new HashSet<>();
//        auths.add(authority);
        return authority.stream()
                .map((AnyxMemberRoleDTO role) -> new SimpleGrantedAuthority("ROLE_" + role.getRoleId() + "." + "LEVEL_" + role.getLevel()))
                .collect(Collectors.toList());
    }
    @Builder
    public AnyxMemberDTO (String memberId, String memberName, String memberPassword, Set<AnyxMemberRoleDTO> roleDTOSet) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.roleDTOSet = roleDTOSet;
        this.tokenWeight = 1L;
    }
    public void increaseTokenWeight() {
        this.tokenWeight++;
    }
}

