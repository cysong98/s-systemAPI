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
@DisplayName("BmsDctEnfDocController_TEST")
@WebMvcTest(BmsDctEnfDocController.class)
public class BmsDctEnfDocControllerTest extends ControllerTest {
    private final static String TEST_SELECTRETURNLIST_URL = "/bmsdctenfdoc/dct/bmsDctEnfDoc/selectReturnList";
    private final static String TEST_SELECTDCTDISTRIBUTELIST_URL = "/bmsdctenfdoc/dct/bmsDctEnfDoc/selectDctDistributeList";
    private final static String TEST_SELECTRECEIPTLIST_URL = "/bmsdctenfdoc/dct/bmsDctEnfDoc/selectReceiptList";
    private final static String TEST_SELECTRECVHISTORY_URL = "/bmsdctenfdoc/dct/bmsDctEnfDoc/selectRecvHistory";
    private final static String TEST_SELECTNOTASSIGNTASKLIST_URL = "/bmsdctenfdoc/dct/bmsDctEnfDoc/selectNotAssignTaskList";
    private final static String TEST_UPDATEENFDOCREPATH_URL = "/bmsdctenfdoc/dct/bmsDctEnfDoc/updateEnfDocRepath";
    private final static String TEST_SELECTREPROCESSLIST_URL = "/bmsdctenfdoc/dct/bmsDctEnfDoc/selectReprocessList";
    private final static String TEST_GETREPROCESS_URL = "/bmsdctenfdoc/dct/bmsDctEnfDoc/getReprocess";
    private final static String TEST_SELECTSENDBACKREADYLIST_URL = "/bmsdctenfdoc/dct/bmsDctEnfDoc/selectSendBackReadyList";


    @Autowired
    private BmsDctEnfDocService bmsDctEnfDocService;

    @Test
    public int selectReturnList() throws Exception {
        // given
        SelectReturnListReqVO selectReturnListReqVO = SelectReturnListReqVO.builder()
            .doctitle("")
            .recvdeptname("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTRETURNLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectDctDistributeList() throws Exception {
        // given
        SelectDctDistributeListReqVO selectDctDistributeListReqVO = SelectDctDistributeListReqVO.builder()
            .docid("")
            .docstatus("")
            .title("")
            .recvdt("")
            .docno("")
            .seclevel("")
            .doctitle("")
            .createdeptname("")
            .enfgubun("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTDCTDISTRIBUTELIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectReceiptList() throws Exception {
        // given
        SelectReceiptListReqVO selectReceiptListReqVO = SelectReceiptListReqVO.builder()
            .docid("")
            .title("")
            .recvdt("")
            .docno("")
            .seclevel("")
            .doctitle("")
            .createdeptname("")
            .enfgubun("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTRECEIPTLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectRecvHistory() throws Exception {
        // given
        SelectRecvHistoryReqVO selectRecvHistoryReqVO = SelectRecvHistoryReqVO.builder()
//            .docid("")
//            .aUTHORID("")
//            .dEPTID("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTRECVHISTORY_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectNotAssignTaskList() throws Exception {
        // given
        SelectNotAssignTaskListReqVO selectNotAssignTaskListReqVO = SelectNotAssignTaskListReqVO.builder()
            .docid("")
            .title("")
            .recvdt("")
            .docno("")
            .seclevel("")
            .doctitle("")
            .createdeptname("")
            .enfgubun("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTNOTASSIGNTASKLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateEnfDocRepath() throws Exception {
        // given
        UpdateEnfDocRepathReqVO updateEnfDocRepathReqVO = UpdateEnfDocRepathReqVO.builder()
            .enfdocid("")
            .build();

        String content = objectMapper.writeValueAsString(updateEnfDocRepathReqVO);

        // when
        mvc.perform(get(TEST_UPDATEENFDOCREPATH_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectReprocessList() throws Exception {
        // given
        SelectReprocessListReqVO selectReprocessListReqVO = SelectReprocessListReqVO.builder()
            .docid("")
            .title("")
            .acceptdt("")
            .acceptdeptname("")
            .authorname("")
            .enfgubun("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTREPROCESSLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int getReprocess() throws Exception {
        // given
        GetReprocessReqVO getReprocessReqVO = GetReprocessReqVO.builder()
            .docid("")
            .build();

        // when
        mvc.perform(get(TEST_GETREPROCESS_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectSendBackReadyList() throws Exception {
        // given
        SelectSendBackReadyListReqVO selectSendBackReadyListReqVO = SelectSendBackReadyListReqVO.builder()
            .docid("")
            .title("")
            .acceptdt("")
            .seclevel("")
            .acceptdeptname("")
            .enfgubun("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTSENDBACKREADYLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
