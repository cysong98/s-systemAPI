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
@DisplayName("BmsDctMgmtRegiController_TEST")
@WebMvcTest(BmsDctMgmtRegiController.class)
public class BmsDctMgmtRegiControllerTest extends ControllerTest {
    private final static String TEST_SELECTCOMINGLIST_URL = "/bmsdctmgmtregi/use/bmsDctMgmtRegi/selectComingList";
    private final static String TEST_SELECTCOMINGRECLASSDETAIL_URL = "/bmsdctmgmtregi/use/bmsDctMgmtRegi/selectComingReclassDetail";
    private final static String TEST_SELECTCOMINGCNSRVDETAIL_URL = "/bmsdctmgmtregi/use/bmsDctMgmtRegi/selectComingCnsrvDetail";
    private final static String TEST_SELECTCOMINGDESTRUCTDETAIL_URL = "/bmsdctmgmtregi/use/bmsDctMgmtRegi/selectComingDestructDetail";
    private final static String TEST_SELECTCOMINGTRANSDETAIL_URL = "/bmsdctmgmtregi/use/bmsDctMgmtRegi/selectComingTransDetail";


    @Autowired
    private BmsDctMgmtRegiService bmsDctMgmtRegiService;

    @Test
    public int selectComingList() throws Exception {
        // given
        SelectComingListReqVO selectComingListReqVO = SelectComingListReqVO.builder()
            .mgmtno("")
            .regirecvdt("")
            .secttl("")
            .copyno("")
            .authorname("")
            .notiprotterm("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTCOMINGLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectComingReclassDetail() throws Exception {
        // given
        SelectComingReclassDetailReqVO selectComingReclassDetailReqVO = SelectComingReclassDetailReqVO.builder()
            .mgmtid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTCOMINGRECLASSDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectComingCnsrvDetail() throws Exception {
        // given
        SelectComingCnsrvDetailReqVO selectComingCnsrvDetailReqVO = SelectComingCnsrvDetailReqVO.builder()
            .mgmtid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTCOMINGCNSRVDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectComingDestructDetail() throws Exception {
        // given
        SelectComingDestructDetailReqVO selectComingDestructDetailReqVO = SelectComingDestructDetailReqVO.builder()
            .mgmtid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTCOMINGDESTRUCTDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectComingTransDetail() throws Exception {
        // given
        SelectComingTransDetailReqVO selectComingTransDetailReqVO = SelectComingTransDetailReqVO.builder()
            .mgmtid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTCOMINGTRANSDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
