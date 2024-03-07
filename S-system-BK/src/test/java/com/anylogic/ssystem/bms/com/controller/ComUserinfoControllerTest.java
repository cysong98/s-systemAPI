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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Slf4j
@DisplayName("ComUserinfoController_TEST")
@WebMvcTest(ComUserinfoController.class)
public class ComUserinfoControllerTest extends ControllerTest {
    private final static String TEST_SELECTUSERLIST_URL = "/comuserinfo/com/comUserinfo/selectUserList";
    private final static String TEST_DELETEUSERBIA_URL = "/comuserinfo/com/comUserinfo/deleteUserBIA";
    private final static String TEST_GETUSERDETAIL_URL = "/comuserinfo/com/comUserinfo/getUserDetail";
    private final static String TEST_CHECKUSERID_URL = "/comuserinfo/com/comUserinfo/checkUserId";
    private final static String TEST_INSERTUSERBIA_URL = "/comuserinfo/com/comUserinfo/insertUserBIA";
    private final static String TEST_UPDATEUSERBIA_URL = "/comuserinfo/com/comUserinfo/updateUserBIA";
    private final static String TEST_SELECTSIGNLIST_URL = "/comuserinfo/com/comUserinfo/selectSignList";
    private final static String TEST_GETSIGNDETAIL_URL = "/comuserinfo/com/comUserinfo/getSignDetail";


    @Autowired
    private ComUserinfoService comUserinfoService;

    @Test
    public int selectUserList() throws Exception {
        // given
        SelectUserListReqVO selectUserListReqVO = SelectUserListReqVO.builder()
            .userid("")
            .username("")
            .deptname("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTUSERLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public List<Integer> deleteUserBIA() throws Exception {
        // given
        DeleteUserBIAReqVO deleteUserBIAReqVO = DeleteUserBIAReqVO.builder()
            .userid("")
            .build();

        // when
        mvc.perform(get(TEST_DELETEUSERBIA_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return new ArrayList<>();
    }

    @Test
    public int getUserDetail() throws Exception {
        // given
        GetUserDetailReqVO getUserDetailReqVO = GetUserDetailReqVO.builder()
            .userid("")
            .build();

        // when
        mvc.perform(get(TEST_GETUSERDETAIL_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int checkUserId() throws Exception {
        // given
        CheckUserIdReqVO checkUserIdReqVO = CheckUserIdReqVO.builder()
            .userid("")
            .build();

        // when
        mvc.perform(get(TEST_CHECKUSERID_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int insertUserBIA() throws Exception {
        // given
        InsertUserBIAReqVO insertUserBIAReqVO = InsertUserBIAReqVO.builder()
            .userid("")
            .build();

        String content = objectMapper.writeValueAsString(insertUserBIAReqVO);

        // when
        mvc.perform(get(TEST_INSERTUSERBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUserBIA() throws Exception {
        // given
        UpdateUserBIAReqVO updateUserBIAReqVO = UpdateUserBIAReqVO.builder()
            .userid("")
            .build();

        String content = objectMapper.writeValueAsString(updateUserBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSERBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int selectSignList() throws Exception {
        // given
        SelectSignListReqVO selectSignListReqVO = SelectSignListReqVO.builder()
            .userid("")
            .username("")
            .deptname("")
            .managegubun("")
            .build();

        // when
        mvc.perform(get(TEST_SELECTSIGNLIST_URL)
            .contentType(CONTENT_TYPE))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int getSignDetail() throws Exception {
        // given
        GetSignDetailReqVO getSignDetailReqVO = GetSignDetailReqVO.builder()
                .orgid("")
                .seq(BigDecimal.ZERO)
                .build();

        // when
        mvc.perform(get(TEST_GETUSERDETAIL_URL)
                .contentType(CONTENT_TYPE))
                .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
