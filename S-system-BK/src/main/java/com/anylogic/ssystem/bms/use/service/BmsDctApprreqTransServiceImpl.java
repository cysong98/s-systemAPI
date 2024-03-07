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
import com.anylogic.ssystem.bms.use.mapper.BmsDctMgmtRegiMapper;
import com.anylogic.ssystem.bms.use.model.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqTransMapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqPathMapper;
import com.anylogic.ssystem.bms.use.mapper.BmsDctApprreqMapper;



@Slf4j
@RequiredArgsConstructor
@Service("BmsDctApprreqTransService")
@Transactional
public class BmsDctApprreqTransServiceImpl implements BmsDctApprreqTransService {
    @Autowired
    private BmsDctHistCardMapper bmsDctHistCardMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;

    @Autowired
    private BmsDctMgmtRegiMapper bmsDctMgmtRegiMapper;

    @Autowired
    private BmsDctApprreqTransMapper bmsDctApprreqTransMapper;

    @Autowired
    private BmsDctApprreqPathMapper bmsDctApprreqPathMapper;

    @Autowired
    private BmsDctApprreqMapper bmsDctApprreqMapper;

    /* 예고문도래목록 > 내부이관 > 내부이관요청서 등록 */
    @Transactional
    @Override
    public int insertComingTransApproveBIA(InsertComingTransApproveBIAReqVO param){
        int resInsertComingTransApproveBIA = 0;
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
            for(InsertComingTransApproveBIAReqVO tempReqVO : param.getPathList()) {
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertDctApprReqPathReqVO insertDctApprReqPathParam = getApprreqPathVO(tempReqVO, formatter.format(today));
                resInsertDctApprReqPath += bmsDctApprreqPathMapper.insertDctApprReqPath(insertDctApprReqPathParam);
            }

            // 03. INSERT BMS_DCT_APPRREQ_TRANS TABLE
            String mgmtid = "";
            for(InsertComingTransApproveBIAReqVO tempReqVO : param.getTransList()) {
                tempReqVO.setTransdt(formatter.format(today));
                tempReqVO.setApprreqid(param.getApprreqid());
                tempReqVO.setGrantflag(param.getGrantflag());
                InsertComingTransApproveBIAReqVO insertComingTransApproveBIAReqParam = getApprreqTransVO(tempReqVO);
                resInsertComingTransApproveBIA += bmsDctApprreqTransMapper.insertComingTransApproveBIA(insertComingTransApproveBIAReqParam);

                // 03-1. UPDATE BMS_DCT_MGMT_REGI TABLE
                mgmtid = tempReqVO.getMgmtid();
                if (param.getReqstatus().equals("1")) {
                    param.setSecstatus("MGT80"); // 내부이관 요청중
                } else if (param.getReqstatus().equals("6")) {
                    param.setSecstatus("MGT81"); // 내부이관 승인
                }
                param.setUpdtid(param.getReqid());
                param.setUpdtname(param.getReqname());
                UpdateComingReqsMgmtReqVO mgmtVO = getMgmtVO(param, mgmtid, formatter.format(today));
                bmsDctMgmtRegiMapper.updateComingReqsMgmt(mgmtVO);
            }

            // 04. INSERT BMS_DCT_HIST_CARD TABLE
            int resInsertHistory = 0;
            String histSeq = bmsDctHistCardMapper.getHistSeq();
            param.setHistgubun("29"); // 내부이관
            param.setHiststatus("010"); // 요청
            for(String resDocid : param.getDocid()){
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resDocid, histSeq, formatter.format(today));
                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resInsertComingTransApproveBIA;
    }


    // 01. INSERT BMS_DCT_APPRREQ TABLE
    private InsertDctApprReqReqVO getApprreqVO(InsertComingTransApproveBIAReqVO vo, String indt){
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
    private InsertDctApprReqPathReqVO getApprreqPathVO(InsertComingTransApproveBIAReqVO vo, String indt){
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

    // 03. INSERT BMS_DCT_APPRREQ_TRANS TABLE
    private InsertComingTransApproveBIAReqVO getApprreqTransVO(InsertComingTransApproveBIAReqVO vo){
        InsertComingTransApproveBIAReqVO build = InsertComingTransApproveBIAReqVO.builder()
                .apprreqid(vo.getApprreqid())
                .mgmtid(vo.getMgmtid())
                .transdt(vo.getTransdt())
                .reqtransnextdt(vo.getReqtransnextdt())
                .granttransnextdt(vo.getGranttransnextdt())
                .grantflag(vo.getGrantflag())
                .build();
        return build;
    }

    // 03-1. UPDATE BMS_DCT_MGMT_REGI TABLE
    private UpdateComingReqsMgmtReqVO getMgmtVO(InsertComingTransApproveBIAReqVO vo, String mgmtid, String indt){
        UpdateComingReqsMgmtReqVO build = UpdateComingReqsMgmtReqVO.builder()
                .mgmtid(mgmtid)
                .secstatus(vo.getSecstatus())
                .updtid(vo.getUpdtid())
                .updtname(vo.getUpdtname())
                .updtdt(indt)
                .build();
        return build;
    }

    // 04. INSERT BMS_DCT_HIST_CARD TABLE
    private InsertHistoryReqVO getHistoryVO (InsertComingTransApproveBIAReqVO vo,String resDocid, String histSeq, String indt){
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

    /* 예고문도래목록 > 이관연기 > 이관연기요청서 등록 */
    @Transactional
    @Override
    public int insertComingTransDelayBIA(InsertComingTransApproveBIAReqVO param){
        int resInsertComingTransDelayBIA = 0;
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
            for(InsertComingTransApproveBIAReqVO tempReqVO : param.getPathList()) {
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertDctApprReqPathReqVO insertDctApprReqPathParam = getApprreqPathVO(tempReqVO, formatter.format(today));
                resInsertDctApprReqPath += bmsDctApprreqPathMapper.insertDctApprReqPath(insertDctApprReqPathParam);
            }

            // 03. INSERT BMS_DCT_APPRREQ_TRANS TABLE
            String mgmtid = "";
            for(InsertComingTransApproveBIAReqVO tempReqVO : param.getTransList()) {
                tempReqVO.setReqtransnextdt(tempReqVO.getReqtransnextdt()+"1231235959");
                tempReqVO.setApprreqid(param.getApprreqid());
                tempReqVO.setGrantflag(param.getGrantflag());
                InsertComingTransApproveBIAReqVO insertComingTransApproveBIAReqParam = getApprreqTransVO(tempReqVO);
                resInsertComingTransDelayBIA += bmsDctApprreqTransMapper.insertComingTransDelayBIA(insertComingTransApproveBIAReqParam);

                // 03-1. UPDATE BMS_DCT_MGMT_REGI TABLE
                mgmtid = tempReqVO.getMgmtid();
                if (param.getReqstatus().equals("1")) {
                    param.setSecstatus("MGT70"); // 이관연기 요청중
                } else if (param.getReqstatus().equals("6")) {
                    param.setSecstatus("MGT71"); // 이관연기 승인
                }
                param.setUpdtid(param.getReqid());
                param.setUpdtname(param.getReqname());
                UpdateComingReqsMgmtReqVO mgmtVO = getMgmtVO(param, mgmtid, formatter.format(today));
                bmsDctMgmtRegiMapper.updateComingReqsMgmt(mgmtVO);
            }

            // 04. INSERT BMS_DCT_HIST_CARD TABLE
            int resInsertHistory = 0;
            String histSeq = bmsDctHistCardMapper.getHistSeq();
            param.setHistgubun("18"); // 이관연장
            param.setHiststatus("010"); // 요청
            for(String resDocid : param.getDocid()){
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resDocid, histSeq, formatter.format(today));
                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resInsertComingTransDelayBIA;
    }

    /* 예고문도래목록 > 이관대기 > 이관대기요청서 등록 */
    @Transactional
    @Override
    public int insertTransWait(InsertTransWaitReqVO param){
        int resInsertDctApprReq = 0;
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
            param.setApprreqid(resApprreqid);
            InsertDctApprReqReqVO insertDctApprReqParam = getTransWaitVO(param, formatter.format(today));
            resInsertDctApprReq += bmsDctApprreqMapper.insertDctApprReq(insertDctApprReqParam);


            // 02. INSERT BMS_DCT_APPRREQ_PATH TABLE
            int resInsertDctApprReqPath = 0;
            for(InsertComingTransApproveBIAReqVO tempReqVO : param.getPathList()) {
                tempReqVO.setApprreqid(param.getApprreqid());
                InsertDctApprReqPathReqVO insertDctApprReqPathParam = getApprreqPathVO(tempReqVO, formatter.format(today));
                resInsertDctApprReqPath += bmsDctApprreqPathMapper.insertDctApprReqPath(insertDctApprReqPathParam);
            }

            // 03. INSERT BMS_DCT_APPRREQ_TRANS TABLE
            String mgmtid = "";
            for(InsertComingTransApproveBIAReqVO tempReqVO : param.getTransList()) {
                tempReqVO.setApprreqid(param.getApprreqid());
                tempReqVO.setGrantflag(param.getGrantflag());
                InsertComingTransApproveBIAReqVO insertComingTransApproveBIAReqParam = getApprreqTransVO(tempReqVO);
                bmsDctApprreqTransMapper.insertComingTransApproveBIA(insertComingTransApproveBIAReqParam);

                // 03-1. UPDATE BMS_DCT_MGMT_REGI TABLE
                mgmtid = tempReqVO.getMgmtid();
                if (param.getReqstatus().equals("1")) {
                    param.setSecstatus("MGT60"); // 이관대기 요청중
                } else if (param.getReqstatus().equals("6")) {
                    param.setSecstatus("MGT62"); // 이관대기
                }
                param.setUpdtid(param.getReqid());
                param.setUpdtname(param.getReqname());
                UpdateComingReqsMgmtReqVO mgmtVO = getMgmtVO(param, mgmtid, formatter.format(today));
                bmsDctMgmtRegiMapper.updateComingReqsMgmt(mgmtVO);
            }

            // 04. UPDATE BMS_DCT_RDOC TABLE
            int resUpdateRdoc = 0;
            for(InsertTransWaitReqVO tempReqVO : param.getRdocList()){
                UpdateTransWaitReqVO updateTransWaitReqParam = getRdocVO(tempReqVO);
                resUpdateRdoc += bmsDctRdocMapper.updateTransWaitRdoc(updateTransWaitReqParam);
            }
            
            // 05. INSERT BMS_DCT_HIST_CARD TABLE
            int resInsertHistory = 0;
            String histSeq = bmsDctHistCardMapper.getHistSeq();
            param.setHistgubun("28"); // 이관대기
            param.setHiststatus("010"); // 요청
            for(String resDocid : param.getDocid()){
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resDocid, histSeq, formatter.format(today));
                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resInsertDctApprReq;
    }

    // 01. INSERT BMS_DCT_APPRREQ TABLE
    private InsertDctApprReqReqVO getTransWaitVO(InsertTransWaitReqVO vo, String indt){
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


    // 03-1. UPDATE BMS_DCT_MGMT_REGI TABLE
    private UpdateComingReqsMgmtReqVO getMgmtVO(InsertTransWaitReqVO vo, String mgmtid, String indt){
        UpdateComingReqsMgmtReqVO build = UpdateComingReqsMgmtReqVO.builder()
                .mgmtid(mgmtid)
                .secstatus(vo.getSecstatus())
                .updtid(vo.getUpdtid())
                .updtname(vo.getUpdtname())
                .updtdt(indt)
                .build();
        return build;
    }

    // 04. UPDATE BMS_DCT_RDOC TABLE
    private UpdateTransWaitReqVO getRdocVO(InsertTransWaitReqVO vo){
        UpdateTransWaitReqVO build = UpdateTransWaitReqVO.builder()
                .docid(vo.getR_docid())
                .open(vo.getR_open())
                .readrangetype(vo.getR_readrangetype())
                .readlimittype(vo.getR_readlimittype())
                .build();
        return build;
    }
    
    // 05. INSERT BMS_DCT_HIST_CARD TABLE
    private InsertHistoryReqVO getHistoryVO (InsertTransWaitReqVO vo, String resDocid, String histSeq, String indt){
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
    

}
