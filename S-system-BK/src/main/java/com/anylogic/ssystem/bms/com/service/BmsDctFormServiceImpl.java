/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.service;


import com.anylogic.ssystem.bms.com.model.*;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.BmsDctFormMapper;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;
import java.util.Map;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@Service("BmsDctFormService")
@Transactional
public class BmsDctFormServiceImpl implements BmsDctFormService {


    @Autowired
    private BmsDctFormMapper bmsDctFormMapper;


    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;


    /* 시스템관리 > 서식관리 > 서식 목록 조회 */
    @Override
    public PageInfo<SelectFormListResVO> selectFormList(SelectFormListReqVO param){
        PageInfo<SelectFormListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(bmsDctFormMapper.selectFormList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 시스템관리 > 서식관리 > 서식 삭제 */
    @Override
    public List<Integer> deleteForm(List<DeleteFormReqVO> params){
        List<Integer> res = new ArrayList<Integer>();
        try {
            for(DeleteFormReqVO param : params) {
                /* 삭제할 리스트 */
                List<DeleteFormSelectFileResVO> fileList = bmsDctFormMapper.deleteFormSelectFile(DeleteFormSelectFileReqVO.builder()
                                                                                                        .fileid(param.getFileid())
                                                                                                        .build());
                /* ============================ 이미지, 정보 삭제 ============================ */
                for(DeleteFormSelectFileResVO vo : fileList) {
                    Path filePath = Paths.get(vo.getFilepath());
                    Files.delete(filePath);

                    int ares = bmsDctFormMapper.deleteForm(param);
                    res.add(ares);
                }
            }
        }
        catch (NoSuchFileException | NullPointerException e) {
            log.debug("파일이 이미 존재하지 않거나 파일경로가 존재하지 않습니다.");
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return res;
    }


    /* 시스템관리 > 서식관리 > 서식 상세 조회 */
    @Override
    public GetFormDetailResVO getFormDetail(GetFormDetailReqVO param){
        log.debug("BmsDctForm getFormDetail" + param.toString());

        GetFormDetailResVO resGetFormDetail;
        try {
            resGetFormDetail = bmsDctFormMapper.getFormDetail(param);
            if(resGetFormDetail.getFileid() != null && !resGetFormDetail.getFileid().isEmpty()) {
                List<GetFormDetailSelectFileResVO> fileList = bmsDctFormMapper.getFormDetailSelectFile(GetFormDetailSelectFileReqVO.builder()
                                                                                                                    .fileid(param.getFileid())
                                                                                                                    .build());
                resGetFormDetail.setFilelist(fileList);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resGetFormDetail;
    }



    @Value("${filePath}")
    private String filePath;
    @Value("${serverUrl}")
    private String serverUrl;

    /* 시스템관리 > 서식관리 > 서식 등록 */
    @Override
    public int insertFormBIA(List<MultipartFile> mf, InsertFormBIAReqVO param){
        int resInsertFormBIA = 0;

        /* ======================================== 파일 업로드 ======================================== */
        if (mf == null || mf.isEmpty()) {
            log.debug("noParam list");
            return -1;
        }

        String absolutePath = new File("").getAbsolutePath();
        String workPath = "";
        if (filePath == null || filePath.isEmpty()) {
            workPath =  absolutePath + "/src/main/webapp/public" + "/fileUpload";
        } else {
            workPath =  filePath + "/fileUpload";
        }

        List<Map<String, Object>> rtnMap = new ArrayList<>();

        java.util.Calendar cal = java.util.Calendar.getInstance();
        int year = cal.get (Calendar.YEAR);
        int month = cal.get (Calendar.MONTH) + 1 ;
        int date = cal.get (Calendar.DATE) ;

        File dir;

        List<File> files = new ArrayList<>();

        try {
            for (int i = 0; i < mf.size(); i++) {
                MultipartFile multipartFile = mf.get(i);

                if (multipartFile != null && !multipartFile.isEmpty()) {
                    UUID uuid = UUID.randomUUID();

                    int pos = Objects.requireNonNull(multipartFile.getOriginalFilename()).lastIndexOf(".");
                    String ext = multipartFile.getOriginalFilename().substring(pos); // 이거는 . 포함 확장자만 ex) .jpg

                    String saveMiddlePath;
                    saveMiddlePath = /*data.get("tableName").toString() + "/" +*/ year + "/" + month + "/form";

                    /* "http://localhost:8580"  "/public/fileUpload" + saveMiddlePath + "/" + uuid + ext;   조회 시점에 도메인 추가 */
                    String fileUrl = serverUrl + "/public/fileUpload/form/" + uuid + ext;      // local 환경
//                    String fileUrl = serverUrl + "/api/public/fileUpload/" + uuid + ext;
                    String saveFullPath = workPath + "/" + saveMiddlePath;

                    dir = new File(saveFullPath);

                    final boolean mkdirs;
                    if (!dir.exists()) {
                        mkdirs = dir.mkdirs();
                    } else {
                        mkdirs = true;
                    }
                    if (mkdirs) {
                        saveFullPath = saveFullPath + "/" + uuid + ext;
                        File f = new File(saveFullPath);
                        files.add(f);
                        Path copyOfLocation = Paths.get(saveFullPath);

                        Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);

                        /* =================================================================================================== */

                        /* bms_dct_form 테이블 데이터 추가 */
                        // fileid가 존재하는 경우, 즉 update 동작의 경우 해당 블럭을 넘어간다.
                        if(param.getFileid() == null || param.getFileid().isEmpty()) {
                            String seq = bmsDctRdocMapper.getSysGuid();
                            param.setFileid("FLE" + seq);
                            param.setFormid("FOM" + seq);
                        }
                        param.setFormsize(new BigDecimal(multipartFile.getSize()));
                        param.setFileurl(fileUrl);
                        param.setFilepath(saveFullPath);
                        param.setFilename(uuid + ext);
                        resInsertFormBIA = bmsDctFormMapper.insertFormBIA(param);
                    } else {
                        log.debug("fail to create directory.");
                    }
                } // if
            } // for
        }
        catch(Exception e){
            for (File file : files) {
                file.delete();
            }
            throw new AnyXException(e);
        }
        return resInsertFormBIA;
    }



    /* 시스템관리 > 서식관리 > 서식 수정 */
    @Override
    public int updateFormBIA(List<MultipartFile> mf, UpdateFormBIAReqVO param){
        log.debug("bmsDctForm updateFormBIA" + param.toString());

        int resUpdateFormBIA = 0;
        try {
            /* ===================== 서식 이미지 정보 수정 : 삭제 후 재등록 ===================== */
            if(!param.getFileInfo().isEmpty()) {        // fileInfo가 빈 값 => 이전 이미지를 그대로 사용함.
                List<DeleteFormSelectFileResVO> fileList = bmsDctFormMapper.deleteFormSelectFile(DeleteFormSelectFileReqVO.builder()
                                                                                                            .fileid(param.getFileid())
                                                                                                            .build());
                for (DeleteFormSelectFileResVO vo : fileList) {
                    Path filePath = Paths.get(vo.getFilepath());
                    Files.delete(filePath);
                    bmsDctFormMapper.deleteForm(DeleteFormReqVO.builder()
                                                    .fileid(vo.getFileid())
                                                    .build());
                }

                InsertFormBIAReqVO data = InsertFormBIAReqVO.builder()
                                                .formtitle(param.getFormtitle())
                                                .formname(param.getFormname())
                                                .formsize(param.getFormsize())
                                                .formorder(param.getFormorder())
                                                .formcomment(param.getFormcomment())
                                                .deptid(param.getDeptid())
                                                .categoryid(param.getCategoryid())
                                                .updt(param.getUpdt())
                                                .upid(param.getUpid())
                                                .build();
                insertFormBIA(mf, data);
            }
            else {
                resUpdateFormBIA = bmsDctFormMapper.updateFormBIA(UpdateFormBIAReqVO.builder()
                                                                    .fileid(param.getFileid())
                                                                    .formtitle(param.getFormtitle())
                                                                    .categoryid(param.getCategoryid())
                                                                    .updt(param.getUpdt())
                                                                    .upid(param.getUpid())
                                                                    .formorder(param.getFormorder())
                                                                    .formcomment(param.getFormcomment())
                                                                    .build());
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateFormBIA;
    }


}
