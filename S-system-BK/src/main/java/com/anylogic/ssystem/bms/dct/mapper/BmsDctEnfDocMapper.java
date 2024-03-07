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

import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import com.anylogic.ssystem.bms.dct.model.InsertEnforceCardReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectReturnListReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectReturnListResVO;
import com.anylogic.ssystem.bms.dct.model.SelectDctDistributeListReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectDctDistributeListResVO;
import com.anylogic.ssystem.bms.dct.model.SelectReceiptListReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectReceiptListResVO;
import com.anylogic.ssystem.bms.dct.model.SelectRecvHistoryReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectRecvHistoryResVO;
import com.anylogic.ssystem.bms.dct.model.SelectNotAssignTaskListReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectNotAssignTaskListResVO;
import com.anylogic.ssystem.bms.dct.model.UpdateEnfDocRepathReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectReprocessListReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectReprocessListResVO;
import com.anylogic.ssystem.bms.dct.model.GetReprocessReqVO;
import com.anylogic.ssystem.bms.dct.model.GetReprocessResVO;
import com.anylogic.ssystem.bms.dct.model.SelectSendBackReadyListReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectSendBackReadyListResVO;


@Mapper
public interface BmsDctEnfDocMapper {

    /* 시행비밀정보 등록 */
    int insertEnforceCard(InsertEnforceCardReqVO param) throws RuntimeException;

    /* 발송 > 반송함 > 반송함 목록 조회 */
    List<SelectReturnListResVO> selectReturnList(SelectReturnListReqVO param) throws RuntimeException;

    /* 접수 > 배부함 > 배부함 목록 조회 */
    List<SelectDctDistributeListResVO> selectDctDistributeList(SelectDctDistributeListReqVO param) throws RuntimeException;

    /* 접수 > 접수대기함 > 접수대기함 목록 조회 */
    List<SelectReceiptListResVO> selectReceiptList(SelectReceiptListReqVO param) throws RuntimeException;

    /* 시행비밀정보 조회 문서&유저 */
    List<SelectReceiptListResVO> getReceiptList(SelectReceiptListReqVO param) throws RuntimeException;

    /* 시행비밀정보 조회 - 문서 */
    List<SelectRecvHistoryResVO> selectRecvHistory(SelectRecvHistoryReqVO param) throws RuntimeException;

    /* 접수한 문서 문서처리 시 상태값 수정 */
    int updateEnfDocRepath(UpdateEnfDocRepathReqVO param) throws RuntimeException;

    /* 접수 > 재지정함 > 재지정함 목록 조회 */
    List<SelectReprocessListResVO> selectReprocessList(SelectReprocessListReqVO param) throws RuntimeException;

    /* 비전자 접수 문서 상세 > 접수 정보 조회 */
    GetReprocessResVO getReprocess(GetReprocessReqVO param) throws RuntimeException;

    /* 접수 > 접수현황함 > 접수현황함 목록 조회 */
    List<SelectReceiptListResVO> selectReceiptCurrentList(SelectReceiptListReqVO param) throws RuntimeException;

    /* 비전자접수 > 접수대기상세 > 문서처리 (원본복사하여 저장) */
    int insertEnforceCardCopy(InsertEnforceCardReqVO param) throws RuntimeException;
}
