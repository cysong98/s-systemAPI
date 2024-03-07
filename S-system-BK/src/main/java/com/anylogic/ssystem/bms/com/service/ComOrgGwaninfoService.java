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

import com.anylogic.ssystem.bms.com.model.SelectGwanListReqVO;
import com.anylogic.ssystem.bms.com.model.SelectGwanListResVO;
import java.util.List;

import com.anylogic.ssystem.common.file.model.ImageVO;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.com.model.DeleteGwanBIAReqVO;
import com.anylogic.ssystem.bms.com.model.GetGwanDetailReqVO;
import com.anylogic.ssystem.bms.com.model.GetGwanDetailResVO;
import com.anylogic.ssystem.bms.com.model.InsertGwanBIAReqVO;
import com.anylogic.ssystem.bms.com.model.UpdateGwanBIAReqVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


public interface ComOrgGwaninfoService {

    /* 관인/서명인 이미지 조회 */
    public ImageVO getImage(String fileName);

    /* 조직관리 > 관인관리 > 관인 목록 */
    public PageInfo<SelectGwanListResVO> selectGwanList(SelectGwanListReqVO param);

    /* 조직관리 > 관인관리 > 관인 삭제 */
    public List<Integer> deleteGwanBIA(List<DeleteGwanBIAReqVO> param);

    /* 조직관리 > 관인관리 > 관인 상세 조회 */
    public GetGwanDetailResVO getGwanDetail(GetGwanDetailReqVO param);

    /* 조직관리 > 관인관리 > 관인 등록 */
    public int insertGwanBIA(List<MultipartFile> multipartFiles, InsertGwanBIAReqVO param);

    /* 조직관리 > 관인관리 > 관인 수정 */
    public int updateGwanBIA(List<MultipartFile> multipartFiles, UpdateGwanBIAReqVO param);

}
