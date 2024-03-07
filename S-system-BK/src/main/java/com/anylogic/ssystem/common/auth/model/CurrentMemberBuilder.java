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


import java.util.Set;

public class CurrentMemberBuilder {
    private String memberId;
    private Long tokenWeight;
    private Set<AnyxMemberRoleDTO> roleDTOSet;

    public CurrentMemberBuilder setMemberId(String memberId) {
        this.memberId = memberId;
        return this;
    }

    public CurrentMemberBuilder setTokenWeight(Long tokenWeight) {
        this.tokenWeight = tokenWeight;
        return this;
    }

    public CurrentMemberBuilder setRoleDTOSet(Set<AnyxMemberRoleDTO> roleDTOSet) {
        this.roleDTOSet = roleDTOSet;
        return this;
    }

    public CurrentMember createCurrentMember() {
        return new CurrentMember(memberId, roleDTOSet, tokenWeight);
    }
}

