/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.trn.controller;

import com.anylogic.ssystem.common.ControllerTest;
import com.anylogic.ssystem.bms.trn.service.*;
import com.anylogic.ssystem.bms.trn.model.*;

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
@DisplayName("BmsStatUserController_TEST")
@WebMvcTest(BmsStatUserController.class)
public class BmsStatUserControllerTest extends ControllerTest {
    private final static String TEST_SELECTSTATUSERSTATDC4COUNT_URL = "/bmsstatuser/trn/bmsStatUser/selectStatUserStatdc4Count";


    @Autowired
    private BmsStatUserService bmsStatUserService;

    @Test
    public int selectStatUserStatdc4Count() throws Exception {
        // given
        SelectStatUserStatdc4CountReqVO selectStatUserStatdc4CountReqVO = SelectStatUserStatdc4CountReqVO.builder()
            .statdc4("")
            .docid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTSTATUSERSTATDC4COUNT_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
