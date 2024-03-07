/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.mapper;

import com.anylogic.ssystem.bms.com.model.DeleteCodeCategoryReqVO;
import com.anylogic.ssystem.bms.com.model.InsertCodeCategoryReqVO;
import com.anylogic.ssystem.bms.dct.model.*;
import com.anylogic.ssystem.bms.use.model.UpdateTransWaitReqVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;


@Mapper
public interface BmsDctRdocMapper {

    /* sys_guid 조회 */
    String getSysGuid();

    /* 관리번호 생성 시 시퀀스 조회 & 리셋 */
    String getTempSeq(); // 임시
    String resetTempSeq(); // 임시 시퀀스 초기화
    String getRecentMgmt();
    String resetActSeq2(); // 임시 시퀀스 초기화
    String getRecentRdoc();


    /* 관련근거 팝업 > 비밀문서 탭 > 목록 조회 */
    List<SelectReferenceListResVO> selectReferenceList(SelectReferenceListReqVO param) throws RuntimeException;

    /* 발송 > 발송대기함 > 발송대기함 목록 조회 */
    List<SelectSendWaitListResVO> selectSendWaitList(SelectSendWaitListReqVO param) throws RuntimeException;

    /* 기안 > 임시저장함 > 임시저장함 목록 조회 */
    List<SelectTempListResVO> selectTempList(SelectTempListReqVO param) throws RuntimeException;

    /* 기안 > 임시저장함 > 삭제 */
    int deleteTempList(Map<String, Object> param) throws RuntimeException;
    int deleteTaskInfo(Map<String, Object> param) throws RuntimeException;
    int deleteRef(Map<String, Object> param) throws RuntimeException;
    int deleteRefTemp(Map<String, Object> param) throws RuntimeException;


    /* (개발 테스트용) 기안 > 기안완료함 > 잘못 생성한 기안문 삭제 */
    int deleteDraftTask(DeleteDraftCompleteListReqVO param) throws RuntimeException;
    int deleteDraftFile(DeleteDraftCompleteListReqVO param) throws RuntimeException;
    int deleteDraftAuthCard(DeleteDraftCompleteListReqVO param) throws RuntimeException;
    int deleteDraftHistCard(DeleteDraftCompleteListReqVO param) throws RuntimeException;
    int deleteDraftRcvs(DeleteDraftCompleteListReqVO param) throws RuntimeException;
    int deleteDraftRecv(DeleteDraftCompleteListReqVO param) throws RuntimeException;
    int deleteDraftSummary(DeleteDraftCompleteListReqVO param) throws RuntimeException;
    int deleteDraftOpinion(DeleteDraftCompleteListReqVO param) throws RuntimeException;
    int deleteDraftPath(DeleteDraftCompleteListReqVO param) throws RuntimeException;
    int deleteDraftMgmt(DeleteDraftCompleteListReqVO param) throws RuntimeException;
    int deleteDraftAttr(DeleteDraftCompleteListReqVO param) throws RuntimeException;
    int deleteDraftRdoc(DeleteDraftCompleteListReqVO param) throws RuntimeException;


    /* 기안 > 기안완료함 > 일괄반려삭제 */
    int deleteRejectedOpinion(RejectedDocumentToTempSaveReqVO param) throws RuntimeException;
    int updateRejectedPath(RejectedDocumentToTempSaveReqVO param) throws RuntimeException;
    int deleteRejectedPath(RejectedDocumentToTempSaveReqVO param) throws RuntimeException;
    int convertTaskinfoToTaskinfotemp(RejectedDocumentToTempSaveReqVO param) throws RuntimeException;
    int deleteRejectedTaskinfo(RejectedDocumentToTempSaveReqVO param) throws RuntimeException;
    int updateRejectedRecv(RejectedDocumentToTempSaveReqVO param) throws RuntimeException;
    int updateRejectedRdoc(RejectedDocumentToTempSaveReqVO param) throws RuntimeException;


    /* 비밀 문서 등록 */
    int insertTempList(InsertTempListReqVO param) throws RuntimeException;

    /* 문서 취지 등록 */
    int insertSummary(InsertTempListReqVO param) throws RuntimeException;

    /* 비밀문서 정보 조회 */
    GetDctRdocResVO getDctRdoc(GetDctRdocReqVO param) throws RuntimeException;

    /* 비밀관리카드 팝업 권한이력 조회 */
    List<GetDctAuthCardResVO> getDctAuthCard(GetDctAuthCardReqVO param) throws RuntimeException;

    /* 기안 > 기안완료함 > 기안완료함 목록 조회 */
    List<SelectReqCompleteListResVO> selectReqCompleteList(SelectReqCompleteListReqVO param) throws RuntimeException;

    /* 결재 > 결재대기함 > 결재대기함 목록 조회 */
    List<SelectWaitListResVO> selectWaitList(SelectWaitListReqVO param) throws RuntimeException;

    /* 검토용인쇄 팝업 조회 */
    GetReviewPrintResVO getReviewPrint(GetReviewPrintReqVO param) throws RuntimeException;

    /* 비밀 수정 */
    int updateCardBIA(UpdateCardBIAReqVO param) throws RuntimeException;

    /* 비밀 수정1 */
    int updateCardBIA1(UpdateCardBIA1ReqVO param) throws RuntimeException;

    /* 결재 > 진행비밀함 > 진행비밀함 목록 조회 */
    List<SelectIngListResVO> selectIngList(SelectIngListReqVO param) throws RuntimeException;

    /* 결재 > 결재완료함 > 결재완료함 목록 조회 */
    List<SelectApprovalCompleteListResVO> selectApprovalCompleteList(SelectApprovalCompleteListReqVO param) throws RuntimeException;

    /* 사본 비밀 등록 */
    int insertCopyCard(InsertCopyCardReqVO param) throws RuntimeException;

    /* 발송 > 발송현황함 > 발송현황함 목록 조회 */
    List<SelectSendCurrentListResVO> selectSendCurrentList(SelectSendCurrentListReqVO param) throws RuntimeException;

    /* 비밀배포카드 팝업 발송정보 조회 */
    List<SelectDistributeListResVO> selectDistributeList(SelectDistributeListReqVO param) throws RuntimeException;

    /* 반송 상세 팝업 조회 */
    GetReturnDetailCardResVO getReturnDetailCard(GetReturnDetailCardReqVO param) throws RuntimeException;

    /* 인계인수 시 비밀 수정 */
    int updateDctRdoc(UpdateDctRdocReqVO param) throws RuntimeException;

    /* 키워드 검색 팝업 조회 */
    List<SelectRdocForKeywordSearchResVO> selectRdocForKeywordSearch(SelectRdocForKeywordSearchReqVO param) throws RuntimeException;

    /* 비밀열람함 목록 조회 */
    List<SelectdctRdocResVO> selectdctRdoc(SelectdctRdocReqVO param) throws RuntimeException;

    /* 접수 상세화면 문서취지 조회 */
    SelectTempCreateCardListResVO getDctSummary(InsertTempListReqVO param);

    /* 이관대기요청서 작성 시 비밀 수정 */
    int updateTransWaitRdoc(UpdateTransWaitReqVO param) throws RuntimeException;

    /* 권한 조회 */
    List<SelectAuthCardResVO> selectAuthCard(SelectAuthCardReqVO param) throws RuntimeException;

    /* 권한 등록 */
    int insertAuthCard(InsertAuthCardReqVO param) throws RuntimeException;

    /* 권한 수정 */
    int updateAuthCard(UpdateAuthCardReqVO param) throws RuntimeException;

    /* 유통비밀송수신결과 등록 */
    int insertRelayHis(InsertRelayHisReqVO param) throws RuntimeException;

    /* 비밀배포카드 팝업 유통비밀송수신결과 조회 */
    List<InsertRelayHisResVO> selectRelayHis(InsertRelayHisReqVO param) throws RuntimeException;

    /* 발송실패 오프라인인쇄 팝업 > 인쇄이력 등록 */
    int insertHistoryCard(InsertHistoryReqVO param) throws RuntimeException;
}
