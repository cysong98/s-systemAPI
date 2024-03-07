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
@DisplayName("BmsDctApprreqController_TEST")
@WebMvcTest(BmsDctApprreqController.class)
public class BmsDctApprreqControllerTest extends ControllerTest {
    private final static String TEST_SELECTUSEREQLIST_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseReqList";
    private final static String TEST_SELECTUSEREQLENDDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseReqLendDetail";
    private final static String TEST_SELECTUSEREQRECYCLEDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseReqRecycleDetail";
    private final static String TEST_SELECTUSEREQVIEWDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseReqViewDetail";
    private final static String TEST_SELECTUSEREQPRINTDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseReqPrintDetail";
    private final static String TEST_SELECTUSEDRAFTLENDDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseDraftLendDetail";
    private final static String TEST_UPDATEUSEDRAFTLENDAPPRECOVERY_URL = "/bmsdctapprreq/use/bmsDctApprreq/updateUseDraftLendAppRecovery";
    private final static String TEST_SELECTUSEDRAFTRECYCLEDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseDraftRecycleDetail";
    private final static String TEST_UPDATEUSEDRAFTRECYCLEAPPRECOVERY_URL = "/bmsdctapprreq/use/bmsDctApprreq/updateUseDraftRecycleAppRecovery";
    private final static String TEST_SELECTUSEDRAFTVIEWDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseDraftViewDetail";
    private final static String TEST_UPDATEUSEDRAFTVIEWAPPRECOVERY_URL = "/bmsdctapprreq/use/bmsDctApprreq/updateUseDraftViewAppRecovery";
    private final static String TEST_SELECTUSEDRAFTPRINTDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseDraftPrintDetail";
    private final static String TEST_UPDATEUSEDRAFTPRINTAPPRECOVERY_URL = "/bmsdctapprreq/use/bmsDctApprreq/updateUseDraftPrintAppRecovery";
    private final static String TEST_SELECTUSEPROCLIST_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseProcList";
    private final static String TEST_SELECTUSEPROCLENDDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseProcLendDetail";
    private final static String TEST_UPDATEUSEPROCLENDAUTHRECOVERY_URL = "/bmsdctapprreq/use/bmsDctApprreq/updateUseProcLendAuthRecovery";
    private final static String TEST_SELECTUSEPROCRECYCLEDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseProcRecycleDetail";
    private final static String TEST_UPDATEUSEPROCRECYCLEAUTHRECOVERY_URL = "/bmsdctapprreq/use/bmsDctApprreq/updateUseProcRecycleAuthRecovery";
    private final static String TEST_SELECTUSEPROCVIEWDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseProcViewDetail";
    private final static String TEST_UPDATEUSEPROCVIEWAUTHRECOVERY_URL = "/bmsdctapprreq/use/bmsDctApprreq/updateUseProcViewAuthRecovery";
    private final static String TEST_SELECTUSEPROCPRINTDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseProcPrintDetail";
    private final static String TEST_UPDATEUSEPROCPRINTAUTHRECOVERY_URL = "/bmsdctapprreq/use/bmsDctApprreq/updateUseProcPrintAuthRecovery";
    private final static String TEST_SELECTUSETEMPLIST_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseTempList";
    private final static String TEST_SELECTUSETEMPLENDDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseTempLendDetail";
    private final static String TEST_SELECTUSETEMPRECYCLEDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseTempRecycleDetail";
    private final static String TEST_SELECTUSETEMPVIEWDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseTempViewDetail";
    private final static String TEST_SELECTUSETEMPPRINTDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseTempPrintDetail";
    private final static String TEST_SELECTUSECOMPLETELIST_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseCompleteList";
    private final static String TEST_SELECTUSECOMPLETELENDDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseCompleteLendDetail";
    private final static String TEST_SELECTUSECOMPLETERECYCLEDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseCompleteRecycleDetail";
    private final static String TEST_SELECTUSECOMPLETEVIEWDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseCompleteViewDetail";
    private final static String TEST_SELECTUSECOMPLETEPRINTDETAIL_URL = "/bmsdctapprreq/use/bmsDctApprreq/selectUseCompletePrintDetail";


    @Autowired
    private BmsDctApprreqService bmsDctApprreqService;

    @Test
    public int selectUseReqList() throws Exception {
        // given
        SelectUseReqListReqVO selectUseReqListReqVO = SelectUseReqListReqVO.builder()
            .apprgubun("")
            .reqttl("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEREQLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseReqLendDetail() throws Exception {
        // given
        SelectUseReqLendDetailReqVO selectUseReqLendDetailReqVO = SelectUseReqLendDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEREQLENDDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseReqRecycleDetail() throws Exception {
        // given
        SelectUseReqRecycleDetailReqVO selectUseReqRecycleDetailReqVO = SelectUseReqRecycleDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEREQRECYCLEDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseReqViewDetail() throws Exception {
        // given
        SelectUseReqViewDetailReqVO selectUseReqViewDetailReqVO = SelectUseReqViewDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEREQVIEWDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseReqPrintDetail() throws Exception {
        // given
        SelectUseReqPrintDetailReqVO selectUseReqPrintDetailReqVO = SelectUseReqPrintDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEREQPRINTDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseDraftLendDetail() throws Exception {
        // given
        SelectUseDraftLendDetailReqVO selectUseDraftLendDetailReqVO = SelectUseDraftLendDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEDRAFTLENDDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseDraftLendAppRecovery() throws Exception {
        // given
        UpdateUseDraftLendAppRecoveryReqVO updateUseDraftLendAppRecoveryReqVO = UpdateUseDraftLendAppRecoveryReqVO.builder()
            .apprreqid("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseDraftLendAppRecoveryReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEDRAFTLENDAPPRECOVERY_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseDraftRecycleDetail() throws Exception {
        // given
        SelectUseDraftRecycleDetailReqVO selectUseDraftRecycleDetailReqVO = SelectUseDraftRecycleDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEDRAFTRECYCLEDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseDraftRecycleAppRecovery() throws Exception {
        // given
        UpdateUseDraftRecycleAppRecoveryReqVO updateUseDraftRecycleAppRecoveryReqVO = UpdateUseDraftRecycleAppRecoveryReqVO.builder()
            .apprreqid("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseDraftRecycleAppRecoveryReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEDRAFTRECYCLEAPPRECOVERY_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseDraftViewDetail() throws Exception {
        // given
        SelectUseDraftViewDetailReqVO selectUseDraftViewDetailReqVO = SelectUseDraftViewDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEDRAFTVIEWDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseDraftViewAppRecovery() throws Exception {
        // given
        UpdateUseDraftViewAppRecoveryReqVO updateUseDraftViewAppRecoveryReqVO = UpdateUseDraftViewAppRecoveryReqVO.builder()
            .apprreqid("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseDraftViewAppRecoveryReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEDRAFTVIEWAPPRECOVERY_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseDraftPrintDetail() throws Exception {
        // given
        SelectUseDraftPrintDetailReqVO selectUseDraftPrintDetailReqVO = SelectUseDraftPrintDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEDRAFTPRINTDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseDraftPrintAppRecovery() throws Exception {
        // given
        UpdateUseDraftPrintAppRecoveryReqVO updateUseDraftPrintAppRecoveryReqVO = UpdateUseDraftPrintAppRecoveryReqVO.builder()
            .apprreqid("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseDraftPrintAppRecoveryReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEDRAFTPRINTAPPRECOVERY_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseProcList() throws Exception {
        // given
        SelectUseProcListReqVO selectUseProcListReqVO = SelectUseProcListReqVO.builder()
            .apprgubun("")
            .reqttl("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEPROCLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseProcLendDetail() throws Exception {
        // given
        SelectUseProcLendDetailReqVO selectUseProcLendDetailReqVO = SelectUseProcLendDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEPROCLENDDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseProcLendAuthRecovery() throws Exception {
        // given
        UpdateUseProcLendAuthRecoveryReqVO updateUseProcLendAuthRecoveryReqVO = UpdateUseProcLendAuthRecoveryReqVO.builder()
            .apprreqid("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseProcLendAuthRecoveryReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEPROCLENDAUTHRECOVERY_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseProcRecycleDetail() throws Exception {
        // given
        SelectUseProcRecycleDetailReqVO selectUseProcRecycleDetailReqVO = SelectUseProcRecycleDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEPROCRECYCLEDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseProcRecycleAuthRecovery() throws Exception {
        // given
        UpdateUseProcRecycleAuthRecoveryReqVO updateUseProcRecycleAuthRecoveryReqVO = UpdateUseProcRecycleAuthRecoveryReqVO.builder()
            .apprreqid("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseProcRecycleAuthRecoveryReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEPROCRECYCLEAUTHRECOVERY_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseProcViewDetail() throws Exception {
        // given
        SelectUseProcViewDetailReqVO selectUseProcViewDetailReqVO = SelectUseProcViewDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEPROCVIEWDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseProcViewAuthRecovery() throws Exception {
        // given
        UpdateUseProcViewAuthRecoveryReqVO updateUseProcViewAuthRecoveryReqVO = UpdateUseProcViewAuthRecoveryReqVO.builder()
            .apprreqid("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseProcViewAuthRecoveryReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEPROCVIEWAUTHRECOVERY_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseProcPrintDetail() throws Exception {
        // given
        SelectUseProcPrintDetailReqVO selectUseProcPrintDetailReqVO = SelectUseProcPrintDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSEPROCPRINTDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseProcPrintAuthRecovery() throws Exception {
        // given
        UpdateUseProcPrintAuthRecoveryReqVO updateUseProcPrintAuthRecoveryReqVO = UpdateUseProcPrintAuthRecoveryReqVO.builder()
            .apprreqid("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseProcPrintAuthRecoveryReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEPROCPRINTAUTHRECOVERY_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseTempList() throws Exception {
        // given
        SelectUseTempListReqVO selectUseTempListReqVO = SelectUseTempListReqVO.builder()
            .apprgubun("")
            .reqttl("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSETEMPLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseTempLendDetail() throws Exception {
        // given
        SelectUseTempLendDetailReqVO selectUseTempLendDetailReqVO = SelectUseTempLendDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSETEMPLENDDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseTempRecycleDetail() throws Exception {
        // given
        SelectUseTempRecycleDetailReqVO selectUseTempRecycleDetailReqVO = SelectUseTempRecycleDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSETEMPRECYCLEDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseTempViewDetail() throws Exception {
        // given
        SelectUseTempViewDetailReqVO selectUseTempViewDetailReqVO = SelectUseTempViewDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSETEMPVIEWDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseTempPrintDetail() throws Exception {
        // given
        SelectUseTempPrintDetailReqVO selectUseTempPrintDetailReqVO = SelectUseTempPrintDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSETEMPPRINTDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseCompleteList() throws Exception {
        // given
        SelectUseCompleteListReqVO selectUseCompleteListReqVO = SelectUseCompleteListReqVO.builder()
            .apprgubun("")
            .reqttl("")
            .startDt("")
            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSECOMPLETELIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseCompleteLendDetail() throws Exception {
        // given
        SelectUseCompleteLendDetailReqVO selectUseCompleteLendDetailReqVO = SelectUseCompleteLendDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSECOMPLETELENDDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseCompleteRecycleDetail() throws Exception {
        // given
        SelectUseCompleteRecycleDetailReqVO selectUseCompleteRecycleDetailReqVO = SelectUseCompleteRecycleDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSECOMPLETERECYCLEDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseCompleteViewDetail() throws Exception {
        // given
        SelectUseCompleteViewDetailReqVO selectUseCompleteViewDetailReqVO = SelectUseCompleteViewDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSECOMPLETEVIEWDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectUseCompletePrintDetail() throws Exception {
        // given
        SelectUseCompletePrintDetailReqVO selectUseCompletePrintDetailReqVO = SelectUseCompletePrintDetailReqVO.builder()
            .reqstatus("")
            .apprreqid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSECOMPLETEPRINTDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
