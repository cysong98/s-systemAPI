/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.use.service;


import com.anylogic.ssystem.bms.dct.mapper.BmsDctHistCardMapper;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;
import com.anylogic.ssystem.bms.dct.model.InsertHistoryReqVO;
import com.anylogic.ssystem.bms.use.model.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqReadMapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqMapper;
import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqPathMapper;


@Slf4j
@RequiredArgsConstructor
@Service("BmsDctApprreqReadService")
@Transactional
public class BmsDctApprreqReadServiceImpl implements BmsDctApprreqReadService {

    @Autowired
    private BmsDctHistCardMapper bmsDctHistCardMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;

    @Autowired
    private BmsDctApprreqReadMapper bmsDctApprreqReadMapper;

    @Autowired
    private BmsDctApprreqMapper bmsDctApprreqMapper;

    @Autowired
    private BmsDctApprreqPathMapper bmsDctApprreqPathMapper;

    /* 비밀문서사용요청 > 열람요청 > 전자 열람요청서 등록 */
    @Transactional
    @Override
    public int insertUseDigitalViewReq(InsertUseDigitalViewReqReqVO param){
        int resInsertUseDigitalViewReq = 0;
        String resSysGuid = "";

        try {

            // 00. CREATE SYS_GUID STRING
            resSysGuid = bmsDctRdocMapper.getSysGuid();
            String resApprreqid = "APP" + resSysGuid;

            Date today = new Date();
            Locale currentLocale = new Locale("KOREAN", "KOREA");
            String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

            // 01. INSERT BMS_DCT_APPRREQ TABLE
            int resInsertDctApprReq = 0;
            param.setApprreqid(resApprreqid);
            InsertDctApprReqReqVO insertDctApprReqParam = getApprreqVO(param, formatter.format(today));
            bmsDctApprreqMapper.insertDctApprReq(insertDctApprReqParam);


            // 02. INSERT BMS_DCT_APPRREQ_PATH TABLE
            int resInsertDctApprReqPath = 0;
            for(InsertUseDigitalViewReqReqVO tempReqVO : param.getPathList()){
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertDctApprReqPathReqVO insertDctApprReqPathParam = getApprreqPathVO(tempReqVO, formatter.format(today));
                resInsertDctApprReqPath += bmsDctApprreqPathMapper.insertDctApprReqPath(insertDctApprReqPathParam);
            }


            // 03. INSERT BMS_DCT_APPRREQ_READ TABLE
            for(InsertUseDigitalViewReqReqVO tempReqVO : param.getReadList()) {
                String begintdt = tempReqVO.getReqreadbegindt().replaceAll("-","")+ "000000";
                String enddt = tempReqVO.getReqreadenddt().replaceAll("-","")+ "235959";
                tempReqVO.setReqreadbegindt(begintdt);
                tempReqVO.setReqreadenddt(enddt);
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertUseDigitalViewReqReqVO insertUseDigitalViewReqParam = getApprreqReadVO(tempReqVO);
                resInsertUseDigitalViewReq += bmsDctApprreqReadMapper.insertUseDigitalViewReq(insertUseDigitalViewReqParam);
            }

            // 04. INSERT BMS_DCT_HIST_CARD TABLE
            int resInsertHistory = 0;
            String histSeq = bmsDctHistCardMapper.getHistSeq();
            param.setHistgubun("05"); // 열람
            param.setHiststatus("010"); // 요청
            for(String resDocid : param.getDocid()){
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resDocid, histSeq, formatter.format(today));
                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertUseDigitalViewReq;
    }

    // 01. INSERT BMS_DCT_APPRREQ TABLE
    private InsertDctApprReqReqVO getApprreqVO(InsertUseDigitalViewReqReqVO vo, String indt){
        InsertDctApprReqReqVO build = InsertDctApprReqReqVO.builder()
                .apprreqid(vo.getApprreqid())
                .reqtype(vo.getReqtype())
                .reqdt(indt)
                .reqttl(vo.getReqttl())
                .reqregdt(indt)
                .reqcontents(vo.getReqcontents())
                .reqid(vo.getReqid())
                .reqname(vo.getReqname())
                .reqdeptid(vo.getReqdeptid())
                .reqdeptname(vo.getReqdeptname())
                .reqstatus(vo.getReqstatus())
                .grantflag(vo.getGrantflag())
                .build();
        return build;
    }

    // 02. INSERT BMS_DCT_APPRREQ_PATH TABLE
    private InsertDctApprReqPathReqVO getApprreqPathVO(InsertUseDigitalViewReqReqVO vo, String indt){
        InsertDctApprReqPathReqVO build = InsertDctApprReqPathReqVO.builder()
                .apprreqid(vo.getApprreqid())
                .apprreqseq(vo.getApprreqseq())
                .apprtype(vo.getApprtype())
                .apprgubun(vo.getApprgubun())
                .apprstatus(vo.getApprstatus())
                .apprid(vo.getApprid())
                .apprname(vo.getApprname())
                .apprdeptid(vo.getApprdeptid())
                .apprdeptname(vo.getApprdeptname())
                .apprdutyname(vo.getApprdutyname())
                .appropinion(vo.getAppropinion())
                .electsign(vo.getElectsign())
                .rgstid(vo.getRgstid())
                .rgstname(vo.getRgstname())
                .rgstdt(indt)
                .build();
        return build;
    }

    // 03. INSERT BMS_DCT_APPRREQ_READ TABLE
    private InsertUseDigitalViewReqReqVO getApprreqReadVO(InsertUseDigitalViewReqReqVO vo){
        InsertUseDigitalViewReqReqVO build = InsertUseDigitalViewReqReqVO.builder()
                .apprreqid(vo.getApprreqid())
                .mgmtid(vo.getMgmtid())
                .reqreadbegindt(vo.getReqreadbegindt())
                .reqreadenddt(vo.getReqreadenddt())
                .grantreadbegindt(vo.getGrantreadbegindt())
                .grantreadenddt(vo.getGrantreadenddt())
                .grantflag(vo.getGrantflag())
                .readcharge(vo.getReadcharge())
                .build();
        return build;
    }

    // 04. INSERT BMS_DCT_HIST_CARD TABLE
    private InsertHistoryReqVO getHistoryVO (InsertUseDigitalViewReqReqVO vo, String resDocid, String histSeq, String indt){
        InsertHistoryReqVO build = InsertHistoryReqVO.builder()
                .docid(resDocid)
                .histdt(indt)
                .histseq(new BigDecimal(histSeq))
                .apprreqid(vo.getApprreqid())
                .histgubun(vo.getHistgubun())
                .deptid(vo.getReqdeptid())
                .deptname(vo.getReqdeptname())
                .dutyname(vo.getDutyname())
                .userid(vo.getReqid())
                .username(vo.getReqname())
                .histstatus(vo.getHiststatus())
                .etc(vo.getEtc())
                .electsignid(vo.getElectsignid())
                .build();
        return build;
    }


    /* 비밀문서사용요청 > 열람요청 > 비전자 열람요청서 등록 */
    @Transactional
    @Override
    public int insertUseNonDigitalViewReq(InsertUseDigitalViewReqReqVO param){
        int resInsertUseNonDigitalViewReq = 0;
        String resSysGuid = "";
        try {

            // 00. CREATE SYS_GUID STRING
            resSysGuid = bmsDctRdocMapper.getSysGuid();
            String resApprreqid = "APP" + resSysGuid;

            Date today = new Date();
            Locale currentLocale = new Locale("KOREAN", "KOREA");
            String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

            // 01. INSERT BMS_DCT_APPRREQ TABLE
            int resInsertDctApprReq = 0;
            param.setApprreqid(resApprreqid);
            InsertDctApprReqReqVO insertDctApprReqParam = getApprreqVO(param, formatter.format(today));
            bmsDctApprreqMapper.insertDctApprReq(insertDctApprReqParam);


            // 02. INSERT BMS_DCT_APPRREQ_PATH TABLE
            int resInsertDctApprReqPath = 0;
            for(InsertUseDigitalViewReqReqVO tempReqVO : param.getPathList()){
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertDctApprReqPathReqVO insertDctApprReqPathParam = getApprreqPathVO(tempReqVO, formatter.format(today));
                resInsertDctApprReqPath += bmsDctApprreqPathMapper.insertDctApprReqPath(insertDctApprReqPathParam);
            }


            // 03. INSERT BMS_DCT_APPRREQ_READ TABLE
            for(InsertUseDigitalViewReqReqVO tempReqVO : param.getReadList()) {
                String begintdt = tempReqVO.getReqreadbegindt().replaceAll("-","")+ "000000";
                String enddt = tempReqVO.getReqreadenddt().replaceAll("-","")+ "235959";
                tempReqVO.setReqreadbegindt(begintdt);
                tempReqVO.setReqreadenddt(enddt);
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertUseDigitalViewReqReqVO insertUseDigitalViewReqParam = getApprreqReadVO(tempReqVO);
                resInsertUseNonDigitalViewReq += bmsDctApprreqReadMapper.insertUseNonDigitalViewReq(insertUseDigitalViewReqParam);
            }

            // 04. INSERT BMS_DCT_HIST_CARD TABLE
            int resInsertHistory = 0;
            String histSeq = bmsDctHistCardMapper.getHistSeq();
            param.setHistgubun("05"); // 열람
            param.setHiststatus("010"); // 요청
            for(String resDocid : param.getDocid()){
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resDocid, histSeq, formatter.format(today));
                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
            }


        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertUseNonDigitalViewReq;
    }


}
