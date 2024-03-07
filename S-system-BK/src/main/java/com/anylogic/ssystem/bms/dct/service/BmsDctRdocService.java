/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.service;

import com.anylogic.ssystem.bms.com.model.DeleteCodeCategoryReqVO;
import com.anylogic.ssystem.bms.com.model.InsertCodeCategoryReqVO;
import com.anylogic.ssystem.bms.dct.model.*;

import java.util.List;

import com.anylogic.ssystem.common.file.model.AttachFileVO;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


public interface BmsDctRdocService {

    /* 관련근거 팝업 > 비밀문서 탭 > 목록 조회 */
    public List<SelectReferenceListResVO> selectReferenceList(SelectReferenceListReqVO param);

    /* 발송 > 발송대기함 > 발송대기함 목록 조회 */
    public PageInfo<SelectSendWaitListResVO> selectSendWaitList(SelectSendWaitListReqVO param);

    /* 기안 문서처리 (결재경로 다수) & 접수 */
    public List<Map<String, List<AttachFileVO>>> insertCardBIA(List<MultipartFile> draftAttach, List<MultipartFile> refAttach, InsertTempListReqVO param, String
            judgeParam, int insertCardBIASuccessCnt);

    /* 기안 > 임시저장함 > 임시저장함 목록 조회 */
    public PageInfo<SelectTempListResVO> selectTempList(SelectTempListReqVO param);

    public PageInfo<SelectTempListResVO> getTempList(SelectTempListReqVO param);

    /* 기안 > 임시저장함 > 삭제 */
    public List<Integer> deleteTempList(List<DeleteTempListReqVO> param);

    /* (개발 테스트용) 기안 > 기안완료함 > 잘못 생성한 기안문 삭제 */
    public int deleteDraftCompleteList(List<DeleteDraftCompleteListReqVO> param);

    /* 기안 > 기안완료함 > 일괄반려삭제 */
    public int rejectedDocumentToTempSave(List<RejectedDocumentToTempSaveReqVO> param);

    /* 기안 임시저장 */
    public int insertTempList(List<MultipartFile> draftAttach, List<MultipartFile> refAttach, InsertTempListReqVO param);

    /* 재사용 문서처리 & 임시저장 */
    public int insertReuseDraft(List<MultipartFile> draftAttach, List<MultipartFile> refAttach, InsertTempListReqVO param);

    /* (접수 > 비전자비밀접수 > 접수처리) & (비밀관리기록부 > 생산비전자비밀등록 > 등록처리) */
    public int insertOfflineRdoc(List<MultipartFile> draftAttach, InsertTempListReqVO param);

    /* 비밀문서 정보 조회 */
    public GetDctRdocResVO getDctRdoc(GetDctRdocReqVO param);

    /* 비밀관리카드 팝업 권한이력 조회 */
    public List<GetDctAuthCardResVO> getDctAuthCard(GetDctAuthCardReqVO param);

    /* 비밀배포카드 팝업, 발송 상세화면 비밀문서 정보 조회 */
    public SelectTempCreateCardListResVO selectTempCreateCardList(SelectTempCreateCardListReqVO param);

    /* 접수 상세 화면 비밀문서 정보 조회 */
    public SelectTempCreateCardListResVO getTempCreateCardList(SelectTempCreateCardListReqVO param);

    /* 기안 > 기안완료함 > 기안완료함 목록 조회 */
    public PageInfo<SelectReqCompleteListResVO> selectReqCompleteList(SelectReqCompleteListReqVO param);

    /* 결재 > 결재대기함 > 결재대기함 목록 조회 */
    public PageInfo<SelectWaitListResVO> selectWaitList(SelectWaitListReqVO param);

    /* 검토용인쇄 팝업 조회 */
    public GetReviewPrintResVO getReviewPrint(GetReviewPrintReqVO param);

    /* (기안 문서처리 1인결재) & (결재대기문서 결재 승인 처리) */
    public int updateCardBIA1(List<MultipartFile> draftAttach, List<MultipartFile> refAttach, InsertTempListReqVO param);

    /* 결재대기문서 결재 반려 처리*/
    public int updateCardBIA2(List<MultipartFile> draftAttach, List<MultipartFile> refAttach, InsertTempListReqVO param);

    /* 결재 > 진행비밀함 > 진행비밀함 목록 조회 */
    public PageInfo<SelectIngListResVO> selectIngList(SelectIngListReqVO param);

    /* 결재 > 결재완료함 > 결재완료함 목록 조회 */
    public PageInfo<SelectApprovalCompleteListResVO> selectApprovalCompleteList(SelectApprovalCompleteListReqVO param);

    /* 발송 처리 */
    public int updateCardBIA(List<MultipartFile> draftAttach, List<MultipartFile> refAttach, UpdateCardBIAReqVO param, String judgeParam);

    /* 발송 > 발송현황함 > 발송현황함 목록 조회 */
    public PageInfo<SelectSendCurrentListResVO> selectSendCurrentList(SelectSendCurrentListReqVO param);

    /* 비밀배포카드 팝업 발송정보 조회 */
    public PageInfo<SelectDistributeListResVO> selectDistributeList(SelectDistributeListReqVO param);

    /* 반송 상세 팝업 조회 */
    public GetReturnDetailCardResVO getReturnDetailCard(GetReturnDetailCardReqVO param);

    /* 키워드 검색 팝업 조회 */
    public PageInfo<SelectRdocForKeywordSearchResVO> selectRdocForKeywordSearch(SelectRdocForKeywordSearchReqVO param);

    /* 비밀열람함 목록 조회 */
    public PageInfo<SelectdctRdocResVO> selectdctRdoc(SelectdctRdocReqVO param);

    SelectTempCreateCardListResVO getDctSummary(InsertTempListReqVO param);

    /* 발송 보류 처리*/
    public int updateCardHoldBIA(UpdateCardBIAReqVO param);

    /* 비밀배포카드 팝업 유통비밀송수신결과 조회 */
    public PageInfo<InsertRelayHisResVO> selectRelayHis(InsertRelayHisReqVO param);

    /* 발송실패 오프라인인쇄 팝업 > 인쇄이력 등록 */
    public int insertHistoryCard(InsertTempListReqVO param);

}
