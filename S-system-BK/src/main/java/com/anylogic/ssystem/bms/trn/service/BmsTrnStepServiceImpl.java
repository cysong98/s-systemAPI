/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.trn.service;


import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;
import com.anylogic.ssystem.bms.dct.model.InsertPathReqVO;
import com.anylogic.ssystem.bms.dct.model.InsertTempListReqVO;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.trn.mapper.BmsTrnStepMapper;
import com.anylogic.ssystem.bms.trn.model.InsertTrnStepReqVO;

import com.anylogic.ssystem.bms.trn.mapper.BmsTrnReportMapper;
import com.anylogic.ssystem.bms.trn.model.InsetTrnObjectReqVO;
import com.anylogic.ssystem.bms.trn.mapper.BmsTrnObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;



@Slf4j
@RequiredArgsConstructor
@Service("BmsTrnStepService")
@Transactional
public class BmsTrnStepServiceImpl implements BmsTrnStepService {


    @Autowired
    private BmsTrnStepMapper bmsTrnStepMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;

    @Autowired
    private BmsTrnReportMapper bmsTrnReportMapper;

    @Autowired
    private BmsTrnObjectMapper bmsTrnObjectMapper;


    /* 인계인수서 등록(승인요청) */
    @Override
    @Transactional
    public int insertTrnStep(InsertTrnStepReqVO param){
        int resInsertTrnStep = 0;
        String resSysGuid = "";
        try {
            // 00. CREATE SYS_GUID STRING
            resSysGuid = bmsDctRdocMapper.getSysGuid();
            String resMgmtId = "MGT" + resSysGuid;
            String resTrnId = "TRN" + resSysGuid; //인계인수일련번호

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String currentDateTime = LocalDateTime.now().format(formatter);

            if(param.getTransferid() == null || param.getTransferid().equals("") || param.getTransferid().isEmpty()){
                param.setTransferid("TRN"+ resSysGuid);
            }
            // 01. INSERT BMS_TRN_REPORT 인계인수서
            int resInsertTrnReport = 0;
            InsertTrnStepReqVO insertTrnStepReqVO = InsertTrnStepReqVO.builder()
                                                                    .transferid(param.getTransferid())
                                                                    .title(param.getReqttl())
                                                                    .reason(param.getReqcontents())
                                                                    .apprstatus(param.getApprstatus())
                                                                    .requsername(param.getRequsername())
                                                                    .requserid(param.getRequserid())
                                                                    .inid(param.getRequserid())
                                                                    .status("TRS01") //결재중
                                                                    .indt(currentDateTime)
                                                                    .build();
            resInsertTrnReport = bmsTrnReportMapper.insertTrnReport(insertTrnStepReqVO);

            // 경로에 이미 존재할경우 삭제, 없으면 인서트
            if( (!param.getHandoverList().isEmpty()) && (!param.getTakeoverList().isEmpty()) ){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_trn_step");
                paramMap.put("transferid", param.getTransferid());
                bmsTrnStepMapper.deleteTempList(paramMap);
            }
            // 02. INSERT BMS_TRN_STEP 인수인계결재경로 - 인계경로
            boolean firstIdx = true;
            for (InsertTrnStepReqVO tempReqVO : param.getHandoverList()) {
//                System.out.println(">>>>>>>> : " + param.getHandoverList());
                String apprstatus = tempReqVO.getApprstatus();
                if(!Objects.equals(tempReqVO.getApprcode(), "")) {
                    tempReqVO.setTransferid(param.getTransferid());
                    tempReqVO.setInid(param.getRequserid());
                    tempReqVO.setApprstatus(apprstatus);
                    if(firstIdx) {
                        tempReqVO.setApprreason(param.getApprreason());
                        tempReqVO.setApprstatus("APR02");
                        firstIdx = false;
                    }
                    InsertTrnStepReqVO insertTrnStepParam = getHandoverVO(tempReqVO, currentDateTime);
                    resInsertTrnStep += bmsTrnStepMapper.insertTrnStep(insertTrnStepParam);
                }
            }
            // 02. INSERT BMS_TRN_STEP 인수인계결재경로 - 인수경로
            for (InsertTrnStepReqVO tempReqVO : param.getTakeoverList()) {
                String apprstatus = tempReqVO.getApprstatus();
                if(!Objects.equals(tempReqVO.getApprcode(), "")) {
                    tempReqVO.setTransferid(param.getTransferid());
                    tempReqVO.setInid(param.getRequserid());
                    tempReqVO.setApprstatus(apprstatus);
                    InsertTrnStepReqVO insertTrnStepParam = getTakeoverVO(tempReqVO, currentDateTime);
                    resInsertTrnStep += bmsTrnStepMapper.insertTrnStep(insertTrnStepParam);
                }
            }

            // 03. INSERT BMS_TRN_OBJECT 인계인수목록 - 대상선택
            int resInsertTrnObject = 0;
            int SelectListIdx = 0;
            if( !param.getSelectList().isEmpty() ){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_trn_object");
                paramMap.put("transferid", param.getTransferid());
                bmsTrnStepMapper.deleteTempList(paramMap);
            }
            for (InsetTrnObjectReqVO tempReqVO : param.getSelectList()) {
                if(!Objects.equals(tempReqVO.getDocnoseq(), "")) {
                    tempReqVO.setTransferid(param.getTransferid());
                    if(tempReqVO.getPaperflag() !=null && tempReqVO.getPaperflag().equals("Y")){
                        tempReqVO.setPaperflag("Y");
                    }else if(tempReqVO.getPaperflag() == null){
                        tempReqVO.setPaperflag(null);
                    }else{
                        tempReqVO.setPaperflag("N");
                    }
                    InsetTrnObjectReqVO insertTrnObjectParam = getTrndocVO(tempReqVO, currentDateTime.substring(0,8));
                    resInsertTrnObject += bmsTrnObjectMapper.insetTrnObject(insertTrnObjectParam);
                }
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertTrnStep;
    }
    // 02. INSERT BMS_TRN_STEP 인수인계결재경로 - 인계경로
    private InsertTrnStepReqVO getHandoverVO (InsertTrnStepReqVO vo, String indt){
        InsertTrnStepReqVO build = InsertTrnStepReqVO.builder()
                                                    .inid(vo.getInid())
                                                    .indt(indt)
                                                    .transferid(vo.getTransferid())
                                                    .apprseq(vo.getApprseq())
                                                    .apprusername(vo.getApprusername())
                                                    .appruserid(vo.getAppruserid())
                                                    .apprdeptname(vo.getApprdeptname())
                                                    .apprdeptid(vo.getApprdeptid())
                                                    .apprposition(vo.getApprposition())
                                                    .apprstatus(vo.getApprstatus())
                                                    .apprcode(vo.getApprcode())
                                                    .apprreason(vo.getApprreason())
                                                    .state(vo.getState())
                                                    .build();
        return build;
    }
    // 02. INSERT BMS_TRN_STEP 인수인계결재경로 - 인수경로
    private InsertTrnStepReqVO getTakeoverVO (InsertTrnStepReqVO vo, String indt){
        InsertTrnStepReqVO build = InsertTrnStepReqVO.builder()
                .inid(vo.getInid())
                .indt(indt)
                .transferid(vo.getTransferid())
                .apprseq(vo.getApprseq())
                .apprusername(vo.getApprusername())
                .appruserid(vo.getAppruserid())
                .apprdeptname(vo.getApprdeptname())
                .apprdeptid(vo.getApprdeptid())
                .apprposition(vo.getApprposition())
                .apprstatus(vo.getApprstatus())
                .apprcode(vo.getApprcode())
                .apprreason(vo.getApprreason())
                .state(vo.getState())
                .build();
        return build;
    }

    // 03. INSERT BMS_TRN_OBJECT 인계인수목록 - 대상선택
    private InsetTrnObjectReqVO getTrndocVO (InsetTrnObjectReqVO vo, String reportdt){
        InsetTrnObjectReqVO build = InsetTrnObjectReqVO.builder()
                .reportdt(reportdt)
                .transferid(vo.getTransferid())
                .objectid(vo.getObjectid())
                .objectname(vo.getObjectname())
                .paperflag(vo.getPaperflag())
//                .objecttype(vo.getObjecttype())
                .docnoseq(vo.getDocnoseq())
                .status(vo.getStatus())
                .mgmtno(vo.getMgmtno())
                .seclevel(vo.getSeclevel())
                .secstatus(vo.getSecstatus())
                .build();
        return build;
    }

}
