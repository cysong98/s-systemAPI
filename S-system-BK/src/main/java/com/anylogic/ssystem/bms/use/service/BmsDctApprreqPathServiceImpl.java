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


import com.anylogic.ssystem.bms.com.model.SelectApprReqListReqVO;
import com.anylogic.ssystem.bms.com.model.SelectApprReqListResVO;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctHistCardMapper;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;
import com.anylogic.ssystem.bms.dct.model.*;
import com.anylogic.ssystem.bms.login.mapper.ComUserinfoMapper;
import com.anylogic.ssystem.bms.login.model.GetUserLoginReqVO;
import com.anylogic.ssystem.bms.login.model.GetUserLoginResVO;
import com.anylogic.ssystem.bms.use.mapper.*;
import com.anylogic.ssystem.bms.use.model.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service("BmsDctApprreqPathService")
@Transactional
public class BmsDctApprreqPathServiceImpl implements BmsDctApprreqPathService {

    @Autowired
    private ComUserinfoMapper comUserinfoMapper;
    @Autowired
    private BmsDctHistCardMapper bmsDctHistCardMapper;

    @Autowired
    private BmsDctApprreqPathMapper bmsDctApprreqPathMapper;

    @Autowired
    private BmsDctApprreqLendMapper bmsDctApprreqLendMapper;

    @Autowired
    private BmsDctApprreqMapper bmsDctApprreqMapper;

    @Autowired
    private BmsDctApprreqReuseMapper bmsDctApprreqReuseMapper;

    @Autowired
    private BmsDctApprreqReadMapper bmsDctApprreqReadMapper;

    @Autowired
    private BmsDctApprreqPrtMapper bmsDctApprreqPrtMapper;

    @Autowired
    private BmsDctApprreqReclassMapper bmsDctApprreqReclassMapper;

    @Autowired
    private BmsDctApprreqDestructMapper bmsDctApprreqDestructMapper;

    @Autowired
    private BmsDctApprreqCnsrvMapper bmsDctApprreqCnsrvMapper;

    @Autowired
    private BmsDctApprreqTransMapper bmsDctApprreqTransMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;


    /* 각 요청서 승인/반려 처리 */
    @Transactional
    @Override
    public int updateUseReqViewAppPathApproveBIA(UpdateUseReqViewAppPathApproveBIAReqVO param) {
        int rtn = 0;
        String apprreqid = param.getApprreqid();
        String grantflag = param.getGrantflag();
        String reqtype = param.getReqtype();

        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyyMMddHHmmss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

        try {

            /* reqtype
             * 01:열람, 02:재사용, 03:인쇄, 04:대출, 05:지출,
             * 06:재분류, 07:파기, 08:존안, 10:이관대기, 11:이관연기, 12:내부이관
             */

            // 열람
            if("01".equals(reqtype)){
                List<HashMap<String, Object>> list = param.getMaplist();
                for (HashMap<String, Object> listParam : list) {
                    listParam.put("grantflag", param.getGrantflag());
                    bmsDctApprreqReadMapper.updateUseReqMapView(listParam);
                }

            // 재사용
            }else if("02".equals(reqtype)){
                List<HashMap<String, Object>> list = param.getMaplist();
                for (HashMap<String, Object> listParam : list) {
                    listParam.put("grantflag", param.getGrantflag());
                    bmsDctApprreqReuseMapper.updateUseReqMapRecycle(listParam);
                }

            // 인쇄
            }else if("03".equals(reqtype)){
                List<HashMap<String, Object>> list = param.getMaplist();
                for (HashMap<String, Object> listParam : list) {
                    listParam.put("grantflag", param.getGrantflag());
                    bmsDctApprreqPrtMapper.updateUseReqMapPrint(listParam);
                }

            // 반출 (대출, 지출)
            }else if("04".equals(reqtype) || "05".equals(reqtype)){
                List<HashMap<String, Object>> list = param.getMaplist();
                for (HashMap<String, Object> listParam : list) {
                    listParam.put("grantflag", param.getGrantflag());
                    listParam.put("grantlenddt", param.getGrantlenddt());
                    listParam.put("grantbackdt", param.getGrantbackdt());
                    bmsDctApprreqLendMapper.updateUseReqMapLend(listParam);
                }

            // 재분류
            }else if("06".equals(reqtype)){
                List<HashMap<String, Object>> list = param.getMaplist();
                for (HashMap<String, Object> listParam : list) {
                    listParam.put("grantflag", param.getGrantflag());
                    bmsDctApprreqReclassMapper.updateUseReqMapReclass(listParam);
                    if(Objects.equals(param.getGrantflag(),"Y")){
                        listParam.put("secstatus", "MGT21");//재분류 승인
                    }else{
                        listParam.put("secstatus", "MGT00");//반려
                    }
                    bmsDctApprreqReclassMapper.updateUseReqMgmtRegiSecStatus(listParam);
                }

            // 파기
            }else if("07".equals(reqtype)){
                List<HashMap<String, Object>> list = param.getMaplist();
                for (HashMap<String, Object> listParam : list) {
                    listParam.put("grantflag", param.getGrantflag());
                    bmsDctApprreqDestructMapper.updateUseReqMapDestruct(listParam);
                    if(Objects.equals(param.getGrantflag(),"Y")){
                        listParam.put("secstatus", "MGT41");//파기 승인
                    }else{
                        listParam.put("secstatus", "MGT00");//반려
                    }
                    bmsDctApprreqReclassMapper.updateUseReqMgmtRegiSecStatus(listParam);
                }

            // 존안
            }else if("08".equals(reqtype)){
                List<HashMap<String, Object>> list = param.getMaplist();
                for (HashMap<String, Object> listParam : list) {
                    listParam.put("grantflag", param.getGrantflag());
                    bmsDctApprreqCnsrvMapper.updateUseReqMapCnsrv(listParam);
                    if(Objects.equals(param.getGrantflag(),"Y")){
                        listParam.put("secstatus", "MGT31");//존안 승인
                    }else{
                        listParam.put("secstatus", "MGT00");//반려
                    }
                    bmsDctApprreqReclassMapper.updateUseReqMgmtRegiSecStatus(listParam);
                }

            // 이관대기
            }else if("10".equals(reqtype)){
                List<HashMap<String, Object>> list = param.getMaplist();
                for (HashMap<String, Object> listParam : list) {
                    listParam.put("grantflag", param.getGrantflag());
                    bmsDctApprreqTransMapper.updateUseReqMapTransWait(listParam);

                    int is_final_approver = bmsDctApprreqPathMapper.checkPathFinalApprover(param); // 0 : 아님, 1 : 최종결재자
                    if(param.getGrantflag().equals("Y") && is_final_approver == 1){
                        // bms_dct_rdoc 열람범위,공개여부,공개제한 update
                        bmsDctApprreqTransMapper.updateUseReqMapTransRdoc(listParam);
                        listParam.put("secstatus", "MGT62"); // 이관대기
                    }else{
                        if(param.getGrantflag().equals("Y")){
                            // bms_dct_rdoc 열람범위,공개여부,공개제한 update
                            bmsDctApprreqTransMapper.updateUseReqMapTransRdoc(listParam);
                            listParam.put("secstatus", "MGT61"); // 이관대기승인
                        }else{
                            listParam.put("secstatus", "MGT00");// 반려
                        }
                    }
                    bmsDctApprreqReclassMapper.updateUseReqMgmtRegiSecStatus(listParam);
                }

            // 이관연기
            }else if("11".equals(reqtype)){
                List<HashMap<String, Object>> list = param.getMaplist();
                for (HashMap<String, Object> listParam : list) {
                    listParam.put("grantflag", param.getGrantflag());
                    String granttransextdt = listParam.get("granttransextdt").toString();
                    if (granttransextdt.length() == 4) {
                        granttransextdt = granttransextdt + "1231235959";
                    }
                    listParam.put("granttransextdt", granttransextdt);
                    bmsDctApprreqTransMapper.updateUseReqMapTrans(listParam);

                    if(Objects.equals(param.getGrantflag(),"Y")){
                        listParam.put("secstatus", "MGT71"); // 이관연기승인
                    }else{
                        listParam.put("secstatus", "MGT00");//반려
                    }
                    bmsDctApprreqReclassMapper.updateUseReqMgmtRegiSecStatus(listParam);
                }

            // 내부이관
            }else if("12".equals(reqtype)){
                List<HashMap<String, Object>> list = param.getMaplist();
                for (HashMap<String, Object> listParam : list) {
                    listParam.put("grantflag", param.getGrantflag());
                    bmsDctApprreqTransMapper.updateUseReqMapTrans(listParam);

                    if(Objects.equals(param.getGrantflag(),"Y")){
                        listParam.put("secstatus", "MGT81");//내부이관 승인
                    }else{
                        listParam.put("secstatus", "MGT00");//반려
                    }
                    bmsDctApprreqReclassMapper.updateUseReqMgmtRegiSecStatus(listParam);
                }

            }else{
                throw new Exception("reqtype error" + reqtype);
            }

            //2. bms_dct_apprreq_path에 apprstatus(결재요청상태)가 "APP20"(결재완료)로 저장
            updatePathStatus(param, formatter.format(today));


            //3. bms_dct_apprreq의 reqstatus(진행상태)를 "2"(결재완료)로 수정 (반려 또는 경로지정에서 맨 마지막인 사람이 승인했을 경우)
            if(param.getGrantflag().equals("N")){ // 반려
                rtn = updateApprreqStatus(grantflag, apprreqid, formatter.format(today));
            }else{
                int is_final_approver = bmsDctApprreqPathMapper.checkPathFinalApprover(param); // 0 : 아님, 1 : 최종결재자
                if(param.getGrantflag().equals("Y") && is_final_approver == 1){
                    rtn = updateApprreqStatus(grantflag, apprreqid, formatter.format(today) );

                    //3-1. bms_dct_auth_card에 권한 추가 (열람/재사용/반출/인쇄 일 경우만) (맨 마지막 결재 승인자만)
                    if ("01".equals(reqtype) || "02".equals(reqtype) || "03".equals(reqtype) || "04".equals(reqtype) || "05".equals(reqtype))
                    insertAuthCard(param, param.getReqtype(), param.getApprid(), param.getReqid(), formatter.format(today));

                }
            }

            // 4. bms_dct_hist_card 이력 저장
            updateHistory(param, formatter.format(today));

        } catch (Exception e) {
            throw new AnyXException(e);
        }


        return rtn;
    }

    // 유저 정보 조회
    private GetUserLoginResVO getUserInfo(String userId) {
        GetUserLoginReqVO getUserLoginReqVO = new GetUserLoginReqVO();
        getUserLoginReqVO.setUserid(userId);
        return comUserinfoMapper.getUserLogin(getUserLoginReqVO);
    }

    // bms_dct_apprreq_path에 apprstatus(결재요청상태)가 "APP20"(결재완료)로 저장
    private void updatePathStatus(UpdateUseReqViewAppPathApproveBIAReqVO param, String today) {
        if(param.getGrantflag().equals("Y")){
            param.setApprgubun("1"); // 승인
        }else{
            param.setApprgubun("2"); // 비승인
        }
        param.setApprstatus("APP20");
        param.setUpdtid(param.getApprid());
        param.setUpdtdt(today);
        bmsDctApprreqPathMapper.updateUseReqViewAppPath(param);
    }

    // bms_dct_apprreq의 reqstatus(진행상태)를 "2"(결재완료)로 저장
    private int updateApprreqStatus(String grantflag, String apprreqid, String today) {
        int rtn;
        UpdateUseReqViewAppApproveReqVO updateUseReqViewAppApproveParam = new UpdateUseReqViewAppApproveReqVO();
        updateUseReqViewAppApproveParam.setApprreqid(apprreqid);
        updateUseReqViewAppApproveParam.setGrantflag(grantflag);
        updateUseReqViewAppApproveParam.setApprenddt(today);
        rtn = bmsDctApprreqMapper.updateUseReqViewApp(updateUseReqViewAppApproveParam);
        return rtn;
    }

    // bms_dct_hist_card 이력 저장
    private String histgubun;
    private String histstatus;

    private void updateHistory(UpdateUseReqViewAppPathApproveBIAReqVO param, String today) {
        List<HashMap<String, Object>> list = param.getMaplist();

        for (HashMap<String, Object> listParam : list) {

            // 결재자 유저정보
            GetUserLoginResVO userinfo = getUserInfo(param.getApprid());

            // 이력 정보
            String histSeq = bmsDctHistCardMapper.getHistSeq();
            setValues(param.getReqtype(), param.getGrantflag());

            InsertHistoryReqVO insertHistoryReqVO = InsertHistoryReqVO.builder()
                    .docid(listParam.get("docid").toString())
                    .histdt(today)
                    .histseq(new BigDecimal(histSeq))
                    .apprreqid(param.getApprreqid())
                    .histgubun(histgubun)
                    .deptid(userinfo.getDeptid())
                    .deptname(userinfo.getDeptname())
                    .dutyname(userinfo.getGrade())
                    .userid(userinfo.getUserid())
                    .username(userinfo.getUsername())
                    .histstatus(histstatus)
                    .build();
            bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
        }
    }

    private void setValues(String reqtype, String grantflag) {
        switch (reqtype) {
            case "01": setValues("05", "041", "042", grantflag); break; // 열람
            case "02": setValues("07", "061", "062", grantflag); break; // 재사용
            case "03": setValues("06", "071", "072", grantflag); break; // 인쇄
            case "04": setValues("24", "081", "082", grantflag); break; // 대출
            case "05": setValues("25", "081", "082", grantflag); break; // 지출
            case "06": setValues("16", "091", "092", grantflag); break; // 재분류
            case "07": setValues("20", "101", "102", grantflag); break; // 파기
            case "08": setValues("17", "111", "112", grantflag); break; // 존안
            case "10": setValues("28", "121", "122", grantflag); break; // 이관대기
            case "11": setValues("18", "131", "132", grantflag); break; // 이관연기
            case "12": setValues("29", "141", "142", grantflag); break; // 내부이관
        }
    }

    private void setValues(String gubun, String statusY, String statusN, String grantflag) {
        histgubun = gubun;
        histstatus = grantflag.equals("Y") ? statusY : statusN;
    }

    // bms_dct_auth_card에 권한 추가
    private void insertAuthCard(UpdateUseReqViewAppPathApproveBIAReqVO param, String reqtype, String approverid, String requesterid, String today){

        List<HashMap<String, Object>> list = param.getMaplist();

        for (HashMap<String, Object> listParam : list) {

            // 결재자 유저정보
            GetUserLoginResVO approverinfo = getUserInfo(approverid);

            // 요청자 유저정보
            GetUserLoginResVO requesterinfo = getUserInfo(requesterid);

            // 비밀담당자 유저정보
            GetDctRdocReqVO getDctRdocReqVO = new GetDctRdocReqVO();
            getDctRdocReqVO.setDocid(listParam.get("docid").toString());
            GetDctRdocResVO dctRdoc = bmsDctRdocMapper.getDctRdoc(getDctRdocReqVO);
            GetUserLoginResVO authorinfo = getUserInfo(dctRdoc.getAuthorid());

            // 권한요청일자 조회
            SelectApprReqListReqVO selectApprReqListReqVO = new SelectApprReqListReqVO();
            selectApprReqListReqVO.setApprreqid(param.getApprreqid());
            SelectApprReqListResVO apprreqVO = bmsDctApprreqMapper.getApprReqregdtByid(selectApprReqListReqVO);
            String reqdt = apprreqVO.getReqregdt();

            // seq 확인
            SelectAuthCardReqVO selectAuthCardParam = new SelectAuthCardReqVO();
            selectAuthCardParam.setDocid(listParam.get("docid").toString());
            List<SelectAuthCardResVO> authCard = bmsDctRdocMapper.selectAuthCard(selectAuthCardParam);
            int seq = 0;
            if (authCard.size() > 0) {
                seq = authCard.get(authCard.size() - 1).getAuthseq() + 1;
            } else {
                seq = 1;
            }

            // 인쇄요청일 경우 파기예정일자 조회
            String reqdestructdt = "";
            if(reqtype.equals("03")){
                SelectUseReqPrintListReqVO selectUseReqPrintListReqVO = new SelectUseReqPrintListReqVO();
                selectUseReqPrintListReqVO.setApprreqid(param.getApprreqid());
                SelectUseReqPrintListResVO prtVO = bmsDctApprreqPrtMapper.getReqdesturcdtByid(selectUseReqPrintListReqVO);
                reqdestructdt = prtVO.getReqdestructdt();
            }

            // 만기일자 확인, 권한요청 내용
            String expiredt = "";
            String authgoal = "";
            switch (reqtype) {
                case "01":
                    expiredt = listParam.get("grantreadenddt").toString().replaceAll("-","")+ "235959";
                    authgoal = "비밀열람";
                    break;
                case "02":
                    expiredt = listParam.get("grantreuseenddt").toString().replaceAll("-","")+ "235959";
                    authgoal = "비밀재사용";
                    break;
                case "03":
                    expiredt = listParam.get("grantdestructdt").toString().replaceAll("-","")+ "235959";
                    authgoal = "비밀인쇄";
                    break;
                case "04":
                case "05":
                    expiredt = listParam.get("grantbackdt").toString().replaceAll("-","")+ "235959";
                    authgoal = "비밀반출";
                    break;
            }

            InsertAuthCardReqVO authCardReqVO = InsertAuthCardReqVO.builder()
                    .docid(listParam.get("docid").toString())
                    .authgrantdt(today)
                    .authseq(seq)
                    .apprreqid(param.getApprreqid())
                    .authgubun(reqtype)
                    .authgoal(authgoal)
                    .authgrantflag("Y")
                    .authreqdt(reqdt)
                    .authexpiredt(expiredt)
                    .prtdestructplandt(reqdestructdt)

                    .reqdeptid(requesterinfo.getDeptid())
                    .reqdeptname(requesterinfo.getDeptname())
                    .reqid(requesterinfo.getUserid())
                    .reqname(requesterinfo.getUsername())

                    .authordeptid(authorinfo.getDeptid())
                    .authordeptname(authorinfo.getDeptname())
                    .authorid(authorinfo.getUserid())
                    .authorname(authorinfo.getUsername())

                    .apprid(approverinfo.getUserid())
                    .apprname(approverinfo.getUsername())
                    .apprdeptid(approverinfo.getDeptid())
                    .apprdeptname(approverinfo.getDeptname())

                    .build();
            bmsDctRdocMapper.insertAuthCard(authCardReqVO);
        }
    }

}
