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
@DisplayName("BmsDctApprreqPathController_TEST")
@WebMvcTest(BmsDctApprreqPathController.class)
public class BmsDctApprreqPathControllerTest extends ControllerTest {
    private final static String TEST_GETREQLISTTOTNUMCOUNT_URL = "/bmsdctapprreqpath/trn/bmsDctApprreqPath/getReqListTotNumCount";
    private final static String TEST_GETREQPROCINGTOTNUM_URL = "/bmsdctapprreqpath/trn/bmsDctApprreqPath/getReqProcingTotNum";
    private final static String TEST_GETPROCDOCLISTCOUNT_URL = "/bmsdctapprreqpath/trn/bmsDctApprreqPath/getProcDocListCount";


    @Autowired
    private BmsDctApprreqPathService bmsDctApprreqPathService;

    @Test
    public int getReqListTotNumCount() throws Exception {
        // given
        GetReqListTotNumCountReqVO getReqListTotNumCountReqVO = GetReqListTotNumCountReqVO.builder()
//            .rEQID("")
//            .aPPRREQID("")
//            .rEQSTATUS("")
            .build();

        // when
        mvc.perform(get(TEST_GETREQLISTTOTNUMCOUNT_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int getReqProcingTotNum() throws Exception {
        // given
        GetReqProcingTotNumReqVO getReqProcingTotNumReqVO = GetReqProcingTotNumReqVO.builder()
            .aPPRID("")
            .aPPRTYPE("")
            .aPPRSTATUS("")
            .build();

        // when
        mvc.perform(get(TEST_GETREQPROCINGTOTNUM_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int getProcDocListCount() throws Exception {
        // given
        GetProcDocListCountReqVO getProcDocListCountReqVO = GetProcDocListCountReqVO.builder()
//            .aPPRREQID("")
//            .rEQTYPE("")
//            .rEQSTATUS("")
            .build();

        // when
        mvc.perform(get(TEST_GETPROCDOCLISTCOUNT_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
