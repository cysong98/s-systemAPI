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
@DisplayName("BmsTrnReportController_TEST")
@WebMvcTest(BmsTrnReportController.class)
public class BmsTrnReportControllerTest extends ControllerTest {
    private final static String TEST_SELECTTRANSPROCLISTCOUNT_URL = "/bmstrnreport/trn/bmsTrnReport/selectTransProcListCount";
    private final static String TEST_SELECTTRANSRECVCOUNT_URL = "/bmstrnreport/trn/bmsTrnReport/selectTransRecvCount";
    private final static String TEST_SELECTCOMTRANSPROCLIST_URL = "/bmstrnreport/trn/bmsTrnReport/selectComTransProcList";
    private final static String TEST_SELECTTRANSPROCTODOLIST_URL = "/bmstrnreport/trn/bmsTrnReport/selectTransProcToDoList";
    private final static String TEST_UPDATETRNREPOTBIA1_URL = "/bmstrnreport/trn/bmsTrnReport/updateTrnRepotBIA1";
    private final static String TEST_UPDATETRNREPOTBIA2_URL = "/bmstrnreport/trn/bmsTrnReport/updateTrnRepotBIA2";
    private final static String TEST_SELECTTRANSPROCDEPTTODOLIST_URL = "/bmstrnreport/trn/bmsTrnReport/selectTransProcDeptToDoList";
    private final static String TEST_SELECTTRNREQLIST_URL = "/bmstrnreport/trn/bmsTrnReport/selectTrnReqList";
    private final static String TEST_BMSTRNCOMPLETELIST_URL = "/bmstrnreport/trn/bmsTrnReport/bmsTrnCompleteList";
    private final static String TEST_BMSTRNCURRENTLIST_URL = "/bmstrnreport/trn/bmsTrnReport/bmsTrnCurrentList";
    private final static String TEST_UPDATETRNREPOTBIA_URL = "/bmstrnreport/trn/bmsTrnReport/updateTrnRepotBIA";


    @Autowired
    private BmsTrnReportService bmsTrnReportService;

    @Test
    public int selectTransProcListCount() throws Exception {
        // given
        SelectTransProcListCountReqVO selectTransProcListCountReqVO = SelectTransProcListCountReqVO.builder()
            .tRANSFERID("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTTRANSPROCLISTCOUNT_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectTransRecvCount() throws Exception {
        // given
        SelectTransRecvCountReqVO selectTransRecvCountReqVO = SelectTransRecvCountReqVO.builder()
//            .tRANSFERID("")
//            .aPPRUSERID("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTTRANSRECVCOUNT_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectComTransProcList() throws Exception {
        // given
        SelectComTransProcListReqVO selectComTransProcListReqVO = SelectComTransProcListReqVO.builder()
//            .transferid("")
//            .mGMTNO("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTCOMTRANSPROCLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectTransProcToDoList() throws Exception {
        // given
        SelectTransProcToDoListReqVO selectTransProcToDoListReqVO = SelectTransProcToDoListReqVO.builder()
//            .title("")
//            .reqdeptname("")
//            .requsername("")
//            .apprdeptname("")
//            .apprusername("")
//            .transferid("")
//            .status("")
//            .apprstatus("")
//            .appuserid("")
//            .startDt("")
//            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTTRANSPROCTODOLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateTrnRepotBIA1() throws Exception {
        // given
        UpdateTrnRepotBIA1ReqVO updateTrnRepotBIA1ReqVO = UpdateTrnRepotBIA1ReqVO.builder()
            .apprstatus("")
            .status("")
            .build();

        String content = objectMapper.writeValueAsString(updateTrnRepotBIA1ReqVO);

        // when
        mvc.perform(get(TEST_UPDATETRNREPOTBIA1_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateTrnRepotBIA2() throws Exception {
        // given
        UpdateTrnRepotBIA2ReqVO updateTrnRepotBIA2ReqVO = UpdateTrnRepotBIA2ReqVO.builder()
            .apprstatus("")
            .status("")
            .build();

        String content = objectMapper.writeValueAsString(updateTrnRepotBIA2ReqVO);

        // when
        mvc.perform(get(TEST_UPDATETRNREPOTBIA2_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectTransProcDeptToDoList() throws Exception {
        // given
        SelectTransProcDeptToDoListReqVO selectTransProcDeptToDoListReqVO = SelectTransProcDeptToDoListReqVO.builder()
//            .title("")
//            .reqdeptname("")
//            .requsername("")
//            .apprdeptname("")
//            .apprusername("")
//            .transferid("")
//            .status("")
//            .apprstatus("")
//            .appuserid("")
//            .startDt("")
//            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTTRANSPROCDEPTTODOLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectTrnReqList() throws Exception {
        // given
        SelectTrnReqListReqVO selectTrnReqListReqVO = SelectTrnReqListReqVO.builder()
//            .title("")
//            .reqdeptname("")
//            .requsername("")
//            .apprdeptname("")
//            .apprusername("")
//            .transferid("")
//            .status("")
//            .apprstatus("")
//            .appuserid("")
//            .startDt("")
//            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTTRNREQLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int bmsTrnCompleteList() throws Exception {
        // given
        BmsTrnCompleteListReqVO bmsTrnCompleteListReqVO = BmsTrnCompleteListReqVO.builder()
//            .title("")
//            .reqdeptname("")
//            .requsername("")
//            .apprdeptname("")
//            .apprusername("")
//            .transferid("")
//            .status("")
//            .apprstatus("")
//            .appuserid("")
//            .startDt("")
//            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_BMSTRNCOMPLETELIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int bmsTrnCurrentList() throws Exception {
        // given
        BmsTrnCurrentListReqVO bmsTrnCurrentListReqVO = BmsTrnCurrentListReqVO.builder()
//            .title("")
//            .reqdeptname("")
//            .requsername("")
//            .apprdeptname("")
//            .apprusername("")
//            .transferid("")
//            .status("")
//            .apprstatus("")
//            .appuserid("")
//            .startDt("")
//            .endDt("")
            .build();

        // when
        mvc.perform(get(TEST_BMSTRNCURRENTLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateTrnRepotBIA() throws Exception {
        // given
        UpdateTrnRepotBIAReqVO updateTrnRepotBIAReqVO = UpdateTrnRepotBIAReqVO.builder()
            .apprstatus("")
            .status("")
            .build();

        String content = objectMapper.writeValueAsString(updateTrnRepotBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATETRNREPOTBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
