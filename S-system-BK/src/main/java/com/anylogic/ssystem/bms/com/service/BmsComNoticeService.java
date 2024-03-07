/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.service;

import com.anylogic.ssystem.bms.com.model.SelectNoticeListReqVO;
import com.anylogic.ssystem.bms.com.model.SelectNoticeListResVO;
import java.util.List;
import com.anylogic.ssystem.bms.com.model.DeleteNoticeBIAReqVO;
import com.anylogic.ssystem.bms.com.model.GetNoticeDetailReqVO;
import com.anylogic.ssystem.bms.com.model.GetNoticeDetailResVO;
import com.anylogic.ssystem.bms.com.model.InsertNoticeBIAReqVO;
import com.anylogic.ssystem.bms.com.model.UpdateNoticeBIAReqVO;
import com.github.pagehelper.PageInfo;

import java.util.*;


public interface BmsComNoticeService {


    /* 게시판관리 > 공지사항 관리 > 공지사항 목록 조회 */
    public PageInfo<SelectNoticeListResVO> selectNoticeList(SelectNoticeListReqVO param);

    /* 게시판관리 > 공지사항 관리 > 공지사항 삭제 */
    public List<Integer> deleteNoticeBIA(List<DeleteNoticeBIAReqVO> param);

    /* 게시판관리 > 공지사항 관리 > 공지사항 상세 조회 */
    public GetNoticeDetailResVO getNoticeDetail(GetNoticeDetailReqVO param);

    /* 게시판관리 > 공지사항 관리 > 공지사항 등록 */
    public int insertNoticeBIA(InsertNoticeBIAReqVO param);

    /* 게시판관리 > 공지사항 관리 > 공지사항 수정 */
    public int updateNoticeBIA(UpdateNoticeBIAReqVO param);

}
