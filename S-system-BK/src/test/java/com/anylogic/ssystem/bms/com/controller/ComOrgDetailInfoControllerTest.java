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
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@DisplayName("ComOrgDetailInfoController_TEST")
@WebMvcTest(ComOrgDetailInfoController.class)
public class ComOrgDetailInfoControllerTest extends ControllerTest {
    private final static String TEST_DELETESIGNBIA_URL = "/comorgdetailinfo/com/comOrgDetailInfo/deleteSignBIA";
    private final static String TEST_INSERTSIGNBIA_URL = "/comorgdetailinfo/com/comOrgDetailInfo/insertSignBIA";
    private final static String TEST_UPDATESIGNBIA_URL = "/comorgdetailinfo/com/comOrgDetailInfo/updateSignBIA";


    @Autowired
    private ComOrgDetailInfoService comOrgDetailInfoService;

    @Test
    public List<Integer> deleteSignBIA() throws Exception {
        // given
        DeleteSignBIAReqVO deleteSignBIAReqVO = DeleteSignBIAReqVO.builder()
            .orgid("")
            .seq(new BigDecimal(0))
            .build();

        // when
        mvc.perform(get(TEST_DELETESIGNBIA_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return new ArrayList<>();
    }

    @Test
    public int insertSignBIA() throws Exception {
        // given
        InsertSignBIAReqVO insertSignBIAReqVO = InsertSignBIAReqVO.builder()
            .orgid("")
            .seq(new BigDecimal(0))
            .build();

        String content = objectMapper.writeValueAsString(insertSignBIAReqVO);

        // when
        mvc.perform(get(TEST_INSERTSIGNBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateSignBIA() throws Exception {
        // given
        UpdateSignBIAReqVO updateSignBIAReqVO = UpdateSignBIAReqVO.builder()
            .orgid("")
            .seq(new BigDecimal(0))
            .build();

        String content = objectMapper.writeValueAsString(updateSignBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATESIGNBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
