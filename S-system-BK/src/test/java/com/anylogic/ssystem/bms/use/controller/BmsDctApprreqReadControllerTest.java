/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.controller;

import com.anylogic.ssystem.common.ControllerTest;
import com.anylogic.ssystem.bms.use.service.*;
import com.anylogic.ssystem.bms.use.model.*;

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
@DisplayName("BmsDctApprreqReadController_TEST")
@WebMvcTest(BmsDctApprreqReadController.class)
public class BmsDctApprreqReadControllerTest extends ControllerTest {
    private final static String TEST_INSERTUSEDIGITALVIEWREQ_URL = "/bmsdctapprreqread/use/bmsDctApprreqRead/insertUseDigitalViewReq";
    private final static String TEST_INSERTUSENONDIGITALVIEWREQ_URL = "/bmsdctapprreqread/use/bmsDctApprreqRead/insertUseNonDigitalViewReq";


    @Autowired
    private BmsDctApprreqReadService bmsDctApprreqReadService;

    @Test
    public int insertUseDigitalViewReq() throws Exception {
        // given
        InsertUseDigitalViewReqReqVO insertUseDigitalViewReqReqVO = InsertUseDigitalViewReqReqVO.builder()
            .apprreqid("")
            .mgmtid("")
            .build();

        String content = objectMapper.writeValueAsString(insertUseDigitalViewReqReqVO);

        // when
        mvc.perform(get(TEST_INSERTUSEDIGITALVIEWREQ_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertUseNonDigitalViewReq() throws Exception {
        // given
        InsertUseNonDigitalViewReqReqVO insertUseNonDigitalViewReqReqVO = InsertUseNonDigitalViewReqReqVO.builder()
            .apprreqid("")
            .mgmtid("")
            .build();

        String content = objectMapper.writeValueAsString(insertUseNonDigitalViewReqReqVO);

        // when
        mvc.perform(get(TEST_INSERTUSENONDIGITALVIEWREQ_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
