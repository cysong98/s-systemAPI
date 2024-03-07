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
import com.anylogic.ssystem.bms.use.model.*;

import org.apache.ibatis.annotations.Mapper;
import java.util.*;


@Mapper
public interface BmsDctMgmtRegiMapper {

    /* 예고문 도래 알림 목록 조회 */
    List<SelectAlertResVO> selectAlert(SelectAlertReqVO param) throws RuntimeException;

    /* 비밀문서사용요청 > 열람요청 > 열람요청 목록 조회 */
    List<SelectUseDigitalViewResVO> selectUseDigitalView(SelectUserReqestsListReqVO param) throws RuntimeException;

    /* 비밀문서사용요청 > 재사용요청 > 재사용요청 목록 조회 */
    List<SelectReUseResVO> selectReUse(SelectUserReqestsListReqVO param) throws RuntimeException;

    /* 비밀문서사용요청 > 인쇄요청 > 인쇄요청 목록 조회 */
    List<SelectUsePrintResVO> selectUsePrint(SelectUserReqestsListReqVO param) throws RuntimeException;

    /* 비밀문서사용요청 > 반출요청 > 반출요청 목록 조회 */
    List<SelectUseTakeOutResVO> selectUseTakeOut(SelectUserReqestsListReqVO param) throws RuntimeException;

    /* 예고문도래목록 목록 조회 */
    List<SelectComingListResVO> selectComingList(SelectComingListReqVO param) throws RuntimeException;

    /* 각 요청서의 요청내용 > 요청된 문서들 정보 조회 */
    List<GetDctMgmtRegiResVO> getDctMgmtRegi(GetDctMgmtRegiReqVO param) throws RuntimeException;

    /* 각 요청서의 요청내용 > 요청된 문서들 정보 조회 - 예고문정보 포함 */
    List<GetDctMgmtRegiAttrResVO> getDctMgmtRegiAttr(GetDctMgmtRegiReqVO param) throws RuntimeException;

    /* 각 요청서의 요청내용 > 요청된 문서들 정보 조회 - 첨부파일 포함 */
    List<GetDctMgmtRegiResVO> getDctMgmtRegiPrint(GetDctMgmtRegiReqVO param) throws RuntimeException;

    /* 비밀관리기록부 상태값, 업데이트이력 수정 */
    int updateComingReqsMgmt(UpdateComingReqsMgmtReqVO param) throws RuntimeException;

}
