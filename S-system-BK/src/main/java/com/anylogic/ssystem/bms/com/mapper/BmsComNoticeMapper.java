/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.mapper;

import com.anylogic.ssystem.bms.com.model.*;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;


@Mapper
public interface BmsComNoticeMapper {

    /* 게시판관리 > 공지사항 관리 > 공지사항 목록 조회 */
    List<SelectNoticeListResVO> selectNoticeList(SelectNoticeListReqVO param) throws RuntimeException;

    /* 게시판관리 > 공지사항 관리 > 공지사항 삭제 */
    int deleteNoticeBIA(DeleteNoticeBIAReqVO param) throws RuntimeException;

    /* 게시판관리 > 공지사항 관리 > 공지사항 상세 조회 */
    GetNoticeDetailResVO getNoticeDetail(GetNoticeDetailReqVO param) throws RuntimeException;

    /* 게시판관리 > 공지사항 관리 > 공지사항 등록 */
    int insertNoticeBIA(InsertNoticeBIAReqVO param) throws RuntimeException;

    /* 게시판관리 > 공지사항 관리 > 공지사항 수정 */
    int updateNoticeBIA(UpdateNoticeBIAReqVO param) throws RuntimeException;

    /* 게시판관리 > 공지사항 관리 > 공지사항 조회수 증가 */
    void addSelCnt(UpdateNoticeBIAReqVO param) throws RuntimeException;
}
