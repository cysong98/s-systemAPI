/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.controller;

import com.anylogic.ssystem.common.ControllerTest;
import com.anylogic.ssystem.bms.dct.service.*;
import com.anylogic.ssystem.bms.dct.model.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@DisplayName("ComUserinfoController_TEST")
@WebMvcTest(ComUserinfoController.class)
public class ComUserinfoControllerTest extends ControllerTest {
    private final static String TEST_SELECTUSERINFO_URL = "/comuserinfo/dct/comUserinfo/selectUserInfo";


    @Autowired
    private ComUserinfoService comUserinfoService;

    @Test
    public int selectUserInfo() throws Exception {
        // given
        SelectUserInfoReqVO selectUserInfoReqVO = SelectUserInfoReqVO.builder()
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSERINFO_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
