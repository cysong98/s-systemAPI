/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.controller;

import com.anylogic.ssystem.bms.dct.service.AnyViewerReqService;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@Api(tags = {"dct"})
@RestController
@RequestMapping("/anyviewer")
public class AnyViewerController {

    @Autowired
    private AnyViewerReqService anyViewerReqService;

    /* HWP 파일을 PDF로 변환 */
    @PostMapping("/hwpconvertpdf")
    public ResponseEntity convertHwpToPdf
    (
            @Valid AttachFileVO hwpFile
    ) throws Exception
    {
        log.debug("Converting HWP to PDF: " + hwpFile.toString());

        String pdfFilePath = anyViewerReqService.convertHwpToPdf(hwpFile);
        return ResponseEntity.ok(pdfFilePath);
    }


    public class TextToPdfController {
        // 텍스트 파일을 PDF로 변환하는 로직을 구현합니다.
        // ...
    }


    public class DocToPdfController {
        // DOC 파일을 PDF로 변환하는 로직을 구현합니다.
        // ...
    }

    @PostMapping("/hwpconvertpdf_")
    public ResponseEntity convertHwpToPdf_
            (
                    @Valid AttachFileVO hwpFile
            ) throws Exception
    {
        log.debug("Converting HWP to PDF: " + hwpFile.toString());

        String pdfFilePath = anyViewerReqService.convertHwpToPdf(hwpFile);
        return ResponseEntity.ok(pdfFilePath);
    }


    @PostMapping("/convertHwpToHtml")
    public ResponseEntity convertHwpToHtml
    (
            @Valid AttachFileVO hwpFile
    ) throws Exception
    {
        String pdfFilePath = anyViewerReqService.convertHwpToHtml(hwpFile);
        return ResponseEntity.ok(pdfFilePath);
    }

    @PostMapping("/convertHtmlToPdf")
    public ResponseEntity convertHtmlToPdf
            (
                    @Valid AttachFileVO hwpFile
            ) throws Exception
    {
        String pdfFilePath = anyViewerReqService.convertHtmlToPdf(hwpFile);
        return ResponseEntity.ok(pdfFilePath);
    }

    @PostMapping("/convertTextToPdf")
    public ResponseEntity convertTextToPdf
            (
                    @Valid AttachFileVO file
            ) throws Exception
    {
        String pdfFilePath = anyViewerReqService.convertTextToPdf(file);
        return ResponseEntity.ok(pdfFilePath);
    }

    @PostMapping("/convertPptToImages")
    public ResponseEntity convertPptToImages
            (
//                    @Valid AttachFileVO file
//                    @Valid String pptFilePath = "path_to_your_ppt_file.pptx";
//                    @Valid String outputDirectory = "//172.18.18.29/share/fileUpload/2024/3_sabon/images";
                    @Valid String pptFilePath
            ) throws Exception
    {
//        String pngFilePath = anyViewerReqService.convertPptToImages(file);
        String pngFilePath = anyViewerReqService.convertPptToImages(pptFilePath);
        return ResponseEntity.ok(pngFilePath);
    }

}
