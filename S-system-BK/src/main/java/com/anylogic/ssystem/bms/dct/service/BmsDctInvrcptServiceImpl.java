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


import com.anylogic.ssystem.bms.dct.mapper.BmsDctEnfDocMapper;
import com.anylogic.ssystem.bms.dct.model.*;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
import com.anylogic.ssystem.common.file.service.AttachFileSbmsService;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctInvrcptMapper;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;



@Slf4j
@RequiredArgsConstructor
@Service("BmsDctInvrcptService")
@Transactional
public class BmsDctInvrcptServiceImpl implements BmsDctInvrcptService {


    @Autowired
    private BmsDctInvrcptMapper bmsDctInvrcptMapper;
    @Autowired
    private BmsDctEnfDocMapper bmsDctEnfDocMapper;
    @Autowired
    private AttachFileSbmsService attachFileService;

    /* 송증/접수증 상세내역 조회 */
    @Override
    public SelectReceiptResVO selectReceipt(SelectReceiptReqVO param){
        SelectReceiptResVO resSelectReceipt;
        try {
            resSelectReceipt = bmsDctInvrcptMapper.selectReceipt(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        if(resSelectReceipt == null){
            resSelectReceipt = new SelectReceiptResVO();
        }
        return resSelectReceipt;
    }

    public int insertReceipt(SelectReceiptReqVO param){
        int resInsertReceipt = 0;
        try {
            /* 송증/영수증 저장 */
            //bms_dct_invrcpt
            SelectReceiptResVO insertInvrcptParam = new SelectReceiptResVO();
            resInsertReceipt = bmsDctInvrcptMapper.insertReceipt(insertInvrcptParam);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resInsertReceipt;
    }

    /* 오프라인발송문서의 접수증을 저장하고 연계동작하는 메서드 */
    @Override
    @Transactional
    public int insertRecvInvrcpt(InsertRecvInvrcptReqVO param, List<MultipartFile> refAttach){
        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초 출력
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

        int resinsertRecvInvrcpt = 0;
        try {
            /* 01. 오프라인발송문서의 접수에 대한 상태값 및 접수정보 업데이트 */
            // Update bms_dct_enf_doc
            UpdateEnfDocRepathReqVO updateEnfDocRepathReqParam = getEnfDocVO(param);
            updateEnfDocRepathReqParam.setUpdt(param.getSnddt());
            updateEnfDocRepathReqParam.setPaperdocflag("Y"); //종이문서.(오프라인발송경우 인쇄하여 발송함)
            updateEnfDocRepathReqParam.setInid(param.getAuthorid()); //오프라인발송은 Inid를 발송자ID로 넣었음.(온라인접수시엔 접수자 아이디가 들어가도록 했음)
            int updateEnfDocRepath = bmsDctEnfDocMapper.updateEnfDocRepath(updateEnfDocRepathReqParam);


            /* 02. 송증/영수증 저장 */
            // Insert bms_dct_invrcpt
            SelectReceiptResVO insertInvrcptParam = getInvrcptVO(param);
            insertInvrcptParam.setRecdeptname(param.getRcvmininame());
            insertInvrcptParam.setSndid(param.getAuthorid());
            insertInvrcptParam.setSndminiid(param.getSnddeptname());
            insertInvrcptParam.setSndmininame(param.getAuthordeptid());
            insertInvrcptParam.setSnddeptid  (param.getAuthordeptid());
            insertInvrcptParam.setSnddeptname(param.getAuthordeptname());
            insertInvrcptParam.setRgstid 	 (param.getAuthorid()); //오프라인발송시 수신자id를 알 수 없는 경우가 있는데, 수신자의 아이디대신 발송자(접수증 저장유저)의 아이디를 넣을 경우.
//            insertInvrcptParam.setRgstid 	 (param.getAcceptname());//오프라인발송시 수신자id를 알 수 없을때도 있는데, 수신자의 아이디대신 수신자의 이름을 넣을 경우.
//            insertInvrcptParam.setRgstid 	 (param.getRgstid());//오프라인발송시 수신자id를 알 수 없을때도 있음
            insertInvrcptParam.setRgstname	 (param.getAuthorname());
            insertInvrcptParam.setRgstdt 	 (param.getSnddt());
//            insertInvrcptParam.setReceiverid  (param.get());//오프라인발송시 수신부서 id를 알 수 없는 경우가 있음.



            insertInvrcptParam.setFailreason ("OffLineSend");
            insertInvrcptParam.setInvrcpttype("NF"); //invrcpttype("NN") //송증영수증 형태 //NN:온라인 NF:오프라인
            insertInvrcptParam.setReceiverid (""); //알 수 없는 경우로 인하여 비워둠.
            insertInvrcptParam.setRcvname	 (param.getAcceptname());
            insertInvrcptParam.setRcvdutyname(param.getAcceptdutyname());
            insertInvrcptParam.setRcvdt      (param.getSnddt());
            int insertReceipt = bmsDctInvrcptMapper.insertReceipt(insertInvrcptParam);
            resinsertRecvInvrcpt = updateEnfDocRepath + insertReceipt;

            /* 접수증 이미지 첨부 */
            // Insert bms_dct_file
            AttachFileVO refFileVO = getFileVO(param, formatter.format(today));
            refFileVO.setGubun("DFT23"); //(접수증 사진)
            List<AttachFileVO> refFileInfos = attachFileService.insertAttachFile(refAttach, refFileVO);

        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resinsertRecvInvrcpt;
    }

    private UpdateEnfDocRepathReqVO getEnfDocVO(InsertRecvInvrcptReqVO vo) {
        UpdateEnfDocRepathReqVO build = UpdateEnfDocRepathReqVO.builder()
                .enfdocid(vo.getEnfdocid())
                .inid(vo.getInid()) //접수처리하는 관리자 유저id
//                .chargerid(vo.getObjid()) //담당정보의 유저id
                .docstatus(vo.getDocstatus())//비밀상태
                .moveflag("N")
                .docid(vo.getDocid())
                .enfgubun("N") //대내
                .distributeflag("N") //비배부 (배부처리하면 배부 Y)
                .emergency(vo.getEmergency())
                .doctitle(vo.getDoctitle())
                .acceptdocseq(vo.getAcceptdocseq())
                .acceptdeptid  (vo.getAcceptdeptid())
                .acceptdeptname(vo.getAcceptdeptname())
                .acceptid      (vo.getAcceptid())
                .acceptname    (vo.getAcceptname())
                .acceptdutyname(vo.getAcceptdutyname())
//                .updt(currentDateTime)
                .updt(vo.getUpdt())
                .build();
        return build;
    }

    private SelectReceiptResVO getInvrcptVO(InsertRecvInvrcptReqVO vo) {
        SelectReceiptResVO build = SelectReceiptResVO.builder()
                .docid          (vo.getDocid() )
                .copyno         (vo.getCopyno() )
                .invrcpttl      (vo.getInvrcpttl() )
                .seclevel       (vo.getSeclevel() )
                .mgmtno         (vo.getMgmtno() )
                .docno          (vo.getDocno() )
                .recdeptname    (vo.getRecdeptname())
                .refdeptname    (vo.getRefdeptname() )
                .sndminiid      (vo.getSndminiid())
                .sndmininame    (vo.getSndmininame() )
                .snddeptid      (vo.getSnddeptid() )
                .snddeptname    (vo.getSnddeptname() )
                .sndid          (vo.getSndid() )
                .sndname        (vo.getSndname() )
                .snddutyname    (vo.getSnddutyname() )
                .snddt          (vo.getSnddt() )
                .sndelectsignid (vo.getSndelectsignid() )
                .rcvminiid      (vo.getRcvminiid() )
                .rcvmininame    (vo.getRcvmininame() )
                .rcvdeptid      (vo.getRcvdeptid() )
                .rcvdeptname    (vo.getRcvdeptname() )
                .rcvid          (vo.getRcvid() )
                .rcvname        (vo.getRcvname() )
                .rcvdutyname    (vo.getRcvdutyname() )
                .rcvdt          (vo.getRcvdt() )
                .rcvelectsignid (vo.getRcvelectsignid() )
                .sndrcvdocid    (vo.getSndrcvdocid() )
                .failreason     (vo.getFailreason() )
                .qty            (vo.getQty() )
                .invrcpttype    (vo.getInvrcpttype() )
                .rgstid         (vo.getRgstid() )
                .rgstname       (vo.getRgstname() )
                .rgstdt         (vo.getRgstdt() )
                .updtid         (vo.getUpdtid() )
                .updtname       (vo.getUpdtname() )
                .updtdt         (vo.getUpdtdt() )
                .sndverifyfileid(vo.getSndverifyfileid() )
                .rcvverifyfileid(vo.getRcvverifyfileid() )
                .receiverid     (vo.getReceiverid() )
                .acceptid       (vo.getAcceptid() )
                .acceptname     (vo.getAcceptname() )
                .acceptdutyname (vo.getAcceptdutyname() )
                .acceptdeptname	(vo.getAcceptdeptname() )
                .build();
        return build;
    }

    private AttachFileVO getFileVO (InsertRecvInvrcptReqVO vo, String indt){
        AttachFileVO build = AttachFileVO.builder()
                .docid(vo.getDocid())
                .indt(indt)
                .inid(vo.getResuserid())
//                .origubun(vo.getGubun())
//                .oridocid(vo.getResdocid())
                .build();
        return build;
    }

}
