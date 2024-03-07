/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.service;

import com.anylogic.ssystem.bms.use.model.*;

import java.util.List;
import com.github.pagehelper.PageInfo;

import java.util.*;


public interface BmsDctApprreqService {

    /* 비밀문서 사용(처리할, 기안한, 처리한, 요청서완료처리) 목록 조회 */
    public PageInfo<SelectUseReqListResVO> selectUseReqList(SelectUseReqListReqVO param);

    /* 각 요청서 상세화면 조회 */
    public SelectUseReqViewDetailResVO selectUseReqViewDetail(SelectUseReqViewDetailReqVO param);

    /* 각 요청서 실행 처리 & 요청서회수 & 권한회수 */
    public int updateUseReqViewAppStatusBIA(UpdateUseReqViewAppApproveReqVO param);

    /* (비밀문서사용처리 > 기안한 요청서 > 요청서회수/권한회수된 요청서 삭제) & (재사용/인쇄/반출 실행 시 열람권한이 없다면 요청서 삭제) */
    public int deleteUseReqList(List<DeleteUseReqListReqVO> param);

    /* 인쇄실행 팝업 > 인쇄한 부수 체크 */
    public CheckPrtCntResVO checkPrtCnt(CheckPrtCntReqVO param);

    /* 인쇄실행 팝업 > 인쇄부수 수정 */
    public int updatePrtCnt(UpdatePrtCntReqVO param);

    /* 재사용 가능 여부 체크*/
    public CheckIsReusableResVO checkIsReusable(CheckIsReusableReqVO param);

    /* 비밀관리기록부 팝업 > 재분류 요청자,확인자 정보 조회 */
    public List<SelectPathInfoResVO> selectReclassPathInfo(SelectPathInfoReqVO param);

    /* 비밀관리기록부 팝업 > 파기 요청자,확인자 정보 조회 */
    public List<SelectPathInfoResVO> selectDestructPathInfo(SelectPathInfoReqVO param);

    /* 비밀관리기록부 팝업 > 이관 요청자,확인자 정보 조회 */
    public List<SelectPathInfoResVO> selectTransPathInfo(SelectPathInfoReqVO param);

    /* 비밀관리기록부 팝업 > 존안 요청자,확인자 정보 조회 */
    public List<SelectPathInfoResVO> selectCnsrvPathInfo(SelectPathInfoReqVO param);

}
