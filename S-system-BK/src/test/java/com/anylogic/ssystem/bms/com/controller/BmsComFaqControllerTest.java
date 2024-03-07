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
@DisplayName("BmsComFaqController_TEST")
@WebMvcTest(BmsComFaqController.class)
public class BmsComFaqControllerTest extends ControllerTest {
    private final static String TEST_SELECTFAQLIST_URL = "/bmscomfaq/com/bmsComFaq/selectFaqList";
    private final static String TEST_DELETEFAQBIA_URL = "/bmscomfaq/com/bmsComFaq/deleteFaqBIA";
    private final static String TEST_GETFAQDETAIL_URL = "/bmscomfaq/com/bmsComFaq/getFaqDetail";
    private final static String TEST_INSERTFAQBIA_URL = "/bmscomfaq/com/bmsComFaq/insertFaqBIA";
    private final static String TEST_UPDATEFAQBIA_URL = "/bmscomfaq/com/bmsComFaq/updateFaqBIA";


    @Autowired
    private BmsComFaqService bmsComFaqService;

    @Test
    public int selectFaqList() throws Exception {
        // given
        SelectFaqListReqVO selectFaqListReqVO = SelectFaqListReqVO.builder()
            .build();

        // when
        mvc.perform(get(TEST_SELECTFAQLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public List<Integer> deleteFaqBIA() throws Exception {
        // given
        DeleteFaqBIAReqVO deleteFaqBIAReqVO = DeleteFaqBIAReqVO.builder()
            .faqid("")
            .build();

        // when
        mvc.perform(get(TEST_DELETEFAQBIA_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return new ArrayList<>();
    }

    @Test
    public int getFaqDetail() throws Exception {
        // given
        GetFaqDetailReqVO getFaqDetailReqVO = GetFaqDetailReqVO.builder()
            .faqid("")
            .build();

        // when
        mvc.perform(get(TEST_GETFAQDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertFaqBIA() throws Exception {
        // given
        InsertFaqBIAReqVO insertFaqBIAReqVO = InsertFaqBIAReqVO.builder()
            .faqid("")
            .build();

        String content = objectMapper.writeValueAsString(insertFaqBIAReqVO);

        // when
        mvc.perform(get(TEST_INSERTFAQBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateFaqBIA() throws Exception {
        // given
        UpdateFaqBIAReqVO updateFaqBIAReqVO = UpdateFaqBIAReqVO.builder()
            .faqid("")
            .build();

        String content = objectMapper.writeValueAsString(updateFaqBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEFAQBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
