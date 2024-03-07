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
@DisplayName("BmsDctOpinionController_TEST")
@WebMvcTest(BmsDctOpinionController.class)
public class BmsDctOpinionControllerTest extends ControllerTest {
    private final static String TEST_INSERTDCTDISTRIBUTEBIA_URL = "/bmsdctopinion/dct/bmsDctOpinion/insertDctDistributeBIA";
    private final static String TEST_INSERTDCTRDOCBIA_URL = "/bmsdctopinion/dct/bmsDctOpinion/insertDctRdocBIA";


    @Autowired
    private BmsDctOpinionService bmsDctOpinionService;

    @Test
    public int insertDctDistributeBIA() throws Exception {
        // given
        InsertDctDistributeBIAReqVO insertDctDistributeBIAReqVO = InsertDctDistributeBIAReqVO.builder()
//            .docid("")
//            .pathorder("")
            .build();

        String content = objectMapper.writeValueAsString(insertDctDistributeBIAReqVO);

        // when
        mvc.perform(get(TEST_INSERTDCTDISTRIBUTEBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertDctRdocBIA() throws Exception {
        // given
        InsertDctRdocBIAReqVO insertDctRdocBIAReqVO = InsertDctRdocBIAReqVO.builder()
//            .docid("")
//            .pathorder("")
            .build();

        String content = objectMapper.writeValueAsString(insertDctRdocBIAReqVO);

        // when
        mvc.perform(get(TEST_INSERTDCTRDOCBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
