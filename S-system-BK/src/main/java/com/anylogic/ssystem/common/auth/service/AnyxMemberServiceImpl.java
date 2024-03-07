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

import com.anylogic.ssystem.common.auth.exception.error.NoneAuthorizedMemberException;
import com.anylogic.ssystem.common.auth.mapper.AnyxMemberMapper;
import com.anylogic.ssystem.common.auth.model.AnyxMemberApplicationDTO;
import com.anylogic.ssystem.common.auth.model.AnyxMemberDTO;
import com.anylogic.ssystem.common.auth.model.AnyxMemberRoleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@Transactional
public class AnyxMemberServiceImpl implements AnyxMemberService{
    @Autowired
    private AnyxMemberMapper anyxMemberMapper;

    public AnyxMemberServiceImpl(AnyxMemberMapper anyxMemberMapper) {
        this.anyxMemberMapper = anyxMemberMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public AnyxMemberDTO getMemberInfo(String memberId) {

        HashMap<String, Object> memberMap = anyxMemberMapper.getMemberById(memberId);
        if (memberMap != null && memberMap.size() > 0) {
            AnyxMemberDTO memberDTO = this.setMemberMaptoDTO(memberMap);
            memberDTO.setRoleDTOSet(selectMemberRoleInfo(memberId));
            return memberDTO;
        } else {
            return null;
        }
    }

    @Override
    public Set<AnyxMemberRoleDTO> selectMemberRoleInfo(String memberId) {
        List<HashMap<String, Object>> memberRoles = anyxMemberMapper.selectMemberRoleById(memberId);
        Set<AnyxMemberRoleDTO> anyxMemberRoleDTOSet = new HashSet<>();
        if (memberRoles.size() > 0) {
            for (HashMap<String, Object> memberRole : memberRoles) {
                AnyxMemberRoleDTO anyxMemberRoleDTO = new AnyxMemberRoleDTO();
                if (memberRole.containsKey("memberRoleSeq")) {
                    anyxMemberRoleDTO.setMemberRoleSeq(Long.parseLong(memberRole.get("memberRoleSeq").toString()));
                }
                if (memberRole.containsKey("memberId")) {
                    anyxMemberRoleDTO.setMemberSeq(Long.parseLong(memberRole.get("memberSeq").toString()));
                }
                if (memberRole.containsKey("anyxRoleSeq")) {
                    anyxMemberRoleDTO.setRoleSeq(Long.parseLong(memberRole.get("anyxRoleSeq").toString()));
                }
                if (memberRole.containsKey("roleId")) {
                    anyxMemberRoleDTO.setRoleId(memberRole.get("roleId").toString());
                }
                if (memberRole.containsKey("roleName")) {
                    anyxMemberRoleDTO.setRoleName(memberRole.get("roleName").toString());
                }
                if (memberRole.containsKey("level")) {
                    anyxMemberRoleDTO.setLevel(Integer.parseInt(memberRole.get("level").toString()));
                }
                anyxMemberRoleDTOSet.add(anyxMemberRoleDTO);
            }
        } else {
            throw new NoneAuthorizedMemberException();
        }
        return anyxMemberRoleDTOSet;
    }

    @Override
    public void updateIncreaseTokenWeightById(String memberId) {
        anyxMemberMapper.updateIncreaseTokenWeightById(memberId);
    }

    @Override
    public List<AnyxMemberDTO> selectMemberInfo() {
        List<HashMap<String, Object>> memberList = anyxMemberMapper.selectMember();
        List<AnyxMemberDTO> memberDTOList = new ArrayList<>();
        for (HashMap<String, Object> memberMap : memberList) {
            AnyxMemberDTO memberDTO = this.setMemberMaptoDTO(memberMap);
            memberDTO.setMemberPassword(null);
            memberDTOList.add(memberDTO);
        }
        return memberDTOList;
    }

    @Override
    public void updateSigninById(String memberId) {
        anyxMemberMapper.updateSigninById(memberId);
    }

    private AnyxMemberDTO setMemberMaptoDTO(HashMap<String, Object> memberMap) {
        AnyxMemberDTO memberDTO = new AnyxMemberDTO();
        if (memberMap.containsKey("registerDate")) {
            String register = memberMap.get("registerDate").toString();
            Date registerDate;
            try {
                registerDate = new SimpleDateFormat("yyyy-MM-dd").parse(register);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            memberDTO.setRegisterDate(registerDate);
        }
        if (memberMap.containsKey("lastLoginTimestamp")) {
            String lastLogin = memberMap.get("lastLoginTimestamp").toString();
            Date lastLoginTimestamp;
            try {
                lastLoginTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastLogin);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            memberDTO.setLastLoginTimestamp(lastLoginTimestamp);
        }
        if (memberMap.containsKey("memberId")) {
            memberDTO.setMemberId(memberMap.get("memberId").toString());
        }
        if (memberMap.containsKey("memberName")) {
            memberDTO.setMemberName(memberMap.get("memberName").toString());
        }
        if (memberMap.containsKey("inactiveYn")) {
            memberDTO.setInactiveYn(Boolean.parseBoolean(memberMap.get("inactiveYn").toString()));
        }
        if (memberMap.containsKey("delYn")) {
            memberDTO.setDelYn(Boolean.parseBoolean(memberMap.get("delYn").toString()));
        }
        if (memberMap.containsKey("memberPassword")) {
            memberDTO.setMemberPassword(memberMap.get("memberPassword").toString());
        }
        if (memberMap.containsKey("tokenWeight")) {
            memberDTO.setTokenWeight(Long.parseLong(memberMap.get("tokenWeight").toString()));
        }
        return memberDTO;
    }

    public HashMap<String, Object> getMemberRoleApplication (String uri, String memberId, List<String> roleIds) {
        return anyxMemberMapper.getMemberRoleApplicationById(uri, memberId, roleIds);
    }

    public List<AnyxMemberApplicationDTO> selectMemberRoleApplicationById (String memberId) {
        List<HashMap<String, Object>> memberApplications = anyxMemberMapper.selectMemberRoleApplicationById(memberId);
        List<AnyxMemberApplicationDTO> anyxMemberApplicationDTOList = new ArrayList<>();
        for (HashMap<String, Object> ma : memberApplications) {
            AnyxMemberApplicationDTO anyxMemberApplicationDTO = new AnyxMemberApplicationDTO();

            if (ma.containsKey("roleLevel")) {
                anyxMemberApplicationDTO.setRoleLevel(Integer.parseInt(ma.get("roleLevel").toString()));
            }
            if (ma.containsKey("roleId")) {
                anyxMemberApplicationDTO.setRoleId(ma.get("roleId").toString());
            }
            if (ma.containsKey("roleName")) {
                anyxMemberApplicationDTO.setRoleName(ma.get("roleName").toString());
            }
            if (ma.containsKey("readYn")) {
                anyxMemberApplicationDTO.setReadYn(Boolean.parseBoolean(ma.get("readYn").toString()));
            }
            if (ma.containsKey("createYn")) {
                anyxMemberApplicationDTO.setCreateYn(Boolean.parseBoolean(ma.get("createYn").toString()));
            }
            if (ma.containsKey("updateYn")) {
                anyxMemberApplicationDTO.setUpdateYn(Boolean.parseBoolean(ma.get("updateYn").toString()));
            }
            if (ma.containsKey("deleteYn")) {
                anyxMemberApplicationDTO.setDeleteYn(Boolean.parseBoolean(ma.get("deleteYn").toString()));
            }
            if (ma.containsKey("anyxRoleApplicationSeq")) {
                anyxMemberApplicationDTO.setAnyxRoleApplicationSeq(Long.parseLong(ma.get("anyxRoleApplicationSeq").toString()));
            }
            if (ma.containsKey("applicationName")) {
                anyxMemberApplicationDTO.setApplicationName(ma.get("applicationName").toString());
            }
            if (ma.containsKey("applicationUri")) {
                anyxMemberApplicationDTO.setApplicationUri(ma.get("applicationUri").toString());
            }
            anyxMemberApplicationDTOList.add(anyxMemberApplicationDTO);
        }
        return anyxMemberApplicationDTOList;
    }
}

