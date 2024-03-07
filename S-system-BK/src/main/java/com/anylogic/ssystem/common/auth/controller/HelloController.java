/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.common.auth.controller;


import com.anylogic.ssystem.common.auth.controller.dto.ResponseMember;
import com.anylogic.ssystem.common.auth.model.AnyxMemberDTO;
import com.anylogic.ssystem.common.auth.service.AnyxMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api")
public class HelloController {
    private final AnyxMemberService memberService;

    public HelloController(AnyxMemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    // redirect test
    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }

    // 인가 테스트
    // Authorization: Bearer {AccessToken}
    // @AuthenticationPrincipal를 통해 JwtFilter에서 토큰을 검증하며 등록한 시큐리티 유저 객체를 꺼내올 수 있다.
    // JwtFilter는 디비 조회를 하지 않기에 유저네임, 권한만 알 수 있음
    // Account 엔티티에 대한 정보를 알고 싶으면 당연 디비 조회를 별도로 해야함
    @GetMapping("/user")
    public ResponseEntity<List<AnyxMemberDTO>> getMyUserInfo() {
//        List<ResponseMember> responseMemberList = new ArrayList<>();
        List<AnyxMemberDTO> memberDTOList = memberService.selectMemberInfo();
//        memberDTOList.forEach(memberDTO -> responseMemberList.add(new ResponseMember(memberDTO)));
        return ResponseEntity.ok(memberDTOList);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ResponseMember> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(new ResponseMember(memberService.getMemberInfo(username)));
    }
}

