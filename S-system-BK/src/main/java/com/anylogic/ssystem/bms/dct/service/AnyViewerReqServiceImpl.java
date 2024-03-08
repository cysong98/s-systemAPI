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

import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import javax.print.*;
//import com.anylogic.ssystem.bms.dct.mapper.HwpToPdfMapper;

///
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.io.StreamDocumentSource;


import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.print.PrinterJob;
import java.io.*;
import java.net.URL;

import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractMethod;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractor;

import java.io.*;

@Slf4j
@RequiredArgsConstructor
@Service("AnyViewerReqService")
@Transactional
public class AnyViewerReqServiceImpl implements AnyViewerReqService {

//    @Autowired
//    private HwpToPdfMapper hwpToPdfMapper;

    @Override
    public String convertHwpToPdf(AttachFileVO hwpFile) throws Exception {
        String hwpFilePath = hwpFile.getFlepath();
        String pdfFilePath = "//172.18.18.29/share/fileUpload/2024/3_sabon/path_to_output_pdf_file.pdf";

//        // HWP 파일을 로드
//        File fileObj = new File(hwpFilePath);
////        File hwpFile = hwpFilePath;
//
//        // PDF 문서를 생성
//        PDDocument document = PDDocument.load(fileObj);
//
//        // PDF 문서를 출력
////        PrinterJob job = PrinterJob.getPrinterJob();
////        job.setPageable(new PDFPageable(document));
//
//        // PDF 파일로 저장
////        job.print();
//        document.save(pdfFilePath);

        return pdfFilePath;
    }

    public void convert(String hwpFilePath, String htmlFilePath, String pdfFilePath) throws Exception {
        // 1. Convert HWP to HTML
//        convertHwpToHtml(hwpFilePath, htmlFilePath);

        // 2. Convert HTML to PDF
        // TODO: Implement HTML to PDF conversion
        // This may involve using a library like Apache PDFBox
    }

//    private void convertHwpToHtml(String hwpFilePath, String htmlFilePath) throws Exception {

    // hwp to html
    public String convertHwpToHtml(AttachFileVO param) throws Exception {
        String hwpFilePath = param.getFlepath();
        String htmlFilePath = "//172.18.18.29/share/fileUpload/2024/3_sabon/path_to_output_pdf_file.html";
        HWPFile hwpFile = HWPReader.fromFile(hwpFilePath);
        if (hwpFile != null) {
            String extractedText = TextExtractor.extract(hwpFile,TextExtractMethod.InsertControlTextBetweenParagraphText);
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(htmlFilePath)))) {
                out.println(extractedText);
            }
        }
        return htmlFilePath;
    }

    // html -> pdf
    public String convertHtmlToPdf(AttachFileVO param) throws Exception {
        String hwpFilePath = param.getFlepath();
        String pdfFilePath = "//172.18.18.29/share/fileUpload/2024/3_sabon/pdf_path_to_output_pdf_file.pdf";
//        try (PDDocument pdfDoc = new PDDocument()) {
//            DocumentSource docSource = new StreamDocumentSource(new File(hwpFilePath), new URL("file:."), "utf-8");
//            org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(docSource.getInputStream());
//            PDFRenderer pdfRenderer = new PDFRenderer(doc, docSource.getURL(), pdfDoc, new Dimension2D.Float(PDDocument.PAGE_SIZE_A4.getWidth(), PDDocument.PAGE_SIZE_A4.getHeight()));
//
//            // Render and save
//            pdfRenderer.renderDocument();
//            pdfDoc.save(pdfFilePath);
//        }
        return pdfFilePath;
    }
}
