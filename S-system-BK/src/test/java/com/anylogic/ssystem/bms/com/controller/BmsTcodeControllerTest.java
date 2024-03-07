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
@DisplayName("BmsTcodeController_TEST")
@WebMvcTest(BmsTcodeController.class)
public class BmsTcodeControllerTest extends ControllerTest {
    private final static String TEST_SELECTCODECATEGORYLIST_URL = "/bmstcode/com/bmsTcode/selectCodeCategoryList";
    private final static String TEST_DELETECODECATEGORY_URL = "/bmstcode/com/bmsTcode/deleteCodeCategory";
    private final static String TEST_INSERTCODECATEGORY_URL = "/bmstcode/com/bmsTcode/insertCodeCategory";
    private final static String TEST_GETCODECATEGORYDETAIL_URL = "/bmstcode/com/bmsTcode/getCodeCategoryDetail";
    private final static String TEST_UPDATECODECATEGORY_URL = "/bmstcode/com/bmsTcode/updateCodeCategory";


    @Autowired
    private BmsTcodeService bmsTcodeService;

    @Test
    public int selectCodeCategoryList() throws Exception {
        // given
        SelectCodeCategoryListReqVO selectCodeCategoryListReqVO = SelectCodeCategoryListReqVO.builder()
            .tcodeid("")
            .tcodename("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTCODECATEGORYLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int deleteCodeCategory() throws Exception {
        // given
        DeleteCodeCategoryReqVO deleteCodeCategoryReqVO = DeleteCodeCategoryReqVO.builder()
            .tcodeid("")
            .build();
        // when
        mvc.perform(get(TEST_DELETECODECATEGORY_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertCodeCategory() throws Exception {
        // given
        InsertCodeCategoryReqVO insertCodeCategoryReqVO = InsertCodeCategoryReqVO.builder()
            .tcodeid("")
            .build();

        String content = objectMapper.writeValueAsString(insertCodeCategoryReqVO);

        // when
        mvc.perform(get(TEST_INSERTCODECATEGORY_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int getCodeCategoryDetail() throws Exception {
        // given
        GetCodeCategoryDetailReqVO getCodeCategoryDetailReqVO = GetCodeCategoryDetailReqVO.builder()
            .tcodeid("")
            .build();

        // when
        mvc.perform(get(TEST_GETCODECATEGORYDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateCodeCategory() throws Exception {
        // given
        UpdateCodeCategoryReqVO updateCodeCategoryReqVO = UpdateCodeCategoryReqVO.builder()
            .tcodeid("")
            .build();

        String content = objectMapper.writeValueAsString(updateCodeCategoryReqVO);

        // when
        mvc.perform(get(TEST_UPDATECODECATEGORY_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
