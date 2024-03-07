/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.common.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface AnyxMemberMapper {
    HashMap<String, Object> getMemberById(@Param("memberId") String memberId);
    List<HashMap<String, Object>> selectMemberRoleById(@Param("memberId") String memberId);

    void updateSigninById(@Param("memberId") String memberId);
    void updateIncreaseTokenWeightById(@Param("memberId") String memberId);
    void updateLogoutById(@Param("memberId") String memberId);

    List<HashMap<String, Object>> selectMember();
    HashMap<String, Object> getMemberRoleApplicationById(@Param("applicationUri") String applicationUri, @Param("memberId") String memberId, @Param("roleIds") List<String> roleIds);
    List<HashMap<String, Object>> selectMemberRoleApplicationById(@Param("memberId") String memberId);

}

