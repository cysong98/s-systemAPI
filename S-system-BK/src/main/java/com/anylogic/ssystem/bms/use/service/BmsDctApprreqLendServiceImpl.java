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
import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqLendMapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqMapper;
import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqPathMapper;
import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqPrtFleMapper;
import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqLendFleMapper;



@Slf4j
@RequiredArgsConstructor
@Service("BmsDctApprreqLendService")
@Transactional
public class BmsDctApprreqLendServiceImpl implements BmsDctApprreqLendService {

    @Autowired
    private BmsDctHistCardMapper bmsDctHistCardMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;

    @Autowired
    private BmsDctApprreqLendMapper bmsDctApprreqLendMapper;

    @Autowired
    private BmsDctApprreqMapper bmsDctApprreqMapper;

    @Autowired
    private BmsDctApprreqPathMapper bmsDctApprreqPathMapper;

    @Autowired
    private BmsDctApprreqLendFleMapper bmsDctApprreqLendFleMapper;

    /* 전자 반출 요청서 작성 */
    @Transactional
    @Override
    public int insertUseTakeOut(InsertUseTakeOutReqVO param){
        int resInsertUseTakeOut = 0;
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
            for(InsertUseTakeOutReqVO tempReqVO : param.getPathList()) {
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertDctApprReqPathReqVO insertDctApprReqPathParam = getApprreqPathVO(tempReqVO, formatter.format(today));
                resInsertDctApprReqPath += bmsDctApprreqPathMapper.insertDctApprReqPath(insertDctApprReqPathParam);
            }

            // 03. INSERT BMS_DCT_APPRREQ_LEND TABLE
            int resInsertApprreqLendFle = 0;
            for(InsertUseTakeOutReqVO tempReqVO : param.getLendList()){
                tempReqVO.setReqlenddt(tempReqVO.getReqlenddt().replaceAll("-","")+ "000000");
                tempReqVO.setReqbackdt(tempReqVO.getReqbackdt().replaceAll("-","")+ "235959");
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertUseTakeOutReqVO insertUseTakeOutReqParam = getApprreqLendVO(tempReqVO);
                resInsertUseTakeOut += bmsDctApprreqLendMapper.insertUseTakeOut(insertUseTakeOutReqParam);

                // 04. INSERT BMS_DCT_APPRREQ_LEND_FLE TABLE
                for(InsertUseTakeOutReqVO fileReqVO : tempReqVO.getFileList()){
                    fileReqVO.setApprreqid(param.getApprreqid());
                    InsertPrtFileReqVO insertPrtFileReqParam = getApprreqLendFileVO(fileReqVO);
                    resInsertApprreqLendFle += bmsDctApprreqLendFleMapper.insertPrtFile(insertPrtFileReqParam);
                }

            }

            // 05. INSERT BMS_DCT_HIST_CARD TABLE
            int resInsertHistory = 0;
            String histSeq = bmsDctHistCardMapper.getHistSeq();
            if(param.getLendList().get(0).getLendgubun().equals("2")){
                param.setHistgubun("24"); // 대출
            }else{
                param.setHistgubun("25"); // 지출
            }
            param.setHiststatus("010"); // 요청
            for(String resDocid : param.getDocid()){
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resDocid, histSeq, formatter.format(today));
                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertUseTakeOut;
    }

    // 01. INSERT BMS_DCT_APPRREQ TABLE
    private InsertDctApprReqReqVO getApprreqVO(InsertUseTakeOutReqVO vo, String indt){
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
    private InsertDctApprReqPathReqVO getApprreqPathVO(InsertUseTakeOutReqVO vo, String indt){
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

    // 03. INSERT BMS_DCT_APPRREQ_LEND TABLE
    private InsertUseTakeOutReqVO getApprreqLendVO(InsertUseTakeOutReqVO vo){
        InsertUseTakeOutReqVO build = InsertUseTakeOutReqVO.builder()
                .apprreqid(vo.getApprreqid())
                .mgmtid(vo.getMgmtid())
                .lendgubun(vo.getLendgubun())
                .reqlenddt(vo.getReqlenddt())
                .reqbackdt(vo.getReqbackdt())
                .grantlenddt(vo.getGrantlenddt())
                .grantbackdt(vo.getGrantbackdt())
                .grantflag(vo.getGrantflag())
                .lendtype(vo.getLendtype())
                .lendcharge(vo.getLendcharge())
                .carrypcid(vo.getCarrypcid())
                .isorgdoc(vo.getIsorgdoc())
                .build();
        return build;
    }

    // 04. INSERT BMS_DCT_APPRREQ_LEND_FLE TABLE
    private InsertPrtFileReqVO getApprreqLendFileVO(InsertUseTakeOutReqVO vo){
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
                .build();
        return build;
    }

    // 05. INSERT BMS_DCT_HIST_CARD TABLE
    private InsertHistoryReqVO getHistoryVO (InsertUseTakeOutReqVO vo, String resDocid, String histSeq, String indt){
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


    /* 비밀문서사용요청 > 반출요청 > 비전자 반출요청서 등록 */
    @Transactional
    @Override
    public int insertUseNondigitalTakeOut(InsertUseTakeOutReqVO param){
        int resInsertUseTakeOut = 0;
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
            for(InsertUseTakeOutReqVO tempReqVO : param.getPathList()) {
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertDctApprReqPathReqVO insertDctApprReqPathParam = getApprreqPathVO(tempReqVO, formatter.format(today));
                resInsertDctApprReqPath += bmsDctApprreqPathMapper.insertDctApprReqPath(insertDctApprReqPathParam);
            }

            // 03. INSERT BMS_DCT_APPRREQ_LEND TABLE
            int resInsertApprreqLendFle = 0;
            for(InsertUseTakeOutReqVO tempReqVO : param.getLendList()){
                tempReqVO.setReqlenddt(tempReqVO.getReqlenddt().replaceAll("-","")+ "000000");
                tempReqVO.setReqbackdt(tempReqVO.getReqbackdt().replaceAll("-","")+ "235959");
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertUseTakeOutReqVO insertUseTakeOutReqParam = getApprreqLendVO(tempReqVO);
                resInsertUseTakeOut += bmsDctApprreqLendMapper.insertUseTakeOut(insertUseTakeOutReqParam);
            }

            // 04. INSERT BMS_DCT_HIST_CARD TABLE
            int resInsertHistory = 0;
            String histSeq = bmsDctHistCardMapper.getHistSeq();
            param.setHistgubun("24"); // 대출
            param.setHiststatus("010"); // 요청
            for(String resDocid : param.getDocid()){
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resDocid, histSeq, formatter.format(today));
                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertUseTakeOut;
    }


}
