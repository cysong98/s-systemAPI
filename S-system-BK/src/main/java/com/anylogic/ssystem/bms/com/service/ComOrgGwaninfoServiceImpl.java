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


import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;
import com.anylogic.ssystem.bms.com.model.*;
import com.anylogic.ssystem.common.file.model.ImageVO;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.bms.com.mapper.ComOrgGwaninfoMapper;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.anylogic.ssystem.bms.com.mapper.ComOrgDetailInfoMapper;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@Service("ComOrgGwaninfoService")
@Transactional
public class ComOrgGwaninfoServiceImpl implements ComOrgGwaninfoService {

    @Autowired
    private ComOrgGwaninfoMapper comOrgGwaninfoMapper;

    @Autowired
    private ComOrgDetailInfoMapper comOrgDetailInfoMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;


    /* 조직관리 > 관인관리 > 관인 목록 */
    @Override
    public PageInfo<SelectGwanListResVO> selectGwanList(SelectGwanListReqVO param){
        PageInfo<SelectGwanListResVO> resultListVO = null;
        try {
            PageHelper.startPage(param);
            resultListVO = PageInfo.of(comOrgGwaninfoMapper.selectGwanList(param));
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resultListVO;
    }


    /* 조직관리 > 관인관리 > 관인 삭제 */
    @Override
    public List<Integer> deleteGwanBIA(List<DeleteGwanBIAReqVO> params){
        log.debug("comOrgGwaninfo deleteGwanBIA" + params.toString());

        List<Integer> res = new ArrayList<Integer>();
        try {
            for(DeleteGwanBIAReqVO param : params) {
                /* com_image 삭제할 리스트 */
                List<DeleteGwanBIASelectFileResVO> fileList = comOrgGwaninfoMapper.deleteGwanBIASelectFile(DeleteGwanBIASelectFileReqVO.builder()
                                                                                                                        .fileid(param.getFileid())
                                                                                                                        .build());
                /* ============================ 관인 이미지, 정보 삭제 ============================ */
                for(DeleteGwanBIASelectFileResVO vo : fileList) {
                    Path filePath = Paths.get(vo.getFilepath());
                    Files.delete(filePath);
                    comOrgGwaninfoMapper.deleteComImage(DeleteComImageReqVO.builder()
                                                                .fileid(vo.getFileid())
                                                                .build());
                }
                /* ============================================================================ */

                /* ============================= 관인 관련 정보 삭제 ============================= */
                comOrgDetailInfoMapper.deleteGwanOrgDetail(DeleteGwanOrgDetailReqVO.builder()
                                                                    .orgid(param.getOrgid())
                                                                    .seq(param.getSeq())
                                                                    .build());
                int ares = comOrgGwaninfoMapper.deleteGwanBIA(param);
                res.add(ares);
                /* ============================================================================ */
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


    /* 조직관리 > 관인관리 > 관인 상세 조회 */
    @Override
    public GetGwanDetailResVO getGwanDetail(GetGwanDetailReqVO param){
        GetGwanDetailResVO resGetGwanDetail;
        try {
            resGetGwanDetail = comOrgGwaninfoMapper.getGwanDetail(param);
            if(resGetGwanDetail != null && resGetGwanDetail.getFileid() != null && !resGetGwanDetail.getFileid().isEmpty()) {
                List<GetGwanDetailSelectFileResVO> fileList = comOrgGwaninfoMapper.getGwanDetailSelectFile(GetGwanDetailSelectFileReqVO.builder()
                                                                                                            .fileid(resGetGwanDetail.getFileid())
                                                                                                            .build());
                resGetGwanDetail.setFilelist(fileList);
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resGetGwanDetail;
    }

    /* 조직관리 > 관인관리 > 관인 등록 */
    @Override
    public int insertGwanBIA(List<MultipartFile> multipartFiles, InsertGwanBIAReqVO param){
        int resInsertGwanBIA = 0;
        param.setIndt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")));
        if(param.getFirstdt() != null && !param.getFirstdt().equals(""))
            param.setFirstdt(param.getFirstdt().replaceAll("-", "") + "000000");
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
            resInsertGwanBIA = comOrgGwaninfoMapper.insertGwanBIA(param);
            System.out.println();

            /* 조직 관인 관련 정보 등록 */
            int resInsertGwanOrgDetail = comOrgDetailInfoMapper.insertGwanOrgDetail(InsertGwanOrgDetailReqVO.builder()
                                                                                            .orgid(param.getOrgid())
                                                                                            .seq(param.getSeq())
                                                                                            .managegubun(param.getManagegubun())
                                                                                            .fileid(fileid)
                                                                                            .etc(param.getEtc())
                                                                                            .isuse(param.getIsuse())
                                                                                            .isdefault(param.getIsdefault())
                                                                                            .inid(param.getInid())
                                                                                            .indt(param.getIndt())
                                                                                            .build());
            /* 관인 이미지 정보 등록 */
            if (multipartFiles != null && !multipartFiles.isEmpty()) {
                InsertComImageReqVO data = InsertComImageReqVO.builder()
                                                        .fileid(fileid)
                                                        .filewidth(param.getFilewidth())
                                                        .fileheight(param.getFileheight())
                                                        .indt(param.getIndt())
                                                        .inid(param.getInid())
                                                        .updt(param.getUpdt())
                                                        .upid(param.getUpid())
                                                        .build();
                insertComImage(data, multipartFiles, "");
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }

        return resInsertGwanBIA;
    }


    /* ========================== 파일 업로드 ========================== */
    @Value("${filePath}")
    private String filePath;
    @Value("${serverUrl}")
    private String serverUrl;
    public int insertComImage(InsertComImageReqVO data, List<MultipartFile> mf, String bizPath){
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
                        saveMiddlePath = /*data.get("tableName").toString() + "/" +*/ year + "/" + month + "/gwanin";
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
                                                                    .filewidth(data.getFilewidth())
                                                                    .fileheight(data.getFileheight())
                                                                    .indt(data.getIndt())
                                                                    .inid(data.getInid())
                                                                    .updt(data.getUpdt())
                                                                    .upid(data.getUpid())
                                                                    .fileurl(fileUrl)
                                                                    .filepath(saveFullPath)
                                                                    .build();
                        resBmsDctFile = comOrgGwaninfoMapper.insertComImage(insertFileParam);
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


    /* 조직관리 > 관인관리 > 관인 수정 */
    @Override
    public int updateGwanBIA(List<MultipartFile> multipartFiles, UpdateGwanBIAReqVO param){
        int resUpdateGwanBIA = 0;
        param.setUpdt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")));
        if(param.getFirstdt() != null && !param.getFirstdt().equals(""))
            param.setFirstdt(param.getFirstdt().replaceAll("-", "") + "000000");
        try {
            /* ===================== 관인 이미지 정보 수정 : 삭제 후 재등록 ===================== */
            if(!param.getFileInfo().isEmpty()) {        // fileInfo가 빈 값 => 이전 이미지를 그대로 사용함.
                List<DeleteGwanBIASelectFileResVO> fileList = comOrgGwaninfoMapper.deleteGwanBIASelectFile(DeleteGwanBIASelectFileReqVO.builder()
                                                                                                                        .fileid(param.getFileid())
                                                                                                                        .build());

                for (DeleteGwanBIASelectFileResVO vo : fileList) {
                    Path filePath = Paths.get(vo.getFilepath());
                    Files.delete(filePath);

                    comOrgGwaninfoMapper.deleteComImage(DeleteComImageReqVO.builder()
                            .fileid(vo.getFileid())
                            .build());
                }

                InsertComImageReqVO data = InsertComImageReqVO.builder()
                                                .fileid(param.getFileid())
                                                .filewidth(param.getFilewidth())
                                                .fileheight(param.getFileheight())
                                                .indt(param.getIndt())
                                                .inid(param.getInid())
                                                .updt(param.getUpdt())
                                                .upid(param.getUpid())
                                                .build();
                insertComImage(data, multipartFiles, "");
            }
            /* ============================================================================ */

            /* ============================= 관인 관련 정보 수정 ============================= */
            int resInsertGwanOrgDetail = comOrgDetailInfoMapper.updateGwanOrgDetail(UpdateGwanOrgDetailReqVO.builder()
                                                                                            .orgid(param.getOrgid())
                                                                                            .seq(param.getSeq())
                                                                                            .managegubun(param.getManagegubun())
                                                                                            .fileid(param.getFileid())
                                                                                            .etc(param.getEtc())
                                                                                            .upid(param.getUpid())
                                                                                            .updt(param.getUpdt())
                                                                                            .build());
            resUpdateGwanBIA = comOrgGwaninfoMapper.updateGwanBIA(param);
            /* ============================================================================ */
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
        return resUpdateGwanBIA;
    }


    /* 관인/서명인 이미지 조회 */
    @Override
    public ImageVO getImage(String fileName) {
        int pos = fileName.lastIndexOf(".");
        String ext = fileName.substring(pos+1);
        String path = getFilePath(fileName).replace(filePath, "").replace(fileName, "");

        File file = new File(filePath + path + fileName);
        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            return ImageVO.builder().contentType("image/"+ ext).bytes(bytes).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* 파일 경로 조회 */
    public String getFilePath(String fileName){
        return comOrgGwaninfoMapper.getFilePath(fileName);
    }
}
