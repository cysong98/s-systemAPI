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
@DisplayName("BmsComQnaController_TEST")
@WebMvcTest(BmsComQnaController.class)
public class BmsComQnaControllerTest extends ControllerTest {
    private final static String TEST_SELECTQNALIST_URL = "/bmscomqna/com/bmsComQna/selectQnaList";
    private final static String TEST_DELETEQNA_URL = "/bmscomqna/com/bmsComQna/deleteQna";
    private final static String TEST_GETQNADETAIL_URL = "/bmscomqna/com/bmsComQna/getQnaDetail";
    private final static String TEST_INSERTQNA_URL = "/bmscomqna/com/bmsComQna/insertQna";
    private final static String TEST_UPDATEQNA_URL = "/bmscomqna/com/bmsComQna/updateQna";


    @Autowired
    private BmsComQnaService bmsComQnaService;

    @Test
    public int selectQnaList() throws Exception {
        // given
        SelectQnaListReqVO selectQnaListReqVO = SelectQnaListReqVO.builder()
            .build();

        // when
        mvc.perform(get(TEST_SELECTQNALIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public List<Integer> deleteQna() throws Exception {
        // given
        DeleteQnaReqVO deleteQnaReqVO = DeleteQnaReqVO.builder()
            .qnaid("")
            .build();

        // when
        mvc.perform(get(TEST_DELETEQNA_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return new ArrayList<>();
    }

    @Test
    public int getQnaDetail() throws Exception {
        // given
        GetQnaDetailReqVO getQnaDetailReqVO = GetQnaDetailReqVO.builder()
            .qnaid("")
            .build();

        // when
        mvc.perform(get(TEST_GETQNADETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertQna() throws Exception {
        // given
        InsertQnaReqVO insertQnaReqVO = InsertQnaReqVO.builder()
            .qnaid("")
            .build();

        String content = objectMapper.writeValueAsString(insertQnaReqVO);

        // when
        mvc.perform(get(TEST_INSERTQNA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateQna() throws Exception {
        // given
        UpdateQnaReqVO updateQnaReqVO = UpdateQnaReqVO.builder()
            .qnaid("")
            .build();

        String content = objectMapper.writeValueAsString(updateQnaReqVO);

        // when
        mvc.perform(get(TEST_UPDATEQNA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
