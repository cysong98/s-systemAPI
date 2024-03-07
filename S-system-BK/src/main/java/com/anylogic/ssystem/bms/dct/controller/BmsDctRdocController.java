/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.controller;

import java.util.*;

import com.anylogic.ssystem.bms.com.model.DeleteCodeCategoryReqVO;
import com.anylogic.ssystem.bms.com.model.InsertCodeCategoryReqVO;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;
import com.anylogic.ssystem.bms.dct.model.*;
import com.anylogic.ssystem.bms.dct.service.ConvertPDFService;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.anylogic.ssystem.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.dct.service.BmsDctRdocService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"dct"})
@RestController
@RequestMapping("/bms/dct/bmsdctrdoc")
public class BmsDctRdocController {

    @Autowired
    private BmsDctRdocService bmsDctRdocService;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;

    /* 관리번호 생성 시 시퀀스 조회 & 리셋 */
    @GetMapping("/getTempSeq")
    public String getTempSeq() {
        String tempSeq = bmsDctRdocMapper.getTempSeq();
        return tempSeq;
    }
    @GetMapping("/getRecentMgmt")
    public String getRecentMgmt() {
        String recentMgmt = bmsDctRdocMapper.getRecentMgmt();
        return recentMgmt;
    }
    @GetMapping("/resetTempSeq")
    public String resetTempSeq() {
        String tempSeq = bmsDctRdocMapper.resetTempSeq();
        return tempSeq;
    }


    /* 관련근거 팝업 > 비밀문서 탭 > 목록 조회 */
    @GetMapping("/selectReferenceList")
    public ResponseEntity selectReferenceList
    (
        @Valid SelectReferenceListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectReferenceList(param));
    }


    /* 발송 > 발송대기함 > 발송대기함 목록 조회 */
    @GetMapping("/selectSendWaitList")
    public ResponseEntity selectSendWaitList
    (
        @Valid SelectSendWaitListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectSendWaitList(param));
    }


    /* 기안 문서처리 (결재경로 다수) & 접수 */
    @PostMapping(value = "/insertCardBIA", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity insertCardBIA
    (
        @RequestPart(value = "draftAttach", required = false) List<MultipartFile> draftAttach
        ,@RequestPart(value = "refAttach", required = false) List<MultipartFile> refAttach
        ,@RequestPart("reqInsertCardBIA") @Valid InsertTempListReqVO param
        ,@RequestPart(value = "judgeParam", required = false) String judgeParam
        ,@RequestPart(value = "insertCardBIASuccessCnt", required = false) Integer insertCardBIASuccessCnt
    )
    {
        List<Map<String, List<AttachFileVO>>> res;
        try {
            int safeInsertCardBIASuccessCnt = insertCardBIASuccessCnt != null ? insertCardBIASuccessCnt : 0;
            res = bmsDctRdocService.insertCardBIA(draftAttach,refAttach,param,judgeParam, safeInsertCardBIASuccessCnt);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 기안 > 임시저장함 > 임시저장함 목록 조회 */
    @GetMapping("/selectTempList")
    public ResponseEntity selectTempList
    (
        @Valid SelectTempListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectTempList(param));
    }


    /* 기안 > 임시저장함 > 삭제 */
    @DeleteMapping(value = "/deleteTempList")
    public ResponseEntity deleteTempList
    (
        @Valid @RequestBody List<DeleteTempListReqVO> params
    )
    {
        List<Integer> res = new ArrayList<>();
        try {
            res = bmsDctRdocService.deleteTempList(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* (개발 테스트용) 기안 > 기안완료함 > 잘못 생성한 기안문 삭제 */
    @DeleteMapping(value = "/deleteDraftCompleteList")
    public ResponseEntity deleteDraftCompleteList
    (
        @Valid @RequestBody List<DeleteDraftCompleteListReqVO> params
    )
    {
        int res = 0;
        try {
            res = bmsDctRdocService.deleteDraftCompleteList(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 기안 > 기안완료함 > 일괄반려삭제 */
    @PutMapping(value = "/rejectedDocumentToTempSave")
    public ResponseEntity rejectedDocumentToTempSave
    (
        @Valid @RequestBody List<RejectedDocumentToTempSaveReqVO> params
    )
    {
        int res = 0;
        try {
            res = bmsDctRdocService.rejectedDocumentToTempSave(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 기안 임시저장 */
    @PostMapping(value = "/insertTempList", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity insertTempList
    (
        @RequestPart(value = "draftAttach", required = false) List<MultipartFile> draftAttach
        ,@RequestPart(value = "refAttach", required = false) List<MultipartFile> refAttach
        , @RequestPart("reqInsertTempList") @Valid InsertTempListReqVO param
    )
    {
        int res = 0;
        try {
            res = bmsDctRdocService.insertTempList(draftAttach,refAttach, param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 재사용 문서처리 & 임시저장 */
    @PostMapping(value = "/insertReuseDraft", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity insertReuseDraft
    (
        @RequestPart(value = "draftAttach", required = false) List<MultipartFile> draftAttach
        ,@RequestPart(value = "refAttach", required = false) List<MultipartFile> refAttach
        , @RequestPart("reqInsertReuseDraft") @Valid InsertTempListReqVO param
    )
    {
        int res = 0;
        try {
            res = bmsDctRdocService.insertReuseDraft(draftAttach,refAttach, param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* (접수 > 비전자비밀접수 > 접수처리) & (비밀관리기록부 > 생산비전자비밀등록 > 등록처리) */
    @PostMapping(value = "/insertOfflineRdoc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity insertOfflineRdoc
    (
        @RequestPart(value = "draftAttach", required = false) List<MultipartFile> draftAttach
        , @RequestPart("reqInsertOfflineRdoc") @Valid InsertTempListReqVO param
    )
    {
        int res = 0;
        try {
            res = bmsDctRdocService.insertOfflineRdoc(draftAttach, param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 비밀배포카드 팝업, 발송 상세화면 비밀문서 정보 조회 */
    @GetMapping("/selectTempCreateCardList")
    public ResponseEntity selectTempCreateCardList
    (
        @Valid SelectTempCreateCardListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectTempCreateCardList(param));
    }


    /* 접수 상세 화면 비밀문서 정보 조회 */
    @GetMapping("/getTempCreateCardList")
    public ResponseEntity getTempCreateCardList
    (
        @Valid SelectTempCreateCardListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.getTempCreateCardList(param));
    }


    /* 비밀문서 정보 조회 */
    @GetMapping("/getDctRdoc")
    public ResponseEntity getDctRdoc
    (
        @Valid GetDctRdocReqVO param

    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.getDctRdoc(param));
    }


    /* 비밀관리카드 팝업 권한이력 조회 */
    @GetMapping("/getDctAuthCard")
    public ResponseEntity getDctAuthCard
    (
        @Valid GetDctAuthCardReqVO param

    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.getDctAuthCard(param));
    }


    /* 기안 > 기안완료함 > 기안완료함 목록 조회 */
    @GetMapping("/selectReqCompleteList")
    public ResponseEntity selectReqCompleteList
    (
        @Valid SelectReqCompleteListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectReqCompleteList(param));
    }


    /* 결재 > 결재대기함 > 결재대기함 목록 조회 */
    @GetMapping("/selectWaitList")
    public ResponseEntity selectWaitList
    (
        @Valid SelectWaitListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectWaitList(param));
    }


    /* 검토용인쇄 팝업 조회 */
    @GetMapping("/getReviewPrint")
    public ResponseEntity getReviewPrint
    (
        @Valid GetReviewPrintReqVO param

    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.getReviewPrint(param));
    }


    /* (기안 문서처리 1인결재) & (결재대기문서 결재 승인 처리) */
    @PostMapping(value = "/updateCardBIA1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateCardBIA1
    (
        @RequestPart(value = "draftAttach", required = false) List<MultipartFile> draftAttach
        ,@RequestPart(value = "refAttach", required = false) List<MultipartFile> refAttach
        , @RequestPart("reqUpdateCardBIA1") @Valid InsertTempListReqVO param
    )
    {
        int res = 0;
        try {
            res = bmsDctRdocService.updateCardBIA1(draftAttach, refAttach, param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 결재대기문서 결재 반려 처리*/
    @PostMapping(value = "/updateCardBIA2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateCardBIA2
    (
        @RequestPart(value = "draftAttach", required = false) List<MultipartFile> draftAttach
        ,@RequestPart(value = "refAttach", required = false) List<MultipartFile> refAttach
        , @RequestPart("reqUpdateCardBIA2") @Valid InsertTempListReqVO param
    )
    {
        int res = 0;
        try {
            res = bmsDctRdocService.updateCardBIA2(draftAttach, refAttach, param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 결재 > 진행비밀함 > 진행비밀함 목록 조회 */
    @GetMapping("/selectIngList")
    public ResponseEntity selectIngList
    (
        @Valid SelectIngListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectIngList(param));
    }


    /* 결재 > 결재완료함 > 결재완료함 목록 조회 */
    @GetMapping("/selectApprovalCompleteList")
    public ResponseEntity selectApprovalCompleteList
    (
        @Valid SelectApprovalCompleteListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectApprovalCompleteList(param));
    }


    /* 발송 처리 */
    @PostMapping(value = "/updateCardBIA", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateCardBIA
    (
        @RequestPart(value = "draftAttach", required = false) List<MultipartFile> draftAttach
        ,@RequestPart(value = "refAttach", required = false) List<MultipartFile> refAttach
        , @RequestPart("reqUpdateCardBIA") @Valid UpdateCardBIAReqVO param
        ,@RequestPart(value = "judgeParam", required = false) String judgeParam
    )
    {
        int res = 0;
        try {
            res = bmsDctRdocService.updateCardBIA(draftAttach,refAttach,param,judgeParam);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 발송 > 발송현황함 > 발송현황함 목록 조회 */
    @GetMapping("/selectSendCurrentList")
    public ResponseEntity selectSendCurrentList
    (
        @Valid SelectSendCurrentListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectSendCurrentList(param));
    }


    /* 비밀배포카드 팝업 발송정보 조회 */
    @GetMapping("/selectDistributeList")
    public ResponseEntity selectDistributeList
    (
        @Valid SelectDistributeListReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectDistributeList(param));
    }


    /* 반송 상세 팝업 조회 */
    @GetMapping("/getReturnDetailCard")
    public ResponseEntity getReturnDetailCard
    (
        @Valid GetReturnDetailCardReqVO param

    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.getReturnDetailCard(param));
    }


    /* 키워드 검색 팝업 조회 */
    @GetMapping("/selectRdocForKeywordSearch")
    public ResponseEntity selectRdocForKeywordSearch
    (
        @Valid SelectRdocForKeywordSearchReqVO param

    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectRdocForKeywordSearch(param));
    }


    /* 비밀열람함 목록 조회 */
    @GetMapping("/selectdctRdoc")
    public ResponseEntity selectdctRdoc
    (
        @Valid SelectdctRdocReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectdctRdoc(param));
    }


    /* 발송 보류 처리*/
    @PutMapping(value = "/updateCardHoldBIA")
    public ResponseEntity updateCardHoldBIA
    (
        @Valid @RequestBody UpdateCardBIAReqVO param
    )
    {
        int res = 0;
        try {
            res = bmsDctRdocService.updateCardHoldBIA(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }


    /* 비밀배포카드 팝업 유통비밀송수신결과 조회 */
    @GetMapping("/selectRelayHis")
    public ResponseEntity selectRelayHis
    (
        @Valid InsertRelayHisReqVO param
    ) throws Exception
    {
        return ResponseEntity.ok(bmsDctRdocService.selectRelayHis(param));
    }


    /* 발송실패 오프라인인쇄 팝업 > 인쇄이력 등록 */
    @PostMapping(value = "/insertHistoryCard")
    public ResponseEntity insertHistoryCard
    (
        @Valid @RequestBody InsertTempListReqVO param
    )
    {
        int res = 0;
        try {
            res = bmsDctRdocService.insertHistoryCard(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }

}
