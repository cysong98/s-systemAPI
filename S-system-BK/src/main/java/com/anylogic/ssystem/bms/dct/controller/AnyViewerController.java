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

import java.util.List;


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

    @PostMapping("/convertPptxToImages")
    public ResponseEntity convertPptxToImages
            (
                  @Valid String pptFilePath
            ) throws Exception
    {
        String pngFilePath = anyViewerReqService.convertPptxToImages(pptFilePath);
        return ResponseEntity.ok(pngFilePath);
    }

    @PostMapping("/convertPptToImages")
    public ResponseEntity convertPptToImages
            (
                  @Valid String pptFilePath
            ) throws Exception
    {
        String pngFilePath = anyViewerReqService.convertPptToImages(pptFilePath);
        return ResponseEntity.ok(pngFilePath);
    }

    @PostMapping("/convertImagesToPdf")
    public ResponseEntity convertImagesToPdf
            (
                    @RequestBody @Valid List<AttachFileVO> attachFiles
            ) throws Exception
    {
        String reseponse = anyViewerReqService.convertImagesToPdf(attachFiles);
        return ResponseEntity.ok(reseponse);
    }

    @PostMapping("/convertImagesToPdf2")
    public ResponseEntity convertImagesToPdf2() throws Exception
    {
        String reseponse = anyViewerReqService.convertImagesToPdf2();
        return ResponseEntity.ok(reseponse);
    }

    @PostMapping("/convertPPTXToPDF")
    public ResponseEntity convertPPTXToPDF
            (
                    @Valid String pptxPath,
                    @Valid String pdfPath,
                    @Valid Double scale,
                    @Valid int dpi
            ) throws Exception
    {
        String reseponse = anyViewerReqService.convertPPTXToPDF(pptxPath, pdfPath, scale, dpi);
        return ResponseEntity.ok(reseponse);
    }

    @PostMapping("/convertPPTToPDF")
    public ResponseEntity convertPPTToPDF
            (
                    @Valid String pptPath,
                    @Valid String pdfPath,
                    @Valid Double scale,
                    @Valid int dpi
            ) throws Exception
    {
        String reseponse = anyViewerReqService.convertPPTToPDF(pptPath, pdfPath, scale, dpi);
        return ResponseEntity.ok(reseponse);
    }

}
