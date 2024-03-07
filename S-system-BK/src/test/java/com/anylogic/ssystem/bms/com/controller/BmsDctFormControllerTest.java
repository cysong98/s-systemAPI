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
@DisplayName("BmsDctFormController_TEST")
@WebMvcTest(BmsDctFormController.class)
public class BmsDctFormControllerTest extends ControllerTest {
    private final static String TEST_SELECTFORMLIST_URL = "/bmsdctform/com/bmsDctForm/selectFormList";
    private final static String TEST_DELETEFORM_URL = "/bmsdctform/com/bmsDctForm/deleteForm";
    private final static String TEST_GETFORMDETAIL_URL = "/bmsdctform/com/bmsDctForm/getFormDetail";
    private final static String TEST_INSERTFORMBIA_URL = "/bmsdctform/com/bmsDctForm/insertFormBIA";
    private final static String TEST_UPDATEFORMBIA_URL = "/bmsdctform/com/bmsDctForm/updateFormBIA";


    @Autowired
    private BmsDctFormService bmsDctFormService;

    @Test
    public int selectFormList() throws Exception {
        // given
        SelectFormListReqVO selectFormListReqVO = SelectFormListReqVO.builder()
            .categoryname("")
            .formname("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTFORMLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public List<Integer> deleteForm() throws Exception {
        // given
        DeleteFormReqVO deleteFormReqVO = DeleteFormReqVO.builder()
            .fileid("")
            .build();

        // when
        mvc.perform(get(TEST_DELETEFORM_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return new ArrayList<>();
    }

    @Test
    public int getFormDetail() throws Exception {
        // given
        GetFormDetailReqVO getFormDetailReqVO = GetFormDetailReqVO.builder()
            .fileid("")
            .build();

        // when
        mvc.perform(get(TEST_GETFORMDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertFormBIA() throws Exception {
        // given
        InsertFormBIAReqVO insertFormBIAReqVO = InsertFormBIAReqVO.builder()
            .fileid("")
            .build();

        String content = objectMapper.writeValueAsString(insertFormBIAReqVO);

        // when
        mvc.perform(get(TEST_INSERTFORMBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateFormBIA() throws Exception {
        // given
        UpdateFormBIAReqVO updateFormBIAReqVO = UpdateFormBIAReqVO.builder()
            .fileid("")
            .build();

        String content = objectMapper.writeValueAsString(updateFormBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEFORMBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
