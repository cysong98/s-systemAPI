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


import com.anylogic.ssystem.bms.com.mapper.ComOrgGwaninfoMapper;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;
import com.anylogic.ssystem.bms.com.mapper.ComOrganizationinfoMapper;
import com.anylogic.ssystem.bms.com.model.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.ComOrgDetailInfoMapper;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@Service("ComOrgDetailInfoService")
@Transactional
public class ComOrgDetailInfoServiceImpl implements ComOrgDetailInfoService {

    @Autowired
    private ComOrganizationinfoMapper comOrganizationinfoMapper;

    @Autowired
    private ComOrgDetailInfoMapper comOrgDetailInfoMapper;

    @Autowired
    private ComOrgGwaninfoMapper comOrgGwaninfoMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;

    /* 조직관리 > 서명인관리 > 서명인 삭제 */
    @Override
    public List<Integer> deleteSignBIA(List<DeleteSignBIAReqVO> params){
        List<Integer> res = new ArrayList<Integer>();
        try {
            for(DeleteSignBIAReqVO param : params) {
                List<DeleteSignBIASelectFileResVO> fileList = comOrgDetailInfoMapper.deleteSignBIASelectFile(DeleteSignBIASelectFileReqVO.builder()
                                                                                                                        .fileid(param.getFileid())
                                                                                                                        .build());
                for(DeleteSignBIASelectFileResVO vo : fileList) {
                    Path filePath = Paths.get(vo.getFilepath());
                    Files.delete(filePath);
                    comOrgDetailInfoMapper.deleteComImage(DeleteComImageReqVO.builder()
                                                                .fileid(vo.getFileid())
                                                                .build());
                }
                comOrgDetailInfoMapper.deleteSignBIA(param);
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


    /* 조직관리 > 서명인관리 > 서명인 등록 */
    @Override
    public int insertSignBIA(List<MultipartFile> multipartFiles, InsertSignBIAReqVO param){
        int resInsertSignBIA = 0;
        param.setIndt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")));
        try {
            /* 시퀀스 번호 획득, fileid 획득 */
            String fileid = "FLE" + bmsDctRdocMapper.getSysGuid();
            GetOrgDetailInfoSeqResVO seq = comOrgDetailInfoMapper.getOrgDetailInfoSeq(GetOrgDetailInfoSeqReqVO.builder()
                                                                                                .orgid(param.getOrgid())
                                                                                                .build());
            if(seq != null)
                param.setSeq(new BigDecimal(seq.getSeq()).add(BigDecimal.ONE));
            else
                param.setSeq(BigDecimal.ONE);

            param.setFileid(fileid);
            resInsertSignBIA = comOrgDetailInfoMapper.insertSignBIA(param);

            /* 서명인 이미지 정보 등록 */
            if (multipartFiles != null && !multipartFiles.isEmpty()) {
                InsertComImageReqVO data = InsertComImageReqVO.builder()
                                                        .fileid(fileid)
                                                        .indt(param.getIndt())
                                                        .inid(param.getInid())
                                                        .updt(param.getUpdt())
                                                        .upid(param.getUpid())
                                                        .build();
                insertComImage(data, multipartFiles, "", true);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resInsertSignBIA;
    }



    /* ========================== 파일 업로드 ========================== */
    @Value("${filePath}")
    private String filePath;
    @Value("${serverUrl}")
    private String serverUrl;
    public int insertComImage(InsertComImageReqVO data, List<MultipartFile> mf, String bizPath, boolean isSign){
        int resBmsDctFile = 0;
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
                    if (bizPath == null || bizPath.isEmpty()) {
                        saveMiddlePath = /*data.get("tableName").toString() + "/" +*/ year + "/" + month + "/";
                        if(isSign) saveMiddlePath += "sign";
                        else saveMiddlePath += "logo";
                    } else {
                        saveMiddlePath = bizPath;
                    }

                    /* "http://localhost:8580"  "/public/fileUpload" + saveMiddlePath + "/" + uuid + ext;   조회 시점에 도메인 추가 */
                    String fileUrl = serverUrl + "/public/fileUpload/com/" + uuid + ext;      // local 환경
//                    String fileUrl = serverUrl + "/api/public/fileUpload/com/" + uuid + ext;
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

                        /* com_image 테이블 데이터 추가 */
                        InsertComImageReqVO insertFileParam = InsertComImageReqVO.builder()
                                                                    .fileid(data.getFileid())
                                                                    .cfilename(uuid + ext)
                                                                    .sfilename(multipartFile.getOriginalFilename())
                                                                    .filesize(new BigDecimal(multipartFile.getSize()))
                                                                    .indt(data.getIndt())
                                                                    .inid(data.getInid())
                                                                    .updt(data.getUpdt())
                                                                    .upid(data.getUpid())
                                                                    .fileurl(fileUrl)
                                                                    .filepath(saveFullPath)
                                                                    .build();
                        resBmsDctFile = comOrgDetailInfoMapper.insertComImage(insertFileParam);
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
        return resBmsDctFile;
    }
    /* =============================================================== */


    /* 조직관리 > 서명인관리 > 서명인 수정 */
    @Override
    public int updateSignBIA(List<MultipartFile> multipartFiles, UpdateSignBIAReqVO param){
        log.debug("comOrgDetailInfo updateSignBIA" + param.toString());

        int resUpdateSignBIA = 0;
        param.setUpdt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")));
        try {
            /* ===================== 서명인 이미지 정보 수정 : 삭제 후 재등록 ===================== */
            // ***** Operation = updateSignImage
            if(!param.getFileInfo().isEmpty()) {        // fileInfo가 빈 값 => 이전 이미지 정보를 그대로 사용함.
                List<DeleteSignBIASelectFileResVO> fileList = comOrgDetailInfoMapper.deleteSignBIASelectFile(DeleteSignBIASelectFileReqVO.builder()
                                                                                                                        .fileid(param.getFileid())
                                                                                                                        .build());

                for (DeleteSignBIASelectFileResVO vo : fileList) {
                    Path filePath = Paths.get(vo.getFilepath());
                    Files.delete(filePath);
                    comOrgDetailInfoMapper.deleteComImage(DeleteComImageReqVO.builder()
                                                            .fileid(vo.getFileid())
                                                            .build());
                }

                InsertComImageReqVO data = InsertComImageReqVO.builder()
                                                            .fileid(param.getFileid())
                                                            .indt(param.getIndt())
                                                            .inid(param.getInid())
                                                            .updt(param.getUpdt())
                                                            .upid(param.getUpid())
                                                            .build();
                insertComImage(data, multipartFiles, "", true);
            }
            /* ============================================================================ */

            /* 서명인 이미지 정보 수정 */
            // ***** Operation = updateSignImage
            resUpdateSignBIA = comOrgDetailInfoMapper.updateSignBIA(param);

        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resUpdateSignBIA;
    }





    /* ============================= 심볼/로고 ============================= */

    /* 조직관리 > 심볼/로고관리 > 심볼/로고 삭제 */
    @Override
    public List<Integer> deleteLogoBIA(List<DeleteLogoBIAReqVO> params){
        List<Integer> res = new ArrayList<Integer>();
        try {
            for(DeleteLogoBIAReqVO param : params) {
                List<DeleteLogoBIASelectFileResVO> fileList = comOrgDetailInfoMapper.deleteLogoBIASelectFile(DeleteLogoBIASelectFileReqVO.builder()
                                                                                                                        .fileid(param.getFileid())
                                                                                                                        .build());
                for(DeleteLogoBIASelectFileResVO vo : fileList) {
                    Path filePath = Paths.get(vo.getFilepath());
                    Files.delete(filePath);
                    comOrgDetailInfoMapper.deleteComImage(DeleteComImageReqVO.builder()
                                                                .fileid(vo.getFileid())
                                                                .build());
                }
                comOrgDetailInfoMapper.deleteLogoBIA(param);
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


    /* 조직관리 > 심볼/로고관리 > 심볼/로고 등록 */
    @Override
    public int insertLogoBIA(List<MultipartFile> multipartFiles, InsertLogoBIAReqVO param){
        int resInsertLogoBIA = 0;
        param.setIndt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        try {
            /* 시퀀스 번호 획득, fileid 획득 */
            String fileid = "FLE" + bmsDctRdocMapper.getSysGuid();
            GetOrgDetailInfoSeqResVO seq = comOrgDetailInfoMapper.getOrgDetailInfoSeq(GetOrgDetailInfoSeqReqVO.builder()
                                                                                                .orgid(param.getOrgid())
                                                                                                .build());
            if(seq != null)
                param.setSeq(new BigDecimal(seq.getSeq()).add(BigDecimal.ONE));
            else
                param.setSeq(BigDecimal.ONE);

            param.setFileid(fileid);
            resInsertLogoBIA = comOrgDetailInfoMapper.insertLogoBIA(param);

            /* 로고 이미지 정보 등록 */
            if (multipartFiles != null && !multipartFiles.isEmpty()) {
                InsertComImageReqVO data = InsertComImageReqVO.builder()
                                                        .fileid(fileid)
                                                        .indt(param.getIndt())
                                                        .inid(param.getInid())
                                                        .updt(param.getUpdt())
                                                        .upid(param.getUpid())
                                                        .build();
                insertComImage(data, multipartFiles, "", false);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resInsertLogoBIA;
    }


    /* 조직관리 > 심볼/로고관리 > 심볼/로고 수정 */
    @Override
    public int updateLogoBIA(List<MultipartFile> multipartFiles, UpdateLogoBIAReqVO param){
        int resUpdateLogoBIA = 0;
        param.setUpdt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        try {
            /* ===================== 로고 이미지 정보 수정 : 삭제 후 재등록 ===================== */
            // ***** Operation = updateLogoImage
            if(!param.getFileInfo().isEmpty()) {        // fileInfo가 빈 값 => 이전 이미지 정보를 그대로 사용함.
                List<DeleteLogoBIASelectFileResVO> fileList = comOrgDetailInfoMapper.deleteLogoBIASelectFile(DeleteLogoBIASelectFileReqVO.builder()
                                                                                                                        .fileid(param.getFileid())
                                                                                                                        .build());
                for (DeleteLogoBIASelectFileResVO vo : fileList) {
                    Path filePath = Paths.get(vo.getFilepath());
                    Files.delete(filePath);
                    comOrgDetailInfoMapper.deleteComImage(DeleteComImageReqVO.builder()
                                                                .fileid(vo.getFileid())
                                                                .build());
                }
                InsertComImageReqVO data = InsertComImageReqVO.builder()
                                                .fileid(param.getFileid())
                                                .indt(param.getIndt())
                                                .inid(param.getInid())
                                                .updt(param.getUpdt())
                                                .upid(param.getUpid())
                                                .build();
                insertComImage(data, multipartFiles, "", false);
            }
            /* ============================================================================ */
            /* 로고 이미지 정보 수정 */
            resUpdateLogoBIA = comOrgDetailInfoMapper.updateLogoBIA(param);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateLogoBIA;
    }
}
