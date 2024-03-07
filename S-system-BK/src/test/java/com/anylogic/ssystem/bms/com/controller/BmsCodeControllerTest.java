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
@DisplayName("BmsCodeController_TEST")
@WebMvcTest(BmsCodeController.class)
public class BmsCodeControllerTest extends ControllerTest {
    private final static String TEST_SELECTCODELIST_URL = "/bmscode/com/bmsCode/selectCodeList";
    private final static String TEST_DELETECODE_URL = "/bmscode/com/bmsCode/deleteCode";
    private final static String TEST_INSERTCODE_URL = "/bmscode/com/bmsCode/insertCode";
    private final static String TEST_GETCODEDETAIL_URL = "/bmscode/com/bmsCode/getCodeDetail";
    private final static String TEST_UPDATECODE_URL = "/bmscode/com/bmsCode/updateCode";


    @Autowired
    private BmsCodeService bmsCodeService;

    @Test
    public int selectCodeList() throws Exception {
        // given
        SelectCodeListReqVO selectCodeListReqVO = SelectCodeListReqVO.builder()
            .tcodeid("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTCODELIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int deleteCode() throws Exception {
        // given
        DeleteCodeReqVO deleteCodeReqVO = DeleteCodeReqVO.builder()
            .codeid("")
            .build();
        // when
        mvc.perform(get(TEST_DELETECODE_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertCode() throws Exception {
        // given
        InsertCodeReqVO insertCodeReqVO = InsertCodeReqVO.builder()
            .codeid("")
            .tcodeid("")
            .build();

        String content = objectMapper.writeValueAsString(insertCodeReqVO);

        // when
        mvc.perform(get(TEST_INSERTCODE_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int getCodeDetail() throws Exception {
        // given
        GetCodeDetailReqVO getCodeDetailReqVO = GetCodeDetailReqVO.builder()
            .codeid("")
            .build();

        // when
        mvc.perform(get(TEST_GETCODEDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateCode() throws Exception {
        // given
        UpdateCodeReqVO updateCodeReqVO = UpdateCodeReqVO.builder()
            .codeid("")
            .tcodeid("")
            .build();

        String content = objectMapper.writeValueAsString(updateCodeReqVO);

        // when
        mvc.perform(get(TEST_UPDATECODE_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
