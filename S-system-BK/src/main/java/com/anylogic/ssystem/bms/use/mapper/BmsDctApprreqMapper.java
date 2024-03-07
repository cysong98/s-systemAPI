/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.mapper;

import com.anylogic.ssystem.bms.com.model.SelectApprReqListReqVO;
import com.anylogic.ssystem.bms.com.model.SelectApprReqListResVO;
import com.anylogic.ssystem.bms.use.model.*;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;


@Mapper
public interface BmsDctApprreqMapper {

    /* 비밀문서 사용(처리할, 기안한, 처리한, 요청서완료처리) 목록 조회 */
    List<SelectUseReqListResVO> selectUseReqList(SelectUseReqListReqVO param) throws RuntimeException;

    /* 요청서 조회 */
    SelectUseReqViewDetailResVO selectUseReqViewDetail(SelectUseReqViewDetailReqVO param) throws RuntimeException;

    /* 반출 요청서 조회 */
    SelectUseReqViewDetailResVO selectUseReqViewLendDetail(SelectUseReqViewDetailReqVO param) throws RuntimeException;

    /* 요청서 결재 경로 조회 */
    List<SelectUseReqViewPathListResVO> selectUseReqViewPathList(SelectUseReqViewPathListReqVO param) throws RuntimeException;

    /* 요청서 등록 */
    int insertDctApprReq(InsertDctApprReqReqVO param) throws RuntimeException;

    /* 요청서 작성일자 조회*/
    SelectApprReqListResVO getApprReqregdtByid(SelectApprReqListReqVO param) throws RuntimeException;

    /* 재사용 가능 여부 체크 */
    CheckIsReusableResVO checkIsReusable(CheckIsReusableReqVO param) throws RuntimeException;

    /* 비밀관리기록부 팝업 > 재분류 요청자,확인자 정보 조회 */
    List<SelectPathInfoResVO> selectReclassPathInfo(SelectPathInfoReqVO param) throws RuntimeException;

    /* 비밀관리기록부 팝업 > 파기 요청자,확인자 정보 조회 */
    List<SelectPathInfoResVO> selectDestructPathInfo(SelectPathInfoReqVO param) throws RuntimeException;

    /* 비밀관리기록부 팝업 > 이관 요청자,확인자 정보 조회 */
    List<SelectPathInfoResVO> selectTransPathInfo(SelectPathInfoReqVO param) throws RuntimeException;

    /* 비밀관리기록부 팝업 > 존안 요청자,확인자 정보 조회 */
    List<SelectPathInfoResVO> selectCnsrvPathInfo(SelectPathInfoReqVO param) throws RuntimeException;

    /* 요청서 진행상태 수정 - 최종결재 완료 */
    int updateUseReqViewApp(UpdateUseReqViewAppApproveReqVO param) throws RuntimeException;

    /* 요청서 진행상태 수정 - 실행, 권한회수, 요청서회수 */
    int updateUseReqViewAppStatus(UpdateUseReqViewAppApproveReqVO param) throws RuntimeException;

    /* 비밀관리기록부 상태값 수정*/
    int updateUseReqViewAppMgmtRegiSecstatus(UpdateUseReqViewAppApproveReqVO param) throws RuntimeException;

    /* 인쇄실행 팝업 > 인쇄한 부수 체크 */
    CheckPrtCntResVO checkPrtCnt(CheckPrtCntReqVO param) throws RuntimeException;

    /* 인쇄실행 팝업 > 인쇄부수 수정 */
    int updatePrtCnt(UpdatePrtCntReqVO param) throws RuntimeException;

    /* 인쇄/파쇄 실행완료 */
    int updateApprreqDestructReserve(UpdateUseReqPrintApproveReqVO param) throws RuntimeException;

    /* 인쇄실행완료시 실제 인쇄부수,날짜 수정 */
    int updateUseExecutePrint(UpdateUseReqPrintApproveReqVO param) throws RuntimeException;

    /* 파쇄실행완료시 실제 파쇄부수,날짜 수정 */
    int updateUseExecutePrintDestruct(UpdateUseReqPrintApproveReqVO param) throws RuntimeException;

    /* 재분류 실행 시 비밀속성부 수정 */
    int updateUseReqViewAppReclassCompleteAttr(UpdateUseReqViewAppApproveReqVO param) throws RuntimeException;

    /* 재분류 실행 시 기존 비밀관리기록부에 변경내역 수정 */
    int updateUseReqViewAppReclassCompleteRegiDel(UpdateUseReqViewAppApproveReqVO param) throws RuntimeException;

    /* 재분류 실행 시 비밀관리기록부 새로 등록 */
    int updateUseReqViewAppReclassCompleteRegiNew(UpdateUseReqViewAppApproveReqVO param) throws RuntimeException;

    /* 파기 실행완료 시 기존 비밀관리기록부에 변경내역 수정 */
    int updateUseReqViewAppDestructRegi(UpdateUseReqViewAppApproveReqVO param) throws RuntimeException;

    /* 존안 실행완료 시 기존 비밀관리기록부에 변경내역 수정 */
    int updateUseReqViewAppCnsrvRegi(UpdateUseReqViewAppApproveReqVO param) throws RuntimeException;

    /* 내부이관 실행완료 시 기존 비밀관리기록부에 변경내역 수정 */
    int updateUseReqViewAppTransRegi(UpdateUseReqViewAppApproveReqVO param) throws RuntimeException;

    /* 이관연기실행, 내부이관실행 시 이관요청 테이블 상태 수정 */
    int updateAppTransSecstatus(HashMap<String, Object> param) throws RuntimeException;

    /* 내부이관 실행 시 비밀문서테이블 이관플래그 수정 */
    int updateUseReqViewAppRdocTransFlag(UpdateUseReqViewAppApproveReqVO param) throws RuntimeException;

    /* (비밀문서사용처리 > 기안한 요청서 > 요청서회수/권한회수된 요청서 삭제) & (재사용/인쇄/반출 실행 시 열람권한이 없다면 요청서 삭제) */
    /* 요청서 삭제 */
    int deleteUseApprreq(HashMap<String, Object> param) throws RuntimeException;
    /* 요청서 경로 삭제 */
    int deleteUseApprreqPath(HashMap<String, Object> param) throws RuntimeException;
    /* 권한 삭제 */
    int deleteUseAuthCard(HashMap<String, Object> param) throws RuntimeException;
    /* 열람 요청서 삭제 */
    int deleteUseApprreqRead(HashMap<String, Object> param) throws RuntimeException;
    /* 재사용 요청서 삭제 */
    int deleteUseApprreqReuse(HashMap<String, Object> param) throws RuntimeException;
    /* 인쇄 요청서 파일 삭제 */
    int deleteUseApprreqPrintFle(HashMap<String, Object> param) throws RuntimeException;
    /* 인쇄 요청서 삭제 */
    int deleteUseApprreqPrint(HashMap<String, Object> param) throws RuntimeException;
    /* 반출 요청서 파일 삭제 */
    int deleteUseApprreqLendFle(HashMap<String, Object> param) throws RuntimeException;
    /* 반출 요청서 삭제 */
    int deleteUseApprreqLend(HashMap<String, Object> param) throws RuntimeException;
    /* 재분류 요청서 삭제 */
    int deleteUseApprreqReclass(HashMap<String, Object> param) throws RuntimeException;
    /* 파기 요청서 삭제 */
    int deleteUseApprreqDestruct(HashMap<String, Object> param) throws RuntimeException;
    /* 존안 요청서 삭제 */
    int deleteUseApprreqCnsrv(HashMap<String, Object> param) throws RuntimeException;
    /* 이관대기/이관연기/내부이관 요청서 삭제 */
    int deleteUseApprreqTrans(HashMap<String, Object> param) throws RuntimeException;

}
