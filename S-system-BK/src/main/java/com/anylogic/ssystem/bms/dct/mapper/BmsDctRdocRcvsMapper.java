/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.mapper;

import com.anylogic.ssystem.bms.dct.model.*;
import com.anylogic.ssystem.bms.trn.model.UpdateMgmtRegiReqVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;


@Mapper
public interface BmsDctRdocRcvsMapper {

    /* 열람권자 조회 */
    List<SelectDctReaderResVO> selectDctReader(SelectDctReaderReqVO param) throws RuntimeException;

    /* 재사용/인쇄/반출 시 열람권한 보유중인지 체크 */
    HasViewingPermissionResVO hasViewingPermission(HasViewingPermissionReqVO param) throws RuntimeException;

    /* 열람권자 등록 */
    int insertDocIdBmsDctRdocRcvsList(InsertDocIdBmsDctRdocRcvsListReqVO param) throws RuntimeException;

    /* 열람권자 삭제 */
    int deleteDocIdBmsDctRdocRcvsList(DeleteDocIdBmsDctRdocRcvsListReqVO param) throws RuntimeException;

    /* 열람권자 수정 */
    int updateRdocRcvs(InsertDocIdBmsDctRdocRcvsListReqVO param) throws RuntimeException;
}
