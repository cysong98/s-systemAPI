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
public interface BmsComFaqAuthorMapper {

    /* 게시판관리 > FAQ 관리 > FAQ AUTHOR 조회 */
    List<SelectFaqAuthorResVO> selectFaqAuthor(SelectFaqAuthorReqVO param) throws RuntimeException;

    /* 게시판관리 > FAQ 관리 > FAQ AUTHOR 삭제 */
    int deleteFaqAuthor(DeleteFaqAuthorReqVO param) throws RuntimeException;

    /* 게시판관리 > FAQ 관리 > FAQ AUTHOR 등록 */
    int insertFaqAuthor(InsertFaqAuthorReqVO param) throws RuntimeException;

    /* 부서ID로 FAQ ID 조회 */
    List<SelectFaqAuthorResVO> selectFaqAuthorByDeptid(SelectFaqAuthorReqVO param) throws RuntimeException;

}
