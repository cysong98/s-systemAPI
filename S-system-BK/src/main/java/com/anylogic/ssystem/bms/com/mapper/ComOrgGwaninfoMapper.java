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
public interface ComOrgGwaninfoMapper {

    /* 조직관리 > 관인관리 > 관인 목록 */
    List<SelectGwanListResVO> selectGwanList(SelectGwanListReqVO param) throws RuntimeException;

    /* 조직관리 > 관인관리 > 삭제할 관인 이미지 정보 조회 */
    List<DeleteGwanBIASelectFileResVO> deleteGwanBIASelectFile(DeleteGwanBIASelectFileReqVO param);

    /* 조직관리 > 관인관리 > 관인 삭제 */
    int deleteGwanBIA(DeleteGwanBIAReqVO param) throws RuntimeException;

    /* 조직관리 > 관인관리 > 관인 상세 조회 */
    GetGwanDetailResVO getGwanDetail(GetGwanDetailReqVO param) throws RuntimeException;

    /* 조직관리 > 관인관리 > 관인 이미지 상세 조회 */
    List<GetGwanDetailSelectFileResVO> getGwanDetailSelectFile(GetGwanDetailSelectFileReqVO param) throws RuntimeException;

    /* 조직관리 > 관인관리 > 관인 이미지 등록 */
    int insertComImage(InsertComImageReqVO param) throws RuntimeException;

    /* 조직관리 > 관인관리 > 관인 등록 */
    int insertGwanBIA(InsertGwanBIAReqVO param) throws RuntimeException;

    /* 조직관리 > 관인관리 > 관인 이미지 삭제 */
    int deleteComImage(DeleteComImageReqVO param) throws RuntimeException;

    /* 조직관리 > 관인관리 > 관인 수정 */
    int updateGwanBIA(UpdateGwanBIAReqVO param) throws RuntimeException;

    /* 파일 경로 조회 */
    String getFilePath(String fileName);

}
