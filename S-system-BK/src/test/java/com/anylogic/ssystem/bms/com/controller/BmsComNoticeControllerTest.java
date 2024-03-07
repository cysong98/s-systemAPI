/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.controller;

import com.anylogic.ssystem.common.ControllerTest;
import com.anylogic.ssystem.bms.com.service.*;
import com.anylogic.ssystem.bms.com.model.*;

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
@DisplayName("BmsComNoticeController_TEST")
@WebMvcTest(BmsComNoticeController.class)
public class BmsComNoticeControllerTest extends ControllerTest {
    private final static String TEST_SELECTNOTICELIST_URL = "/bmscomnotice/com/bmsComNotice/selectNoticeList";
    private final static String TEST_DELETENOTICEBIA_URL = "/bmscomnotice/com/bmsComNotice/deleteNoticeBIA";
    private final static String TEST_GETNOTICEDETAIL_URL = "/bmscomnotice/com/bmsComNotice/getNoticeDetail";
    private final static String TEST_INSERTNOTICEBIA_URL = "/bmscomnotice/com/bmsComNotice/insertNoticeBIA";
    private final static String TEST_UPDATENOTICEBIA_URL = "/bmscomnotice/com/bmsComNotice/updateNoticeBIA";


    @Autowired
    private BmsComNoticeService bmsComNoticeService;

    @Test
    public int selectNoticeList() throws Exception {
        // given
        SelectNoticeListReqVO selectNoticeListReqVO = SelectNoticeListReqVO.builder()
            .build();

        // when
        mvc.perform(get(TEST_SELECTNOTICELIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public List<Integer> deleteNoticeBIA() throws Exception {
        // given
        DeleteNoticeBIAReqVO deleteNoticeBIAReqVO = DeleteNoticeBIAReqVO.builder()
            .ntcid("")
            .build();

        // when
        mvc.perform(get(TEST_DELETENOTICEBIA_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return new ArrayList<>();
    }

    @Test
    public int getNoticeDetail() throws Exception {
        // given
        GetNoticeDetailReqVO getNoticeDetailReqVO = GetNoticeDetailReqVO.builder()
            .ntcid("")
            .build();

        // when
        mvc.perform(get(TEST_GETNOTICEDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertNoticeBIA() throws Exception {
        // given
        InsertNoticeBIAReqVO insertNoticeBIAReqVO = InsertNoticeBIAReqVO.builder()
            .ntcid("")
            .build();

        String content = objectMapper.writeValueAsString(insertNoticeBIAReqVO);

        // when
        mvc.perform(get(TEST_INSERTNOTICEBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateNoticeBIA() throws Exception {
        // given
        UpdateNoticeBIAReqVO updateNoticeBIAReqVO = UpdateNoticeBIAReqVO.builder()
            .ntcid("")
            .build();

        String content = objectMapper.writeValueAsString(updateNoticeBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATENOTICEBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
