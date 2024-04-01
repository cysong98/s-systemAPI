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
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.awt.geom.Dimension2D;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


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


/*======================================== for Convert ====================================================*/
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.List;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.awt.Dimension;
import java.awt.Graphics2D;
/*========================================================================================================*/


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
//        pptxFilePath = "//172.18.18.29/share/forConvertTest/01_input/pptx/testpptx.pptx";
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

    /**
     * 이미지 파일들을 PDF로 변환하고 합치는 메소드입니다.
     */
    public String convertImagesToPdf(List<AttachFileVO> attachFiles) {
        String outputPath = "//172.18.18.29/share/forConvertTest/02_output/images/convertedImages.pdf";
        try (PDDocument document = new PDDocument()) {
            for (AttachFileVO attachFile : attachFiles) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDImageXObject image = PDImageXObject.createFromFile(attachFile.getFlepath(), document);
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    float scale = Math.min(page.getMediaBox().getWidth() / image.getWidth(), page.getMediaBox().getHeight() / image.getHeight());
                    float imageWidth = image.getWidth() * scale;
                    float imageHeight = image.getHeight() * scale;
                    contentStream.drawImage(image, (page.getMediaBox().getWidth() - imageWidth) / 2, (page.getMediaBox().getHeight() - imageHeight) / 2, imageWidth, imageHeight);
                }
            }
            document.save(outputPath);
            System.out.println("PDF 파일 생성 완료: " + outputPath);
            return "PDF Conversion completed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("PDF 변환 중 오류가 발생하였습니다.");
            return "PDF로 변환하는 과정에서 오류가 발생했습니다.";
        }
    }

    /**
     * //테스트용 하드코딩 임시 메서드
     */
    public String convertImagesToPdf2() {
        List<String> imagePaths = Arrays.asList(
//                "//172.18.18.29/share/forConvertTest/02_output/images/pptslide-1.png",
//                "//172.18.18.29/share/forConvertTest/02_output/images/pptslide-2.png",
//                "//172.18.18.29/share/forConvertTest/02_output/images/pptslide-3.png"
                "//172.18.18.29/share/forConvertTest/01_input/png/D_C1_005_02  _ 비문관리카드 조회(기안자).png"
        );

        // 이후 PDF 변환 로직...
        try {
            String outputPath = "//172.18.18.29/share/forConvertTest/02_output/images/convertedImages.pdf";
            try (PDDocument document = new PDDocument()) {
                for (String imagePath : imagePaths) {
                    PDPage page = new PDPage(PDRectangle.A4);
                    document.addPage(page);

                    PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                        float scale = Math.min(page.getMediaBox().getWidth() / image.getWidth(), page.getMediaBox().getHeight() / image.getHeight());
                        float imageWidth = image.getWidth() * scale;
                        float imageHeight = image.getHeight() * scale;
                        contentStream.drawImage(image, (page.getMediaBox().getWidth() - imageWidth) / 2, (page.getMediaBox().getHeight() - imageHeight) / 2, imageWidth, imageHeight);
                    }
                }
                document.save(outputPath);
                System.out.println("PDF 파일 생성 완료: " + outputPath);
                return "PDF Conversion completed successfully.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("PDF 변환 중 오류가 발생하였습니다.");
            return "PDF로 변환하는 과정에서 오류가 발생했습니다.";
        }
    }


    public String convertPPTXToPDF(String pptxPath, String pdfPath, double scale, int dpi) {
//        pptxPath = "//172.18.18.29/share/forConvertTest/01_input/pptx/testpptx.pptx";
//        pptxPath = "//172.18.18.29/share/forConvertTest/01_input/pptx/231.화면 설계서_v1.0_계측기관리 - 복사본.pptx";
//        pdfPath = "//172.18.18.29/share/forConvertTest/02_output/images/pptx_converted202403.pdf";

//        scale = 1.0;
//        int dpi = 125; // 원하는 해상도로 설정 //못알아봄

//        scale = 3.0;
//        int dpi = 250; // 원하는 해상도로 설정 //너무 큼(안좋음)

//        scale = 1.0;
//        int dpi = 500; // 원하는 해상도로 설정 //3분30초 큼..((괜찮)

//        scale = 2.0;
//        dpi = 300; // 원하는 해상도로 설정 // 제일큼..(가독성 괜찮음)

        try (XMLSlideShow pptx = new XMLSlideShow(new FileInputStream(pptxPath));
             PDDocument doc = new PDDocument()) {

            for (XSLFSlide slide : pptx.getSlides()) {
                processSlide(pptx, slide, doc, scale, dpi);
            }

            doc.save(pdfPath);
            System.out.println("PDF 파일 생성 완료: " + pdfPath);
            return "PDF Conversion completed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "PDF로 변환하는 과정에서 오류가 발생했습니다.";
        }
    }

    private void processSlide(XMLSlideShow pptx, XSLFSlide slide, PDDocument doc, double scale, int dpi) {
        // XMLSlideShow 객체에서 페이지 크기를 얻음
        Dimension pageSize = pptx.getPageSize();

        double scaleFactor = dpi / 96.0; // 96 DPI는 기본 해상도
        int scaledWidth = (int) (pageSize.width * scaleFactor * scale);
        int scaledHeight = (int) (pageSize.height * scaleFactor * scale);

        BufferedImage img = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = img.createGraphics();
        graphics.scale(scaleFactor * scale, scaleFactor * scale);

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setPaint(Color.white);
        graphics.fill(new Rectangle2D.Float(0, 0, scaledWidth, scaledHeight));
        slide.draw(graphics);
        graphics.dispose(); // Graphics 객체 해제

        try {
            // 이미지를 ByteArrayOutputStream에 쓰기
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            // PDImageXObject 생성 및 PDF 문서에 이미지 추가
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, imageBytes, "slide-image");
            PDPage page = new PDPage(new PDRectangle(scaledWidth, scaledHeight));
            doc.addPage(page);

            //이미지를 실제로 페이지에 그리는 부분
            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                contentStream.drawImage(pdImage, 0, 0, scaledWidth, scaledHeight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String convertPPTToPDF(String pptPath, String pdfPath, double scale, int dpi) throws IOException {
//        pptPath = "//172.18.18.29/share/forConvertTest/01_input/ppt/test2.ppt";
//        pdfPath = "//172.18.18.29/share/forConvertTest/02_output/images/converted202403.pdf";
//        scale = 1.0;
//        dpi = 125; // 원하는 해상도로 설정

        try (HSLFSlideShow ppt = new HSLFSlideShow(new FileInputStream(pptPath));
             PDDocument doc = new PDDocument()) {

            Dimension pgsize = ppt.getPageSize();
            // DPI에 따른 스케일 조정
            double scaleFactor = dpi / 96.0;
            int scaledWidth = (int) (pgsize.width * scaleFactor * scale);
            int scaledHeight = (int) (pgsize.height * scaleFactor * scale);

            for (HSLFSlide slide : ppt.getSlides()) {
                BufferedImage img = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = img.createGraphics();

                // Apply scale
                graphics.scale(scaleFactor, scaleFactor);

                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, scaledWidth, scaledHeight));
                slide.draw(graphics);
                graphics.dispose();

                PDPage page = new PDPage(new PDRectangle(scaledWidth, scaledHeight));
                doc.addPage(page);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "png", baos);
                byte[] imageBytes = baos.toByteArray();

                PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, imageBytes, "slide-image");

                try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                    contentStream.drawImage(pdImage, 0, 0, scaledWidth, scaledHeight);
                }
            }

            doc.save(pdfPath);
            return "PDF Conversion completed successfully.";
        }catch (IOException e) {
            e.printStackTrace();
            return "PDF로 변환하는 과정에서 오류가 발생했습니다.";
        }
    }

    // pdf 낱장단위 분할 저장.
    public String splitPDF(String pdfPath, String outputFolder) {
        File inputFile = new File(pdfPath);
        File outputDir = new File(outputFolder);

        if (!outputDir.exists()) {
            boolean wasSuccessful = outputDir.mkdirs();
            if (!wasSuccessful) {
                System.err.println("출력 폴더를 생성할 수 없습니다.");
                return "출력 폴더를 생성할 수 없습니다.";
            }
        }

        try (PDDocument document = Loader.loadPDF(inputFile)) {
            Iterable<PDPage> pages = document.getPages();
            int pageNumber = 1;

            for (PDPage page : pages) {
                try (PDDocument newDoc = new PDDocument()) {
                    newDoc.addPage(page);
                    String baseName = inputFile.getName().replace(".pdf", "");
                    String newFileName = outputFolder + File.separator + baseName + "_" + pageNumber + ".pdf";
                    newDoc.save(newFileName);
                    System.out.println("저장된 페이지: " + newFileName);
                    pageNumber++;
                }
            }
            System.out.println("PDF 분할 완료");
            return "PDF 분할 완료";
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("PDF 파일 처리 중 오류 발생");
            return "PDF 파일 처리 중 오류 발생";
        }
    }

}
