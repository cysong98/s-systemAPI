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

import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import com.anylogic.ssystem.bms.com.model.SelectFaqListReqVO;
import com.anylogic.ssystem.bms.com.model.SelectFaqListResVO;
import com.anylogic.ssystem.bms.com.model.DeleteFaqBIAReqVO;
import com.anylogic.ssystem.bms.com.model.GetFaqDetailReqVO;
import com.anylogic.ssystem.bms.com.model.GetFaqDetailResVO;
import com.anylogic.ssystem.bms.com.model.InsertFaqBIAReqVO;
import com.anylogic.ssystem.bms.com.model.UpdateFaqBIAReqVO;


@Mapper
public interface BmsComFaqMapper {

    /* 게시판관리 > FAQ 관리 > FAQ 목록 조회 */
    List<SelectFaqListResVO> selectFaqList(SelectFaqListReqVO param) throws RuntimeException;

    /* 게시판관리 > FAQ 관리 > FAQ 삭제 */
    int deleteFaqBIA(DeleteFaqBIAReqVO param) throws RuntimeException;

    /* 게시판관리 > FAQ 관리 > FAQ 상세 조회 */
    GetFaqDetailResVO getFaqDetail(GetFaqDetailReqVO param) throws RuntimeException;

    /* 게시판관리 > FAQ 관리 > FAQ 등록 */
    int insertFaqBIA(InsertFaqBIAReqVO param) throws RuntimeException;

    /* 게시판관리 > FAQ 관리 > FAQ 수정 */
    int updateFaqBIA(UpdateFaqBIAReqVO param) throws RuntimeException;

    /* 게시판관리 > FAQ 관리 > FAQ 조회수 증가 */
    void addSelCnt(UpdateFaqBIAReqVO param) throws RuntimeException;
}
