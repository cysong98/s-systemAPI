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


import com.anylogic.ssystem.bms.dct.mapper.BmsDctHistCardMapper;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocRcvsMapper;
import com.anylogic.ssystem.bms.dct.model.*;
import com.anylogic.ssystem.bms.login.mapper.ComUserinfoMapper;
import com.anylogic.ssystem.bms.login.model.GetUserLoginReqVO;
import com.anylogic.ssystem.bms.login.model.GetUserLoginResVO;
import com.anylogic.ssystem.bms.trn.mapper.BmsTrnObjectMapper;
import com.anylogic.ssystem.bms.trn.model.*;
import com.anylogic.ssystem.bms.use.model.InsertUseTakeOutReqVO;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.trn.mapper.BmsTrnReportMapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import java.util.Map;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.trn.mapper.BmsTrnStepMapper;
import com.anylogic.ssystem.bms.trn.mapper.BmsDctMgmtRegiMapper;


@Slf4j
@RequiredArgsConstructor
@Service("BmsTrnReportService")
@Transactional
public class BmsTrnReportServiceImpl implements BmsTrnReportService {

    @Autowired
    private BmsTrnObjectMapper bmsTrnObjectMapper;

    @Autowired
    private BmsTrnReportMapper bmsTrnReportMapper;

    @Autowired
    private BmsTrnStepMapper bmsTrnStepMapper;

    @Autowired
    private BmsDctMgmtRegiMapper bmsDctMgmtRegiMapper;

    @Autowired
    private ComUserinfoMapper comUserinfoMapper;

    @Autowired
    private BmsDctRdocRcvsMapper bmsDctRdocRcvsMapper;

    @Autowired
    private BmsDctHistCardMapper bmsDctHistCardMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;


    /* 인계인수 > 처리할 인계인수서 > 처리할 인계인수서 목록 조회 */
    @Override
    public PageInfo<SelectTransProcToDoListResVO> selectTransProcToDoList(SelectTransProcToDoListReqVO param){
        param.setTransferid(param.getTransferid());
        param.setAppruserid(param.getAppruserid());
        param.setApprdeptid(param.getApprdeptid());
        PageInfo<SelectTransProcToDoListResVO> resultListVO = null;
        try {
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            List<SelectTransStepResVO> list = null;
            List<SelectTransProcToDoListResVO> resList = bmsTrnReportMapper.selectTransProcToDoList(param);
            for(int i=0; i<resList.size(); i++) {
                list = bmsTrnStepMapper.selectTransStep(SelectTransStepReqVO.builder()
                                                            .transferid(resList.get(i).getTransferid())
                                                            .build());
                resList.get(i).setFinalTakeOver(list.get(0).getApprusername());
            }

            PageHelper.startPage(param);
            resultListVO = PageInfo.of(resList);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 처리할 인계인수서 승인 처리 */
    @Override
    @Transactional
    public int updateTrnRepotBIA1(UpdateTrnRepotBIA1ReqVO param){
        int resUpdateTrnRepotBIA1 = 0;
        try {
            /* 로그인한 유저 정보 조회 */
            GetUserLoginResVO resGetUserLogin;
            GetUserLoginReqVO getUserLoginReqVO = GetUserLoginReqVO.builder()
                                                                .userid(param.getAppruserid())
                                                                .build();
            //getUserLogin(param)부분은 로그인 api가 변동시 수정예정.2023-10-27 기준 현재 로직은 이렇게.
            resGetUserLogin = comUserinfoMapper.getUserLogin(GetUserLoginReqVO.builder()
//                                                                .userid(param.getLoginuserid())
                                                                .userid(getUserLoginReqVO.getUserid())
                                                                .build());
            GetUserLoginResVO resGetReqUser = comUserinfoMapper.getUserLogin(GetUserLoginReqVO.builder()
//                                                                .userid(param.getLoginuserid())
                                                                .userid(param.getRequserid())
                                                                .build());

            /* 현재시간 출력 */
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String currentDateTime = LocalDateTime.now().format(formatter);

//            param.setApprstatus("APP03"); //결재완료
            //유저에게 할당된 로직 따라 인계,인수 분기처리예정
//            param.setStatus("TRS04"); //인수완료 //TRS02 인계완료
            param.setTitle(param.getReqttl());
            param.setReason(param.getReqcontents());
            param.setReqdate(currentDateTime.substring(0,8));
            resUpdateTrnRepotBIA1 = bmsTrnReportMapper.updateTrnRepotBIA1(param);

            /* 승인반려여부 APR02(승인) */
            // ***** Operation = updateTrnStep
            UpdateTrnStepReqVO updateTrnStepParam = new UpdateTrnStepReqVO();

            /* 비밀상태 MGT51(인계완료) */
            // ***** Operation = updateMgmtRegi
            UpdateMgmtRegiReqVO updateMgmtRegiParam = new UpdateMgmtRegiReqVO();
            InsertDocIdBmsDctRdocRcvsListReqVO updateRdocRcvsParam = new InsertDocIdBmsDctRdocRcvsListReqVO();
            int resUpdateMgmtRegi = 0;
            // 인계정보 승인
            List<InsertTrnStepReqVO> handoverList = param.getHandoverList();
            List<InsetTrnObjectReqVO> selectObjectList = param.getSelectList();
            for(InsertTrnStepReqVO handoverListVO : handoverList) {
                if(handoverListVO.getAppruserid().equals(param.getAppruserid()) &&
                        handoverListVO.getApprseq().compareTo(param.getApprseq()) == 0) {
                    String apprcode = handoverListVO.getApprcode();
                    updateTrnStepParam.setTransferid(param.getTransferid());
                    updateTrnStepParam.setApprseq(handoverListVO.getApprseq());
                    updateTrnStepParam.setApprreason(param.getApprreason());
                    if (param.getGojudge().equals("go")) {
                        updateTrnStepParam.setApprstatus("APR02"); //승인
                        updateTrnStepParam.setState("DCST3"); //처리완료
                        updateMgmtRegiParam.setSecstatus("MGT51"); //인계완료
                        resUpdateMgmtRegi = bmsDctMgmtRegiMapper.updateMgmtRegi(updateMgmtRegiParam);
                        if (apprcode.equals("ARC01") /* || apprcode.equals("ARC02") */ || apprcode.equals("ARC03") || apprcode.equals("ARC04")) {
                            param.setStatus("TRS02"); //(인계완료)
                        } else {
                            param.setStatus("TRS01"); //(결재중)
                        }
                        resUpdateTrnRepotBIA1 = bmsTrnReportMapper.updateTrnRepotBIA1(param);

                        if(param.getStatus().equals("TRS01") || param.getStatus().equals("TRS02")) {
                            for (InsetTrnObjectReqVO selectObjectListVO : selectObjectList) {
                                // bms_dct_hist_card
                                if(param.getApprseq().compareTo(BigDecimal.valueOf(2)) == 0) {
                                    String histSeq = bmsDctHistCardMapper.getHistSeq();
                                    InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(selectObjectListVO, selectObjectListVO.getObjectid(), histSeq, currentDateTime);
                                    insertHistoryReqVO.setHiststatus("151"); // 인계담당
                                    insertHistoryReqVO.setDeptid(resGetReqUser.getDeptid());
                                    insertHistoryReqVO.setDeptname(resGetReqUser.getDeptname());
                                    insertHistoryReqVO.setDutyname(resGetReqUser.getGrade());
                                    insertHistoryReqVO.setUserid(resGetReqUser.getUserid());
                                    insertHistoryReqVO.setUsername(resGetReqUser.getUsername());
                                    bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
                                }

                                String histSeq = bmsDctHistCardMapper.getHistSeq();
                                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(selectObjectListVO, selectObjectListVO.getObjectid(), histSeq, currentDateTime);
                                insertHistoryReqVO.setHiststatus("153"); // 인계결재
                                insertHistoryReqVO.setDeptid(resGetUserLogin.getDeptid());
                                insertHistoryReqVO.setDeptname(resGetUserLogin.getDeptname());
                                insertHistoryReqVO.setDutyname(resGetUserLogin.getGrade());
                                insertHistoryReqVO.setUserid(resGetUserLogin.getUserid());
                                insertHistoryReqVO.setUsername(resGetUserLogin.getUsername());
                                bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);

                                // bms_trn_object
                                if(param.getStatus().equals("TRS02")) {
                                    bmsTrnObjectMapper.updateTrnObject(UpdateTrnObjectReqVO.builder()
                                                                            .transferid(selectObjectListVO.getTransferid())
                                                                            .objectid(selectObjectListVO.getObjectid())
                                                                            .secstatus("MGT51") // 인계완료
                                                                            .build());
                                }
                            }
                        }
                    } else if (param.getGojudge().equals("stop")) {
                        updateTrnStepParam.setApprstatus("APR01"); //요청
                        updateTrnStepParam.setState("DCST2"); //처리대기
                        updateMgmtRegiParam.setSecstatus("MGT50"); //인수인계중
                        resUpdateMgmtRegi = bmsDctMgmtRegiMapper.updateMgmtRegi(updateMgmtRegiParam);
                        param.setStatus("TRS01"); //(결재중)
                        resUpdateTrnRepotBIA1 = bmsTrnReportMapper.updateTrnRepotBIA1(param);
                    }
                    int resUpdateTrnStep = bmsTrnStepMapper.updateTrnStep(updateTrnStepParam);
                }
            }
            // 인수정보 승인
            List<InsertTrnStepReqVO> takeoverList = param.getTakeoverList();
            selectObjectList = param.getSelectList();
            UpdateDctHistCardReqVO updateDctHistCardReqVO = new UpdateDctHistCardReqVO();
            UpdateDctRdocReqVO updateDctRdocParam = new UpdateDctRdocReqVO();
            UpdateAuthCardReqVO updateAuthCardVO = new UpdateAuthCardReqVO();
            int resUpdateMgmtRegiObj = 0;
            int resUpdateRdocRcvs = 0;
            int resInsertHistory = 0;
            int resUpdateDctRdoc = 0;
            int resUpdateAuthCard = 0;
            for (InsertTrnStepReqVO takeoverListVO : takeoverList) {
                if (takeoverListVO.getAppruserid().equals(param.getAppruserid()) &&
                        takeoverListVO.getApprseq().compareTo(param.getApprseq()) == 0) {
                    String apprcode = takeoverListVO.getApprcode();
                    updateTrnStepParam.setTransferid(param.getTransferid());
                    updateTrnStepParam.setApprseq(takeoverListVO.getApprseq());
                    updateTrnStepParam.setApprreason(param.getApprreason());
                    if (param.getGojudge().equals("go")) {
                        updateTrnStepParam.setApprstatus("APR02"); //승인
                        updateTrnStepParam.setState("DCST3"); //처리완료

                        if (apprcode.equals("ARC05") /* || apprcode.equals("ARC06") */ || apprcode.equals("ARC07") || apprcode.equals("ARC08")) {
                            param.setStatus(param.getStatus());
                        } else {
                            param.setStatus("TRS03"); //(인수중)
                        }
                        resUpdateTrnRepotBIA1 = bmsTrnReportMapper.updateTrnRepotBIA1(param);

                        if(param.getStatus().equals("TRS04")) {
                            for (InsetTrnObjectReqVO selectObjectListVO : selectObjectList) {
                                // bms_dct_mgmt_regi
                                updateMgmtRegiParam = setMgmtRegiVO(selectObjectListVO);
                                updateMgmtRegiParam.setSecstatus("MGT52"); //인수완료
                                updateMgmtRegiParam.setDeptid(resGetUserLogin.getDeptid());
                                updateMgmtRegiParam.setDeptname(resGetUserLogin.getDeptname());
                                updateMgmtRegiParam.setAuthorid(resGetUserLogin.getUserid());
                                updateMgmtRegiParam.setAuthorname(resGetUserLogin.getUsername());
                                updateMgmtRegiParam.setAuthordutyname(resGetUserLogin.getGrade());
                                resUpdateMgmtRegiObj += bmsDctMgmtRegiMapper.updateMgmtRegi(updateMgmtRegiParam);

                                // bms_dct_rdoc_rcvs
                                updateRdocRcvsParam.setDocid(selectObjectListVO.getObjectid());
                                updateRdocRcvsParam.setDeptid(resGetUserLogin.getDeptid());
                                updateRdocRcvsParam.setDeptname(resGetUserLogin.getDeptname());
                                updateRdocRcvsParam.setDutyname(resGetUserLogin.getGrade());
                                updateRdocRcvsParam.setUserid(resGetUserLogin.getUserid());
                                updateRdocRcvsParam.setUsername(resGetUserLogin.getUsername());
                                resUpdateRdocRcvs += bmsDctRdocRcvsMapper.updateRdocRcvs(updateRdocRcvsParam);

                                // bms_dct_rdoc
                                updateDctRdocParam.setDocid(selectObjectListVO.getObjectid());
                                updateDctRdocParam.setAuthorid(resGetUserLogin.getUserid());
                                updateDctRdocParam.setAuthorname(resGetUserLogin.getUsername());
                                updateDctRdocParam.setAuthordutyname(resGetUserLogin.getGrade());
                                updateDctRdocParam.setAuthordeptid(resGetUserLogin.getDeptid());
                                updateDctRdocParam.setAuthordeptname(resGetUserLogin.getDeptname());
                                resUpdateDctRdoc += bmsDctRdocMapper.updateDctRdoc(updateDctRdocParam);

                                // bms_trn_object
                                bmsTrnObjectMapper.updateTrnObject(UpdateTrnObjectReqVO.builder()
                                                                    .transferid(selectObjectListVO.getTransferid())
                                                                    .objectid(selectObjectListVO.getObjectid())
                                                                    .secstatus("MGT52") // 인수완료
                                                                    .build());

                                // bms_dct_hist_card
                                String histSeq = bmsDctHistCardMapper.getHistSeq();
                                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(selectObjectListVO, selectObjectListVO.getObjectid(), histSeq, currentDateTime);
                                insertHistoryReqVO.setHiststatus("152"); // 인수담당
                                insertHistoryReqVO.setDeptid(resGetUserLogin.getDeptid());
                                insertHistoryReqVO.setDeptname(resGetUserLogin.getDeptname());
                                insertHistoryReqVO.setDutyname(resGetUserLogin.getGrade());
                                insertHistoryReqVO.setUserid(resGetUserLogin.getUserid());
                                insertHistoryReqVO.setUsername(resGetUserLogin.getUsername()); // 인수자
                                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);

                                updateAuthCardVO = setAuthCardVO(selectObjectListVO);
                                updateAuthCardVO.setReqdeptid(resGetUserLogin.getDeptid());
                                updateAuthCardVO.setReqdeptname(resGetUserLogin.getDeptname());
                                updateAuthCardVO.setReqid(resGetUserLogin.getUserid());
                                updateAuthCardVO.setReqname(resGetUserLogin.getUsername());
                                resUpdateAuthCard += bmsDctRdocMapper.updateAuthCard(updateAuthCardVO);
                            }
                        }
                        else if(param.getStatus().equals("TRS03")) {
                            // bms_dct_hist_card
                            for (InsetTrnObjectReqVO selectObjectListVO : selectObjectList) {
                                String histSeq = bmsDctHistCardMapper.getHistSeq();
                                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(selectObjectListVO, selectObjectListVO.getObjectid(), histSeq, currentDateTime);
                                insertHistoryReqVO.setHiststatus("154"); // 인수결재
                                insertHistoryReqVO.setDeptid(resGetUserLogin.getDeptid());
                                insertHistoryReqVO.setDeptname(resGetUserLogin.getDeptname());
                                insertHistoryReqVO.setDutyname(resGetUserLogin.getGrade());
                                insertHistoryReqVO.setUserid(resGetUserLogin.getUserid());
                                insertHistoryReqVO.setUsername(resGetUserLogin.getUsername());
                                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
                            }
                        }
                    } else if (param.getGojudge().equals("stop")) {
                        updateTrnStepParam.setApprstatus("APR01"); //요청
                        updateTrnStepParam.setState("DCST2"); //처리대기
                        updateMgmtRegiParam.setSecstatus("MGT50"); //인수인계중
                        resUpdateMgmtRegiObj += bmsDctMgmtRegiMapper.updateMgmtRegi(updateMgmtRegiParam);
                    }
                    int resUpdateTrnStep = bmsTrnStepMapper.updateTrnStep(updateTrnStepParam);
                }
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateTrnRepotBIA1;
    }

    private UpdateMgmtRegiReqVO setMgmtRegiVO (InsetTrnObjectReqVO vo){
        UpdateMgmtRegiReqVO build = UpdateMgmtRegiReqVO.builder()
                .mgmtid(vo.getMgmtid())
                .docid(vo.getObjectid())
                .secstatus(vo.getSecstatus())
                .build();
        return build;
    }

    // 05. INSERT BMS_DCT_HIST_CARD TABLE
    private InsertHistoryReqVO getHistoryVO (InsetTrnObjectReqVO vo, String resDocid, String histSeq, String indt){
        InsertHistoryReqVO build = InsertHistoryReqVO.builder()
                .docid(resDocid)
                .histdt(indt)
                .histseq(new BigDecimal(histSeq))
                .histgubun("22") //22 : 인계인수 (코드)
//                .etc(vo.getEtc())
//                .electsignid(vo.getElectsignid())
                .build();
        return build;
    }

    private UpdateAuthCardReqVO setAuthCardVO (InsetTrnObjectReqVO vo){
        UpdateAuthCardReqVO build = UpdateAuthCardReqVO.builder()
                .docid(vo.getObjectid())
                .build();
        return build;
    }


    /* 처리할 인계인수서 반려 처리 */
    @Override
    @Transactional
    public int updateTrnRepotBIA2(UpdateTrnRepotBIA2ReqVO param){
        int resUpdateTrnRepotBIA2 = 0;
        try {
            GetUserLoginResVO resGetUserLogin = comUserinfoMapper.getUserLogin(GetUserLoginReqVO.builder()
                                                                            .userid(param.getAppruserid())
                                                                            .build());
            // 01. UPDATE bms_trn_report
            param.setApprstatus("APP04"); //결재반려
            //유저에게 할당된 로직 따라 인계,인수 분기처리예정
            param.setStatus("TRS05"); //반려
            param.setTitle(param.getReqttl());
            param.setReason(param.getReqcontents());
            resUpdateTrnRepotBIA2 = bmsTrnReportMapper.updateTrnRepotBIA2(param);

            /* 승인반려여부 APR03(반려) */
            // ***** Operation = updateTrnStep
            UpdateTrnStepReqVO updateTrnStepParam = null;
            // 인계정보 반려
            boolean flag = false;
            List<InsertTrnStepReqVO> handoverList = param.getHandoverList();
            for (InsertTrnStepReqVO handoverListVO : handoverList) {
                updateTrnStepParam = new UpdateTrnStepReqVO();
                String customState = handoverListVO.getState();
                if(flag || resGetUserLogin.getUserid().equals(handoverListVO.getAppruserid())) {
                    if(resGetUserLogin.getUserid().equals(handoverListVO.getAppruserid())) {
                        customState = "DCST3"; //처리완료
                        updateTrnStepParam.setApprreason(param.getApprreason());
                    }
                    flag = true;
                    updateTrnStepParam.setApprstatus("APR03"); //반려
                }
                updateTrnStepParam.setTransferid(param.getTransferid());
                updateTrnStepParam.setApprseq(handoverListVO.getApprseq());
                updateTrnStepParam.setState(customState); //처리완료
                int resUpdateTrnStep = bmsTrnStepMapper.updateTrnStep(updateTrnStepParam);
            }
            // 인수정보 반려
            List<InsertTrnStepReqVO> takeoverList = param.getTakeoverList();
            for (InsertTrnStepReqVO takeoverListVO : takeoverList) {
                updateTrnStepParam = new UpdateTrnStepReqVO();
                String customState = takeoverListVO.getState();
                if(flag || resGetUserLogin.getUserid().equals(takeoverListVO.getAppruserid())) {
                    if(resGetUserLogin.getUserid().equals(takeoverListVO.getAppruserid())) {
                        customState = "DCST3"; //처리완료
                        updateTrnStepParam.setApprreason(param.getApprreason());
                    }
                    flag = true;
                    updateTrnStepParam.setApprstatus("APR03"); //반려
                }
                updateTrnStepParam.setTransferid(param.getTransferid());
                updateTrnStepParam.setApprseq(takeoverListVO.getApprseq());
                updateTrnStepParam.setState(customState); //처리완료
                int resUpdateTrnStep = bmsTrnStepMapper.updateTrnStep(updateTrnStepParam);
            }

            // 이력카드 반려
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String currentDateTime = LocalDateTime.now().format(formatter);

            List<InsetTrnObjectReqVO> selectObjectList = param.getSelectList();
            for (InsetTrnObjectReqVO selectObjectListVO : selectObjectList) {
                // bms_dct_hist_card
                String histSeq = bmsDctHistCardMapper.getHistSeq();
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(selectObjectListVO, selectObjectListVO.getObjectid(), histSeq, currentDateTime);
                insertHistoryReqVO.setHiststatus("012"); //반려
                insertHistoryReqVO.setDeptid(resGetUserLogin.getDeptid());
                insertHistoryReqVO.setDeptname(resGetUserLogin.getDeptname());
                insertHistoryReqVO.setDutyname(resGetUserLogin.getGrade());
                insertHistoryReqVO.setUserid(resGetUserLogin.getUserid());
                insertHistoryReqVO.setUsername(resGetUserLogin.getUsername());
                bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);

                // bms_trn_object
                bmsTrnObjectMapper.updateTrnObject(UpdateTrnObjectReqVO.builder()
                                    .transferid(selectObjectListVO.getTransferid())
                                    .objectid(selectObjectListVO.getObjectid())
                                    .secstatus("MGT99") // 반송
                                    .build());
            }

            /* 비밀상태 MGT99(반송) */
            // ***** Operation = updateMgmtRegi
            UpdateMgmtRegiReqVO updateMgmtRegiParam = new UpdateMgmtRegiReqVO();
            updateMgmtRegiParam.setSecstatus("MGT99"); //반송
            int resUpdateMgmtRegi = bmsDctMgmtRegiMapper.updateMgmtRegi(updateMgmtRegiParam);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateTrnRepotBIA2;
    }


    /* 인계인수 > 요청한 인계인수서 > 요청한 인계인수서 목록 조회 */
    @Override
    public PageInfo<SelectTrnReqListResVO> selectTrnReqList(SelectTrnReqListReqVO param){
        param.setAppruserid(param.getAppruserid());
        param.setTransferid(param.getTransferid());
//        param.setApprdeptid(param.getApprdeptid());
        PageInfo<SelectTrnReqListResVO> resultListVO = null;
        try {
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }

            List<SelectTransStepResVO> list = null;
            List<SelectTrnReqListResVO> resList = bmsTrnReportMapper.selectTrnReqList(param);
            for(int i=0; i<resList.size(); i++) {
                list = bmsTrnStepMapper.selectTransStep(SelectTransStepReqVO.builder()
                                                            .transferid(resList.get(i).getTransferid())
                                                            .build());
                resList.get(i).setFinalTakeOver(list.get(0).getApprusername());
            }

            PageHelper.startPage(param);
            resultListVO = PageInfo.of(resList);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 인계인수 > 처리한 인계인수서 > 처리한 인계인수서 목록 조회 */
    @Override
    public PageInfo<BmsTrnCompleteListResVO> bmsTrnCompleteList(BmsTrnCompleteListReqVO param){
        param.setAppruserid(param.getAppruserid());
        param.setTransferid(param.getTransferid());

        List<String> apprstatusList = Arrays.asList(param.getApprstatus().split(","));
        param.setApprstatusList(apprstatusList);
        PageInfo<BmsTrnCompleteListResVO> resultListVO = null;
        try {
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }

            List<SelectTransStepResVO> list = null;
            List<BmsTrnCompleteListResVO> resList = bmsTrnReportMapper.bmsTrnCompleteList(param);
            for(int i=0; i<resList.size(); i++) {
                list = bmsTrnStepMapper.selectTransStep(SelectTransStepReqVO.builder()
                                                            .transferid(resList.get(i).getTransferid())
                                                            .build());
                resList.get(i).setFinalTakeOver(list.get(0).getApprusername());
            }

            PageHelper.startPage(param);
            resultListVO = PageInfo.of(resList);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 인계인수 > 인수현황함 > 인수현황함 목록 조회 */
    @Override
    public PageInfo<BmsTrnCurrentListResVO> bmsTrnCurrentList(BmsTrnCurrentListReqVO param){
        PageInfo<BmsTrnCurrentListResVO> resultListVO = null;
        try {
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(bmsTrnReportMapper.bmsTrnCurrentList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 인계자 & 인수자 정보 조회 */
    @Override
    public List<SelectTrnReqListResVO> selectTrnOverList(SelectTrnReqListReqVO param){
        param.setTransferid(param.getTransferid());

        List<SelectTrnReqListResVO> resultListVO;
        try {
            resultListVO = bmsTrnReportMapper.selectTrnOverList(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 인계인수 대상 목록 조회 */
    @Override
    public List<SelectTrnReqListResVO> selectTrnObjectList(SelectTrnReqListReqVO param){
        param.setTransferid(param.getTransferid());

        List<SelectTrnReqListResVO> resultListVO;
        try {
            resultListVO = bmsTrnReportMapper.selectTrnObjectList(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

}
