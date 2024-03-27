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
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.awt.geom.Dimension2D;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


import java.io.File;


import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.io.StreamDocumentSource;



import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;

import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractMethod;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractor;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/*======================================== ppt to png ====================================================*/import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
/*========================================================================================================*/

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
        InputStream ishwpFilePath = new FileInputStream(hwpFilePath);
        try (PDDocument pdfDoc = new PDDocument()) {
            DocumentSource docSource = new StreamDocumentSource(ishwpFilePath, new URL("file:."), "utf-8");
            org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(docSource.getInputStream());
//            PDFRenderer pdfRenderer = new PDFRenderer(doc, docSource.getURL(), pdfDoc, new Dimension2D.Float(PDDocument.PAGE_SIZE_A4.getWidth(), PDDocument.PAGE_SIZE_A4.getHeight()));
//            PDFRenderer pdfRenderer = new PDFRenderer(doc, docSource.getURL(), pdfDoc, new Dimension2D.Float(PDDocument.PAGE_SIZE_A4.getWidth(), PDDocument.PAGE_SIZE_A4.getHeight()));
            // A4 크기를 포인트 단위로 계산
            float a4WidthInPoints = (210 / 25.4f) * 72;
            float a4HeightInPoints = (297 / 25.4f) * 72;
//            PDFRenderer pdfRenderer = new PDFRenderer(doc, docSource.getURL(), pdfDoc, new FloatDimension(PDDocument.PAGE_SIZE_A4.getWidth(), PDDocument.PAGE_SIZE_A4.getHeight()));
//            PDFRenderer pdfRenderer = new PDFRenderer(doc, docSource.getURL(), pdfDoc, new FloatDimension(a4WidthInPoints, a4HeightInPoints));


//            // Render and save
//            pdfRenderer.renderDocument();
//            pdfDoc.save(pdfFilePath);
            // Render each page and save
            for (int pageIndex = 0; pageIndex < pdfDoc.getNumberOfPages(); pageIndex++) {
//                BufferedImage image = pdfRenderer.renderImage(pageIndex);
                // Save the image or perform other operations
            }
            pdfDoc.save(pdfFilePath);
        }
        return pdfFilePath;
    }

    // txt -> pdf
    public String convertTextToPdf (AttachFileVO param) throws Exception {
        String textFilePath = param.getFlepath(); // 텍스트 파일 경로
        String pdfFilePath = "//172.18.18.29/share/fileUpload/2024/3_sabon/txt_path_to_output_pdf_file.pdf"; // 출력 PDF 파일 경로


        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDType0Font font = PDType0Font.load(document, new File("//172.18.18.29/share/anyFont/NanumSquareNeo-Variable.ttf"));

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page);
                 BufferedReader br = new BufferedReader(new FileReader(textFilePath))) {
                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(25, 725);

                String line;
                while ((line = br.readLine()) != null) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -15); // 다음 줄로 이동
                }
                contentStream.endText();
            }

            document.save(pdfFilePath);
            System.out.println("PDF created successfully at " + pdfFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfFilePath;
    }



    // pptx -> png
    public String convertPptxToImages(String pptxFilePath) throws IOException {
//        pptFilePath = "//172.18.18.29/share/forConvertTest/01_input/pptx/testpptx.pptx";
        String outputDirectory = "//172.18.18.29/share/forConvertTest/02_output/images";

        try (FileInputStream inputStream = new FileInputStream(pptxFilePath);
             XMLSlideShow pptx = new XMLSlideShow(inputStream)) {

            Dimension pgsize = pptx.getPageSize();
            List<XSLFSlide> slides = pptx.getSlides();

            for (int i = 0; i < slides.size(); i++) {
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                slides.get(i).draw(graphics);

                String outputFileName = outputDirectory + "/slide-" + (i + 1) + ".png";
                try (FileOutputStream out = new FileOutputStream(outputFileName)) {
                    ImageIO.write(img, "png", out);
                }
            }
            return "PPTX Conversion completed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to convert PPT to images due to an IO exception.";
        }
    }

    @Override
    public String convertPptToImages(String pptFilePath) throws Exception {
        String outputDirectory = "//172.18.18.29/share/forConvertTest/02_output/images";

        try (FileInputStream inputStream = new FileInputStream(pptFilePath);
             HSLFSlideShow ppt = new HSLFSlideShow(inputStream)) {

            Dimension pgsize = ppt.getPageSize();
            List<HSLFSlide> slides = ppt.getSlides();

            for (int i = 0; i < slides.size(); i++) {
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

                slides.get(i).draw(graphics);

                String outputFileName = outputDirectory + "/pptslide-" + (i + 1) + ".png";
                try (FileOutputStream out = new FileOutputStream(outputFileName)) {
                    ImageIO.write(img, "png", out);
                }
            }
            return "PPT Conversion completed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to convert PPT to images due to an IO exception.";
        }
    }


//    public class FloatDimension extends Dimension2D {
//        private double width;
//        private double height;
//
//        public FloatDimension(double width, double height) {
//            this.width = width;
//            this.height = height;
//        }
//
//        @Override
//        public double getWidth() {
//            return width;
//        }
//
//        @Override
//        public double getHeight() {
//            return height;
//        }
//
//        @Override
//        public void setSize(double width, double height) {
//            this.width = width;
//            this.height = height;
//        }
//    }
}
