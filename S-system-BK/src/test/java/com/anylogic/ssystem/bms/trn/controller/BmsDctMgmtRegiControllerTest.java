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
@DisplayName("BmsDctMgmtRegiController_TEST")
@WebMvcTest(BmsDctMgmtRegiController.class)
public class BmsDctMgmtRegiControllerTest extends ControllerTest {
    private final static String TEST_SELECTMGMTREGISECSTATUSRECLASSCONTCOUNT_URL = "/bmsdctmgmtregi/trn/bmsDctMgmtRegi/selectMgmtRegiSecStatusReClasscontCount";
    private final static String TEST_SELECTSENDLISTCOUNT_URL = "/bmsdctmgmtregi/trn/bmsDctMgmtRegi/selectSendListCount";
    private final static String TEST_SELECTDCTNOTCOMPLETELIST_URL = "/bmsdctmgmtregi/trn/bmsDctMgmtRegi/selectDctNotCompleteList";
    private final static String TEST_SELECTDCTPRODMGMTCARDLIST_URL = "/bmsdctmgmtregi/trn/bmsDctMgmtRegi/selectDctProdMgmtCardList";
    private final static String TEST_SELECTRECLASSMGMGCARDLIST_URL = "/bmsdctmgmtregi/trn/bmsDctMgmtRegi/selectReClassMgmgCardList";


    @Autowired
    private BmsDctMgmtRegiService bmsDctMgmtRegiService;

    @Test
    public int selectMgmtRegiSecStatusReClasscontCount() throws Exception {
        // given
        SelectMgmtRegiSecStatusReClasscontCountReqVO selectMgmtRegiSecStatusReClasscontCountReqVO = SelectMgmtRegiSecStatusReClasscontCountReqVO.builder()
//            .sECSTATUS("")
//            .rECLASSCONT("")
//            .orgid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTMGMTREGISECSTATUSRECLASSCONTCOUNT_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectSendListCount() throws Exception {
        // given
        SelectSendListCountReqVO selectSendListCountReqVO = SelectSendListCountReqVO.builder()
            .docid("")
            .state("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTSENDLISTCOUNT_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectDctNotCompleteList() throws Exception {
        // given
        SelectDctNotCompleteListReqVO selectDctNotCompleteListReqVO = SelectDctNotCompleteListReqVO.builder()
            .sECSTATUS("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTDCTNOTCOMPLETELIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectDctProdMgmtCardList() throws Exception {
        // given
        SelectDctProdMgmtCardListReqVO selectDctProdMgmtCardListReqVO = SelectDctProdMgmtCardListReqVO.builder()
            .rEGIRECVGUBUN("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTDCTPRODMGMTCARDLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectReClassMgmgCardList() throws Exception {
        // given
        SelectReClassMgmgCardListReqVO selectReClassMgmgCardListReqVO = SelectReClassMgmgCardListReqVO.builder()
            .title("")
            .docid("")
            .secstatus("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTRECLASSMGMGCARDLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
