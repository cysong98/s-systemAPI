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
@DisplayName("BmsDctRdocController_TEST")
@WebMvcTest(BmsDctRdocController.class)
public class BmsDctRdocControllerTest extends ControllerTest {
    private final static String TEST__URL = "/bmsdctrdoc/dct/bmsDctRdoc/";
    private final static String TEST_SELECTAPPRWAITLIST_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectApprWaitList";
    private final static String TEST_SELECTSENDWAITLIST_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectSendWaitList";
    private final static String TEST_SELECTRECWAITLIST_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectRecWaitList";
    private final static String TEST_SELECTMYREADLIST_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectMyReadList";
    private final static String TEST_INSERTCARDBIA_URL = "/bmsdctrdoc/dct/bmsDctRdoc/insertCardBIA";
    private final static String TEST_SELECTTEMPLIST_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectTempList";
    private final static String TEST_GETDCTRDOC_URL = "/bmsdctrdoc/dct/bmsDctRdoc/getDctRdoc";
    private final static String TEST_SELECTREQCOMPLETELIST_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectReqCompleteList";
    private final static String TEST_SELECTWAITLIST_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectWaitList";
    private final static String TEST_GETREVIEWPRINT_URL = "/bmsdctrdoc/dct/bmsDctRdoc/getReviewPrint";
    private final static String TEST_UPDATECARDBIA1_URL = "/bmsdctrdoc/dct/bmsDctRdoc/updateCardBIA1";
    private final static String TEST_UPDATECARDBIA2_URL = "/bmsdctrdoc/dct/bmsDctRdoc/updateCardBIA2";
    private final static String TEST_SELECTINGLIST_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectIngList";
    private final static String TEST_SELECTAPPROVALCOMPLETELIST_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectApprovalCompleteList";
    private final static String TEST_UPDATECARDBIA_URL = "/bmsdctrdoc/dct/bmsDctRdoc/updateCardBIA";
    private final static String TEST_SELECTSENDCURRENTLIST_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectSendCurrentList";
    private final static String TEST_SELECTDISTRIBUTELIST_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectDistributeList";
    private final static String TEST_GETMANAGEVIEW_URL = "/bmsdctrdoc/dct/bmsDctRdoc/getManageView";
    private final static String TEST_GETRECVCONFIRM_URL = "/bmsdctrdoc/dct/bmsDctRdoc/getRecvConfirm";
    private final static String TEST_INSERTRECVCONFIRMBIA_URL = "/bmsdctrdoc/dct/bmsDctRdoc/insertRecvConfirmBIA";
    private final static String TEST_GETRDOC_URL = "/bmsdctrdoc/dct/bmsDctRdoc/getRdoc";
    private final static String TEST_SELECTTODOLISTAPPRREADY_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectToDoListApprReady";
    private final static String TEST_GETDCTRDOCPROC_URL = "/bmsdctrdoc/dct/bmsDctRdoc/getDctRdocProc";
    private final static String TEST_SELECTDCTRDOC_URL = "/bmsdctrdoc/dct/bmsDctRdoc/selectdctRdoc";


    @Autowired
    private BmsDctRdocService bmsDctRdocService;


    @Test
    public int selectApprWaitList() throws Exception {
        // given
        SelectApprWaitListReqVO selectApprWaitListReqVO = SelectApprWaitListReqVO.builder()
            .build();

        // when
        mvc.perform(get(TEST_SELECTAPPRWAITLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectSendWaitList() throws Exception {
        // given
        SelectSendWaitListReqVO selectSendWaitListReqVO = SelectSendWaitListReqVO.builder()
            .build();

        // when
        mvc.perform(get(TEST_SELECTSENDWAITLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectRecWaitList() throws Exception {
        // given
        SelectRecWaitListReqVO selectRecWaitListReqVO = SelectRecWaitListReqVO.builder()
            .build();

        // when
        mvc.perform(get(TEST_SELECTRECWAITLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectMyReadList() throws Exception {
        // given
        SelectMyReadListReqVO selectMyReadListReqVO = SelectMyReadListReqVO.builder()
            .build();

        // when
        mvc.perform(get(TEST_SELECTMYREADLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertCardBIA() throws Exception {
        // given
        InsertCardBIAReqVO insertCardBIAReqVO = InsertCardBIAReqVO.builder()
            .docid("")
            .build();

        String content = objectMapper.writeValueAsString(insertCardBIAReqVO);

        // when
        mvc.perform(get(TEST_INSERTCARDBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectTempList() throws Exception {
        // given
        SelectTempListReqVO selectTempListReqVO = SelectTempListReqVO.builder()
            .docttl("")
            .authorname("")
            .authordeptname("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTTEMPLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int getDctRdoc() throws Exception {
        // given
        GetDctRdocReqVO getDctRdocReqVO = GetDctRdocReqVO.builder()
            .docid("")
            .build();

        // when
        mvc.perform(get(TEST_GETDCTRDOC_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectReqCompleteList() throws Exception {
        // given
        SelectReqCompleteListReqVO selectReqCompleteListReqVO = SelectReqCompleteListReqVO.builder()
            .docttl("")
            .authorname("")
            .authordeptname("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTREQCOMPLETELIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectWaitList() throws Exception {
        // given
        SelectWaitListReqVO selectWaitListReqVO = SelectWaitListReqVO.builder()
            .docttl("")
            .authorname("")
            .authordeptname("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTWAITLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int getReviewPrint() throws Exception {
        // given
        GetReviewPrintReqVO getReviewPrintReqVO = GetReviewPrintReqVO.builder()
//            .docid("")
            .build();

        // when
        mvc.perform(get(TEST_GETREVIEWPRINT_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateCardBIA1() throws Exception {
        // given
        UpdateCardBIA1ReqVO updateCardBIA1ReqVO = UpdateCardBIA1ReqVO.builder()
            .docid("")
            .build();

        String content = objectMapper.writeValueAsString(updateCardBIA1ReqVO);

        // when
        mvc.perform(get(TEST_UPDATECARDBIA1_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateCardBIA2() throws Exception {
        // given
        UpdateCardBIA2ReqVO updateCardBIA2ReqVO = UpdateCardBIA2ReqVO.builder()
            .docid("")
            .build();

        String content = objectMapper.writeValueAsString(updateCardBIA2ReqVO);

        // when
        mvc.perform(get(TEST_UPDATECARDBIA2_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectIngList() throws Exception {
        // given
        SelectIngListReqVO selectIngListReqVO = SelectIngListReqVO.builder()
            .docttl("")
            .authorname("")
            .authordeptname("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTINGLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectApprovalCompleteList() throws Exception {
        // given
        SelectApprovalCompleteListReqVO selectApprovalCompleteListReqVO = SelectApprovalCompleteListReqVO.builder()
            .docttl("")
            .authorname("")
            .authordeptname("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTAPPROVALCOMPLETELIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateCardBIA() throws Exception {
        // given
        UpdateCardBIAReqVO updateCardBIAReqVO = UpdateCardBIAReqVO.builder()
            .docid("")
            .build();

        String content = objectMapper.writeValueAsString(updateCardBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATECARDBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectSendCurrentList() throws Exception {
        // given
        SelectSendCurrentListReqVO selectSendCurrentListReqVO = SelectSendCurrentListReqVO.builder()
            .docttl("")
            .authorname("")
            .recvdeptname("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTSENDCURRENTLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectDistributeList() throws Exception {
        // given
        SelectDistributeListReqVO selectDistributeListReqVO = SelectDistributeListReqVO.builder()
            .build();

        // when
        mvc.perform(get(TEST_SELECTDISTRIBUTELIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int getManageView() throws Exception {
        // given
        GetManageViewReqVO getManageViewReqVO = GetManageViewReqVO.builder()
            .docid("")
            .build();

        // when
        mvc.perform(get(TEST_GETMANAGEVIEW_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int getRecvConfirm() throws Exception {
        // given
        GetRecvConfirmReqVO getRecvConfirmReqVO = GetRecvConfirmReqVO.builder()
            .docid("")
            .build();

        // when
        mvc.perform(get(TEST_GETRECVCONFIRM_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertRecvConfirmBIA() throws Exception {
        // given
        InsertRecvConfirmBIAReqVO insertRecvConfirmBIAReqVO = InsertRecvConfirmBIAReqVO.builder()
            .docid("")
            .build();

        String content = objectMapper.writeValueAsString(insertRecvConfirmBIAReqVO);

        // when
        mvc.perform(get(TEST_INSERTRECVCONFIRMBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int getRdoc() throws Exception {
        // given
        GetRdocReqVO getRdocReqVO = GetRdocReqVO.builder()
//            .docid("")
            .build();

        // when
        mvc.perform(get(TEST_GETRDOC_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectToDoListApprReady() throws Exception {
        // given
        SelectToDoListApprReadyReqVO selectToDoListApprReadyReqVO = SelectToDoListApprReadyReqVO.builder()
            .docid("")
            .title("")
            .actgubun("")
            .reportdt("")
            .seclevel("")
            .deptname("")
            .dutyname("")
            .proposer("")
            .sendorgname("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTTODOLISTAPPRREADY_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int getDctRdocProc() throws Exception {
        // given
        GetDctRdocProcReqVO getDctRdocProcReqVO = GetDctRdocProcReqVO.builder()
            .docid("")
            .build();

        // when
        mvc.perform(get(TEST_GETDCTRDOCPROC_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectdctRdoc() throws Exception {
        // given
        SelectdctRdocReqVO selectdctRdocReqVO = SelectdctRdocReqVO.builder()
            .authorid("")
            .docid("")
            .docttl("")
            .mgmtno("")
            .mgmtno("")
            .seclevel("")
            .regirecvdt("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTDCTRDOC_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
