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

import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@DisplayName("ComOrgGwaninfoController_TEST")
@WebMvcTest(ComOrgGwaninfoController.class)
public class ComOrgGwaninfoControllerTest extends ControllerTest {
    private final static String TEST_SELECTGWANLIST_URL = "/comorggwaninfo/com/comOrgGwaninfo/selectGwanList";
    private final static String TEST_DELETEGWANBIA_URL = "/comorggwaninfo/com/comOrgGwaninfo/deleteGwanBIA";
    private final static String TEST_GETGWANDETAIL_URL = "/comorggwaninfo/com/comOrgGwaninfo/getGwanDetail";
    private final static String TEST_INSERTGWANBIA_URL = "/comorggwaninfo/com/comOrgGwaninfo/insertGwanBIA";
    private final static String TEST_UPDATEGWANBIA_URL = "/comorggwaninfo/com/comOrgGwaninfo/updateGwanBIA";


    @Autowired
    private ComOrgGwaninfoService comOrgGwaninfoService;

    @Test
    public int selectGwanList() throws Exception {
        // given
        SelectGwanListReqVO selectGwanListReqVO = SelectGwanListReqVO.builder()
            .gwaninname("")
            .etc("")
            .orgname("")
            .managegubun("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTGWANLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public List<Integer> deleteGwanBIA() throws Exception {
        // given
        DeleteGwanBIAReqVO deleteGwanBIAReqVO = DeleteGwanBIAReqVO.builder()
            .orgid("")
            .seq(BigDecimal.ZERO)
            .build();

        // when
        mvc.perform(get(TEST_DELETEGWANBIA_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return new ArrayList<>();
    }

    @Test
    public int getGwanDetail() throws Exception {
        // given
        GetGwanDetailReqVO getGwanDetailReqVO = GetGwanDetailReqVO.builder()
            .orgid("")
            .seq(BigDecimal.ZERO)
            .build();

        // when
        mvc.perform(get(TEST_GETGWANDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertGwanBIA() throws Exception {
        // given
        InsertGwanBIAReqVO insertGwanBIAReqVO = InsertGwanBIAReqVO.builder()
            .orgid("")
            .seq(BigDecimal.ZERO)
            .build();

        String content = objectMapper.writeValueAsString(insertGwanBIAReqVO);

        // when
        mvc.perform(get(TEST_INSERTGWANBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateGwanBIA() throws Exception {
        // given
        UpdateGwanBIAReqVO updateGwanBIAReqVO = UpdateGwanBIAReqVO.builder()
            .orgid("")
            .seq(BigDecimal.ZERO)
            .build();

        String content = objectMapper.writeValueAsString(updateGwanBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEGWANBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
