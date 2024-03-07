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

import com.anylogic.ssystem.bms.com.model.SelectQnaListReqVO;
import com.anylogic.ssystem.bms.com.model.SelectQnaListResVO;
import java.util.List;
import com.anylogic.ssystem.bms.com.model.DeleteQnaReqVO;
import com.anylogic.ssystem.bms.com.model.GetQnaDetailReqVO;
import com.anylogic.ssystem.bms.com.model.GetQnaDetailResVO;
import com.anylogic.ssystem.bms.com.model.InsertQnaReqVO;
import com.anylogic.ssystem.bms.com.model.UpdateQnaReqVO;
import com.github.pagehelper.PageInfo;

import java.util.*;


public interface BmsComQnaService {

    /* 게시판관리 > Q&A 관리 > Q&A 목록 조회 */
    public PageInfo<SelectQnaListResVO> selectQnaList(SelectQnaListReqVO param);

    /* 게시판관리 > Q&A 관리 > Q&A 삭제 */
    public List<Integer> deleteQna(List<DeleteQnaReqVO> param);

    /* 게시판관리 > Q&A 관리 > Q&A 상세 조회 */
    public GetQnaDetailResVO getQnaDetail(GetQnaDetailReqVO param);

    /* 게시판관리 > Q&A 관리 > Q&A 등록 */
    public int insertQna(InsertQnaReqVO param);

    /* 게시판관리 > Q&A 관리 > Q&A 수정 */
    public int updateQna(UpdateQnaReqVO param);

}
