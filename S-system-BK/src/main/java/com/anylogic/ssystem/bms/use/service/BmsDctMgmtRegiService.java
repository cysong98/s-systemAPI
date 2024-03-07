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


public interface BmsDctMgmtRegiService {

    /* 예고문 도래 알림 목록 조회 */
    public List<SelectAlertResVO> selectAlert(SelectAlertReqVO param);

    /* 비밀문서사용요청 > 열람요청 > 열람요청 목록 조회 */
    public PageInfo<SelectUseDigitalViewResVO> selectUseDigitalView(SelectUserReqestsListReqVO param);

    /* 비밀문서사용요청 > 재사용요청 > 재사용요청 목록 조회 */
    public PageInfo<SelectReUseResVO> selectReUse(SelectUserReqestsListReqVO param);

    /* 비밀문서사용요청 > 인쇄요청 > 인쇄요청 목록 조회 */
    public PageInfo<SelectUsePrintResVO> selectUsePrint(SelectUserReqestsListReqVO param);

    /* 비밀문서사용요청 > 반출요청 > 반출요청 목록 조회 */
    public PageInfo<SelectUseTakeOutResVO> selectUseTakeOut(SelectUserReqestsListReqVO param);

    /* 예고문도래목록 목록 조회 */
    public PageInfo<SelectComingListResVO> selectComingList(SelectComingListReqVO param);

    /* 각 요청서의 요청내용 > 요청된 문서들 정보 조회 */
    public List<GetDctMgmtRegiResVO> getDctMgmtRegi(GetDctMgmtRegiReqVO param);

    /* 각 요청서의 요청내용 > 요청된 문서들 정보 조회 - 예고문정보 포함 */
    public List<GetDctMgmtRegiAttrResVO> getDctMgmtRegiAttr(GetDctMgmtRegiReqVO param);

    /* 각 요청서의 요청내용 > 요청된 문서들 정보 조회 - 첨부파일 포함 */
    public List<GetDctMgmtRegiResVO> getDctMgmtRegiPrint(GetDctMgmtRegiReqVO param);

}
