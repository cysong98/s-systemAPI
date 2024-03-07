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


import com.anylogic.ssystem.bms.com.model.SelectVgroupUserReqVO;
import com.anylogic.ssystem.bms.com.model.SelectVgroupUserResVO;
import com.anylogic.ssystem.bms.dct.mapper.*;
import com.anylogic.ssystem.bms.dct.model.*;
import com.anylogic.ssystem.bms.login.mapper.ComUserinfoMapper;
import com.anylogic.ssystem.bms.login.model.GetUserLoginReqVO;
import com.anylogic.ssystem.bms.login.model.GetUserLoginResVO;
import com.anylogic.ssystem.bms.trn.model.InsertTrnStepReqVO;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;


@Slf4j
@RequiredArgsConstructor
@Service("BmsDctEnfHistoryService")
@Transactional
public class BmsDctEnfHistoryServiceImpl implements BmsDctEnfHistoryService {


    @Autowired
    private BmsDctEnfHistoryMapper bmsDctEnfHistoryMapper;

    @Autowired
    private BmsDctEnfPathMapper bmsDctEnfPathMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;

    @Autowired
    private ComUserinfoMapper comUserinfoMapper;

    @Autowired
    private BmsDctEnfDocMapper bmsDctEnfDocMapper;

    @Autowired
    private BmsDctInvrcptMapper bmsDctInvrcptMapper;

    @Autowired
    private BmsDctRdocRcvsMapper bmsDctRdocRcvsMapper;
    @Autowired
    private BmsDctHistCardMapper bmsDctHistCardMapper;

    String seq = "";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    String currentDateTime = LocalDateTime.now().format(formatter);
    GetUserLoginResVO resGetUserLogin;
    List<InsertEnforcePathReqVO> filteredObjIdList;
    int actseq;
    /* 접수대기함 디테일카드 - 문서처리버튼 - 의견저장, 문서처리 */
    /* 1. 처리의견 insert */
    /* 2. 상태값 update (접수대기 -> 접수 상신) */
    @Override
    @Transactional
    public int documentProcBIA(DocumentProcBIAReqVO param){
        int resDocumentProcBIA = 0;
        try {

            /* 00. 로그인한 유저 정보 조회 */
            GetUserLoginReqVO getUserLoginReqVO = GetUserLoginReqVO.builder()
                    .userid(param.getLoginuserid())
                    .build();
            //getUserLogin(param)부분은 로그인 api가 변동시 수정예정.2023-10-27 기준 현재 로직은 이렇게.
            resGetUserLogin = comUserinfoMapper.getUserLogin(getUserLoginReqVO.builder()
//                    .userid(param.getLoginuserid())
                    .userid(getUserLoginReqVO.getUserid())
                    .build());

            currentDateTime = LocalDateTime.now().format(formatter);
            //GetUserLoginResVO(userid=song, loginid=null, username=송채연, deptid=1233000, deptname=전산팀, residentno=null, position=팀원, grade=주임, userorder=0.0, imgubun=null, isdeleted=null, isconcurrent=null, positiondetail=null, vgroupid=null)
//            System.out.println("deptid >>>>>>>>> : " + resGetUserLogin.get(0).getDeptid());

//            actseq = bmsDctEnfPathMapper.getActSeq(); //시퀀스 nextval 사용.

            List<InsertEnforcePathReqVO> chargerList = param.getChargerList(); //charcherList (담당자 리스트)를 불러옴.

//            // pathList2 중에 askTemp 값이 "결재"인 objid 값들 필터링 및 출력
//            chargerList.stream()
//                    .filter(item -> "결재".equals(item.getAskTemp()))
//                    .map(InsertEnforcePathReqVO::getObjid)
//                    .forEach(System.out::println);

            //담당으로 지정된 유저 리스트
            if(Objects.equals(param.getActgubun(), "EPA17")) {
                filteredObjIdList = chargerList.stream()
                        .filter(item -> "EPA08".equals(item.getActgubun()))
                        .collect(Collectors.toList()); // EPA08 업무담당지정 (업무담당자로 지정된 유저리스트)
            }else{
                filteredObjIdList = chargerList.stream()
                    .filter(item -> "담당".equals(item.getAskTemp()))
                    .collect(Collectors.toList());
            }

            /* 04. INSERT BMS_DCT_ENF_PATH */
            // 담당정보 (chargerList) 저장
            int resInsertCharger = 0;
            SelectDctEnfPathReqVO selectDctEnfPathReqVO = new SelectDctEnfPathReqVO();
            selectDctEnfPathReqVO.setEnfdocid(param.getEnfdocid());
            List<SelectDctEnfPathResVO> selectDctEnfPathResVOS = bmsDctEnfPathMapper.selectDctEnfPath(selectDctEnfPathReqVO);
            if(!selectDctEnfPathResVOS.isEmpty() && !Objects.equals(param.getActgubun(), "EPA17")){
//            if(!selectDctEnfPathResVOS.isEmpty()){
//                Map<String, Object> paramMap = new HashMap<>();
//                paramMap.put("table", "sbms.sbms_user.bms_dct_enf_path");
//                paramMap.put("docid", param.getEnfdocid());
                bmsDctEnfPathMapper.deleteEnfPathList(param.getEnfdocid());
            }

            String histSeq = "0";
            /* 01. UPDATE bms_dct_enf_doc */
            /* 02. INSERT BMS_DCT_ENF_PATH */
            /* 03. INSERT BMS_DCT_ENF_HISTORY */
            int updateEnfDocRepath = 0;
            int resUpdateReceipt = 0;
            if( filteredObjIdList.size() > 0 && !Objects.equals(param.getActgubun(), "EPA05")) { //(담당지정있음,반송아님)접수처리시 동작
                Date today = new Date();
                Locale currentLocale = new Locale("KOREAN", "KOREA");
                String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
                SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
                for (int i = 0; i < filteredObjIdList.size(); i++) {
                    actseq = bmsDctEnfPathMapper.getActSeq(); //시퀀스 nextval 사용.
                    InsertEnforcePathReqVO tempReqVO = filteredObjIdList.get(i);
                    if (!Objects.equals(tempReqVO.getActseq(), "")) {
                        tempReqVO.setEnfdocid(param.getEnfdocid());
                        tempReqVO.setActopinion(param.getActopinion());

                        /* 01. UPDATE bms_dct_enf_doc */
                        tempReqVO.setDocid(param.getDocid());
                        tempReqVO.setEmergency(param.getEmergency());
                        tempReqVO.setDoctitle(param.getDocttl());
                        tempReqVO.setDocstatus(param.getDocstatus());
                        if(!Objects.equals(param.getActgubun(), "EPA17")) { //EPA17: 재지정요청
                            tempReqVO.setInid(param.getLoginuserid()); //접수처리하는 관리자 유저id
                        }
                        tempReqVO.setUpid(param.getLoginuserid());
                        tempReqVO.setKeyword(param.getKeyword());
                        tempReqVO.setState(param.getState());
                        tempReqVO.setAcceptdocseq(BigDecimal.valueOf(actseq));
                        tempReqVO.setAcceptdeptid(resGetUserLogin.getDeptid()); //접수자 부서ID
                        tempReqVO.setAcceptdeptname(resGetUserLogin.getDeptname()); //접수자 부서명
                        tempReqVO.setAcceptid(resGetUserLogin.getUserid()); //접수자 ID
                        tempReqVO.setAcceptname(resGetUserLogin.getUsername()); //접수자 이름
                        tempReqVO.setAcceptdutyname(resGetUserLogin.getGrade() + "/" + resGetUserLogin.getPosition()); //접수자 직위/직급
                        tempReqVO.setActseq(BigDecimal.valueOf(i));
                        UpdateEnfDocRepathReqVO insertEnforceCardParamVO = getEnfDocVO(tempReqVO, i);
                        //01. UPDATE bms_dct_enf_doc
                        updateEnfDocRepath += bmsDctEnfDocMapper.updateEnfDocRepath(insertEnforceCardParamVO);

                        /* 02. INSERT BMS_DCT_ENF_PATH */
                        if(!Objects.equals(tempReqVO.getActseq(), "") && !Objects.equals(param.getActgubun(), "EPA17")) {
                            tempReqVO.setEnfdocid(param.getEnfdocid());
                            InsertEnforcePathReqVO insertEnfPathParam = getEnfPathVO(tempReqVO);
                            resInsertCharger += bmsDctEnfPathMapper.insertEnforcePath(insertEnfPathParam);
                        }

                        /* 03. INSERT BMS_DCT_ENF_HSITORY */
//                        tempReqVO.setRecname(param.getRecname());
                        tempReqVO.setRecname(param.getChargerList().get(i).getActername());
                        tempReqVO.setActgubun(param.getChargerList().get(i).getActgubun());
                        if(Objects.equals(param.getActgubun(), "EPA17")){
                            tempReqVO.setActgubun("EPA17");
                        }
                        DocumentProcBIAReqVO documentProcBIAReqVO = getEnfHisVO(tempReqVO, i);
                        resDocumentProcBIA += bmsDctEnfHistoryMapper.documentProcBIA(documentProcBIAReqVO);

                        /* 04. UPDATE bms_dct_invrcpt */
                        if(Objects.equals(param.getActgubun(), "EPA04")) {
                            SelectReceiptResVO updateInvrcptParam = getInvrcptVO(tempReqVO, i);
                            resUpdateReceipt += bmsDctInvrcptMapper.updateReceipt(updateInvrcptParam);
                        }

                        /* 05. INSERT BMS_DCT_RDOC_RCVS */
                        int resInsertRdocRcvs = 0;
                        SelectDctReaderReqVO selectDctReaderReqVO = new SelectDctReaderReqVO();
                        selectDctReaderReqVO.setDocid(param.getDocid());
                        List<SelectDctReaderResVO> selectDctReaderResVOS = bmsDctRdocRcvsMapper.selectDctReader(selectDctReaderReqVO);
                        if(!selectDctReaderResVOS.isEmpty()){
                            Map<String, Object> paramMap = new HashMap<>();
                            paramMap.put("table", "sbms.sbms_user.bms_dct_rdoc_rcvs");
                            paramMap.put("docid", param.getDocid());
                            bmsDctRdocMapper.deleteTempList(paramMap);
                        }
                        int rcvsListIdx = 0;
                        for (InsertTempListReqVO tempReqVO2 : param.getRcvsList()) {
                            if (!Objects.equals(tempReqVO2.getRcvsusername(), "")) {
                                tempReqVO2.setDocid(param.getDocid());
                                tempReqVO2.setResuserid(param.getRcvsList().get(rcvsListIdx).getRcvsuserid());
                                InsertDocIdBmsDctRdocRcvsListReqVO insertDocIdBmsDctRdocRcvsListParam = getRdocRcvsVO(tempReqVO2, formatter.format(today));
                                resInsertRdocRcvs += bmsDctRdocRcvsMapper.insertDocIdBmsDctRdocRcvsList(insertDocIdBmsDctRdocRcvsListParam);
                                rcvsListIdx++;
                            }
                        }

                        /* 이력카드 이력 확인 (발송이력은 최초 한번만 찍도록)*/
                        GetHistCardInfoReqVO getHistCardInfoReqVO = new GetHistCardInfoReqVO();
                        getHistCardInfoReqVO.setDocid(param.getOridocid());
                        getHistCardInfoReqVO.setHistgubun("14");
                        getHistCardInfoReqVO.setDeptid(resGetUserLogin.getDeptid());
                        List<GetHistCardInfoResVO> resGetHistCardInfo = bmsDctHistCardMapper.getHistCardInfo(getHistCardInfoReqVO);
                        System.out.println("resGetHistCardInfo >>> "+resGetHistCardInfo);

                        /* 이력카드 저장 */
                        // INSERT bms_dct_hist_card
                        int resInsertHistory = 0;
                        InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resGetUserLogin, histSeq, currentDateTime);
                        insertHistoryReqVO.setEtc(param.getChargerList().get(0).getActername() + "/" + param.getChargerList().get(0).getDutyname());
                        insertHistoryReqVO.setHiststatus(null);
                        insertHistoryReqVO.setHistgubun("14"); //접수
                        // 최초 접수(첫 담당자 지정)할때만 이력을 저장. 그 후 이력(접수변경,접수 등)은 접수기안의 이력에서 확인.
                        if(resGetHistCardInfo != null && resGetHistCardInfo.size() < 1) {
                            SelectReceiptReqVO getReceiptReqVO = new SelectReceiptReqVO();
                            getReceiptReqVO.setDocid(param.getDocid());
                            SelectReceiptResVO resGetReceipt = bmsDctInvrcptMapper.selectReceipt(getReceiptReqVO);
                            if(resGetReceipt != null && Objects.equals(resGetReceipt.getInvrcpttype(), "NF")){
                                histSeq = bmsDctHistCardMapper.getHistSeq();
                                insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
                                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
                            }else{
                                for(int histCardTempIdx = 0; histCardTempIdx < 2; histCardTempIdx++){
                                    if(histCardTempIdx == 0){
                                        insertHistoryReqVO.setDocid(param.getOridocid()); //원본docid
                                    }else{
                                        insertHistoryReqVO.setDocid(param.getDocid()); //사본docid
                                    }
                                    histSeq = bmsDctHistCardMapper.getHistSeq();
                                    insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
                                    resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
                                }
                            }
                            System.out.println(resInsertHistory);
                        } else if (resGetHistCardInfo != null && resGetHistCardInfo.size() >= 1) {
                            insertHistoryReqVO.setDocid(param.getDocid()); //사본docid
                            if(Objects.equals(param.getDocstatus(), "EDS12")){ //EDS12 : 담당변경
                                insertHistoryReqVO.setHistgubun("37"); //담당변경
                            }
                            histSeq = bmsDctHistCardMapper.getHistSeq();
                            insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
                            resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
                            System.out.println(resInsertHistory);
                        }
                    }
                }
            } else { //(담당지정없음)반송 처리시 동작
                actseq = bmsDctEnfPathMapper.getActSeq();

                /* 01. UPDATE bms_dct_enf_doc */
                UpdateEnfDocRepathReqVO enfDocVO = UpdateEnfDocRepathReqVO.builder()
                        .enfdocid(param.getEnfdocid())
//                        .chargerid(param.getObjid()) //담당정보의 유저id
                        .docstatus(param.getDocstatus())//비밀상태
                        .moveflag("N")
                        .docid(param.getDocid())
                        .enfgubun("N") //대내
                        .distributeflag("N") //비배부 (배부처리하면 배부 Y)
                        .emergency(param.getEmergency())
                        .doctitle(param.getDocttl())
                        .updt(currentDateTime)
                        .build();
                updateEnfDocRepath = bmsDctEnfDocMapper.updateEnfDocRepath(enfDocVO);

                /* 02. INSERT BMS_DCT_ENF_PATH */ //담당자(담당경로) 없이 반려하는 경우이므로 생략.

                /* 03. INSERT BMS_DCT_ENF_HISTORY */
                DocumentProcBIAReqVO documentProcBIAReqVO = DocumentProcBIAReqVO.builder()
                        .enfdocid(param.getEnfdocid())
                        .actseq(BigDecimal.valueOf(actseq))
//                .actseq(BigDecimal.valueOf(parseInt(currentReqVO.getChargerorder())))
                        .acterid(resGetUserLogin.getUserid()) //처리자ID
                        .actername(resGetUserLogin.getUsername()) //처리자이름
                        .acterdeptname(resGetUserLogin.getGrade()) //처리자 직위직급이름
                        .acterdeptid(resGetUserLogin.getDeptid()) //처리자부서ID
                        .acterdeptname(resGetUserLogin.getDeptname()) //처리자 부서명
                        .actgubun(param.getActgubun()) //EPA05 반송 // EPA17 재지정요청
                        .actdt(currentDateTime) //처리일
                        .actopinion(param.getActopinion())
//                .pathhisid("")//처리경로이력ID
                        .build();
                resDocumentProcBIA = bmsDctEnfHistoryMapper.documentProcBIA(documentProcBIAReqVO);

                /* 이력카드 저장 */
                // INSERT bms_dct_hist_card
                int resInsertHistory = 0;
                InsertHistoryReqVO insertHistoryReqVO = getHistoryVO(param, resGetUserLogin, histSeq, currentDateTime);
                insertHistoryReqVO.setDocid(param.getDocid()); //원본docid
                insertHistoryReqVO.setHistgubun("13"); //반송
                insertHistoryReqVO.setHiststatus(null);
                histSeq = bmsDctHistCardMapper.getHistSeq();
                insertHistoryReqVO.setHistseq(new BigDecimal(histSeq));
                resInsertHistory += bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
                System.out.println(resInsertHistory);
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resDocumentProcBIA;
    }

    // 1. UPDATE bms_dct_enf_doc
    private UpdateEnfDocRepathReqVO getEnfDocVO(InsertEnforcePathReqVO vo, int index) {
//        InsertEnforcePathReqVO currentReqVO = filteredObjIdList.get(index); // 주석 해제시 마지막에 있는 담당자로 저장됨.
        InsertEnforcePathReqVO currentReqVO = filteredObjIdList.get(0); //bms_dct_enf_doc 고유키 값 (enfdocid) 딱 하나라 enfdocid과 담당자는 현재 1:1 구조.
        UpdateEnfDocRepathReqVO build = UpdateEnfDocRepathReqVO.builder()
                .enfdocid(currentReqVO.getEnfdocid())
                .inid(currentReqVO.getInid()) //접수처리하는 관리자 유저id
                .chargerid(currentReqVO.getObjid()) //담당정보의 유저id
                .docstatus(currentReqVO.getDocstatus())//비밀상태
                .moveflag("N")
                .docid(currentReqVO.getDocid())
                .enfgubun("N") //대내
                .distributeflag("N") //비배부 (배부처리하면 배부 Y)
                .emergency(currentReqVO.getEmergency())
                .doctitle(currentReqVO.getDoctitle())
                .acceptdocseq(currentReqVO.getAcceptdocseq())
                .acceptdeptid  (currentReqVO.getAcceptdeptid())
                .acceptdeptname(currentReqVO.getAcceptdeptname())
                .acceptid      (currentReqVO.getAcceptid())
                .acceptname    (currentReqVO.getAcceptname())
                .acceptdutyname(currentReqVO.getAcceptdutyname())
                .updt(currentDateTime)
                .build();
        return build;
    }

    // 2. INSERT or UPDATE BMS_DCT_ENF_PATH
    private InsertEnforcePathReqVO getEnfPathVO (InsertEnforcePathReqVO vo){
        InsertEnforcePathReqVO build = InsertEnforcePathReqVO.builder()
                .enfdocid(vo.getEnfdocid())
                .actseq(BigDecimal.valueOf(actseq))
//                .acterid(resGetUserLogin.getUserid())
//                .actseq(BigDecimal.valueOf(parseInt(vo.getChargerorder())))
                .acterid(vo.getObjid()) //담당자 id
//                .actername(vo.getObjname())
                .actername(vo.getActername())
                .deptid(vo.getDeptid())
                .deptname(vo.getDeptname())
                .dutyname(vo.getDutyname())
                .deptnamedesc(vo.getDeptnamedesc())
                .indt(currentDateTime)
                .build();
        return build;
    }

    // 3. INSERT BMS_DCT_ENF_HSITORY
    private DocumentProcBIAReqVO getEnfHisVO(InsertEnforcePathReqVO vo, int index){
        InsertEnforcePathReqVO currentReqVO = filteredObjIdList.get(index);
        DocumentProcBIAReqVO build = DocumentProcBIAReqVO.builder()
            .enfdocid(currentReqVO.getEnfdocid())
            .actseq(BigDecimal.valueOf(actseq))
//                .actseq(BigDecimal.valueOf(parseInt(currentReqVO.getChargerorder())))
            .acterid(resGetUserLogin.getUserid()) //처리자ID
            .actername(resGetUserLogin.getUsername()) //처리자이름
//                .acterdeptname("") //처리자 직위직급이름
            .acterdeptid(resGetUserLogin.getDeptid()) //처리자부서ID
            .acterdeptname(resGetUserLogin.getDeptname()) //처리자 부서명
            .actgubun(currentReqVO.getActgubun()) //EPA08 업무담당지정 //EPA17 재지정요청  //EPA05 반송
            .actdt(currentDateTime) //처리일
            .actopinion(currentReqVO.getActopinion())
            .recid(currentReqVO.getObjid()) // 수신자ID
            .recdutyname(currentReqVO.getDutynamedesc()) // 수신자직위직급
            .recdeptid(currentReqVO.getDeptid()) // 수신자부서ID
            .recname(currentReqVO.getRecname())//수신담당자이름
            .recdeptname(currentReqVO.getDeptname()) // 수신자부서이름
//                .pathhisid("")//처리경로이력ID
            .build();
        return build;
    }

    // 4. UPDATE bms_dct_invrcpt
    private SelectReceiptResVO getInvrcptVO(InsertEnforcePathReqVO vo, int index){
        InsertEnforcePathReqVO currentReqVO = filteredObjIdList.get(index);
        SelectReceiptResVO build = SelectReceiptResVO.builder()
                .docid(vo.getDocid()) //사본DocId
                .rcvdt(currentDateTime) //접수일자
                .rcvdeptid(currentReqVO.getDeptid())
                .rcvdeptname(currentReqVO.getDeptname())
                .rcvid(currentReqVO.getObjid()) //영수증의 수신자(담당자)ID
                .rcvname(currentReqVO.getRecname()) //영수증의 수신자(담당자)이름
                .rcvdutyname(currentReqVO.getDutynamedesc())
                .build();
        return build;
    }

    // . INSERT or UPDATE BMS_DCT_PATH
    private InsertPathReqVO getPathVO (InsertTempListReqVO vo){
        InsertPathReqVO build = InsertPathReqVO.builder()
                .docid(vo.getDocid())
                .pathorder(new BigDecimal(vo.getPathorder()))
                .state("DCST1") //DCST1:처리대기
                .deptname(vo.getDeptname())
                .dutyname(vo.getDutyname())
                .dutynamedesc(vo.getDutynamedesc())
                .electsignid(vo.getElectsignid())
                .asktype(vo.getAsktype())
                .mainver(vo.getMainver())
                .indt(currentDateTime)
                .inid(vo.getResuserid())
                .build();
        return build;
    }

    /* 05. INSERT BMS_DCT_RDOC_RCVS */
    private InsertDocIdBmsDctRdocRcvsListReqVO getRdocRcvsVO (InsertTempListReqVO vo, String indt){
        InsertDocIdBmsDctRdocRcvsListReqVO build = InsertDocIdBmsDctRdocRcvsListReqVO.builder()
                .docid(vo.getDocid())
                .userid(vo.getRcvsuserid())
                .username(vo.getRcvsusername())
                .deptid(vo.getRcvdeptid())
                .deptname(vo.getRcvdeptname())
                .dutyname(vo.getRcvdutyname())
                .inseq(String.valueOf(vo.getRcvsinseq()))
                .indt(indt)
                .inid(vo.getResuserid())
                .build();
        return build;
    }

    private InsertHistoryReqVO getHistoryVO(DocumentProcBIAReqVO vo, GetUserLoginResVO userInfo,String histSeq, String indt) {
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


}
