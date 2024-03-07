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
public interface BmsComQnaMapper {

    /* 게시판관리 > Q&A 관리 > Q&A 목록 조회 */
    List<SelectQnaListResVO> selectQnaList(SelectQnaListReqVO param) throws RuntimeException;

    /* 게시판관리 > Q&A 관리 > Q&A 삭제 */
    int deleteQna(DeleteQnaReqVO param) throws RuntimeException;

    /* 게시판관리 > Q&A 관리 > Q&A 상세 조회 */
    GetQnaDetailResVO getQnaDetail(GetQnaDetailReqVO param) throws RuntimeException;

    /* 게시판관리 > Q&A 관리 > Q&A 등록 */
    int insertQna(InsertQnaReqVO param) throws RuntimeException;

    /* 게시판관리 > Q&A 관리 > Q&A 수정 */
    int updateQna(UpdateQnaReqVO param) throws RuntimeException;

    /* 게시판관리 > Q&A 관리 > 마지막 시퀀스 번호 획득 */
    GetSeqResVO getSeq() throws RuntimeException;

}
