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
@DisplayName("ComOrganizationinfoController_TEST")
@WebMvcTest(ComOrganizationinfoController.class)
public class ComOrganizationinfoControllerTest extends ControllerTest {
    private final static String TEST_SELECTORGANIZATIONLIST_URL = "/comorganizationinfo/com/comOrganizationinfo/selectOrganizationList";
    private final static String TEST_DELETEORGANIZATIONBIA_URL = "/comorganizationinfo/com/comOrganizationinfo/deleteOrganizationBIA";
    private final static String TEST_GETORGANIZATIONDETAIL_URL = "/comorganizationinfo/com/comOrganizationinfo/getOrganizationDetail";
    private final static String TEST_INSERTORGANIZATIONBIA_URL = "/comorganizationinfo/com/comOrganizationinfo/insertOrganizationBIA";
    private final static String TEST_UPDATEORGANIZATIONBIA_URL = "/comorganizationinfo/com/comOrganizationinfo/updateOrganizationBIA";


    @Autowired
    private ComOrganizationinfoService comOrganizationinfoService;

    @Test
    public int selectOrganizationList() throws Exception {
        // given
        SelectOrganizationListReqVO selectOrganizationListReqVO = SelectOrganizationListReqVO.builder()
            .orgid("")
            .orgname("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTORGANIZATIONLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public List<Integer> deleteOrganizationBIA() throws Exception {
        // given
        DeleteOrganizationBIAReqVO deleteOrganizationBIAReqVO = DeleteOrganizationBIAReqVO.builder()
            .orgid("")
            .build();

        // when
        mvc.perform(get(TEST_DELETEORGANIZATIONBIA_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return new ArrayList<>();
    }

    @Test
    public int getOrganizationDetail() throws Exception {
        // given
        GetOrganizationDetailReqVO getOrganizationDetailReqVO = GetOrganizationDetailReqVO.builder()
            .orgid("")
            .build();

        // when
        mvc.perform(get(TEST_GETORGANIZATIONDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertOrganizationBIA() throws Exception {
        // given
        InsertOrganizationBIAReqVO insertOrganizationBIAReqVO = InsertOrganizationBIAReqVO.builder()
            .orgid("")
            .build();

        String content = objectMapper.writeValueAsString(insertOrganizationBIAReqVO);

        // when
        mvc.perform(get(TEST_INSERTORGANIZATIONBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateOrganizationBIA() throws Exception {
        // given
        UpdateOrganizationBIAReqVO updateOrganizationBIAReqVO = UpdateOrganizationBIAReqVO.builder()
            .orgid("")
            .build();

        String content = objectMapper.writeValueAsString(updateOrganizationBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEORGANIZATIONBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
