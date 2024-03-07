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

import com.anylogic.ssystem.bms.com.model.SelectFaqListReqVO;
import com.anylogic.ssystem.bms.com.model.SelectFaqListResVO;
import java.util.List;
import com.anylogic.ssystem.bms.com.model.DeleteFaqBIAReqVO;
import com.anylogic.ssystem.bms.com.model.GetFaqDetailReqVO;
import com.anylogic.ssystem.bms.com.model.GetFaqDetailResVO;
import com.anylogic.ssystem.bms.com.model.InsertFaqBIAReqVO;
import com.anylogic.ssystem.bms.com.model.UpdateFaqBIAReqVO;
import com.github.pagehelper.PageInfo;

import java.util.*;


public interface BmsComFaqService {

    /* 게시판관리 > FAQ 관리 > FAQ 목록 조회 */
    public PageInfo<SelectFaqListResVO> selectFaqList(SelectFaqListReqVO param);

    /* 게시판관리 > FAQ 관리 > FAQ 삭제 */
    public List<Integer> deleteFaqBIA(List<DeleteFaqBIAReqVO> param);

    /* 게시판관리 > FAQ 관리 > FAQ 상세 조회 */
    public GetFaqDetailResVO getFaqDetail(GetFaqDetailReqVO param);

    /* 게시판관리 > FAQ 관리 > FAQ 등록 */
    public int insertFaqBIA(InsertFaqBIAReqVO param);

    /* 게시판관리 > FAQ 관리 > FAQ 수정 */
    public int updateFaqBIA(UpdateFaqBIAReqVO param);

}
