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
@DisplayName("BmsDctOffsendReqController_TEST")
@WebMvcTest(BmsDctOffsendReqController.class)
public class BmsDctOffsendReqControllerTest extends ControllerTest {
    private final static String TEST_INSERTOFFSEND_URL = "/bmsdctoffsendreq/dct/bmsDctOffsendReq/insertOffSend";
    private final static String TEST_UPDATEOFFSEND_URL = "/bmsdctoffsendreq/dct/bmsDctOffsendReq/updateOffSend";
    private final static String TEST_INSERTOFFSENDALL_URL = "/bmsdctoffsendreq/dct/bmsDctOffsendReq/insertOffSendAll";
    private final static String TEST_UPDATEOFFSENDALL_URL = "/bmsdctoffsendreq/dct/bmsDctOffsendReq/updateOffSendAll";


    @Autowired
    private BmsDctOffsendReqService bmsDctOffsendReqService;

    @Test
    public int insertOffSend() throws Exception {
        // given
        InsertOffSendReqVO insertOffSendReqVO = InsertOffSendReqVO.builder()
            .docid("")
            .receiverid("")
            .fileid("")
            .build();

        String content = objectMapper.writeValueAsString(insertOffSendReqVO);

        // when
        mvc.perform(get(TEST_INSERTOFFSEND_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateOffSend() throws Exception {
        // given
        UpdateOffSendReqVO updateOffSendReqVO = UpdateOffSendReqVO.builder()
            .docid("")
            .receiverid("")
            .fileid("")
            .build();

        String content = objectMapper.writeValueAsString(updateOffSendReqVO);

        // when
        mvc.perform(get(TEST_UPDATEOFFSEND_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public List<Integer> insertOffSendAll() throws Exception {
        // given
        InsertOffSendAllReqVO insertOffSendAllReqVO = InsertOffSendAllReqVO.builder()
            .docid("")
            .receiverid("")
            .fileid("")
            .build();

        String content = objectMapper.writeValueAsString(insertOffSendAllReqVO);

        // when
        mvc.perform(get(TEST_INSERTOFFSENDALL_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return new ArrayList<>();
    }

    @Test
    public List<Integer> updateOffSendAll() throws Exception {
        // given
        UpdateOffSendAllReqVO updateOffSendAllReqVO = UpdateOffSendAllReqVO.builder()
            .docid("")
            .receiverid("")
            .fileid("")
            .build();

        String content = objectMapper.writeValueAsString(updateOffSendAllReqVO);

        // when
        mvc.perform(get(TEST_UPDATEOFFSENDALL_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return new ArrayList<>();
    }

}
