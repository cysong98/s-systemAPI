/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.service;


import com.anylogic.ssystem.bms.com.model.SelectOrganizationListReqVO;
import com.anylogic.ssystem.bms.com.model.SelectOrganizationListResVO;
import com.anylogic.ssystem.bms.com.model.SelectVgroupUserReqVO;
import com.anylogic.ssystem.bms.com.model.SelectVgroupUserResVO;
import com.anylogic.ssystem.bms.dct.mapper.*;
import com.anylogic.ssystem.bms.dct.model.*;
import com.anylogic.ssystem.bms.login.mapper.ComUserinfoMapper;
import com.anylogic.ssystem.bms.com.mapper.ComVgroupUserMapper;
import com.anylogic.ssystem.bms.login.model.GetUserLoginReqVO;
import com.anylogic.ssystem.bms.login.model.GetUserLoginResVO;
import com.anylogic.ssystem.bms.sse.SSEController;
import com.anylogic.ssystem.common.file.mapper.AttachFileSbmsMapper;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
import com.anylogic.ssystem.common.file.service.AttachFileSbmsService;
import com.anylogic.ssystem.common.util.DateUtil;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;


@Slf4j
@RequiredArgsConstructor
@Service("BmsDctRdocService")
@Transactional
public class BmsDctRdocServiceImpl implements BmsDctRdocService {

    @Autowired
    private AttachFileSbmsService attachFileService;

    @Autowired
    private AttachFileSbmsMapper attachFileMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;

    @Autowired
    private ComUserinfoMapper comUserinfoMapper;

    @Autowired
    private ComOrganizationinfoMapper comOrganizationinfoMapper;

    @Autowired
    private ComVgroupUserMapper comVgroupUserMapper;

    @Autowired
    private BmsDctPathMapper bmsDctPathMapper;

    @Autowired
    private BmsDctApprRecvMapper bmsDctApprRecvMapper;

    @Autowired
    private BmsDctRdocRcvsMapper bmsDctRdocRcvsMapper;

    @Autowired
    private BmsDctAttrMapper bmsDctAttrMapper;

    @Autowired
    private BmsDctFileMapper bmsDctFileMapper;

    @Autowired
    private BmsDctHistCardMapper bmsDctHistCardMapper;

    @Autowired
    private BmsDctMgmtRegiMapper bmsDctMgmtRegiMapper;

    @Autowired
    private BmsDctEnfDocMapper bmsDctEnfDocMapper;

    @Autowired
    private BmsDctEnfPathMapper bmsDctEnfPathMapper;

    @Autowired
    private BmsDctEnfRecvMapper bmsDctEnfRecvMapper;

    @Autowired
    private BmsDctEnfHistoryMapper bmsDctEnfHistoryMapper;

    @Autowired
    private BmsDctEnfPathHistoryMapper bmsDctEnfPathHistoryMapper;

    @Autowired
    private BmsDctSndrcvRegiMapper bmsDctSndrcvRegiMapper;

    @Autowired
    private BmsTaskInfomationTempMapper bmsTaskInfomationTempMapper;

    @Autowired
    private BmsDctOpinionMapper bmsDctOpinionMapper;

    @Autowired
    private BmsDctInvrcptMapper bmsDctInvrcptMapper;

    @Autowired
    private BmsDctDocSendinfoMapper bmsDctDocSendinfoMapper;

    @Autowired
    private BmsTaskInfomationMapper bmsTaskInfomationMapper;

    @Autowired
    private SSEController sseController;


    /* 관련근거 팝업 > 비밀문서 탭 > 목록 조회 */
    @Override
    public List<SelectReferenceListResVO> selectReferenceList(SelectReferenceListReqVO param){
        PageHelper.clearPage();
        List<SelectReferenceListResVO> resultListVO = new ArrayList<SelectReferenceListResVO>();
        try {
            resultListVO = bmsDctRdocMapper.selectReferenceList(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 발송 > 발송대기함 > 발송대기함 목록 조회 */
    @Override
    public PageInfo<SelectSendWaitListResVO> selectSendWaitList(SelectSendWaitListReqVO param){
        PageInfo<SelectSendWaitListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctRdocMapper.selectSendWaitList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 기안 문서처리 (결재경로 다수) & 접수 */
    @Override
    @Transactional
    public List<Map<String, List<AttachFileVO>>>  insertCardBIA(List<MultipartFile> draftAttach, List<MultipartFile> refAttach, InsertTempListReqVO param, String judgeParam, int insertCardBIASuccessCnt){
        List<Map<String, List<AttachFileVO>>> resListinsertCardBIA = new ArrayList<>();
        int resInsertCardBIA = 0;
        String resSysGuid = "";
        double totalSize = 1;
        double offsetForSend = 0;

        try {
            if(draftAttach != null)
                totalSize += draftAttach.size();
            if(refAttach != null)
                totalSize += refAttach.size();
            if(param.getFileList() != null)
                totalSize += param.getFileList().size();

            param.setDocid(param.getResdocid());

            // 00. CREATE SYS_GUID STRING
            resSysGuid = bmsDctRdocMapper.getSysGuid();
            String resDocid = "DCT" + resSysGuid;

            if(param.getResdocid().isEmpty()){
                param.setResdocid(resDocid);
            }

            Date today = new Date();
            Locale currentLocale = new Locale("KOREAN", "KOREA");
            String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

            // 01. INSERT BMS_DCT_RDOC TABLE
            resInsertCardBIA = 0;
            param.setReportdt(formatter.format(today));
            InsertTempListReqVO insertTempListReqVO = getRdocVO(param, formatter.format(today));
            if(Objects.equals(param.getJudgeParam(),"receipt")){ //접수문서를 기안하는 경우.
                insertTempListReqVO.setOridocid(param.getOridocid()); // 사본을 원본으로 가짐.
            }
            resInsertCardBIA = bmsDctRdocMapper.insertTempList(insertTempListReqVO);


            // 02. INSERT BMS_DCT_SUMMARY TABLE
            int resInsertSummary = 0;
            if( param.getSummary() != null && !Objects.equals(param.getSummary(), "")) {
                InsertTempListReqVO insertSummaryReqVO = getSummaryVO(param);
                resInsertSummary = bmsDctRdocMapper.insertSummary(insertSummaryReqVO);
            }


            // 03. INSERT BMS_DCT_ATTR TABLE
            int resInsertAttribute = 0;
            if(Objects.equals(judgeParam, "send")){
                param.setMgmtno(param.getMgmtno()); // 비밀등급-년도-시퀀스
                param.setDocno(param.getDocno());
                String sendcopyNo = "";
                int copyNoFisrt = insertCardBIASuccessCnt + 1;
                int copyNoMax = param.getRecvList().size();
                sendcopyNo = copyNoFisrt + "/" + copyNoMax;
                if(Objects.equals(param.getJudgeParam(), "distribute")){
                    param.setCopyno(param.getCopyno());
                }else{
                    param.setCopyno(sendcopyNo);
                }

            }else{
                int actSeq2 = bmsDctEnfPathMapper.getActSeq2();
                String recentRdoc = bmsDctRdocMapper.getRecentRdoc();
                // 가장 최근 비밀관리기록부 데이터의 등록일보다 DateUtil.getYear()가 크면 시퀀스 reset
                if(recentRdoc != null) {
                    if (DateUtil.getYear() > parseInt(recentRdoc.substring(0, 4))) {
                        actSeq2 = parseInt(bmsDctRdocMapper.resetActSeq2());
                    }
                }

                param.setDocno(param.getAuthordeptname() +"-S" + actSeq2);
            }
            InsertAttributeReqVO insertAttributeParam = getAttrVO(param);
            resInsertAttribute = bmsDctAttrMapper.insertAttribute(insertAttributeParam);


            // 04. INSERT BMS_DCT_PATH TABLE
            int resInsertPath = 0;
            int resInsertOpinion = 0; // 05. INSERT BMS_DCT_OPINION TABLE
            int idx = 0;
            SelectDctPathReqVO selectDctPathReqVO = new SelectDctPathReqVO();
            selectDctPathReqVO.setDocid(param.getResdocid());
            List<SelectDctPathResVO> selectDctPathResVOS = bmsDctPathMapper.selectDctPath(selectDctPathReqVO);
            if(!selectDctPathResVOS.isEmpty()){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_dct_path");
                paramMap.put("docid", param.getResdocid());
                bmsDctRdocMapper.deleteTempList(paramMap);
            }
            for (InsertTempListReqVO tempReqVO : param.getPathList()) {
                if(!Objects.equals(tempReqVO.getAsktype(), "")) {
                    if(idx == 0){
                        tempReqVO.setReportdt(formatter.format(today));
                        tempReqVO.setActapprdt(formatter.format(today));
                    }
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    InsertPathReqVO insertPathParam = getPathVO(tempReqVO, formatter.format(today));
                    resInsertPath += bmsDctPathMapper.insertPath(insertPathParam);
                    if(judgeParam != null && !judgeParam.isEmpty() && judgeParam.equals("send")){
                        tempReqVO.setOpinion(param.getPathList().get(idx).getOpinion());
                        tempReqVO.setPathorder(param.getPathList().get(idx).getPathorder());
                        tempReqVO.setOpinionpathorder(param.getPathList().get(idx).getPathorder());
                        InsertOpinionReqVO insertOpinionReqVO = getOpinionVO(tempReqVO);
                        resInsertOpinion += bmsDctOpinionMapper.insertDctOpinion(insertOpinionReqVO);
                    }
                    idx++;
                }
            }
            sseController.progressCheck(Math.round(0.25 / totalSize * 100) + "%");

            // 05. INSERT BMS_DCT_OPINION TABLE
            if(!Objects.equals(param.getOpinion(), "") && (judgeParam == null || judgeParam.isEmpty())) {
                InsertOpinionReqVO insertOpinionReqVO = getOpinionVO(param);
                resInsertOpinion = bmsDctOpinionMapper.insertDctOpinion(insertOpinionReqVO);
            }


            // 06. INSERT BMS_DCT_APPR_RECV
            int resInsertApprRecv = 0;
            GetDctRecvReqVO getDctRecvReqVO = new GetDctRecvReqVO();
            getDctRecvReqVO.setDocid(param.getResdocid());
            List<GetDctRecvResVO> dctRecv = bmsDctApprRecvMapper.getDctRecv(getDctRecvReqVO);
            if(!dctRecv.isEmpty() && ( !Objects.equals(param.getJudgeParam(), "receipt") && !Objects.equals(param.getJudgeParam(), "distribute")) ){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_dct_appr_recv");
                paramMap.put("docid", param.getResdocid());
                bmsDctRdocMapper.deleteTempList(paramMap);
            }
            if(judgeParam != null && judgeParam.equals("send")) {
                if (insertCardBIASuccessCnt >= 0 && insertCardBIASuccessCnt < param.getRecvList().size()) {
                    InsertTempListReqVO tempReqVO = param.getRecvList().get(insertCardBIASuccessCnt);
                    if (!Objects.equals(tempReqVO.getApprrecvdeptname(), "")) {
                        tempReqVO.setResdocid(param.getResdocid());
                        tempReqVO.setResuserid(param.getResuserid());
                        InsertRecvReqVO insertRecvParam = getApprRecvVO(tempReqVO, formatter.format(today));
                        insertRecvParam.setSendNameData(param.getRecvList().get(insertCardBIASuccessCnt).getDistname());
                        insertRecvParam.setSendOrgNameData(param.getRecvList().get(insertCardBIASuccessCnt).getDistdeptname());
                        if (Objects.equals(param.getJudgeParam(), "distribute")){
                            insertRecvParam.setSendNameData(param.getSendNameData());
                            insertRecvParam.setSendOrgNameData(param.getSendOrgNameData());
                        }
                        resInsertApprRecv += bmsDctApprRecvMapper.insertRecv(insertRecvParam);
                    }
                }
            }else{
                if(param.getRecvList() != null) {
                    for (InsertTempListReqVO tempReqVO : param.getRecvList()) {
                        if (!Objects.equals(tempReqVO.getApprrecvdeptname(), "")) {
                            tempReqVO.setResdocid(param.getResdocid());
                            tempReqVO.setResuserid(param.getResuserid());
                            tempReqVO.setSendOrgNameData(param.getSendOrgNameData());
                            tempReqVO.setSendNameData(param.getSendNameData());
                            InsertRecvReqVO insertRecvParam = getApprRecvVO(tempReqVO, formatter.format(today));
                            resInsertApprRecv += bmsDctApprRecvMapper.insertRecv(insertRecvParam);
                        }
                    }
                }
            }
            sseController.progressCheck(Math.round(0.5 / totalSize * 100) + "%");

            // 07. INSERT BMS_DCT_RDOC_RCVS
            int resInsertRdocRcvs = 0;
            SelectDctReaderReqVO selectDctReaderReqVO = new SelectDctReaderReqVO();
            selectDctReaderReqVO.setDocid(param.getResdocid());
            List<SelectDctReaderResVO> selectDctReaderResVOS = bmsDctRdocRcvsMapper.selectDctReader(selectDctReaderReqVO);
            if(!selectDctReaderResVOS.isEmpty()){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_dct_rdoc_rcvs");
                paramMap.put("docid", param.getResdocid());
                bmsDctRdocMapper.deleteTempList(paramMap);
            }
            for (InsertTempListReqVO tempReqVO : param.getRcvsList()) {
                if (!Objects.equals(tempReqVO.getRcvsusername(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    InsertDocIdBmsDctRdocRcvsListReqVO insertDocIdBmsDctRdocRcvsListParam = getRdocRcvsVO(tempReqVO, formatter.format(today));
                    resInsertRdocRcvs += bmsDctRdocRcvsMapper.insertDocIdBmsDctRdocRcvsList(insertDocIdBmsDctRdocRcvsListParam);
                }
            }

            // 2024-02-07 lbh, 추가
            Map<String, Object> docidData = new HashMap<>();
            docidData.put("docid", param.getResdocid());
            List<Map<String, Object>> selectList = bmsDctFileMapper.selectDctFileDFT02(docidData);

            List<AttachFileVO> refFileInfos = null;
            if( (judgeParam == null || judgeParam.isEmpty()) && !Objects.equals(param.getJudgeParam(), "receipt") ) {
                // 관련근거 - 직접입력 첨부파일
                AttachFileVO refFileVO = getFileVO(param, formatter.format(today));
                refFileVO.setGubun("DFT03");
                refFileVO.setTotal(totalSize);
                refFileInfos = attachFileService.insertAttachFile(refAttach, refFileVO);
            }else{
                // 사본에 대한 관련근거 - 직접입력 첨부파일
                AttachFileVO attachFileVO = getFileVO(param, formatter.format(today));
                attachFileVO.setGubun("DFT03");
                attachFileVO.setOridocid(param.getDocid());

                SelectTaskInfoTempReqVO selectTaskInfoTempReqVO = new SelectTaskInfoTempReqVO();

                if(Objects.equals(param.getJudgeParam(), "distribute")){

                }else{
                    selectTaskInfoTempReqVO.setOridocid(param.getOridocid());

                    List<SelectTaskInfoTempResVO> taskInfoList = bmsTaskInfomationTempMapper.selectTaskInfoJoinFile(selectTaskInfoTempReqVO);
                    List<InsertTempListReqVO> fileList = new ArrayList<>();
                    if (taskInfoList != null && !taskInfoList.isEmpty()) {
                        for (SelectTaskInfoTempResVO item : taskInfoList) {
                            InsertTempListReqVO tempItem = new InsertTempListReqVO();
                            tempItem.setDocid(item.getDocid());                 // 비밀 ID
                            tempItem.setFleid(item.getFleid());                 // 첨부 ID
                            tempItem.setSfilename(item.getSfilename());         // 파일고유이름
                            tempItem.setGubun(item.getGubun());                 // 구분
                            tempItem.setFlettl(item.getFlettl());               // 첨부명
                            tempItem.setFlesize(item.getFlesize());             // 첨부크기
                            tempItem.setFleorder(item.getFleorder());           // 첨부순서
                            tempItem.setFlepath(item.getFlepath());             // 파일경로
                            tempItem.setMarkflag(item.getMarkflag());           // 비밀등급표시여부
                            tempItem.setOrigubun(item.getOrigubun());           // 기존 문서의 구분
                            tempItem.setOridocid(item.getOridocid());           // 기존 문서의 비밀 ID
                            tempItem.setIsviewtype(item.getIsviewtype());       // 비전자 첨부구분
                            fileList.add(tempItem);
                        }
                        attachFileVO.setFileList(fileList);
                        if(fileList.size() > 0) {
                            totalSize = fileList.size() + param.getFileList().size() + param.getDraftfile().size();
                            offsetForSend = fileList.size();

                            attachFileVO.setTotal(totalSize);
                            refFileInfos = attachFileService.copyFile(attachFileVO, null, null);
                            Map<String, List<AttachFileVO>> fileInfoMap = new HashMap<>();
                            fileInfoMap.put("resFileInfosList", refFileInfos);
                            resListinsertCardBIA.add(fileInfoMap);
                        }
                    }
                }
            }

            // 08. INSERT BMS_TASK_INFOMATION TABLE
            int resInsertTaskInfo = 0;
            if(param.getDelRef() != null){
                for(String infoId : param.getDelRef()){

                    SelectTaskInfoTempReqVO selectTaskInfoTempReqVO = new SelectTaskInfoTempReqVO();
                    selectTaskInfoTempReqVO.setInfomationid(infoId);
                    List<SelectTaskInfoTempResVO> selectTaskInfoTempResVOS = bmsTaskInfomationTempMapper.selectTaskInfo(selectTaskInfoTempReqVO);
                    Optional<List<SelectTaskInfoTempResVO>> optionalSelectTaskInfoTempResVOS = Optional.ofNullable(selectTaskInfoTempResVOS);

                    if (optionalSelectTaskInfoTempResVOS.isPresent() && !optionalSelectTaskInfoTempResVOS.get().isEmpty()) {
                        String attathId = optionalSelectTaskInfoTempResVOS.get().get(0).getAttachid();
                        if(attathId != null){
                            AttachFileVO attachFileVO = new AttachFileVO();
                            attachFileVO.setFleid(attathId);
                            AttachFileVO attachFileByFleid = attachFileMapper.getAttachFileByFleid(attachFileVO);
                            String fileFullPath = attachFileByFleid.getFlepath();
                            File delFile = new File(fileFullPath);
                            delFile.delete();
                            attachFileMapper.deleteAttachFileByFleid(attachFileVO);
                        }
                    }

                    Map<String, Object> fileMap = new HashMap<>();
                    fileMap.put("infomationid", infoId);
                    bmsDctRdocMapper.deleteRefTemp(fileMap);
                }
            }

            int cnt = 0;
            int temp = 0;
            if(param.getRefList() != null && param.getRefList().size() > 0) {
                for (InsertTempListReqVO tempReqVO : param.getRefList()) {
                    if (!Objects.equals(tempReqVO.getInfotype(), "")) {
                        tempReqVO.setResdocid(param.getResdocid());
                        tempReqVO.setResuserid(param.getResuserid());

                        if (tempReqVO.getFileexist() != null) {
                            if (tempReqVO.getFileexist().equals("N")) {
                                resInsertTaskInfo = insertTaskInfo(tempReqVO, formatter, today, resInsertTaskInfo);
                            } else {
                                tempReqVO.setAttachid(refFileInfos.get(cnt++).getFleid());
                                resInsertTaskInfo = insertTaskInfo(tempReqVO, formatter, today, resInsertTaskInfo);
                            }
                        } else {

                            if (temp == 0 && (!Objects.equals(param.getJudgeParam(), "distribute"))) {
                                SelectTaskInfoTempReqVO selectTaskInfoTempReqVO = new SelectTaskInfoTempReqVO();
                                selectTaskInfoTempReqVO.setDocid(param.getResdocid());
                                List<SelectTaskInfoTempResVO> selectTaskInfoTempResVOS = null;
                                if (Objects.equals(param.getJudgeParam(), "receipt")) { //접수문서를 기안하는 경우.
                                    selectTaskInfoTempReqVO.setDocid(param.getOridocid());
                                    selectTaskInfoTempResVOS = bmsTaskInfomationTempMapper.selectTaskInfo(selectTaskInfoTempReqVO);
                                } else {
                                    selectTaskInfoTempResVOS = bmsTaskInfomationTempMapper.selectTaskInfoTemp(selectTaskInfoTempReqVO);
                                }
                                Optional<List<SelectTaskInfoTempResVO>> optionalSelectTaskInfoTempResVOS = Optional.ofNullable(selectTaskInfoTempResVOS);

                                if (optionalSelectTaskInfoTempResVOS.isPresent() && !optionalSelectTaskInfoTempResVOS.get().isEmpty()) {
                                    for (SelectTaskInfoTempResVO optionalSelectTaskInfoTempResVO : optionalSelectTaskInfoTempResVOS.get()) {
                                        InsertTaskInfomationtReqVO vo = InsertTaskInfomationtReqVO.builder()
                                                .infomationid("INF" + bmsDctRdocMapper.getSysGuid())
                                                .resultid(param.getResdocid())
                                                .attachid(optionalSelectTaskInfoTempResVO.getAttachid())
                                                .infotext(optionalSelectTaskInfoTempResVO.getInfotext())
                                                .infotype(optionalSelectTaskInfoTempResVO.getInfotype())
                                                .deleteyn("N")
                                                .infotitle(optionalSelectTaskInfoTempResVO.getInfotitle())
                                                .inid(optionalSelectTaskInfoTempResVO.getInid())
                                                .indt(formatter.format(today))
                                                .build();
                                        temp += bmsTaskInfomationMapper.insertTaskInfo(vo);
                                    }
                                }

                            }

                        }
                    }
                }
            }

            // 09. INSERT BMS_DCT_HIST_CARD TABLE (이력카드 저장)
            int resInsertHistory = 0;
//            String histSeq = bmsDctHistCardMapper.getHistSeq();
            String histSeq = "0";
            InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, histSeq, formatter.format(today));
            if(judgeParam != null && judgeParam.equals("send")) {
            }else{
                histSeq = bmsDctHistCardMapper.getHistSeq();
                insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
                resInsertHistory = bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
            }


            // 10. INSERT BMS_DCT_FILE TABLE
//            if(param.getDelFileids() != null){
//                AttachFileVO delFileVO = null;
//                // 새로 문서가 생성되는 경우 delFileids가 존재해도 파일 지우지 않도록
//                if(param.getDocid().equals(param.getResdocid())) {
//                    for (String DelFleid : param.getDelFileids()) {
//                        delFileVO = getDelFileVO(param.getResdocid(), DelFleid);
//                        attachFileService.deleteAttachFile(delFileVO);
//                    }
//                }
//            }


            if(param.getDelFileids() != null){
//                AttachFileVO delFileVO = null;
//                for (String DelFleid : param.getDelFileids()) {
//                    delFileVO = getDelFileVO(param.getResdocid(), DelFleid);
//                    attachFileService.deleteAttachFile(delFileVO);
//                }

                if(param.getDocid().equals(param.getResdocid())) {
                    List<String> fleidList = new ArrayList<>();
                    for (String DelFleid : param.getDelFileids()) {
                        fleidList.add(DelFleid);
                    }

                    Iterator<Map<String, Object>> iterator = selectList.iterator();
                    while (iterator.hasNext()) {
                        Map<String, Object> map = iterator.next();
                        String fleidValue = map.get("fleid").toString();

                        if (fleidList.contains(fleidValue)) {
                            iterator.remove();
                        }
                    }
                }
            }
            int fileCnt = draftAttach == null ? 0:draftAttach.size();
            if(!Objects.equals(param.getJudgeParam(), "distribute")) {
                for (Map<String, Object> selectData : selectList) {
                    InsertFileReqVO insertFileReqVO = new InsertFileReqVO();
                    insertFileReqVO.setDocid(param.getResdocid());

                    String resSysGuid2 = bmsDctRdocMapper.getSysGuid();
                    insertFileReqVO.setFleid("FLE" + resSysGuid2);

                    insertFileReqVO.setFleorder(fileCnt);
                    insertFileReqVO.setFlepath(selectData.get("flepath").toString());
                    insertFileReqVO.setFlesize((BigDecimal) selectData.get("flesize"));
                    insertFileReqVO.setFlettl(selectData.get("flettl").toString());
                    insertFileReqVO.setFleurl(selectData.get("fleurl").toString());
                    insertFileReqVO.setGubun(selectData.get("gubun").toString());
                    insertFileReqVO.setIndt(formatter.format(today));
                    insertFileReqVO.setInid(param.getResuserid());
                    insertFileReqVO.setMarkflag(selectData.get("markflag").toString());
                    insertFileReqVO.setOridocid(selectData.get("oridocid").toString());
                    insertFileReqVO.setOrigubun(selectData.get("origubun").toString());
                    insertFileReqVO.setSfilename(selectData.get("sfilename").toString());

                    bmsDctFileMapper.insertFile(insertFileReqVO);
                    fileCnt++;
                }
            }

            List<AttachFileVO> resDft02fileInfos = null;
            List<AttachFileVO> resDraftfileInfos = null;
            if(judgeParam != null && judgeParam.equals("send") && (!Objects.equals(param.getJudgeParam(), "distribute"))){ // 붙임파일 복사
                if(param.getFileList().size() > 0 && !param.getFileList().isEmpty() && !param.getFileList().get(0).getSfilename().equals("")) {
                    AttachFileVO attachFileVO = getFileVO(param, formatter.format(today));
                    attachFileVO.setGubun("DFT02");
                    attachFileVO.setOridocid(param.getDocid());
                    attachFileVO.setFileList(param.getFileList());
                    attachFileVO.setTotal(totalSize);
                    attachFileVO.setProgressOffset(offsetForSend);
                    offsetForSend += param.getFileList().size();

                    resDft02fileInfos = attachFileService.copyFile(attachFileVO, null, null);
                    Map<String, List<AttachFileVO>> dft02fileMap = new HashMap<>();
                    dft02fileMap.put("resDFT02fileList", resDft02fileInfos);
                    resListinsertCardBIA.add(dft02fileMap);
                }
                // 본문파일 복사
                if(param.getDraftfile().size() > 0 && !param.getDraftfile().isEmpty() && !param.getDraftfile().get(0).getSfilename().equals("")) {
                    AttachFileVO draftFileVO = getFileVO(param, formatter.format(today));
                    draftFileVO.setGubun("DFT01");
                    draftFileVO.setOridocid(param.getDocid());
                    draftFileVO.setFileList(param.getDraftfile());
                    draftFileVO.setTotal(totalSize);
                    draftFileVO.setProgressOffset(offsetForSend);

                    resDraftfileInfos = attachFileService.copyFile(draftFileVO, null, null);
                    System.out.println("refDraftfileInfos = " + resDraftfileInfos);
                    Map<String, List<AttachFileVO>> draftfileMap = new HashMap<>();
                    draftfileMap.put("resDraftfileList", resDraftfileInfos);
                    resListinsertCardBIA.add(draftfileMap);
                    System.out.println("resListinsertCardBIA = " + resListinsertCardBIA);
                }
            }else if(Objects.equals(param.getJudgeParam(), "receipt")){
                if(param.getFileList().size() > 0 && !param.getFileList().isEmpty() && !param.getFileList().get(0).getSfilename().equals("")) {
                    AttachFileVO attachFileVO = getFileVO(param, formatter.format(today));
                    attachFileVO.setGubun("DFT02");
                    attachFileVO.setOridocid(param.getDocid());
                    attachFileVO.setFileList(param.getFileList());
                    attachFileVO.setTotal(totalSize);
                    attachFileVO.setProgressOffset(offsetForSend);
                    offsetForSend += param.getFileList().size();

                    resDraftfileInfos = attachFileService.copyFile(attachFileVO, null, null);
                    Map<String, List<AttachFileVO>> dft02fileMap = new HashMap<>();
                    dft02fileMap.put("resDFT02fileList", resDraftfileInfos);
                    resListinsertCardBIA.add(dft02fileMap);
                }
                if(param.getDraftfile().size() > 0 && !param.getDraftfile().isEmpty() && !param.getDraftfile().get(0).getSfilename().equals("")) {
                    AttachFileVO draftFileVO = getFileVO(param, formatter.format(today));
                    draftFileVO.setGubun("DFT01");
                    draftFileVO.setOridocid(param.getDocid());
                    draftFileVO.setFileList(param.getDraftfile());
                    draftFileVO.setTotal(totalSize);
                    draftFileVO.setProgressOffset(offsetForSend);

                    resDraftfileInfos = attachFileService.copyFile(draftFileVO, null, null);
                    Map<String, List<AttachFileVO>> draftfileMap = new HashMap<>();
                    draftfileMap.put("resDraftfileList", resDraftfileInfos);
                    resListinsertCardBIA.add(draftfileMap);
                }
            }else{
                AttachFileVO attachFileVO = getFileVO(param, formatter.format(today));
                attachFileVO.setGubun("DFT02");
                attachFileVO.setTotal(totalSize);
                if(refAttach != null && (!Objects.equals(param.getJudgeParam(), "distribute"))) {
                    attachFileVO.setProgressOffset(refAttach.size());
                    attachFileService.insertAttachFile(draftAttach, attachFileVO);
                }
            }


            if(judgeParam != null && judgeParam.equals("send")) {

            }else if(param.getJudgeParam() != null && param.getJudgeParam().equals("receipt")){

            }else{
                // 본문파일 받아와서 변환
                if (param.getDrafturl() != null && !param.getDrafturl().equals("")) {
                    insertDraftFile(param, formatter, today);
                }
            }


        }

        catch(Exception e){
            throw new AnyXException(e);
        }

        return resListinsertCardBIA;
    }


    /* 기안 > 임시저장함 > 임시저장함 목록 조회 */
    @Override
    public PageInfo<SelectTempListResVO> selectTempList(SelectTempListReqVO param){
        PageInfo<SelectTempListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctRdocMapper.selectTempList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    @Override
    public PageInfo<SelectTempListResVO> getTempList(SelectTempListReqVO param) {
        return null;
    }

    /* 기안 > 임시저장함 > 삭제 */
    @Override
    @Transactional
    public List<Integer> deleteTempList(List<DeleteTempListReqVO> params){
        List<Integer> res = new ArrayList<>();

        List<String> tables = new ArrayList<>();
        tables.add("sbms.sbms_user.bms_dct_hist_card");
        tables.add("sbms.sbms_user.bms_dct_rdoc");
        tables.add("sbms.sbms_user.bms_dct_summary");
        tables.add("sbms.sbms_user.bms_dct_attr");
        tables.add("sbms.sbms_user.bms_dct_path");
        tables.add("sbms.sbms_user.bms_dct_appr_recv");
        tables.add("sbms.sbms_user.bms_dct_rdoc_rcvs");
        tables.add("sbms.sbms_user.bms_dct_file");

        try {
            for (DeleteTempListReqVO param : params) {
                String docid = param.getDocid();
                for (String table : tables) {
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("table", table);
                    paramMap.put("docid", docid);

                    if(table.equals("sbms.sbms_user.bms_dct_file")){
                        AttachFileVO attachFileVO = new AttachFileVO();
                        attachFileVO.setDocid(docid);
                        List<AttachFileVO> attachFileVOS = attachFileMapper.selectAttachFileByRecord(attachFileVO);
                        String fileFullPath = null;
                        for (AttachFileVO vo : attachFileVOS) {
                            fileFullPath = vo.getFlepath();
                            File delFile = new File(fileFullPath);
                            delFile.delete();
                        }
                    }

                    int ares = bmsDctRdocMapper.deleteTempList(paramMap);
                    res.add(ares);

                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("table", "sbms.sbms_user.bms_task_infomation_temp");
                    taskMap.put("docid", docid);
                    res.add(bmsDctRdocMapper.deleteTaskInfo(taskMap));

                }
            }
        }

        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* (개발 테스트용) 기안 > 기안완료함 > 잘못 생성한 기안문 삭제 */
    @Transactional
    public int deleteDraftCompleteList(List<DeleteDraftCompleteListReqVO> params){
        int res = 0;
        try {
            for(DeleteDraftCompleteListReqVO param : params){
                bmsDctRdocMapper.deleteDraftFile(param);
                AttachFileVO attachFileVO = new AttachFileVO();
                attachFileVO.setDocid(param.getDocid());
                List<AttachFileVO> attachFileVOS = attachFileMapper.selectAttachFileByRecord(attachFileVO);
                String fileFullPath = null;
                for (AttachFileVO vo : attachFileVOS) {
                    fileFullPath = vo.getFlepath();
                    File delFile = new File(fileFullPath);
                    delFile.delete();
                }
                bmsDctRdocMapper.deleteDraftTask(param);
                bmsDctRdocMapper.deleteDraftAuthCard(param);
                bmsDctRdocMapper.deleteDraftHistCard(param);
                bmsDctRdocMapper.deleteDraftRcvs(param);
                bmsDctRdocMapper.deleteDraftRecv(param);
                bmsDctRdocMapper.deleteDraftSummary(param);
                bmsDctRdocMapper.deleteDraftOpinion(param);
                bmsDctRdocMapper.deleteDraftPath(param);
                bmsDctRdocMapper.deleteDraftMgmt(param);
                bmsDctRdocMapper.deleteDraftAttr(param);
                res = bmsDctRdocMapper.deleteDraftRdoc(param);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }

    /* 기안 > 기안완료함 > 일괄반려삭제 */
    @Transactional
    public int rejectedDocumentToTempSave(List<RejectedDocumentToTempSaveReqVO> params){
        int res = 0;

        try {
            for(RejectedDocumentToTempSaveReqVO param : params){
                bmsDctRdocMapper.deleteRejectedOpinion(param);
                bmsDctRdocMapper.updateRejectedPath(param);
                bmsDctRdocMapper.deleteRejectedPath(param);
                bmsDctRdocMapper.convertTaskinfoToTaskinfotemp(param);
                bmsDctRdocMapper.deleteRejectedTaskinfo(param);
                bmsDctRdocMapper.updateRejectedRecv(param);
                res = bmsDctRdocMapper.updateRejectedRdoc(param);
            }
        }

        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }

    /* 기안 임시저장 */
    @Override
    @Transactional
    public int insertTempList(List<MultipartFile> draftAttach, List<MultipartFile> refAttach, InsertTempListReqVO param) {
        int resInsertTempList = 0;
        String resSysGuid = "";
        double totalSize = 1;

        try {
            if(draftAttach != null)
                totalSize += draftAttach.size();
            if(refAttach != null)
                totalSize += refAttach.size();
            if(param.getFileList() != null)
                totalSize += param.getFileList().size();

            // 00. CREATE SYS_GUID STRING
            resSysGuid = bmsDctRdocMapper.getSysGuid();
            String resDocid = "DCT" + resSysGuid;

            if(param.getResdocid().isEmpty()){
                param.setResdocid(resDocid);
            }

            Date today = new Date();
            Locale currentLocale = new Locale("KOREAN", "KOREA");
            String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

            // 01. INSERT BMS_DCT_RDOC TABLE
            resInsertTempList = 0;
            InsertTempListReqVO insertTempListReqVO = getRdocVO(param, formatter.format(today));
            resInsertTempList = bmsDctRdocMapper.insertTempList(insertTempListReqVO);


            // 02. INSERT BMS_DCT_SUMMARY TABLE
            int resInsertSummary = 0;
            if(!Objects.equals(param.getSummary(), "")) {
                InsertTempListReqVO insertSummaryReqVO = getSummaryVO(param);
                resInsertSummary = bmsDctRdocMapper.insertSummary(insertSummaryReqVO);
            }


            // 03. INSERT BMS_DCT_ATTR TABLE
            int resInsertAttribute = 0;
            InsertAttributeReqVO insertAttributeParam = getAttrVO(param);
            resInsertAttribute = bmsDctAttrMapper.insertAttribute(insertAttributeParam);


            // 04. INSERT BMS_DCT_PATH TABLE
            int resInsertPath = 0;
            SelectDctPathReqVO selectDctPathReqVO = new SelectDctPathReqVO();
            selectDctPathReqVO.setDocid(param.getResdocid());
            List<SelectDctPathResVO> selectDctPathResVOS = bmsDctPathMapper.selectDctPath(selectDctPathReqVO);
            if(!selectDctPathResVOS.isEmpty() && !Objects.equals(param.getJudgeParam(), "receipt")){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_dct_path");
                paramMap.put("docid", param.getResdocid());
                bmsDctRdocMapper.deleteTempList(paramMap);
            }
            for (InsertTempListReqVO tempReqVO : param.getPathList()) {
                if(!Objects.equals(tempReqVO.getAsktype(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    InsertPathReqVO insertPathParam = getPathVO(tempReqVO, formatter.format(today));
                    resInsertPath += bmsDctPathMapper.insertPath(insertPathParam);
                }
            }


            // 06. INSERT BMS_DCT_APPR_RECV
            int resInsertApprRecv = 0;
            GetDctRecvReqVO getDctRecvReqVO = new GetDctRecvReqVO();
            getDctRecvReqVO.setDocid(param.getResdocid());
            List<GetDctRecvResVO> dctRecv = bmsDctApprRecvMapper.getDctRecv(getDctRecvReqVO);
            if(!dctRecv.isEmpty()){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_dct_appr_recv");
                paramMap.put("docid", param.getResdocid());
                bmsDctRdocMapper.deleteTempList(paramMap);
            }
            for (InsertTempListReqVO tempReqVO : param.getRecvList()) {
                if (!Objects.equals(tempReqVO.getApprrecvdeptname(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    tempReqVO.setSendOrgNameData(param.getSendOrgNameData());
                    tempReqVO.setSendNameData(param.getSendNameData());
                    InsertRecvReqVO insertRecvParam = getApprRecvVO(tempReqVO, formatter.format(today));
                    resInsertApprRecv += bmsDctApprRecvMapper.insertRecv(insertRecvParam);
                }
            }


            // 07. INSERT BMS_DCT_RDOC_RCVS
            int resInsertRdocRcvs = 0;
            SelectDctReaderReqVO selectDctReaderReqVO = new SelectDctReaderReqVO();
            selectDctReaderReqVO.setDocid(param.getResdocid());
            List<SelectDctReaderResVO> selectDctReaderResVOS = bmsDctRdocRcvsMapper.selectDctReader(selectDctReaderReqVO);
            if(!selectDctReaderResVOS.isEmpty()){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_dct_rdoc_rcvs");
                paramMap.put("docid", param.getResdocid());
                bmsDctRdocMapper.deleteTempList(paramMap);
            }
            for (InsertTempListReqVO tempReqVO : param.getRcvsList()) {
                if (!Objects.equals(tempReqVO.getRcvsusername(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    InsertDocIdBmsDctRdocRcvsListReqVO insertDocIdBmsDctRdocRcvsListParam = getRdocRcvsVO(tempReqVO, formatter.format(today));
                    resInsertRdocRcvs += bmsDctRdocRcvsMapper.insertDocIdBmsDctRdocRcvsList(insertDocIdBmsDctRdocRcvsListParam);
                }
            }

            // 2024-02-07 lbh, 추가
            Map<String, Object> docidData = new HashMap<>();
            docidData.put("docid", param.getResdocid());
            List<Map<String, Object>> selectList = bmsDctFileMapper.selectDctFileDFT02(docidData);

            // 관련근거 - 직접입력 첨부파일
            AttachFileVO refFileVO = getFileVO(param, formatter.format(today));
            refFileVO.setGubun("DFT03");
            refFileVO.setTotal(totalSize);
            List<AttachFileVO> refFileInfos = attachFileService.insertAttachFile(refAttach, refFileVO);


            // 08. INSERT BMS_TASK_INFOMATION_TEMP TABLE
            int resInsertTaskInfo = 0;
            if (param.getDelRef() != null) {
                for (String infoId : param.getDelRef()) {

                    SelectTaskInfoTempReqVO selectTaskInfoTempReqVO = new SelectTaskInfoTempReqVO();
                    selectTaskInfoTempReqVO.setInfomationid(infoId);
                    List<SelectTaskInfoTempResVO> selectTaskInfoTempResVOS = bmsTaskInfomationTempMapper.selectTaskInfoTemp(selectTaskInfoTempReqVO);
                    Optional<List<SelectTaskInfoTempResVO>> optionalSelectTaskInfoTempResVOS = Optional.ofNullable(selectTaskInfoTempResVOS);

                    if (optionalSelectTaskInfoTempResVOS.isPresent() && !optionalSelectTaskInfoTempResVOS.get().isEmpty()) {
                        String attathId = selectTaskInfoTempResVOS.get(0).getAttachid();
                        if (attathId != null) {
                            AttachFileVO attachFileVO = new AttachFileVO();
                            attachFileVO.setFleid(attathId);
                            AttachFileVO attachFileByFleid = attachFileMapper.getAttachFileByFleid(attachFileVO);
                            String fileFullPath = attachFileByFleid.getFlepath();
                            File delFile = new File(fileFullPath);
                            delFile.delete();
                            attachFileMapper.deleteAttachFileByFleid(attachFileVO);
                        }
                    }

                    Map<String, Object> fileMap = new HashMap<>();
                    fileMap.put("infomationid", infoId);
                    bmsDctRdocMapper.deleteRefTemp(fileMap);
                }
            }

            int cnt = 0;
            for (InsertTempListReqVO tempReqVO : param.getRefList()) {
                SelectTaskInfoTempReqVO selectTaskInfoTempReqVO = new SelectTaskInfoTempReqVO();
                selectTaskInfoTempReqVO.setDocid(param.getResdocid());
                List<SelectTaskInfoTempResVO> selectTaskInfoTempResVOS = bmsTaskInfomationTempMapper.selectTaskInfoTemp(selectTaskInfoTempReqVO);

                if (!Objects.equals(tempReqVO.getInfotype(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());

                    if(tempReqVO.getFileexist() != null){
                        if(tempReqVO.getFileexist().equals("N")){
                            resInsertTaskInfo = insertTaskInfoTemp(tempReqVO, formatter, today, resInsertTaskInfo);
                        }else{
                            tempReqVO.setAttachid(refFileInfos.get(cnt++).getFleid());
                            resInsertTaskInfo = insertTaskInfoTemp(tempReqVO, formatter, today, resInsertTaskInfo);
                        }
                    }

                }
            }

            // 10. INSERT BMS_DCT_FILE TABLE
//            if(param.getDelFileids() != null){
//                AttachFileVO delFileVO = null;
//                for (String DelFleid : param.getDelFileids()) {
//                    delFileVO = getDelFileVO(param.getResdocid(), DelFleid);
//                    attachFileService.deleteAttachFile(delFileVO);
//                }
//            }

            if(param.getDelFileids() != null){
//                AttachFileVO delFileVO = null;
//                for (String DelFleid : param.getDelFileids()) {
//                    delFileVO = getDelFileVO(param.getResdocid(), DelFleid);
//                    attachFileService.deleteAttachFile(delFileVO);
//                }

                if(param.getDocid().equals(param.getResdocid())) {
                    List<String> fleidList = new ArrayList<>();
                    for (String DelFleid : param.getDelFileids()) {
                        fleidList.add(DelFleid);
                    }

                    Iterator<Map<String, Object>> iterator = selectList.iterator();
                    while (iterator.hasNext()) {
                        Map<String, Object> map = iterator.next();
                        String fleidValue = map.get("fleid").toString();

                        if (fleidList.contains(fleidValue)) {
                            iterator.remove();
                        }
                    }
                }
            }
            int fileCnt = draftAttach == null ? 0:draftAttach.size();
            for(Map<String, Object> selectData : selectList) {
                InsertFileReqVO insertFileReqVO = new InsertFileReqVO();
                insertFileReqVO.setDocid(param.getResdocid());

                String resSysGuid2 = bmsDctRdocMapper.getSysGuid();
                insertFileReqVO.setFleid("FLE"+resSysGuid2);

                insertFileReqVO.setFleorder(fileCnt);
                insertFileReqVO.setFlepath(selectData.get("flepath").toString());
                insertFileReqVO.setFlesize((BigDecimal) selectData.get("flesize"));
                insertFileReqVO.setFlettl(selectData.get("flettl").toString());
                insertFileReqVO.setFleurl(selectData.get("fleurl").toString());
                insertFileReqVO.setGubun(selectData.get("gubun").toString());
                insertFileReqVO.setIndt(formatter.format(today));
                insertFileReqVO.setInid(param.getResuserid());
                insertFileReqVO.setMarkflag(selectData.get("markflag").toString());
                insertFileReqVO.setOridocid(selectData.get("oridocid").toString());
                insertFileReqVO.setOrigubun(selectData.get("origubun").toString());
                insertFileReqVO.setSfilename(selectData.get("sfilename").toString());

                bmsDctFileMapper.insertFile(insertFileReqVO);
                fileCnt++;
            }

            AttachFileVO attachFileVO = getFileVO(param,formatter.format(today));
            attachFileVO.setGubun("DFT02");
            attachFileVO.setTotal(totalSize);
            if(refAttach != null)
                attachFileVO.setProgressOffset(refAttach.size());
            attachFileService.insertAttachFile(draftAttach, attachFileVO);

            // 재사용 등록일 경우 (기존 첨부파일 복사)
            Optional.ofNullable(param.getIsreuse())
                    .filter(isReuse -> isReuse.equals("Y"))
                    .ifPresent(isReuse -> {

                        List<InsertTempListReqVO> filelist = param.getFileList();
                        if (!filelist.isEmpty()) {
                            AttachFileVO fileVO = getFileVO(param, formatter.format(today));
                            fileVO.setGubun("DFT02");
                            fileVO.setFileList(filelist);
                            attachFileService.copyFile(fileVO, null, "reuse");
                        }

                    });


            // 본문파일 받아와서 변환
            if(!param.getDrafturl().equals("") && param.getDrafturl() != null) {
                AttachFileVO vo = new AttachFileVO();
                vo.setDocid(param.getResdocid());
                vo.setGubun("DFT01");
                vo.setInid(param.getResuserid());
                List<AttachFileVO> attachFileVOS = attachFileMapper.selectAttachFileByRecord(vo);
                Optional<List<AttachFileVO>> optionalattachFileVOS = Optional.ofNullable(attachFileVOS);

                // insert
                if(!param.getDrafturl().contains("fileUpload")) {
                    insertDraftFile(param, formatter, today);
                } else {
                    List<InsertTempListReqVO> draftfile = new ArrayList<>();
                    for(InsertTempListReqVO file : param.getDraftfile()){
                        if(file.getInid().equals(param.getResuserid())) {
                            draftfile.add(file);
                        }
                    }

                    if (!draftfile.isEmpty()) {
                        AttachFileVO fileVO = getFileVO(param, formatter.format(today));
                        fileVO.setSfilename(param.getDocttl());
                        fileVO.setGubun("DFT01");
                        fileVO.setFileList(draftfile);
                        attachFileService.copyFile(fileVO, null, "reuse");
                    }
                }

                if (optionalattachFileVOS.isPresent() && !optionalattachFileVOS.get().isEmpty()) {
                    // 이전의 본문 파일은 삭제
                    AttachFileVO delFileVO = null;
                    for (AttachFileVO delFile : attachFileVOS) {
                        String fleid = delFile.getFleid();
                        delFileVO = getDelFileVO(param.getResdocid(), fleid);
                        attachFileService.deleteAttachFile(delFileVO);
                    }
                }
            }


        } catch (Exception e) {
            throw new AnyXException(e);
        }

        return resInsertTempList;
    }

    // 관련근거 임시저장
    private int insertTaskInfoTemp(InsertTempListReqVO tempReqVO, SimpleDateFormat formatter, Date today, int resInsertTaskInfo) {
        tempReqVO.setInfomationid("INF" + bmsDctRdocMapper.getSysGuid());
        InsertTaskInfomationtReqVO insertTaskInfoParam = getTakInfoVO(tempReqVO, formatter.format(today));
        resInsertTaskInfo += bmsTaskInfomationMapper.insertTaskInfoTemp(insertTaskInfoParam);
        return resInsertTaskInfo;
    }

    // 관련근거 문서처리
    private int insertTaskInfo(InsertTempListReqVO tempReqVO, SimpleDateFormat formatter, Date today, int resInsertTaskInfo) {
        tempReqVO.setInfomationid("INF" + bmsDctRdocMapper.getSysGuid());
        InsertTaskInfomationtReqVO insertTaskInfoParam = getTakInfoVO(tempReqVO, formatter.format(today));
        resInsertTaskInfo += bmsTaskInfomationMapper.insertTaskInfo(insertTaskInfoParam);
        return resInsertTaskInfo;
    }


    // 01. INSERT BMS_DCT_RDOC TABLE
    private InsertTempListReqVO getRdocVO(InsertTempListReqVO vo, String indt){
        InsertTempListReqVO build = InsertTempListReqVO.builder()
                .docid(vo.getResdocid())
                .docttl(vo.getDocttl())
                .gubun(vo.getGubun())
                .state(vo.getState())
                .open(vo.getOpen())
                .readrangetype(vo.getReadrangetype())
                .emergency(vo.getEmergency())
                .keyword(vo.getKeyword())
                .authorid(vo.getAuthorid())
                .authorname(vo.getAuthorname())
                .authordutyname(vo.getAuthordutyname())
                .authordeptid(vo.getAuthordeptid())
                .authordeptname(vo.getAuthordeptname())
                .tempflag(vo.getTempflag())
                .indt(indt)
                .updt(indt)
                .reportdt(vo.getReportdt())
                .oridocid(vo.getOridocid())
                .build();
        return build;
    }

    // 02. INSERT BMS_DCT_SUMMARY TABLE
    private InsertTempListReqVO getSummaryVO (InsertTempListReqVO vo){
        InsertTempListReqVO build = InsertTempListReqVO.builder()
                .docid(vo.getResdocid())
                .docgubun(vo.getDocgubun())
                .summary(vo.getSummary())
                .build();
        return build;
    }

    // 03. INSERT BMS_DCT_ATTR TABLE
    private InsertAttributeReqVO getAttrVO (InsertTempListReqVO vo){
        InsertAttributeReqVO build = InsertAttributeReqVO.builder()
                .docid(vo.getResdocid())
                .secgubun(vo.getSecgubun())
                .seclevel(vo.getSeclevel())
                .prsrvterm(vo.getPrsrvterm())
                .orgnprotdt(vo.getOrgnprotdt())
                .orgnnextlevel(vo.getOrgnnextlevel())
                .orgnreclass(vo.getOrgnreclass())
                .copyprotdt(vo.getCopyprotdt())
                .copynextlevel(vo.getCopynextlevel())
                .copyreclass(vo.getCopyreclass())
                .docno(vo.getDocno())
                .mgmtno(vo.getMgmtno())
                .copyno(vo.getCopyno())
                .build();
        return build;
    }

    // 04. INSERT BMS_DCT_PATH TABLE
    private InsertPathReqVO getPathVO (InsertTempListReqVO vo, String indt){
        InsertPathReqVO build = InsertPathReqVO.builder()
                .docid(vo.getResdocid())
                .pathorder(new BigDecimal(vo.getPathorder()))
                .state(vo.getPathstate())
                .objid(vo.getObjid())
                .objname(vo.getObjname())
                .deptid(vo.getDeptid())
                .deptname(vo.getDeptname())
                .dutyname(vo.getDutyname())
                .dutynamedesc(vo.getDutynamedesc())
                .electsignid(vo.getElectsignid())
                .asktype(vo.getAsktype())
                .acttype(vo.getActtype())
                .mainver(vo.getMainver())
                .indt(indt)
                .reportdt(vo.getReportdt())
                .actapprdt(vo.getActapprdt())
                .inid(vo.getResuserid())
                .build();
        return build;
    }

    // 05. INSERT BMS_DCT_OPINION TABLE
    private InsertOpinionReqVO getOpinionVO (InsertTempListReqVO vo){
        InsertOpinionReqVO build = InsertOpinionReqVO.builder()
                .docid(vo.getResdocid())
                .pathorder(new BigDecimal(vo.getOpinionpathorder()))
                .opinion(vo.getOpinion())
                .build();
        return build;
    }

    // 06. INSERT BMS_DCT_APPR_RECV
    private InsertRecvReqVO getApprRecvVO (InsertTempListReqVO vo, String indt){
        InsertRecvReqVO build = InsertRecvReqVO.builder()
                .docid(vo.getResdocid())
                .receiverid(vo.getReceiverid())
                .tempflag(vo.getApprrecvtempflag())
                .seq(new BigDecimal(vo.getApprrecvseq()))
                .gubun(vo.getApprrecvgubun())
                .deptid(vo.getReceiverid())
                .deptname(vo.getApprrecvdeptname())
                .refdeptname(vo.getApprrecvrefdeptname())
                .sendOrgNameData(vo.getSendOrgNameData())
                .sendNameData(vo.getSendNameData())
                .inid(vo.getResuserid())
                .indt(indt)
                .recsymbol(vo.getRecsymbol())
                .relaytype(vo.getRelaytype())
                .build();
        return build;
    }

    // 07. INSERT BMS_DCT_RDOC_RCVS
    private InsertDocIdBmsDctRdocRcvsListReqVO getRdocRcvsVO (InsertTempListReqVO vo, String indt){
        InsertDocIdBmsDctRdocRcvsListReqVO build = InsertDocIdBmsDctRdocRcvsListReqVO.builder()
                .docid(vo.getResdocid())
                .userid(vo.getRcvsuserid())
                .username(vo.getRcvsusername())
                .deptid(vo.getRcvdeptid())
                .deptname(vo.getRcvdeptname())
                .dutyname(vo.getRcvdutyname())
                .inseq(vo.getRcvsinseq())
                .indt(indt)
                .inid(vo.getResuserid())
                .build();
        return build;
    }

    // 08. INSERT BMS_TASK_INFOMATION (_TEMP) TABLE
    private InsertTaskInfomationtReqVO getTakInfoVO (InsertTempListReqVO vo, String indt){
        InsertTaskInfomationtReqVO build = InsertTaskInfomationtReqVO.builder()
                .infomationid(vo.getInfomationid())
                .resultid(vo.getResdocid())
                .attachid(vo.getAttachid())
                .infotext(vo.getInfotext())
                .infotype(vo.getInfotype())
                .deleteyn("N")
                .infotitle(vo.getInfotitle())
                .inid(vo.getResuserid())
                .indt(indt)
                .build();
        return build;
    }

    // 9. INSERT BMS_DCT_HIST_CARD TABLE
    private InsertHistoryReqVO getHistoryVO (InsertTempListReqVO vo, String histSeq, String indt){
        InsertHistoryReqVO build = InsertHistoryReqVO.builder()
                .docid(vo.getResdocid())
                .histdt(indt)
                .histseq(new BigDecimal(histSeq))
                .apprreqid(vo.getApprreqid())
                .histgubun(vo.getHistgubun())
                .deptid(vo.getDeptid())
                .deptname(vo.getDeptname())
                .dutyname(vo.getDutyname())
                .userid(vo.getResuserid())
                .username(vo.getUsername())
                .histstatus(vo.getHiststatus())
                .etc(vo.getEtc())
                .electsignid(vo.getElectsignid())
                .build();
        return build;
    }

    // 10. INSERT BMS_DCT_FILE TABLE
    private AttachFileVO getFileVO (InsertTempListReqVO vo, String indt){
        AttachFileVO build = AttachFileVO.builder()
                .docid(vo.getResdocid())
                .indt(indt)
                .inid(vo.getResuserid())
                .origubun(vo.getGubun())
                .oridocid(vo.getResdocid())
                .build();
        return build;
    }

    // 10-1. DELETE BMS_DCT_FILE TABLE
    private AttachFileVO getDelFileVO (String docid, String fleid){
        AttachFileVO build = AttachFileVO.builder()
                .docid(docid)
                .fleid(fleid)
                .build();
        return build;
    }


    /* 재사용 문서처리 & 임시저장 */
    @Override
    @Transactional
    public int insertReuseDraft(List<MultipartFile> draftAttach, List<MultipartFile> refAttach, InsertTempListReqVO param) {
        int resInsertReuseDraft = 0;
        String resSysGuid = "";
        String isTemp = "";
        String isOneApprove = "";
        double totalSize = 1;
        double offsetForSend = 0;

        try {
            if(draftAttach != null)
                totalSize += draftAttach.size();
            if(refAttach != null)
                totalSize += refAttach.size();
            if(param.getFileList() != null)
                totalSize += param.getFileList().size();

            Date today = new Date();
            Locale currentLocale = new Locale("KOREAN", "KOREA");
            String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

            // 문서처리(N),임시저장(Y) 구분
            isTemp = param.getTempflag();

            // 문서처리일 경우 결재(N), 1인결재(Y) 구분
            if("N".equals(isTemp)){
                isOneApprove = param.getIsOneApprove();
            }

            // 00. CREATE SYS_GUID STRING
            resSysGuid = bmsDctRdocMapper.getSysGuid();
            String resDocid = "DCT" + resSysGuid;
            param.setResdocid(resDocid);

            // 01. INSERT BMS_DCT_RDOC TABLE
            param.setReportdt(formatter.format(today));
            InsertTempListReqVO insertTempListReqVO = getRdocVO(param, formatter.format(today));
            resInsertReuseDraft = bmsDctRdocMapper.insertTempList(insertTempListReqVO);

            // 02. INSERT BMS_DCT_SUMMARY TABLE
            int resInsertSummary = 0;
            if(!Objects.equals(param.getSummary(), "")) {
                InsertTempListReqVO insertSummaryReqVO = getSummaryVO(param);
                resInsertSummary = bmsDctRdocMapper.insertSummary(insertSummaryReqVO);
            }

            // 03. INSERT BMS_DCT_ATTR TABLE
            int resInsertAttribute = 0;
            if("N".equals(isTemp)) {
                int actSeq2 = bmsDctEnfPathMapper.getActSeq2();
                String recentRdoc = bmsDctRdocMapper.getRecentRdoc();
                // 가장 최근 비밀관리기록부 데이터의 등록일보다 DateUtil.getYear()가 크면 시퀀스 reset
                if(recentRdoc != null) {
                    if (DateUtil.getYear() > parseInt(recentRdoc.substring(0, 4))) {
                        actSeq2 = parseInt(bmsDctRdocMapper.resetActSeq2());
                    }
                }
                param.setDocno(param.getAuthordeptname() +"-S" + actSeq2);
            }
            InsertAttributeReqVO insertAttributeParam = getAttrVO(param);
            resInsertAttribute = bmsDctAttrMapper.insertAttribute(insertAttributeParam);

            // 04. INSERT BMS_DCT_PATH TABLE
            int resInsertPath = 0;
            for (InsertTempListReqVO tempReqVO : param.getPathList()) {
                if(!Objects.equals(tempReqVO.getAsktype(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    if("N".equals(isTemp) && "1".equals(tempReqVO.getPathorder())){
                        tempReqVO.setReportdt(formatter.format(today));
                    }
                    InsertPathReqVO insertPathParam = getPathVO(tempReqVO, formatter.format(today));
                    resInsertPath += bmsDctPathMapper.insertPath(insertPathParam);
                }
            }
            sseController.progressCheck(Math.round(0.25 / totalSize * 100) + "%");

            // 05. INSERT BMS_DCT_OPINION TABLE - 문서처리일 경우에만 등록
            int resInsertOpinion = 0;
            if("N".equals(isTemp)){
                if(!Objects.equals(param.getOpinion(), "")) {
                    InsertOpinionReqVO insertOpinionReqVO = getOpinionVO(param);
                    resInsertOpinion = bmsDctOpinionMapper.insertDctOpinion(insertOpinionReqVO);
                }
            }

            // 06. INSERT BMS_DCT_APPR_RECV
            int resInsertApprRecv = 0;
            for (InsertTempListReqVO tempReqVO : param.getRecvList()) {
                if (!Objects.equals(tempReqVO.getApprrecvdeptname(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    tempReqVO.setSendOrgNameData(param.getSendOrgNameData());
                    tempReqVO.setSendNameData(param.getSendNameData());
                    InsertRecvReqVO insertRecvParam = getApprRecvVO(tempReqVO, formatter.format(today));
                    resInsertApprRecv += bmsDctApprRecvMapper.insertRecv(insertRecvParam);
                }
            }
            sseController.progressCheck(Math.round(0.5 / totalSize * 100) + "%");

            // 07. INSERT BMS_DCT_RDOC_RCVS
            int resInsertRdocRcvs = 0;
            for (InsertTempListReqVO tempReqVO : param.getRcvsList()) {
                if (!Objects.equals(tempReqVO.getRcvsusername(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    InsertDocIdBmsDctRdocRcvsListReqVO insertDocIdBmsDctRdocRcvsListParam = getRdocRcvsVO(tempReqVO, formatter.format(today));
                    resInsertRdocRcvs += bmsDctRdocRcvsMapper.insertDocIdBmsDctRdocRcvsList(insertDocIdBmsDctRdocRcvsListParam);
                }
            }

            // 08. 관련근거 - 직접입력 첨부파일 (재사용문서 기존x, 새로 추가한 MultipartFile) BMS_DCT_FILE
            AttachFileVO refFileVO = getFileVO(param, formatter.format(today));
            refFileVO.setGubun("DFT03");
            refFileVO.setTotal(totalSize);
            List<AttachFileVO> refFileInfos = attachFileService.insertAttachFile(refAttach, refFileVO);
            if(refAttach != null)
                offsetForSend += refAttach.size();

            // 08-1. INSERT BMS_TASK_INFOMATION 또는 INSERT BMS_TASK_INFOMATION_TEMP TABLE
            int resInsertTaskInfo = 0;
            int cnt = 0;
            for (InsertTempListReqVO tempReqVO : param.getRefList()) {
                if (!Objects.equals(tempReqVO.getInfotype(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                     if(tempReqVO.getSfilename() != null && tempReqVO.getFileexist() == null){ // 첨부O 기존 직접입력
                        SelectTaskInfoTempReqVO selectTaskInfoTempReqVO = new SelectTaskInfoTempReqVO();
                        selectTaskInfoTempReqVO.setInfomationid(tempReqVO.getInfomationid());
                        List<SelectTaskInfoTempResVO> taskInfoList = bmsTaskInfomationTempMapper.selectTaskInfoJoinFile(selectTaskInfoTempReqVO);
                        String attachid = taskInfoList.get(0).getAttachid();
                        tempReqVO.setAttachid(attachid);
                    }else if("Y".equals(tempReqVO.getFileexist())){ // 첨부O 추가한 직접입력
                        tempReqVO.setAttachid(refFileInfos.get(cnt++).getFleid());
                    }else{
                        tempReqVO.setAttachid(null);
                    }

                    if("N".equals(isTemp)){ // 문서처리
                        resInsertTaskInfo = insertTaskInfo(tempReqVO, formatter, today, resInsertTaskInfo);
                    }else{ // 임시저장
                        resInsertTaskInfo = insertTaskInfoTemp(tempReqVO, formatter, today, resInsertTaskInfo);
                    }

                }
            }

            // 09. INSERT BMS_DCT_HIST_CARD TABLE
            int resInsertHistory = 0;
            if("N".equals(isTemp)) { // 문서처리
                if("Y".equals(isOneApprove)){ // 1인결재
                    // 기안
                    param.setHistgubun("01");
                    param.setHiststatus("010");
                    insertHist(param, formatter, today);
                    // 결재
                    param.setHistgubun("02");
                    param.setHiststatus("011");
                    insertHist(param, formatter, today);
                }else{
                    insertHist(param, formatter, today);
                }
            }

            // 10. INSERT BMS_DCT_FILE TABLE
            // 재사용문서 기존x, 붙임 새로 추가한 MultipartFile
            AttachFileVO attachFileVO = getFileVO(param,formatter.format(today));
            attachFileVO.setGubun("DFT02");
            attachFileVO.setTotal(totalSize);
            attachFileVO.setProgressOffset(offsetForSend);
            attachFileService.insertAttachFile(draftAttach, attachFileVO);
            if(draftAttach != null)
                offsetForSend += draftAttach.size();

            // 재사용문서 기존 붙임파일 복사
            List<InsertTempListReqVO> filelist = param.getFileList();
            if (!filelist.isEmpty()) {
                AttachFileVO fileVO = getFileVO(param, formatter.format(today));
                fileVO.setGubun("DFT02");
                fileVO.setFileList(filelist);
                fileVO.setTotal(totalSize);
                fileVO.setProgressOffset(offsetForSend);
                attachFileService.copyFile(fileVO, null, "reuse");
                offsetForSend += filelist.size();
            }
            // 기안문 본문파일
            if(!param.getDrafturl().equals("") && param.getDrafturl() != null) {
                if(!param.getDrafturl().contains("fileUpload")) {
                    insertDraftFile(param, formatter, today);
                } else {
                    List<InsertTempListReqVO> draftfile = new ArrayList<>();
                    for(InsertTempListReqVO file : param.getDraftfile()){
                        if(file.getInid().equals(param.getResuserid())) {
                            draftfile.add(file);
                        }
                    }

                    if (!draftfile.isEmpty()) {
                        AttachFileVO fileVO = getFileVO(param, formatter.format(today));
                        fileVO.setSfilename(param.getDocttl());
                        fileVO.setGubun("DFT01");
                        fileVO.setFileList(draftfile);
                        fileVO.setProgressOffset(offsetForSend);
                        attachFileService.copyFile(fileVO, null, "reuse");
                    }
                }
            }

            // 11. 1인결재시 비밀관리기록부 저장
            if("Y".equals(isOneApprove)){
                insertMgmt(param, formatter, today, "1", "");
            }

        } catch (Exception e) {
            throw new AnyXException(e);
        }

        return resInsertReuseDraft;
    }

    private void insertMgmt(InsertTempListReqVO param, SimpleDateFormat formatter, Date today, String regirecvtype, String isProductOrReceived) {
        String resMgmtid = "MGT" + bmsDctRdocMapper.getSysGuid();

        // 1. INSERT BMS_DCT_MGMT_REGI TABLE
        InsertMgmtRegiReqVO vo = InsertMgmtRegiReqVO.builder()
                .mgmtid(resMgmtid)
                .mgmtno(param.getMgmtno())
                .docid(param.getResdocid())
                .seclevel(param.getSeclevel())
                .mgmtseq(BigDecimal.valueOf(1))
                .regirecvyear(Integer.toString(DateUtil.getYear()))
                .regirecvgubun(isProductOrReceived.isEmpty() ? "1":(isProductOrReceived.equals("접수") ? "2" : "1"))
                .regirecvdt(formatter.format(today))
                .regirecvsendid(param.getDeptid())
                .regirecvsendname(param.getDeptname())
                .regirecvtype(regirecvtype) // 1: 전자, 2: 비전자
                .secttl(param.getDocttl())
                .sectype(BigDecimal.valueOf(1))
                .attchcnt(!param.getFileList().isEmpty() ? BigDecimal.valueOf(param.getFileList().size()) : BigDecimal.valueOf(1))
                .copyno(isProductOrReceived.equals("접수") ? setCopyNo(param) : param.getCopyno())
                .notiprotterm(param.getOrgnprotdt() == null ? param.getCopyprotdt() : param.getOrgnprotdt())
                .notiprsrvterm(param.getPrsrvterm())
                .deptid(param.getDeptid())
                .deptname(param.getDeptname())
                .secstatus("MGT00") // MGT00: 상태없음
                .authorid(param.getAuthorid())
                .authorname(param.getAuthorname())
                .authordutyname(param.getAuthordutyname())
                .rgstdt(formatter.format(today))
                .indt(formatter.format(today))
                .build();
        bmsDctMgmtRegiMapper.insertMgmtRegi(vo);


        // 2. UPDATE BMS_DCT_ATTR - mgmtno, copyno 업데이트
        UpdateDctAttrReqVO updateDctAttrParam = new UpdateDctAttrReqVO();
        updateDctAttrParam.setDocid(param.getResdocid());
        updateDctAttrParam.setMgmtno(vo.getMgmtno());
        updateDctAttrParam.setCopyno(vo.getCopyno());
        bmsDctAttrMapper.updateDctAttr(updateDctAttrParam);

        // 3. INSERT BMS_DCT_AUTH_CARD - 열람권한 등록
        String authDate = formatter.format(today);
        String authGubun = "01"; // 01: 열람, 02: 재사용, 03: 인쇄, 04: 대출, 05: 지출
        insertAuthCard(param, authDate, authGubun);
    }

    private String getSecLevel(String secLevel) {
        switch (secLevel) {
            case "2":
                return "II";
            case "3":
                return "III";
            case "4":
                return "대외비";
            case "5":
                return "일반";
            default:
                return "";
        }
    }

    private String setMgmtNo(InsertTempListReqVO param) {
        String mgmtno = "";

        if(param.getMgmtno() != null) {
            mgmtno = param.getMgmtno();
        } else {
            String secLevel = getSecLevel(param.getSeclevel());
            String tempSeq = bmsDctRdocMapper.getTempSeq();
            String recentMgmt = bmsDctRdocMapper.getRecentMgmt();
            if (recentMgmt != null && DateUtil.getYear() > Integer.parseInt(recentMgmt.substring(0, 4))) {
                tempSeq = bmsDctRdocMapper.resetTempSeq();
            }
            mgmtno = secLevel + "-" + DateUtil.getYear() + "-" + tempSeq;
        }

        return mgmtno;
    }

    private String setCopyNo(InsertTempListReqVO param) {
        if (param.getCopyno() != null) {
            return param.getCopyno();
        } else {
            if (param.getRecvList() != null && !param.getRecvList().isEmpty()) {
                if (!param.getRecvList().get(0).getReceiverid().isEmpty()) {
                    return "원본/" + param.getRecvList().size();
                } else {
                    return "원/본";
                }
            } else {
                return "원/본";
            }
        }
    }

    private void insertHist(InsertTempListReqVO param, SimpleDateFormat formatter, Date today) {
        String histSeq = bmsDctHistCardMapper.getHistSeq();
        InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, histSeq, formatter.format(today));
        bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
    }


    /* (접수 > 비전자비밀접수 > 접수처리) & (비밀관리기록부 > 생산비전자비밀등록 > 등록처리) */
    @Override
    @Transactional
    public int insertOfflineRdoc(List<MultipartFile> draftAttach, InsertTempListReqVO param) {

        int resInsertOfflineRdoc = 0;
        String resSysGuid = "";
        String isOneApprove = "";
        String isProductOrReceived = param.getPaperrecgubun();
        double totalSize = 0;

        try {
            if(draftAttach != null)
                totalSize += draftAttach.size();
            else
                totalSize = 1;

            Date today = new Date();
            Locale currentLocale = new Locale("KOREAN", "KOREA");
            String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

            // 결재(N), 1인결재(Y) 구분
            isOneApprove = param.getIsOneApprove();

            // 00. CREATE SYS_GUID STRING
            resSysGuid = bmsDctRdocMapper.getSysGuid();
            String resDocid = "DCT" + resSysGuid;
            param.setResdocid(resDocid);

            // 01. INSERT BMS_DCT_RDOC TABLE
            param.setReportdt(formatter.format(today));
            InsertTempListReqVO insertTempListReqVO = getRdocVO(param, formatter.format(today));
            insertTempListReqVO.setPaperflag("Y");
            insertTempListReqVO.setPaperrecgubun(isProductOrReceived);
            resInsertOfflineRdoc = bmsDctRdocMapper.insertTempList(insertTempListReqVO);

            // 03. INSERT BMS_DCT_ATTR TABLE
            int resInsertAttribute = 0;
            int actSeq2 = bmsDctEnfPathMapper.getActSeq2();
            String recentRdoc = bmsDctRdocMapper.getRecentRdoc();
            // 가장 최근 비밀관리기록부 데이터의 등록일보다 DateUtil.getYear()가 크면 시퀀스 reset
            if(recentRdoc != null) {
                if (DateUtil.getYear() > parseInt(recentRdoc.substring(0, 4))) {
                    actSeq2 = parseInt(bmsDctRdocMapper.resetActSeq2());
                }
            }
            if(isProductOrReceived.equals("접수")){
                param.setDocno(param.getAuthordeptname() +"-S" + actSeq2);
            }
            InsertAttributeReqVO insertAttributeParam = getAttrVO(param);
            insertAttributeParam.setDocpagenum(param.getDocpagenum());
            resInsertAttribute = bmsDctAttrMapper.insertAttribute(insertAttributeParam);

            // 03-1. INSERT BMS_DCT_ATTR_ADD TABLE
            InsertAttrAddReqVO insertAttrAddParam = InsertAttrAddReqVO.builder()
                                                            .docid(param.getResdocid())
                                                            .realexist("Y")
                                                            .doctype(param.getDoctype())
                                                            .mediatype(param.getMediatype())
                                                            .locker(param.getLocker())
                                                            .unitnum(param.getUnitnum())
                                                            .proddocnum(param.getProddocnum())
                                                            .build();
            bmsDctAttrMapper.insertAttrAdd(insertAttrAddParam);

            // 04. INSERT BMS_DCT_PATH TABLE
            int resInsertPath = 0;
            for (InsertTempListReqVO tempReqVO : param.getPathList()) {
                if(!Objects.equals(tempReqVO.getAsktype(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    if("1".equals(tempReqVO.getPathorder()) || isProductOrReceived.equals("생산")){
                        tempReqVO.setReportdt(formatter.format(today));
                    }
                    InsertPathReqVO insertPathParam = getPathVO(tempReqVO, formatter.format(today));
                    resInsertPath += bmsDctPathMapper.insertPath(insertPathParam);
                }
            }
            sseController.progressCheck(Math.round(0.25 / totalSize * 100) + "%");

            // 05. INSERT BMS_DCT_OPINION TABLE
            int resInsertOpinion = 0;
            if(!Objects.equals(param.getOpinion(), "")) {
                InsertOpinionReqVO insertOpinionReqVO = getOpinionVO(param);
                resInsertOpinion = bmsDctOpinionMapper.insertDctOpinion(insertOpinionReqVO);
            }

            // 06. INSERT BMS_DCT_APPR_RECV
            int resInsertApprRecv = 0;
            if(param.getRecvList() != null && !param.getRecvList().isEmpty()) {
                for (InsertTempListReqVO tempReqVO : param.getRecvList()) {
                    if (!Objects.equals(tempReqVO.getApprrecvdeptname(), "")) {
                        tempReqVO.setResdocid(param.getResdocid());
                        tempReqVO.setResuserid(param.getResuserid());
                        tempReqVO.setSendOrgNameData(param.getSendOrgNameData());
                        tempReqVO.setSendNameData(param.getSendNameData());
                        InsertRecvReqVO insertRecvParam = getApprRecvVO(tempReqVO, formatter.format(today));
                        insertRecvParam.setActdt(param.getSenddate().replaceAll("-", "") + "000000");
                        resInsertApprRecv += bmsDctApprRecvMapper.insertRecv(insertRecvParam);
                    }
                }
            }
            sseController.progressCheck(Math.round(0.5 / totalSize * 100) + "%");

            // 07. INSERT BMS_DCT_RDOC_RCVS
            int resInsertRdocRcvs = 0;
            for (InsertTempListReqVO tempReqVO : param.getRcvsList()) {
                if (!Objects.equals(tempReqVO.getRcvsusername(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    InsertDocIdBmsDctRdocRcvsListReqVO insertDocIdBmsDctRdocRcvsListParam = getRdocRcvsVO(tempReqVO, formatter.format(today));
                    resInsertRdocRcvs += bmsDctRdocRcvsMapper.insertDocIdBmsDctRdocRcvsList(insertDocIdBmsDctRdocRcvsListParam);
                }
            }


            // 09. INSERT BMS_DCT_HIST_CARD TABLE
            int resInsertHistory = 0;
            if("Y".equals(isOneApprove)){ // 1인결재
                // 기안
                param.setHistgubun("01");
                param.setHiststatus("010");
                insertHist(param, formatter, today);
                // 결재
                param.setHistgubun("02");
                param.setHiststatus("011");
                insertHist(param, formatter, today);
            }else{
                insertHist(param, formatter, today);
            }

            // 10. INSERT BMS_DCT_FILE TABLE
            AttachFileVO attachFileVO = getFileVO(param,formatter.format(today));
            attachFileVO.setGubun("DFT02");
            attachFileVO.setTotal(totalSize);
            attachFileService.insertAttachFile(draftAttach, attachFileVO);
            sseController.progressCheck("100%");

            // 11. 1인결재시 비밀관리기록부 저장
            if("Y".equals(isOneApprove) || isProductOrReceived.equals("생산")){
                param.setPrsrvterm("005"); // mgmt 테이블 notiprsrvterm이 필수값이라 임의 설정
                insertMgmt(param, formatter, today, "2", isProductOrReceived);
            }

            // 12. INSERT BMS_DCT_ENF_DOC (배부함에서 보이기 위함)
            if(isProductOrReceived.equals("접수") && !Objects.equals(param.getJudgeParam(), "offReceipt")){
                insertOfflineEnfDoc(param, resSysGuid, formatter.format(today));
            }

            // 13. 비전자접수 기안일때
            List<AttachFileVO> resDftfileInfos = null;
            if(Objects.equals(param.getJudgeParam(), "offReceipt")){
                double offsetForSend = 0;
                if(param.getFileList().size() > 0 && !param.getFileList().isEmpty() && !param.getFileList().get(0).getSfilename().equals("")) {
                    attachFileVO = getFileVO(param, formatter.format(today));
                    attachFileVO.setGubun("DFT02");
                    attachFileVO.setOridocid(param.getDocid());
                    attachFileVO.setFileList(param.getFileList());
                    attachFileVO.setTotal(totalSize);
                    attachFileVO.setProgressOffset(offsetForSend);
                    offsetForSend += param.getFileList().size();

                    //  붙임파일 저장
                    resDftfileInfos = attachFileService.copyFile(attachFileVO, null, null);

                    // 접수번호 저장
                    InsertEnforceCardReqVO enfDocParam = InsertEnforceCardReqVO.builder()
                            .docid(param.getResdocid())
                            .oridocid(param.getOridocid())
                            .build();
                    bmsDctEnfDocMapper.insertEnforceCardCopy(enfDocParam);
                }

            }

        } catch (Exception e) {
            throw new AnyXException(e);
        }

        return resInsertOfflineRdoc;
    }

    private void insertOfflineEnfDoc(InsertTempListReqVO param, String resSysGuid, String today) {
        String enfDocid = "ENF" + resSysGuid;
        InsertEnforceCardReqVO enfDocParam = InsertEnforceCardReqVO.builder()
                .enfdocid(enfDocid)
                .docid(param.getResdocid())
                .enfgubun("N")
                .recvdeptid(param.getDeptid())
                .doctitle(param.getDocttl())
                .docstatus("EDS02")
                .acceptdocseq(BigDecimal.valueOf(bmsDctEnfPathMapper.getActSeq()))
                .acceptid(param.getAuthorid())
                .acceptname(param.getAuthorname())
                .acceptdutyname(param.getAuthordutyname())
                .acceptdeptid(param.getDeptid())
                .acceptdeptname(param.getDeptname())
                .inid(param.getAuthorid())
                .indt(today)
                .paperdocflag("Y")
                .build();
        bmsDctEnfDocMapper.insertEnforceCard(enfDocParam);
    }


    private BmsDctPathService bmsDctPathService;
    @Autowired
    public void ForBmsDctPathServiceImpl(BmsDctPathService bmsDctPathService) {
        this.bmsDctPathService = bmsDctPathService;
    }

    private BmsDctApprRecvService bmsDctApprRecvService;
    @Autowired
    public void ForBmsDctApprRecvServiceImpl(BmsDctApprRecvService bmsDctApprRecvService) {
        this.bmsDctApprRecvService = bmsDctApprRecvService;
    }

    private BmsDctAttrService bmsDctAttrRecvService;
    @Autowired
    public void ForBmsDctAttrServiceImpl(BmsDctAttrService bmsDctAttrRecvService) {
        this.bmsDctAttrRecvService = bmsDctAttrRecvService;
    }
    private BmsDctRdocRcvsService bmsDctRdocRcvsService;
    @Autowired
    public void ForBmsDctRdocRcvsServiceImpl(BmsDctRdocRcvsService bmsDctRdocRcvsService) {
        this.bmsDctRdocRcvsService = bmsDctRdocRcvsService;
    }

    private BmsDctDocSendinfoService bmsDctDocSendinfoService;
    @Autowired
    public void ForBmsDctDocSendinfoServiceImpl(BmsDctDocSendinfoService bmsDctDocSendinfoService) {
        this.bmsDctDocSendinfoService = bmsDctDocSendinfoService;
    }

    /* 비밀배포카드 팝업, 발송 상세화면 비밀문서 정보 조회 */
    @Override
    public SelectTempCreateCardListResVO selectTempCreateCardList(SelectTempCreateCardListReqVO param){
        System.out.println("BmsDctRdocServiceImpl.java BmsDctRdoc selectTempCreateCardList START");

        String paramDocId = "";

        paramDocId = param.getDocid();

        SelectTempCreateCardListResVO resultListVO = new SelectTempCreateCardListResVO();
        try {
            /* 00. 로그인한 유저 정보 조회 */
            GetUserLoginResVO resGetUserLogin;
            GetUserLoginReqVO getUserLoginReqVO = GetUserLoginReqVO.builder()
                    .userid(param.getLoginuserid())
                    .build();
            //getUserLogin(param)부분은 로그인 api가 변동시 수정예정.2023-10-27 기준 현재 로직은 이렇게.
            resGetUserLogin = comUserinfoMapper.getUserLogin(getUserLoginReqVO.builder()
//                    .userid(param.getLoginuserid())
                    .userid(getUserLoginReqVO.getUserid())
                    .build());
//            PageHelper.startPage(param);
            // GET_01 // 01. BMS_DCT_RDOC TABLE
//            /* 비밀관리카드 기본정보 조회 */
//            getDctRdoc <bms_dct_rdoc> ^s : iv(docid)


            GetDctRdocReqVO getDctRdocReqVO = new GetDctRdocReqVO();
            getDctRdocReqVO.setDocid(paramDocId);

            GetDctRdocResVO getDctRdocResVO = new GetDctRdocResVO();
            getDctRdocResVO = getDctRdoc(getDctRdocReqVO);

            System.out.println("BmsDctRdocServiceImpl.java BmsDctRdoc selectTempCreateCardList START");
            System.out.println("BmsDctRdocServiceImpl.java BmsDctRdoc selectTempCreateCardList getDctRdocResVO.getDocid() = [" + getDctRdocResVO.getDocid() + "]");
            System.out.println("BmsDctRdocServiceImpl.java BmsDctRdoc selectTempCreateCardList getDctRdocResVO.getDocttl() = [" + getDctRdocResVO.getDocttl() + "]");
            System.out.println("BmsDctRdocServiceImpl.java BmsDctRdoc selectTempCreateCardList getDctRdocResVO.getKeyword() = [" + getDctRdocResVO.getKeyword() + "]");
            System.out.println("BmsDctRdocServiceImpl.java BmsDctRdoc selectTempCreateCardList getDctRdocResVO.getEmergency() = [" + getDctRdocResVO.getEmergency() + "]");

            resultListVO.setDocid(getDctRdocResVO.getDocid());
            resultListVO.setDocttl(getDctRdocResVO.getDocttl());
            resultListVO.setKeyword(getDctRdocResVO.getKeyword());
            resultListVO.setEmergency(getDctRdocResVO.getEmergency());

            // GET_02 // 02. BMS_DCT_SUMMARY TABLE
            /* 요약테이블 조회 */
            // 2023.09.13. Lee.h.k. start 요약테이블 (문서취지) 조회를 위한 코드 start
            InsertTempListReqVO getDctSummaryReqVO = new InsertTempListReqVO();
            getDctSummaryReqVO.setDocid(paramDocId);

            SelectTempCreateCardListResVO getDctSummaryResVO = new SelectTempCreateCardListResVO();
            getDctSummaryResVO = getDctSummary(getDctSummaryReqVO);
            System.out.println("getDctSummaryResVO >>>>>>>>>>>>>> = " + getDctSummaryResVO);
            resultListVO.setDocgubun(getDctSummaryResVO.getDocgubun());
            resultListVO.setSummary(getDctSummaryResVO.getSummary());
            // 2023.09.13. Lee.h.k. end 요약테이블 (문서취지) 조회를 위한 코드 end


            // GET_03 // 03. BMS_DCT_ATTR TABLE
            /* 비밀속성 조회 */
//            ; getDctAttr <bms_dct_attr> ^s : iv(docid)
            // 2023.09.13. Lee.h.k. start 비밀속성 조회를 위한 코드 start
            GetDctAttrReqVO getDctAttrReqVO = new GetDctAttrReqVO();
            getDctAttrReqVO.setDocid(paramDocId);

            GetDctAttrResVO getDctAttrResVO = new GetDctAttrResVO();
            getDctAttrResVO = bmsDctAttrRecvService.getDctAttr(getDctAttrReqVO);
            System.out.println("getDctAttrResVO >>>>>>>>>>>>>> = " + getDctAttrResVO);

            String docId = getDctAttrResVO.getDocid();
//            System.out.println("docid >>>>>>>>>> : " + docId);
//            resultListVO.setAuthorid(docId);
            String secgubun = getDctAttrResVO.getSecgubun();
            String seclevel = getDctAttrResVO.getSeclevel();
            String prsrvterm = getDctAttrResVO.getPrsrvterm();
            String orgnprotdt = getDctAttrResVO.getOrgnprotdt();
            String orgnnextlevel = getDctAttrResVO.getOrgnnextlevel();
            String orgnreclass = getDctAttrResVO.getOrgnreclass();
            String copyprotdt = getDctAttrResVO.getCopyprotdt();
            String copynextlevel = getDctAttrResVO.getCopynextlevel();
            String copyreclass = getDctAttrResVO.getCopyreclass();
            String mgmtno = getDctAttrResVO.getMgmtno();
            String copyno = getDctAttrResVO.getCopyno();

            resultListVO.setSecgubun(secgubun);
            resultListVO.setSeclevel(seclevel);
            resultListVO.setPrsrvterm(prsrvterm);
            resultListVO.setOrgnprotdt(orgnprotdt);
            resultListVO.setOrgnnextlevel(orgnnextlevel);
            resultListVO.setOrgnreclass(orgnreclass);
            resultListVO.setCopyprotdt(copyprotdt);
            resultListVO.setCopynextlevel(copynextlevel);
            resultListVO.setCopyreclass(copyreclass);
            resultListVO.setMgmtno(mgmtno);
            resultListVO.setCopyno(copyno);
            // 2023.09.13. Lee.h.k. end 비밀속성 조회를 위한 코드 end


            // GET_04 // 04. BMS_DCT_PATH TABLE
//            /* 결재경로 조회 */
//            ; selectDctPath <bms_dct_path> ^S : iv(docid)
            // 2023.09.12. Lee.h.k. start 결재경로 조회를 위한 코드 start
            SelectDctPathReqVO selectDctPathReqVO = new SelectDctPathReqVO();
            selectDctPathReqVO.setDocid(paramDocId);

            SelectDctPathResVO selectDctPathResVO = new SelectDctPathResVO();
            List<SelectDctPathResVO> selectDctPathResVOS = bmsDctPathService.selectDctPath(selectDctPathReqVO);
            System.out.println("selectDctPathResVOS >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> = " + selectDctPathResVOS);
            // [SelectDctPathResVO(docid=DCT20230912112703, pathorder=100.0, state=DCST1, objid=null, objtype=null,
            // objname=null, deptid=null, deptname=경영관리팀, dutyname=주사, dutynamedesc=null, ... ... )]
            resultListVO.setPathlist(selectDctPathResVOS);
//            List<String> pathInfo = new ArrayList<>();
            for (SelectDctPathResVO item : selectDctPathResVOS) {
                docId = item.getDocid();
//                System.out.println("docid >>>>>>>>>> : " + docId);
//                resultListVO.setAuthorid(docId);
                String state = item.getState(); // 구분
                String deptname = item.getDeptname(); // 부서
                String dutyname = item.getDutyname(); // 직위/성명
                String electsignid = item.getElectsignid(); // 서명
                String reportdt = item.getReportdt(); // 처리결과
                String asktype = item.getAsktype(); // 요청유형
                String acttype = item.getActtype();
                String opinion = item.getOpinion(); // 요청의견

                resultListVO.setState(state);
                resultListVO.setDeptname(deptname);
                resultListVO.setDutyname(dutyname);
                resultListVO.setElectsignid(electsignid);
                resultListVO.setReportdt(reportdt);
                resultListVO.setAsktype(asktype);
                resultListVO.setActtype(acttype);
                resultListVO.setOpinion(opinion);
            }
//            resultListVO.setPathInfo(pathInfo);
            // 2023.09.12. Lee.h.k. end 결재경로 조회를 위한 코드 end

            // GET_05 // 05. BMS_DCT_OPINION TABLE
            // 2023.09.13. Lee.h.k. start 의견 조회를 위한 코드 start
//            InsertTempListReqVO getDctOpinionReqVO = new InsertTempListReqVO();
//            getDctOpinionReqVO.setDocid(paramDocId);
//
//            SelectTempCreateCardListResVO getDctOpinionResVO = new SelectTempCreateCardListResVO();
//            getDctOpinionResVO = bmsDctOpinionService.getDctOpinion(getDctOpinionReqVO);
//            System.out.println("getDctOpinionResVO >>>>>>>>>>>>>> = " + getDctOpinionResVO);
//            resultListVO.setOpinion(getDctOpinionResVO.getOpinion());
            // 2023.09.13. Lee.h.k. end 의견 조회를 위한 코드 end

            // GET_06 // 06. BMS_DCT_DOC_SENDINFO
            // 2023.09.13. Lee.h.k. start 발신기관 조회를 위한 코드 start
            SelectSendinfoReqVO selectSendinfoReqVO = new SelectSendinfoReqVO();
            selectSendinfoReqVO.setDocid(paramDocId);

            SelectSendinfoResVO selectSendinfoResVO = new SelectSendinfoResVO();
            List<SelectSendinfoResVO> selectSendinfoResVOS = bmsDctDocSendinfoService.selectSendinfo(selectSendinfoReqVO);
            System.out.println("selectSendinfoResVOS >>>>>>>>>>>>>>>>> = " + selectSendinfoResVOS);
            resultListVO.setSendinfolist(selectSendinfoResVOS);
            // 2023.09.13. Lee.h.k. end 발신기관 조회를 위한 코드 end

            // GET_07 // 07. BMS_DCT_APPR_RECV
//            /* 수신처 조회 */
//            ; getDctRecv <bms_dct_appr_recv> ^S : iv(docid)
            // 2023.09.13. Lee.h.k. start 수신처 조회를 위한 코드 Start
            System.out.println("getDctRecv start--------------");
            GetDctRecvReqVO getDctRecvReqVO = new GetDctRecvReqVO();
            getDctRecvReqVO.setDocid(paramDocId);

            GetDctRecvResVO getDctRecvResVO = new GetDctRecvResVO();
            List<GetDctRecvResVO> getDctRecvResVOS = bmsDctApprRecvService.getDctRecv(getDctRecvReqVO);
            System.out.println("getDctRecvResVOS >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> = " + getDctRecvResVOS);

            resultListVO.setRecvlist(getDctRecvResVOS);
            // 2023.09.13. Lee.h.k. start 수신처 조회를 위한 코드 End


            // GET_08 // 08. BMS_DCT_RDOC_RCVS
            /* 열람자 조회 */
//            ; selectDctReader <bms_dct_rdoc_rcvs> ^s : iv(docid)
            // 2023.09.13. Lee.h.k. start 열람자 조회를 위한 코드 start
            SelectDctReaderReqVO selectDctReaderReqVO = new SelectDctReaderReqVO();
            selectDctReaderReqVO.setDocid(paramDocId);

            List<SelectDctReaderResVO> selectDctReaderResVO = bmsDctRdocRcvsService.selectDctReader(selectDctReaderReqVO);
            System.out.println("selectDctReaderResVO >>>>>>>>>>>>>>>>>>>>> = " + selectDctReaderResVO);

            resultListVO.setRcvslist(selectDctReaderResVO);
            // 2023.09.13. Lee.h.k. end 열람자 조회를 위한 코드 end


            // GET_09
//            /* 파일조회 */
//            ; selectDctFile <bms_dct_file> ^s : iv(docid)

            // GET_10 // 10. BMS_TASK_INFOMATION_TEMP
//            SelectTempCreateCardListReqVO selectTempCreateCardListReqVO = new SelectTempCreateCardListReqVO();
//            selectTempCreateCardListReqVO.setDocid(paramDocId);
//            selectTempCreateCardListReqVO.setResultid(paramDocId);
//
//            SelectTempCreateCardListResVO selectTempCreateCardListResVO = new SelectTempCreateCardListResVO();
//            selectTempCreateCardListResVO = bmsDctaaaaService.selectTaskInfoTemp(selectTempCreateCardListReqVO);
//            System.out.println("selectTempCreateCardListResVO >>>>>>>>>>>>>>>>>>>>> = " + selectTempCreateCardListResVO);
//
//            resultListVO.setResultid(selectTempCreateCardListResVO.getResultid());

            // 10. GET bms_dct_enf_doc
            /* bms_dct_enf_doc.chargerid 조회목적 (지정된 담당자인지 확인하기 위함) */
            List<SelectReceiptListResVO> resSelectReceiptList;
            SelectReceiptListReqVO selectReceiptListParamVO = SelectReceiptListReqVO.builder()
                    .userid(resGetUserLogin.getUserid())
                    .vgroupid(resGetUserLogin.getVgroupid())
                    .deptid(resGetUserLogin.getDeptid())
                    .build();
//            /* 결재자의 부서를 필터링 */
            List<SelectDctPathResVO> filteredList = new ArrayList<>();

            for (SelectDctPathResVO vo : selectDctPathResVOS) {
                if ("DEA41".equals(vo.getAsktype())) {
                    filteredList.add(vo);
                }
            }
            if(!filteredList.isEmpty()) {
                System.out.println("filteredList >>> : " + filteredList);
                System.out.println("getDeptid >>> : " + filteredList.get(0).getDeptid());
            }
            resSelectReceiptList = bmsDctEnfDocMapper.selectReceiptList(selectReceiptListParamVO.builder()
                    .userid(selectReceiptListParamVO.getUserid())
                    .vgroupid(selectReceiptListParamVO.getVgroupid())
                    .deptid(selectReceiptListParamVO.getDeptid())
//                    .relaytype(selectReceiptListParamVO.getR)
                    .enfdocid(param.getEnfdocid())
                    .build());
            System.out.println("resSelectReceiptList >>>>>>>>> : " + resSelectReceiptList);
            if (!resSelectReceiptList.isEmpty()) {
                resultListVO.setVgroupid(resSelectReceiptList.get(0).getVgroupid());
                resultListVO.setOrgid(resSelectReceiptList.get(0).getOrgid());
                resultListVO.setChargerid(resSelectReceiptList.get(0).getChargerid());
            }

            /* 11. Select bms_dct_enf_path (chargerList) 담당정보의 담당자 조회 */
            List<SelectDctEnfPathResVO> selectDctEnfPathResVOS;
            if(param.getEnfdocid() != null) {
                SelectDctEnfPathReqVO selectDctEnfPathReqVO = new SelectDctEnfPathReqVO();
                selectDctEnfPathReqVO.setEnfdocid(param.getEnfdocid());
                selectDctEnfPathResVOS = bmsDctEnfPathMapper.selectDctEnfPath(selectDctEnfPathReqVO);
                System.out.println("selectDctEnfPathResVOS >>>>>>>>>>>>>>>>>>>>> = " + selectDctEnfPathResVOS);
                resultListVO.setChargerList(selectDctEnfPathResVOS);
            }else{
                resultListVO.setChargerList(null);
            }



            /* selectTempCreateCardList END */
            System.out.println("resultListVO >>>>>>>>>>>>> : " + resultListVO);

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        System.out.println(resultListVO);
        return resultListVO;
    }


    /* 비밀문서 정보 조회 */
    @Override
    public GetDctRdocResVO getDctRdoc(GetDctRdocReqVO param){
        GetDctRdocResVO resGetDctRdoc;
        try {
            resGetDctRdoc = bmsDctRdocMapper.getDctRdoc(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetDctRdoc == null){
            resGetDctRdoc = new GetDctRdocResVO();
        }
        return resGetDctRdoc;
    }


    /* 비밀관리카드 팝업 권한이력 조회 */
    @Override
    public List<GetDctAuthCardResVO> getDctAuthCard(GetDctAuthCardReqVO param){
        PageHelper.clearPage();
        List<GetDctAuthCardResVO> resGetDctAuthCard;
        try {
            resGetDctAuthCard = bmsDctRdocMapper.getDctAuthCard(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resGetDctAuthCard;
    }


    /* 기안 > 기안완료함 > 기안완료함 목록 조회 */
    @Override
    public PageInfo<SelectReqCompleteListResVO> selectReqCompleteList(SelectReqCompleteListReqVO param){
        PageInfo<SelectReqCompleteListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctRdocMapper.selectReqCompleteList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 결재 > 결재대기함 > 결재대기함 목록 조회 */
    @Override
    public PageInfo<SelectWaitListResVO> selectWaitList(SelectWaitListReqVO param){
        PageInfo<SelectWaitListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctRdocMapper.selectWaitList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 검토용인쇄 팝업 조회 */
    @Override
    public GetReviewPrintResVO getReviewPrint(GetReviewPrintReqVO param){
        GetReviewPrintResVO resGetReviewPrint;
        try {
            resGetReviewPrint = bmsDctRdocMapper.getReviewPrint(param);

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetReviewPrint == null){
            resGetReviewPrint = new GetReviewPrintResVO();
        }
        return resGetReviewPrint;
    }

    /* (기안 문서처리 1인결재) & (결재대기문서 결재 승인 처리) */
    @Override
    @Transactional
    public int updateCardBIA1(List<MultipartFile> draftAttach, List<MultipartFile> refAttach, InsertTempListReqVO param){
        int resUpdateCardBIA1 = 0;
        double totalSize = 1;
        double offsetForSend = 0;
        try {
            if(draftAttach != null)
                totalSize += draftAttach.size();
            if(refAttach != null)
                totalSize += refAttach.size();
            if(param.getFileList() != null)
                totalSize += param.getFileList().size();

            Date today = new Date();
            Locale currentLocale = new Locale("KOREAN", "KOREA");
            String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

            // 1인결재인 경우 docid/docno 만들어줌
            if(param.getResasktype().equals("DEA45")) {
                String resSysGuidDct = bmsDctRdocMapper.getSysGuid();
                String resDocid = "DCT" + resSysGuidDct;
                if(param.getResdocid().isEmpty()){
                    param.setResdocid(resDocid);
                }
                int actSeq2 = bmsDctEnfPathMapper.getActSeq2();
                String recentRdoc = bmsDctRdocMapper.getRecentRdoc();
                // 가장 최근 비밀관리기록부 데이터의 등록일보다 DateUtil.getYear()가 크면 시퀀스 reset
                if(recentRdoc != null) {
                    if (DateUtil.getYear() > parseInt(recentRdoc.substring(0, 4))) {
                        actSeq2 = parseInt(bmsDctRdocMapper.resetActSeq2());
                    }
                }

                param.setDocno(param.getAuthordeptname() +"-S" + actSeq2);
            }

            /* 수정 처리 */
            // 01. INSERT BMS_DCT_RDOC TABLE
            param.setReportdt(formatter.format(today));
            InsertTempListReqVO insertTempListReqVO = getRdocVO(param, formatter.format(today));
            if(Objects.equals(param.getJudgeParam(),"receipt")){ //접수문서를 기안하는 경우.
                insertTempListReqVO.setOridocid(param.getOridocid()); // 사본을 원본으로 가짐.
            }
            int resInsertCardBIA = bmsDctRdocMapper.insertTempList(insertTempListReqVO);


            // 02. INSERT BMS_DCT_SUMMARY TABLE
            int resInsertSummary = 0;
            if(!Objects.equals(param.getSummary(), "")) {
                InsertTempListReqVO insertSummaryReqVO = getSummaryVO(param);
                resInsertSummary = bmsDctRdocMapper.insertSummary(insertSummaryReqVO);
            }


            // 03. INSERT BMS_DCT_ATTR TABLE
            int resInsertAttribute = 0;
            InsertAttributeReqVO insertAttributeParam = getAttrVO(param);
            resInsertAttribute = bmsDctAttrMapper.insertAttribute(insertAttributeParam);


            // 04. INSERT BMS_DCT_PATH TABLE
            int resInsertPath = 0;
            SelectDctPathReqVO selectDctPathReqVO = new SelectDctPathReqVO();
            selectDctPathReqVO.setDocid(param.getResdocid());
            List<SelectDctPathResVO> selectDctPathResVOS = bmsDctPathMapper.selectDctPath(selectDctPathReqVO);
            if(!selectDctPathResVOS.isEmpty()){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_dct_path");
                paramMap.put("docid", param.getResdocid());
                bmsDctRdocMapper.deleteTempList(paramMap);
            }
            for (InsertTempListReqVO tempReqVO : param.getPathList()) {
                if(!Objects.equals(tempReqVO.getAsktype(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    InsertPathReqVO insertPathParam = getPathVO(tempReqVO, formatter.format(today));

                    if(param.getResasktype().equals("DEA45")) {
                        insertPathParam.setActtype("DEA45");
                    } else {
                        insertPathParam.setActtype("DEA41");
                    }

                    for(SelectDctPathResVO tempResVO : selectDctPathResVOS) {
                        if(tempResVO.getObjid().equals(tempReqVO.getObjid())) {
                            insertPathParam.setReportdt(tempResVO.getReportdt());
                        }
                    }

                    resInsertPath += bmsDctPathMapper.insertPath(insertPathParam);
                }
            }
            sseController.progressCheck(Math.round(0.25 / totalSize * 100) + "%");

            // 06. INSERT BMS_DCT_APPR_RECV
            int resInsertApprRecv = 0;
            GetDctRecvReqVO getDctRecvReqVO = new GetDctRecvReqVO();
            getDctRecvReqVO.setDocid(param.getResdocid());
            List<GetDctRecvResVO> dctRecv = bmsDctApprRecvMapper.getDctRecv(getDctRecvReqVO);
            if(!dctRecv.isEmpty()){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_dct_appr_recv");
                paramMap.put("docid", param.getResdocid());
                bmsDctRdocMapper.deleteTempList(paramMap);
            }
            if(param.getRecvList() != null) {
                for (InsertTempListReqVO tempReqVO : param.getRecvList()) {
                    if (!Objects.equals(tempReqVO.getApprrecvdeptname(), "")) {
                        tempReqVO.setResdocid(param.getResdocid());
                        tempReqVO.setResuserid(param.getResuserid());
                        tempReqVO.setSendOrgNameData(param.getSendOrgNameData());
                        tempReqVO.setSendNameData(param.getSendNameData());
                        InsertRecvReqVO insertRecvParam = getApprRecvVO(tempReqVO, formatter.format(today));

                        if(insertRecvParam.getTempflag() == null) {
                            insertRecvParam.setTempflag("Y");
                        }

                        resInsertApprRecv += bmsDctApprRecvMapper.insertRecv(insertRecvParam);
                    }
                }
            }
            sseController.progressCheck(Math.round(0.5 / totalSize * 100) + "%");


            // 07. INSERT BMS_DCT_RDOC_RCVS
            int resInsertRdocRcvs = 0;
            SelectDctReaderReqVO selectDctReaderReqVO = new SelectDctReaderReqVO();
            selectDctReaderReqVO.setDocid(param.getResdocid());
            List<SelectDctReaderResVO> selectDctReaderResVOS = bmsDctRdocRcvsMapper.selectDctReader(selectDctReaderReqVO);
            if(!selectDctReaderResVOS.isEmpty()){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_dct_rdoc_rcvs");
                paramMap.put("docid", param.getResdocid());
                bmsDctRdocMapper.deleteTempList(paramMap);
            }
            for (InsertTempListReqVO tempReqVO : param.getRcvsList()) {
                if (!Objects.equals(tempReqVO.getRcvsusername(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    InsertDocIdBmsDctRdocRcvsListReqVO insertDocIdBmsDctRdocRcvsListParam = getRdocRcvsVO(tempReqVO, formatter.format(today));
                    resInsertRdocRcvs += bmsDctRdocRcvsMapper.insertDocIdBmsDctRdocRcvsList(insertDocIdBmsDctRdocRcvsListParam);
                }
            }


            // 2024-02-06 lbh, 트랜잭션 문제로 위치 변경
            Map<String, Object> docidData = new HashMap<>();
            docidData.put("docid", param.getResdocid());
            List<Map<String, Object>> selectList = bmsDctFileMapper.selectDctFileDFT02(docidData);

            List<AttachFileVO> refFileInfos = null;
            if(Objects.equals(param.getJudgeParam(),"receipt")){ //접수문서를 기안하는 경우.
                insertTempListReqVO.setOridocid(param.getOridocid()); // 사본을 원본으로 가짐.
                // 사본에 대한 관련근거 - 직접입력 첨부파일
                AttachFileVO attachFileVO = getFileVO(param, formatter.format(today));
                attachFileVO.setGubun("DFT03");
                attachFileVO.setOridocid(param.getDocid());

                SelectTaskInfoTempReqVO selectTaskInfoTempReqVO = new SelectTaskInfoTempReqVO();
                selectTaskInfoTempReqVO.setOridocid(param.getOridocid());
                List<SelectTaskInfoTempResVO> taskInfoList = bmsTaskInfomationTempMapper.selectTaskInfoJoinFile(selectTaskInfoTempReqVO);
                List<InsertTempListReqVO> fileList = new ArrayList<>();
                if (taskInfoList != null && !taskInfoList.isEmpty()) {
                    for (SelectTaskInfoTempResVO item : taskInfoList) {
                        InsertTempListReqVO tempItem = new InsertTempListReqVO();
                        tempItem.setDocid(item.getDocid());                 // 비밀 ID
                        tempItem.setFleid(item.getFleid());                 // 첨부 ID
                        tempItem.setSfilename(item.getSfilename());         // 파일고유이름
                        tempItem.setGubun(item.getGubun());                 // 구분
                        tempItem.setFlettl(item.getFlettl());               // 첨부명
                        tempItem.setFlesize(item.getFlesize());             // 첨부크기
                        tempItem.setFleorder(item.getFleorder());           // 첨부순서
                        tempItem.setFlepath(item.getFlepath());             // 파일경로
                        tempItem.setMarkflag(item.getMarkflag());           // 비밀등급표시여부
                        tempItem.setOrigubun(item.getOrigubun());           // 기존 문서의 구분
                        tempItem.setOridocid(item.getOridocid());           // 기존 문서의 비밀 ID
                        tempItem.setIsviewtype(item.getIsviewtype());       // 비전자 첨부구분
                        fileList.add(tempItem);
                    }
                    attachFileVO.setFileList(fileList);
                    if(fileList.size() > 0) {
                        totalSize = fileList.size() + param.getFileList().size() + param.getDraftfile().size();
                        offsetForSend = fileList.size();
                        attachFileVO.setTotal(totalSize);
                        refFileInfos = attachFileService.copyFile(attachFileVO, null, null);
                    }
                }
            }else{
                // 관련근거 - 직접입력 첨부파일
                AttachFileVO refFileVO = getFileVO(param, formatter.format(today));
                refFileVO.setGubun("DFT03");
                refFileVO.setTotal(totalSize);
                refFileInfos = attachFileService.insertAttachFile(refAttach, refFileVO);
            }

            // 08. INSERT BMS_TASK_INFOMATION TABLE
            int resInsertTaskInfo = 0;
            if(param.getDelRef() != null){
                for(String infoId : param.getDelRef()){

                    SelectTaskInfoTempReqVO selectTaskInfoTempReqVO = new SelectTaskInfoTempReqVO();
                    selectTaskInfoTempReqVO.setInfomationid(infoId);
                    List<SelectTaskInfoTempResVO> selectTaskInfoTempResVOS = bmsTaskInfomationTempMapper.selectTaskInfo(selectTaskInfoTempReqVO);
                    Optional<List<SelectTaskInfoTempResVO>> optionalSelectTaskInfoTempResVOS = Optional.ofNullable(selectTaskInfoTempResVOS);

                    if (optionalSelectTaskInfoTempResVOS.isPresent() && !optionalSelectTaskInfoTempResVOS.get().isEmpty()) {
                        String attathId = optionalSelectTaskInfoTempResVOS.get().get(0).getAttachid();
                        if(attathId != null){
                            AttachFileVO attachFileVO = new AttachFileVO();
                            attachFileVO.setFleid(attathId);
                            AttachFileVO attachFileByFleid = attachFileMapper.getAttachFileByFleid(attachFileVO);
                            String fileFullPath = attachFileByFleid.getFlepath();
                            File delFile = new File(fileFullPath);
                            delFile.delete();
                            attachFileMapper.deleteAttachFileByFleid(attachFileVO);
                        }
                    }

                    Map<String, Object> fileMap = new HashMap<>();
                    fileMap.put("infomationid", infoId);
                    bmsDctRdocMapper.deleteRef(fileMap);
                }
            }

            int cnt = 0;
            int temp = 0;
            if(param.getRefList() != null && param.getRefList().size() > 0) {
                for (InsertTempListReqVO tempReqVO : param.getRefList()) {
                    if (!Objects.equals(tempReqVO.getInfotype(), "")) {
                        tempReqVO.setResdocid(param.getResdocid());
                        tempReqVO.setResuserid(param.getResuserid());

                        if(tempReqVO.getFileexist() != null){
                            if(tempReqVO.getFileexist().equals("N")){
                                resInsertTaskInfo = insertTaskInfo(tempReqVO, formatter, today, resInsertTaskInfo);
                            }else{
                                tempReqVO.setAttachid(refFileInfos.get(cnt++).getFleid());
                                resInsertTaskInfo = insertTaskInfo(tempReqVO, formatter, today, resInsertTaskInfo);
                            }
                        } else{
                            if(param.getResasktype().equals("DEA45")) {
                                if(temp == 0){
                                    List<SelectTaskInfoTempResVO> selectTaskInfoTempResVOS = null;
                                    Optional<List<SelectTaskInfoTempResVO>> optionalSelectTaskInfoTempResVOS = null;

                                    SelectTaskInfoTempReqVO selectTaskInfoTempReqVO = new SelectTaskInfoTempReqVO();
                                    selectTaskInfoTempReqVO.setDocid(param.getResdocid());
                                    if(Objects.equals(param.getJudgeParam(),"receipt")) { //접수문서를 기안하는 경우.
                                        selectTaskInfoTempReqVO.setDocid(param.getOridocid());
                                        selectTaskInfoTempResVOS = bmsTaskInfomationTempMapper.selectTaskInfo(selectTaskInfoTempReqVO);
                                    }else{
                                        selectTaskInfoTempResVOS = bmsTaskInfomationTempMapper.selectTaskInfoTemp(selectTaskInfoTempReqVO);
                                    }
                                    optionalSelectTaskInfoTempResVOS = Optional.ofNullable(selectTaskInfoTempResVOS);
//                                List<SelectTaskInfoTempResVO> selectTaskInfoTempResVOS = bmsTaskInfomationTempMapper.selectTaskInfoTemp(selectTaskInfoTempReqVO);
//                                Optional<List<SelectTaskInfoTempResVO>> optionalSelectTaskInfoTempResVOS = Optional.ofNullable(selectTaskInfoTempResVOS);

                                    if (optionalSelectTaskInfoTempResVOS.isPresent() && !optionalSelectTaskInfoTempResVOS.get().isEmpty()) {
                                        for(SelectTaskInfoTempResVO optionalSelectTaskInfoTempResVO : optionalSelectTaskInfoTempResVOS.get()){
                                            InsertTaskInfomationtReqVO vo = InsertTaskInfomationtReqVO.builder()
                                                    .infomationid("INF" + bmsDctRdocMapper.getSysGuid())
                                                    .resultid(param.getResdocid())
                                                    .attachid(optionalSelectTaskInfoTempResVO.getAttachid())
                                                    .infotext(optionalSelectTaskInfoTempResVO.getInfotext())
                                                    .infotype(optionalSelectTaskInfoTempResVO.getInfotype())
                                                    .deleteyn("N")
                                                    .infotitle(optionalSelectTaskInfoTempResVO.getInfotitle())
                                                    .inid(optionalSelectTaskInfoTempResVO.getInid())
                                                    .indt(formatter.format(today))
                                                    .build();
                                            temp += bmsTaskInfomationMapper.insertTaskInfo(vo);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


            // 10. INSERT BMS_DCT_FILE TABLE
            if(param.getDelFileids() != null){
//                AttachFileVO delFileVO = null;
//                for (String DelFleid : param.getDelFileids()) {
//                    delFileVO = getDelFileVO(param.getResdocid(), DelFleid);
//                    attachFileService.deleteAttachFile(delFileVO);
//                }

                List<String> fleidList = new ArrayList<>();
                for(String DelFleid : param.getDelFileids()) {
                    fleidList.add(DelFleid);
                }

                Iterator<Map<String, Object>> iterator = selectList.iterator();
                while(iterator.hasNext()) {
                    Map<String, Object> map = iterator.next();
                    String fleidValue = map.get("fleid").toString();

                    if (fleidList.contains(fleidValue)) {
                        iterator.remove();
                    }
                }
            }
            int fileCnt = draftAttach == null ? 0:draftAttach.size();
            for(Map<String, Object> selectData : selectList) {
                InsertFileReqVO insertFileReqVO = new InsertFileReqVO();
                insertFileReqVO.setDocid(param.getResdocid());

                String resSysGuid = bmsDctRdocMapper.getSysGuid();
                insertFileReqVO.setFleid("FLE"+resSysGuid);

                insertFileReqVO.setFleorder(fileCnt);
                insertFileReqVO.setFlepath(selectData.get("flepath").toString());
                insertFileReqVO.setFlesize((BigDecimal) selectData.get("flesize"));
                insertFileReqVO.setFlettl(selectData.get("flettl").toString());
                insertFileReqVO.setFleurl(selectData.get("fleurl").toString());
                insertFileReqVO.setGubun(selectData.get("gubun").toString());
                insertFileReqVO.setIndt(formatter.format(today));
                insertFileReqVO.setInid(param.getResuserid());
                insertFileReqVO.setMarkflag(selectData.get("markflag").toString());
                insertFileReqVO.setOridocid(selectData.get("oridocid").toString());
                insertFileReqVO.setOrigubun(selectData.get("origubun").toString());
                insertFileReqVO.setSfilename(selectData.get("sfilename").toString());

                bmsDctFileMapper.insertFile(insertFileReqVO);
                fileCnt++;
            }

            if(Objects.equals(param.getJudgeParam(), "receipt")){
                if(param.getFileList() != null && !param.getFileList().isEmpty() && !param.getFileList().get(0).getSfilename().equals("")) {
                    AttachFileVO attachFileVO = getFileVO(param, formatter.format(today));
                    attachFileVO.setGubun("DFT02");
                    attachFileVO.setOridocid(param.getDocid());
                    attachFileVO.setFileList(param.getFileList());
                    attachFileVO.setTotal(totalSize);
                    attachFileService.copyFile(attachFileVO, null, null);
                }
                if(param.getDraftfile() != null && !param.getDraftfile().isEmpty() && !param.getDraftfile().get(0).getSfilename().equals("")) {
                    AttachFileVO draftFileVO = getFileVO(param, formatter.format(today));
                    draftFileVO.setGubun("DFT01");
                    draftFileVO.setOridocid(param.getDocid());
                    draftFileVO.setFileList(param.getDraftfile());
                    draftFileVO.setTotal(1);
                    attachFileService.copyFile(draftFileVO, null, null);
                }
            }else{
                if(param.getDraftfile() != null && !param.getDraftfile().isEmpty() && !param.getDraftfile().get(0).getSfilename().equals("")) {
                    AttachFileVO draftFileVO = getFileVO(param, formatter.format(today));
                    draftFileVO.setGubun("DFT01");
                    draftFileVO.setOridocid(param.getDocid());
                    draftFileVO.setFileList(param.getDraftfile());
                    draftFileVO.setTotal(1);
                    attachFileService.copyFile(draftFileVO, null, null);
                }
                AttachFileVO attachFileVO = getFileVO(param, formatter.format(today));
                attachFileVO.setGubun("DFT02");
                attachFileVO.setTotal(totalSize);
                if(refAttach != null)
                    attachFileVO.setProgressOffset(refAttach.size());
                attachFileService.insertAttachFile(draftAttach, attachFileVO);
            }



            if(param.getJudgeParam() != null && param.getJudgeParam().equals("receipt")){

            }else{
                // 본문파일 받아와서 변환
                if (param.getDrafturl() != null && !param.getDrafturl().equals("")) {
                    if(!param.getDrafturl().contains("fileUpload")) {
                        if(param.getResasktype().equals("DEA45")) { // 1인 결재인 경우
                            AttachFileVO vo = new AttachFileVO();
                            vo.setDocid(param.getResdocid());
                            vo.setGubun("DFT01");
                            vo.setInid(param.getResuserid());
                            List<AttachFileVO> attachFileVOS = attachFileMapper.selectAttachFileByRecord(vo);
                            Optional<List<AttachFileVO>> optionalattachFileVOS = Optional.ofNullable(attachFileVOS);

                            // insert
                            if(!param.getDrafturl().contains("fileUpload")) {
                                insertDraftFile(param, formatter, today);
                            } else {
                                List<InsertTempListReqVO> draftfile = new ArrayList<>();
                                for(InsertTempListReqVO file : param.getDraftfile()){
                                    if(file.getInid().equals(param.getResuserid())) {
                                        draftfile.add(file);
                                    }
                                }

                                if (!draftfile.isEmpty()) {
                                    AttachFileVO fileVO = getFileVO(param, formatter.format(today));
                                    fileVO.setSfilename(param.getDocttl());
                                    fileVO.setGubun("DFT01");
                                    fileVO.setFileList(draftfile);
                                    fileVO.setTotal(totalSize);
                                    attachFileService.copyFile(fileVO, null, "reuse");
                                }
                            }

                            if (optionalattachFileVOS.isPresent() && !optionalattachFileVOS.get().isEmpty()) {
                                // 이전의 본문 파일은 삭제
                                AttachFileVO delFileVO = null;
                                for (AttachFileVO delFile : attachFileVOS) {
                                    String fleid = delFile.getFleid();
                                    delFileVO = getDelFileVO(param.getResdocid(), fleid);
                                    attachFileService.deleteAttachFile(delFileVO);
                                }
                            }
                        } else {
                            insertDraftFile(param, formatter, today);
                        }
                    }
                }
            }

            /* 문서 처리(승인) */
            /* 경로 상태값 변경 DCST5 */
            UpdatePathReqVO updatePathParam = new UpdatePathReqVO();
            updatePathParam.setDocid(param.getResdocid());
            updatePathParam.setObjid(param.getResuserid());
            updatePathParam.setMainver(BigDecimal.valueOf(param.getMainver())); // 수정이력
            updatePathParam.setReportdt(formatter.format(today)); // 처리결과
            updatePathParam.setState("DCST5");

            int resUpdatePath = bmsDctPathMapper.updatePath(updatePathParam);

            /* 처리의견 저장 */
            int resInsertOpinion = 0;
            if(!Objects.equals(param.getOpinion(), "")) {
                InsertOpinionReqVO insertOpinionReqVO = getOpinionVO(param);
                resInsertOpinion = bmsDctOpinionMapper.insertDctOpinion(insertOpinionReqVO);
            }


            if(param.getResasktype().equals("DEA41") || param.getResasktype().equals("DEA45") || param.getResasktype().equals("DEA51")
                    || param.getResasktype().equals("DEA61") || param.getResasktype().equals("DEA71")) { // 결재인 경우
                /* 비밀관리카드 변경*/
                UpdateCardBIA1ReqVO updateCardBIA1ReqVO = new UpdateCardBIA1ReqVO();
                // docid
                updateCardBIA1ReqVO.setDocid(param.getResdocid());
                // state DRDS3: 종료, DRDS4: 발송
                if(param.getRecvList() != null && param.getRecvList().size() > 0) {
                    if(param.getRecvList().size() == 1) {
                        if(!param.getRecvList().get(0).getReceiverid().equals("")) {
                            updateCardBIA1ReqVO.setState("DRDS4");
                        } else {
                            updateCardBIA1ReqVO.setState("DRDS3");
                        }
                    } else {
                        if(Objects.equals(param.getJudgeParam(), "offApprove")){
                            updateCardBIA1ReqVO.setState("DRDS3");
                        }else{
                            updateCardBIA1ReqVO.setState("DRDS4");
                        }
                    }
                } else {
                    updateCardBIA1ReqVO.setState("DRDS3");
                }

                resUpdateCardBIA1 = bmsDctRdocMapper.updateCardBIA1(updateCardBIA1ReqVO);

                /* 기안자 상태값 변경 DCST1 */
                if(!param.getResasktype().equals("DEA45")) {
                    UpdatePathReqVO updatePathParam2 = new UpdatePathReqVO();
                    if (param.getRecvList().size() > 0) { // 발송인 경우
                        for (InsertTempListReqVO tempReqVO : param.getPathList()) {
                            if (!Objects.equals(tempReqVO.getAsktype(), "")) {
                                if (tempReqVO.getAsktype().equals("DEA11")) {
                                    updatePathParam2.setDocid(param.getResdocid());
                                    updatePathParam2.setObjid(tempReqVO.getObjid());
                                    updatePathParam2.setState("DCST1");
                                }
                            }
                        }
                    } else {
                        for (InsertTempListReqVO tempReqVO : param.getPathList()) {
                            if (!Objects.equals(tempReqVO.getAsktype(), "")) {
                                if (tempReqVO.getAsktype().equals("DEA11")) {
                                    updatePathParam2.setDocid(param.getResdocid());
                                    updatePathParam2.setObjid(tempReqVO.getObjid());
                                    updatePathParam2.setState("DCST5");
                                }
                            }
                        }
                    }
                    int resUpdatePath2 = bmsDctPathMapper.updatePath(updatePathParam2);
                }

                /* 비밀관리기록부 저장 */
                InsertMgmtRegiReqVO insertMgmtRegiParam = new InsertMgmtRegiReqVO();
                // mgmtid
                String resSysGuidMgt = bmsDctRdocMapper.getSysGuid();
                insertMgmtRegiParam.setMgmtid("MGT"+resSysGuidMgt);
                // mgmtno
                String mgmtno = "";
                if(param.getMgmtno() != null) {
                    mgmtno = param.getMgmtno();
                } else {
                    String secLevel = "";
                    if(param.getSeclevel().equals("2")) {
                        secLevel = "II";
                    }
                    if(param.getSeclevel().equals("3")) {
                        secLevel = "III";
                    }
                    if(param.getSeclevel().equals("4")) {
                        secLevel = "대외비";
                    }
                    if(param.getSeclevel().equals("5")) {
                        secLevel = "일반";
                    }
                    String tempSeq = bmsDctRdocMapper.getTempSeq();
                    String recentMgmt = bmsDctRdocMapper.getRecentMgmt();
                    // 가장 최근 비밀관리기록부 데이터의 등록일보다 DateUtil.getYear()가 크면 시퀀스 reset
                    if(recentMgmt != null) {
                        if (DateUtil.getYear() > parseInt(recentMgmt.substring(0, 4))) {
                            tempSeq = bmsDctRdocMapper.resetTempSeq();
                        }
                    }
                    mgmtno = secLevel + "-" + DateUtil.getYear() + "-" + tempSeq;
                }
                insertMgmtRegiParam.setMgmtno(mgmtno); // 비밀등급-년도-시퀀스
                // docid
                insertMgmtRegiParam.setDocid(param.getResdocid());
                // seclevel
                insertMgmtRegiParam.setSeclevel(param.getSeclevel());
                // mgmtseq
                insertMgmtRegiParam.setMgmtseq(BigDecimal.valueOf(1)); // 이후에 수정해야함
                // regirecvyear
                insertMgmtRegiParam.setRegirecvyear(Integer.toString(DateUtil.getYear()));
                // regirecvgubun
                //if(param.getOridocid() == null) { // 사본이 아닌 경우
//                if(param.getCopyno().contains("원")) {
                if(param.getCopyno() == null || param.getCopyno().contains("원")) { //웹기안기 떼서 임시조치함.
                    insertMgmtRegiParam.setRegirecvgubun("1"); // 1: 생산, 2: 접수
                } else { // 사본인 경우
                    insertMgmtRegiParam.setRegirecvgubun("2"); // 1: 생산, 2: 접수
                }
                // regirecvdt
                insertMgmtRegiParam.setRegirecvdt(formatter.format(today));

                // 기안자의 부서아이디, 부서명 가져오도록
                String deptid = "";
                String deptname = "";
                if(!param.getResasktype().equals("DEA45")) {
                    for (InsertTempListReqVO tempReqVO : param.getPathList()) {
                        if (!Objects.equals(tempReqVO.getAsktype(), "")) {
                            if (tempReqVO.getAsktype().equals("DEA11")) {
                                deptid = tempReqVO.getDeptid();
                                deptname = tempReqVO.getDeptname();
                            }
                        }
                    }
                } else {
                    deptid = param.getDeptid();
                    deptname = param.getDeptname();
                }
                // 접수문서 상신할 결우의 기안자의 부서 설정.
                if(Objects.equals(param.getJudgeParam(), "receipt")){
                    deptname = param.getPathList().get(0).getDeptname();
                    deptid = param.getPathList().get(0).getDeptid();
                }
                // regirecvsendid
                insertMgmtRegiParam.setRegirecvsendid(deptid);
                // regirecvsendname
                insertMgmtRegiParam.setRegirecvsendname(deptname);
                // regirecvtype
                if(param.getPaperflag() != null && param.getPaperflag().equals("Y")) {
                    insertMgmtRegiParam.setRegirecvtype("2"); // 1: 전자, 2: 비전자
                } else {
                    insertMgmtRegiParam.setRegirecvtype("1"); // 1: 전자, 2: 비전자
                }
                // secttl
                insertMgmtRegiParam.setSecttl(param.getDocttl());
                // sectype
                insertMgmtRegiParam.setSectype(BigDecimal.valueOf(1));
                // attchcnt
                if(param.getFileList().size() > 0) {
                    insertMgmtRegiParam.setAttchcnt(BigDecimal.valueOf(param.getFileList().size()));
                } else {
                    insertMgmtRegiParam.setAttchcnt(BigDecimal.valueOf(1));
                }
                // copyno
                String copyno = "";

                if(param.getCopyno() != null) {
                    copyno = param.getCopyno();
                } else {
                    if(param.getRecvList() != null && param.getRecvList().size() > 0) {
                        if(!param.getRecvList().get(0).getReceiverid().isEmpty()) {
                            copyno = "원본/" + param.getRecvList().size();
                        } else {
                            copyno = "원/본";
                        }
                    } else {
                        copyno = "원/본";
                    }
                }
                insertMgmtRegiParam.setCopyno(copyno);

                // notiprotterm
                insertMgmtRegiParam.setNotiprotterm(param.getOrgnprotdt()==null?param.getCopyprotdt():param.getOrgnprotdt());
                // notiprsrvterm
                insertMgmtRegiParam.setNotiprsrvterm(param.getPrsrvterm()==null?"005":param.getPrsrvterm());
                // deptid
                insertMgmtRegiParam.setDeptid(deptid);
                // deptname
                insertMgmtRegiParam.setDeptname(deptname);
                // secstatus
                insertMgmtRegiParam.setSecstatus("MGT00"); // MGT00: 상태없음
                // authorid
                insertMgmtRegiParam.setAuthorid(param.getAuthorid());
                // authorname
                insertMgmtRegiParam.setAuthorname(param.getAuthorname());
                // authordutyname
                insertMgmtRegiParam.setAuthordutyname(param.getAuthordutyname());
                // rgstdt
                insertMgmtRegiParam.setRgstdt(formatter.format(today));
                // indt
                insertMgmtRegiParam.setIndt(formatter.format(today));


                int resInsertMgmtRegi = bmsDctMgmtRegiMapper.insertMgmtRegi(insertMgmtRegiParam);

                /* 비밀 속성부 mgmtno update */
                UpdateDctAttrReqVO updateDctAttrParam = new UpdateDctAttrReqVO();
                updateDctAttrParam.setDocid(param.getResdocid());
                updateDctAttrParam.setMgmtno(mgmtno);
                updateDctAttrParam.setCopyno(copyno);
                int resUpdateDctAttr = bmsDctAttrMapper.updateDctAttr(updateDctAttrParam);

                /* 권한관리카드 열람권한 저장 */
                String authDate = formatter.format(today);
                String authGubun = "01"; // 01: 열람, 02: 재사용, 03: 인쇄, 04: 대출, 05: 지출
                insertAuthCard(param, authDate, authGubun);
            }

            /* 이력카드 저장 */
            // 1인결재인 경우엔 기안(01), 결재(02) 둘다 이력 저장
            if(param.getResasktype().equals("DEA45")) {
                // 기안
                String reqHistSeq = bmsDctHistCardMapper.getHistSeq();
                InsertHistoryReqVO reqInsertHistoryReqVO = getHistoryVO(param, reqHistSeq, formatter.format(today));
                reqInsertHistoryReqVO.setHistgubun("01");
                reqInsertHistoryReqVO.setHiststatus("010");
                bmsDctHistCardMapper.insertHistory(reqInsertHistoryReqVO);

                // 결재
                String apprHistSeq = bmsDctHistCardMapper.getHistSeq();
                InsertHistoryReqVO apprInsertHistoryReqVO = getHistoryVO(param, apprHistSeq, formatter.format(today));
                apprInsertHistoryReqVO.setHistgubun("02");
                apprInsertHistoryReqVO.setHiststatus("011");
                bmsDctHistCardMapper.insertHistory(apprInsertHistoryReqVO);
            } else {
                String histSeq = bmsDctHistCardMapper.getHistSeq();
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, histSeq, formatter.format(today));
                bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
            }


        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resUpdateCardBIA1;
    }


    /* 결재대기문서 결재 반려 처리*/
    @Override
    @Transactional
    public int updateCardBIA2(List<MultipartFile> draftAttach, List<MultipartFile> refAttach, InsertTempListReqVO param){
        int resUpdateCardBIA2 = 0;
        double totalSize = 1;
        try {
            if(draftAttach != null)
                totalSize += draftAttach.size();
            if(refAttach != null)
                totalSize += refAttach.size();
            if(param.getFileList() != null)
                totalSize += param.getFileList().size();

            Date today = new Date();
            Locale currentLocale = new Locale("KOREAN", "KOREA");
            String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

            // 01. INSERT BMS_DCT_RDOC TABLE
            param.setReportdt(formatter.format(today));
            InsertTempListReqVO insertTempListReqVO = getRdocVO(param, formatter.format(today));
            insertTempListReqVO.setState("DRDS3"); // 종료
            int resInsertCardBIA = bmsDctRdocMapper.insertTempList(insertTempListReqVO);


            // 02. INSERT BMS_DCT_SUMMARY TABLE
            int resInsertSummary = 0;
            if(!Objects.equals(param.getSummary(), "")) {
                InsertTempListReqVO insertSummaryReqVO = getSummaryVO(param);
                resInsertSummary = bmsDctRdocMapper.insertSummary(insertSummaryReqVO);
            }


            // 03. INSERT BMS_DCT_ATTR TABLE
            int resInsertAttribute = 0;
            InsertAttributeReqVO insertAttributeParam = getAttrVO(param);
            resInsertAttribute = bmsDctAttrMapper.insertAttribute(insertAttributeParam);


            // 04. INSERT BMS_DCT_PATH TABLE
            int resInsertPath = 0;
            SelectDctPathReqVO selectDctPathReqVO = new SelectDctPathReqVO();
            selectDctPathReqVO.setDocid(param.getResdocid());
            List<SelectDctPathResVO> selectDctPathResVOS = bmsDctPathMapper.selectDctPath(selectDctPathReqVO);
            if(!selectDctPathResVOS.isEmpty()){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_dct_path");
                paramMap.put("docid", param.getResdocid());
                bmsDctRdocMapper.deleteTempList(paramMap);
            }
            for (InsertTempListReqVO tempReqVO : param.getPathList()) {
                if(!Objects.equals(tempReqVO.getAsktype(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    InsertPathReqVO insertPathParam = getPathVO(tempReqVO, formatter.format(today));

//                    for(SelectDctPathResVO tempResVO : selectDctPathResVOS) {
//                        if(tempResVO.getObjid().equals(tempReqVO.getObjid())) {
//                            insertPathParam.setReportdt(tempResVO.getReportdt());
                    insertPathParam.setActtype("DEA43");
                    insertPathParam.setState("DCST5");
//                        }
//                    }

                    resInsertPath += bmsDctPathMapper.insertPath(insertPathParam);
                }
            }


            // 06. INSERT BMS_DCT_APPR_RECV
            int resInsertApprRecv = 0;
            GetDctRecvReqVO getDctRecvReqVO = new GetDctRecvReqVO();
            getDctRecvReqVO.setDocid(param.getResdocid());
            List<GetDctRecvResVO> dctRecv = bmsDctApprRecvMapper.getDctRecv(getDctRecvReqVO);
            if(!dctRecv.isEmpty()){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_dct_appr_recv");
                paramMap.put("docid", param.getResdocid());
                bmsDctRdocMapper.deleteTempList(paramMap);
            }
            for (InsertTempListReqVO tempReqVO : param.getRecvList()) {
                if (!Objects.equals(tempReqVO.getApprrecvdeptname(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    tempReqVO.setSendOrgNameData(param.getSendOrgNameData());
                    tempReqVO.setSendNameData(param.getSendNameData());
                    InsertRecvReqVO insertRecvParam = getApprRecvVO(tempReqVO, formatter.format(today));
                    resInsertApprRecv += bmsDctApprRecvMapper.insertRecv(insertRecvParam);
                }
            }


            // 07. INSERT BMS_DCT_RDOC_RCVS
            int resInsertRdocRcvs = 0;
            SelectDctReaderReqVO selectDctReaderReqVO = new SelectDctReaderReqVO();
            selectDctReaderReqVO.setDocid(param.getResdocid());
            List<SelectDctReaderResVO> selectDctReaderResVOS = bmsDctRdocRcvsMapper.selectDctReader(selectDctReaderReqVO);
            if(!selectDctReaderResVOS.isEmpty()){
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("table", "sbms.sbms_user.bms_dct_rdoc_rcvs");
                paramMap.put("docid", param.getResdocid());
                bmsDctRdocMapper.deleteTempList(paramMap);
            }
            for (InsertTempListReqVO tempReqVO : param.getRcvsList()) {
                if (!Objects.equals(tempReqVO.getRcvsusername(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());
                    InsertDocIdBmsDctRdocRcvsListReqVO insertDocIdBmsDctRdocRcvsListParam = getRdocRcvsVO(tempReqVO, formatter.format(today));
                    resInsertRdocRcvs += bmsDctRdocRcvsMapper.insertDocIdBmsDctRdocRcvsList(insertDocIdBmsDctRdocRcvsListParam);
                }
            }

            Map<String, Object> docidData = new HashMap<>();
            docidData.put("docid", param.getResdocid());
            List<Map<String, Object>> selectList = bmsDctFileMapper.selectDctFileDFT02(docidData);

            // 관련근거 - 직접입력 첨부파일
            AttachFileVO refFileVO = getFileVO(param,formatter.format(today));
            refFileVO.setGubun("DFT03");
            refFileVO.setTotal(totalSize);
            List<AttachFileVO> refFileInfos = attachFileService.insertAttachFile(refAttach, refFileVO);


            // 08. INSERT BMS_TASK_INFOMATION TABLE
            int resInsertTaskInfo = 0;
            if(param.getDelRef() != null){
                for(String infoId : param.getDelRef()){

                    SelectTaskInfoTempReqVO selectTaskInfoTempReqVO = new SelectTaskInfoTempReqVO();
                    selectTaskInfoTempReqVO.setInfomationid(infoId);
                    List<SelectTaskInfoTempResVO> selectTaskInfoTempResVOS = bmsTaskInfomationTempMapper.selectTaskInfo(selectTaskInfoTempReqVO);
                    Optional<List<SelectTaskInfoTempResVO>> optionalSelectTaskInfoTempResVOS = Optional.ofNullable(selectTaskInfoTempResVOS);

                    if (optionalSelectTaskInfoTempResVOS.isPresent() && !optionalSelectTaskInfoTempResVOS.get().isEmpty()) {
                        String attathId = optionalSelectTaskInfoTempResVOS.get().get(0).getAttachid();
                        if(attathId != null){
                            AttachFileVO attachFileVO = new AttachFileVO();
                            attachFileVO.setFleid(attathId);
                            AttachFileVO attachFileByFleid = attachFileMapper.getAttachFileByFleid(attachFileVO);
                            String fileFullPath = attachFileByFleid.getFlepath();
                            File delFile = new File(fileFullPath);
                            delFile.delete();
                            attachFileMapper.deleteAttachFileByFleid(attachFileVO);
                        }
                    }

                    Map<String, Object> fileMap = new HashMap<>();
                    fileMap.put("infomationid", infoId);
                    bmsDctRdocMapper.deleteRef(fileMap);
                }
            }

            int cnt = 0;
            int temp = 0;
            for (InsertTempListReqVO tempReqVO : param.getRefList()) {
                if (!Objects.equals(tempReqVO.getInfotype(), "")) {
                    tempReqVO.setResdocid(param.getResdocid());
                    tempReqVO.setResuserid(param.getResuserid());

                    if(tempReqVO.getFileexist() != null){
                        if(tempReqVO.getFileexist().equals("N")){
                            resInsertTaskInfo = insertTaskInfo(tempReqVO, formatter, today, resInsertTaskInfo);
                        }else{
                            tempReqVO.setAttachid(refFileInfos.get(cnt++).getFleid());
                            resInsertTaskInfo = insertTaskInfo(tempReqVO, formatter, today, resInsertTaskInfo);
                        }
                    }else{

                        if(temp == 0){
                            SelectTaskInfoTempReqVO selectTaskInfoTempReqVO = new SelectTaskInfoTempReqVO();
                            selectTaskInfoTempReqVO.setDocid(param.getResdocid());
                            List<SelectTaskInfoTempResVO> selectTaskInfoTempResVOS = bmsTaskInfomationTempMapper.selectTaskInfoTemp(selectTaskInfoTempReqVO);
                            Optional<List<SelectTaskInfoTempResVO>> optionalSelectTaskInfoTempResVOS = Optional.ofNullable(selectTaskInfoTempResVOS);

                            if (optionalSelectTaskInfoTempResVOS.isPresent() && !optionalSelectTaskInfoTempResVOS.get().isEmpty()) {
                                for(SelectTaskInfoTempResVO optionalSelectTaskInfoTempResVO : optionalSelectTaskInfoTempResVOS.get()){
                                    InsertTaskInfomationtReqVO vo = InsertTaskInfomationtReqVO.builder()
                                            .infomationid("INF" + bmsDctRdocMapper.getSysGuid())
                                            .resultid(param.getResdocid())
                                            .attachid(optionalSelectTaskInfoTempResVO.getAttachid())
                                            .infotext(optionalSelectTaskInfoTempResVO.getInfotext())
                                            .infotype(optionalSelectTaskInfoTempResVO.getInfotype())
                                            .deleteyn("N")
                                            .infotitle(optionalSelectTaskInfoTempResVO.getInfotitle())
                                            .inid(optionalSelectTaskInfoTempResVO.getInid())
                                            .indt(formatter.format(today))
                                            .build();
                                    temp += bmsTaskInfomationMapper.insertTaskInfo(vo);
                                }
                            }

                        }

                    }
                }
            }

            // 10. INSERT BMS_DCT_FILE TABLE
            if(param.getDelFileids() != null){
//                AttachFileVO delFileVO = null;
//                for (String DelFleid : param.getDelFileids()) {
//                    delFileVO = getDelFileVO(param.getResdocid(), DelFleid);
//                    attachFileService.deleteAttachFile(delFileVO);
//                }

                List<String> fleidList = new ArrayList<>();
                for(String DelFleid : param.getDelFileids()) {
                    fleidList.add(DelFleid);
                }

                Iterator<Map<String, Object>> iterator = selectList.iterator();
                while(iterator.hasNext()) {
                    Map<String, Object> map = iterator.next();
                    String fleidValue = map.get("fleid").toString();

                    if (fleidList.contains(fleidValue)) {
                        iterator.remove();
                    }
                }
            }
            int fileCnt = draftAttach == null ? 0:draftAttach.size();
            for(Map<String, Object> selectData : selectList) {
                InsertFileReqVO insertFileReqVO = new InsertFileReqVO();
                insertFileReqVO.setDocid(param.getResdocid());

                String resSysGuid = bmsDctRdocMapper.getSysGuid();
                insertFileReqVO.setFleid("FLE"+resSysGuid);

                insertFileReqVO.setFleorder(fileCnt);
                insertFileReqVO.setFlepath(selectData.get("flepath").toString());
                insertFileReqVO.setFlesize((BigDecimal) selectData.get("flesize"));
                insertFileReqVO.setFlettl(selectData.get("flettl").toString());
                insertFileReqVO.setFleurl(selectData.get("fleurl").toString());
                insertFileReqVO.setGubun(selectData.get("gubun").toString());
                insertFileReqVO.setIndt(formatter.format(today));
                insertFileReqVO.setInid(param.getResuserid());
                insertFileReqVO.setMarkflag(selectData.get("markflag").toString());
                insertFileReqVO.setOridocid(selectData.get("oridocid").toString());
                insertFileReqVO.setOrigubun(selectData.get("origubun").toString());
                insertFileReqVO.setSfilename(selectData.get("sfilename").toString());

                bmsDctFileMapper.insertFile(insertFileReqVO);
                fileCnt++;
            }
            AttachFileVO attachFileVO = getFileVO(param,formatter.format(today));
            attachFileVO.setGubun("DFT02");
            attachFileVO.setTotal(totalSize);
            if(refAttach != null)
                attachFileVO.setProgressOffset(refAttach.size());
            attachFileService.insertAttachFile(draftAttach, attachFileVO);



            /* 문서 처리(반려) */
            /* 경로 상태값 변경 DCST4 */
            UpdatePathReqVO updatePathParam = new UpdatePathReqVO();
            updatePathParam.setDocid(param.getResdocid());
            updatePathParam.setObjid(param.getResuserid());
            updatePathParam.setMainver(BigDecimal.valueOf(param.getMainver())); // 수정이력
            updatePathParam.setReportdt(formatter.format(today)); // 처리결과
//            updatePathParam.setState("DCST4");

            int resUpdatePath = bmsDctPathMapper.updatePath(updatePathParam);

            /* 처리의견 저장 */
            int resInsertOpinion = 0;
            if(!Objects.equals(param.getOpinion(), "")) {
                InsertOpinionReqVO insertOpinionReqVO = getOpinionVO(param);
                resInsertOpinion = bmsDctOpinionMapper.insertDctOpinion(insertOpinionReqVO);
            }

            /* 이력카드 저장 */
            String histSeq = bmsDctHistCardMapper.getHistSeq();
            InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, histSeq, formatter.format(today));
            int resInsertHistory = bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);

        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resUpdateCardBIA2;
    }


    /* 결재 > 진행비밀함 > 진행비밀함 목록 조회 */
    @Override
    public PageInfo<SelectIngListResVO> selectIngList(SelectIngListReqVO param){
        PageInfo<SelectIngListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctRdocMapper.selectIngList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 결재 > 결재완료함 > 결재완료함 목록 조회 */
    @Override
    public PageInfo<SelectApprovalCompleteListResVO> selectApprovalCompleteList(SelectApprovalCompleteListReqVO param){
        PageInfo<SelectApprovalCompleteListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctRdocMapper.selectApprovalCompleteList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 발송 처리 */
    @Override
    @Transactional
    public int updateCardBIA(List<MultipartFile> draftAttach, List<MultipartFile> refAttach, UpdateCardBIAReqVO param, String judgeParam) {
        int resUpdateCardBIA = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String currentDateTime = LocalDateTime.now().format(formatter);
        double totalSize = 0;

        /* 로그인한 유저 정보 조회 */
        GetUserLoginResVO resGetUserLogin;
        GetUserLoginReqVO getUserLoginReqVO = GetUserLoginReqVO.builder()
                .userid(param.getLoginuserid())
                .build();
        resGetUserLogin = comUserinfoMapper.getUserLogin(getUserLoginReqVO.builder()
                .userid(getUserLoginReqVO.getUserid())
                .build());

        /* 로그인한 유저의 부처정보 조회(상위 부서 조회) */
        SelectOrganizationReqVO getOrganizationListReqParam = SelectOrganizationReqVO.builder()
                .orgid(resGetUserLogin.getParentorgid())
                .build();

        List<SelectOrganizationResVO> resOrganizationInfo = comOrganizationinfoMapper.selectOrganization(getOrganizationListReqParam.builder()
                .orgid(getOrganizationListReqParam.getOrgid())
                .build());

        System.out.println("currentDateTime >>>>>>>>> : " + currentDateTime);
        try {
            if(draftAttach != null)
                totalSize += draftAttach.size();
            if(refAttach != null)
                totalSize += refAttach.size();
            if(param.getFileList() != null)
                totalSize += param.getFileList().size();

            /* 유통비밀송수신결과정보 저장 결과 */
            int resInsertRelayHis = 0;
            String exstatus = "";
            /* 유통비밀송수신결과정보 저장 */
            // INSERT & UPDATE bms_dct_relay_his
//            exstatus = "arrive"; // 추후 수신기 확인 하여 받아온 값으로 교체
//            exstatus = "fail"; // 추후 수신기 확인 하여 받아온 값으로 교체
            exstatus = param.getExstatus();

            /* 경로 상태값 변경 */
            // ***** Operation = updatePath
            // to do
            // 03. UPDATE bms_dct_path
            UpdatePathReqVO updatePathParam = UpdatePathReqVO.builder()
                    .docid(param.getDocid())
                    .objid(param.getLoginuserid())
                    .state("DCST5") //처리완료
//                    .pathorder(BigDecimal.valueOf(500))
                    .build();
            int resUpdatePath = bmsDctPathMapper.updatePath(updatePathParam);

            /* 본문 저장 start */
            // 2024-01-23 l.b.h
            Date today = new Date();
            Locale currentLocale = new Locale("KOREAN", "KOREA");
            String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
            SimpleDateFormat formatter2 = new SimpleDateFormat(pattern, currentLocale);

            InsertTempListReqVO draftData = new InsertTempListReqVO();
            draftData.setDrafturl(param.getDrafturl());
            draftData.setDraftdocttl(param.getDraftdocttl());
            draftData.setResdocid(param.getDocid());
            draftData.setResuserid(param.getAuthorid());
            draftData.setGubun("DFT01"); //본문
            draftData.setOrigubun("DOPT2");

            // 본문파일 받아와서 변환
            List<InsertTempListReqVO> draftFileList = new ArrayList<>();
            InsertTempListReqVO draftFileData = new InsertTempListReqVO();

            if (draftData.getDrafturl() != null && !draftData.getDrafturl().equals("")) {
                if(!draftData.getDrafturl().contains("fileUpload")) {
                    AttachFileVO attachFileVO = insertDraftFile(draftData, formatter2, today);

                    draftFileData.setDocid(attachFileVO.getDocid());
                    draftFileData.setGubun(attachFileVO.getGubun());
                    draftFileData.setInid(attachFileVO.getInid());
                    draftFileData.setIndt(attachFileVO.getIndt());
                    draftFileData.setOrigubun(attachFileVO.getOrigubun());
                    draftFileData.setOridocid(attachFileVO.getOridocid());
                    draftFileData.setSfilename(attachFileVO.getSfilename());
                    draftFileData.setFlepath(attachFileVO.getFlepath());
                    draftFileData.setFlesize(new BigDecimal(attachFileVO.getFlesize()));

                    draftFileList.add(draftFileData);
                }
            }
            param.setDraftfile(draftFileList);
            /* 본문 저장 end */

            UpdateCardBIAReqVO updateCardBIAParam;

            String resSysGuid = "";

            int insertCardBIASuccessCnt = 0;

            String[] partsTemp = param.getCopyno().split("/");
            String copyPart = "";
            if (partsTemp.length > 1) {
                copyPart = partsTemp[0];
            } else {
                copyPart = "(원본에 '/' 문자가 없습니다.)";
            }
            if ( ( Objects.equals(param.getState(),"DRDS5") || Objects.equals(param.getState(),"DRDS0") ) && Objects.equals(copyPart, "원본") ) {
                List<InsertTempListReqVO> list = param.getRecvMultiList();
                if (!list.isEmpty()) {
                    list.subList(1, list.size()).clear();
                }
            }
            /* 오프라인발송 */
            if( Objects.equals(param.getJudgeParam(), "offSend") ){
                updateCardBIAParam = UpdateCardBIAReqVO.builder()
                        .docid(param.getOridocid()) // 원본docid
                        .state("DRDS8") //발송완료
                        .updt(currentDateTime)
                        .build();
                // UPDATE bms_dct_rdoc
                resUpdateCardBIA += bmsDctRdocMapper.updateCardBIA(updateCardBIAParam);
                updateCardBIAParam = UpdateCardBIAReqVO.builder()
                        .docid(param.getDocid()) // 사본docid
                        .state("DRDS8") //발송완료
                        .updt(currentDateTime)
                        .build();
                // UPDATE bms_dct_rdoc
                resUpdateCardBIA += bmsDctRdocMapper.updateCardBIA(updateCardBIAParam);

                /* 이력카드 저장 */
                // INSERT bms_dct_hist_card
                int resInsertHistory = 0;
                String histSeq = "0";
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resGetUserLogin, histSeq, currentDateTime);
                insertHistoryReqVO.setDocid(param.getOridocid()); //원본docid에 대한 이력(사본 이력추적을 원본docid로)
//                for (int i = 0; i < 2; i++) {
//                    if (i == 0) {
//                        insertHistoryReqVO.setHistgubun("09"); //발신
//                        insertHistoryReqVO.setEtc(param.getRecvdeptname() + " " + param.getCopyno());//수신부서명, 사본번호
//                        histSeq = bmsDctHistCardMapper.getHistSeq();
//                        insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
//                    } else {
//                        insertHistoryReqVO.setHistgubun("08"); //시행
//                        histSeq = bmsDctHistCardMapper.getHistSeq();
//                        insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
//                    }
//                    resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
//                }
                insertHistoryReqVO.setHistgubun("38"); //오프라인발송
                insertHistoryReqVO.setEtc(param.getRecvdeptname() + " " + param.getCopyno());//수신부서명, 사본번호
                histSeq = bmsDctHistCardMapper.getHistSeq();
                insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
                System.out.println(resInsertHistory);
            }

            if(param.getRecvMultiList() != null) {
                /* 수신처 수(recvList) 만큼 반복  start */
                for (int recvListIdx = 0; recvListIdx < param.getRecvMultiList().size(); recvListIdx++) {
                    currentDateTime = LocalDateTime.now().format(formatter);
                    InsertRelayHisReqVO insertRelayHisReqVO = getInsertRelayHisVO(param, recvListIdx, exstatus, currentDateTime);

                    // 00. CREATE SYS_GUID STRING
                    resSysGuid = bmsDctRdocMapper.getSysGuid();

                    String resEnfDocId = "";
                    if (param.getEnfdocid() == null || param.getEnfdocid().isEmpty()) {
                        resEnfDocId = "ENF" + resSysGuid;
                    } else {
                        resEnfDocId = param.getEnfdocid();
                    }
                    String resDocId = "";
                    String histSeq = "0";
                    int reTryCount = 0;
                    if (param.getCopyparams() != null) {
                        reTryCount = param.getCopyparams().get(0).getRetry();
                    }
                    if (reTryCount == 3) {
                        reTryCount = 0;
                    }

                    List<Map<String, List<AttachFileVO>>> insertCardBIA = null;

                    if ((Objects.equals(param.getIsresend(), "resend") && param.getCopyparams() != null) || Objects.equals(param.getIsNotSend(), "NotSend")) {
                        if (param.getCopyparams() != null && !param.getCopyparams().isEmpty()) {
                            resDocId = param.getCopyparams().get(0).getDocid(); //사본docid
                        } else {
                            resDocId = param.getDocid(); //원본docid (발송보류상태에서 발송안함의 케이스. 발송을 안하여 사본이 없음.)
                        }

                        //                    resDocId = param.getDocid(); //원본docid
                        /* 이력카드 저장 */
                        // INSERT bms_dct_hist_card
                        int resInsertHistory = 0;
                        InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resGetUserLogin, histSeq, currentDateTime);
                        insertHistoryReqVO.setDocid(param.getDocid()); //원본docid
                        if (Objects.equals(param.getIsresend(), "resend")) {
                            insertHistoryReqVO.setHistgubun("36"); //재발송
                        } else {
                            insertHistoryReqVO.setHistgubun("35"); //발송안함
                        }
                        histSeq = bmsDctHistCardMapper.getHistSeq();
                        insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
                        String copyNum = "";
                        String copyMaxNum = "";
                        if (param.getCopyOrder() != null) {
                            String[] parts = param.getCopyno().split("/");
                            if (parts.length > 1) {
                                copyMaxNum = parts[1];
                            } else {
                                copyMaxNum = "(원본에 '/' 문자가 없습니다.)";
                            }
                            copyNum = param.getCopyOrder();
                        } else {
                            if (param.getCopyparams() != null && !param.getCopyparams().isEmpty()) {
                                copyNum = (Integer.toString(recvListIdx + 1) + "/" + param.getRecvMultiList().size());
                            } else {
                                copyNum = param.getCopyno();
                            }
                        }
                        insertHistoryReqVO.setEtc(param.getRecvMultiList().get(recvListIdx).getApprrecvdeptname() + " " + copyNum + "/" + copyMaxNum);
                        resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
                        System.out.println(resInsertHistory);
                   } else {
                        resDocId = "DCT" + resSysGuid; //사본docid //원본은 param.getDocid()
                        if(Objects.equals(param.getJudgeParam(), "distribute")){
                            resDocId = param.getResdocid();
                        }else{
                            /* 사본저장 */
                            // INSERT bms_dct_rdoc
                            InsertCopyCardReqVO insertCopyCardParam = InsertCopyCardReqVO.builder()
                                    .docid((resDocId))
                                    .docttl(param.getDocttl())
                                    .gubun("DOPT2") //시행용
                                    .keyword(param.getKeyword())
                                    .authorid(param.getAuthorid())
                                    .authorname(param.getAuthorname())
                                    .authordeptid(param.getAuthordeptid())
                                    .authordutyname(param.getAuthordutyname())
                                    .authordeptname(param.getAuthordeptname())
                                    .state("DRDS4") //발송
                                    .indt(currentDateTime)
                                    .reportdt(currentDateTime) //or .reportdt(param.getReportdt())
                                    .updt(currentDateTime)
                                    .oridocid(param.getDocid())
                                    .build();
                            int resInsertCopyCard = bmsDctRdocMapper.insertCopyCard(insertCopyCardParam);
                        }
                        /* 발송 의견 저장 */
                        // 02. INESERT bms_dct_doc_sendinfo
                        InsertDocSendInfoReqVO insertDctDocSendinfoParam = InsertDocSendInfoReqVO.builder()
                                //                    .docid(param.getDocid()) //원본
                                .docid(resDocId) //사본
                                .sendopinion(param.getSendopinion())
                                .docgubun("A")
                                .tempflag("N")
                                .build();
                        int resInsertDctDocSendinfo = bmsDctDocSendinfoMapper.insertDctDocSendinfo(insertDctDocSendinfoParam);

                        /* **사본생성** 기안시 저장되는 데이터들을 사본도 똑같이 저장한다. */
                        InsertTempListReqVO insertParam = convertUpdateToInsertParam(param);
                        insertParam.setResdocid(resDocId); //사본docid
                        insertParam.setGubun("DOPT2"); //시행용
                        insertParam.setOridocid(param.getDocid()); //원본docid
                        insertParam.setRecvList(param.getRecvMultiList());

                        List<InsertTempListReqVO> tempList = new ArrayList<>();
                        for (InsertSendRecvReqVO rcvs : param.getRcvsList()) {
                            tempList.add(convertSendRecvToTempList(rcvs));
                        }
                        insertParam.setRcvsList(tempList);
                        if(Objects.equals(param.getJudgeParam(), "distribute")){
                            insertParam.setJudgeParam(param.getJudgeParam());
                            insertParam.setSendNameData(param.getSendNameData());
                            insertParam.setSendOrgNameData(param.getSendOrgNameData());
                            insertParam.setCopyno(param.getCopyno());
                        }

                        judgeParam = "send";
                        insertCardBIA = insertCardBIA(draftAttach, refAttach, insertParam, judgeParam, insertCardBIASuccessCnt);
                        System.out.println("insertCardBIA = " + insertCardBIA);
                        insertCardBIASuccessCnt++;
                        System.out.println("insertCardBIASuccessCnt = " + insertCardBIASuccessCnt);

                        /* 이력카드 저장 */
                        // INSERT bms_dct_hist_card
                        int resInsertHistory = 0;
                        InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resGetUserLogin, histSeq, currentDateTime);
                        insertHistoryReqVO.setDocid(param.getDocid()); //원본docid에 대한 이력(사본 이력추적을 원본docid로)
                        if(Objects.equals(param.getJudgeParam(), "distribute")){
//                            insertHistoryReqVO.setDocid(param.getResdocid());
                            insertHistoryReqVO.setDocid(resDocId);
                            insertHistoryReqVO.setHistgubun("11"); //배부
                            insertHistoryReqVO.setEtc(param.getRecvMultiList().get(recvListIdx).getApprrecvdeptname() + " " + (recvListIdx + 1) + "/" + param.getRecvMultiList().size());//수신부서명, 사본번호
                            histSeq = bmsDctHistCardMapper.getHistSeq();
                            insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
                            resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
                        }else{
                            for (int i = 0; i < 2; i++) {
                                if (i == 0) {
                                    insertHistoryReqVO.setHistgubun("08"); //시행
                                    insertHistoryReqVO.setEtc(param.getRecvMultiList().get(recvListIdx).getApprrecvdeptname() + " " + (recvListIdx + 1) + "/" + param.getRecvMultiList().size());//수신부서명, 사본번호
                                    histSeq = bmsDctHistCardMapper.getHistSeq();
                                    insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
                                } else {
                                    insertHistoryReqVO.setHistgubun("09"); //발신
                                    histSeq = bmsDctHistCardMapper.getHistSeq();
                                    insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
                                }
                                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
                            }
                        }
                        System.out.println(resInsertHistory);
                    }

                    currentDateTime = LocalDateTime.now().format(formatter);

                    if (Objects.equals(param.getIsNotSend(), "NotSend")) {
                        updateCardBIAParam = UpdateCardBIAReqVO.builder()
                                .docid(param.getDocid()) // 원본docid
                                .state("DRDS5") //발송안함
                                .updt(currentDateTime)
                                .build();
                        // UPDATE bms_dct_rdoc
                        resUpdateCardBIA += bmsDctRdocMapper.updateCardBIA(updateCardBIAParam);
                        if (!Objects.equals(param.getDocid(), resDocId)) {
                            updateCardBIAParam = UpdateCardBIAReqVO.builder()
                                    //                        .docid(param.getDocid()) // 원본docid
                                    .docid(resDocId) // 사본docid
                                    .state("DRDS5") //발송안함
                                    .updt(currentDateTime)
                                    .build();
                            // UPDATE bms_dct_rdoc
                            resUpdateCardBIA += bmsDctRdocMapper.updateCardBIA(updateCardBIAParam);
                        }
                    }

                    String convertPDF = "";

                    if (insertCardBIA != null && insertCardBIA.size() > 1) {
                        Map<String, List<AttachFileVO>> map0 = insertCardBIA.get(0);
                        // 본문파일 responseStr 값을 검사
                        if (map0.containsKey("resDraftfileList")) {
                            List<AttachFileVO> fileList0 = map0.get("resDraftfileList");
                            for (AttachFileVO vo : fileList0) {
                                String responseStr = vo.getResponseStr();
                                if (!Objects.equals(responseStr, "ok")) {
                                    convertPDF = "fail";
                                }
                            }
                        }
                    }

                    if (insertCardBIA != null && insertCardBIA.size() > 1) {
                        Map<String, List<AttachFileVO>> map1 = insertCardBIA.get(1);
                        // 붙임파일리스트의 responseStr 값을 검사
                        if (map1.containsKey("resDFT02fileList")) {
                            List<AttachFileVO> fileList1 = map1.get("resDFT02fileList");
                            for (AttachFileVO vo : fileList1) {
                                String responseStr = vo.getResponseStr();
                                if (!Objects.equals(responseStr, "ok")) {
                                    convertPDF = "fail";
                                }
                            }
                        }
                    }

                    /* 발송 및 재발송 케이스에서 수신기나 변환 실패시 로직  */
                    if (!Objects.equals(param.getIsNotSend(), "NotSend") && (Objects.equals(exstatus, "fail") || Objects.equals(convertPDF, "fail"))) {
                        updateCardBIAParam = UpdateCardBIAReqVO.builder()
                                .docid(param.getDocid()) // 원본docid
                                .state("DRDS9") //발송실패
                                .updt(currentDateTime)
                                .build();
                        if(Objects.equals(param.getJudgeParam(), "distribute")){
                            updateCardBIAParam.setDocid(param.getResdocid());
                        }
                        // UPDATE bms_dct_rdoc
                        resUpdateCardBIA += bmsDctRdocMapper.updateCardBIA(updateCardBIAParam);
                        updateCardBIAParam = UpdateCardBIAReqVO.builder()
                                //                        .docid(param.getDocid()) // 원본docid
                                .docid(resDocId) // 사본docid
                                .state("DRDS9") //발송실패
                                .updt(currentDateTime)
                                .build();
                        // UPDATE bms_dct_rdoc
                        resUpdateCardBIA += bmsDctRdocMapper.updateCardBIA(updateCardBIAParam);

                        resUpdateCardBIA += 10000; //프론트에서 발송실패 구분용도.


                        if (!Objects.equals(param.getIsresend(), "resend")) {
                            insertRelayHisReqVO.setRetry(0);
                        } else {
                            insertRelayHisReqVO.setRetry(reTryCount + 1);
                        }
                        insertRelayHisReqVO.setDocid(resDocId); // 발송에 따른 사본docid
                        insertRelayHisReqVO.setReceiverid(param.getRecvMultiList().get(recvListIdx).getReceiverid());
                        insertRelayHisReqVO.setDocorder(parseInt(param.getRecvMultiList().get(recvListIdx).getApprrecvseq()));
                        insertRelayHisReqVO.setExtype("S"); //전송
                        if (Objects.equals(convertPDF, "fail")) {
                            insertRelayHisReqVO.setErrormsg("PDF변환이 실패함");
                        } else {
                            insertRelayHisReqVO.setErrormsg("전송은 하였으나, 수신기에서 받지 못함");
                        }
                        insertRelayHisReqVO.setActername(resGetUserLogin.getUsername());
                        insertRelayHisReqVO.setActerdeptname(resGetUserLogin.getDeptname());
                        insertRelayHisReqVO.setIndeptid(resGetUserLogin.getDeptid());
                        resInsertRelayHis += bmsDctRdocMapper.insertRelayHis(insertRelayHisReqVO);

                        // 03. UPDATE bms_dct_path
                        updatePathParam = UpdatePathReqVO.builder()
                                .docid(resDocId)
                                .objid(param.getLoginuserid())
                                .state("DCST5") //처리완료
                                .build();
                        resUpdatePath = bmsDctPathMapper.updatePath(updatePathParam);
                        //                    throw new FailException("수신불량으로 전송 실패하였습니다.");

                        String resSysGuidTemp = resEnfDocId;
                        /* 권한을 갖고있는 userid 조회하고, 시행비밀관리카드 저장 */
                        String vgroupid = "";
                        Set<String> processedGubuns = new HashSet<>(); //recvlist 에 처리한(중복되는) gubun을 담기위함
                        List<InsertEnforceRecvReqVO> recv = param.getRecvlist();
                        int resInsertEnforceCard = 0;
                        for (int thisrecvListIdx = recvListIdx; thisrecvListIdx < recv.size(); thisrecvListIdx++) {
                            InsertEnforceRecvReqVO currentRecv = param.getRecvlist().get(recvListIdx);
                            String gubun = (String) currentRecv.getGubun();


                            if (processedGubuns.contains(gubun)) { //결과적으로 고유한 gubun값만 처리하는 로직.
                                continue;
                            }
                            processedGubuns.add(gubun); //처리한 구분에 추가함.

                            switch (gubun) {
                                case "RDR08": //원내 코드
                                    vgroupid = "5220";
                                    break;
                                case "RDR01": // 대내 코드
                                    vgroupid = "5210";
                                    break;
                                case "RDR02": //  외부 코드
                                    vgroupid = "5200"; //관리번호 확인후 수정 요망
                                    break;

                                default:
                                    vgroupid = "5220"; //기본값을 원내코드로
                                    break;
                            }
                            SelectVgroupUserReqVO selectVgroupUserReqVO = SelectVgroupUserReqVO.builder()
                                    .vgroupid(vgroupid)
                                    .build();

                            /* selectReceiptList에서 bms_dct_enf_doc.docstatus 값을 조회 */
                            SelectReceiptListResVO resSelectReceiptList;
                            SelectReceiptListReqVO selectReceiptListParamVO = SelectReceiptListReqVO.builder()
                                    .userid(resGetUserLogin.getUserid())
                                    .build();
                            /* 권한을 갖고있는 userid 조회 */
                            // 05-1. SELECT com_vgroup_user
                            List<SelectVgroupUserResVO> resSelectVgroupUserIds = comVgroupUserMapper.selectVgroupUser(selectVgroupUserReqVO);

                            /* 시행비밀관리카드 저장 */
                            // ***** Operation = insertEnforceCard
                            // to do
                            // 05-2. INSERT bms_dct_enf_doc
                            int enfIdx = 0;
                            for (int vgroupIdx = 0; vgroupIdx < resSelectVgroupUserIds.size(); vgroupIdx++) {
//                                if (param.getRecvMultiList().get(recvListIdx).getReceiverid().equals(resSelectVgroupUserIds.get(vgroupIdx).getOrgid()) || Objects.equals(param.getRecvMultiList().get(recvListIdx).getRelaytype(), "anylo")) {
                                    SelectVgroupUserResVO tempVO = resSelectVgroupUserIds.get(vgroupIdx);
                                    BigInteger bigIntResSysGuid = new BigInteger(resSysGuid);
                                    BigInteger result = bigIntResSysGuid.add(BigInteger.valueOf(enfIdx));
//                                    String resSysGuidTemp = "ENF" + result.toString();
                                    if(Objects.equals(param.getIsresend(), "resend")){
                                        resSysGuidTemp = param.getEnfdocid();
                                    }
                                    InsertEnforceCardReqVO insertEnforceCardParam = InsertEnforceCardReqVO.builder()
                                            .enfdocid(resSysGuidTemp)
                                            .inid(tempVO.getUserid()) //배부담당자id com_vgroup_user.userid
                                            .recvdeptid(tempVO.getOrgid()) //발송시 채워둔 수신부서id (접수대기함에서 사용)
                                            .docstatus(null)
                                            .moveflag("N")
                                            //                            .docid(param.getDocid())//원본비밀문서
                                            .docid(resDocId)//사본비밀문서
                                            .enfgubun("N") //대내
                                            .distributeflag("N") //비배부 (배부처리하면 배부 Y)
                                            .emergency(param.getEmergency())
                                            .doctitle(param.getDocttl())
                                            .indt(currentDateTime)
                                            .build();
                                    if(Objects.equals(param.getJudgeParam(), "distribute")){
                                        UpdateEnfDocRepathReqVO updateEnfDocRepathParam = UpdateEnfDocRepathReqVO.builder()
                                                .enfdocid(param.getEnfdocid())
                                                .inid(tempVO.getUserid()) //배부담당자id com_vgroup_user.userid
                                                .recvdeptid(tempVO.getOrgid()) //발송시 채워둔 수신부서id (접수대기함에서 사용)
                                                .docstatus("EDS09")//접수대기
                                                .moveflag("N")
                                                .docid(resDocId)//사본비밀문서
                                                .enfgubun("N") //대내
                                                .distributeflag("N") //비배부 (배부처리하면 배부 Y)
                                                .emergency(param.getEmergency())
                                                .doctitle(param.getDocttl())
                                                .indt(currentDateTime)
                                                .paperdocflag("Y")
                                                .distributeflag("Y")
                                                .build();
                                        resInsertEnforceCard += bmsDctEnfDocMapper.updateEnfDocRepath(updateEnfDocRepathParam);
                                    }else{
                                        resInsertEnforceCard += bmsDctEnfDocMapper.insertEnforceCard(insertEnforceCardParam);
                                    }
                                    enfIdx++;
//                                }
                            }
                        }

                    } else if (Objects.equals(exstatus, "arrive") && !Objects.equals(param.getIsNotSend(), "NotSend")) {

                        updateCardBIAParam = UpdateCardBIAReqVO.builder()
                                .docid(param.getDocid()) // 원본docid
                                .state("DRDS8") //발송완료
                                .updt(currentDateTime)
                                .build();
                        // UPDATE bms_dct_rdoc
                        resUpdateCardBIA += bmsDctRdocMapper.updateCardBIA(updateCardBIAParam);

                        updateCardBIAParam = UpdateCardBIAReqVO.builder()
                                .docid(resDocId) // 사본docid
                                .state("DRDS8") //발송완료  //DRDS5 발송
                                .updt(currentDateTime)
                                .build();
                        if(Objects.equals(param.getJudgeParam(), "distribute")){
                            updateCardBIAParam.setState("DRDS2"); // 처리중(배부)
                        }
                        // UPDATE bms_dct_rdoc
                        resUpdateCardBIA += bmsDctRdocMapper.updateCardBIA(updateCardBIAParam);

                        if (Objects.equals(param.getIsresend(), "resend")) {
                            insertRelayHisReqVO.setRetry(reTryCount + 1);
                        } else {
                            insertRelayHisReqVO.setRetry(0);
                        }
                        insertRelayHisReqVO.setDocid(resDocId); // 발송에 따른 사본docid
                        insertRelayHisReqVO.setReceiverid(param.getRecvMultiList().get(recvListIdx).getReceiverid());
                        if (param.getCopyOrder() != null) {
                            //                        insertRelayHisReqVO.setDocorder(parseInt(param.getCopyOrder()) - 1);
                            insertRelayHisReqVO.setDocorder(parseInt(param.getRecvMultiList().get(recvListIdx).getApprrecvseq()));
                        } else {
                            insertRelayHisReqVO.setDocorder(parseInt(param.getRecvMultiList().get(recvListIdx).getApprrecvseq()));
                        }
                        insertRelayHisReqVO.setExtype("R"); //수신
                        insertRelayHisReqVO.setActername(resGetUserLogin.getUsername());
                        insertRelayHisReqVO.setErrormsg("");
                        insertRelayHisReqVO.setActerdeptname(resGetUserLogin.getDeptname());
                        insertRelayHisReqVO.setIndeptid(resGetUserLogin.getDeptid());
                        resInsertRelayHis += bmsDctRdocMapper.insertRelayHis(insertRelayHisReqVO);

                        // 03. UPDATE bms_dct_path
                        updatePathParam = UpdatePathReqVO.builder()
                                .docid(resDocId)
                                .objid(param.getLoginuserid())
                                .state("DCST5") //처리완료
                                .build();
                        resUpdatePath = bmsDctPathMapper.updatePath(updatePathParam);



                        /* 권한을 갖고있는 userid 조회하고, 시행비밀관리카드 저장 */
                        String vgroupid = "";
                        Set<String> processedGubuns = new HashSet<>(); //recvlist 에 처리한(중복되는) gubun을 담기위함
                        List<InsertEnforceRecvReqVO> recv = param.getRecvlist();
                        int resInsertEnforceCard = 0;
                        for (int thisrecvListIdx = recvListIdx; thisrecvListIdx < recv.size(); thisrecvListIdx++) {
                            InsertEnforceRecvReqVO currentRecv = param.getRecvlist().get(recvListIdx);
                            String gubun = (String) currentRecv.getGubun();


                            if (processedGubuns.contains(gubun)) { //결과적으로 고유한 gubun값만 처리하는 로직.
                                continue;
                            }
                            processedGubuns.add(gubun); //처리한 구분에 추가함.

                            switch (gubun) {
                                case "RDR08": //원내 코드
                                    vgroupid = "5220";
                                    break;
                                case "RDR01": // 대내 코드
                                    vgroupid = "5210";
                                    break;
                                case "RDR02": //  외부 코드
                                    vgroupid = "5200"; //관리번호 확인후 수정 요망
                                    break;

                                default:
                                    vgroupid = "5220"; //기본값을 원내코드로
                                    break;
                            }
                            SelectVgroupUserReqVO selectVgroupUserReqVO = SelectVgroupUserReqVO.builder()
                                    .vgroupid(vgroupid)
                                    .build();

                            /* selectReceiptList에서 bms_dct_enf_doc.docstatus 값을 조회 */
                            SelectReceiptListResVO resSelectReceiptList;
                            SelectReceiptListReqVO selectReceiptListParamVO = SelectReceiptListReqVO.builder()
                                    .userid(resGetUserLogin.getUserid())
                                    .build();
                            /* 권한을 갖고있는 userid 조회 */
                            // 05-1. SELECT com_vgroup_user
                            List<SelectVgroupUserResVO> resSelectVgroupUserIds = comVgroupUserMapper.selectVgroupUser(selectVgroupUserReqVO);

                            /* 시행비밀관리카드 저장 */
                            // ***** Operation = insertEnforceCard
                            // to do
                            // 05-2. INSERT bms_dct_enf_doc
                            int enfIdx = 0;
                            if(Objects.equals(param.getJudgeParam(), "distribute")){
                                UpdateEnfDocRepathReqVO updateEnfDocRepathParam = UpdateEnfDocRepathReqVO.builder()
                                        .enfdocid(param.getEnfdocid())
//                                        .inid(tempVO.getUserid())
//                                        .recvdeptid(tempVO.getOrgid())
                                        .inid(param.getRcvsList().get(0).getRcvsuserid())
                                        .recvdeptid(param.getRcvsList().get(0).getRcvdeptid())
                                        .docstatus("EDS09")//접수대기
                                        .moveflag("N")
                                        .docid(resDocId)//사본비밀문서
                                        .enfgubun("N") //대내
                                        .distributeflag("N") //비배부 (배부처리하면 배부 Y)
                                        .emergency(param.getEmergency())
                                        .doctitle(param.getDocttl())
                                        .indt(currentDateTime)
                                        .paperdocflag("Y")
                                        .distributeflag("Y")
                                        .build();
                                resInsertEnforceCard += bmsDctEnfDocMapper.updateEnfDocRepath(updateEnfDocRepathParam);
                                break;
                            }else{
                                for (int vgroupIdx = 0; vgroupIdx < resSelectVgroupUserIds.size(); vgroupIdx++) {
                                    if (param.getRecvMultiList().get(recvListIdx).getReceiverid().equals(resSelectVgroupUserIds.get(vgroupIdx).getOrgid()) || Objects.equals(param.getRecvMultiList().get(recvListIdx).getRelaytype(), "anylo")) {
                                        SelectVgroupUserResVO tempVO = resSelectVgroupUserIds.get(vgroupIdx);
                                        BigInteger bigIntResSysGuid = new BigInteger(resSysGuid);
                                        BigInteger result = bigIntResSysGuid.add(BigInteger.valueOf(enfIdx));
                                        String resSysGuidTemp = "ENF" + result.toString();
                                        if(Objects.equals(param.getIsresend(), "resend")){
                                            resSysGuidTemp = param.getEnfdocid();
                                        }
                                        InsertEnforceCardReqVO insertEnforceCardParam = InsertEnforceCardReqVO.builder()
                                                .enfdocid(resSysGuidTemp)
                                                .inid(tempVO.getUserid()) //배부담당자id com_vgroup_user.userid
                                                .recvdeptid(tempVO.getOrgid()) //발송시 채워둔 수신부서id (접수대기함에서 사용)
                                                //                            .docstatus("EDS01")//수신
                                                //                            .docstatus("EDS02")//배부
                                                //                            .docstatus("EDS05")//접수
                                                .docstatus("EDS09")//접수대기
                                                .moveflag("N")
                                                //                            .docid(param.getDocid())//원본비밀문서
                                                .docid(resDocId)//사본비밀문서
                                                .enfgubun("N") //대내
                                                .distributeflag("N") //비배부 (배부처리하면 배부 Y)
                                                .emergency(param.getEmergency())
                                                .doctitle(param.getDocttl())
                                                .indt(currentDateTime)
                                                .build();
                                        resInsertEnforceCard += bmsDctEnfDocMapper.insertEnforceCard(insertEnforceCardParam);
                                        enfIdx++;
                                    }
                                }
                            }
                        }

                        /* 시행비밀 경로 저장 */
                        // ***** Operation = insertEnforcePath
                        // to do
                        // 06. INSERT bms_dct_enf_path
                        int actseq = bmsDctEnfPathMapper.getActSeq(); //시퀀스 nextval 사용.
                        InsertEnforcePathReqVO insertEnforcePathParam = InsertEnforcePathReqVO.builder()
                                .enfdocid(resEnfDocId)
                                .actseq(BigDecimal.valueOf(actseq))
                                //                    .acterid(param.getUserid())
                                .acterid(resGetUserLogin.getUserid())
                                .actername(resGetUserLogin.getUsername())
                                .deptid(resGetUserLogin.getDeptid())
                                .deptname(resGetUserLogin.getDeptname())
                                .dutyname(resGetUserLogin.getPosition())
                                .indt(currentDateTime)
                                .build();
                        int resInsertEnforcePath = bmsDctEnfPathMapper.insertEnforcePath(insertEnforcePathParam);

                        /* 시행비밀 수신처 저장 */
                        // ***** Operation = insertEnforceRecv
                        // to do
                        // 07. INSERT bms_dct_enf_recv
                        int resInsertEnforceRecv = 0;
                        System.out.println("param.getRecvList() >>> " + param.getRecvlist());
                        InsertEnforceRecvReqVO insertEnforceRecvParam = InsertEnforceRecvReqVO.builder()
                                .recvlist(param.getRecvlist())
                                .build();

                        for (InsertEnforceRecvReqVO tempVO : insertEnforceRecvParam.getRecvlist()) {
                            String tempSysGuid = bmsDctRdocMapper.getSysGuid();
                            String resRecvDocId = "HIS" + tempSysGuid; //sendhisid
                            tempVO.setSendhisid(resRecvDocId);
                            //                tempVO.setDocid(param.getDocid()); //원본비밀문서
                            tempVO.setDocid(resDocId); //사본비밀문서
                            insertEnforceRecvParam = getRecvListVO(tempVO);
                            resInsertEnforceRecv += bmsDctEnfRecvMapper.insertEnforceRecv(insertEnforceRecvParam);
                        }
                        //
                        /* 시행비밀 처리이력 저장 */
                        // ***** Operation = insertEnforceHistory
                        // to do
                        // INSERT bms_dct_enf_history
                        InsertEnforceHistoryReqVO insertEnforceHistoryParam = InsertEnforceHistoryReqVO.builder()
                                .enfdocid(resEnfDocId)
                                .actseq(BigDecimal.valueOf(actseq))
                                .acterid(resGetUserLogin.getUserid())
                                .actername(resGetUserLogin.getUsername())
                                .acterdeptid(resGetUserLogin.getDeptid())
                                .acterdeptname(resGetUserLogin.getDeptname())
                                .acterdutyname(resGetUserLogin.getGrade())
                                .actgubun(param.getActgubun()) //EPA09 업무관리지정 //EPA01 배부
                                .actdt(currentDateTime)
                                .actopinion(param.getSendopinion())
                                .build();
                        int resInsertEnforceHistory = bmsDctEnfHistoryMapper.insertEnforceHistory(insertEnforceHistoryParam);
                        //
                        /* 시행비밀 처리경로 이력 저장 */
                        // ***** Operation = insertEnforcePathHistory
                        // to do
                        String resEnfPathHisId = "PHS" + resSysGuid;

                        InsertEnforcePathHistoryReqVO insertEnforcePathHistoryParam = InsertEnforcePathHistoryReqVO.builder()
                                .enfdocid(resEnfDocId)
                                .pathhisid(resEnfPathHisId)
                                //                    .pathhisseq(BigDecimal.valueOf(parseInt(histSeq)))
                                .pathhisseq(BigDecimal.valueOf(1))
                                .build();
                        int resInsertEnforcePathHistory = bmsDctEnfPathHistoryMapper.insertEnforcePathHistory(insertEnforcePathHistoryParam);

                        /* 비밀수발대장 저장 */
                        // ***** Operation = insertSendRecv
                        // to do
                        String sndrcvseq = "";
                        try {
                            sndrcvseq = bmsDctSndrcvRegiMapper.getSndrcvseq();
                            System.out.println(sndrcvseq);
                        } catch (Exception e) {
                            throw new AnyXException(e);
                        }
                        int resInsertSendRecv = 0;
                        //bms_dct_sndrcv_regi 테이블
                        InsertSendRecvReqVO insertSendRecvParam = InsertSendRecvReqVO.builder()
                                .recvList(param.getRecvMultiList())
                                .build();
                        int i = 0;
                        for (InsertTempListReqVO tempRecvVO : insertSendRecvParam.getRecvList()) {
                            //                tempRecvVO.setDocid(param.getDocid()); //원본비밀문서
                            tempRecvVO.setDocid(resDocId); //사본비밀문서
                            sndrcvseq = bmsDctSndrcvRegiMapper.getSndrcvseq();
                            tempRecvVO.setSndrcvseq(sndrcvseq);
                            tempRecvVO.setSndrcvttl(param.getDocttl());
                            tempRecvVO.setSeclevel(param.getSeclevel());
                            tempRecvVO.setCopyno(Integer.toString(i + 1));
                            tempRecvVO.setSndrcvdt(currentDateTime);
                            //                tempRcvsVO.setRgstid(param.getLoginuserid());
                            tempRecvVO.setRgstid(resGetUserLogin.getUserid());
                            tempRecvVO.setSnddeptid(resGetUserLogin.getDeptid());
                            tempRecvVO.setSnddeptname(resGetUserLogin.getDeptname());
                            tempRecvVO.setSnduserid(resGetUserLogin.getUserid());
                            tempRecvVO.setSndusername(resGetUserLogin.getUsername());
                            //                tempRcvsVO.setRgstname(param.getGetUserLoginData().get(0).getUsername());
                            //                tempRcvsVO.setRgstname(param.getLoginusername());
                            tempRecvVO.setRgstname(resGetUserLogin.getUsername());
                            tempRecvVO.setRgstdt(currentDateTime);
                            insertSendRecvParam = getRecvTempListVO(tempRecvVO);
                            resInsertSendRecv += bmsDctSndrcvRegiMapper.insertSendRecv(insertSendRecvParam);
                            i++;
                        }

                        int qtyValue = 0;
                        if (param.getFileList() != null && param.getFileList().size() > 0 && !param.getFileList().isEmpty()) {
                            qtyValue = param.getFileList().size();
                        } else {
                            qtyValue = 0;
                        }
                        // INSERT bms_dct_invrcpt //송증영수증 등록
                        SelectReceiptResVO insertInvrcptParam = SelectReceiptResVO.builder()
                                .docid(resDocId) //사본DocId
                                .copyno(Integer.toString(recvListIdx + 1) + "/" + param.getRecvMultiList().size())
                                .invrcpttl(param.getDocttl())
                                .seclevel(param.getSeclevel())
                                .mgmtno(param.getMgmtno())
                                .docno(param.getDocno())
                                .recdeptname(param.getRecvMultiList().get(recvListIdx).getApprrecvdeptname()) //수신부서
                                .refdeptname(param.getRefdeptname())
                                .sndminiid(resOrganizationInfo.get(0).getOrgname())//발신자 부처명
                                .sndmininame(resOrganizationInfo.get(0).getOrgid()) //발신자 부처ID
                                .snddeptid(resGetUserLogin.getDeptid())
                                .snddeptname(resGetUserLogin.getDeptname())
                                .sndid(resGetUserLogin.getUserid())
                                .sndname(resGetUserLogin.getUsername())
                                .snddutyname(resGetUserLogin.getPosition() + "/" + resGetUserLogin.getGrade())
                                .snddt(currentDateTime)
                                .sndelectsignid(resGetUserLogin.getUsername()) //전자서명
                                .rcvminiid(param.getRecvMultiList().get(recvListIdx).getReceiverid()) //수신자 부처ID
                                .rcvmininame(param.getRecvMultiList().get(recvListIdx).getApprrecvdeptname()) //수신자 부처명
                                .rcvdeptid(param.getRecvMultiList().get(recvListIdx).getReceiverid())
                                .rcvdeptname(param.getRecvMultiList().get(recvListIdx).getApprrecvdeptname())
                                //                        .rcvid(param.getRcvid())
                                //                        .rcvname(param.getRcvname())
                                //                        .rcvdutyname(param.getRcvdutyname())
                                //                        .rcvdt(param.getRcvdt())
                                //                        .rcvelectsignid(param.getRcvelectsignid())
                                //                        .sndrcvdocid(param.getSndrcvdocid())
                                //                        .failreason(param.getFailreason())
                                .qty(BigDecimal.valueOf(qtyValue)) //붙임파일수량
                                .invrcpttype("NN") //송증영수증 형태 //NN:온라인 NF:오프라인
                                .rgstid(resGetUserLogin.getUserid())
                                .rgstname(resGetUserLogin.getUsername())
//                                .rgstdt(param.getPathList().get(0).getReportdt()) //등록일자
                                .rgstdt(param.getIndt()) //등록일자
                                //                        .updtid(param.getUpdtid())
                                //                        .updtname(param.getUpdtname())
                                //                        .updtdt(param.getUpdtdt())
                                //                        .sndverifyfileid(param.getSndverifyfileid())
                                //                        .rcvverifyfileid(param.getRcvverifyfileid())
                                .receiverid(param.getRecvMultiList().get(recvListIdx).getReceiverid())
                                .build();

                        System.out.println("insertInvrcptParam = " + insertInvrcptParam);

                        if (param.getCopyOrder() != null) {
                            String copyMaxNum = "";
                            String[] parts = param.getCopyno().split("/");
                            if (parts.length > 1) {
                                copyMaxNum = parts[1];
                            } else {
                                copyMaxNum = "(원본에 '/' 문자가 없습니다.)";
                            }
                            insertInvrcptParam.setCopyno((param.getCopyOrder()) + "/" + copyMaxNum);
                        } else {
                            insertInvrcptParam.setCopyno(Integer.toString(recvListIdx + 1) + "/" + param.getRecvMultiList().size());
                            if(Objects.equals(param.getJudgeParam(), "distribute")){
                                insertInvrcptParam.setCopyno(param.getCopyno());
                                insertInvrcptParam.setInvrcpttype("NF");
                            }
                        }
                        int resInsertReceipt = bmsDctInvrcptMapper.insertReceipt(insertInvrcptParam);
                    }
                    if(Objects.equals(param.getJudgeParam(), "distribute")){
                        break; //수신처 수(recvList) 반복 탈출
                    }
                }/* 수신처 수(recvList) 만큼 반복  end */
            }
        }
//        catch (FailException fe){
//            throw fe;
//        }
        catch(Exception e){
            throw new AnyXException(e);
        }

//        return resUpdateCardBIA;
        return resUpdateCardBIA;
    }

//    static class FailException extends Exception {
//        public FailException(String message) {
//            super(message);
//        }
//    }

    private InsertRelayHisReqVO getInsertRelayHisVO (UpdateCardBIAReqVO vo, int recvListIdx, String exstatus, String currentDateTime){
        InsertRelayHisReqVO build = InsertRelayHisReqVO.builder()
//                .docid(vo.getDocid()) //원본
//                .receiverid(vo.getReceiverid()) // 수신처 ID는 대외발송 기능 추가시 수정예정
                .docorder(parseInt(vo.getRecvMultiList().get(recvListIdx).getApprrecvseq()))
                .deptid(vo.getDeptid())
                .deptname(vo.getRecvMultiList().get(recvListIdx).getApprrecvdeptname())
                .exstatus(exstatus)
                .indt(currentDateTime)
                .title(vo.getDocttl())
                .retry(0)
                .errormsg("")
                .systemtype("0: 비밀관리시스템")
                .actdt(currentDateTime)
                .build();
        return build;
    }

    private InsertEnforceRecvReqVO getRecvListVO (InsertEnforceRecvReqVO vo){
        InsertEnforceRecvReqVO build = InsertEnforceRecvReqVO.builder()
                .docid(vo.getDocid())
                .sendhisid(vo.getSendhisid())
//                .recvdeptname(vo.getApprrecvrefdeptname())
                .recvdeptname(vo.getDeptname())
                .build();
        return build;
    }
    private InsertTempListReqVO getRcvsListVO (InsertTempListReqVO vo){
        InsertTempListReqVO build = InsertTempListReqVO.builder()
                .docid(vo.getDocid())
                .sndrcvseq(vo.getSndrcvseq())
                .orgid(vo.getRcvdeptid())
                .orgname(vo.getRcvdeptname())
                .sndrcvgubun("1")
                .sndrcvttl(vo.getSndrcvttl())
                .seclevel(vo.getSeclevel())
                .copyno(vo.getCopyno())
                .sndrcvdt(vo.getSndrcvdt()) //수발신일시
                .rgstid(vo.getRgstid())
                .rgstname(vo.getRgstname())
                .rgstdt(vo.getRgstdt())
//                .rcvdutyname(vo.getRcvdutyname())
//                .rcvsdeptnamedesc(vo.getRcvsdeptnamedesc())
//                .rcvsinseq(vo.getRcvsinseq())
//                .rcvsuserid(vo.getRcvsuserid())
//                .rcvsusername(vo.getRcvsusername())
                .build();
        return build;
    }

    private InsertSendRecvReqVO getRecvTempListVO(InsertTempListReqVO vo) {
        InsertSendRecvReqVO build = InsertSendRecvReqVO.builder()
                .docid(vo.getDocid())
                .sndrcvseq(vo.getSndrcvseq())
                .orgid(vo.getReceiverid())
                .orgname(vo.getApprrecvdeptname()) //기관이름
                .sndrcvgubun("1")
                .sndrcvttl(vo.getSndrcvttl())
                .seclevel(vo.getSeclevel())
                .copyno(vo.getCopyno())
                .docnodeptname(vo.getDocnodeptname())
                .docnoseq(vo.getDocnoseq())
                .snduserid(vo.getSnduserid())
                .snddeptid(vo.getSnddeptid())
                .sndusername(vo.getSndusername())
                .snddeptname(vo.getSnddeptname())
                .mgmtno(vo.getMgmtno())
                .docnodeptid(vo.getDocnodeptid())
                .sndrcvdt(vo.getSndrcvdt()) // 수발신일시
                .rcvdeptid(vo.getReceiverid()) //수신자부서ID
                .rcvdeptname((vo.getApprrecvdeptname() +"/"+ vo.getApprrecvrefdeptname())) //수신자 부서명 (혁신전략실/전산팀)
                .rcvuserid(vo.getRcvuserid())
                .rcvusername(vo.getRcvusername())
                .rgstname(vo.getRgstname())
                .rgstdt(vo.getRgstdt())
                .rgstid(vo.getRgstid())
                .updtname(vo.getUpdtname())
                .updtdt(vo.getUpdtdt())
                .build();
        return build;
    }

    private InsertSendRecvReqVO getRecvList (InsertSendRecvReqVO vo){
        InsertSendRecvReqVO build = InsertSendRecvReqVO.builder()
                .docid(vo.getDocid())
//                .sndrcvseq(vo.getSndrcvseq())
//                .orgid(vo.getRcvdeptid())
//                .orgname(vo.getRcvdeptname())
//                .sndrcvgubun("1")
//                .sndrcvttl(vo.getSndrcvttl())
//                .seclevel(vo.getSeclevel())
//                .copyno(vo.getCopyno())
//                .sndrcvdt(vo.getSndrcvdt()) //수발신일시
//                .rgstid(vo.getRgstid())
//                .rgstname(vo.getRgstname())
//                .rgstdt(vo.getRgstdt())
                .build();
        return build;
    }

    private InsertTempListReqVO convertUpdateToInsertParam(UpdateCardBIAReqVO updateParam) {
        InsertTempListReqVO insertParam = new InsertTempListReqVO();

        insertParam.setDocid(updateParam.getDocid());
        insertParam.setDocttl(updateParam.getDocttl());
        insertParam.setDocno(updateParam.getDocno());
        insertParam.setMgmtno(updateParam.getMgmtno());
        insertParam.setGubun(updateParam.getGubun());
        insertParam.setKeyword(updateParam.getKeyword());
        insertParam.setAuthorid(updateParam.getAuthorid());
        insertParam.setAuthorname(updateParam.getAuthorname());
        insertParam.setAuthordutyname(updateParam.getAuthordutyname());
        insertParam.setAuthorpositionid(updateParam.getAuthorpositionid());
        insertParam.setAuthorgradeid(updateParam.getAuthorgradeid());
        insertParam.setAuthordeptid(updateParam.getAuthordeptid());
        insertParam.setAuthordeptname(updateParam.getAuthordeptname());
        insertParam.setAuthordeptnamedesc(updateParam.getAuthordeptnamedesc());
        insertParam.setLastauthorid(updateParam.getLastauthorid());
        insertParam.setLastauthorname(updateParam.getLastauthorname());
        insertParam.setState(updateParam.getState());
        insertParam.setDocnoseq(updateParam.getDocnoseq() != null ? updateParam.getDocnoseq().toString() : null);
        insertParam.setOpen(updateParam.getOpen());
        insertParam.setOpenbasis(updateParam.getOpenbasis());
        insertParam.setOpenreason(updateParam.getOpenreason());
        insertParam.setReadrangetype(updateParam.getReadrangetype());
        insertParam.setReadlimittype(updateParam.getReadlimittype());
        insertParam.setReadlimitdate(updateParam.getReadlimitdate());
        insertParam.setDelflag(updateParam.getDelflag());
        insertParam.setTempflag(updateParam.getTempflag());
        insertParam.setInid(updateParam.getInid());
        insertParam.setIndt(updateParam.getIndt());
        insertParam.setReportdt(updateParam.getReportdt());
        insertParam.setEnddt(updateParam.getEnddt());
        insertParam.setUpid(updateParam.getUpid());
        insertParam.setUpdt(updateParam.getUpdt());
        insertParam.setDocfrom(updateParam.getDocfrom());
        insertParam.setMoveflag(updateParam.getMoveflag());
        insertParam.setRcvsflag(updateParam.getRcvsflag());
        insertParam.setTransflag(updateParam.getTransflag());
        insertParam.setTypeflag(updateParam.getTypeflag());
        insertParam.setProtectflag(updateParam.getProtectflag());
        insertParam.setSetupflag(updateParam.getSetupflag());
        insertParam.setOridocid(updateParam.getOridocid());
        insertParam.setOriseq(updateParam.getOriseq() != null ? updateParam.getOriseq().toString() : null);
        insertParam.setBatchseq(updateParam.getBatchseq() != null ? updateParam.getBatchseq().toString() : null);
        insertParam.setPaperflag(updateParam.getPaperflag());
        insertParam.setMaindocflag(updateParam.getMaindocflag());
        insertParam.setAttachseq(updateParam.getAttachseq() != null ? updateParam.getAttachseq().toString() : null);
        insertParam.setEnfgubun(updateParam.getEnfgubun());
        insertParam.setEmergency(updateParam.getEmergency());
        insertParam.setDrmgubun(updateParam.getDrmgubun());
        insertParam.setPaperrecgubun(updateParam.getPaperrecgubun());
        insertParam.setPaperrecspecial(updateParam.getPaperrecspecial());
        insertParam.setPaperrectypes(updateParam.getPaperrectypes());
        insertParam.setMigflag(updateParam.getMigflag());
        insertParam.setAddsendstate(updateParam.getAddsendstate());
        insertParam.setAddsendcnt(updateParam.getAddsendcnt() != null ? updateParam.getAddsendcnt().toString() : null);
        insertParam.setDocgroup(updateParam.getDocgroup());
        insertParam.setFaketitle(updateParam.getFaketitle());
        insertParam.setPapersendflag(updateParam.getPapersendflag());
        insertParam.setFilelendflag(updateParam.getFilelendflag());
        insertParam.setSeclevel(updateParam.getSeclevel());
        insertParam.setDocgubun(updateParam.getDocgubun());
        insertParam.setSummary(updateParam.getSummary());
//        insertParam.setLoginuserid(updateParam.getLoginuserid());
        insertParam.setResuserid(updateParam.getLoginuserid());
//        insertParam.setLoginusername(updateParam.getLoginusername());
        insertParam.setSecgubun(updateParam.getSecgubun());
        insertParam.setPrsrvterm(updateParam.getPrsrvterm());
        insertParam.setOrgnprotdt(updateParam.getOrgnprotdt());
        insertParam.setOrgnnextlevel(updateParam.getOrgnnextlevel());
        insertParam.setOrgnreclass(updateParam.getOrgnreclass());
        insertParam.setCopyprotdt(updateParam.getCopyprotdt());
        insertParam.setCopyreclass(updateParam.getCopyreclass());
        insertParam.setCopynextlevel(updateParam.getCopynextlevel());

        insertParam.setPathList(updateParam.getPathList());
        insertParam.setPathorder(updateParam.getPathorder());
        insertParam.setPathstate(updateParam.getPathstate());
        insertParam.setAsktype(updateParam.getAsktype());
        insertParam.setActtype(updateParam.getActtype());
        insertParam.setObjid(updateParam.getObjid());
        insertParam.setObjname(updateParam.getObjname());
        insertParam.setDeptid(updateParam.getDeptid());
        insertParam.setDutyname(updateParam.getDutyname());
        insertParam.setDutynamedesc(updateParam.getDutynamedesc());
        insertParam.setMainver(updateParam.getMainver());
        insertParam.setActapprdt(updateParam.getActapprdt());
        insertParam.setElectsignid(updateParam.getElectsignid());
        insertParam.setOpinionpathorder(updateParam.getOpinionpathorder());
        insertParam.setOpinion(updateParam.getOpinion());

//        insertParam.setRcvsList(updateParam.getRcvsList());
        insertParam.setRefList(updateParam.getRefList()); //관련근거파일 리스트
        insertParam.setFileList(updateParam.getFileList()); //붙임파일 리스트
        insertParam.setDraftfile(updateParam.getDraftfile()); //본문파일
        insertParam.setReceiverid(updateParam.getReceiverid());
        insertParam.setApprrecvtempflag(updateParam.getApprrecvtempflag());
        insertParam.setApprrecvseq(updateParam.getApprrecvseq());
        insertParam.setApprrecvgubun(updateParam.getApprrecvgubun());
        insertParam.setApprrecvdeptname(updateParam.getApprrecvdeptname());
        insertParam.setApprrecvrefdeptname(updateParam.getApprrecvrefdeptname());
        insertParam.setRcvsuserid(updateParam.getRcvsuserid());
        insertParam.setRcvsusername(updateParam.getRcvsusername());
        insertParam.setRcvdeptid(updateParam.getRcvdeptid());
        insertParam.setRcvdeptname(updateParam.getRcvdeptname());
        insertParam.setRcvdutyname(updateParam.getRcvdutyname());
        insertParam.setRcvsinseq(updateParam.getRcvsinseq());
        insertParam.setInfomationid(updateParam.getInfomationid());
        insertParam.setResultid(updateParam.getResultid());
        insertParam.setInfotext(updateParam.getInfotext());
        insertParam.setAttachid(updateParam.getAttachid());
        insertParam.setInfotype(updateParam.getInfotype());
        insertParam.setDeleteyn(updateParam.getDeleteyn());
        insertParam.setInfotitle(updateParam.getInfotitle());
        insertParam.setFileexist(updateParam.getFileexist());
        insertParam.setDelRef(updateParam.getDelRef());
        insertParam.setHistdt(updateParam.getHistdt());
        insertParam.setHistseq(updateParam.getHistseq());
        insertParam.setApprreqid(updateParam.getApprreqid());
        insertParam.setHistgubun(updateParam.getHistgubun());
        insertParam.setUserid(updateParam.getUserid());
        insertParam.setHiststatus(updateParam.getHiststatus());
        insertParam.setEtc(updateParam.getEtc());
        insertParam.setFilegubun(updateParam.getFilegubun());
        insertParam.setFleid(updateParam.getFleid());
        insertParam.setSfilename(updateParam.getSfilename());
        insertParam.setFlettl(updateParam.getFlettl());
        insertParam.setFlesize(updateParam.getFlesize());
        insertParam.setFleorder(updateParam.getFleorder());
        insertParam.setMarkflag(updateParam.getMarkflag());
        insertParam.setOrigubun(updateParam.getOrigubun());
        insertParam.setIsviewtype(updateParam.getIsviewtype());
        insertParam.setFleurl(updateParam.getFleurl());
        insertParam.setFlepath(updateParam.getFlepath());
        insertParam.setDelFileids(updateParam.getDelFileids());
//        insertParam.setVgroupid(updateParam.getVgroupid());
//        insertParam.setOrgid(updateParam.getOrgid());
//        insertParam.setVgrouptype(updateParam.getVgrouptype());
//        insertParam.setActgubun(updateParam.getActgubun());
        insertParam.setDistdeptname(updateParam.getDistdeptname());
        insertParam.setDistname(updateParam.getDistname());

        return insertParam;
    }

    public InsertTempListReqVO convertSendRecvToTempList(InsertSendRecvReqVO rcvs) {
        InsertTempListReqVO tempList = new InsertTempListReqVO();
        tempList.setRcvsuserid(rcvs.getRcvsuserid());// userid ( 사용자ID )
        tempList.setRcvsusername(rcvs.getRcvsusername());// username ( 사용자이름 = 열람권자 )
        if(rcvs.getRcvsdeptid() != null){
            tempList.setRcvdeptid(rcvs.getRcvsdeptid());// deptid ( 부서ID )
        }else{
            tempList.setRcvdeptid(rcvs.getRcvdeptid());// deptid ( 부서ID )
        }
        tempList.setRcvdeptname(rcvs.getRcvdeptname());// deptname ( 부서이름 )
        tempList.setRcvdutyname(rcvs.getRcvdutyname()); // dutyname ( 직위직급이름 )
        tempList.setRcvsinseq(rcvs.getRcvsinseq());// inseq ( 등록일련번호 )
        return tempList;
    }


    /* 발송 보류 처리*/
    @Override
    @Transactional
    public int updateCardHoldBIA(UpdateCardBIAReqVO param){
        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

        /* 00. 로그인한 유저 정보 조회 */
        GetUserLoginResVO resGetUserLogin;
        GetUserLoginReqVO getUserLoginReqVO = GetUserLoginReqVO.builder()
                .userid(param.getUserid())
                .build();
        resGetUserLogin = comUserinfoMapper.getUserLogin(getUserLoginReqVO.builder()
//                    .userid(param.getLoginuserid())
                .userid(getUserLoginReqVO.getUserid())
                .build());

        int resUpdateCardHoldBIA = 0;
        try {
            UpdateCardBIAReqVO updateCardBIAParam = UpdateCardBIAReqVO.builder()
                    .docid(param.getDocid())
                    .state("DRDS0") //발송보류
                    .updt(formatter.format(today))
                    .build();
            resUpdateCardHoldBIA = bmsDctRdocMapper.updateCardBIA(updateCardBIAParam);

            /* 발송 의견 저장 */
            InsertDocSendInfoReqVO insertDctDocSendinfoParam = InsertDocSendInfoReqVO.builder()
                    .docid(param.getDocid())
                    .sendopinion(param.getSendopinion())
                    .docgubun("A") //발송 //tempfplag 의 값을 Y로 두어 구분함.
                    .tempflag("Y") //임시. // N이면 발송 Y면 발송보류상태.
                    .build();
            int resInsertDctDocSendinfo = bmsDctDocSendinfoMapper.insertDctDocSendinfo(insertDctDocSendinfoParam);

            // 09. INSERT BMS_DCT_HIST_CARD TABLE (이력카드 저장, 이력 카드)
            int resInsertHistory = 0;
//            String histSeq = bmsDctHistCardMapper.getHistSeq();
            String histSeq = "0";
            InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resGetUserLogin, histSeq, formatter.format(today));
            histSeq = bmsDctHistCardMapper.getHistSeq();
            insertHistoryReqVO.setHistgubun("34"); // 34: 발송보류,발송안함
            insertHistoryReqVO.setHiststatus("013"); // 013: 보류 (발송보류,발송안함)
            insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
            resInsertHistory = bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateCardHoldBIA;
    }

    private InsertHistoryReqVO getHistoryVO(UpdateCardBIAReqVO vo, GetUserLoginResVO userInfo,String histSeq, String indt) {
        InsertHistoryReqVO build = InsertHistoryReqVO.builder()
                .docid(vo.getDocid())
                .histdt(indt)
                .histseq(new BigDecimal(histSeq))
                .apprreqid(vo.getApprreqid())
                .histgubun(vo.getHistgubun())
                .deptid(userInfo.getDeptid())
                .deptname(userInfo.getDeptname())
//                .dutyname(userInfo.getDutyname())
                .dutyname(userInfo.getGrade())
                .userid(userInfo.getUserid())
                .username(userInfo.getUsername())
                .histstatus(vo.getHiststatus())
                .etc(vo.getEtc())
                .electsignid(userInfo.getUsername())
                .build();
        return build;
    }


    /* 발송 > 발송현황함 > 발송현황함 목록 조회 */
    @Override
    public PageInfo<SelectSendCurrentListResVO> selectSendCurrentList(SelectSendCurrentListReqVO param){
        PageInfo<SelectSendCurrentListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(!Objects.equals(param.getStartDt(), "")){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(!Objects.equals(param.getEndDt(), "")){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctRdocMapper.selectSendCurrentList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resultListVO;
    }


    /* 비밀배포카드 팝업 발송정보 조회 */
    @Override
    public PageInfo<SelectDistributeListResVO> selectDistributeList(SelectDistributeListReqVO param){
        PageInfo<SelectDistributeListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(bmsDctRdocMapper.selectDistributeList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 반송 상세 팝업 조회 */
    @Override
    public GetReturnDetailCardResVO getReturnDetailCard(GetReturnDetailCardReqVO param){
        GetReturnDetailCardResVO resGetReturnDetailCard;
        try {
            resGetReturnDetailCard = bmsDctRdocMapper.getReturnDetailCard(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetReturnDetailCard == null){
            resGetReturnDetailCard = new GetReturnDetailCardResVO();
        }
        return resGetReturnDetailCard;
    }

    /* 키워드 검색 팝업 조회 */
    @Override
    public PageInfo<SelectRdocForKeywordSearchResVO> selectRdocForKeywordSearch(SelectRdocForKeywordSearchReqVO param){
        PageInfo<SelectRdocForKeywordSearchResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(bmsDctRdocMapper.selectRdocForKeywordSearch(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 비밀열람함 목록 조회 */
    @Override
    public PageInfo<SelectdctRdocResVO> selectdctRdoc(SelectdctRdocReqVO param){
        PageInfo<SelectdctRdocResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            if(param.getStartDt() != null){
                String startDt = param.getStartDt().replaceAll("-","")+ "000000";
                param.setStartDt(startDt);
            }
            if(param.getEndDt() != null){
                String endDt = param.getEndDt().replaceAll("-","")+ "235959";
                param.setEndDt(endDt);
            }
            resultListVO = PageInfo.of(bmsDctRdocMapper.selectdctRdoc(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* 접수 상세화면 문서취지 조회 */
    @Override
    public SelectTempCreateCardListResVO getDctSummary(InsertTempListReqVO param){
        SelectTempCreateCardListResVO resGetDctSummary;
        try {
            resGetDctSummary = bmsDctRdocMapper.getDctSummary(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resGetDctSummary == null){
            resGetDctSummary = new SelectTempCreateCardListResVO();
        }
        return resGetDctSummary;
    }


    /* 접수 상세 화면 비밀문서 정보 조회 */
    @Override
    public SelectTempCreateCardListResVO getTempCreateCardList(SelectTempCreateCardListReqVO param){
        System.out.println("BmsDctRdocServiceImpl.java BmsDctRdoc selectTempCreateCardList START");

        String paramDocId = "";

        paramDocId = param.getDocid();

        SelectTempCreateCardListResVO resultListVO = new SelectTempCreateCardListResVO();
        try {
            /* 00. 로그인한 유저 정보 조회 */
            GetUserLoginResVO resGetUserLogin;
            GetUserLoginReqVO getUserLoginReqVO = GetUserLoginReqVO.builder()
                    .userid(param.getLoginuserid())
                    .build();
            //getUserLogin(param)부분은 로그인 api가 변동시 수정예정.2023-10-27 기준 현재 로직은 이렇게.
            resGetUserLogin = comUserinfoMapper.getUserLogin(getUserLoginReqVO.builder()
//                    .userid(param.getLoginuserid())
                    .userid(getUserLoginReqVO.getUserid())
                    .build());
//            PageHelper.startPage(param);
            // GET_01 // 01. BMS_DCT_RDOC TABLE
//            /* 비밀관리카드 기본정보 조회 */
//            getDctRdoc <bms_dct_rdoc> ^s : iv(docid)


            GetDctRdocReqVO getDctRdocReqVO = new GetDctRdocReqVO();
            getDctRdocReqVO.setDocid(paramDocId);

            GetDctRdocResVO getDctRdocResVO = new GetDctRdocResVO();
            getDctRdocResVO = getDctRdoc(getDctRdocReqVO);

            System.out.println("BmsDctRdocServiceImpl.java BmsDctRdoc selectTempCreateCardList START");
            System.out.println("BmsDctRdocServiceImpl.java BmsDctRdoc selectTempCreateCardList getDctRdocResVO.getDocid() = [" + getDctRdocResVO.getDocid() + "]");
            System.out.println("BmsDctRdocServiceImpl.java BmsDctRdoc selectTempCreateCardList getDctRdocResVO.getDocttl() = [" + getDctRdocResVO.getDocttl() + "]");
            System.out.println("BmsDctRdocServiceImpl.java BmsDctRdoc selectTempCreateCardList getDctRdocResVO.getKeyword() = [" + getDctRdocResVO.getKeyword() + "]");
            System.out.println("BmsDctRdocServiceImpl.java BmsDctRdoc selectTempCreateCardList getDctRdocResVO.getEmergency() = [" + getDctRdocResVO.getEmergency() + "]");

            resultListVO.setDocid(getDctRdocResVO.getDocid());
            resultListVO.setDocttl(getDctRdocResVO.getDocttl());
            resultListVO.setKeyword(getDctRdocResVO.getKeyword());
            resultListVO.setEmergency(getDctRdocResVO.getEmergency());

            // GET_02 // 02. BMS_DCT_SUMMARY TABLE
            /* 요약테이블 조회 */
            // 2023.09.13. Lee.h.k. start 요약테이블 (문서취지) 조회를 위한 코드 start
            InsertTempListReqVO getDctSummaryReqVO = new InsertTempListReqVO();
            getDctSummaryReqVO.setDocid(paramDocId);

            SelectTempCreateCardListResVO getDctSummaryResVO = new SelectTempCreateCardListResVO();
            getDctSummaryResVO = getDctSummary(getDctSummaryReqVO);
            System.out.println("getDctSummaryResVO >>>>>>>>>>>>>> = " + getDctSummaryResVO);
            resultListVO.setDocgubun(getDctSummaryResVO.getDocgubun());
            resultListVO.setSummary(getDctSummaryResVO.getSummary());
            // 2023.09.13. Lee.h.k. end 요약테이블 (문서취지) 조회를 위한 코드 end


            // GET_03 // 03. BMS_DCT_ATTR TABLE
            /* 비밀속성 조회 */
//            ; getDctAttr <bms_dct_attr> ^s : iv(docid)
            // 2023.09.13. Lee.h.k. start 비밀속성 조회를 위한 코드 start
            GetDctAttrReqVO getDctAttrReqVO = new GetDctAttrReqVO();
            getDctAttrReqVO.setDocid(paramDocId);

            GetDctAttrResVO getDctAttrResVO = new GetDctAttrResVO();
            getDctAttrResVO = bmsDctAttrRecvService.getDctAttr(getDctAttrReqVO);
            System.out.println("getDctAttrResVO >>>>>>>>>>>>>> = " + getDctAttrResVO);

            String docId = getDctAttrResVO.getDocid();
//            System.out.println("docid >>>>>>>>>> : " + docId);
//            resultListVO.setAuthorid(docId);
            String secgubun = getDctAttrResVO.getSecgubun();
            String seclevel = getDctAttrResVO.getSeclevel();
            String prsrvterm = getDctAttrResVO.getPrsrvterm();
            String orgnprotdt = getDctAttrResVO.getOrgnprotdt();
            String orgnnextlevel = getDctAttrResVO.getOrgnnextlevel();
            String orgnreclass = getDctAttrResVO.getOrgnreclass();
            String copyprotdt = getDctAttrResVO.getCopyprotdt();
            String copynextlevel = getDctAttrResVO.getCopynextlevel();
            String copyreclass = getDctAttrResVO.getCopyreclass();
            String mgmtno = getDctAttrResVO.getMgmtno();
            String copyno = getDctAttrResVO.getCopyno();

            resultListVO.setSecgubun(secgubun);
            resultListVO.setSeclevel(seclevel);
            resultListVO.setPrsrvterm(prsrvterm);
            resultListVO.setOrgnprotdt(orgnprotdt);
            resultListVO.setOrgnnextlevel(orgnnextlevel);
            resultListVO.setOrgnreclass(orgnreclass);
            resultListVO.setCopyprotdt(copyprotdt);
            resultListVO.setCopynextlevel(copynextlevel);
            resultListVO.setCopyreclass(copyreclass);
            resultListVO.setMgmtno(mgmtno);
            resultListVO.setCopyno(copyno);
            resultListVO.setDocno(getDctAttrResVO.getDocno());
            // 2023.09.13. Lee.h.k. end 비밀속성 조회를 위한 코드 end


            // GET_04 // 04. BMS_DCT_PATH TABLE
//            /* 결재경로 조회 */
//            ; selectDctPath <bms_dct_path> ^S : iv(docid)
            // 2023.09.12. Lee.h.k. start 결재경로 조회를 위한 코드 start
            SelectDctPathReqVO selectDctPathReqVO = new SelectDctPathReqVO();
            selectDctPathReqVO.setDocid(paramDocId);

            SelectDctPathResVO selectDctPathResVO = new SelectDctPathResVO();
            List<SelectDctPathResVO> selectDctPathResVOS = bmsDctPathService.selectDctPath(selectDctPathReqVO);
            System.out.println("selectDctPathResVOS >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> = " + selectDctPathResVOS);
            // [SelectDctPathResVO(docid=DCT20230912112703, pathorder=100.0, state=DCST1, objid=null, objtype=null,
            // objname=null, deptid=null, deptname=경영관리팀, dutyname=주사, dutynamedesc=null, ... ... )]
            resultListVO.setPathlist(selectDctPathResVOS);
//            List<String> pathInfo = new ArrayList<>();
            for (SelectDctPathResVO item : selectDctPathResVOS) {
                docId = item.getDocid();
//                System.out.println("docid >>>>>>>>>> : " + docId);
//                resultListVO.setAuthorid(docId);
                String state = item.getState(); // 구분
                String deptname = item.getDeptname(); // 부서
                String dutyname = item.getDutyname(); // 직위/성명
                String electsignid = item.getElectsignid(); // 서명
                String reportdt = item.getReportdt(); // 처리결과
                String asktype = item.getAsktype(); // 요청유형
                String opinion = item.getOpinion(); // 요청의견

                resultListVO.setState(state);
                resultListVO.setDeptname(deptname);
                resultListVO.setDutyname(dutyname);
                resultListVO.setElectsignid(electsignid);
                resultListVO.setReportdt(reportdt);
                resultListVO.setAsktype(asktype);
                resultListVO.setOpinion(opinion);
            }
//            resultListVO.setPathInfo(pathInfo);
            // 2023.09.12. Lee.h.k. end 결재경로 조회를 위한 코드 end

            // GET_05 // 05. BMS_DCT_OPINION TABLE
            // 2023.09.13. Lee.h.k. start 의견 조회를 위한 코드 start
//            InsertTempListReqVO getDctOpinionReqVO = new InsertTempListReqVO();
//            getDctOpinionReqVO.setDocid(paramDocId);
//
//            SelectTempCreateCardListResVO getDctOpinionResVO = new SelectTempCreateCardListResVO();
//            getDctOpinionResVO = bmsDctOpinionService.getDctOpinion(getDctOpinionReqVO);
//            System.out.println("getDctOpinionResVO >>>>>>>>>>>>>> = " + getDctOpinionResVO);
//            resultListVO.setOpinion(getDctOpinionResVO.getOpinion());
            // 2023.09.13. Lee.h.k. end 의견 조회를 위한 코드 end

            // GET_06 // 06. BMS_DCT_DOC_SENDINFO
            // 2023.09.13. Lee.h.k. start 발신기관 조회를 위한 코드 start
            SelectSendinfoReqVO selectSendinfoReqVO = new SelectSendinfoReqVO();
            selectSendinfoReqVO.setDocid(paramDocId);

            SelectSendinfoResVO selectSendinfoResVO = new SelectSendinfoResVO();
            List<SelectSendinfoResVO> selectSendinfoResVOS = bmsDctDocSendinfoService.selectSendinfo(selectSendinfoReqVO);
            System.out.println("selectSendinfoResVOS >>>>>>>>>>>>>>>>> = " + selectSendinfoResVOS);
            resultListVO.setSendinfolist(selectSendinfoResVOS);
            // 2023.09.13. Lee.h.k. end 발신기관 조회를 위한 코드 end

            // GET_07 // 07. BMS_DCT_APPR_RECV
//            /* 수신처 조회 */
//            ; getDctRecv <bms_dct_appr_recv> ^S : iv(docid)
            // 2023.09.13. Lee.h.k. start 수신처 조회를 위한 코드 Start
            System.out.println("getDctRecv start--------------");
            GetDctRecvReqVO getDctRecvReqVO = new GetDctRecvReqVO();
            getDctRecvReqVO.setDocid(paramDocId);

            GetDctRecvResVO getDctRecvResVO = new GetDctRecvResVO();
            List<GetDctRecvResVO> getDctRecvResVOS = bmsDctApprRecvService.getDctRecv(getDctRecvReqVO);
            System.out.println("getDctRecvResVOS >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> = " + getDctRecvResVOS);

            resultListVO.setRecvlist(getDctRecvResVOS);
            // 2023.09.13. Lee.h.k. start 수신처 조회를 위한 코드 End


            // GET_08 // 08. BMS_DCT_RDOC_RCVS
            /* 열람자 조회 */
//            ; selectDctReader <bms_dct_rdoc_rcvs> ^s : iv(docid)
            // 2023.09.13. Lee.h.k. start 열람자 조회를 위한 코드 start
            SelectDctReaderReqVO selectDctReaderReqVO = new SelectDctReaderReqVO();
            selectDctReaderReqVO.setDocid(paramDocId);

            List<SelectDctReaderResVO> selectDctReaderResVO = bmsDctRdocRcvsService.selectDctReader(selectDctReaderReqVO);
            System.out.println("selectDctReaderResVO >>>>>>>>>>>>>>>>>>>>> = " + selectDctReaderResVO);

            resultListVO.setRcvslist(selectDctReaderResVO);
            // 2023.09.13. Lee.h.k. end 열람자 조회를 위한 코드 end


            // GET_09
//            /* 파일조회 */
//            ; selectDctFile <bms_dct_file> ^s : iv(docid)

            // GET_10 // 10. BMS_TASK_INFOMATION_TEMP
//            SelectTempCreateCardListReqVO selectTempCreateCardListReqVO = new SelectTempCreateCardListReqVO();
//            selectTempCreateCardListReqVO.setDocid(paramDocId);
//            selectTempCreateCardListReqVO.setResultid(paramDocId);
//
//            SelectTempCreateCardListResVO selectTempCreateCardListResVO = new SelectTempCreateCardListResVO();
//            selectTempCreateCardListResVO = bmsDctaaaaService.selectTaskInfoTemp(selectTempCreateCardListReqVO);
//            System.out.println("selectTempCreateCardListResVO >>>>>>>>>>>>>>>>>>>>> = " + selectTempCreateCardListResVO);
//
//            resultListVO.setResultid(selectTempCreateCardListResVO.getResultid());

            // 10. GET bms_dct_enf_doc
            /* bms_dct_enf_doc.chargerid 조회목적 (지정된 담당자인지 확인하기 위함) */
            List<SelectReceiptListResVO> resSelectReceiptList;
            SelectReceiptListReqVO selectReceiptListParamVO = SelectReceiptListReqVO.builder()
                    .userid(resGetUserLogin.getUserid())
                    .vgroupid(resGetUserLogin.getVgroupid())
                    .deptid(resGetUserLogin.getDeptid())
                    .build();
//            /* 결재자의 부서를 필터링 */
            List<SelectDctPathResVO> filteredList = new ArrayList<>();

            for (SelectDctPathResVO vo : selectDctPathResVOS) {
                if ("DEA41".equals(vo.getAsktype())) {
                    filteredList.add(vo);
                }
            }
            if(!filteredList.isEmpty()) {
                System.out.println("filteredList >>> : " + filteredList);
                System.out.println("getDeptid >>> : " + filteredList.get(0).getDeptid());
            }
            resSelectReceiptList = bmsDctEnfDocMapper.getReceiptList(selectReceiptListParamVO.builder()
                    .userid(selectReceiptListParamVO.getUserid())
                    .vgroupid(selectReceiptListParamVO.getVgroupid())
                    .deptid(selectReceiptListParamVO.getDeptid())
                    .enfdocid(param.getEnfdocid())
                    .build());
            System.out.println("resSelectReceiptList >>>>>>>>> : " + resSelectReceiptList);
            if (!resSelectReceiptList.isEmpty()) {
                resultListVO.setVgroupid(resSelectReceiptList.get(0).getVgroupid());
                resultListVO.setOrgid(resSelectReceiptList.get(0).getOrgid());
                resultListVO.setChargerid(resSelectReceiptList.get(0).getChargerid());
                resultListVO.setAcceptdeptname(resSelectReceiptList.get(0).getAcceptdeptname());
                resultListVO.setAcceptdocseq(resSelectReceiptList.get(0).getAcceptdocseq());
            }else{
                SelectRecvHistoryReqVO getRecvHistoryReqVO = SelectRecvHistoryReqVO.builder()
                        .enfdocid(param.getEnfdocid())
                        .build();
                List<SelectRecvHistoryResVO> getReceiptdetail = bmsDctEnfDocMapper.selectRecvHistory(getRecvHistoryReqVO);
                resultListVO.setChargerid(getReceiptdetail.get(0).getChargerid());
            }

            /* 11. Select bms_dct_enf_path (chargerList) 담당정보의 담당자 조회 */
            SelectDctEnfPathReqVO selectDctEnfPathReqVO = new SelectDctEnfPathReqVO();
            selectDctEnfPathReqVO.setEnfdocid(param.getEnfdocid());
            List<SelectDctEnfPathResVO> selectDctEnfPathResVOS = bmsDctEnfPathMapper.selectDctEnfPath(selectDctEnfPathReqVO);
            System.out.println("selectDctEnfPathResVOS >>>>>>>>>>>>>>>>>>>>> = " + selectDctEnfPathResVOS);
            resultListVO.setChargerList(selectDctEnfPathResVOS);



            /* selectTempCreateCardList END */
            System.out.println("resultListVO >>>>>>>>>>>>> : " + resultListVO);

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        System.out.println(resultListVO);
        return resultListVO;
    }


    /* 비밀배포카드 팝업 유통비밀송수신결과 조회 */
    @Override
    public PageInfo<InsertRelayHisResVO> selectRelayHis(InsertRelayHisReqVO param){
        PageInfo<InsertRelayHisResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(bmsDctRdocMapper.selectRelayHis(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }



    // 본문파일 저장 관련
    @Value("${tempFilePath}")
    private String tempFilePath;

    private final RestTemplate restTemplate;

    public File downloadFile(String fileUrl, String destinationPath) throws IOException {
//        return restTemplate.execute(
//                fileUrl,
//                HttpMethod.GET,
//                null,
//                (ResponseExtractor<File>) clientHttpResponse -> {
//                    File file = new File(destinationPath);
//                    FileUtils.copyInputStreamToFile(clientHttpResponse.getBody(), file);
//                    return file;
//                }
//        );

        SslContext context = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        HttpClient httpClient = HttpClient.create().secure(provider -> provider.sslContext(context));

        byte[] fileData = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build()
                .get()
                .uri(fileUrl)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();  // blocking call to wait for the response

        if (fileData != null) {
            File file = new File(destinationPath);
            FileUtils.writeByteArrayToFile(file, fileData);
            return file;
        } else {
            throw new IOException("Failed to download file from: " + fileUrl);
        }
    }
    public File draftFileToFileObject(String url, String name) {
        File file = null;

        try {
            file = downloadFile(url, tempFilePath + "/" +name);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public MultipartFile convertFileToMultipartFile(File file) throws IOException {
        // 부모 디렉토리가 null인 경우, 기본적으로 현재 작업 디렉토리를 사용하도록 설정
        File parentDirectory = (file.getParentFile() != null) ? file.getParentFile() : new File(".");

        FileItem fileItem = new DiskFileItem("file"
                , Files.probeContentType(file.toPath())
                , false, file.getName()
                , (int) file.length(),
                parentDirectory);

        try {
            InputStream is = new FileInputStream(file);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(is, os);
        } catch (IOException e) {
            log.error("[convertFileToMultipartFile] error {}", e.getMessage());

            throw new IOException(e);
        }
        return new CommonsMultipartFile(fileItem);
    }

    public AttachFileVO insertDraftFile(InsertTempListReqVO param, SimpleDateFormat formatter, Date today) throws IOException {
        AttachFileVO res = new AttachFileVO();
        File draftFile = draftFileToFileObject(param.getDrafturl(), param.getDraftdocttl());
        if(draftFile != null) {
            MultipartFile draftMultipartFile = convertFileToMultipartFile(draftFile);

            List<MultipartFile> mainAttach = new ArrayList<>();
            if (draftMultipartFile != null) {
                mainAttach.add(draftMultipartFile);
            }

            // 본문 저장
            if (mainAttach.size() > 0) {
                AttachFileVO attachFileVO = getFileVO(param, formatter.format(today));
                attachFileVO.setGubun("DFT01");
                attachFileVO.setTotal(1);
                attachFileVO.setProgressOffset(0);
                List<AttachFileVO> list = attachFileService.insertAttachFile(mainAttach, attachFileVO);

                res = list.get(0);
//                res = attachFileVO;

                // temp파일 삭제
                draftFile.delete();
            }
        }
        return res;
    }

    // 권한관리카드 insert
    public void insertAuthCard(InsertTempListReqVO param, String authDate, String authGubun) {
        // 열람권자 수 만큼 insert
        for (InsertTempListReqVO rcvsData : param.getRcvsList()) {
            InsertAuthCardReqVO insertAuthCardParam = new InsertAuthCardReqVO();
            insertAuthCardParam.setDocid(param.getResdocid());
            insertAuthCardParam.setAuthgrantdt(authDate);

            // seq 확인
            SelectAuthCardReqVO selectAuthCardParam = new SelectAuthCardReqVO();
            selectAuthCardParam.setDocid(param.getResdocid());
            List<SelectAuthCardResVO> authCard = bmsDctRdocMapper.selectAuthCard(selectAuthCardParam);

            if (authCard.size() > 0) {
                insertAuthCardParam.setAuthseq((authCard.get(authCard.size() - 1).getAuthseq()) + 1);
            } else {
                insertAuthCardParam.setAuthseq(1);
            }

            insertAuthCardParam.setAuthgubun(authGubun); // 01(열람), 02(재사용), 03(인쇄), 04(대출), 05(지출)
            insertAuthCardParam.setAuthgrantflag("Y");
            insertAuthCardParam.setAuthreqdt(authDate);
            insertAuthCardParam.setAuthexpiredt("99991231235959");

            // 요청자
            String reqDeptid = rcvsData.getRcvdeptid();
            String reqDeptname = rcvsData.getRcvdeptname();
            String reqId = rcvsData.getRcvsuserid();
            String reqName = rcvsData.getRcvsusername();

            // 담당자
            String authorDeptid = "";
            String authorDeptname = "";
            String authorId = "";
            String authorName = "";

            // 결재자
            String apprDeptid = "";
            String apprDeptname = "";
            String apprId = "";
            String apprName = "";

            if (param.getResasktype().equals("DEA45")) { // 1인결재 : 담당/결재-기안자
                for (InsertTempListReqVO pathData : param.getPathList()) {
                    if (!Objects.equals(pathData.getAsktype(), "")) {
                        authorDeptid = pathData.getDeptid();
                        authorDeptname = pathData.getDeptname();
                        authorId = pathData.getObjid();
                        authorName = pathData.getObjname();

                        apprDeptid = pathData.getDeptid();
                        apprDeptname = pathData.getDeptname();
                        apprId = pathData.getObjid();
                        apprName = pathData.getObjname();
                    }
                }
            } else { // 결재 : 담당-기안자, 결재-결재자
                for (InsertTempListReqVO pathData : param.getPathList()) {
                    if (!Objects.equals(pathData.getAsktype(), "")) {
                        if (pathData.getAsktype().equals("DEA11")) {
                            authorDeptid = pathData.getDeptid();
                            authorDeptname = pathData.getDeptname();
                            authorId = pathData.getObjid();
                            authorName = pathData.getObjname();
                        }

                        if (pathData.getAsktype().equals("DEA41") || pathData.getAsktype().equals("DEA51")
                                || pathData.getAsktype().equals("DEA61") || pathData.getAsktype().equals("DEA71")) {
                            apprDeptid = pathData.getDeptid();
                            apprDeptname = pathData.getDeptname();
                            apprId = pathData.getObjid();
                            apprName = pathData.getObjname();
                        }
                    }
                }
            }

            insertAuthCardParam.setReqdeptid(reqDeptid);
            insertAuthCardParam.setReqdeptname(reqDeptname);
            insertAuthCardParam.setReqid(reqId);
            insertAuthCardParam.setReqname(reqName);
            insertAuthCardParam.setAuthordeptid(authorDeptid);
            insertAuthCardParam.setAuthordeptname(authorDeptname);
            insertAuthCardParam.setAuthorid(authorId);
            insertAuthCardParam.setAuthorname(authorName);
            insertAuthCardParam.setApprdeptid(apprDeptid);
            insertAuthCardParam.setApprdeptname(apprDeptname);
            insertAuthCardParam.setApprid(apprId);
            insertAuthCardParam.setApprname(apprName);
            int resInsertAuthCard = bmsDctRdocMapper.insertAuthCard(insertAuthCardParam);
        }
    }

    /* 발송실패 오프라인인쇄 팝업 > 인쇄이력 등록 */
    public int insertHistoryCard(InsertTempListReqVO param) {
        String paramDocId = "";
        paramDocId = param.getDocid();

        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

        String histSeq = bmsDctHistCardMapper.getHistSeq();
        InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, histSeq, formatter.format(today));
        insertHistoryReqVO.setDocid(param.getDocid()); //사본docid
        insertHistoryReqVO.setHistgubun("06"); //인쇄
        insertHistoryReqVO.setHiststatus("057"); //시행용 인쇄
        int resInsertHistory = bmsDctRdocMapper.insertHistoryCard(insertHistoryReqVO);
        System.out.println(resInsertHistory);
        return resInsertHistory;
    }

    public AttachFileVO useInsertedData(AttachFileVO data) {
        return data;
    }

}
