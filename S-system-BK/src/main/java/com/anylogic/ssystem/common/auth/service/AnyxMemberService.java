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


import com.anylogic.ssystem.common.auth.model.AnyxMemberApplicationDTO;
import com.anylogic.ssystem.common.auth.model.AnyxMemberDTO;
import com.anylogic.ssystem.common.auth.model.AnyxMemberRoleDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface AnyxMemberService {
    AnyxMemberDTO getMemberInfo(String memberId);
    Set<AnyxMemberRoleDTO> selectMemberRoleInfo(String memberId);
    void updateSigninById(String memberId);
    void updateIncreaseTokenWeightById(String memberId);

    List<AnyxMemberDTO> selectMemberInfo();
    HashMap<String, Object> getMemberRoleApplication (String uri, String memberId, List<String> roleIds);
    List<AnyxMemberApplicationDTO> selectMemberRoleApplicationById (String memberId);
}

