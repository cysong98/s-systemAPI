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
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocRcvsMapper;
import com.anylogic.ssystem.bms.dct.model.DeleteDocIdBmsDctRdocRcvsListReqVO;
import com.anylogic.ssystem.bms.dct.model.InsertDocIdBmsDctRdocRcvsListReqVO;
import com.anylogic.ssystem.bms.dct.model.InsertHistoryReqVO;
import com.anylogic.ssystem.bms.dct.model.UpdateAuthCardReqVO;
import com.anylogic.ssystem.bms.login.mapper.ComUserinfoMapper;
import com.anylogic.ssystem.bms.login.model.GetUserLoginReqVO;
import com.anylogic.ssystem.bms.login.model.GetUserLoginResVO;
import com.anylogic.ssystem.bms.use.mapper.*;
import com.anylogic.ssystem.bms.use.model.*;
import com.anylogic.ssystem.common.file.mapper.AttachFileSbmsMapper;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import java.util.Map;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Slf4j
@RequiredArgsConstructor
@Service("BmsDctApprreqService")
@Transactional
public class BmsDctApprreqServiceImpl implements BmsDctApprreqService {

    @Value("${filePath}")
    private String filePath;

    @Autowired
    private AttachFileSbmsMapper attachFileSbmsMapper;
    
    @Autowired
    private BmsDctHistCardMapper bmsDctHistCardMapper;

    @Autowired
    BmsDctMgmtRegiMapper bmsDctMgmtRegiMapper;

    @Autowired
    private ComUserinfoMapper comUserinfoMapper;

    @Autowired
    private BmsDctRdocRcvsMapper bmsDctRdocRcvsMapper;
    
    @Autowired
    private BmsDctApprreqMapper bmsDctApprreqMapper;

    @Autowired
    private BmsDctApprreqLendMapper bmsDctApprreqLendMapper;

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


    /* 재사용 가능 여부 체크*/
    @Override
    public CheckIsReusableResVO checkIsReusable(CheckIsReusableReqVO param){
        CheckIsReusableResVO checkIsReusableResVO;
        try {
            checkIsReusableResVO = bmsDctApprreqMapper.checkIsReusable(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return checkIsReusableResVO;
    }

    /* 비밀관리기록부 팝업 > 재분류 요청자,확인자 정보 조회 */
    @Override
    public List<SelectPathInfoResVO> selectReclassPathInfo(SelectPathInfoReqVO param){
        List<SelectPathInfoResVO> selectPathInfoResVO;
        try {
            selectPathInfoResVO = bmsDctApprreqMapper.selectReclassPathInfo(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return selectPathInfoResVO;
    }

    /* 비밀관리기록부 팝업 > 파기 요청자,확인자 정보 조회 */
    @Override
    public List<SelectPathInfoResVO> selectDestructPathInfo(SelectPathInfoReqVO param){
        List<SelectPathInfoResVO> selectPathInfoResVO;
        try {
            selectPathInfoResVO = bmsDctApprreqMapper.selectDestructPathInfo(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return selectPathInfoResVO;
    }

    /* 비밀관리기록부 팝업 > 이관 요청자,확인자 정보 조회 */
    @Override
    public List<SelectPathInfoResVO> selectTransPathInfo(SelectPathInfoReqVO param){
        List<SelectPathInfoResVO> selectPathInfoResVO;
        try {
            selectPathInfoResVO = bmsDctApprreqMapper.selectTransPathInfo(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return selectPathInfoResVO;
    }

    /* 비밀관리기록부 팝업 > 존안 요청자,확인자 정보 조회 */
    @Override
    public List<SelectPathInfoResVO> selectCnsrvPathInfo(SelectPathInfoReqVO param){
        List<SelectPathInfoResVO> selectPathInfoResVO;
        try {
            selectPathInfoResVO = bmsDctApprreqMapper.selectCnsrvPathInfo(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return selectPathInfoResVO;
    }

    /* 비밀문서 사용(처리할, 기안한, 처리한, 요청서완료처리) 목록 조회 */
    @Override
    public PageInfo<SelectUseReqListResVO> selectUseReqList(SelectUseReqListReqVO param){
        PageInfo<SelectUseReqListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(bmsDctApprreqMapper.selectUseReqList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }

    /* (비밀문서사용처리 > 기안한 요청서 > 요청서회수/권한회수된 요청서 삭제) & (재사용/인쇄/반출 실행 시 열람권한이 없다면 요청서 삭제) */
    @Transactional
    @Override
    public int deleteUseReqList(List<DeleteUseReqListReqVO> params){
        int res = 0;

        try {
            /* 요청 타입 (reqtype)
             * 01:열람, 02:재사용, 03:인쇄, 04:대출, 05:지출,
             * 06:재분류, 07:파기, 08:존안,
             * 10:이관대기, 11:이관연기, 12:내부이관
             */
            String[] reqTypes1 = {"01","02","03","06","07","08","10","11","12"};
            String[] reqTypes2 = {"04","05"};

            for (DeleteUseReqListReqVO param : params) {

                HashMap<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("apprreqid",param.getApprreqid());

                //1. 상세 요청서 삭제
                if("01".equals(param.getReqtype())){
                    bmsDctApprreqMapper.deleteUseApprreqRead(paramMap);
                }else if("02".equals(param.getReqtype())){
                    bmsDctApprreqMapper.deleteUseApprreqReuse(paramMap);
                }else if("03".equals(param.getReqtype())){
                    bmsDctApprreqMapper.deleteUseApprreqPrintFle(paramMap);
                    bmsDctApprreqMapper.deleteUseApprreqPrint(paramMap);
                }else if("04".equals(param.getReqtype()) || "05".equals(param.getReqtype())){
                    bmsDctApprreqMapper.deleteUseApprreqLendFle(paramMap);
                    bmsDctApprreqMapper.deleteUseApprreqLend(paramMap);
                }else if("06".equals(param.getReqtype())){
                    bmsDctApprreqMapper.deleteUseApprreqReclass(paramMap);
                }else if("07".equals(param.getReqtype())){
                    bmsDctApprreqMapper.deleteUseApprreqDestruct(paramMap);
                }else if("08".equals(param.getReqtype())){
                    bmsDctApprreqMapper.deleteUseApprreqCnsrv(paramMap);
                }else if("10".equals(param.getReqtype()) || "11".equals(param.getReqtype()) || "12".equals(param.getReqtype())){
                    bmsDctApprreqMapper.deleteUseApprreqTrans(paramMap);
                }

                //2. 권한 삭제
                bmsDctApprreqMapper.deleteUseAuthCard(paramMap);

                //3. 경로 삭제
                bmsDctApprreqMapper.deleteUseApprreqPath(paramMap);

                //4. 요청서 삭제
                res = bmsDctApprreqMapper.deleteUseApprreq(paramMap);
            }

        } catch (Exception e) {
            throw new AnyXException(e);
        }
        return res;
    }


    /* 각 요청서 상세화면 조회 */
    @Override
    public SelectUseReqViewDetailResVO selectUseReqViewDetail(SelectUseReqViewDetailReqVO param){
        SelectUseReqViewDetailResVO resSelectUseReqViewDetail = new SelectUseReqViewDetailResVO();
        try {
            // 1. 요청서 조회
            String[] reqTypes1 = {"01","02","03","06","07","08","09","10","11","12"};
            String[] reqTypes2 = {"04","05"};

            if (Arrays.asList(reqTypes1).contains(param.getReqtype())) {
                resSelectUseReqViewDetail = bmsDctApprreqMapper.selectUseReqViewDetail(param);
            } else if (Arrays.asList(reqTypes2).contains(param.getReqtype())) {
                resSelectUseReqViewDetail = bmsDctApprreqMapper.selectUseReqViewLendDetail(param);
            }

            // 2. 요청서 결재 경로 조회
            SelectUseReqViewPathListReqVO selectUseReqViewPathListParams = new SelectUseReqViewPathListReqVO();
            selectUseReqViewPathListParams.setApprreqid(param.getApprreqid());
            resSelectUseReqViewDetail.setPathList(bmsDctApprreqMapper.selectUseReqViewPathList(selectUseReqViewPathListParams));

            // 3. 요청된 문서 정보 조회 */
            /* reqtype
             * 01:열람, 02:재사용, 03:인쇄, 04:대출,05:지출,
             * 06:재분류, 07:파기, 08:존안, 09:이관, 10:이관대기
             */
            HashMap<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("apprreqid",param.getApprreqid());
            if("01".equals(param.getReqtype())){
                resSelectUseReqViewDetail.setMapList(bmsDctApprreqReadMapper.selectUseReqViewMapList(paramMap));
            }else if("02".equals(param.getReqtype())){
                resSelectUseReqViewDetail.setMapList(bmsDctApprreqReuseMapper.selectUseReqRecycleMapList(paramMap));
            }else if("03".equals(param.getReqtype())){
                resSelectUseReqViewDetail.setMapList(bmsDctApprreqPrtMapper.selectUseReqPrintMapList(paramMap));
            }else if("04".equals(param.getReqtype()) || "05".equals(param.getReqtype())){
                resSelectUseReqViewDetail.setMapList(bmsDctApprreqLendMapper.selectUseReqLendMapList(paramMap));
            }else if("06".equals(param.getReqtype())){
                resSelectUseReqViewDetail.setMapList(bmsDctApprreqReclassMapper.selectUseReqReclassMapList(paramMap));
            }else if("07".equals(param.getReqtype())){
                resSelectUseReqViewDetail.setMapList(bmsDctApprreqDestructMapper.selectUseReqDestructMapList(paramMap));
            }else if("08".equals(param.getReqtype())){
                resSelectUseReqViewDetail.setMapList(bmsDctApprreqCnsrvMapper.selectUseReqCnsrvMapList(paramMap));
            }else if("10".equals(param.getReqtype()) || "11".equals(param.getReqtype()) || "12".equals(param.getReqtype())){
                resSelectUseReqViewDetail.setMapList(bmsDctApprreqTransMapper.selectUseReqTransMapList(paramMap));
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resSelectUseReqViewDetail == null){
            resSelectUseReqViewDetail = new SelectUseReqViewDetailResVO();
        }
        return resSelectUseReqViewDetail;
    }


    /* 인쇄실행 팝업 > 인쇄한 부수 체크 */
    @Override
    public CheckPrtCntResVO checkPrtCnt(CheckPrtCntReqVO param){
        CheckPrtCntResVO checkPrtCntResVO;
        try {
            checkPrtCntResVO = bmsDctApprreqMapper.checkPrtCnt(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return checkPrtCntResVO;
    }

    /* 인쇄실행 팝업 > 인쇄부수 수정 */
    @Override
    public int updatePrtCnt(UpdatePrtCntReqVO param){
        int res = 0;
        try {
            res = bmsDctApprreqMapper.updatePrtCnt(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }

    /* 각 요청서 실행 처리 & 요청서회수 & 권한회수 */
    @Transactional
    @Override
    public int updateUseReqViewAppStatusBIA(UpdateUseReqViewAppApproveReqVO param){
        int res = 0;

        String reqtype = param.getReqtype();
        String savetype = param.getSavetype();
        String reqstatus = param.getReqstatus();
        String completeGubun = "";

        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyyMMddHHmmss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

        try {

            Boolean isPrtExecute = param.getReqtype().equals("03") && param.getReqstatus().equals("3"); // 인쇄요청 실행

            if(!isPrtExecute){
                res = bmsDctApprreqMapper.updateUseReqViewAppStatus(param);
            }

            // 열람
            if("01".equals(reqtype)){ //
                if("3".equals(reqstatus)){
                    updateRcvs(param, formatter.format(today), "insert");
                } else if ("4".equals(reqstatus)) {
                    updateRcvs(param, formatter.format(today), "delete");
                }

            // 인쇄
            } else if ("03".equals(reqtype) && "3".equals(reqstatus)) {
                List<UpdateUseReqPrintApproveReqVO> prtlist = param.getPrtlist();
                if(prtlist != null && !prtlist.isEmpty()){
                    for (UpdateUseReqPrintApproveReqVO prtParam : prtlist) {
                        if(param.getIsprintreq().equals("printReq")){
                            // 인쇄완료실행
                            prtParam.setPrtdt(formatter.format(today));
                            bmsDctApprreqMapper.updateUseExecutePrint(prtParam);
                            prtParam.setDestructreserve("prtComplete");
                            bmsDctApprreqMapper.updateApprreqDestructReserve(prtParam);
                            completeGubun = "prtComplete";
                        } else if (param.getIsprintreq().equals("destructReq")) {
                            // 파쇄완료실행
                            prtParam.setDestructdt(formatter.format(today));
                            bmsDctApprreqMapper.updateUseExecutePrintDestruct(prtParam);
                            prtParam.setDestructreserve("dstComplete");
                            bmsDctApprreqMapper.updateApprreqDestructReserve(prtParam);
                            param.setApprreqid(prtParam.getApprreqid());
                            res = bmsDctApprreqMapper.updateUseReqViewAppStatus(param);
                            updateAuthCardPrtDestructdt(param, formatter.format(today), prtParam.getDocid());
                            completeGubun = "dstComplete";
                        }
                    }
                }
            }

            //요청서 회수, 권한회수시 bms_dct_mgmt_regi secstatus MGT00 (이관연기, 내부이관 제외)
            if (("11".equals(reqtype) || "12".equals(reqtype)) && Objects.equals(savetype, "MGT62")) {
                updateSecStatus(param.getList(), "MGT62");
            } else if (!"11".equals(reqtype) && !"12".equals(reqtype) && Objects.equals(savetype, "MGT00")) {
                updateSecStatus(param.getList(), "MGT00");
            }


            //재분류실행
            if (Objects.equals(savetype, "reclasscomplete")) {
                bmsDctApprreqMapper.updateUseReqViewAppReclassCompleteRegiNew(param);
                bmsDctApprreqMapper.updateUseReqViewAppReclassCompleteRegiDel(param);
                List<HashMap<String, Object>> reclassList = param.getMaplist();
                for (HashMap<String, Object> reclass : reclassList) {
                    param.setApprreqid(param.getApprreqid().toString());
                    param.setDocid(reclass.get("docid").toString());
                    param.setMgmtid(reclass.get("mgmtid").toString());
                    bmsDctApprreqMapper.updateUseReqViewAppReclassCompleteAttr(param);
                }

            //파기실행
            }else if (Objects.equals(savetype, "destructcomplete")) {
                bmsDctApprreqMapper.updateUseReqViewAppDestructRegi(param);

            //존안실행
            }else if (Objects.equals(savetype, "cnsrvcomplete")) {
                bmsDctApprreqMapper.updateUseReqViewAppCnsrvRegi(param);

            //반출실행
            }else if (Objects.equals(savetype, "lendcomplete")) {
                HashMap<String, Object> lendparam = new HashMap<>();
                lendparam.put("apprreqid", param.getApprreqid());
                bmsDctApprreqLendMapper.updateLenddt(lendparam);

            //반납실행
            }else if (Objects.equals(savetype, "lendbackcomplete")) {
                HashMap<String, Object> lendparam = new HashMap<>();
                lendparam.put("apprreqid", param.getApprreqid());
                bmsDctApprreqLendMapper.updateLendBackdt(lendparam);
                List<HashMap<String, Object>> lendList = param.getMaplist();
                for (HashMap<String, Object> lend : lendList) {
                    updateAuthCardPrtDestructdt(param, formatter.format(today), lend.get("docid").toString());
                }

            //이관연기실행
            }else if (Objects.equals(savetype, "transwaitcomplete")) {
                List<UpdateUseReqViewAppApproveReqVO> list = param.getList();
                if(list != null && !list.isEmpty()){
                    for (UpdateUseReqViewAppApproveReqVO viewParam : list) {
                        viewParam.setSecstatus("MGT72");
                        bmsDctApprreqMapper.updateUseReqViewAppMgmtRegiSecstatus(viewParam);

                        HashMap<String, Object> transdelayParam = new HashMap<>();
                        transdelayParam.put("apprreqid", param.getApprreqid());
                        transdelayParam.put("mgmtid", viewParam.getMgmtid());
                        transdelayParam.put("secstatus", "MGT72");
                        bmsDctApprreqMapper.updateAppTransSecstatus(transdelayParam);
                    }
                }

            //내부이관실행
            }else if (Objects.equals(savetype, "transcomplete")) {
                List<UpdateUseReqViewAppApproveReqVO> list = param.getList();
                if(list != null && !list.isEmpty()){
                    for (int i = 0; i < list.size(); i++) {
                        UpdateUseReqViewAppApproveReqVO viewParam = list.get(i);
                        viewParam.setSecstatus("MGT82");
                        bmsDctApprreqMapper.updateUseReqViewAppMgmtRegiSecstatus(viewParam);
                        viewParam.setTransflag("Y");
                        bmsDctApprreqMapper.updateUseReqViewAppRdocTransFlag(viewParam);
                        bmsDctApprreqMapper.updateUseReqViewAppTransRegi(param);

                        HashMap<String, Object> transdelayParam = new HashMap<>();
                        transdelayParam.put("apprreqid", param.getApprreqid());
                        transdelayParam.put("mgmtid", viewParam.getMgmtid());
                        transdelayParam.put("secstatus", "MGT82");
                        bmsDctApprreqMapper.updateAppTransSecstatus(transdelayParam);

                        if(param.getMaplist().get(i).get("paperrecgubun") == null){
                            // 파일 이동(fileUpload -> transedFile)
                            transFiles(viewParam);
                        }
                    }
                }
            }

            // 2. bms_dct_hist_card 이력 저장
            updateHistory(param, formatter.format(today), param.getReqstatus().equals("5") ? param.getReqid() : param.getApprid(), completeGubun);

            // 3. 권한회수시 bms_dct_auth_card 업데이트 (열람/재사용/반출/인쇄 일 경우만)
            if (("01".equals(reqtype) || "02".equals(reqtype) || "03".equals(reqtype) || "04".equals(reqtype) || "05".equals(reqtype)) && "4".equals(reqstatus)) {
                updateAuthCard(param, formatter.format(today));
            }

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }

    // 내부이관 파일 위치 이동
    private void transFiles(UpdateUseReqViewAppApproveReqVO viewParam) throws IOException {
        AttachFileVO attachFileVO = new AttachFileVO();
        attachFileVO.setDocid(viewParam.getDocid());
        List<AttachFileVO> attachFileVOS = attachFileSbmsMapper.selectAttachFileByRecord(attachFileVO);
        if (attachFileVOS.isEmpty()) {
            return;
        }
        String destinationFilePath;
        Calendar cal = Calendar.getInstance();
        int year = cal.get (Calendar.YEAR);
        int month = cal.get (Calendar.MONTH) + 1 ;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        destinationFilePath = filePath + "/transedFile/"+ year + "/" + month + "/" + day + "/" + viewParam.getDocid();
        for(AttachFileVO fileVO : attachFileVOS){
            int lastSlashIndex = fileVO.getFlepath().lastIndexOf('/');
            if (lastSlashIndex >= 0) {
                lastSlashIndex = fileVO.getFlepath().lastIndexOf('/', lastSlashIndex - 1);
            }
            moveFile(fileVO.getFlepath().substring(0,lastSlashIndex), destinationFilePath);
            String originalPath = fileVO.getFlepath();
            String updatedPath = originalPath.replaceAll("fileUpload/(\\d+)/(\\d+)/(\\d+)", "transedFile/" + year + "/" + month + "/" + day);
            fileVO.setFlepath(updatedPath);
            attachFileSbmsMapper.updateAttachFilePath(fileVO);
        }
    }

    // 파일 이동
    private static void moveFile(String sourceFilePath, String destinationFilePath) throws IOException {
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationFilePath);
        if (!Files.exists(sourcePath)) {
            return;
        }
        try {
            Files.createDirectories(destinationPath.getParent());
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Error moving file from " + sourcePath + " to " + destinationFilePath, e);
        }
    }

    // 유저 정보 조회
    private GetUserLoginResVO getUserInfo(String userId) {
        GetUserLoginReqVO getUserLoginReqVO = new GetUserLoginReqVO();
        getUserLoginReqVO.setUserid(userId);
        return comUserinfoMapper.getUserLogin(getUserLoginReqVO);
    }


    //요청서 회수, 권한회수시 bms_dct_mgmt_regi secstatus MGT00 (이관연기, 내부이관 제외)
    private void updateSecStatus(List<UpdateUseReqViewAppApproveReqVO> list, String secStatus) {
        if (list != null && !list.isEmpty()) {
            for (UpdateUseReqViewAppApproveReqVO viewParam : list) {
                viewParam.setSecstatus(secStatus);
                bmsDctApprreqMapper.updateUseReqViewAppMgmtRegiSecstatus(viewParam);
            }
        }
    }

    // 열람요청 rdoc_rcvs 테이블 insert/delete
    private void updateRcvs(UpdateUseReqViewAppApproveReqVO param, String today, String type) {

        // 열람실행자 유저정보
        GetUserLoginResVO userinfo = getUserInfo(param.getApprid());

        List<HashMap<String, Object>> rcvsList = param.getMaplist();
        for (HashMap<String, Object> rcvsListParam : rcvsList) {

            if(type.equals("insert")){

                // 문서 작성자 정보
                GetDctMgmtRegiReqVO getDctMgmtRegiReqVO = new GetDctMgmtRegiReqVO();
                String mgmtid = rcvsListParam.get("mgmtid").toString();
                String[] mgmtidArray = mgmtid.split(",");
                getDctMgmtRegiReqVO.setMgmtid(mgmtidArray);
                List<GetDctMgmtRegiResVO> dctMgmtRegi = bmsDctMgmtRegiMapper.getDctMgmtRegi(getDctMgmtRegiReqVO);

                InsertDocIdBmsDctRdocRcvsListReqVO insertDocIdBmsDctRdocRcvsListParam = InsertDocIdBmsDctRdocRcvsListReqVO.builder()
                    .docid(rcvsListParam.get("docid").toString())
                    .userid(userinfo.getUserid())
                    .username(userinfo.getUsername())
                    .deptid(userinfo.getDeptid())
                    .deptname(userinfo.getDeptname())
                    .dutyname(userinfo.getGrade())
                    .inseq("0")
                    .indt(today)
                    .inid(dctMgmtRegi.get(0).getAuthorid())
                    .build();
                bmsDctRdocRcvsMapper.insertDocIdBmsDctRdocRcvsList(insertDocIdBmsDctRdocRcvsListParam);

            } else if (type.equals("delete")) {
                DeleteDocIdBmsDctRdocRcvsListReqVO deleteDocIdBmsDctRdocRcvsListParam =  DeleteDocIdBmsDctRdocRcvsListReqVO.builder()
                    .docid(rcvsListParam.get("docid").toString())
                    .userid(param.getReqid())
                    .build();
                bmsDctRdocRcvsMapper.deleteDocIdBmsDctRdocRcvsList(deleteDocIdBmsDctRdocRcvsListParam);
            }
        }
    }


    // bms_dct_hist_card 이력 저장
    private String histgubun;
    private String histstatus;

    private void updateHistory(UpdateUseReqViewAppApproveReqVO param, String today, String id, String completeGubun) {
        List<HashMap<String, Object>> list = param.getMaplist();

        for (HashMap<String, Object> listParam : list) {

            // 유저정보
            GetUserLoginResVO userinfo = getUserInfo(id);

            // 이력 정보
            String histSeq = bmsDctHistCardMapper.getHistSeq();
            setValues(param.getReqtype(), param.getReqstatus(), completeGubun);

            // 기타
            String etc = "";
            // 존안실행일 경우 승인존안기간을 etc 컬럼에 추가
            etc = param.getReqtype().equals("08") && param.getReqstatus().equals("3") ? listParam.get("grantcnsrvdt").toString().replaceAll("-", ".") : "";
            // 이관연기실행일 경우 연장된 이관년도를 etc 컬럼에 추가
            etc = param.getReqtype().equals("11") && param.getReqstatus().equals("3") ? listParam.get("granttransextdt").toString().substring(0,4) : "";

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
                    .etc(etc)
                    .build();
            bmsDctHistCardMapper.insertHistory(insertHistoryReqVO);
        }
    }

    private void setValues(String reqtype, String reqstatus, String completeGubun) {
        switch (reqtype) {
            case "01": setValues("05", "016", "017", "018", reqstatus, completeGubun); break; // 열람
            case "02": setValues("07", "016", "017", "018", reqstatus, completeGubun); break; // 재사용
            case "03": setValues("06", "016", "017", "018", reqstatus, completeGubun); break; // 인쇄
            case "04": setValues("24", "016", "017", "018", reqstatus, completeGubun); break; // 대출
            case "05": setValues("25", "016", "017", "018", reqstatus, completeGubun); break; // 지출
            case "06": setValues("16", "016", "017", "018", reqstatus, completeGubun); break; // 재분류
            case "07": setValues("20", "016", "017", "018", reqstatus, completeGubun); break; // 파기
            case "08": setValues("17", "016", "017", "018", reqstatus, completeGubun); break; // 존안
            case "10": setValues("28", "016", "017", "018", reqstatus, completeGubun); break; // 이관대기
            case "11": setValues("18", "016", "017", "018", reqstatus, completeGubun); break; // 이관연기
            case "12": setValues("29", "016", "017", "018", reqstatus, completeGubun); break; // 내부이관
        }
    }

    private void setValues(String gubun, String execute, String permissionRevoke, String reqRevoke, String reqstatus, String completeGubun) {
        histgubun = gubun;
        if(reqstatus.equals("3") && completeGubun.equals("")){ // 실행
            histstatus = execute;
        } else if (reqstatus.equals("3") && completeGubun.equals("prtComplete")) { // 인쇄 실행
            histstatus = "073";
        } else if (reqstatus.equals("3") && completeGubun.equals("dstComplete")) { // 파쇄 실행
            histstatus = "074";
        } else if (reqstatus.equals("7")) { // 반출 실행
            histstatus = "083";
        } else if (reqstatus.equals("8")) { // 반납 요청
            histstatus = "084";;
        } else if (reqstatus.equals("9")) { // 반납 실행
            histstatus = "085";;
        } else if (reqstatus.equals("4")) { // 권한 회수
            histstatus = permissionRevoke;
        }else if (reqstatus.equals("5")) { // 요청서 회수
            histstatus = reqRevoke;
        }
    }

    // 권한회수 시 bms_dct_auth_card 테이블 revoke 관련 컬럼 업데이트
    private void updateAuthCard(UpdateUseReqViewAppApproveReqVO param, String today){
        GetUserLoginResVO userinfo = getUserInfo(param.getApprid());

        List<HashMap<String, Object>> list = param.getMaplist();
        for (HashMap<String, Object> listParam : list) {
            UpdateAuthCardReqVO authCardReqVO = UpdateAuthCardReqVO.builder()
                    .apprreqid(param.getApprreqid())
                    .docid(listParam.get("docid").toString())
                    .authgrantflag("N")
                    .revokedt(today)
                    .revokeid(param.getApprid())
                    .revokename(userinfo.getUsername())
                    .build();
            bmsDctRdocMapper.updateAuthCard(authCardReqVO);
        }
    }


    // 파쇄실행 시 bms_dct_auth_card 테이블 prtdestrucdt 컬럼 업데이트
    private void updateAuthCardPrtDestructdt(UpdateUseReqViewAppApproveReqVO param, String destructdt, String docid){
        UpdateAuthCardReqVO authCardReqVO = UpdateAuthCardReqVO.builder()
                .apprreqid(param.getApprreqid())
                .docid(docid)
                .prtdestructdt(destructdt)
                .build();
        bmsDctRdocMapper.updateAuthCard(authCardReqVO);
    }

}
