/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.common.file.service;

import com.anylogic.ssystem.bms.dct.mapper.BmsDctFileMapper;
import com.anylogic.ssystem.bms.dct.mapper.BmsDctRdocMapper;
import com.anylogic.ssystem.bms.dct.model.ConvertPDFResVO;
import com.anylogic.ssystem.bms.dct.model.InsertTempListReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectDctFileReqVO;
import com.anylogic.ssystem.bms.dct.model.SelectDctFileResVO;
import com.anylogic.ssystem.bms.dct.service.ConvertPDFService;
import com.anylogic.ssystem.bms.sse.SSEController;
import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.common.file.mapper.AttachFileSbmsMapper;
import com.anylogic.ssystem.common.file.model.*;
import com.anylogic.ssystem.common.util.PropUtil;
import com.anylogic.ssystem.common.util.excel.ExcelRead;
import com.anylogic.ssystem.common.util.excel.ExcelReadOption;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Transactional
public class AttachFileSbmsService implements AttachFileService {

    @Value("${filePath}")
    private String filePath;

    @Value("${serverUrl}")
    private String serverUrl;


    @Autowired
    private AttachFileSbmsMapper attachFileMapper;

    @Autowired
    private BmsDctRdocMapper bmsDctRdocMapper;

    @Autowired
    private ConvertPDFService convertPDFService;

    @Autowired
    private SSEController sseController;

    @Autowired
    private BmsDctFileMapper bmsDctFileMapper;


    // @Autowired
    // private ExcelSVC excelSVC;

    private static final Logger logger = LoggerFactory.getLogger(AttachFileSbmsService.class);


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

    @Override
    public FileVO getHwpFile(String fileName, String flag) {
        int pos = fileName.lastIndexOf(".");
        String ext = fileName.substring(pos+1);

        String path = "";
        if(flag.equals("0")) {
            path = getDctFilePath(fileName).replace(filePath, "").replace(fileName, "");
        } else {
            path = getFormFilePath(fileName).replace(filePath, "").replace(fileName, "");
        }


        File file = new File(filePath + path + fileName);
        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            String contentType;
            // Set content type based on file extension
            Tika tika = new Tika();
            try {
                contentType = tika.detect(file);
            } catch (IOException e) {
                throw new RuntimeException("Error detecting content type", e);
            }

            return FileVO.builder().contentType(contentType).bytes(bytes).build();
//            return ImageVO.builder().contentType("image/"+ ext).bytes(bytes).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFilePath(String fileName){
        String filePath = attachFileMapper.getFilePath(fileName);

        return filePath;
    }

    public String getDctFilePath(String fileName){
        String filePath = attachFileMapper.getDctFilePath(fileName);

        return filePath;
    }

    public String getFormFilePath(String fileName){
        String filePath = attachFileMapper.getFormFilePath(fileName);

        return filePath;
    }


    @Override
    public List<AttachFileVO> selectAttachFileByRecord(AttachFileVO parameter)
    {
        return attachFileMapper.selectAttachFileByRecord(parameter);
    }

    @Override
    public AttachFileVO getAttachFile(AttachFileVO parameter)
    {
        return attachFileMapper.getAttachFile(parameter);
    }


    @Override
    @Transactional(propagation = Propagation.NESTED)
    public List<AttachFileVO> insertAttachFile(List<MultipartFile> mf, AttachFileVO data, String bizPath)
    {
        if (mf == null || mf.isEmpty()) {
            logger.warn("noParam list");
            return null;
        }

        String absolutePath = new File("").getAbsolutePath();
        String workPath = "";
        if (filePath == null || filePath.isEmpty()) {
            workPath =  absolutePath + "/src/main/webapp/public" + "/fileUpload";
        } else {
            workPath =  filePath + "/fileUpload";
        }

        List<AttachFileVO> rtnMap = new ArrayList<>();
//        int failCnt = -1;

        java.util.Calendar cal = java.util.Calendar.getInstance();
        int year = cal.get (Calendar.YEAR);
        int month = cal.get (Calendar.MONTH) + 1 ;
        int date = cal.get (Calendar.DATE) ;

        File dir;

//        List<IndexCommentVO> indexCommentList = data.getIndexComment();

        List<File> files = new ArrayList<>();

        try {
//            for(IndexCommentVO indexCommnet : indexCommentList) {
                for (int i = 0; i < mf.size(); i++) {
//                    if(indexCommnet.getFileIndex() != i)
//                        continue;
                    MultipartFile multipartFile = mf.get(i);

                    if (multipartFile != null && !multipartFile.isEmpty()) {
                        UUID uuid = UUID.randomUUID();

                        int pos = Objects.requireNonNull(multipartFile.getOriginalFilename()).lastIndexOf(".");
                        String ext = multipartFile.getOriginalFilename().substring(pos); // 이거는 . 포함 확장자만 ex) .jpg

                        String saveMiddlePath;
                        if (bizPath == null || bizPath.isEmpty()) {
//                            saveMiddlePath = /*data.getTableName() + "/" +*/ year + "/" + month;
                            saveMiddlePath = year + "/" + month + "/" + date + "/" + data.getDocid() + "/" + data.getInid();
                        } else {
                            saveMiddlePath = bizPath;
                        }

                        /* "http://localhost:8580"  "/public/fileUpload" + saveMiddlePath + "/" + uuid.toString() + ext;   조회 시점에 도메인 추가 */
                        String fileUrl = serverUrl + "/public/fileUpload" + "/" + uuid.toString() + ext;
                        String saveFullPath = workPath + "/" + saveMiddlePath + "/" + uuid + ext;

                        dir = new File(saveFullPath);

                        final boolean mkdirs;
                        if (!dir.isDirectory()) {
                            mkdirs = dir.mkdirs();
                        } else {
                            mkdirs = true;
                        }
                        if (mkdirs) {
                            File f = new File(saveFullPath);
                            files.add(f);
                            Path copyOfLocation = Paths.get(saveFullPath);

                            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);

                            AttachFileVO insertFileParam = new AttachFileVO();

                            String resSysGuid = bmsDctRdocMapper.getSysGuid();

                            insertFileParam.setDocid(data.getDocid());
                            insertFileParam.setGubun(data.getGubun());
                            insertFileParam.setIndt(data.getIndt());
                            insertFileParam.setInid(data.getInid());
                            insertFileParam.setOrigubun(data.getOrigubun());
                            insertFileParam.setOridocid(data.getOridocid());
                            insertFileParam.setFleid("FLE"+resSysGuid);
                            insertFileParam.setFlettl(uuid + ext);
                            insertFileParam.setFlepath(saveFullPath);
                            insertFileParam.setSfilename(multipartFile.getOriginalFilename());
                            insertFileParam.setFlesize(multipartFile.getSize());
                            insertFileParam.setFleorder(i);
                            insertFileParam.setFleurl(fileUrl);

                            logger.info("insertUserFileMulti.insertFileParam()  {}", insertFileParam);
                            int insertResult = attachFileMapper.insertAttachFile(insertFileParam);

                            rtnMap.add(insertFileParam);
                            sseController.progressCheck(Math.round((data.getProgressOffset() + (double)i + 1.0) / data.getTotal() * 100) + "%");
//                            sseController.progressCheck("(" + data.getProgressOffset() + " + " + i + " + 1.0) / " + data.getTotal() + " * 100" + "% insertfile");

                        } else {
                            logger.warn("fail to create directory.");
                        }
                    } // if
                } // for
//            }
        }
        catch(Exception e){
            for (File file : files) {
                file.delete();
            }
            throw new AnyXException(e);
        }
        return rtnMap;
    }



    @Override
    public List<AttachFileVO> updateAttachFile(List<MultipartFile> mf, AttachFileVO sendInfo, String bizPath)
   // ,  String atcFileId, HttpServletRequest request, HttpServletResponse response)
    {
        List<AttachFileVO> delFileList = sendInfo.getListDelFile();
        if(!delFileList.isEmpty()){
            for(AttachFileVO delFile : delFileList){
                this.deleteAttachFile(delFile);
            }
        }

        // 파라메터 없으면 null 반환
        if (mf == null || mf.isEmpty()) {
            logger.warn("noParam list");
            return null;
        }

        return this.insertAttachFile(mf, sendInfo, bizPath);
    }


    @Override
    public void deleteAttachFileByRecordDelYN(AttachFileVO recordInfo)
    {
        List<AttachFileVO> fileInfos = attachFileMapper.selectAttachFileByRecord(recordInfo);
        try {
            if (fileInfos.size() > 0) {
                for (AttachFileVO fileInfo : fileInfos)
                {
                    int res = attachFileMapper.deleteAttachFileDelYN(fileInfo);
                }
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
    }


    @Override
    public void deleteAttachFileByRecord(AttachFileVO recordInfo)
    {

        List<AttachFileVO> fileInfos = attachFileMapper.selectAttachFileByRecord(recordInfo);
        try {
            if (fileInfos.size() > 0) {
                for (AttachFileVO fileInfo : fileInfos)
                {
                    String fileFullPath = fileInfo.getFlepath();
                    File delFile = new File(fileFullPath);
                    delFile.delete();

                    int res = attachFileMapper.deleteAttachFile(fileInfo);
                }
            }
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
    }


    @Override
    public void deleteAttachFile(AttachFileVO sendInfo)
    {

        AttachFileVO fileInfo = attachFileMapper.getAttachFile(sendInfo);
        try {
            String fileFullPath = fileInfo.getFlepath();
            File delFile = new File(fileFullPath);
            delFile.delete();

            int res = attachFileMapper.deleteAttachFile(fileInfo);
        }
        catch(Exception e){
            throw new AnyXException(e);
        }
    }


    public void insertFileDataExcelUpload(File destFile) {
        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath(destFile.getAbsolutePath());
        excelReadOption.setOutputColumns("A", "B", "C", "D", "E", "F");
        excelReadOption.setStartRow(2);

        List<Map<String, String>> excelContent = ExcelRead.read(excelReadOption);

        for (Map<String, String> article : excelContent) {
            System.out.println(article.get("A"));
            System.out.println(article.get("B"));
            System.out.println(article.get("C"));
            System.out.println(article.get("D"));
            System.out.println(article.get("E"));
            System.out.println(article.get("F"));
        }
    }



    public List<Map<String, Object>> insertExcel (HttpServletRequest request) {
        List<Map<String, Object>> rtnMap = new ArrayList<>();
        File file;

        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> lmf = multipartRequest.getFiles("excelFile");

            if (!lmf.isEmpty()) { // if1
                for (MultipartFile mf : lmf) { // for1
                    if (!(mf == null || mf.isEmpty())) { // if2
                        logger.info("insertExcel().mf  {}", mf);

                        String tempExcelPath = PropUtil.getInstance().getValues("properties/serverpath.properties", "tempExcelPath");

                        int pos = Objects.requireNonNull(mf.getOriginalFilename()).lastIndexOf(".");
                        //String ext = mf.getOriginalFilename().substring(pos + 1); // 이거는 . 없이 확장자만 ex) jpg
                        String ext = mf.getOriginalFilename().substring(pos); // 이거는 . 포함 확장자만 ex) .jpg

                        UUID uuid = UUID.randomUUID();

                        String savePath = tempExcelPath + "\\";
                        String saveFullPath = savePath + uuid + ext;

                        file = new File(savePath);
                        final boolean mkdirs;
                        if (!file.isDirectory()) {
                            mkdirs = file.mkdirs();
                        }

                        mf.transferTo(new File(saveFullPath));

                        Map<String, Object> fileInfo = new HashMap<>();
                        fileInfo.put("excelPath", saveFullPath);
                        rtnMap.add(fileInfo);
                    } // if2
                } // for1
            } // if1

        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw new AnyXException(e);
        }

        return rtnMap;
    }

    public void fileDownload(HttpServletResponse response, AttachFileVO parameter) {
        AttachFileVO file = attachFileMapper.getAttachFile(parameter);

        if (file != null) {
            byte[] fileByte = new byte[0];
            try {
                fileByte = FileUtils.readFileToByteArray(new File((String) file.getFlepath()));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }

            try {
                response.setContentType("application/octet-stream");
                response.setContentLength(fileByte.length);
                response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode((String) file.getSfilename(), String.valueOf(StandardCharsets.UTF_8))+"\";");
                //response.setHeader("Content-Transfer-Encoding", "binary");
                response.getOutputStream().write(fileByte);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

//    @Override
    @Transactional(propagation = Propagation.NESTED)
    public List<AttachFileVO> copyFile(AttachFileVO data, String bizPath, String isreuse)
    {
        List<InsertTempListReqVO> fileList = data.getFileList();

        String absolutePath = new File("").getAbsolutePath();
        String workPath = "";
        if (filePath == null || filePath.isEmpty()) {
            workPath =  absolutePath + "/src/main/webapp/public" + "/fileUpload";
        } else {
            workPath =  filePath + "/fileUpload";
        }

        List<AttachFileVO> rtnMap = new ArrayList<>();
//        int failCnt = -1;

        java.util.Calendar cal = java.util.Calendar.getInstance();
        int year = cal.get (Calendar.YEAR);
        int month = cal.get (Calendar.MONTH) + 1 ;
        int date = cal.get (Calendar.DATE) ;

        File dir;
        List<File> files = new ArrayList<>();

        int i=0;
        int insertResult = 0;
        int insertCommFile = 0;
//        for (InsertTempListReqVO fileDetail : fileList) {
        for (int n=0; n<fileList.size(); n++) {
            InsertTempListReqVO fileDetail = fileList.get(n);

            UUID uuid = UUID.randomUUID();
            String filename = fileDetail.getSfilename();
            String ext = "";
            if (filename != null) {
                int pos = filename.lastIndexOf(".");
                if (pos > 0) {
                    ext = filename.substring(pos);
                }
            }

            String saveMiddlePath;
            if (bizPath == null || bizPath.isEmpty()) {
//                saveMiddlePath = /*data.getTableName() + "/" +*/ year + "/" + month;
                saveMiddlePath = /*data.getTableName() + "/" +*/ year + "/" + month + "_sabon";
            } else {
                saveMiddlePath = bizPath;
            }
            String fileUrl = serverUrl + "/public/fileUpload" + "/" + uuid.toString() + ext;
            String saveFullPath = workPath + "/" + saveMiddlePath + "/" + uuid + ext;
            dir = new File(saveFullPath);

            final boolean mkdirs;
            if (!dir.isDirectory()) {
                mkdirs = dir.mkdirs();
            } else {
                mkdirs = true;
            }
            if (mkdirs) {
                File f = new File(saveFullPath);
                files.add(f);
                Path copyOfLocation = Paths.get(saveFullPath);

                String sourceFilePath = fileDetail.getFlepath(); // 원본 파일 경로

                String resSysGuid = bmsDctRdocMapper.getSysGuid();

                AttachFileVO insertFileParam = new AttachFileVO();
                insertFileParam.setDocid(data.getDocid());
                insertFileParam.setGubun(data.getGubun());
                insertFileParam.setIndt(data.getIndt());
                insertFileParam.setInid(data.getInid());
                insertFileParam.setOrigubun(data.getOrigubun());
                insertFileParam.setOridocid(data.getOridocid());
                insertFileParam.setFleid("FLE"+resSysGuid);
                insertFileParam.setFlettl(uuid + ext);
                insertFileParam.setFlepath(saveFullPath);
                insertFileParam.setSfilename(fileList.get(i).getSfilename());
                insertFileParam.setFlesize(fileList.get(i).getFlesize().longValue());
                insertFileParam.setFleorder(i);
                insertFileParam.setFleurl(fileUrl);

                logger.info("insertUserFileMulti.insertFileParam()  {}", insertFileParam);

                SelectDctFileReqVO selectDctFileParam = new SelectDctFileReqVO();
                selectDctFileParam.setDocid(data.getDocid());
                List<SelectDctFileResVO> resSelectDctFile = bmsDctFileMapper.selectCommonDctFile(selectDctFileParam);
//                if(resSelectDctFile == null || resSelectDctFile.size() == 0){
                    insertResult = attachFileMapper.insertAttachFile(insertFileParam);
//                }

//

                rtnMap.add(insertFileParam);

                if(isreuse == null){
                    /* INSERT BMS_DCT_COMM_FILE */
                    CommFileVO insertCommFileParam = new CommFileVO();
                    insertCommFileParam.setDocid(data.getDocid());
                    insertCommFileParam.setDocgubun("A"); //A:발송 //E:시행
                    insertCommFileParam.setFileid(insertFileParam.getFleid());
                    insertCommFileParam.setFileseq(insertFileParam.getFleorder());
                    insertCommFileParam.setSfilename(insertFileParam.getSfilename());
                    insertCommFileParam.setFilegubun(insertFileParam.getGubun());
                    insertCommFileParam.setFilewidth(null);
                    insertCommFileParam.setFileheight(null);
                    insertCommFileParam.setFiletitle(insertFileParam.getFlettl());
                    insertCommFileParam.setFilesize(insertFileParam.getFlesize());
                    insertCommFileParam.setIndt(insertFileParam.getIndt());
                    insertCommFileParam.setInid(insertFileParam.getInid());
                    insertCommFileParam.setMarkflag("y");
                    insertCommFileParam.setOrigubun(insertFileParam.getOrigubun());
                    insertCommFileParam.setOridocid(insertFileParam.getOridocid());
                    insertCommFileParam.setIsviewtype(insertFileParam.getIsviewtype());
                    if(resSelectDctFile == null || resSelectDctFile.size() == 0) {
                        insertCommFile = attachFileMapper.insertCommFile(insertCommFileParam);
                    }
                }

                try {
                    // 파일 복사
                    Files.copy(Paths.get(sourceFilePath), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
                    sseController.progressCheck(Math.round((data.getProgressOffset() + (double)n + 1.0) / data.getTotal() * 100) + "%");
//                    sseController.progressCheck("(" + data.getProgressOffset() + " + " + n + " + 1.0) / " + data.getTotal() + " * 100" + "% copyfile");

                    String source = "";
                    int index = sourceFilePath.indexOf("/fileUpload");
                    if (index != -1) {
                        source = sourceFilePath.substring(index);
                    } else {
                        System.out.println(" !!!!!!!!!!!! 경로에 '/fileUpload'가 포함되어 있지 않습니다. !!!!!!!!");
                    }
                    String target = "/fileUpload" + "/" + saveMiddlePath + "/" + uuid + ".pdf";



                    // PDF 변환결과 응답 변수 선언
                    String responseStr = null;

//                    // 확장자 블랙리스트
//                    List<String> extensionsBlack = Arrays.asList("txt", "jpg");
//                    // 확장자 화이트리스트
//                    List<String> extensionsWhite = Arrays.asList("hwp");
                    List<String> extensionsWhite = Arrays.asList("");
//                    // 점(.)을 제거하고 확장자만 추출
                    String cleanExt = ext.startsWith(".") ? ext.substring(1) : ext;
//                    // 확장자가 목록에 있는지 확인
                    /* ---------- 확장자 확인하면서 할 경우 ---------- */
//                    if (extensionsWhite.contains(cleanExt)) {
//                        responseStr = convertPDFService.convertPDF(source, target);
//
//                        AttachFileVO existingParam = rtnMap.get(i);
//                        existingParam.setResponseStr(responseStr);
//                        rtnMap.set(i,existingParam);
//                    } else {
//////                        System.out.println("False: 확장자가 목록에 없음."); //카피만
//                        responseStr = "ok"; //JAYOO 변환 PDF API 기능 완료까지 임시처리로 대기(api 개발자 완료시 수정해야함)
//                        AttachFileVO existingParam = rtnMap.get(i);
//                        existingParam.setResponseStr(responseStr);
//
//                    }
                    /* ---------- 확장자 확인하면서 할 경우 ---------- */

                    /* API에 확장자 상관없이 모두 돌리는 경우 */
                    responseStr = convertPDFService.convertPDF(source, target);
                    AttachFileVO existingParam = rtnMap.get(i);
                    existingParam.setResponseStr(responseStr);
                    rtnMap.set(i,existingParam);
                    /* API에 확장자 상관없이 모두 돌리는 경우 */


//                    System.out.println("File copied successfully: " + sourceFilePath);
                } catch (Exception e) {
                    System.out.println("Error copying file: " + sourceFilePath);
                    for (File file : files) {
                        file.delete();
                    }
                    throw new AnyXException(e);
//                e.printStackTrace();
                }
                i++;
            }else {
                logger.warn("fail to create directory.");
            }
        } //for
        return rtnMap;
    }


}

