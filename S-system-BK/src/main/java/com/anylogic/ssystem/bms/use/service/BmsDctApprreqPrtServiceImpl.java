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

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqPrtMapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqMapper;
import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqPathMapper;
import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqPrtFleMapper;


@Slf4j
@RequiredArgsConstructor
@Service("BmsDctApprreqPrtService")
@Transactional
public class BmsDctApprreqPrtServiceImpl implements BmsDctApprreqPrtService {

    @Autowired
    private BmsDctHistCardMapper bmsDctHistCardMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;

    @Autowired
    private BmsDctApprreqPrtMapper bmsDctApprreqPrtMapper;

    @Autowired
    private BmsDctApprreqMapper bmsDctApprreqMapper;

    @Autowired
    private BmsDctApprreqPathMapper bmsDctApprreqPathMapper;

    @Autowired
    private BmsDctApprreqPrtFleMapper bmsDctApprreqPrtFleMapper;


    /* 비밀문서사용요청 > 인쇄요청 > 전자 인쇄요청서 등록 */
    @Transactional
    @Override
    public int insertUseDigitalPrint(InsertUseDigitalPrintReqVO param){
        int resInsertUseDigitalPrint = 0;
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
            resInsertDctApprReq = bmsDctApprreqMapper.insertDctApprReq(insertDctApprReqParam);


            // 02. INSERT BMS_DCT_APPRREQ_PATH TABLE
            int resInsertDctApprReqPath = 0;
            for(InsertUseDigitalPrintReqVO tempReqVO : param.getPathList()) {
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertDctApprReqPathReqVO insertDctApprReqPathParam = getApprreqPathVO(tempReqVO, formatter.format(today));
                resInsertDctApprReqPath += bmsDctApprreqPathMapper.insertDctApprReqPath(insertDctApprReqPathParam);
            }

            // 03. INSERT BMS_DCT_APPRREQ_PRT TABLE
            int resInsertUseDigitalPrintFile = 0;
            for(InsertUseDigitalPrintReqVO tempReqVO : param.getPrintList()) {
                tempReqVO.setReqdestructdt(tempReqVO.getReqdestructdt().replaceAll("-","")+ "235959");
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertUseDigitalPrintReqVO insertUseDigitalPrintReqParam = getApprreqPrtVO(tempReqVO);
                resInsertUseDigitalPrint += bmsDctApprreqPrtMapper.insertUseDigitalPrint(insertUseDigitalPrintReqParam);

                // 04. INSERT BMS_DCT_APPRREQ_PRT_FILE TABLE
                for(InsertUseDigitalPrintReqVO fileReqVO : tempReqVO.getFileList()){
                    fileReqVO.setApprreqid(param.getApprreqid());
                    InsertPrtFileReqVO insertPrtFileReqParam = getApprreqPrtFileVO(fileReqVO);
                    resInsertUseDigitalPrintFile += bmsDctApprreqPrtFleMapper.insertPrtFile(insertPrtFileReqParam);
                }
            }

            // 05. INSERT BMS_DCT_HIST_CARD TABLE
            int resInsertHistory = 0;
            String histSeq = bmsDctHistCardMapper.getHistSeq();
            param.setHistgubun("06"); // 인쇄
            param.setHiststatus("010"); // 요청
            for(String resDocid : param.getDocid()){
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resDocid, histSeq, formatter.format(today));
                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertUseDigitalPrint;
    }

    // 01. INSERT BMS_DCT_APPRREQ TABLE
    private InsertDctApprReqReqVO getApprreqVO(InsertUseDigitalPrintReqVO vo, String indt){
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
    private InsertDctApprReqPathReqVO getApprreqPathVO(InsertUseDigitalPrintReqVO vo, String indt){
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

    // 03. INSERT BMS_DCT_APPRREQ_PRT TABLE
    private InsertUseDigitalPrintReqVO getApprreqPrtVO(InsertUseDigitalPrintReqVO vo){
        InsertUseDigitalPrintReqVO build = InsertUseDigitalPrintReqVO.builder()
                .apprreqid(vo.getApprreqid())
                .mgmtid(vo.getMgmtid())
                .reqprtcnt(vo.getReqprtcnt())
                .reqdestructdt(vo.getReqdestructdt())
                .grantprtcnt(vo.getGrantprtcnt())
                .grantdestructdt(vo.getGrantdestructdt())
                .grantflag(vo.getGrantflag())
                .prtcharge(vo.getPrtcharge())
                .build();
        return build;
    }

    // 04. INSERT BMS_DCT_APPRREQ_PRT_FILE TABLE
    private InsertPrtFileReqVO getApprreqPrtFileVO(InsertUseDigitalPrintReqVO vo){
        InsertPrtFileReqVO build = InsertPrtFileReqVO.builder()
                .apprreqid(vo.getApprreqid())
                .mgmtid(vo.getMgmtid())
                .fleorder(vo.getFleorder())
                .gubun(vo.getGubun())
                .fleid(vo.getFleid())
                .sfilename(vo.getSfilename())
                .flettl(vo.getFlettl())
                .flesize(vo.getFlesize())
                .grantflag(vo.getGrantflag())
                .prtcnt(vo.getPrtcnt())
                .build();
        return build;
    }

    // 05. INSERT BMS_DCT_HIST_CARD TABLE
    private InsertHistoryReqVO getHistoryVO (InsertUseDigitalPrintReqVO vo, String resDocid, String histSeq, String indt){
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


    /* (반영X) 비밀문서사용요청 > 인쇄요청 > 비전자 인쇄요청서 등록 */
    @Transactional
    @Override
    public int insertUseNonDigitalPrint(InsertUseDigitalPrintReqVO param){
        int resInsertUseNonDigitalPrint = 0;
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
            resInsertDctApprReq = bmsDctApprreqMapper.insertDctApprReq(insertDctApprReqParam);


            // 02. INSERT BMS_DCT_APPRREQ_PATH TABLE
            int resInsertDctApprReqPath = 0;
            for(InsertUseDigitalPrintReqVO tempReqVO : param.getPathList()) {
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertDctApprReqPathReqVO insertDctApprReqPathParam = getApprreqPathVO(tempReqVO, formatter.format(today));
                resInsertDctApprReqPath += bmsDctApprreqPathMapper.insertDctApprReqPath(insertDctApprReqPathParam);
            }

            // 03. INSERT BMS_DCT_APPRREQ_PRT TABLE
            int resInsertUseDigitalPrintFile = 0;
            for(InsertUseDigitalPrintReqVO tempReqVO : param.getPrintList()) {
                tempReqVO.setReqdestructdt(tempReqVO.getReqdestructdt().replaceAll("-","")+ "235959");
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertUseDigitalPrintReqVO insertUseDigitalPrintReqParam = getApprreqPrtVO(tempReqVO);
                resInsertUseNonDigitalPrint += bmsDctApprreqPrtMapper.insertUseNonDigitalPrint(insertUseDigitalPrintReqParam);
            }

            // 05. INSERT BMS_DCT_HIST_CARD TABLE
            int resInsertHistory = 0;
            String histSeq = bmsDctHistCardMapper.getHistSeq();
            param.setHistgubun("06"); // 인쇄
            param.setHiststatus("010"); // 요청
            for(String resDocid : param.getDocid()){
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resDocid, histSeq, formatter.format(today));
                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertUseNonDigitalPrint;
    }


}
