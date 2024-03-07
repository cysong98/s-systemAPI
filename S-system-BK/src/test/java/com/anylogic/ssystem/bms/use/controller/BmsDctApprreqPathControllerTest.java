/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.controller;

import com.anylogic.ssystem.common.ControllerTest;
import com.anylogic.ssystem.bms.use.service.*;
import com.anylogic.ssystem.bms.use.model.*;

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
@DisplayName("BmsDctApprreqPathController_TEST")
@WebMvcTest(BmsDctApprreqPathController.class)
public class BmsDctApprreqPathControllerTest extends ControllerTest {
    private final static String TEST_UPDATEUSEREQLENDAPPPATHAPPROVEBIA_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseReqLendAppPathApproveBIA";
    private final static String TEST_UPDATEUSEREQLENDAPPPATHREJECTBIA_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseReqLendAppPathRejectBIA";
    private final static String TEST_UPDATEUSEREQRECYCLEAPPPATHAPPROVEBIA_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseReqRecycleAppPathApproveBIA";
    private final static String TEST_UPDATEUSEREQRECYCLEAPPPATHREJECTBIA_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseReqRecycleAppPathRejectBIA";
    private final static String TEST_UPDATEUSEREQVIEWAPPPATHAPPROVEBIA_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseReqViewAppPathApproveBIA";
    private final static String TEST_UPDATEUSEREQVIEWAPPPATHREJECTBIA_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseReqViewAppPathRejectBIA";
    private final static String TEST_UPDATEUSEREQPRINTAPPPATHAPPROVEBIA_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseReqPrintAppPathApproveBIA";
    private final static String TEST_UPDATEUSEREQPRINTAPPPATHREJECTBIA_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseReqPrintAppPathRejectBIA";
    private final static String TEST_UPDATEUSETEMPLENDAPPPATHAPPROVEBIA_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseTempLendAppPathApproveBIA";
    private final static String TEST_UPDATEUSETEMPRECYCLEAPPPATHAPPROVEBIA_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseTempRecycleAppPathApproveBIA";
    private final static String TEST_UPDATEUSETEMPVIEWAPPPATHAPPROVEBIA_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseTempViewAppPathApproveBIA";
    private final static String TEST_UPDATEUSETEMPPRINTAPPPATHAPPROVEBIA_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseTempPrintAppPathApproveBIA";
    private final static String TEST_UPDATEUSECOMPLETELENDAPPAPPROVE_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseCompleteLendAppApprove";
    private final static String TEST_UPDATEUSECOMPLETERECYCLEAPPAPPROVE_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseCompleteRecycleAppApprove";
    private final static String TEST_UPDATEUSECOMPLETEVIEWAPPAPPROVE_URL = "/bmsdctapprreqpath/use/bmsDctApprreqPath/updateUseCompleteViewAppApprove";


    @Autowired
    private BmsDctApprreqPathService bmsDctApprreqPathService;

    @Test
    public int updateUseReqLendAppPathApproveBIA() throws Exception {
        // given
        UpdateUseReqLendAppPathApproveBIAReqVO updateUseReqLendAppPathApproveBIAReqVO = UpdateUseReqLendAppPathApproveBIAReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseReqLendAppPathApproveBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEREQLENDAPPPATHAPPROVEBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseReqLendAppPathRejectBIA() throws Exception {
        // given
        UpdateUseReqLendAppPathRejectBIAReqVO updateUseReqLendAppPathRejectBIAReqVO = UpdateUseReqLendAppPathRejectBIAReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseReqLendAppPathRejectBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEREQLENDAPPPATHREJECTBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseReqRecycleAppPathApproveBIA() throws Exception {
        // given
        UpdateUseReqRecycleAppPathApproveBIAReqVO updateUseReqRecycleAppPathApproveBIAReqVO = UpdateUseReqRecycleAppPathApproveBIAReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseReqRecycleAppPathApproveBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEREQRECYCLEAPPPATHAPPROVEBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseReqRecycleAppPathRejectBIA() throws Exception {
        // given
        UpdateUseReqRecycleAppPathRejectBIAReqVO updateUseReqRecycleAppPathRejectBIAReqVO = UpdateUseReqRecycleAppPathRejectBIAReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseReqRecycleAppPathRejectBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEREQRECYCLEAPPPATHREJECTBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseReqViewAppPathApproveBIA() throws Exception {
        // given
        UpdateUseReqViewAppPathApproveBIAReqVO updateUseReqViewAppPathApproveBIAReqVO = UpdateUseReqViewAppPathApproveBIAReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseReqViewAppPathApproveBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEREQVIEWAPPPATHAPPROVEBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseReqViewAppPathRejectBIA() throws Exception {
        // given
        UpdateUseReqViewAppPathRejectBIAReqVO updateUseReqViewAppPathRejectBIAReqVO = UpdateUseReqViewAppPathRejectBIAReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseReqViewAppPathRejectBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEREQVIEWAPPPATHREJECTBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseReqPrintAppPathApproveBIA() throws Exception {
        // given
        UpdateUseReqPrintAppPathApproveBIAReqVO updateUseReqPrintAppPathApproveBIAReqVO = UpdateUseReqPrintAppPathApproveBIAReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseReqPrintAppPathApproveBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEREQPRINTAPPPATHAPPROVEBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseReqPrintAppPathRejectBIA() throws Exception {
        // given
        UpdateUseReqPrintAppPathRejectBIAReqVO updateUseReqPrintAppPathRejectBIAReqVO = UpdateUseReqPrintAppPathRejectBIAReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseReqPrintAppPathRejectBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSEREQPRINTAPPPATHREJECTBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseTempLendAppPathApproveBIA() throws Exception {
        // given
        UpdateUseTempLendAppPathApproveBIAReqVO updateUseTempLendAppPathApproveBIAReqVO = UpdateUseTempLendAppPathApproveBIAReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseTempLendAppPathApproveBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSETEMPLENDAPPPATHAPPROVEBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseTempRecycleAppPathApproveBIA() throws Exception {
        // given
        UpdateUseTempRecycleAppPathApproveBIAReqVO updateUseTempRecycleAppPathApproveBIAReqVO = UpdateUseTempRecycleAppPathApproveBIAReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseTempRecycleAppPathApproveBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSETEMPRECYCLEAPPPATHAPPROVEBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseTempViewAppPathApproveBIA() throws Exception {
        // given
        UpdateUseTempViewAppPathApproveBIAReqVO updateUseTempViewAppPathApproveBIAReqVO = UpdateUseTempViewAppPathApproveBIAReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseTempViewAppPathApproveBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSETEMPVIEWAPPPATHAPPROVEBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseTempPrintAppPathApproveBIA() throws Exception {
        // given
        UpdateUseTempPrintAppPathApproveBIAReqVO updateUseTempPrintAppPathApproveBIAReqVO = UpdateUseTempPrintAppPathApproveBIAReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseTempPrintAppPathApproveBIAReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSETEMPPRINTAPPPATHAPPROVEBIA_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseCompleteLendAppApprove() throws Exception {
        // given
        UpdateUseCompleteLendAppApproveReqVO updateUseCompleteLendAppApproveReqVO = UpdateUseCompleteLendAppApproveReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseCompleteLendAppApproveReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSECOMPLETELENDAPPAPPROVE_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseCompleteRecycleAppApprove() throws Exception {
        // given
        UpdateUseCompleteRecycleAppApproveReqVO updateUseCompleteRecycleAppApproveReqVO = UpdateUseCompleteRecycleAppApproveReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseCompleteRecycleAppApproveReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSECOMPLETERECYCLEAPPAPPROVE_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

    @Test
    public int updateUseCompleteViewAppApprove() throws Exception {
        // given
        UpdateUseCompleteViewAppApproveReqVO updateUseCompleteViewAppApproveReqVO = UpdateUseCompleteViewAppApproveReqVO.builder()
            .apprreqid("")
            .apprreqseq("")
            .build();

        String content = objectMapper.writeValueAsString(updateUseCompleteViewAppApproveReqVO);

        // when
        mvc.perform(get(TEST_UPDATEUSECOMPLETEVIEWAPPAPPROVE_URL)
            .contentType(CONTENT_TYPE)
            .content(content))
            .andExpect(STATUS_IS_OK);

        // then

        return 1;
    }

}
